package liuliu.hotel.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ivsign.android.IDCReader.Cvr100bMYTask;
import com.ivsign.android.IDCReader.Cvr100bTask;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.view.ImageEditText;
import net.tsz.afinal.view.SpinnerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import liuliu.hotel.R;
import liuliu.hotel.activity.BluetoothListActivity;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.control.IDownHotelView;
import liuliu.hotel.control.RegPersonListener;
import liuliu.hotel.model.BlueToothModel;
import liuliu.hotel.model.CodeModel;
import liuliu.hotel.model.CustomerModel;
import liuliu.hotel.model.PersonModel;
import liuliu.hotel.utils.Utils;

/**
 * 旅客入住
 * Created by Administrator on 2016/5/21.
 */
public class RegPersonActivity extends BaseActivity implements IDownHotelView {
    public static RegPersonActivity mInstance;
    @CodeNote(id = R.id.login_btn, click = "onClick")
    Button login_btn;
    @CodeNote(id = R.id.user_img_iv, click = "onClick")
    ImageView user_img_iv;
    @CodeNote(id = R.id.user_name_iet)
    ImageEditText user_name_iet;//姓名
    @CodeNote(id = R.id.minzu_val_tv)
    TextView minzu_val_tv;//民族
    @CodeNote(id = R.id.xingbie_val_tv)
    TextView xingbie_val_tv;//性别
    @CodeNote(id = R.id.zhengjian_val_tv)
    TextView zhengjian_val_tv;//证件类型
    @CodeNote(id = R.id.minzu_ll, click = "onClick")
    LinearLayout minzu_ll;//证件
    @CodeNote(id = R.id.xingbie_ll, click = "onClick")
    LinearLayout xingbie_ll;//证件
    @CodeNote(id = R.id.zhengjian_ll, click = "onClick")
    LinearLayout zhenjian_ll;//证件
    @CodeNote(id = R.id.home_num_iet)
    ImageEditText home_num_iet;//房间号
    @CodeNote(id = R.id.idcard_iet)
    ImageEditText idcard_iet;//证件号
    @CodeNote(id = R.id.address_iet)
    ImageEditText address_iet;//地址
    SpinnerDialog dialog;
    @CodeNote(id = R.id.reg_person_read, click = "onClick")
    Button btnRead;
    StringBuffer path = new StringBuffer();
    RegPersonListener listener;
    CustomerModel customerModel;
List<CodeModel> MZcode=new ArrayList<>();
    List<CodeModel>XBcode=new ArrayList<>();
    @Override
    public void initViews() {
        setContentView(R.layout.activity_reg_person);
        mInstance = this;
        listener = new RegPersonListener(this, this, finalDb);
        customerModel = new CustomerModel();
    }

    @Override
    public void initEvents() {
        dialog = new SpinnerDialog(this);
        dialog.setCanceledOnTouchOutside(true);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                if(checkInfo()) {
                    getCustomerInfo();
                    listener.addRZInfo(customerModel);
                }
                break;
            case R.id.zhengjian_ll://证件照
                dialog.setListView(getData(1));
                dialog.show();
                dialog.setOnItemClick(new SpinnerDialog.OnItemClick() {
                    @Override
                    public void onClick(int position, String val) {
                        zhengjian_val_tv.setText(val);
                    }
                });
                break;
            case R.id.xingbie_ll://性别
                dialog.setListView(getData(1));
                dialog.show();
                dialog.setOnItemClick(new SpinnerDialog.OnItemClick() {
                    @Override
                    public void onClick(int position, String val) {
                        xingbie_val_tv.setText(val);
                    }
                });
                break;
            case R.id.minzu_ll://民族
                dialog.setListView(getData(4));
                dialog.show();
                dialog.setOnItemClick(new SpinnerDialog.OnItemClick() {
                    @Override
                    public void onClick(int position, String val) {
                        minzu_val_tv.setText(val);
                    }
                });
                break;
            case R.id.reg_person_read:
                //跳转到选择蓝牙设备页面
                //读取身份证
                if (Utils.ReadString("BlueToothAddress").equals("")) {
                    ToastShort("请检查蓝牙读卡设备设置！");
                    Utils.IntentPost(BluetoothListActivity.class);//跳转登录
                } else {
                    onReadCardCvr();
                }
                break;
        }
    }

    private List<String> getData(int num) {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < num; i++) {
            data.add("测试数据1");
            data.add("测试数据2");
            data.add("测试数据3");
            data.add("测试数据4");
        }
        return data;
    }
    //开启拍照
    public void startCamera(int type) {
        // 利用系统自带的相机应用:拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        path = new StringBuffer();
        path.append(RegPersonActivity.this.getExternalFilesDir(null)).append("/header.jpg");
        File file = new File(path.toString());
        Uri uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, type);
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
                            setPerson(person);
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
private boolean checkInfo(){
    if(user_name_iet.getText().trim().equals("")){
        ToastShort("请填写姓名");
        return false;
    }else if(idcard_iet.getText().trim().equals("")){
        ToastShort("请填写证件号");
        return false;
    }else if(home_num_iet.getText().trim().equals("")){
        ToastShort("请填写房间号");
        return false;
    }else if(address_iet.getText().trim().equals("")){
        ToastShort("请填写地址");
        return false;
    }
    return true;
}
    //从界面获取值
    private void getCustomerInfo() {
        customerModel.setName(user_name_iet.getText());
        customerModel.setCardId(idcard_iet.getText());
        customerModel.setSex("");
        customerModel.setAddress(address_iet.getText());
        customerModel.setNation("");
        customerModel.setNative("");
    }

    //读卡以后界面赋值
    private void setPerson(PersonModel person) {
        user_name_iet.setText(person.getPersonName());
        idcard_iet.setText(person.getPersonCardId());
        xingbie_val_tv.setText("");
        address_iet.setText(person.getPersonAddress());
        minzu_val_tv.setText(person.getPersonNation());
        customerModel.setBirthday(person.getPersonBirthday());
        if (person.getPersonCardImage() != null) {
            if (!person.getPersonCardImage().equals("")) {
                Bitmap bitmap = Utils.getBitmapByte(person.getPersonCardImage());
                user_img_iv.setImageBitmap(bitmap);
                customerModel.setUrl(person.getPersonImgUrl());
            }
        } else {
          ToastShort("照片解码失败！");
        }
    }

    @Override
    public void checkHotel(boolean result, String mes) {
        ToastShort(mes);
    }
}
