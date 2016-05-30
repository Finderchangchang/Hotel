package liuliu.hotel.control;

import android.content.Context;

import net.tsz.afinal.FinalDb;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

import liuliu.hotel.model.CodeModel;
import liuliu.hotel.model.CustomerModel;
import liuliu.hotel.model.DBLGInfo;
import liuliu.hotel.model.InvokeReturn;
import liuliu.hotel.utils.Utils;
import liuliu.hotel.web.SoapObjectUtils;
import liuliu.hotel.web.WebServiceUtils;

/**
 * 手机app与pc端进行绑定
 * Created by Administrator on 2016/5/19.
 */
public class DownHotelListener {
    IDownHotelView mView;
    FinalDb db;

    public DownHotelListener(IDownHotelView mView, FinalDb finalDb) {
        this.mView = mView;
        db = finalDb;
    }

    /**
     * 发送旅馆代码与随机码到后台
     *
     * @param hotelId   旅馆代码
     * @param code      随机码
     * @param imei      手机识别码
     * @param phoneNum  手机号码
     * @param phoneType 手机品牌型号
     */
    public void pushCode(String hotelId, String code, String imei, String phoneNum, String phoneType) {

        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("lgdm", "1306010001");
        properties.put("BSM", "123456");
        properties.put("SJM", "123456");
        properties.put("SJH", "15911111111");
        properties.put("SJPP", "三星");
        WebServiceUtils.callWebService(false, "GetLGInfoByLGDM", properties, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(SoapObject result) {
                if (null != result) {
                    InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "GetLGInfoByLGDM");
                    if (invokeReturn.isSuccess()) {
                        //mView.checkHotel(true,"旅馆信息下载成功");
                        db.deleteAll(DBLGInfo.class);
                        //设置密码为1

                        //？
                        db.save((DBLGInfo) invokeReturn.getData().get(0));
                        //下载字典
                        getCodeServer("");

                    } else {
                        mView.checkHotel(false,"旅馆信息下载失败");
                    }
                } else {
                    mView.checkHotel(false,"旅馆信息下载失败");
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
