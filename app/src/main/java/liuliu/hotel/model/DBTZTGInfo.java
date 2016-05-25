package liuliu.hotel.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/25.
 * 通知通告
 */
public class DBTZTGInfo implements Serializable {
    private int id;
    private String TGID;//编码
    private String TZFW;//范围
    private String TGBT;//通告标题
    private String TGNR;//通告内容
    private String TGTP;//通告图片
    private String FBR;//发布人
    private String FBSJ;//发布时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTGID() {
        return TGID;
    }

    public void setTGID(String TGID) {
        this.TGID = TGID;
    }

    public String getTZFW() {
        return TZFW;
    }

    public void setTZFW(String TZFW) {
        this.TZFW = TZFW;
    }

    public String getTGBT() {
        return TGBT;
    }

    public void setTGBT(String TGBT) {
        this.TGBT = TGBT;
    }

    public String getTGNR() {
        return TGNR;
    }

    public void setTGNR(String TGNR) {
        this.TGNR = TGNR;
    }

    public String getTGTP() {
        return TGTP;
    }

    public void setTGTP(String TGTP) {
        this.TGTP = TGTP;
    }

    public String getFBR() {
        return FBR;
    }

    public void setFBR(String FBR) {
        this.FBR = FBR;
    }

    public String getFBSJ() {
        return FBSJ;
    }

    public void setFBSJ(String FBSJ) {
        this.FBSJ = FBSJ;
    }
}
