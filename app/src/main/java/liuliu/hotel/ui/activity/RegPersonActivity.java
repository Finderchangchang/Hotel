package liuliu.hotel.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.ivsign.android.IDCReader.CopyFile;
import com.ivsign.android.IDCReader.Cvr100bMYTask;
import com.ivsign.android.IDCReader.Cvr100bTask;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.view.ImageEditText;
import net.tsz.afinal.view.SpinnerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.control.IDownHotelView;
import liuliu.hotel.control.RegPersonListener;
import liuliu.hotel.model.BlueToothModel;

import net.tsz.afinal.model.CodeModel;

import liuliu.hotel.model.CustomerModel;
import liuliu.hotel.model.DBLGInfo;
import liuliu.hotel.model.InvokeReturn;
import liuliu.hotel.model.PersonModel;
import liuliu.hotel.model.SerialNumModel;
import liuliu.hotel.utils.Utils;
import liuliu.hotel.web.DBHelper;
import liuliu.hotel.web.WebServiceUtils;
import liuliu.hotel.web.XmlUtils;

/**
 * 旅客入住
 * Created by Administrator on 2016/5/21.
 */
public class RegPersonActivity extends BaseActivity implements IDownHotelView {
    public static RegPersonActivity mInstance;
    @CodeNote(id = R.id.save_btn, click = "onClick")
    Button save_btn;
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
    List<CodeModel> xbCode;
    List<CodeModel>MZcode=new ArrayList<>();
    List<CodeModel>ZJLXcode=new ArrayList<>();
    Bitmap bm = null;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_reg_person);
        setTitleBar("旅客登记");
        mInstance = this;
        listener = new RegPersonListener(this, this, finalDb);
        customerModel = new CustomerModel();
        CopyFile.CopyWltlib(this);
    }

    @Override
    public void initEvents() {
        ZJLXcode=finalDb.findAllByWhere(CodeModel.class, "CodeName='ZJLX'");
        zhengjian_val_tv.setText(ZJLXcode.get(0).getVal());
        customerModel.setCardType(ZJLXcode.get(0).getKey());
        MZcode=finalDb.findAllByWhere(CodeModel.class, "CodeName='MZ'");
        minzu_val_tv.setText(MZcode.get(0).getVal());
        customerModel.setNation(MZcode.get(0).getKey());
        xbCode = new ArrayList<CodeModel>();
        xbCode.add(new CodeModel("1", "男"));
        xbCode.add(new CodeModel("2", "女"));
        xingbie_val_tv.setText("男");
        customerModel.setSex("1");//性别
        dialog = new SpinnerDialog(this);
        dialog.setCanceledOnTouchOutside(true);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_btn:
                if (checkInfo()) {
                    getCustomerInfo();
                    listener.addRZInfo(customerModel);
                }
                break;
            case R.id.zhengjian_ll://证件照
                dialog.setListView(ZJLXcode);
                dialog.show();
                dialog.setOnItemClick(new SpinnerDialog.OnItemClick() {
                    @Override
                    public void onClick(int position, CodeModel model) {
                        zhengjian_val_tv.setText(model.getVal());
                        customerModel.setCardType(model.getKey());
                    }
                });
                break;
            case R.id.xingbie_ll://性别
                dialog.setListView(xbCode);
                dialog.show();
                dialog.setOnItemClick(new SpinnerDialog.OnItemClick() {
                    @Override
                    public void onClick(int position, CodeModel model) {
                        xingbie_val_tv.setText(model.getVal());
                        customerModel.setSex(model.getKey());//性别
                    }
                });
                break;
            case R.id.minzu_ll://民族
                dialog.setListView(MZcode);
                dialog.show();
                dialog.setOnItemClick(new SpinnerDialog.OnItemClick() {
                    @Override
                    public void onClick(int position, CodeModel model) {
                        minzu_val_tv.setText(model.getVal());
                        customerModel.setNation(model.getKey());
                    }
                });
                break;
            case R.id.reg_person_read:
                //跳转到选择蓝牙设备页面
                //读取身份证
                if (Utils.ReadString("BlueToothAddress").equals("")) {
                    ToastShort("请检查蓝牙读卡设备设置！");
                } else {
                    onReadCardCvr();
                }
                break;
            case R.id.user_img_iv:
                startCamera(11);
                break;
        }
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
                        }   else {
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

    private boolean checkInfo() {
        if (user_name_iet.getText().trim().equals("")) {
            ToastShort("请填写姓名");
            return false;
        } else if (idcard_iet.getText().trim().equals("")) {
            ToastShort("请填写证件号");
            return false;
        } else if (home_num_iet.getText().trim().equals("")) {
            ToastShort("请填写房间号");
            return false;
        } else if (address_iet.getText().trim().equals("")) {
            ToastShort("请填写地址");
            return false;
        }
        return true;
    }

    /**
     * 从界面获取值
     */
    private void getCustomerInfo() {
        String card_num = idcard_iet.getText();
        customerModel.setName(user_name_iet.getText());
        customerModel.setCardId(idcard_iet.getText());
        //customerModel.setSex(xingbie_val_tv.getText().toString());
        customerModel.setAddress(address_iet.getText());
        //customerModel.setNation(minzu_val_tv.getText().toString());
        customerModel.setCheckInTime(Utils.getNormalTime());
        customerModel.setCheckOutTime("");
        if (card_num.length() == 15 || card_num.length() == 18) {
            customerModel.setBirthday(new StringBuffer(card_num.substring(6, 14)).insert(4, "-").insert(7, "-").toString());
            customerModel.setNative(card_num.substring(0, 6));
        }
        customerModel.setCheckInSign(Utils.getVersionName());//当前系统版本
        customerModel.setHeadphoto(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        String num = new DBHelper(finalDb, this).getSeralNum();
        customerModel.setSerialId(num);
        customerModel.setRoomId(home_num_iet.getText());
        customerModel.setCheckInSign(getVersionName());
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
        if(result) {
            setResult(-1);
            finish();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 11 && resultCode == -1) {
            bm = Utils.getimage(this, path.toString());
           // person_path = path.toString();
            if (bm != null) {
                user_img_iv.setImageBitmap(Utils.centerSquareScaleBitmap(bm, 60));
                user_img_iv.setTag(bm);
            } else {
                user_img_iv.setImageResource(R.mipmap.main_zhengjian);
                user_img_iv.setTag(bm);
            }
            //personModel.setPersonImgUrl(path.toString());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 获取当前应用的版本号：
     */
    public String getVersionName() {
        // 获取packagemanager的实例
        String Version = "[Version:num]-[Registe:Phone]";
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String version = packInfo.versionName;
            return Version.replace("num", version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return Version.replace("num", "1.0");
    }
}
