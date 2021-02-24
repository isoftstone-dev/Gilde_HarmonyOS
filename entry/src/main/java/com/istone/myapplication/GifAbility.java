package com.istone.myapplication;

import com.istone.myapplication.slice.GifAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

/**
 * description gif
 * @author baihe
 * created 2021/2/8 14:51
 */
public class GifAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(GifAbilitySlice.class.getName());
    }
}
