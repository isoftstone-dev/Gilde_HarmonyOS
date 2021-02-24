package com.istone.myapplication;

import com.istone.myapplication.slice.ListAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

/**
 * description list
 * @author baihe
 * created 2021/2/8 14:50
 */
public class ListAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ListAbilitySlice.class.getName());
    }
}
