package liuliu.hotel.control;

import net.tsz.afinal.FinalDb;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import liuliu.hotel.config.SaveKey;
import liuliu.hotel.model.DBLGInfo;
import liuliu.hotel.model.DBTZTGInfo;
import liuliu.hotel.model.InvokeReturn;
import liuliu.hotel.utils.Utils;
import liuliu.hotel.web.SoapObjectUtils;
import liuliu.hotel.web.WebServiceUtils;

/**
 * Created by Administrator on 2016/6/5.
 */
public class LoginListener {
    INoticeView mView;
    List<DBTZTGInfo> mList = null;
    HashMap<String, String> properties;
    FinalDb finalDb;
    List<DBLGInfo> myList;
    DBLGInfo info = null;

    public LoginListener(INoticeView mView, FinalDb db) {
        this.mView = mView;
        finalDb = db;
    }

    /**
     * 请求资源
     */
    public void request() {
        myList = finalDb.findAll(DBLGInfo.class);
        if (myList.size() > 0) {
            HashMap<String, String> properties = new HashMap<String, String>();
            properties.put("lgdm", myList.get(0).getLGDM());//如果建立资源，就返回true
            properties.put("qyscm", myList.get(0).getQYSCM());//DisposeServerSource(string lgdm)释放资源
            WebServiceUtils.callWebService(true, "RequestServerSource", properties, new WebServiceUtils.WebServiceCallBack() {

                @Override
                public void callBack(SoapObject result) {
                    mView.loadView(null);
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
        } else {
            mView.loadView(null);
        }
    }

    /**
     * 根据指定内容进行查询
     *
     * @param word
     */
    public void searchWord(String word) {

        if (myList.size() > 0) {
            info = myList.get(0);
            String time = Utils.ReadString(SaveKey.KEY_NOTICE_LASTTIME);
            System.out.println("time==" + time);
            if (time.equals("")) {
                time = "2016-01-01";
            }

            properties = new HashMap<String, String>();
            properties.put("lgdm", info.getLGDM());
            properties.put("pcs", info.getSSXQ());
            properties.put("lastGetTimes", time.replace(" ", "T"));
            final String finalTime = time;
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
                                //info.setFBSJ(Utils.DataTimeTO(info.getFBSJ()));
                                mList.add(info);
                                info.setFBSJ(info.getFBSJ().replace("/","-"));
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
                    } else {
                        Utils.WriteString(SaveKey.KEY_NOTICE_LASTTIME, finalTime);
                    }
                }
            });
        }
    }

    //释放资源
    private void cancelRequest() {
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("lgdm", info.getLGDM());//如果建立资源，就返回true
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
