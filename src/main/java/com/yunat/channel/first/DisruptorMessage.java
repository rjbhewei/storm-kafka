package com.yunat.channel.first;
/**
 * 
 * @author hewei
 * 
 * @date 2015/4/16  18:02
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class DisruptorMessage extends BaseMessage{

    private String clazz;

    private long sequence;

    public DisruptorMessage(String name, long time, String processer, String clazz, long sequence) {
        super(name, time, processer);
        this.clazz = clazz;
        this.sequence = sequence;
    }

    public String getClazz() {
        return clazz;
    }

    public long getSequence() {
        return sequence;
    }
}
