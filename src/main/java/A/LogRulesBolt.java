//package A;
//
//import backtype.storm.task.OutputCollector;
//import backtype.storm.task.TopologyContext;
//import backtype.storm.topology.OutputFieldsDeclarer;
//import backtype.storm.topology.base.BaseRichBolt;
//import backtype.storm.tuple.Tuple;
//import backtype.storm.tuple.Values;
//import backtype.storm.tuple.Fields;
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
//public class LogRulesBolt extends BaseRichBolt {
//
//    private static final long serialVersionUID = 1L;
//
//    public static final String LOG_ENTRY = "LogEntry";
//
//    private StatelessKnowledgeSession ksession;
//
//    private OutputCollector collector;
//
//    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
//        this.collector = collector;
//        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
//        kbuilder.add(ResourceFactory.newClassPathResource("Syslog.drl", getClass()), ResourceType.DRL);
//        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
//        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
//        ksession = kbase.newStatelessKnowledgeSession();
//    }
//
//    public void execute(Tuple input) {
//        LogEntry1 entry1 = (LogEntry1) input.getValueByField(LOG_ENTRY);
//        ksession.execute(entry1);
//        collector.emit(new Values(entry1));
//    }
//
//    public void declareOutputFields(OutputFieldsDeclarer declarer) {
//        declarer.declare(new Fields(LOG_ENTRY));
//    }
//}
