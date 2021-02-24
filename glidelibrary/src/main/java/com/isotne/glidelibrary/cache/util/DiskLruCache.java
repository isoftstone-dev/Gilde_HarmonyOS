package com.isotne.glidelibrary.cache.util;


import com.isotne.glidelibrary.utils.LogUtils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.FilterOutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description DiskLruCache
 *
 * @author baihe
 * created 2021/2/9 8:41
 */
public final class DiskLruCache implements Closeable {
    /**
     * JOURNAL_FILE
     */
    static final String JOURNAL_FILE = "journal";
    /**
     * JOURNAL_FILE_TEMP
     */
    static final String JOURNAL_FILE_TEMP = "journal.tmp";
    /**
     * JOURNAL_FILE_BACKUP
     */
    static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    /**
     * MAGIC
     */
    static final String MAGIC = "libcore.io.DiskLruCache";
    /**
     * VERSION_1
     */
    static final String VERSION_1 = "1";
    /**
     * ANY_SEQUENCE_NUMBER
     */
    static final long ANY_SEQUENCE_NUMBER = -1;
    /**
     * ANY_SEQUENCE_NUMBER
     */
    static final String STRING_KEY_PATTERN = "[a-z0-9_-]{1,120}";
    /**
     * STRING_KEY_PATTERN
     */
    static final Pattern LEGAL_KEY_PATTERN = Pattern.compile(STRING_KEY_PATTERN);
    /**
     * CLEAN
     */
    private static final String CLEAN = "CLEAN";
    /**
     * DIRTY
     */
    private static final String DIRTY = "DIRTY";
    /**
     * REMOVE
     */
    private static final String REMOVE = "REMOVE";
    /**
     * READ
     */
    private static final String READ = "READ";
    /**
     * directory
     */
    private final File directory;
    /**
     * journalFile
     */
    private final File journalFile;
    /**
     * journalFileTmp
     */
    private final File journalFileTmp;
    /**
     * journalFileBackup
     */
    private final File journalFileBackup;
    /**
     * appVersion
     */
    private final int appVersion;
    /**
     * maxSize
     */
    private long maxSize;
    /**
     * valueCount
     */
    private final int valueCount;
    /**
     * valueCount
     */
    private long size = 0;
    /**
     * journalWriter
     */
    private Writer journalWriter;
    /**
     * lruEntries
     */
    private final LinkedHashMap<String, Entry> lruEntries =
            new LinkedHashMap<String, Entry>(0, 0.75f, true);
    /**
     * redundantOpCount
     */
    private int redundantOpCount; // redundantOpCount

    /**
     * To differentiate between old and current snapshots, each entry is given
     * a sequence number each time an edit is committed. A snapshot is stale if
     * its sequence number is not equal to its entry's sequence number.
     */
    private long nextSequenceNumber = 0;

    /**
     * This cache uses a single background thread to evict entries.
     */
    private final ThreadPoolExecutor executorService =
            new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    /**
     * cleanupCallable
     */
    private final Callable<Void> cleanupCallable = new Callable<Void>() {
        public Void call() throws Exception {
            synchronized (DiskLruCache.this) {
                if (journalWriter == null) {
                    return null; // Closed.
                }
                trimToSize();
                if (journalRebuildRequired()) {
                    rebuildJournal();
                    redundantOpCount = 0;
                }
            }
            return null;
        }
    };

    private DiskLruCache(File directory, int appVersion, int valueCount, long maxSize) {
        this.directory = directory;
        this.appVersion = appVersion;
        this.journalFile = new File(directory, JOURNAL_FILE);
        this.journalFileTmp = new File(directory, JOURNAL_FILE_TEMP);
        this.journalFileBackup = new File(directory, JOURNAL_FILE_BACKUP);
        this.valueCount = valueCount;
        this.maxSize = maxSize;
    }

    /**
     * @param directory  file path
     * @param appVersion appversion
     * @param valueCount valueCount
     * @param maxSize    maxSize
     * @return DiskLruCache
     * @throws IOException exception
     */
    public static DiskLruCache open(File directory, int appVersion, int valueCount, long maxSize)
            throws IOException {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        if (valueCount <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        }

        // If a bkp file exists, use it instead.
        File backupFile = new File(directory, JOURNAL_FILE_BACKUP);
        if (backupFile.exists()) {
            File journalFile = new File(directory, JOURNAL_FILE);
            // If journal file also exists just delete backup file.
            if (journalFile.exists()) {
                backupFile.delete();
            } else {
                renameTo(backupFile, journalFile, false);
            }
        }

        // Prefer to pick up where we left off.
        DiskLruCache cache = new DiskLruCache(directory, appVersion, valueCount, maxSize);
        if (cache.journalFile.exists()) {
            try {
                cache.readJournal();
                cache.processJournal();
                return cache;
            } catch (IOException journalIsCorrupt) {
                System.out
                        .println("DiskLruCache "
                                + directory
                                + " is corrupt: "
                                + journalIsCorrupt.getMessage()
                                + ", removing");
                cache.delete();
            }
        }

        // Create a new empty cache.
        directory.mkdirs();
        cache = new DiskLruCache(directory, appVersion, valueCount, maxSize);
        cache.rebuildJournal();
        return cache;
    }

    /**
     * @throws IOException exception
     */
    private void readJournal() throws IOException {
        InputStream inputStream = new FileInputStream(journalFile);
        StrictLineReader reader = new StrictLineReader(inputStream, Charset.forName("US-ASCII"));
        try {
            String magic = reader.readLine();
            String version = reader.readLine();
            String appVersionString = reader.readLine();
            String valueCountString = reader.readLine();
            String blank = reader.readLine();
            if (!MAGIC.equals(magic)
                    || !VERSION_1.equals(version)
                    || !Integer.toString(appVersion).equals(appVersionString)
                    || !Integer.toString(valueCount).equals(valueCountString)
                    || !"".equals(blank)) {
                throw new IOException("unexpected journal header: [" + magic + ", " + version + ", "
                        + valueCountString + ", " + blank + "]");

            }

            int lineCount = 0;
            while (true) {
                try {
                    readJournalLine(reader.readLine());
                    lineCount++;
                } catch (EOFException endOfJournal) {
                    break;
                }
            }
            redundantOpCount = lineCount - lruEntries.size();

            // If we ended on a truncated line, rebuild the journal before appending to it.
            if (reader.hasUnterminatedLine()) {
                rebuildJournal();
            } else {
                journalWriter = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(journalFile, true), Charset.forName("US-ASCII")));
            }
        } finally {
            Utils.closeQuietly(reader);
        }
    }

    /**
     * @param line line
     * @throws IOException IOException
     */
    private void readJournalLine(String line) throws IOException {
        int firstSpace = line.indexOf(' ');
        if (firstSpace == -1) {
            throw new IOException("unexpected journal line: " + line);
        }

        int keyBegin = firstSpace + 1;
        int secondSpace = line.indexOf(' ', keyBegin);
        final String key;
        if (secondSpace == -1) {
            key = line.substring(keyBegin);
            if (firstSpace == REMOVE.length() && line.startsWith(REMOVE)) {
                lruEntries.remove(key);
                return;
            }
        } else {
            key = line.substring(keyBegin, secondSpace);
        }

        Entry entry = lruEntries.get(key);
        if (entry == null) {
            entry = new Entry(key);
            lruEntries.put(key, entry);
        }

        if (secondSpace != -1 && firstSpace == CLEAN.length() && line.startsWith(CLEAN)) {
            String[] parts = line.substring(secondSpace + 1).split(" ");
            entry.readable = true;
            entry.currentEditor = null;
            entry.setLengths(parts);
        } else if (secondSpace == -1 && firstSpace == DIRTY.length() && line.startsWith(DIRTY)) {
            entry.currentEditor = new Editor(entry);
        } else if (secondSpace == -1 && firstSpace == READ.length() && line.startsWith(READ)) {
            // This work was already done by calling lruEntries.get().
        } else {
            throw new IOException("unexpected journal line: " + line);
        }
    }

    /**
     * @throws IOException IOException
     */
    private void processJournal() throws IOException {
        deleteIfExists(journalFileTmp);
        for (Iterator<Entry> i = lruEntries.values().iterator(); i.hasNext(); ) {
            Entry entry = i.next();
            if (entry.currentEditor == null) {
                for (int t = 0; t < valueCount; t++) {
                    size += entry.lengths[t];
                }
            } else {
                entry.currentEditor = null;
                for (int t = 0; t < valueCount; t++) {
                    deleteIfExists(entry.getCleanFile(t));
                    deleteIfExists(entry.getDirtyFile(t));
                }
                i.remove();
            }
        }
    }

    /**
     * @throws IOException IOException
     */
    private synchronized void rebuildJournal() throws IOException {
        if (journalWriter != null) {
            journalWriter.close();
        }

        Writer writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(journalFileTmp), Utils.US_ASCII));
        try {
            writer.write(MAGIC);
            writer.write("\n");
            writer.write(VERSION_1);
            writer.write("\n");
            writer.write(Integer.toString(appVersion));
            writer.write("\n");
            writer.write(Integer.toString(valueCount));
            writer.write("\n");
            writer.write("\n");

            for (Entry entry : lruEntries.values()) {
                if (entry.currentEditor != null) {
                    writer.write(DIRTY + ' ' + entry.key + '\n');
                } else {
                    writer.write(CLEAN + ' ' + entry.key + entry.getLengths() + '\n');
                }
            }
        } finally {
            writer.close();
        }

        if (journalFile.exists()) {
            renameTo(journalFile, journalFileBackup, true);
        }
        renameTo(journalFileTmp, journalFile, false);
        journalFileBackup.delete();

        journalWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(journalFile, true), Utils.US_ASCII));
    }

    /**
     * @param file file
     * @throws IOException IOException
     */
    private static void deleteIfExists(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }

    /**
     * @param from              from
     * @param to                to
     * @param deleteDestination deleteDestination
     * @throws IOException IOException
     */
    private static void renameTo(File from, File to, boolean deleteDestination) throws IOException {
        if (deleteDestination) {
            deleteIfExists(to);
        }
        if (!from.renameTo(to)) {
            throw new IOException();
        }
    }

    /**
     * @param key key
     * @return Snapshot
     * @throws IOException IOException
     */
    public synchronized Snapshot get(String key) throws IOException {
        checkNotClosed();
        validateKey(key);
        Entry entry = lruEntries.get(key);
        if (entry == null) {
            return null;
        }

        if (!entry.readable) {
            return null;
        }

        // Open all streams eagerly to guarantee that we see a single published
        // snapshot. If we opened streams lazily then the streams could come
        // from different edits.
        InputStream[] ins = new InputStream[valueCount];
        try {
            for (int i = 0; i < valueCount; i++) {
                ins[i] = new FileInputStream(entry.getCleanFile(i));
            }
        } catch (FileNotFoundException e) {
            LogUtils.log(LogUtils.ERROR, "AAA", "e=" + e.getMessage());
            // A file must have been deleted manually!
            for (int i = 0; i < valueCount; i++) {
                if (ins[i] != null) {
                    Utils.closeQuietly(ins[i]);
                } else {
                    break;
                }
            }
            return null;
        }

        redundantOpCount++;
        journalWriter.append(READ + ' ' + key + '\n');
        if (journalRebuildRequired()) {
            executorService.submit(cleanupCallable);
        }

        return new Snapshot(key, entry.sequenceNumber, ins, entry.lengths);
    }

    /**
     * @param key key
     * @return Editor
     * @throws IOException IOException
     */
    public Editor edit(String key) throws IOException {
        return edit(key, ANY_SEQUENCE_NUMBER);
    }

    /**
     * @param key                    key
     * @param expectedSequenceNumber expectedSequenceNumber
     * @return Editor
     * @throws IOException IOException
     */
    private synchronized Editor edit(String key, long expectedSequenceNumber) throws IOException {
        checkNotClosed();
        validateKey(key);
        Entry entry = lruEntries.get(key);
        if (expectedSequenceNumber != ANY_SEQUENCE_NUMBER && (entry == null
                || entry.sequenceNumber != expectedSequenceNumber)) {
            return null; // Snapshot is stale.
        }
        if (entry == null) {
            entry = new Entry(key);
            lruEntries.put(key, entry);
        } else if (entry.currentEditor != null) {
            return null; // Another edit is in progress.
        }

        Editor editor = new Editor(entry);
        entry.currentEditor = editor;

        // Flush the journal before creating files to prevent file leaks.
        journalWriter.write(DIRTY + ' ' + key + '\n');
        journalWriter.flush();
        return editor;
    }

    /**
     * @return File
     */
    public File getDirectory() {
        return directory;
    }

    /**
     * @return size
     */
    public synchronized long getMaxSize() {
        return maxSize;
    }

    /**
     * @param maxSize maxSize
     */
    public synchronized void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
        executorService.submit(cleanupCallable);
    }

    /**
     * @return size
     */
    public synchronized long size() {
        return size;
    }

    /**
     * @param editor  editor
     * @param success success
     * @throws IOException IOException
     */
    private synchronized void completeEdit(Editor editor, boolean success) throws IOException {
        Entry entry = editor.entry;
        if (entry.currentEditor != editor) {
            throw new IllegalStateException();
        }

        // If this edit is creating the entry for the first time, every index must have a value.
        if (success && !entry.readable) {
            for (int i = 0; i < valueCount; i++) {
                if (!editor.written[i]) {
                    editor.abort();
                    throw new IllegalStateException("Newly created entry didn't create value for index " + i);
                }
                if (!entry.getDirtyFile(i).exists()) {
                    editor.abort();
                    return;
                }
            }
        }

        for (int i = 0; i < valueCount; i++) {
            File dirty = entry.getDirtyFile(i);
            if (success) {
                if (dirty.exists()) {
                    File clean = entry.getCleanFile(i);
                    dirty.renameTo(clean);
                    long oldLength = entry.lengths[i];
                    long newLength = clean.length();
                    entry.lengths[i] = newLength;
                    size = size - oldLength + newLength;
                }
            } else {
                deleteIfExists(dirty);
            }
        }

        redundantOpCount++;
        entry.currentEditor = null;
        if (entry.readable | success) {
            entry.readable = true;
            journalWriter.write(CLEAN + ' ' + entry.key + entry.getLengths() + '\n');
            if (success) {
                entry.sequenceNumber = nextSequenceNumber++;
            }
        } else {
            lruEntries.remove(entry.key);
            journalWriter.write(REMOVE + ' ' + entry.key + '\n');
        }
        journalWriter.flush();

        if (size > maxSize || journalRebuildRequired()) {
            executorService.submit(cleanupCallable);
        }
    }

    /**
     *
     * @return boolean
     */
    private boolean journalRebuildRequired() {
        final int redundantOpCompactThreshold = 2000;
        return redundantOpCount >= redundantOpCompactThreshold //
                && redundantOpCount >= lruEntries.size();
    }

    /**
     * @param key key
     * @return true or false
     * @throws IOException IOException
     */
    public synchronized boolean remove(String key) throws IOException {
        checkNotClosed();
        validateKey(key);
        Entry entry = lruEntries.get(key);
        if (entry == null || entry.currentEditor != null) {
            return false;
        }

        for (int i = 0; i < valueCount; i++) {
            File file = entry.getCleanFile(i);
            if (file.exists() && !file.delete()) {
                throw new IOException("failed to delete " + file);
            }
            size -= entry.lengths[i];
            entry.lengths[i] = 0;
        }

        redundantOpCount++;
        journalWriter.append(REMOVE + ' ' + key + '\n');
        lruEntries.remove(key);

        if (journalRebuildRequired()) {
            executorService.submit(cleanupCallable);
        }

        return true;
    }

    /**
     * @return true or false
     */
    public synchronized boolean isClosed() {
        return journalWriter == null;
    }

    /**
     * checkNotClosed
     */
    private void checkNotClosed() {
        if (journalWriter == null) {
            throw new IllegalStateException("cache is closed");
        }
    }

    /**
     * @throws IOException IOException
     */
    public synchronized void flush() throws IOException {
        checkNotClosed();
        trimToSize();
        journalWriter.flush();
    }

    /**
     * @throws IOException IOException
     */
    public synchronized void close() throws IOException {
        if (journalWriter == null) {
            return; // Already closed.
        }
        for (Entry entry : new ArrayList<Entry>(lruEntries.values())) {
            if (entry.currentEditor != null) {
                entry.currentEditor.abort();
            }
        }
        trimToSize();
        journalWriter.close();
        journalWriter = null;
    }

    /**
     * @throws IOException IOException
     */
    private void trimToSize() throws IOException {
        while (size > maxSize) {
            Map.Entry<String, Entry> toEvict = lruEntries.entrySet().iterator().next();
            remove(toEvict.getKey());
        }
    }

    /**
     * @throws IOException IOException
     */
    public void delete() throws IOException {
        close();
        Utils.deleteContents(directory);
    }

    /**
     * @param key key
     */
    private void validateKey(String key) {
        Matcher matcher = LEGAL_KEY_PATTERN.matcher(key);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("keys must match regex "
                    + STRING_KEY_PATTERN + ": \"" + key + "\"");
        }
    }

    /**
     * @param in in
     * @return String
     * @throws IOException IOException
     */
    private static String inputStreamToString(InputStream in) throws IOException {
        return Utils.readFully(new InputStreamReader(in, Utils.UTF_8));
    }

    /**
     * description Snapshot
     *
     * @author baihe
     * created 2021/2/9 9:51
     */
    public final class Snapshot implements Closeable {
        /**
         * key
         */
        private final String key;
        /**
         * sequenceNumber
         */
        private final long sequenceNumber;
        /**
         * ins
         */
        private final InputStream[] ins;
        /**
         * lengths
         */
        private final long[] lengths;

        private Snapshot(String key, long sequenceNumber, InputStream[] ins, long[] lengths) {
            this.key = key;
            this.sequenceNumber = sequenceNumber;
            this.ins = ins;
            this.lengths = lengths;
        }

        /**
         * @return Editor
         * @throws IOException IOException
         */
        public Editor edit() throws IOException {
            return DiskLruCache.this.edit(key, sequenceNumber);
        }

        /**
         *
         * @param index index
         * @return InputStream
         */
        public InputStream getInputStream(int index) {
            return ins[index];
        }

        /**
         * @param index index
         * @return String
         * @throws IOException IOException
         */
        public String getString(int index) throws IOException {
            return inputStreamToString(getInputStream(index));
        }

        /**
         *
         * @param index index
         * @return length
         */
        public long getLength(int index) {
            return lengths[index];
        }

        /**
         * close
         */
        public void close() {
            for (InputStream in : ins) {
                Utils.closeQuietly(in);
            }
        }
    }

    /**
     * OutputStream
     */
    private static final OutputStream NULL_OUTPUT_STREAM = new OutputStream() {
        @Override
        public void write(int b) throws IOException {
            // Eat all writes silently. Nom nom.
        }
    };

    /**
     * Edits the values for an entry.
     */
    public final class Editor {
        /**
         * entry
         */
        private final Entry entry;
        /**
         * written
         */
        private final boolean[] written;
        /**
         * hasErrors
         */
        private boolean hasErrors;
        /**
         * committed
         */
        private boolean committed;

        private Editor(Entry entry) {
            this.entry = entry;
            this.written = (entry.readable) ? null : new boolean[valueCount];
        }

        /**
         * @param index index
         * @return InputStream
         * @throws IOException IOException
         */
        public InputStream newInputStream(int index) throws IOException {
            synchronized (DiskLruCache.this) {
                if (entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                if (!entry.readable) {
                    return null;
                }
                try {
                    return new FileInputStream(entry.getCleanFile(index));
                } catch (FileNotFoundException e) {
                    return null;
                }
            }
        }

        /**
         * @param index index
         * @return String
         * @throws IOException IOException
         */
        public String getString(int index) throws IOException {
            InputStream in = newInputStream(index);
            return in != null ? inputStreamToString(in) : null;
        }

        /**
         * @param index index
         * @return OutputStream
         * @throws IOException IOException
         */
        public OutputStream newOutputStream(int index) throws IOException {
            if (index < 0 || index >= valueCount) {
                throw new IllegalArgumentException("Expected index " + index + " to "
                        + "be greater than 0 and less than the maximum value count "
                        + "of " + valueCount);
            }
            synchronized (DiskLruCache.this) {
                if (entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                if (!entry.readable) {
                    written[index] = true;
                }
                File dirtyFile = entry.getDirtyFile(index);
                FileOutputStream outputStream;
                try {
                    outputStream = new FileOutputStream(dirtyFile);
                } catch (FileNotFoundException e) {
                    // Attempt to recreate the cache directory.
                    directory.mkdirs();
                    try {
                        outputStream = new FileOutputStream(dirtyFile);
                    } catch (FileNotFoundException e2) {
                        // We are unable to recover. Silently eat the writes.
                        return NULL_OUTPUT_STREAM;
                    }
                }
                return new FaultHidingOutputStream(outputStream);
            }
        }

        /**
         * @param index index
         * @param value value
         * @throws IOException IOException
         */
        public void set(int index, String value) throws IOException {
            Writer writer = null;
            try {
                writer = new OutputStreamWriter(newOutputStream(index), Utils.UTF_8);
                writer.write(value);
            } finally {
                Utils.closeQuietly(writer);
            }
        }

        /**
         * @throws IOException IOException
         */
        public void commit() throws IOException {
            if (hasErrors) {
                completeEdit(this, false);
                remove(entry.key); // The previous entry is stale.
            } else {
                completeEdit(this, true);
            }
            committed = true;
        }

        /**
         * @throws IOException IOException
         */
        public void abort() throws IOException {
            completeEdit(this, false);
        }

        /**
         * abortUnlessCommitted
         */
        public void abortUnlessCommitted() {
            if (!committed) {
                try {
                    abort();
                } catch (IOException ignored) {
                }
            }
        }


        private final class FaultHidingOutputStream extends FilterOutputStream {
            private FaultHidingOutputStream(OutputStream out) {
                super(out);
            }

            @Override
            public void write(int oneByte) {
                try {
                    out.write(oneByte);
                } catch (IOException e) {
                    hasErrors = true;
                }
            }

            @Override
            public void write(byte[] buffer, int offset, int length) {
                try {
                    out.write(buffer, offset, length);
                } catch (IOException e) {
                    hasErrors = true;
                }
            }

            @Override
            public void close() {
                try {
                    out.close();
                } catch (IOException e) {
                    hasErrors = true;
                }
            }

            @Override
            public void flush() {
                try {
                    out.flush();
                } catch (IOException e) {
                    hasErrors = true;
                }
            }
        }
    }

    private final class Entry {
        /**
         * key
         */
        private final String key;

        /**
         * Lengths of this entry's files.
         */
        private final long[] lengths;

        /**
         * True if this entry has ever been published.
         */
        private boolean readable;

        /**
         * The ongoing edit or null if this entry is not being edited.
         */
        private Editor currentEditor;

        /**
         * The sequence number of the most recently committed edit to this entry.
         */
        private long sequenceNumber;

        private Entry(String key) {
            this.key = key;
            this.lengths = new long[valueCount];
        }

        /**
         * @return String
         * @throws IOException IOException
         */
        public String getLengths() throws IOException {
            StringBuilder result = new StringBuilder();
            for (long size : lengths) {
                result.append(' ').append(size);
            }
            return result.toString();
        }

        /**
         * @param strings strings
         * @throws IOException IOException
         */
        private void setLengths(String[] strings) throws IOException {
            if (strings.length != valueCount) {
                throw invalidLengths(strings);
            }

            try {
                for (int i = 0; i < strings.length; i++) {
                    lengths[i] = Long.parseLong(strings[i]);
                }
            } catch (NumberFormatException e) {
                throw invalidLengths(strings);
            }
        }

        /**
         * @param strings strings
         * @return IOException
         * @throws IOException IOException
         */
        private IOException invalidLengths(String[] strings) throws IOException {
            throw new IOException("unexpected journal line: " + java.util.Arrays.toString(strings));
        }

        /**
         * @param i i
         * @return File
         */
        public File getCleanFile(int i) {
            return new File(directory, key + "." + i);
        }

        /**
         * @param i i
         * @return File
         */
        public File getDirtyFile(int i) {
            return new File(directory, key + "." + i + ".tmp");
        }
    }
}
