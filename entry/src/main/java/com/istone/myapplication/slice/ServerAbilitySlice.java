package com.istone.myapplication.slice;

import com.isotne.glidelibrary.utils.OhosGlide;
import com.istone.myapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Image;

import java.io.IOException;

/**
 * description serverslice
 * @author baihe
 * created 2021/2/8 14:52
 */
public class ServerAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_server);
        Image image = (Image) findComponentById(ResourceTable.Id_server_image);
        try {
            OhosGlide.with(this).load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fwww.wallcoo" +
                    ".com%2Fnature%2Fda_ps_landscape_01%2Fwallpapers%2F1280x1024%2F%5Bwallcoo_com%5D_1" +
                    ".jpg&refer=http%3A%2F%2Fwww.wallcoo.com&app=2002&size=f9999," +
                    "10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614218186&t=37efae92a69da2fe7685a2813ad36b50").def(ResourceTable.Media_A).into(image);
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
