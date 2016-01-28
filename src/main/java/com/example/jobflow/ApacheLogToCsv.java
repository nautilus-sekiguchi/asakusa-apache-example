package com.example.jobflow;

import com.example.modelgen.dmdl.csv.AbstractApacheLogCsvOutputDescription;

/**
 * Apacheログの出力
 * 入力データにエラーが合った時に出力される
 *
 * @author Tadatoshi
 */
public class ApacheLogToCsv extends AbstractApacheLogCsvOutputDescription {
    @Override public String getBasePath() {
        return "inputerrors";
    }

    @Override public String getResourcePattern() {
        return "error.*.csv";
    }
}
