package liuliu.hotel.ui.frag;

import android.app.Dialog;
import android.widget.TextView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.model.CodeModel;
import net.tsz.afinal.view.SpinnerDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseFragment;
import liuliu.hotel.config.Key;
import liuliu.hotel.control.IDownHotelView;
import liuliu.hotel.control.SettingSysListener;
import liuliu.hotel.ui.activity.NoticeActivity;
import liuliu.hotel.ui.activity.RevisePwdActivity;
import liuliu.hotel.ui.activity.MainActivity;
import liuliu.hotel.ui.activity.SetIpActivity;
import liuliu.hotel.utils.Utils;

/**
 * 设置
 * Created by Administrator on 2016/5/24.
 */
public class SettingFragment extends BaseFragment implements IDownHotelView {
    @CodeNote(id = R.id.title_name_tv)
    TextView center_title_tv;
    SpinnerDialog Contentdialog;//选择
    @CodeNote(id = R.id.tongzhi_wode_ll, click = "onClick")
    LinearLayout tongzhi;
    @CodeNote(id = R.id.update_sys_wode_ll, click = "onClick")
    LinearLayout update;
    @CodeNote(id = R.id.update_mima_wode_ll, click = "onClick")
    LinearLayout changepsw;
    @CodeNote(id = R.id.sys_setting_wode_ll, click = "onClick")
    LinearLayout setting;
    @CodeNote(id = R.id.lanya_setting_wode_ll, click = "onClick")
    LinearLayout blooth;
    @CodeNote(id = R.id.update_zidian_wode_ll, click = "onClick")
    LinearLayout llCodel;
    List<CodeModel> listblue;
    List<String> listAddress;
    @CodeNote(id = R.id.wode_blueth_name)
    TextView myBlooth;
    SettingSysListener listener;
    Dialog dialog;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_setting);
        listener = new SettingSysListener(MainActivity.mInstance, this, MainActivity.mInstance.finalDb);
    }

    @Override
    public void initEvents() {
        Contentdialog = new SpinnerDialog(MainActivity.mInstance);
        center_title_tv.setText("设置");
        dialog = new SpinnerDialog(MainActivity.mInstance);
        dialog.setCanceledOnTouchOutside(true);
        myBlooth.setText(Utils.ReadString("BlueToothName"));
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_mima_wode_ll:
                Utils.IntentPost(RevisePwdActivity.class);
                break;
            case R.id.update_zidian_wode_ll:
                //更新字典
                dialog = Utils.ProgressDialog(MainActivity.mInstance, dialog, "更新字典中，请稍候...", false);
                dialog.show();
                listener.request();
                break;
            case R.id.tongzhi_wode_ll:
                Utils.IntentPost(NoticeActivity.class);
                break;
            case R.id.update_sys_wode_ll:
                break;
            case R.id.sys_setting_wode_ll:
                Utils.IntentPost(SetIpActivity.class, new Utils.putListener() {
                    @Override
                    public void put(Intent intent) {
                        intent.putExtra(Key.Reg_IP_Port, "setting");//跳转到设置ip端口页面
                    }
                });
                break;
            case R.id.lanya_setting_wode_ll:
                getBluetooth();
                break;
        }
    }

    /**
     * 获得蓝牙的相关信息
     */
    private void getBluetooth() {
        BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            MainActivity.mInstance.ToastShort("不支持蓝牙设备！");
        } else {
            Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
            if (pairedDevices.size() == 0) {
                MainActivity.mInstance.ToastShort("没有配对蓝牙设备！");
                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
            }
            // blue_device_scale = new String[pairedDevices.size()];
            int count = 0;
            listblue = new ArrayList<CodeModel>();
            listAddress = new ArrayList<String>();
            for (BluetoothDevice device : pairedDevices) {
//                BlueToothModel blue = new BlueToothModel();
//                blue.setDeviceName(device.getName());
//                blue.setDeviceAddress(device.getAddress());
                listblue.add(new CodeModel("", device.getName()));
                listAddress.add(device.getAddress());
                // blue_device_scale[count++] = device.getName() + "\n" + device.getAddress();
            }
            //蓝牙设置选择
            // MainActivity.mInstance.ToastShort("list size:"+listblue.size());
            if(listblue.size()>0) {
                Contentdialog.setListView(listblue);
                Contentdialog.show();
                Contentdialog.setOnItemClick(new SpinnerDialog.OnItemClick() {
                    @Override
                    public void onClick(int position, CodeModel val) {
                        myBlooth.setText(val.getVal());
                        Utils.WriteString("BlueToothName", listblue.get(position).getVal());
                        Utils.WriteString("BlueToothAddress", listAddress.get(position));
                    }
                });
            }
        }
    }

    @Override
    public void checkHotel(boolean result, String mes) {
        dialog.dismiss();
        MainActivity.mInstance.ToastShort(mes);
    }
}
