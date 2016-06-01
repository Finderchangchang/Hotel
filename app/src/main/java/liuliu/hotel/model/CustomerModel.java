package liuliu.hotel.model;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import liuliu.hotel.base.Base64Util;
import liuliu.hotel.utils.Utils;
import liuliu.hotel.web.globalfunc;

public class CustomerModel implements Serializable {
    private int id;
    //旅客信息
    private String SerialId;//流水号
    private String Name;//姓名
    private String Sex;//性别
    private String Nation;//民族
    private String Birthday;//出生日期
    private String CardType;//证件类型
    private String CardId;//证件号码
    private String Native;//籍贯
    private String Address;//地址
    private String RoomId;//房间号
    private String Area;//所属辖区
    private String CheckInTime;//入住时间
    private String CheckOutTime;//离店时间
    private String CheckInSign;//入住表示，当前程序版本号
    private Bitmap Headphoto;//头像
    private String Comment;//备注
    private String url;

    public CustomerModel() {

    }

    public CustomerModel(String name, String sex, String nation, String address, String roomId) {
        Name = name;
        Sex = sex;
        Nation = nation;
        Address = address;
        RoomId = roomId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getXml(String xml, boolean isSave, DBLGInfo info) {

        //String xml = gfunc.getAssetsFileData("checkInNativeParameter.xml");

        if (xml != null && !xml.equals("")) {
            if (isSave) {
                xml = xml.replace("Method", "CheckInNative");
            } else {
                xml = xml.replace("Method", "CheckOutNative");
            }

            xml = xml.replace("InputHotelId", info.getLGDM());
            xml = xml.replace("InputAuthorizationCode",
                    info.getQYSCM());

            xml = xml.replace("InputSerialId", Utils.URLEncode(getSerialId()));
            xml = xml.replace("InputName", Utils.URLEncode(getName()));
            xml = xml.replace("InputSex", Utils.URLEncode(getSex()));
            xml = xml.replace("InputNation", Utils.URLEncode(getNation()));
            xml = xml.replace("InputBirthday", Utils.URLEncode(getBirthday()));
            xml = xml.replace("InputCardType", Utils.URLEncode(getCardType()));
            xml = xml.replace("InputCardId", Utils.URLEncode(getCardId()));

            xml = xml.replace("InputNative", Utils.URLEncode(getNative()));
            xml = xml.replace("InputAddress", Utils.URLEncode(getAddress()));
            xml = xml.replace("InputRoomId", Utils.URLEncode(getRoomId()));
            xml = xml.replace("InputCheckInTime", Utils.URLEncode(getCheckInTime()));
            xml = xml.replace("InputCheckOutTime", Utils.URLEncode(getCheckOutTime()));
            xml = xml.replace("InputReceiveTime", "");
            xml = xml.replace("InputCheckInSigen", Utils.URLEncode(getCheckInSign()));
            if (Headphoto != null) {
                ByteArrayOutputStream photo = new ByteArrayOutputStream();
                Headphoto.compress(Bitmap.CompressFormat.JPEG, 95, photo);
                byte[] ba = photo.toByteArray();
                String photodata = Base64Util.encode(ba);
                xml = xml.replace("InputHeadPhoto", photodata);
            } else {
                xml = xml.replace("InputHeadPhoto", "");
            }
            xml = xml.replace("InputComment", "");
        }

        return xml;
    }

    public String getLeaveXml(String xml) {
        if (!xml.equals("") && xml != null) {
            xml = xml.replace("InputSerialId", getSerialId());
            xml = xml.replace("InputLeaveTime", getCheckOutTime());
        }
        return xml;
    }

    public String getSerialId() {
        return SerialId;
    }

    public void setSerialId(String serialId) {
        SerialId = serialId;
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

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getCardType() {
        return CardType;
    }

    public void setCardType(String cardType) {
        CardType = cardType;
    }

    public String getCardId() {
        return CardId;
    }

    public void setCardId(String cardId) {
        CardId = cardId;
    }

    public String getNative() {
        return Native;
    }

    public void setNative(String aNative) {
        Native = aNative;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getRoomId() {
        return RoomId;
    }

    public void setRoomId(String roomId) {
        RoomId = roomId;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getCheckInTime() {
        return CheckInTime;
    }

    public void setCheckInTime(String checkInTime) {
        CheckInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return CheckOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        CheckOutTime = checkOutTime;
    }

    public String getCheckInSign() {
        return CheckInSign;
    }

    public void setCheckInSign(String checkInSign) {
        CheckInSign = checkInSign;
    }

    public Bitmap getHeadphoto() {
        return Headphoto;
    }

    public void setHeadphoto(Bitmap headphoto) {
        Headphoto = headphoto;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}
