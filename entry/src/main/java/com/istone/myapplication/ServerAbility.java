package com.istone.myapplication;

import com.istone.myapplication.slice.ServerAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

/**
 * description server
 * @author baihe
 * created 2021/2/8 14:51
 */
public class ServerAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ServerAbilitySlice.class.getName());
    }
}
