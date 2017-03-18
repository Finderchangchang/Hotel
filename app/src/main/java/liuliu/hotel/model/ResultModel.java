package liuliu.hotel.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/5.
 */
public class ResultModel implements Serializable {
    //返回的数据
    private String Data;
    //指令命令是否已被成功执行
    private boolean Success;
    //命令执行后的通知通告
    private String Message;
    //命令执行结束时间
    private String Time;

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
