package com.example.jobflow;

import com.example.modelgen.dmdl.csv.AbstractApacheLogCsvInputDescription;

/**
 * CSVファイルからApacheログデータモデルを読み込む
 */
public class ApacheLogFromCsv extends AbstractApacheLogCsvInputDescription {
    @Override
    public String getBasePath() {
        return "logfiles";
    }

    @Override
    public String getResourcePattern() {
        return "*.csv";
    }
}
