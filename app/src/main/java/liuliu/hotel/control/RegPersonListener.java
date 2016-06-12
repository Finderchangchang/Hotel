package liuliu.hotel.control;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import net.tsz.afinal.FinalDb;

import java.util.List;

import liuliu.hotel.model.CustomerModel;
import liuliu.hotel.model.DBLGInfo;
import liuliu.hotel.model.InvokeReturn;
import liuliu.hotel.model.SerialNumModel;
import liuliu.hotel.utils.Utils;
import liuliu.hotel.web.DBHelper;
import liuliu.hotel.web.WebServiceUtils;
import liuliu.hotel.web.XmlUtils;

/**
 * Created by Administrator on 2016/5/25.
 */
public class RegPersonListener {
    IDownHotelView mView;
    FinalDb db;
    DBHelper dbHelper ;
    Context myContext;
    public RegPersonListener(Context context,IDownHotelView view, FinalDb finalDb){
        mView=view;
        db=finalDb;
        myContext=context;
        dbHelper = new DBHelper(finalDb, context);
    }
    //添加入住信息
    public void addRZInfo(CustomerModel model){
        List<DBLGInfo> info=db.findAll(DBLGInfo.class);
        if(info.size()>0) {
            model.setArea(info.get(0).getSSXQ());//所属辖区

        final String num = dbHelper.getSeralNum();
        model.setSerialId(num);
        DBLGInfo dblgInfo = new DBLGInfo();
        dblgInfo.setLGDM(info.get(0).getLGDM());
        dblgInfo.setQYSCM(info.get(0).getQYSCM());

        String xml = model.getXml(Utils.getAssetsFileData(myContext,"checkInNativeParameter.xml"), true, dblgInfo);
        WebServiceUtils.SendDataToServer(xml, "GeneralInvoke", new WebServiceUtils.WebServiceCallBackString() {
            @Override
            public void callBack(String result) {
                if (!result.equals("")) {
                    InvokeReturn invokeReturn = XmlUtils.parseXml(result, "");
                    System.out.println(result);
                    if (invokeReturn.isSuccess()) {
                        //添加成功
                        mView.checkHotel(true,"添加成功");
                        SerialNumModel model = db.findAll(SerialNumModel.class).get(0);
                        model.setSerialNum((Integer.parseInt(model.getSerialNum()) + 1) + "");
                        db.update(model);
                    } else {
                        System.out.println("添加失败");
                        //添加失败
                        mView.checkHotel(true,"添加失败");
                    }
                }else{
                    mView.checkHotel(true,"添加失败");
                }
            }
        });
        }
    }


}
