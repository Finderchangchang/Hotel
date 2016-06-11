package liuliu.hotel.control;

import android.content.Context;

import net.tsz.afinal.FinalDb;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import liuliu.hotel.config.SaveKey;
import liuliu.hotel.model.CustomerModel;
import liuliu.hotel.model.DBLGInfo;
import liuliu.hotel.model.InvokeReturn;
import liuliu.hotel.utils.Utils;
import liuliu.hotel.web.DBHelper;
import liuliu.hotel.web.SoapObjectUtils;
import liuliu.hotel.web.WebServiceUtils;
import liuliu.hotel.web.XmlUtils;

/**
 * Created by Administrator on 2016/5/30.
 */
public class MainSearchListener {
    IFMainView mMain = null;
    IFSearchView mSearch = null;
    Context myContext;

    public MainSearchListener(Context context, IFMainView view) {
        mMain = view;
        myContext = context;
    }

    public MainSearchListener(Context myContext, IFSearchView mSearch) {
        this.mSearch = mSearch;
        this.myContext = myContext;
    }

    /**
     * 在住人员列表
     *
     * @param page_num  当前页数
     * @param isRefresh 是否刷新
     */
    public void LeavePerson(int page_num, boolean isRefresh) {
        SearchByWord("2015-01-01", Utils.getNormalTime().substring(0, 10), "", page_num, isRefresh);
    }

    /**
     * 根据指定条件进行查询
     *
     * @param startTime 开始入住时间
     * @param end       结束入住时间
     * @param homeId    房间号
     * @param page_num  当前页数
     */
    public void SearchByWord(String startTime, String end, String homeId, int page_num, final boolean isRefresh) {
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("RZSJBEGIN", startTime);//入住起始时间
        properties.put("RZSJEND", end);//入住截止时间
        properties.put("FH", homeId);
        properties.put("LKZT", "0");//0在住，1离店
        properties.put("LGDM", Utils.ReadString(SaveKey.KEY_Hotel_Id));
        properties.put("YS", page_num + "");
//        mMain.LoadStayPerson(null, false, true);
        WebServiceUtils.callWebService(true, "SearchNative", properties, new WebServiceUtils.WebServiceCallBack() {
            /**
             * @param result
             */
            @Override
            public void callBack(SoapObject result) {
                System.out.println(result);
                List<CustomerModel> list = null;
                if (null != result) {
                    InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "SearchNative");
                    if (invokeReturn.isSuccess()) {
                        list = new ArrayList<CustomerModel>();
                        for (int i = 0; i < invokeReturn.getData().size(); i++) {
                            list.add((CustomerModel) invokeReturn.getData().get(i));
                        }
                        if (mMain != null) {
                            mMain.LoadStayPerson(list, isRefresh, invokeReturn.getMessage());
                        }
                        if (mSearch != null) {
                            mSearch.loadPerson(list);
                        }
                    } else {
                        if (mMain != null) {
                            mMain.LoadStayPerson(list, isRefresh, invokeReturn.getMessage());
                        }
                        if (mSearch != null) {
                            mSearch.loadPerson(null);
                        }
                    }
                } else {
                    if (mMain != null) {
                        mMain.LoadStayPerson(null, isRefresh, "False");
                    }
                    if (mSearch != null) {
                        mSearch.loadPerson(null);
                    }
                }
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
        customerModel.setSerialId(serialId);//人员序列号
        customerModel.setCheckOutTime(Utils.getNormalTime());//离店时间
        String xml = customerModel.getLeaveXml(Utils.getAssetsFileData("checkOutNativeParameter.xml"));
        WebServiceUtils.SendDataToServer(xml, "GeneralInvoke", new WebServiceUtils.WebServiceCallBackString() {
            @Override
            public void callBack(String result) {
                System.out.println(result);
                if (!result.equals("")) {
                    InvokeReturn invokeReturn = XmlUtils.parseXml(result, "");
                    if (invokeReturn.isSuccess()) {
                        mMain.LeaveHotel(true);
                    } else {
                        mMain.LeaveHotel(false);
                    }
                } else {
                    mMain.LeaveHotel(false);
                }
            }
        });
    }

    /**
     * 加载主Fragment获得百分比以及全部在住人数
     */
    public void LoadMain() {
        HashMap<String, String> p = new HashMap<String, String>();
        p.put("hotelId", Utils.ReadString(SaveKey.KEY_Hotel_Id));
        WebServiceUtils.callWebService(true, "GetRoomRate", p, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(SoapObject result) {
                if (null != result) {
                    InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "GetRoomRate");
                    System.out.println(result);
                    if (invokeReturn.isSuccess()) {
                        String[] threeNum = invokeReturn.getMessage().split(":");
                        int[] nums = new int[3];
                        for (int i = 0; i < 3; i++) {
                            nums[i] = Integer.parseInt(threeNum[i]);
                        }
                        mMain.GetPersonNum(nums[0], nums[1], nums[2]);
                    }
                }
                System.out.println("result==" + result);
            }
        });
    }
}
