package com.istone.myapplication.slice;

import com.isotne.glidelibrary.utils.OhosGlide;
import com.istone.myapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Image;
import ohos.app.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;


/**
 * description localslice
 * @author baihe
 * created 2021/2/8 14:52
 */
public class LocalAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_local);
        Image image = (Image) findComponentById(ResourceTable.Id_local_image);
        File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath(), "test.png");
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            OhosGlide.with(this).load(inputStream).def(ResourceTable.Media_B).into(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
