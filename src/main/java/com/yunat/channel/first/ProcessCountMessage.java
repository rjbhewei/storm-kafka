package com.yunat.channel.first;
/**
 * 
 * @author hewei
 * 
 * @date 2015/4/16  18:16
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class ProcessCountMessage extends BaseMessage {

    private long sendGatewayId;

    public ProcessCountMessage(String name, long time, String processer, long sendGatewayId) {
        super(name, time, processer);
        this.sendGatewayId = sendGatewayId;
    }

    public long getSendGatewayId() {
        return sendGatewayId;
    }
}
