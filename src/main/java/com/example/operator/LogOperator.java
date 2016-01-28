package com.example.operator;

import com.asakusafw.runtime.value.DateTime;
import com.asakusafw.runtime.value.DateTimeOption;
import com.asakusafw.runtime.value.StringOption;
import com.asakusafw.vocabulary.operator.Branch;
import com.asakusafw.vocabulary.operator.Summarize;
import com.asakusafw.vocabulary.operator.Update;
import com.example.modelgen.dmdl.model.AccessLog;
import com.example.modelgen.dmdl.model.ApacheLog;
import com.example.modelgen.dmdl.model.LogTmp;
import com.example.modelgen.dmdl.model.UrlCount;

/**
 * 演算を定義
 */
public abstract class LogOperator {

    public enum Status {
        OK,
        NG
    }

    /**
     * ステータスコードを元に処理を振り分ける
     * 正常(200)とそれ以外に分割
     * @param log
     */
    @Branch
    public Status branchByStatus(LogTmp log){
        if("200".equals(log.getResponseStatusAsString())){
            return Status.OK;
        }else{
            return Status.NG;
        }
    }

    /**
     * 日付やURLがnullでないかチェック。nullのものはファイルを分割
     * @param log
     * @return
     */
    @Branch
    public Status checkNull(ApacheLog log){
        final DateTimeOption dt = log.getRequestTimeOption();
        final StringOption request = log.getRequestLineOption();
        if(dt.isNull() || request.isNull()){
            return Status.NG;
        }else{
            return Status.OK;
        }
    }

    /**
     * 入力内容の日付を分解して年月日と時間を抽出する。
     * また、アクセスメソッド、URLを分解し、URLを抽出する。
     * @param log
     */
    @Update
    public void change(LogTmp log){
        DateTime d = log.getRequestTime();
        log.setRequestDateAsString(String.format("%4d%02d%02d",d.getYear(),d.getMonth(),d.getDay()));
        log.setRequestHourAsString(String.format("%02d",d.getHour()));

        String url = log.getRequestLineAsString().split("\\s+")[1];
        log.setRequestUrlAsString(url);
    }

    /**
     * URL,Statusをキーに集計を行う
     * @param log
     * @return
     */
    @Summarize
    public abstract UrlCount sumByUrlStatus(LogTmp log);

    /**
     * URLをキーに集計を行う
     * @param log
     * @return
     */
    @Summarize
    public abstract AccessLog sumByUrl(LogTmp log);
}
