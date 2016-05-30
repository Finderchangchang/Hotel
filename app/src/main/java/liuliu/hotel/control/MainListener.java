package liuliu.hotel.control;

import android.content.Context;

import net.tsz.afinal.FinalDb;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import liuliu.hotel.model.CustomerModel;
import liuliu.hotel.model.InvokeReturn;
import liuliu.hotel.web.DBHelper;
import liuliu.hotel.web.SoapObjectUtils;
import liuliu.hotel.web.WebServiceUtils;

/**
 * Created by Administrator on 2016/5/30.
 */
public class MainListener {
    ReturnListView mView;
    FinalDb db;
    DBHelper dbHelper;
    Context myContext;

    public MainListener(Context context, ReturnListView view, FinalDb finalDb) {
        mView = view;
        db = finalDb;
        myContext = context;
        dbHelper = new DBHelper(finalDb, context);
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
                List<CustomerModel> list=null;
                if (null != result) {
                    InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "SearchNative");
                    if (invokeReturn.isSuccess()) {
                        list=new ArrayList<CustomerModel>();
                        //ToastShort("下载成功");
                        // finalDb.deleteAll(DBLGInfo.class);
                        // finalDb.save((DBLGInfo) invokeReturn.getData().get(0));
                        for(int i=0;i<invokeReturn.getData().size();i++){
                            list.add((CustomerModel) invokeReturn.getData().get(i));
                        }
                        mView.SearchCustomer(true, list);
                    } else {
                        // ToastShort("下载失败");
                        mView.SearchCustomer(false, null);
                    }
                } else {
                    //ToastShort("下载失败");
                    mView.SearchCustomer(false, null);
                }
                System.out.println("result==" + result);
            }
        });
    }
}
