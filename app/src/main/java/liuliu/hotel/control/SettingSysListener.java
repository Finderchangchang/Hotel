package liuliu.hotel.control;

import android.content.Context;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.model.CodeModel;

import org.ksoap2.serialization.SoapObject;
import java.util.HashMap;
import liuliu.hotel.model.InvokeReturn;
import liuliu.hotel.utils.Utils;
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
                        String localTime= Utils.ReadString("CodeLastChangeTime");

                        if(Utils.DateCompare(localTime,invokeReturn.getTime())){
                            //不需要更新
                            mView.checkHotel(true,"字典已经是最新");

                        }else{
                            //需要更新
                            db.deleteAll(CodeModel.class);
                            getCodeServer("XZQH");
                        }
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
    private void getCodeServer(final String name){
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("lgdm", "1306010001");
        properties.put("codeName", name);//XZQH,行政区划，民族MZ,证件类型ZJLX,1男2女
        WebServiceUtils.callWebService(true, "GetCodeInfoByCodeName", properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                if (null != result) {
                    //mView.checkHotel(false,"字典下载成功");
                    InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "GetCodeInfoByCodeName");
                    System.out.println(result);
                    for(int i=0;i<invokeReturn.getData().size();i++){
                        CodeModel model= (CodeModel) invokeReturn.getData().get(i);
                        model.setCodeName(name);
                        db.save(model);
                        if(name.equals("ZJLX")&&i==invokeReturn.getData().size()){
                            getCodeServer("MZ");
                        }
                    }
                    if(name.equals("MZ")){
                        mView.checkHotel(true,"数据下载成功");
                    }
                } else {
                    mView.checkHotel(false, "字典下载失败");
                }
            }
        });
    }
}
