package liuliu.hotel.model;

import java.io.Serializable;

/**
 * 作者：zz on 2016/7/3 16:31
 */
public class APKVersionsModel implements Serializable {
     //路径
    private String Path;
    //服务器版本号
    private String Versions;

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getVersions() {
        return Versions;
    }

    public void setVersions(String versions) {
        Versions = versions;
    }
}
