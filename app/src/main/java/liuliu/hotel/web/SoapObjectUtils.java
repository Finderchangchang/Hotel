package liuliu.hotel.web;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import liuliu.hotel.model.DBLGInfo;
import liuliu.hotel.model.InvokeReturn;

/**
 * Created by Administrator on 2016/5/20.
 */
public class SoapObjectUtils {
    public static InvokeReturn parseSoapObject(SoapObject result, String method) {
        InvokeReturn invokeReturn = null;
        SoapObject provinceSoapObject = (SoapObject) result.getProperty(method + "Result");
        List<Object> list = new ArrayList<>();
        if (provinceSoapObject.getProperty("Sucess").toString().equals("true")) {
            SoapObject soaplist = (SoapObject) provinceSoapObject.getProperty("Obj");
            if (method.equals("GetLGInfoByLGDM")) {
                DBLGInfo lg = new DBLGInfo();
                lg.setLGDM(soaplist.getProperty("LGDM").toString());
                lg.setLGDZ(soaplist.getProperty("LGDZ").toString());
                lg.setLGMC(soaplist.getProperty("LGMC").toString());
                lg.setQYSCM(soaplist.getProperty("QYSCM").toString());

                list.add(lg);
                invokeReturn.setData(list);
            }
        }
        return invokeReturn;
    }
}
