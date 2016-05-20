package liuliu.hotel.model;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import liuliu.hotel.web.globalfunc;

public class CustomerModel implements Serializable {
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
    private String Headphoto;//头像
    private String Comment;//备注


    public String getXml(String xml,boolean isSave, DBLGInfo info) {

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
            xml = xml.replace("InputSerialId", getSerialId());
            xml = xml.replace("InputName", getName());
            xml = xml.replace("InputSex", getSex());
            xml = xml.replace("InputNation", getNation());
            xml = xml.replace("InputBirthday", getBirthday());
            xml = xml.replace("InputCardType", getCardType());
            xml = xml.replace("InputCardId", getCardId());

            xml = xml.replace("InputNative", getNative());
            xml = xml.replace("InputAddress", getAddress());
            xml = xml.replace("InputRoomId", getRoomId());
            xml = xml.replace("InputArea", getArea());
            xml = xml.replace("InputCheckInTime", getCheckInTime());
            xml = xml.replace("InputCheckOutTime", getCheckOutTime());
            xml = xml.replace("InputReceiveTime", "");
            xml = xml.replace("InputCheckInSigen", getCheckInSign());

            if (Headphoto != null) {
//                ByteArrayOutputStream photo = new ByteArrayOutputStream();
//                Headphoto.compress(Bitmap.CompressFormat.JPEG, 95, photo);
//                byte[] ba = photo.toByteArray();
//                String photodata = Base64Util.encode(ba);
//                xml = xml.replace("InputHeadPhoto", photodata);
            } else {
                xml = xml.replace("InputHeadPhoto", "");
            }

            xml = xml.replace("InputComment", "");


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

    public String getHeadphoto() {
        return Headphoto;
    }

    public void setHeadphoto(String headphoto) {
        Headphoto = headphoto;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}
