package liuliu.hotel.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/21.
 */
public class SerialNumModel implements Serializable {
    private int id;
    private  String SerialNum;
    private String LastUserDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerialNum() {
        return SerialNum;
    }

    public void setSerialNum(String serialNum) {
        SerialNum = serialNum;
    }

    public String getLastUserDate() {
        return LastUserDate;
    }

    public void setLastUserDate(String lastUserDate) {
        LastUserDate = lastUserDate;
    }
}
