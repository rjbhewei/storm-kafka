//package A;
//
//import backtype.storm.spout.SpoutOutputCollector;
//import backtype.storm.task.TopologyContext;
//import backtype.storm.topology.OutputFieldsDeclarer;
//import backtype.storm.topology.base.BaseRichSpout;
//import backtype.storm.tuple.Values;
//import org.json.simple.JSONObject;
//import backtype.storm.tuple.Fields;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
///**
// *
// * @author hewei
// *
// * @date 2015/4/9  19:35
// *
// * @version 5.0
// *
// * @desc
// *
// */
//public class LogSpout extends BaseRichSpout {
//
//    public static final String LOG_ENTRY = "LogEntry";
//
//    private int curIdx = 0;
//
//    private SpoutOutputCollector collector;
//
//    private List<JSONObject> logEntry1s;
//
//    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
//        outputFieldsDeclarer.declare(new Fields(LOG_ENTRY));
//    }
//
//    public void open(Map conf, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
//        this.collector = spoutOutputCollector;
//        this.logEntry1s = getJsonObj();
//    }
//
//    public List<JSONObject> getJsonObj() {
//        List<JSONObject> list = new ArrayList<>();
//        LogEntry1 entry1 = new LogEntry1();
//        entry1.setAppId("app1");
//        entry1.setModuleName("module1");
//        entry1.setRuleType("rule1");
//        entry1.setTableName("table1");
//        entry1.setUserName("user1");
//        entry1.setStartTime(new Date());
//        entry1.setEndTime(new Date());
//        JSONObject obj1 = entry1.toJSON();
//        list.add(obj1);
//        return list;
//    }
//
//    public void nextTuple() {
//        if (curIdx < logEntry1s.size()) {
//            LogEntry1 entry = new LogEntry1(logEntry1s.get(curIdx));
//            collector.emit(new Values(entry));
//            curIdx++;
//        }
//    }
//}
