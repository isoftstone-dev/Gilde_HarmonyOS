package com.istone.myapplication.slice;

import com.isotne.glidelibrary.utils.OhosGlide;
import com.istone.myapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Image;

import java.io.IOException;

/**
 * description gifslice
 * @author baihe
 * created 2021/2/8 14:53
 */
public class GifAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_gif);
        Image image = (Image) findComponentById(ResourceTable.Id_gif_image);
        try {
            OhosGlide.with(this).load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fb-ssl.duitang" +
                    ".com%2Fuploads%2Fitem%2F201611%2F04%2F20161104110413_XzVAk.thumb.700_0" +
                    ".gif&refer=http%3A%2F%2Fb-ssl.duitang.com&app=2002&size=f9999," +
                    "10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614137486&t=ad88afe8f35232022db6009e8a165889").def(ResourceTable.Media_A).into(image);
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
