package liuliu.hotel.web;

import android.content.Context;
import android.database.SQLException;

import net.tsz.afinal.FinalDb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import liuliu.hotel.config.SaveKey;
import liuliu.hotel.model.SerialNumModel;
import liuliu.hotel.utils.Utils;

/**
 * Created by Administrator on 2016/5/21.
 */
public class DBHelper {
    public static FinalDb finalDb;
    public Context context;

    public DBHelper(FinalDb DB, Context cn) {
        finalDb = DB;
        context = cn;
    }


    //获取流水徐号
    public static String getSeralNum() {
        SerialNumModel model = new SerialNumModel();
        System.out.println("-----------------------------");
        List<SerialNumModel> list = finalDb.findAll(SerialNumModel.class);
        if (list.size() > 0) {
            System.out.println("-----------------------------" + Utils.getSystemNowDate());
            model = list.get(0);
            if (model.getLastUserDate().equals(Utils.getSystemNowDate())) {
                System.out.println(model.getSerialNum() + "---");
                model.setSerialNum((Integer.parseInt(model.getSerialNum()) + 1) + "");
            } else {
                model.setLastUserDate(Utils.getSystemNowDate());
                model.setSerialNum("9501");
                finalDb.update(model);
            }

        } else {
            System.out.println("9501-----------------------------");
            model.setSerialNum("9501");
            model.setLastUserDate(Utils.getSystemNowDate());
            finalDb.save(model);
        }
        String companyID =Utils.ReadString(SaveKey.KEY_Hotel_Id);//企业ID

        return companyID + Utils.getSystemNowDate() + model.getSerialNum();
    }

}
