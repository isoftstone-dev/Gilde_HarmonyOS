package com.istone.myapplication;

import com.istone.myapplication.slice.LocalAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

/**
*description local
*@author baihe
*created 2021/2/8 14:52
*
*/
public class LocalAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(LocalAbilitySlice.class.getName());
    }
}
