package liuliu.hotel.control;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;

import liuliu.hotel.model.DBTZTGInfo;
import liuliu.hotel.model.InvokeReturn;
import liuliu.hotel.web.SoapObjectUtils;
import liuliu.hotel.web.WebServiceUtils;

/**
 * Created by Administrator on 2016/6/1.
 */
public class NoticeListener {
    INoticeView mView;
    List<DBTZTGInfo> mList;
    HashMap<String, String> properties;
    public NoticeListener(INoticeView mView) {
        this.mView = mView;
    }

    /**
     * 根据指定内容进行查询
     *
     * @param word
     */
    public void searchWord(String word) {
        properties = new HashMap<String, String>();
        properties.put("lgdm", "1306010001");
        properties.put("pcs", "130601400");
        properties.put("lastGetTime", "2016-05-25");

        WebServiceUtils.callWebService(true, "GetAllUndownloadTZTGInfo", properties, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(SoapObject result) {
                if (null != result) {
                    InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "GetAllUndownloadTZTGInfo");

                    System.out.println("GetAllUndownloadTZTGInfo---" + result);
                    if (invokeReturn.isSuccess()) {
//                        mView.loadView();
                    } else {
//                        ToastShort("下载失败");
                    }
                } else {
//                    ToastShort("下载失败");
                }
                System.out.println("result==" + result);
            }
        });
        mView.loadView(mList);
    }
}
