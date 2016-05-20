package liuliu.hotel.model;

import java.io.Serializable;

public class CustomerModel implements Serializable {
    private String Sernum;
    private String HouseNum;
    private String Status;
    private String Name;
    private String Sex;
    private String Nation;
    private String CardNum;
    private String Native;
    private String imagelike;
    private String img1;
    private String img2;

    public String getSernum() {
        return Sernum;
    }

    public void setSernum(String sernum) {
        Sernum = sernum;
    }

    public String getHouseNum() {
        return HouseNum;
    }

    public void setHouseNum(String houseNum) {
        HouseNum = houseNum;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getNation() {
        return Nation;
    }

    public void setNation(String nation) {
        Nation = nation;
    }

    public String getCardNum() {
        return CardNum;
    }

    public void setCardNum(String cardNum) {
        CardNum = cardNum;
    }

    public String getNative() {
        return Native;
    }

    public void setNative(String aNative) {
        Native = aNative;
    }

    public String getImagelike() {
        return imagelike;
    }

    public void setImagelike(String imagelike) {
        this.imagelike = imagelike;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }
}
