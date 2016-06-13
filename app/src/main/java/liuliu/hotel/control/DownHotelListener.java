package liuliu.hotel.control;

import net.tsz.afinal.FinalDb;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

import liuliu.hotel.config.SaveKey;

import net.tsz.afinal.model.CodeModel;

import liuliu.hotel.model.DBLGInfo;
import liuliu.hotel.model.DBTZTGInfo;
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
    DBLGInfo model;

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
    public void pushCode(final String hotelId, String code, String imei, String phoneNum, String phoneType) {
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("lgdm", hotelId);
        properties.put("BSM", imei);//手机识别码
        properties.put("SJM", "123456");
        properties.put("SJH", phoneNum);
        properties.put("SJPP", phoneType);
        WebServiceUtils.callWebService(true, "GetLGInfoByLGDM", properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                if (null != result) {
                    System.out.println("下载：" + result);
                    InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "GetLGInfoByLGDM");
                    if (invokeReturn.isSuccess()) {
                        //设置密码为1
                        db.deleteAll(DBTZTGInfo.class);
                        model = (DBLGInfo) invokeReturn.getData().get(0);
                        model.setLoginPwd("1");
                        db.save(model);
                        Utils.WriteString(SaveKey.KEY_Hotel_Name, model.getLGMC());
                        Utils.WriteString(SaveKey.KEY_Hotel_Id, model.getLGDM());
                        request();
                    } else {
                        mView.checkHotel(false, "旅馆信息下载失败");
                    }
                } else {
                    mView.checkHotel(false, result + "");
                }
            }
        });
    }

    /**
     * 请求下载字典信息
     */
    private void request() {
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("lgdm", model.getLGDM());//如果建立资源，就返回true
        properties.put("qyscm", model.getQYSCM());//DisposeServerSource(string lgdm)释放资源
        WebServiceUtils.callWebService(true, "RequestServerSource", properties, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(SoapObject result) {
                if (null != result) {
                    InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "RequestServerSource");
                    System.out.println(result);
                    if (invokeReturn.isSuccess()) {
                        Utils.WriteString("CodeLastChangeTime", Utils.getNormalTime());
                        getCodeServer("ZJLX");
                    } else {
                        mView.checkHotel(false, invokeReturn.getMessage());
                    }
                } else {
                    mView.checkHotel(false, "网络错误！");
                }
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
        properties.put("lgdm", model.getLGDM());
        properties.put("codeName", name);//民族MZ,证件类型ZJLX
        WebServiceUtils.callWebService(true, "GetCodeInfoByCodeName", properties, new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(SoapObject result) {
                        if (null != result) {
                            InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "GetCodeInfoByCodeName");
                            System.out.println(result);
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
                                            // getCodeServer("SSXQ");//辖区
                                            getCodeServer("XZQH");//籍贯
                                        }
                                    }
                                }
                            } else {
                                mView.checkHotel(false, "字典下载失败，请重新下载！");
                            }
                            if (name.equals("XZQH")) {//籍贯
                                cancelRequest();
                                mView.checkHotel(true, "");

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
        properties.put("lgdm", model.getLGDM());//如果建立资源，就返回true
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
