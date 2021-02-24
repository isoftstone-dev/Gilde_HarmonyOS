package com.istone.myapplication.slice;

import com.istone.myapplication.ResourceTable;
import com.istone.myapplication.adapter.TopicItemProvider;
import com.istone.myapplication.domain.TopicDomain;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.ListContainer;
import java.util.ArrayList;
import java.util.List;

/**
 * description listslice
 *
 * @author baihe
 * created 2021/2/8 14:53
 */
public class ListAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_list);
        initListContainer();
    }

    /**
     * initListContainer
     */
    private void initListContainer() {
        ListContainer listContainer = (ListContainer) findComponentById(ResourceTable.Id_list_container);
        List<TopicDomain> list = getData();
        TopicItemProvider sampleItemProvider = new TopicItemProvider(list, this);
        listContainer.setItemProvider(sampleItemProvider);
    }

    /**
     *
     * @return ArrayList
     */
    private ArrayList<TopicDomain> getData() {
        ArrayList<TopicDomain> list = new ArrayList<>();

        list.add(new TopicDomain("hello,1", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa2.att.hudong" +
                ".com%2F86%2F10%2F01300000184180121920108394217.jpg&refer=http%3A%2F%2Fa2.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=8bbec0d30e98bdd5d5fdea87628554da"));
        list.add(new TopicDomain("hello,2", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa3.att.hudong" +
                ".com%2F65%2F38%2F16300534049378135355388981738.jpg&refer=http%3A%2F%2Fa3.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=d27e0975ecff17c5ce14fa8494fb0733"));
        list.add(new TopicDomain("hello,3", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa3.att.hudong" +
                ".com%2F35%2F34%2F19300001295750130986345801104.jpg&refer=http%3A%2F%2Fa3.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=555f2ca5b1b68279c47be3efab31efdc"));
        list.add(new TopicDomain("hello,4", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi2.w.yun.hjfile" +
                ".cn%2Fdoc%2F201303%2Fd5547c74-d9ad-4625-bd93-41c2817f1dff_03.jpg&refer=http%3A%2F%2Fi2.w.yun.hjfile" +
                ".cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=b3454729f39611ac36cde849c02c2a7a"));
        list.add(new TopicDomain("hello,5", "https://ss0.baidu.com/7Po3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item" +
                "/9c16fdfaaf51f3de9ba8ee1194eef01f3a2979a8.jpg"));
        list.add(new TopicDomain("hello,6", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa1.att.hudong" +
                ".com%2F81%2F71%2F01300000164151121808718718556.jpg&refer=http%3A%2F%2Fa1.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=de1cc282c8911a5c5fc2ffb9523bca16"));
        list.add(new TopicDomain("hello,7", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa3.att.hudong" +
                ".com%2F61%2F98%2F01300000248068123885985729957.jpg&refer=http%3A%2F%2Fa3.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=50a9dc20e962e5b8464c6cc3525e6e20"));
        list.add(new TopicDomain("hello,8", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.aiimg" +
                ".com%2Fuploads%2Fuserup%2F0909%2F2Z64022L38.jpg&refer=http%3A%2F%2Fimg.aiimg" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=e56a3b4c167d619173aaa77a1b33cf48"));
        list.add(new TopicDomain("hello,9", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa4.att.hudong" +
                ".com%2F63%2F70%2F06300000046969120433706514748.jpg&refer=http%3A%2F%2Fa4.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=4bdcf09101945fd4f78fdf943dc886dc"));
        list.add(new TopicDomain("hello,10", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa3.att.hudong" +
                ".com%2F02%2F38%2F01300000237560123245382609951.jpg&refer=http%3A%2F%2Fa3.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=13dec1ea4bae058c49ec0f28281445f9"));
        list.add(new TopicDomain("hello,11", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa2.att.hudong" +
                ".com%2F42%2F31%2F01300001320894132989315766618.jpg&refer=http%3A%2F%2Fa2.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=96a6896fae35450377ec81ea19a09a1f"));
        list.add(new TopicDomain("hello,12", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic25.nipic" +
                ".com%2F20121107%2F8847866_164210379199_2.jpg&refer=http%3A%2F%2Fpic25.nipic.com&app=2002&size=f9999," +
                "10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t=b535ad3cac3cfaf5ebcab0cb4c987dd7"));
        list.add(new TopicDomain("hello,13", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic16.nipic" +
                ".com%2F20110928%2F5200151_002314030000_2.jpg&refer=http%3A%2F%2Fpic16.nipic.com&app=2002&size=f9999," +
                "10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t=e37c1cca646f0ccc773ade0fbe007b29"));
        list.add(new TopicDomain("hello,14", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa4.att.hudong" +
                ".com%2F43%2F83%2F01300000241358124822839175242.jpg&refer=http%3A%2F%2Fa4.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=18a0ca4b0c1efd6a1bd5cb9760128060"));
        list.add(new TopicDomain("hello,15", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa0.att.hudong" +
                ".com%2F65%2F07%2F01300000204202121839075492554.jpg&refer=http%3A%2F%2Fa0.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=cd1f8cc3f7114cd2a5453a6d34cb83e5"));
        list.add(new TopicDomain("hello,16", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa2.att.hudong" +
                ".com%2F14%2F68%2F19300001338674131496682910142.jpg&refer=http%3A%2F%2Fa2.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=7d2fffd5d8565abd0ec6653681047631"));
        list.add(new TopicDomain("hello,17", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa2.att.hudong" +
                ".com%2F06%2F02%2F19300534106437134465026151672.jpg&refer=http%3A%2F%2Fa2.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=c44578bae5bc4cdb90453804ad07f25f"));
        list.add(new TopicDomain("hello,18", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa2.att.hudong" +
                ".com%2F50%2F71%2F01300000240273131339713219664.jpg&refer=http%3A%2F%2Fa2.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=5a3e158a0210c9eb5a47d0901b46bede"));
        list.add(new TopicDomain("hello,19", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa3.att.hudong" +
                ".com%2F45%2F36%2F14300000491308128107360639165.jpg&refer=http%3A%2F%2Fa3.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=628a23b650920999e2f4c5987d1362a9"));
        list.add(new TopicDomain("hello,20", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa3.att.hudong" +
                ".com%2F13%2F41%2F01300000201800122190411861466.jpg&refer=http%3A%2F%2Fa3.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=b90979078e62ebf9b549d19e726e18f0"));
        list.add(new TopicDomain("hello,21", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa4.att.hudong" +
                ".com%2F25%2F10%2F01300473586198134027108433788.jpg&refer=http%3A%2F%2Fa4.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=42ba823236600a0f3b38fa049eb1048f"));
        list.add(new TopicDomain("hello,22", "https://ss3.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item" +
                "/42166d224f4a20a4b44741a690529822720ed072.jpg"));
        list.add(new TopicDomain("hello,23", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fbbs.jooyoo" +
                ".net%2Fattachment%2FMon_0910%2F24_293205_2d4adfacccd3031.jpg&refer=http%3A%2F%2Fbbs.jooyoo" +
                ".net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=a9a3fd7321f526fce2efbc3f9ca12691"));
        list.add(new TopicDomain("hello,24", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa1.att.hudong" +
                ".com%2F57%2F92%2F01300542193590138063924441627.jpg&refer=http%3A%2F%2Fa1.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=e70284b49966a7ed68dd66dc61f2265b"));
        list.add(new TopicDomain("hello,25", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa4.att.hudong" +
                ".com%2F20%2F39%2F01300542519189139990390839214.jpg&refer=http%3A%2F%2Fa4.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=8872f9316d250b09c62bdaac4b0f39fd"));
        list.add(new TopicDomain("hello,26", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa3.att.hudong" +
                ".com%2F08%2F22%2F01300000013945118822221353308.jpg&refer=http%3A%2F%2Fa3.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=c342b96383bf760f3850c6aab89ae44c"));
        list.add(new TopicDomain("hello,27", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa2.att.hudong" +
                ".com%2F12%2F87%2F01300001149956130041875096065.jpg&refer=http%3A%2F%2Fa2.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=1c2d14c12c3fdbb784cfc4faedcbd21b"));
        list.add(new TopicDomain("hello,28", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa3.att.hudong" +
                ".com%2F92%2F04%2F01000000000000119090475560392.jpg&refer=http%3A%2F%2Fa3.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=de7a697e58b7d9973f53641042b24633"));
        list.add(new TopicDomain("hello,29", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa2.att.hudong" +
                ".com%2F11%2F48%2F01300000195282124296481807051.jpg&refer=http%3A%2F%2Fa2.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819582&t" +
                "=7cfffaaed1cb61482d00de24ac3caa41"));
        list.add(new TopicDomain("hello,30", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa3.att.hudong" +
                ".com%2F03%2F88%2F01300000400534127693880874175.jpg&refer=http%3A%2F%2Fa3.att.hudong" +
                ".com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614819998&t" +
                "=697fb067f33484da214a584bf9d4cf48"));

        return list;
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
