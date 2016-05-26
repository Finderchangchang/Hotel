package liuliu.hotel.web;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import liuliu.hotel.model.CodeModel;
import liuliu.hotel.model.CustomerModel;
import liuliu.hotel.model.DBLGInfo;
import liuliu.hotel.model.DBTZTGInfo;
import liuliu.hotel.model.InvokeReturn;

/**
 * Created by Administrator on 2016/5/20.
 */
public class SoapObjectUtils {
    public static InvokeReturn parseSoapObject(SoapObject result, String method) {
        InvokeReturn invokeReturn = new InvokeReturn();
        SoapObject provinceSoapObject = (SoapObject) result.getProperty(method + "Result");
        List<Object> list = new ArrayList<>();
        if (provinceSoapObject.getProperty("Sucess").toString().equals("true")) {
            invokeReturn.setSuccess(true);
            SoapObject soaplist = (SoapObject) provinceSoapObject.getProperty("Obj");
            if (method.equals("GetLGInfoByLGDM")) {
                DBLGInfo lg = new DBLGInfo();
                lg.setLGDM(soaplist.getProperty("LGDM").toString());
                lg.setLGDZ(soaplist.getProperty("LGDZ").toString());
                lg.setLGMC(soaplist.getProperty("LGMC").toString());
                lg.setQYSCM(soaplist.getProperty("QYSCM").toString());
                list.add(lg);
            }else if (method.equals("GetAllCodeLastChangeTime")){

            }else if (method.equals("GetCodeInfoByCodeName")||method.equals("SearchNative")||method.equals("GetAllUndownloadTZTGInfo")) {
                for (int i = 0; i < soaplist.getPropertyCount(); i++) {

                    SoapObject soapObject = (SoapObject) soaplist.getProperty(i);
                    if (method.equals("GetCodeInfoByCodeName")) {
                        CodeModel codeModel = new CodeModel();
                        codeModel.setKey(soapObject.getProperty("Key").toString());
                        codeModel.setVal(soapObject.getProperty("Value").toString());
                        list.add(codeModel);
                    }else if (method.equals("SearchNative")) {
                        CustomerModel model = new CustomerModel();
                        model.setSerialId(soapObject.getProperty("ZKLSH").toString());
                        model.setName(soapObject.getProperty("XM").toString());
                        model.setSex(soapObject.getProperty("XB").toString());
                        model.setNation(soapObject.getProperty("MZ").toString());
                        model.setNative(soapObject.getProperty("XZQH").toString());
                        model.setBirthday(soapObject.getProperty("CSRQ").toString());
                        model.setCardType(soapObject.getProperty("ZJLX").toString());
                        model.setCardId(soapObject.getProperty("ZJHM").toString());
                        model.setAddress(soapObject.getProperty("XZ").toString());
                        model.setRoomId(soapObject.getProperty("FH").toString());
                    //    model.setArea(soapObject.getProperty("SSXQ").toString());
                        //model.setHeadphoto(soapObject.getProperty("NBZP").toString());
                        model.setCheckInSign(soapObject.getProperty("FSBZ").toString());
                        model.setCheckOutTime(soapObject.getProperty("LDSJ").toString());
                        model.setCheckInTime(soapObject.getProperty("RZSJ").toString());
                        model.setComment(soapObject.getProperty("BZ").toString());


                    }else if(method.equals("GetAllUndownloadTZTGInfo")){
                        DBTZTGInfo info=new DBTZTGInfo();
                        info.setTGID(soapObject.getProperty("TGID").toString());
                        info.setTZFW(soapObject.getProperty("TZFW").toString());
                        info.setTGBT(soapObject.getProperty("TGBT").toString());
                        info.setTGNR(soapObject.getProperty("TGNR").toString());
                        info.setTGTP(soapObject.getProperty("TGTP").toString());
                        info.setFBR(soapObject.getProperty("FBR").toString());
                        info.setFBSJ(soapObject.getProperty("FBSJ").toString());
                        list.add(info);
                    }

                }
            }
        } else {
            invokeReturn.setSuccess(false);
        }
        invokeReturn.setData(list);
        return invokeReturn;
    }
}
