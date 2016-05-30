package liuliu.hotel.ui.frag;

import android.widget.TextView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.view.SpinnerDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseFragment;
import liuliu.hotel.config.Key;
import liuliu.hotel.ui.activity.NoticeActivity;
import liuliu.hotel.ui.activity.RevisePwdActivity;
import liuliu.hotel.ui.activity.MainActivity;
import liuliu.hotel.ui.activity.SetIpActivity;
import liuliu.hotel.utils.Utils;

/**
 * 设置
 * Created by Administrator on 2016/5/24.
 */
public class SettingFragment extends BaseFragment {
    @CodeNote(id = R.id.title_name_tv, text = "设置")
    TextView center_title_tv;
    SpinnerDialog dialog;//选择
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
    @CodeNote(id=R.id.update_zidian_wode_ll,click = "onClick")LinearLayout llCodel;
    List<String> listblue;
    List<String> listAddress;
    @CodeNote(id = R.id.wode_blueth_name)
    TextView myBlooth;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_setting);
    }

    @Override
    public void initEvents() {
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
                //蓝牙设置选择
                dialog.setListView(listblue);
                dialog.show();
                dialog.setOnItemClick(new SpinnerDialog.OnItemClick() {
                    @Override
                    public void onClick(int position, String val) {
                        myBlooth.setText(val);
                        Utils.WriteString("BlueToothName", listblue.get(position));
                        Utils.WriteString("BlueToothAddress", listAddress.get(position));
                    }
                });
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
            listblue = new ArrayList<String>();
            listAddress = new ArrayList<String>();
            for (BluetoothDevice device : pairedDevices) {
//                BlueToothModel blue = new BlueToothModel();
//                blue.setDeviceName(device.getName());
//                blue.setDeviceAddress(device.getAddress());
                listblue.add(device.getName());
                listAddress.add(device.getAddress());
                // blue_device_scale[count++] = device.getName() + "\n" + device.getAddress();
            }
        }
    }
}
