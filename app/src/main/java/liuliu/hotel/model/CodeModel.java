package liuliu.hotel.model;

/**
 * Created by LiuWeiJie on 2015/8/4 0004.
 * Email:1031066280@qq.com
 */
public class CodeModel {
    private int id;
    private String Key;
    private String Val;
    private String CodeName;

    public String getCodeName() {
        return CodeName;
    }

    public void setCodeName(String codeName) {
        CodeName = codeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getVal() {
        return Val;
    }

    public void setVal(String val) {
        Val = val;
    }
}
