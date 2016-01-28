package com.example.com.example.batch;

import com.asakusafw.vocabulary.batch.Batch;
import com.asakusafw.vocabulary.batch.BatchDescription;
import com.example.jobflow.LogSummarizeJobFlow;

/**
 * ログの集計を行うバッチファイル
 */
@Batch(name = "example.log")
public class LogBatch extends BatchDescription {
    @Override
    protected void describe() {
        run(LogSummarizeJobFlow.class).soon();
    }
}
