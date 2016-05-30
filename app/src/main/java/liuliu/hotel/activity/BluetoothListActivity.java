package liuliu.hotel.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.tsz.afinal.annotation.view.CodeNote;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import liuliu.hotel.base.BaseActivity;
import net.tsz.afinal.utils.CommonAdapter;
import net.tsz.afinal.utils.ViewHolder;
import liuliu.hotel.model.BlueToothModel;
import liuliu.hotel.utils.Utils;
import liuliu.hotel.R;

/**
 * 蓝牙列表
 * Created by Administrator on 2016/3/30.
 */
public class BluetoothListActivity extends BaseActivity {
    @CodeNote(id = R.id.bluth_lv)
    ListView lv;
    private String[] blue_device_scale;
    List<BlueToothModel> listblue;
    @CodeNote(id = R.id.toolbar)
    Toolbar mToolbar;

    /**
     * 加载自定义View
     */
    @Override
    public void initViews() {
        setContentView(R.layout.activity_blueth_list);
        getBluetooth();
    }

    @Override
    public void initEvents() {

        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Utils.WriteString("BlueToothAddress", listblue.get(position).getDeviceAddress());
                        Utils.WriteString("BlueToothName", listblue.get(position).getDeviceName());
                        System.out.println("name"+listblue.get(position).getDeviceName()+"address:"+listblue.get(position).getDeviceAddress());
                        Intent intent = new Intent();
                        intent.putExtra("BlueToothName", listblue.get(position).getDeviceName());
                        setResult(100, intent);
                        finish();
                    }
                }
        );
    }

    /**
     * 获得蓝牙的相关信息
     */
    private void getBluetooth() {
        BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            ToastShort( "不支持蓝牙设备！");
        } else {
            Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
            if (pairedDevices.size() == 0) {
                ToastShort("没有配对蓝牙设备！");
                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
            }
            blue_device_scale = new String[pairedDevices.size()];
            int count = 0;
            listblue = new ArrayList<BlueToothModel>();
            for (BluetoothDevice device : pairedDevices) {
                BlueToothModel blue = new BlueToothModel();
                blue.setDeviceName(device.getName());
                blue.setDeviceAddress(device.getAddress());
                listblue.add(blue);
                blue_device_scale[count++] = device.getName() + "\n" + device.getAddress();
            }
            setLv();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(100);
        finish();
    }

    private void setLv() {
        if (null != listblue) {
            lv.setAdapter(new CommonAdapter<BlueToothModel>(this, listblue, R.layout.item_bluthe) {
                @Override
                public void convert(final ViewHolder holder, final BlueToothModel blueToothModel, int position) {
                    holder.setText(R.id.blue_name, blueToothModel.getDeviceName() + ":" + blueToothModel.getDeviceAddress());
                }
            });
        } else {
            ToastShort("不支持蓝牙设备！");
        }

    }
}
