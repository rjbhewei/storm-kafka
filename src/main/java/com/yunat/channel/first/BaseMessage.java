package com.yunat.channel.first;
/**
 * 
 * @author hewei
 * 
 * @date 2015/4/16  18:20
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class BaseMessage {

    private String name;

    private long time;

    private String processer;

    public BaseMessage(String name, long time, String processer) {
        this.name = name;
        this.time = time;
        this.processer = processer;
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }

    public String getProcesser() {
        return processer;
    }
}
