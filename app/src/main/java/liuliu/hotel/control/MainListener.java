package liuliu.hotel.control;

import android.content.Context;

import net.tsz.afinal.FinalDb;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import liuliu.hotel.model.CustomerModel;
import liuliu.hotel.model.InvokeReturn;
import liuliu.hotel.utils.Utils;
import liuliu.hotel.web.DBHelper;
import liuliu.hotel.web.SoapObjectUtils;
import liuliu.hotel.web.WebServiceUtils;
import liuliu.hotel.web.XmlUtils;

/**
 * Created by Administrator on 2016/5/30.
 */
public class MainListener {
    IFMainView mView;
    Context myContext;

    public MainListener(Context context, IFMainView view) {
        mView = view;
        myContext = context;
    }

    public void SearchList() {
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("RZSJBEGIN", "2015-01-01");//入住起始时间
        properties.put("RZSJEND", "2016-05-25");//入住截止时间
        properties.put("FH", "");
        properties.put("LKZT", "0");//0在住，1离店
        properties.put("LGDM", "1306010001");
        properties.put("YS", "1");
        WebServiceUtils.callWebService(true, "SearchNative", properties, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(SoapObject result) {
                List<CustomerModel> list = null;
                if (null != result) {
                    InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "SearchNative");
                    if (invokeReturn.isSuccess()) {
                        list = new ArrayList<CustomerModel>();
                        for (int i = 0; i < invokeReturn.getData().size(); i++) {
                            list.add((CustomerModel) invokeReturn.getData().get(i));
                        }
                        mView.LoadStayPerson(list);
                    } else {
                        mView.LoadStayPerson(null);
                    }
                } else {
                    mView.LoadStayPerson(null);
                }
                System.out.println("result==" + result);
            }
        });
    }

    /**
     * 执行离店操作
     *
     * @param serialId 流水号
     */
    public void MakeLeaveHotel(String serialId) {
        CustomerModel customerModel = new CustomerModel();
        customerModel.setSerialId("1306010001201605219502");//人员序列号
        customerModel.setCheckOutTime(Utils.getNormalTime());//离店时间
        String xml = customerModel.getLeaveXml(Utils.getAssetsFileData("checkOutNativeParameter.xml"));
        WebServiceUtils.SendDataToServer(xml, "GeneralInvoke", new WebServiceUtils.WebServiceCallBackString() {
            @Override
            public void callBack(String result) {
                System.out.println(result);
                if (!result.equals("")) {
                    InvokeReturn invokeReturn = XmlUtils.parseXml(result, "");
                    if (invokeReturn.isSuccess()) {
                        mView.LeaveHotel(true);
                    } else {
                        mView.LeaveHotel(false);
                    }
                } else {
                    mView.LeaveHotel(false);
                }
            }
        });
    }

    /**
     * 加载主Fragment获得百分比以及全部在住人数
     */
    public void LoadMain() {
        mView.GetPersonNum("75%", 37);
    }
}
