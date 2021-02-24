package com.istone.myapplication.adapter;

import com.isotne.glidelibrary.utils.LogUtils;
import com.isotne.glidelibrary.utils.OhosGlide;
import com.istone.myapplication.ResourceTable;
import com.istone.myapplication.domain.TopicDomain;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.RecycleItemProvider;
import ohos.agp.components.Text;
import ohos.agp.components.Image;
import java.io.IOException;
import java.util.List;

/**
 * description topicitem provider
 *
 * @author baihe
 * created 2021/2/8 14:53
 */
public class TopicItemProvider extends RecycleItemProvider {
    /**
     * list
     */
    private List<TopicDomain> list;
    /**
     * slice
     */
    private AbilitySlice slice;

    public TopicItemProvider(List<TopicDomain> list, AbilitySlice slice) {
//        subcribeNetwork();
        this.list = list;
        this.slice = slice;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list != null && position > 0 && position < list.size()) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component convertComponent, ComponentContainer componentContainer) {
        Component cpt;
        ViewHolder viewHolder;
        LogUtils.log(LogUtils.ERROR,"abc=","===========33333");
        if (convertComponent == null) {
            cpt = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_list_item_layout, null, false);
            viewHolder = new ViewHolder();
            viewHolder.topic = (Text) cpt.findComponentById(ResourceTable.Id_topic);
            viewHolder.image = (Image) cpt.findComponentById(ResourceTable.Id_imageView);
            cpt.setTag(viewHolder);
        } else {
            cpt = convertComponent;
            viewHolder = (ViewHolder) cpt.getTag();
        }

        TopicDomain topic = list.get(position);
        viewHolder.topic.setText(topic.getTopic());
        try {
            OhosGlide.with(slice).load(topic.getUrl()).def(ResourceTable.Media_A).hasDiskCache(true).into(viewHolder.image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cpt;
    }

//    private void subcribeNetwork(){
//        LogUtils.log(LogUtils.ERROR,"abc=","========22222222");
//        MatchingSkills matchingSkills = new MatchingSkills();
//        matchingSkills.addEvent(CommonEventSupport.COMMON_EVENT_WIFI_CONN_STATE); // 网络链接状态监听事件
//        matchingSkills.addEvent(CommonEventSupport.COMMON_EVENT_WIFI_P2P_STATE_CHANGED);
//        CommonEventSubscribeInfo subscribeInfo = new CommonEventSubscribeInfo(matchingSkills);
//        WifiEventSubscriber subscriber = new WifiEventSubscriber(subscribeInfo);
//        try {
//            CommonEventManager.subscribeCommonEvent(subscriber);
//            publish();
//        } catch (RemoteException e) {
//        }
//    }

//    private void publish(){
//        try {
//            Intent intent = new Intent();
//            Operation operation = new Intent.OperationBuilder()
//                    .build();
//            intent.setOperation(operation);
//            CommonEventData eventData = new CommonEventData(intent);
//            CommonEventManager.publishCommonEvent(eventData);
//        } catch (RemoteException e) {
//        }
//    }

    /**
     * viewholder
     */
    class ViewHolder {
        /**
         * topic
         */
        private Text topic;
        /**
         * image
         */
        private Image image;

        /**
         * @return topic
         */
        public Text getTopic() {
            return topic;
        }

        /**
         * @param topic topic text
         */
        public void setTopic(Text topic) {
            this.topic = topic;
        }

        /**
         * @return iamge
         */
        public Image getImage() {
            return image;
        }

        /**
         * @param image image
         */
        public void setImage(Image image) {
            this.image = image;
        }
    }

//    class WifiEventSubscriber extends CommonEventSubscriber {
//        EventRunner runner = EventRunner.create(true);
//
//        public WifiEventSubscriber(CommonEventSubscribeInfo subscribeInfo) {
//            super(subscribeInfo);
//        }
//
//        @Override
//        public void onReceiveEvent(CommonEventData commonEventData) {
//            LogUtils.log(LogUtils.ERROR, "abc=", "======================");
//            // 待执行的操作，由开发者定义
//            MyEventHandler myHandler = new MyEventHandler(runner);
//            InnerEvent innerEvent = InnerEvent.get();
//            myHandler.sendEvent(innerEvent, 0, EventHandler.Priority.IMMEDIATE);
//
////            NetManager netManager = NetManager.getInstance(null);
////
////            if (!netManager.hasDefaultNet()) {
////                return;
////            } else {
//////            EventRunner runner = EventRunner.create(false);
//////            MyEventHandler myHandler = new MyEventHandler(runner);
//////            InnerEvent innerEvent = InnerEvent.get();
//////            myHandler.sendEvent(innerEvent);
////            }
//
////            if (WifiEvents.EVENT_ACTIVE_STATE.equals(commonEventData.getIntent().getAction())) {
////                // 获取附带参数
////                IntentParams params = commonEventData.getIntent().getParams();
////                if (params == null) {
////                    return;
////                }
////                int wifiState = (int) params.getParam(WifiEvents.PARAM_ACTIVE_STATE);
////
////                if (wifiState == WifiEvents.STATE_ACTIVE) { // 处理WLAN被打开消息
////                } else if (wifiState == WifiEvents.STATE_INACTIVE) { // 处理WLAN被关闭消息
////                } else { // 处理WLAN异常状态
////                }
////            }
//        }
//    }
//
//    public  class MyEventHandler extends EventHandler {
//        public MyEventHandler(EventRunner runner) {
//            super(runner);
//        }
//        // 重写实现processEvent方法
//        @Override
//        public void processEvent(InnerEvent event) {
//            super.processEvent(event);
//
//            notifyDataChanged();
//
//
//        }
//    }
}
