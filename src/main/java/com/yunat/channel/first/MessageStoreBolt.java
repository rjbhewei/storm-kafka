package com.yunat.channel.first;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import storm.kafka.StringScheme;

import java.util.Map;

/**
 * 
 * @author hewei
 * 
 * @date 2015/4/16  18:24
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class MessageStoreBolt extends BaseRichBolt {

    OutputCollector _collector;

    JedisPool pool = null;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        _collector = collector;
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(1024);
        config.setMaxIdle(200);
        config.setMaxWaitMillis(1000);
        config.setTestOnBorrow(true);
        pool = new JedisPool(config, "10.200.187.45", 7008, 1000000);
    }

    @Override
    public void execute(Tuple input) {

        BaseMessage message = (BaseMessage) input.getValueByField(StringScheme.STRING_SCHEME_KEY);

        boolean broken = false;

        Jedis jedis = null;

        try {
            jedis = pool.getResource();

            if (message instanceof DisruptorMessage) {
                DisruptorMessage disruptorMessage = (DisruptorMessage) message;
                jedis.hset(message.getName(), message.getProcesser() + message.getTime(), disruptorMessage.getClazz() + disruptorMessage.getSequence());
            }

            if (message instanceof ProcessCountMessage) {
                ProcessCountMessage processCountMessage = (ProcessCountMessage) message;
                jedis.hset(message.getName(), message.getProcesser() + message.getTime(), processCountMessage.getSendGatewayId() + "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            broken = true;
        } finally {
            if (jedis != null) {
                if (broken) {
                    pool.returnBrokenResource(jedis);
                } else {
                    pool.returnResource(jedis);
                }
            }
        }

        _collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(StringScheme.STRING_SCHEME_KEY));
    }
}
