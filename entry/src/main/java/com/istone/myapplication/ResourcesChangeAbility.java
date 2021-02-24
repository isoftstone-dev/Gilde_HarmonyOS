package com.istone.myapplication;

import com.istone.myapplication.slice.ResourcesChangeAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

/**
 * description resources
 * @author baihe
 * created 2021/2/8 14:51
 */
public class ResourcesChangeAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ResourcesChangeAbilitySlice.class.getName());
    }
}
