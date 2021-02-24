package com.istone.myapplication.slice;

import com.istone.myapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;


/**
 * description mainslice
 *
 * @author baihe
 * created 2021/2/8 14:52
 */
public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        requestPermissionsFromUser(
                new String[]{"ohos.permission.READ_MEDIA", "ohos.permission.WRITE_MEDIA", "ohos.permission" +
                        ".READ_USER_STORAGE", "ohos.permission.WRITE_USER_STORAGE",}, 0);

        Button button1 = (Button) findComponentById(ResourceTable.Id_change_local_button);
        button1.setClickedListener(component -> {
            Intent intent2 = new Intent();
            present(new ResourcesChangeAbilitySlice(), new Intent());
        });

        Button button2 = (Button) findComponentById(ResourceTable.Id_show_server_button);
        button2.setClickedListener(component -> {

            present(new ServerAbilitySlice(), new Intent());
        });

        Button button3 = (Button) findComponentById(ResourceTable.Id_show_local_button);
        button3.setClickedListener(component -> {
            present(new LocalAbilitySlice(), new Intent());
        });

        Button button4 = (Button) findComponentById(ResourceTable.Id_show_gif_button);
        button4.setClickedListener(component -> {
            present(new GifAbilitySlice(), new Intent());
        });

        Button button5 = (Button) findComponentById(ResourceTable.Id_disk_cache_button);
        button5.setClickedListener(component -> {
            present(new ListAbilitySlice(), new Intent());
        });
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
