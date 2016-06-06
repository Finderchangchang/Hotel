package liuliu.hotel.control;

import android.content.Context;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.model.CodeModel;

import org.ksoap2.serialization.SoapObject;
import java.util.HashMap;
import java.util.List;

import liuliu.hotel.config.Key;
import liuliu.hotel.config.SaveKey;
import liuliu.hotel.model.DBLGInfo;
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
    List<DBLGInfo> myList;
    DBLGInfo info = null;
    public SettingSysListener(Context context,IDownHotelView view, FinalDb finalDb){
        mView=view;
        db=finalDb;
        myContext=context;
        dbHelper = new DBHelper(finalDb, context);
    }



    /**
     * 请求下载字典信息
     */
    public void request() {
        myList=db.findAll(DBLGInfo.class);

          if(myList.size()>0) {
              info=myList.get(0);
            HashMap<String, String> properties = new HashMap<String, String>();
            properties.put("lgdm", info.getLGDM());//如果建立资源，就返回true
            properties.put("qyscm", info.getQYSCM());//DisposeServerSource(string lgdm)释放资源
            WebServiceUtils.callWebService(true, "RequestServerSource", properties, new WebServiceUtils.WebServiceCallBack() {

                @Override
                public void callBack(SoapObject result) {

                    if (null != result) {
                        InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "RequestServerSource");
                        System.out.println(result);
                        if (invokeReturn.isSuccess()) {
                            refushCode();
                        } else {

                        }
                    } else {

                    }
                }
            });
        }else{

        }
    }

    public void refushCode(){
        HashMap<String, String>properties = new HashMap<String, String>();
        properties.put("lgdm", Utils.ReadString(SaveKey.KEY_Hotel_Id));
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

    /**
     * 下载地点信息
     *
     * @param name
     */
    private void getCodeServer(final String name) {
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("lgdm",info.getLGDM());
        properties.put("codeName", name);//民族MZ,证件类型ZJLX
        WebServiceUtils.callWebService(true, "GetCodeInfoByCodeName", properties, new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(SoapObject result) {
                        if (null != result) {
                            InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "GetCodeInfoByCodeName");
                            if (invokeReturn.isSuccess()) {
                                for (int i = 0; i < invokeReturn.getData().size(); i++) {
                                    CodeModel code = (CodeModel) invokeReturn.getData().get(i);
                                    code.setCodeName(name);
                                    db.save(code);
                                    if (i == invokeReturn.getData().size() - 1) {
                                        if (name.equals("ZJLX")) {//证件类型
                                            getCodeServer("MZ");//民族
                                        }
                                        if (name.equals("MZ")) {
                                            getCodeServer("SSXQ");//辖区
                                        }
                                        if(name.equals("SSXQ")){
                                            getCodeServer("QFJG");//籍贯
                                        }
                                    }
                                }
                            }
                            if (name.equals("QFJG")) {//籍贯

                                cancelRequest();
                            }
                        } else {
                            mView.checkHotel(false, "字典下载失败，请重新下载！");
                        }
                    }
                }

        );
    }
    private void cancelRequest() {
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("lgdm",info.getLGDM());//如果建立资源，就返回true
        WebServiceUtils.callWebService(true, "DisposeServerSource", properties, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(SoapObject result) {
                if (null != result) {
                    InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "DisposeServerSource");
                    System.out.println("DisposeServerSource" + result);
                    if (invokeReturn.isSuccess()) {
                        //ToastShort("下载成功");
                    } else {
                        //ToastShort("下载失败");
                    }
                } else {
                    //ToastShort("下载失败");
                }
            }
        });
    }
}
