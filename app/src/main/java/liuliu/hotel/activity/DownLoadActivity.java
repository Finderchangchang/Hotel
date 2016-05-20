package liuliu.hotel.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.ivsign.android.IDCReader.CopyFile;
import com.ivsign.android.IDCReader.Cvr100bMYTask;
import com.ivsign.android.IDCReader.Cvr100bTask;

import net.tsz.afinal.annotation.view.CodeNote;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.model.BlueToothModel;
import liuliu.hotel.model.PersonModel;
import liuliu.hotel.utils.Utils;
import liuliu.hotel.web.WebServiceUtils;

/**
 * Created by Administrator on 2016/5/19.
 */
public class DownLoadActivity extends BaseActivity {
    @CodeNote(id = R.id.login_down, click = "onClick")
    Button down;
    @CodeNote(id = R.id.login_bluth, click = "onClick")
    Button bluth;
    @CodeNote(id = R.id.login_read, click = "onClick")
    Button read;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_login);
       // CopyFile.CopyWltlib(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_down:
                HashMap<String, String> properties = new HashMap<String, String>();
                properties.put("lgdm", "1306010001");
                properties.put("BSM", "123456");
                WebServiceUtils.callWebService(WebServiceUtils.MYURL, "GetLGInfoByLGDM", properties, new WebServiceUtils.WebServiceCallBack() {

                    @Override
                    public void callBack(SoapObject result) {
                        if (null != result) {

                        }
                        System.out.println("result==" + result);
                    }
                });


                break;
            case R.id.login_bluth:
                Utils.IntentPost(BluetoothListActivity.class);
                break;
            case R.id.login_read:
                if (Utils.ReadString("BlueToothAddress").equals("")) {
                    ToastShort( "请检查蓝牙读卡设备设置！");
                    Utils.IntentPost(BluetoothListActivity.class);//跳转登录
                } else {
                    onReadCardCvr();
                }

                break;

        }
    }

    @Override
    public void initEvents() {

    }
    //cvr
    private void onReadCardCvr() {
        if (Utils.checkBluetooth(this, 2)) {
            final ProgressDialog progressDialog = ProgressDialog.show(this, "", "正在读取身份证信息...", true, false);
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Bundle bundle = msg.getData();
                    boolean find = false;
                   progressDialog.dismiss();
                    if (bundle != null) {
                        boolean result = bundle.getBoolean("result");
                        PersonModel person = (PersonModel) bundle.getSerializable("person_info");
                        if (result) {
                            find = true;
                           // setPerson(person);
                        } else {
                            if (null != person) {
                                find = true;
                                ToastShort( person.getPersonName());
                            }
                        }
                    }
                    if (!find) {
                        ToastShort("证件读取失败！（cvr）");
                    }
                }
            };

            new Cvr100bMYTask().startCvr100bTask(this, new Cvr100bTask.Cvr100bListener() {
                @Override
                public BlueToothModel reauestBlueDevice() {
                    BlueToothModel blue = new BlueToothModel();
                    blue.setDeviceAddress(Utils.ReadString("BlueToothAddress"));
                    return blue;
                }

                @Override
                public void onResult(boolean result, PersonModel person) {
                    Message msg = handler.obtainMessage(1);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("result", result);
                    bundle.putSerializable("person_info", person);
                    msg.setData(bundle);
                    msg.sendToTarget();
                }
            });
        } else {
            ToastShort( "请检查蓝牙读卡设备设置！");
        }
    }
}
