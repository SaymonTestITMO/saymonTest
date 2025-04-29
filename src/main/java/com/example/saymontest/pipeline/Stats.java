package com.example.saymontest.pipeline;

import com.example.saymontest.model.SinkMessageImpl;
import java.util.HashMap;
import java.util.Map;

public class Stats {

    private long from;
    private long to;
    private double max = Double.MAX_VALUE;
    private double min = Double.MIN_VALUE;
    private double sum = 0;
    private int count = 0;

    public Stats(long timeStamp) {
        this.from = timeStamp;
        this.to = timeStamp;
    }

    public void update(double value, long timeStamp) {
        if (timeStamp < from)
            from = timeStamp;
        if (timeStamp > to)
            to = timeStamp;
        min = Math.min(min, value);
        max = Math.max(max, value);
        sum += value;
        count++;
    }

    public SinkMessageImpl toSinkMessage(String groupKey, long now) {
        return new SinkMessageImpl(
                from,
                to,
                parseGroupKey(groupKey),
                min,
                max,
                count == 0 ? 0 : sum / count,
                count
        );
    }

    private Map<String, String> parseGroupKey(String key) {
        Map<String, String> result = new HashMap<>();

        String[] pairs = key.split("\\|");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2)
                result.put(kv[0], kv[1]);
        }
        return result;
    }
}
