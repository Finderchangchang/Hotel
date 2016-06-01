package liuliu.hotel.ui.frag;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.utils.AUtils;
import net.tsz.afinal.utils.CommonAdapter;
import net.tsz.afinal.utils.ViewHolder;
import net.tsz.afinal.view.NormalDialog;
import net.tsz.afinal.view.TotalListView;

import java.util.ArrayList;
import java.util.List;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseApplication;
import liuliu.hotel.base.BaseFragment;
import liuliu.hotel.config.Key;
import liuliu.hotel.config.SaveKey;
import liuliu.hotel.control.IDownHotelView;
import liuliu.hotel.control.IFMainView;
import liuliu.hotel.control.MainListener;
import liuliu.hotel.control.ReturnListView;
import liuliu.hotel.model.CustomerModel;
import liuliu.hotel.model.DBLGInfo;
import liuliu.hotel.model.PersonModel;
import liuliu.hotel.ui.activity.MainActivity;
import liuliu.hotel.ui.activity.PersonDetailActivity;
import liuliu.hotel.ui.activity.RegPersonActivity;
import liuliu.hotel.utils.Utils;

/**
 * 首页详细内容
 * Created by Administrator on 2016/5/24.
 */
public class MainFragment extends BaseFragment implements IFMainView {
    @CodeNote(id = R.id.add_person_btn, click = "onClick")
    Button add_person_btn;
    @CodeNote(id = R.id.live_lv)
    TotalListView live_lv;
    @CodeNote(id = R.id.hotel_name_tv)
    TextView hotel_name_tv;
    @CodeNote(id = R.id.live_num_tv)
    TextView live_num_tv;
    @CodeNote(id = R.id.liveing_chart)
    PieChart liveing_chart;
    CommonAdapter<CustomerModel> mAdapter;
    List<CustomerModel> mList;
    NormalDialog dialog;
    MainListener listener;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_main);
        mList = new ArrayList<>();
        listener = new MainListener(MainActivity.mInstance, this);
        listener.SearchList();
        dialog = new NormalDialog(MainActivity.mInstance);
    }

    @Override
    public void initEvents() {
        hotel_name_tv.setText(Utils.ReadString(SaveKey.KEY_Hotel_Name));
        listener.LoadMain();
        listener.SearchList();
    }

    /**
     * 加载人员信息
     */
    private void initPersons() {

        mAdapter = new CommonAdapter<CustomerModel>(MainActivity.mInstance, mList, R.layout.item_person) {
            @Override
            public void convert(ViewHolder holder, final CustomerModel model, final int position) {
                holder.setText(R.id.num_btn, (position + 1) + "");
                holder.setText(R.id.person_name_tv, model.getName());
                if (model.getSex().equals("2")) {
                    holder.setText(R.id.sex_tv, "女");
                }
                holder.setText(R.id.nation_tv, model.getNation());
                holder.setText(R.id.hotel_num_tv, model.getRoomId());
                holder.setText(R.id.address_tv,model.getAddress());
                holder.setOnClickListener(R.id.leave_hotel_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.setMiddleMessage("确定要离店？");
                        dialog.setOnPositiveListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {//确定
                                listener.MakeLeaveHotel(model.getSerialId());//执行离店操作
                                dialog.cancel();
                            }
                        });
                        dialog.setOnNegativeListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {//取消
                                dialog.cancel();
                            }
                        });
                        dialog.show();
                    }
                });
                holder.setOnClickListener(R.id.total_ll, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.IntentPost(PersonDetailActivity.class, new Utils.putListener() {
                            @Override
                            public void put(Intent intent) {
                                intent.putExtra(Key.Person_Detail_SerialId, model.getSerialId());
                            }
                        });
                    }
                });
            }
        };
        mAdapter.notifyDataSetChanged();
        live_lv.setAdapter(mAdapter);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_person_btn://点击添加按钮触发事件
                Utils.IntentPost(RegPersonActivity.class);
                break;
        }
    }


    @Override
    public void GetPersonNum(int hcount, int allhcount, int personcount) {
        live_num_tv.setText(personcount+"");
        AUtils.showChart(MainActivity.mInstance, 2, 120, liveing_chart, allhcount, hcount);//显示百分比盘
    }

    /**
     * @param list 在住人员
     */
    @Override
    public void LoadStayPerson(List<CustomerModel> list) {
        if (null != list) {
            mList = list;
            initPersons();
        }
    }

    /**
     * 离店处理结果
     *
     * @param result true,离店成功。false,离店失败
     */
    @Override
    public void LeaveHotel(boolean result) {
        if (result) {
            MainActivity.mInstance.ToastShort("离店成功");
            //刷新在住人
        } else {
            MainActivity.mInstance.ToastShort("离店失败，请重新操作！");
        }
    }
}
