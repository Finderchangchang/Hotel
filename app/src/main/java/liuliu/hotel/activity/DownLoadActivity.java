package liuliu.hotel.activity;

import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.view.View;
import android.widget.Button;

import com.ivsign.android.IDCReader.CopyFile;
import com.ivsign.android.IDCReader.Cvr100bMYTask;
import com.ivsign.android.IDCReader.Cvr100bTask;

import net.tsz.afinal.annotation.view.CodeNote;

import org.ksoap2.serialization.SoapObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.model.BlueToothModel;
import liuliu.hotel.model.CustomerModel;
import liuliu.hotel.model.DBLGInfo;
import liuliu.hotel.model.InvokeReturn;
import liuliu.hotel.model.PersonModel;
import liuliu.hotel.model.SerialNumModel;
import liuliu.hotel.utils.Utils;
import liuliu.hotel.web.DBHelper;
import liuliu.hotel.web.JSONUtils;
import liuliu.hotel.web.SoapObjectUtils;
import liuliu.hotel.web.WebServiceUtils;
import liuliu.hotel.web.XmlUtils;
import liuliu.hotel.web.globalfunc;

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
    DBHelper dbHelper;
    @CodeNote(id = R.id.login_code, click = "onClick")
    Button code;
    @CodeNote(id=R.id.login_add,click = "onClick")Button btnadd;
    @CodeNote(id=R.id.login_leave,click = "onClick")Button btnleave;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_down_load);
        // CopyFile.CopyWltlib(this);
        dbHelper = new DBHelper(finalDb, this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_down:
                HashMap<String, String> properties = new HashMap<String, String>();
                properties.put("lgdm", "1306010001");
                properties.put("BSM", "123456");
                properties.put("SJM", "123456");
                properties.put("SJH", "15911111111");
                properties.put("SJPP", "三星");
                WebServiceUtils.callWebService(WebServiceUtils.MYURL, "GetLGInfoByLGDM", properties, new WebServiceUtils.WebServiceCallBack() {

                    @Override
                    public void callBack(SoapObject result) {
                        if (null != result) {
                            InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "GetLGInfoByLGDM");
                            if (invokeReturn.isSuccess()) {
                                ToastShort("下载成功");
                                finalDb.save((DBLGInfo) invokeReturn.getData().get(0));
                            } else {
                                ToastShort("下载失败");
                            }
                        } else {
                            ToastShort("下载失败");
                        }
                        System.out.println("result==" + result);
                    }
                });


                break;
            case R.id.login_add:
                add();//入住
                break;
            case R.id.login_leave:
                Leave();//离店
                break;
            case R.id.login_bluth:
                //跳转到选择蓝牙设备页面
                Utils.IntentPost(BluetoothListActivity.class);
                break;
            case R.id.login_read:
                //读取身份证
                if (Utils.ReadString("BlueToothAddress").equals("")) {
                    ToastShort("请检查蓝牙读卡设备设置！");
                    Utils.IntentPost(BluetoothListActivity.class);//跳转登录
                } else {
                    onReadCardCvr();
                }
                break;
            case R.id.login_code:
                properties = new HashMap<String, String>();
                properties.put("lgdm", "1306010001");
                properties.put("codeName", "XZQH");//XZQH,行政区划，民族MZ,证件类型ZJLX,1男2女


                WebServiceUtils.callWebService(WebServiceUtils.MYURL, "GetCodeInfoByCodeName", properties, new WebServiceUtils.WebServiceCallBack() {

                    @Override
                    public void callBack(SoapObject result) {
                        if (null != result) {
                            System.out.println(result);
                           // JSONUtils.parse(result);
//                            InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "CodeModel");
//                            if (invokeReturn.isSuccess()) {
//                                ToastShort("下载成功");
//                                finalDb.save((DBLGInfo) invokeReturn.getData().get(0));
//                            } else {
//                                ToastShort("下载失败");
//                            }
                        } else {
                            ToastShort("下载失败");
                        }
                        System.out.println("result==" + result);
                    }
                });
                break;
        }
    }

    private void getCode() {


    }

    //离店操作
    private void Leave() {
        CustomerModel customerModel = new CustomerModel();
        customerModel.setSerialId("1306010001201605219502");//人员序列号
        customerModel.setCheckOutTime("2016-05-21 00:00:00");//离店时间
        String xml = customerModel.getLeaveXml(getAssetsFileData("checkOutNativeParameter.xml"));
        WebServiceUtils.SendDataToServer(xml, "GeneralInvoke", new WebServiceUtils.WebServiceCallBackString() {
            @Override
            public void callBack(String result) {
                System.out.println(result);
                if (!result.equals("")) {
                    InvokeReturn invokeReturn = XmlUtils.parseXml(result, "");
                    if (invokeReturn.isSuccess()) {
                        //添加成功
                        System.out.println("离店成功");
                    } else {
                        //添加失败
                        System.out.println("离店失败");
                    }
                } else {
                    System.out.println("离店失败");
                }
            }
        });

    }

    //添加入住人员
    private void add() {
        // List<DBLGInfo> list=finalDb.findAll(DBLGInfo.class);
        //globalfunc gfunc =(globalfunc)DownLoadActivity.this.getApplication();
        //if(list.size()>0) {
        //DBLGInfo dblgInfo = list.get(0);
        CustomerModel customerModel = new CustomerModel();
        customerModel.setAddress("2");
        customerModel.setCheckOutTime("2016-05-20 00:00:00");
        customerModel.setArea("");
        customerModel.setBirthday("1992-06-03");
        customerModel.setCardId("130682199206033463");
        customerModel.setCardType("11");
        customerModel.setCheckInSign(getVersionName());
        customerModel.setCheckInTime("2016-05-20 00:00:00");
        customerModel.setName("1");
        customerModel.setHeadphoto(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        final String num = dbHelper.getSeralNum();
        customerModel.setSerialId(num);
        customerModel.setRoomId("23");
        customerModel.setSex("1");
        customerModel.setNative("142429");
        customerModel.setNation("01");
        DBLGInfo dblgInfo = new DBLGInfo();
        dblgInfo.setLGDM("1306010001");
        dblgInfo.setQYSCM("A0A91-2384F-5FD17-225EA-CB717");
        String xml = customerModel.getXml(getAssetsFileData("checkInNativeParameter.xml"), true, dblgInfo);


        WebServiceUtils.SendDataToServer(xml, "GeneralInvoke", new WebServiceUtils.WebServiceCallBackString() {
            @Override
            public void callBack(String result) {
                if (!result.equals("")) {
                    System.out.println(result);
                    InvokeReturn invokeReturn = XmlUtils.parseXml(result, "");
                    if (invokeReturn.isSuccess()) {
                        //添加成功
                        System.out.println("添加成功");
                        SerialNumModel model = finalDb.findAll(SerialNumModel.class).get(0);
                        model.setSerialNum((Integer.parseInt(model.getSerialNum())+1)+"");
                        finalDb.update(model);
                    } else {
                        System.out.println("添加失败");
                        //添加失败
                    }
                }
            }
        });

    }

    @Override
    public void initEvents() {

    }

    public String getAssetsFileData(String FileName) {
        String str = "";
        try {
            InputStream is = getAssets().open(FileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            str = new String(buffer);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;

    }

    /**
     * 获取当前应用的版本号：
     */

    public String getVersionName() {
        // 获取packagemanager的实例
        String Version = "[Version:num]-[Registe:Mobile]";
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String version = packInfo.versionName;
            return Version.replace("num", version);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Version.replace("num", "1.0");
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
                                ToastShort(person.getPersonName());
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
            ToastShort("请检查蓝牙读卡设备设置！");
        }
    }
}
