package com.yunat.channel.first;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import storm.kafka.StringScheme;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author hewei
 * 
 * @date 2015/4/16  18:03
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class MessageAnalysisBolt extends BaseRichBolt {

    OutputCollector _collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        _collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {

        String message = tuple.getString(0);

        List<String> list = Splitter.on("\t").trimResults().omitEmptyStrings().splitToList(message);

        List<Tuple> a = Lists.newArrayList(tuple);

        if (list.get(1).equals("disruptor")) {
            DisruptorMessage disruptorMessage = new DisruptorMessage(list.get(1), Long.parseLong(list.get(2)), list.get(3), list.get(4), Integer.parseInt(list.get(5)));
            _collector.emit(a, new Values(disruptorMessage));
        }

        if (list.get(1).equals("process-count")) {
            ProcessCountMessage processCountMessage = new ProcessCountMessage(list.get(1), Long.parseLong(list.get(2)), list.get(3), Long.parseLong(list.get(4)));
            _collector.emit(a, new Values(processCountMessage));
        }

        _collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(StringScheme.STRING_SCHEME_KEY));
    }
}
