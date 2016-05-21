package liuliu.hotel.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/20.
 */
public class DBLGInfo implements Serializable {
    private int id;
    //旅馆信息
    private String LGDM;//旅馆代码
    private String LGMC;//旅馆名称
    private String LGDZ;//旅馆地址
    private String QYSCM;//企业授权码、注册码

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
}
