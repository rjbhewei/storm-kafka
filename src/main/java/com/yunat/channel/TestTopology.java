package com.yunat.channel;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import storm.kafka.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author hewei
 * 
 * @date 2015/4/9  19:55
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class TestTopology {

    public static final String zk = "10.200.187.43:2181,10.200.187.44:2181,10.200.187.45:2181";

    public static final String topic = "kafkaToptic";

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {

        BrokerHosts brokerHosts = new ZkHosts(zk);

        SpoutConfig spoutConf = new SpoutConfig(brokerHosts, topic, "/" + topic, "storm-kafka-" + topic);

        spoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());

        IRichSpout spout = new KafkaSpout(spoutConf);

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("kafka-reader", spout, 3);
        builder.setBolt("KafkaPrintBolt", new KafkaPrintBolt(), 2).shuffleGrouping("kafka-reader");
        builder.setBolt("KafkaCounterBolt", new KafkaCounterBolt(), 2).shuffleGrouping("KafkaPrintBolt");

        Config conf = new Config();
        conf.setDebug(true);

        if (args != null && args.length > 0) {
            conf.setNumWorkers(3);
            StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
        } else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("test", conf, builder.createTopology());
//            Utils.sleep(10000);
//            cluster.killTopology("test");
//            cluster.shutdown();
        }
    }

    public static class KafkaPrintBolt extends BaseRichBolt {

        OutputCollector _collector;

        JedisPool pool = null;

        public static AtomicLong AL =new AtomicLong();

        @Override
        public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
            _collector = collector;
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(1024);
            config.setMaxIdle(200);
            config.setMaxWaitMillis(1000);
            config.setTestOnBorrow(true);
            pool = new JedisPool(config, "10.200.187.45", 7009, 1000000);
        }

        @Override
        public void execute(Tuple tuple) {
            System.out.println(tuple.getString(0)+"---------------");


            boolean broken = false;
            Jedis jedis = null;
            try {
                jedis = pool.getResource();
                jedis.hset("kafka-storm", AL.incrementAndGet() + "", tuple.getString(0) + "---------------");
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


            List a=new ArrayList();
            a.add(tuple);
            _collector.emit(a,new Values(tuple.getString(0)));



            _collector.ack(tuple);
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("word"));
        }
    }

    public static class KafkaCounterBolt extends BaseRichBolt {

        OutputCollector _collector;
        JedisPool pool = null;
        public static AtomicLong AL =new AtomicLong();
        @Override
        public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
            _collector = collector;
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(1024);
            config.setMaxIdle(200);
            config.setMaxWaitMillis(1000);
            config.setTestOnBorrow(true);
            pool = new JedisPool(config, "10.200.187.45", 7009, 1000000);
        }

        @Override
        public void execute(Tuple input) {
            System.out.println(input.getString(0) + "++++++++++++");

            boolean broken = false;
            Jedis jedis = null;
            try {
                jedis = pool.getResource();
                jedis.hset("kafka-storm", AL.incrementAndGet()+"", input.getString(0) + "++++++++++++");
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
            declarer.declare(new Fields("word"));
        }
    }
}
