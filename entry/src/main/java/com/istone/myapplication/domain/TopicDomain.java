package com.istone.myapplication.domain;


/**
 * description list domain
 *
 * @author baihe
 * created 2021/2/8 14:37
 */
public class TopicDomain {
    /**
     * topic
     */
    private String topic;
    /**
     * url
     */
    private String url;

    public TopicDomain(String topic, String url) {
        this.topic = topic;
        this.url = url;

    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
