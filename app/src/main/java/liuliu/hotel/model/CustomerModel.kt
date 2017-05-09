package liuliu.hotel.model

import android.graphics.Bitmap

import java.io.ByteArrayOutputStream
import java.io.Serializable

import liuliu.hotel.base.Base64Util
import liuliu.hotel.method.*
import liuliu.hotel.method.DBFHInfo
import liuliu.hotel.utils.Utils
import liuliu.hotel.web.globalfunc

class CustomerModel : Serializable {
    var id: Int = 0
    //旅客信息
    var serialId: String? = null//流水号
    var name: String? = null//姓名
    var sex: String? = null//性别
    var nation: String? = null//民族
    var birthday: String? = null//出生日期
    var cardType: String? = null//证件类型
    var cardId: String? = null//证件号码
    var native: String? = null//籍贯
    var address: String? = null//地址
    var roomId: String? = null//房间号
    var area: String? = null//所属辖区
    var checkInTime: String? = null//入住时间
    var checkOutTime: String? = null//离店时间
    var checkInSign: String? = null//入住表示，当前程序版本号
    var headphoto: Bitmap? = null//头像
    var comment: String? = null//备注
    var url: String? = null

    constructor() {
        val fh = arrayOfNulls<DBFHInfo>(3)
    }

    constructor(name: String, sex: String, nation: String, address: String, roomId: String) {
        this.name = name
        this.sex = sex
        this.nation = nation
        this.address = address
        this.roomId = roomId
    }

    fun getXml(xml: String?, isSave: Boolean, info: DBLGInfo): String {
        var xml = xml

        //String xml = gfunc.getAssetsFileData("checkInNativeParameter.xml");

        if (xml != null && xml != "") {
            if (isSave) {
                xml = xml.replace("Method", "CheckInNative")
            } else {
                xml = xml.replace("Method", "CheckOutNative")
            }

            xml = xml.replace("InputHotelId", info.lgdm)
            xml = xml.replace("InputAuthorizationCode", info.qyscm)
            xml = xml.replace("InputSerialId", Utils.URLEncode(serialId))
            xml = xml.replace("InputName", Utils.URLEncode(name))
            xml = xml.replace("InputSex", Utils.URLEncode(sex))
            xml = xml.replace("InputNation", Utils.URLEncode(nation))
            xml = xml.replace("InputBirthday", Utils.URLEncode(birthday))
            xml = xml.replace("InputCardType", Utils.URLEncode(cardType))
            xml = xml.replace("InputCardId", Utils.URLEncode(cardId))
            xml = xml.replace("InputNative", Utils.URLEncode(native))
            xml = xml.replace("InputAddress", Utils.URLEncode(address))
            xml = xml.replace("InputRoomId", Utils.URLEncode(roomId))
            xml = xml.replace("InputCheckInTime", Utils.URLEncode(checkInTime))
            xml = xml.replace("InputCheckOutTime", Utils.URLEncode(checkOutTime))
            xml = xml.replace("InputReceiveTime", "")
            xml = xml.replace("InputCheckInSigen", Utils.URLEncode(checkInSign))
            if (headphoto != null) {
                val photo = ByteArrayOutputStream()
                headphoto!!.compress(Bitmap.CompressFormat.JPEG, 90, photo)
                val ba = photo.toByteArray()
                val photodata = Base64Util.encode(ba)
                xml = xml.replace("InputHeadPhoto", photodata)
            } else {
                xml = xml.replace("InputHeadPhoto", "")
            }
            xml = xml.replace("InputComment", "")
        }

        return xml
    }

    fun getLeaveXml(xml: String?): String {
        var xml = xml
        if (xml != "" && xml != null) {
            xml = xml.replace("InputSerialId", serialId)
            xml = xml.replace("InputLeaveTime", checkOutTime)
        }
        return xml
    }
}
