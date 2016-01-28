package com.example.jobflow;

import com.example.modelgen.dmdl.csv.AbstractAccessLogCsvOutputDescription;

import java.util.Arrays;
import java.util.List;

/**
 * アクセスログ集計を出力する
 */
public class AccessLogToCsv extends AbstractAccessLogCsvOutputDescription{
    @Override
    public String getBasePath() {
        return "logsummaries";
    }

    @Override
    public String getResourcePattern() {
        return "summary.{date}.csv";
    }

    @Override
    public List<String> getOrder() {
        return Arrays.asList("+hour","-count");
    }
}
