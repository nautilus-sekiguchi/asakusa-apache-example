package com.example.jobflow;

import com.asakusafw.vocabulary.flow.*;
import com.asakusafw.vocabulary.flow.util.CoreOperatorFactory;
import com.example.modelgen.dmdl.model.AccessLog;
import com.example.modelgen.dmdl.model.ApacheLog;
import com.example.modelgen.dmdl.model.LogTmp;
import com.example.modelgen.dmdl.model.UrlCount;
import com.example.operator.LogOperatorFactory;

/**
 * 集計するジョブフロー
 */
@JobFlow(name = "summaryflow")
public class LogSummarizeJobFlow extends FlowDescription{

    final In<ApacheLog> inMessage;
    final Out<AccessLog> outMessage;
    final Out<UrlCount> outCount;
    final Out<ApacheLog> error;

    /**
     * コンストラクタ。ジョブフローの入出力も定義する。
     * @param inMessage
     * @param outMessage
     */
    public LogSummarizeJobFlow(
            @Import(name = "apachelog", description = ApacheLogFromCsv.class)
            In<ApacheLog> inMessage,
            @Export(name = "accesslog", description = AccessLogToCsv.class)
            Out<AccessLog> outMessage,
            @Export(name = "urlcount", description = UrlCountToCsv.class)
            Out<UrlCount> outCount,
            @Export(name = "apacheerr", description = ApacheLogToCsv.class)
            Out<ApacheLog> error
    ) {
        this.inMessage = inMessage;
        this.outMessage = outMessage;
        this.outCount = outCount;
        this.error = error;
    }

    /**
     * ジョブフロー本体
     */
    @Override
    protected void describe() {
        CoreOperatorFactory core = new CoreOperatorFactory();
        LogOperatorFactory op = new LogOperatorFactory();

        // 入力チェック(NULLチェック)
        LogOperatorFactory.CheckNull in
                = op.checkNull(inMessage);
        // NGは出力
        error.add(in.ng);

        // ApacheLogを拡張してLogTmpを作成
        CoreOperatorFactory.Extend<LogTmp> tmpEx
                = core.extend(in.ok, LogTmp.class);

        // LogTmpの更新処理（日時を取り出したり、URLを取り出したり)
        LogOperatorFactory.Change logtmp = op.change(tmpEx.out);

        // ステータスで処理の振り分け
        LogOperatorFactory.BranchByStatus status
                = op.branchByStatus(logtmp.out);

        // 異常ステータス集計
        LogOperatorFactory.SumByUrlStatus ngSum
                = op.sumByUrlStatus(status.ng);
        outCount.add(ngSum.out);

        // 正常ステータス集計
        LogOperatorFactory.SumByUrl okSum = op.sumByUrl(status.ok);
        outMessage.add(okSum.out);

    }
}
