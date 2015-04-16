//package A;
//
//import backtype.storm.task.TopologyContext;
//import backtype.storm.topology.BasicOutputCollector;
//import backtype.storm.topology.IBasicBolt;
//import backtype.storm.topology.OutputFieldsDeclarer;
//import backtype.storm.tuple.Tuple;
//import javafx.util.Pair;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import redis.clients.jedis.Jedis;
//
//import java.util.Map;
//
///**
// *
// * @author hewei
// *
// * @date 2015/4/9  19:36
// *
// * @version 5.0
// *
// * @desc
// *
// */
//public class SaveToRedisBolt implements IBasicBolt {
//
//    Log LOG = LogFactory.getLog(SaveToRedisBolt.class);
//
//    private static final long serialVersionUID = 1L;
//
//    public static final String LOG_ENTRY = "LogEntry";
//
//    static Jedis jedis;
//
//    public static void insert(Pair<String, Map<String, String>> nameValuePair) {
//        jedis = ConnRedis.getRedis();
//        String key = nameValuePair.first;
//        Map<String, String> maps = nameValuePair.second;
//        System.out.println("插入key" + key);
//        for (String field : maps.keySet()) {
//            jedis.hset(key, field, maps.get(field));
//            System.out.println("在key：" + key + "中插入field：" + field);
//        }
//    }
//
//    public void cleanup() {
//    }
//
//    public void declareOutputFields(OutputFieldsDeclarer declarer) {
//    }
//
//    public Map<String, Object> getComponentConfiguration() {
//        return null;
//    }
//
//    public void prepare(Map stormConf, TopologyContext context) {
//    }
//
//    public void execute(Tuple input, BasicOutputCollector collector) {
//        LogEntry1 entry = (LogEntry1) input.getValueByField(LOG_ENTRY);
//        insert(entry.getNameValuePair());
//    }
//}
