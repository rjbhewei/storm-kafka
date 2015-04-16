package com.yunat.channel.first;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;
import storm.kafka.*;

/**
 * 
 * @author hewei
 * 
 * @date 2015/4/15  17:28
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class Luancher {

    public static final String zk = "10.200.187.43:2181,10.200.187.44:2181,10.200.187.45:2181";

    public static final String topic = "kafkaToptic";

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {

        BrokerHosts brokerHosts = new ZkHosts(zk);

        SpoutConfig spoutConf = new SpoutConfig(brokerHosts, topic, "/test" + topic, "storm-kafka-" + topic);

        spoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());

        IRichSpout spout = new KafkaSpout(spoutConf);

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("kafka-reader", spout, 3);
        builder.setBolt("MessageAnalysisBolt", new MessageAnalysisBolt(), 2).shuffleGrouping("kafka-reader");
        builder.setBolt("KafkaCounterBolt", new MessageStoreBolt(), 2).shuffleGrouping("MessageAnalysisBolt");

        Config conf = new Config();
        conf.setDebug(true);

        if (args != null && args.length > 0) {
            conf.setNumWorkers(3);
            StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
        } else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("test", conf, builder.createTopology());
            Utils.sleep(10000000);
            cluster.killTopology("test");
            cluster.shutdown();
        }
    }
}
