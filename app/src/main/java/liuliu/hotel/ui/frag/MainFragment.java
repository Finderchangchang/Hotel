package liuliu.hotel.ui.frag;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.utils.AUtils;
import net.tsz.afinal.utils.CommonAdapter;
import net.tsz.afinal.utils.ViewHolder;
import net.tsz.afinal.view.NormalDialog;
import net.tsz.afinal.view.PullListView;
import net.tsz.afinal.view.PullScrollView;
import net.tsz.afinal.view.TotalListView;

import java.util.ArrayList;
import java.util.List;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseFragment;
import liuliu.hotel.config.Key;
import liuliu.hotel.config.SaveKey;
import liuliu.hotel.control.IFMainView;
import liuliu.hotel.control.MainSearchListener;
import liuliu.hotel.model.CustomerModel;
import liuliu.hotel.ui.activity.MainActivity;
import liuliu.hotel.ui.activity.PersonDetailActivity;
import liuliu.hotel.ui.activity.RegPersonActivity;
import liuliu.hotel.utils.Utils;

/**
 * 首页详细内容
 * Created by Administrator on 2016/5/24.
 */
public class MainFragment extends BaseFragment implements IFMainView, PullScrollView.OnScrollChange {
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
    @CodeNote(id = R.id.no_connect_tv)
    TextView no_connect_tv;
    @CodeNote(id = R.id.title_bg_iv)
    ImageView title_bg_iv;
    @CodeNote(id = R.id.main_sv, click = "onClick")
    PullScrollView main_sv;
    @CodeNote(id = R.id.bottom_loading_more_tv)
    TextView bottom_loading_more_tv;
    CommonAdapter<CustomerModel> mAdapter;
    List<CustomerModel> mList;
    NormalDialog dialog;
    MainSearchListener listener;
    @CodeNote(id = R.id.title_shuaxin_ll)
    LinearLayout title_shuaxin_ll;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_main);
        mList = new ArrayList<>();
        listener = new MainSearchListener(MainActivity.mInstance, this);
        dialog = new NormalDialog(MainActivity.mInstance);
    }

    @Override
    public void initEvents() {
        main_sv.setHeader(title_bg_iv);
        main_sv.setOnScrollChange(this);
        hotel_name_tv.setText(Utils.ReadString(SaveKey.KEY_Hotel_Name));
        listener.LoadMain();//加载百分比数据，以及在住人员数量
//      listener.LeavePerson(1);
        AUtils.showChart(MainActivity.mInstance, 2, 120, liveing_chart, 100, 100);//显示百分比盘
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_person_btn://点击添加按钮触发事件
                Utils.IntentPost(RegPersonActivity.class);
                break;
            case R.id.main_sv:
                MainActivity.mInstance.ToastShort("what");
                break;
        }
    }


    /**
     * @param hcount      在住房间数
     * @param allhcount   全部房间数
     * @param personcount 在住总人数
     */
    @Override
    public void GetPersonNum(int hcount, int allhcount, int personcount) {
        live_num_tv.setText(personcount + "");
        AUtils.showChart(MainActivity.mInstance, 2, 120, liveing_chart, allhcount, hcount);//显示百分比盘
    }

    /**
     * @param list 在住人员
     */
    @Override
    public void LoadStayPerson(List<CustomerModel> list, boolean isRefresh, String haveLoading) {
        if (null != list) {
            no_connect_tv.setVisibility(View.GONE);
            mList = list;
            mAdapter = new CommonAdapter<CustomerModel>(MainActivity.mInstance, mList, R.layout.item_person) {
                @Override
                public void convert(ViewHolder holder, final CustomerModel model, final int position) {
//                    holder.setText(R.id.num_btn, (position + 1) + "");
                    holder.setText(R.id.person_name_tv, model.getName());
                    holder.setCubeImage(R.id.person_iv, model.getUrl(), MainActivity.mInstance.mLoader);
                    if (model.getSex().equals("2")) {
                        holder.setText(R.id.sex_tv, "女");
                    }
                    holder.setText(R.id.nation_tv, model.getNation());
                    // holder.setText(R.id.hotel_num_tv, model.getRoomId());
                    holder.setText(R.id.address_tv, model.getAddress());
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
                                    intent.putExtra(Key.Person_Detail_Model, model);
                                }
                            });
                        }
                    });
                }
            };
            mAdapter.notifyDataSetChanged();
            live_lv.setAdapter(mAdapter);
            live_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    Utils.IntentPost(PersonDetailActivity.class, new Utils.putListener() {
                        @Override
                        public void put(Intent intent) {
                            intent.putExtra(Key.Person_Detail_Model, mList.get(position));
                        }
                    });
                }
            });
            title_shuaxin_ll.setVisibility(View.GONE);
            if (isRefresh) {
                MainActivity.mInstance.ToastShort("更新成功");
            }
        } else {
            no_connect_tv.setVisibility(View.VISIBLE);
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
//            listener.LeavePerson(1);
        } else {
            MainActivity.mInstance.ToastShort("离店失败，请重新操作！");
        }
    }

    float start_y;//开始位置
    float end_y;//结束位置
    float start_sl;//scrollview起始位置

    /**
     * @param event
     */
    @Override
    public void onState(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
                start_y = event.getY();
                start_sl = main_sv.getScrollY();
                break;
            case MotionEvent.ACTION_MOVE://滚动
                if (start_sl == 0 && (event.getY() - start_y) > 100) {
                    title_shuaxin_ll.setVisibility(View.VISIBLE);
                } else {
                    title_shuaxin_ll.setVisibility(View.INVISIBLE);
                }
                break;
            case MotionEvent.ACTION_UP://抬起
                end_y = event.getY();
                if (start_sl == 0 && (end_y - start_y) > 100) {
//                    listener.LeavePerson(1);
                } else {
                    title_shuaxin_ll.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }
}
