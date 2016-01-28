package com.example.jobflow;

import com.example.modelgen.dmdl.csv.AbstractUrlCountCsvOutputDescription;

import java.util.Arrays;
import java.util.List;

/**
 * URL毎に集計した結果を出力する
 *
 * @author Tadatoshi
 */
public class UrlCountToCsv extends AbstractUrlCountCsvOutputDescription {
    @Override public String getBasePath() {
        return "urlsummaries";
    }

    @Override public String getResourcePattern() {
        return "urlcount.{status}.csv";
    }

    @Override public List<String> getOrder() {
        return Arrays.asList("-count");
    }
}
