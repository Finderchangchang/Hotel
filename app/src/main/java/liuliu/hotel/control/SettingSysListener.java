package liuliu.hotel.control;

import android.content.Context;

import net.tsz.afinal.FinalDb;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

import liuliu.hotel.model.InvokeReturn;
import liuliu.hotel.web.DBHelper;
import liuliu.hotel.web.SoapObjectUtils;
import liuliu.hotel.web.WebServiceUtils;

/**
 * Created by Administrator on 2016/5/27.
 */
public class SettingSysListener {
    IDownHotelView mView;
    FinalDb db;
    DBHelper dbHelper ;
    Context myContext;
    public SettingSysListener(Context context,IDownHotelView view, FinalDb finalDb){
        mView=view;
        db=finalDb;
        myContext=context;
        dbHelper = new DBHelper(finalDb, context);
    }
    public void refushCode(){
        HashMap<String, String>properties = new HashMap<String, String>();
        properties.put("lgdm", "1306010001");
        WebServiceUtils.callWebService(true, "GetAllCodeLastChangeTime", properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                if (null != result) {
                    InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "GetAllCodeLastChangeTime");
                    if (invokeReturn.isSuccess()) {
                        //ToastShort("下载成功");

                        //比对时间，服务器更新时间大于本地更新时间，就更新字典


                    } else {
                        //ToastShort("下载失败");
                        mView.checkHotel(false,"");
                    }
                } else {
                    //ToastShort("下载失败");
                    mView.checkHotel(false,"请检查网络连接");
                }
                System.out.println("result==" + result);
            }
        });
    }
}
