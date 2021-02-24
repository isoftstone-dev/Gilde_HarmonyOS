package com.istone.myapplication;

import com.istone.myapplication.slice.BAbilitySlice;
import com.istone.myapplication.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

/**
 * description main
 * @author baihe
 * created 2021/2/8 14:50
 */
public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
        addActionRoute("action.goto.page", BAbilitySlice.class.getName());

    }
}
