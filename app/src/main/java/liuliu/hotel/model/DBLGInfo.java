package liuliu.hotel.model;

import net.tsz.afinal.annotation.sqlite.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/20.
 */
@Table(name = "db_DBLGInfo")
public class DBLGInfo implements Serializable {
    private int id;
    //旅馆信息
    private String LGDM;//旅馆代码
    private String LGMC;//旅馆名称
    private String LGDZ;//旅馆地址
    private String QYSCM;//企业授权码、注册码
    private String LoginPwd;


    public String getLGDM() {
        return LGDM;
    }

    public void setLGDM(String LGDM) {
        this.LGDM = LGDM;
    }

    public String getLGMC() {
        return LGMC;
    }

    public void setLGMC(String LGMC) {
        this.LGMC = LGMC;
    }

    public String getLGDZ() {
        return LGDZ;
    }

    public void setLGDZ(String LGDZ) {
        this.LGDZ = LGDZ;
    }

    public String getQYSCM() {
        return QYSCM;
    }

    public void setQYSCM(String QYSCM) {
        this.QYSCM = QYSCM;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginPwd() {
        return LoginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        LoginPwd = loginPwd;
    }

    /**
     * 获得旅馆测试数据
     *
     * @return
     */
    public static DBLGInfo getTestHotel() {
        DBLGInfo model = new DBLGInfo();
        model.setLGDM("1306020001");
        model.setLGMC("易佰快捷酒店");
        model.setLGDZ("河北省保定市新市区东风中路12号");
        model.setQYSCM("12342312");
        model.setLoginPwd("1");
        return model;
    }
}
