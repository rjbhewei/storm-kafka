//package A;
//
//import backtype.storm.LocalCluster;
//import backtype.storm.StormSubmitter;
//import backtype.storm.topology.TopologyBuilder;
//import backtype.storm.utils.Utils;
//import backtype.storm.Config;
///**
// *
// * @author hewei
// *
// * @date 2015/4/9  19:31
// *
// * @version 5.0
// *
// * @desc
// *
// */
//public class LogTopology {
//
//    public LogTopology() {
//    }
//
//    public static void main(String[] args) throws Exception {
//        TopologyBuilder topologyBuilder = new TopologyBuilder();
//        topologyBuilder.setSpout("logSpout", new LogSpout(), 1);
//        topologyBuilder.setBolt("logRules", new LogRulesBolt(), 3).shuffleGrouping("logSpout");
//        topologyBuilder.setBolt("counter", new SaveToRedisBolt(), 3).shuffleGrouping("logRules");
//        Config conf = new Config();
//        conf.setDebug(true);
//        if (args != null && args.length > 0) {
//            conf.setNumWorkers(3);
//            StormSubmitter.submitTopology(args[0], conf, topologyBuilder.createTopology());
//        } else {
//            LocalCluster cluster = new LocalCluster();
//            cluster.submitTopology("test", conf, topologyBuilder.createTopology());
//            Utils.sleep(5000);
//            cluster.killTopology("test");
//            cluster.shutdown();
//        }
//    }
//}