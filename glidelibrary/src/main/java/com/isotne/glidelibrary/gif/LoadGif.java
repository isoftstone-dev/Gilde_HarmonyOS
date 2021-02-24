package com.isotne.glidelibrary.gif;

import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.Image;
import ohos.media.image.PixelMap;

import java.util.ArrayList;
import java.util.List;

/**
 * description 展示动画
 *
 * @author baihe
 * created 2021/2/8 14:34
 */
public class LoadGif {
    /**
     * pixelmap list
     */
    private static List<PixelMap> PIXEL_MAP_LIST = new ArrayList<>();
    /**
     * image
     */
    private static Image IMAGE;
    /**
     * animatorvalue
     */
    private static AnimatorValue ANIMATOR_VALUE;

    /**
     * @param abilitySlice abilitySlice
     * @param pixelMapList pixelMapList
     * @param image        image
     * @param i            i
     */
    public static void loadGif(AbilitySlice abilitySlice, List<PixelMap> pixelMapList, Image image, int i) {
        IMAGE = image;
        PIXEL_MAP_LIST = pixelMapList;
        ANIMATOR_VALUE = new AnimatorValue();
        ANIMATOR_VALUE.setCurveType(Animator.CurveType.LINEAR);
        ANIMATOR_VALUE.setDelay(100);
        ANIMATOR_VALUE.setLoopedCount(Animator.INFINITE);//无限次循环
        ANIMATOR_VALUE.setDuration(i / 3 * 100);
        ANIMATOR_VALUE.setValueUpdateListener(M_ANIMATOR_UPDATE_LISTENER);
        ANIMATOR_VALUE.start();

    }

    /**
     * M_ANIMATOR_UPDATE_LISTENER
     */
    private static final AnimatorValue.ValueUpdateListener M_ANIMATOR_UPDATE_LISTENER
            = new AnimatorValue.ValueUpdateListener() {
                @Override
                public void onUpdate(AnimatorValue animatorValue, float v) {
                    IMAGE.setPixelMap(PIXEL_MAP_LIST.get((int) (v * PIXEL_MAP_LIST.size())));
                    IMAGE.invalidate();
                }
            };

}
