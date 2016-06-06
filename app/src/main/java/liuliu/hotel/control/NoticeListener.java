package liuliu.hotel.control;

import net.tsz.afinal.FinalDb;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import liuliu.hotel.config.SaveKey;
import liuliu.hotel.model.DBTZTGInfo;
import liuliu.hotel.model.InvokeReturn;
import liuliu.hotel.utils.Utils;
import liuliu.hotel.web.SoapObjectUtils;
import liuliu.hotel.web.WebServiceUtils;

/**
 * Created by Administrator on 2016/6/1.
 */
public class NoticeListener {
    INoticeView mView;
    List<DBTZTGInfo> mList = null;
    HashMap<String, String> properties;
    FinalDb finalDb;

    public NoticeListener(INoticeView mView, FinalDb db) {
        this.mView = mView;
        finalDb = db;
    }

    /**
     * 请求下载字典信息
     */
    public void request() {
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("lgdm", "1306010010");//如果建立资源，就返回true
        properties.put("qyscm", "CDFFB-115B6-0555B-3F0C8-2F716");//DisposeServerSource(string lgdm)释放资源
        WebServiceUtils.callWebService(true, "RequestServerSource", properties, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(SoapObject result) {
                if (null != result) {
                    InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "RequestServerSource");
                    System.out.println(result);
                    if (invokeReturn.isSuccess()) {
                        searchWord("");
                    } else {
                        // mView.checkHotel(false, invokeReturn.getMessage());
                    }
                } else {
                    //mView.checkHotel(false, "网络错误！");
                }
            }
        });
    }

    /**
     * 根据指定内容进行查询
     *
     * @param word
     */
    public void searchWord(String word) {
        properties = new HashMap<String, String>();
        properties.put("lgdm", "1306010010");
        properties.put("pcs", "130601400");
        properties.put("lastGetTime", "2016-05-25");

        WebServiceUtils.callWebService(true, "GetAllUndownloadTZTGInfo", properties, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(SoapObject result) {
                if (null != result) {
                    InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "GetAllUndownloadTZTGInfo");
                    mList = new ArrayList<DBTZTGInfo>();
                    System.out.println("GetAllUndownloadTZTGInfo---" + result);
                    cancelRequest();
                    if (invokeReturn.isSuccess()) {
                        for (Object ob : invokeReturn.getData()) {
                            DBTZTGInfo info = (DBTZTGInfo) ob;
                            info.setIsRead(0);
                            // info.setFBSJ(Utils.DataTimeTO(info.getFBSJ()));
                            mList.add(info);
                            finalDb.save(info);
                        }
//                        mView.loadView();
                    } else {
//                        ToastShort("下载失败");
                    }
                } else {
//                    ToastShort("下载失败");
                }
                List<DBTZTGInfo> list = finalDb.findAllByWhere(DBTZTGInfo.class, " 1=1 order by FBSJ desc");
                if (list.size() > 0) {
                    Utils.WriteString(SaveKey.KEY_NOTICE_LASTTIME, list.get(0).getFBSJ());
                }
                mView.loadView(list);
                System.out.println("result==" + result);
            }
        });

    }

    //释放资源
    private void cancelRequest() {
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("lgdm", "1306010001");//如果建立资源，就返回true
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
