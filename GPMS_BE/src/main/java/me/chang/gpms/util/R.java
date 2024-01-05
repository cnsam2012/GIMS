package me.chang.gpms.util;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.HashMap;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class R<T> implements Serializable {

    private static final long serialVersionUID = 5187687995319002219L;

    private Integer code;
    private Boolean success;
    private String msg;
    private T data;

//    public R() {
//    }

    public static <T> R<T> definition(int code, String message, Boolean success, T data) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setSuccess(success);
        r.setMsg(message);
        r.setData(data);
        return r;
    }

    public static <T> R<T> definition(HttpServletResponse resp, int code, String message, Boolean success, T data) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setSuccess(success);
        r.setMsg(message);
        r.setData(data);
        resp.setStatus(code);
        return r;
    }

    public static <T> R<T> ok(String message, T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMsg(message);
        r.setSuccess(true);
        r.setData(data);
        return r;
    }

    public static <T> R<T> ok(int code, String message, T data) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(message);
        r.setSuccess(true);
        r.setData(data);
        return r;
    }
    public static <T> R<T> ok(HttpServletResponse resp, int code, String message, T data) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(message);
        r.setSuccess(true);
        r.setData(data);
        resp.setStatus(code);
        return r;
    }
    public static <T> R<T> ok(HttpServletResponse resp, String message, T data) {
        R<T> r = new R<>();
        r.setCode(HttpServletResponse.SC_OK);
        r.setMsg(message);
        r.setSuccess(true);
        r.setData(data);
        resp.setStatus(HttpServletResponse.SC_OK);
        return r;
    }

    public static <T> R<T> ok(HttpServletResponse resp, String message) {
        R<T> r = new R<>();
        r.setCode(HttpServletResponse.SC_OK);
        r.setMsg(message);
        r.setSuccess(true);
        r.setData(null);
        resp.setStatus(HttpServletResponse.SC_OK);
        return r;
    }

    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMsg("操作成功");
        r.setSuccess(true);
        r.setData(data);
        return r;
    }

    public static <T> R<T> ok(String message) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setSuccess(true);
        r.setMsg(message);
        return r;
    }

    public static <T> R<T> ok(int code, String message) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setSuccess(true);
        r.setMsg(message);
        return r;
    }

    public static <T> R<T> ok() {
        R<T> r = new R<>();
        r.setCode(200);
        r.setSuccess(true);
        r.setMsg("操作成功");
        return r;
    }

    public static <T> R<T> error() {
        R<T> r = new R<>();
        r.setCode(500);
        r.setSuccess(false);
        r.setMsg("操作失败");
        return r;
    }

    public static <T> R<T> error(String message) {
        R<T> r = new R<>();
        r.setCode(500);
        r.setSuccess(false);
        r.setMsg(message);
        return r;
    }

    public static <T> R<T> error(int code, String message) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setSuccess(false);
        r.setMsg(message);
        return r;
    }

    public static <T> R<T> error(HttpServletResponse resp, int code, String message) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setSuccess(false);
        r.setMsg(message);
        resp.setStatus(code);
        return r;
    }

    public static <T> R<T> error(int code, String message, T data) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setSuccess(false);
        r.setMsg(message);
        r.setData(data);
        return r;
    }

    public static <T> R<T> error(HttpServletResponse resp, int code, String message, T data) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setSuccess(false);
        r.setMsg(message);
        r.setData(data);
        resp.setStatus(code);
        return r;
    }

    public static <T> R<T> error(HttpServletResponse resp, String message, T data) {
        R<T> r = new R<>();
        r.setCode(HttpServletResponse.SC_BAD_REQUEST);
        r.setSuccess(false);
        r.setMsg(message);
        r.setData(data);
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return r;
    }

    public static <T> R<T> error(HttpServletResponse resp, String message) {
        R<T> r = new R<>();
        r.setCode(HttpServletResponse.SC_BAD_REQUEST);
        r.setSuccess(false);
        r.setMsg(message);
        r.setData(null);
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return r;
    }


    public static <T> R<T> ok(HttpServletResponse resp, int code, String message) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setSuccess(true);
        r.setMsg(message);
        r.setData(null);
        resp.setStatus(code);
        return r;
    }

    public String toJsonString() {
        JSONObject json = new JSONObject();
        /**
         *     private Integer code;
         *     private Boolean success;
         *     private String msg;
         *     private T data;
         */
        if (ObjectUtil.isNotEmpty(this.code)) {
            json.put("code", this.code);
        }
        if (ObjectUtil.isNotEmpty(this.success)) {
            json.put("success", this.success);
        }
        if (ObjectUtil.isNotEmpty(this.msg)) {
            json.put("msg", this.msg);
        }
        if (ObjectUtil.isNotEmpty(this.data)) {
            json.put("data", this.data);
        }

        return json.toJSONString();
    }

    @Test
    public void test() {
        var data = new HashMap<>();
        data.put("data", 732034);
        String string = R.ok(123, "456", data).toJsonString();
        log.info(string);
    }
}
