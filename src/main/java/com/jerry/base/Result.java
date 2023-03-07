package com.jerry.base;

import java.io.Serializable;

/**
 * 通用返回对象
 * @param <T>返回类型
 * @author zhangzhilin
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -5339905835789989772L;
    /**
     * 响应参数500(服务器错误)，200(响应成功)，
     */
    private int code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    /** 操作成功 **/
    public Result<T> success() {
        return success("操作成功");
    }

    /**
     * 操作成功-方法重载
     * @param message 提示信息
     * @return
     */
    public Result<T> success(String message) {
        return success(200, message);
    }

    /**
     * 操作成功-方法重载
     * @param code 状态码
     * @param message 提示信息
     * @return
     */
    public Result<T> success(int code, String message) {
        this.setCode(code);
        this.setMsg(message);
        return this;
    }

    /** 操作失败 **/
    public Result<?> error(String message){
        return  error(500,message);
    }

    /**
     * 操作失败-方法重载
     * @param code 状态码
     * @param message 提示信息
     * @return
     */
    public Result<?> error(int code,String message){
        this.setCode(code);
        this.setMsg(message);
        return this;
    }
    /** 数据存储 **/
    public Result<T> put(T object) {
        this.setData(object);
        return this;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
