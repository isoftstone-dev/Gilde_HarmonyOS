package com.istone.myapplication.slice;

import com.istone.myapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Image;

/**
 * description resourcesslice
 * @author baihe
 * created 2021/2/8 14:52
 */
public class ResourcesChangeAbilitySlice extends AbilitySlice {
    /**
     * istrue
     */
    private boolean isTrue = true;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_resources_change);
        Image image = (Image) findComponentById(ResourceTable.Id_resources_change_image);
        Button button = (Button) findComponentById(ResourceTable.Id_resources_change_button);
        button.setClickedListener(component -> {
            image.setPixelMap(isTrue ? ResourceTable.Media_A : ResourceTable.Media_B);
            isTrue = !isTrue;
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
