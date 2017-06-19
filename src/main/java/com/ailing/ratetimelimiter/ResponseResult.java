package com.ailing.ratetimelimiter;

import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created by zhengmiao on 17/6/20.
 */
public class ResponseResult<T> implements java.io.Serializable {
    private static final long serialVersionUID = 9220803609675350406L;
    private Integer result;
    private T data;
    private String msg;

    public ResponseResult() {
        this.result = 0;
        this.msg = "";
        this.data = null;
    }

    public static <T> ResponseResult<T> build(Integer code, String msg, T data) {
        ResponseResult result = new ResponseResult();
        result.setResult(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toString() {
        return org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }
}
