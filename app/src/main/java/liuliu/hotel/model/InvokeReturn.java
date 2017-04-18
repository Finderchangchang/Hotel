package liuliu.hotel.model;

import java.util.List;

import liuliu.hotel.method.Version;

/**
 * Created by Administrator on 2015/11/11.
 */
public class InvokeReturn {
    /// <summary>
    /// 指示操作是否成功完成。
    /// </summary>
    private boolean success;

    /// <summary>
    /// 操作返回的状态码。
    /// </summary>
    private String code;

    /// <summary>
    /// 操作返回的信息。
    /// </summary>
    private String message;

    /// <summary>
    /// 操作完成时间。
    /// </summary>
    private String time;

    /// <summary>
    /// 操作返回的对象。
    /// </summary>
    private List<Object> data;
    private Version obj;

    public Version getObj() {
        return obj;
    }

    public void setObj(Version obj) {
        this.obj = obj;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
