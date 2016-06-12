package liuliu.hotel.ui.frag;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.lhh.ptrrv.library.footer.loadmore.BaseLoadMoreView;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.model.CodeModel;
import net.tsz.afinal.utils.AUtils;
import net.tsz.afinal.utils.CommonAdapter;
import net.tsz.afinal.utils.ViewHolder;
import net.tsz.afinal.view.NormalDialog;
import net.tsz.afinal.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseFragment;
import liuliu.hotel.config.Key;
import liuliu.hotel.control.IFMainView;
import liuliu.hotel.control.MainSearchListener;
import liuliu.hotel.model.CustomerModel;
import liuliu.hotel.ui.activity.MainActivity;
import liuliu.hotel.ui.activity.PersonDetailActivity;
import liuliu.hotel.ui.activity.RegPersonActivity;
import liuliu.hotel.utils.Utils;
import liuliu.hotel.view.DemoLoadMoreView;
import liuliu.hotel.view.DividerItemDecoration;
import liuliu.hotel.view.PersonAdapter;
import liuliu.hotel.view.PtrrvBaseAdapter;
import liuliu.hotel.view.RecycleBaseAdapter;
import liuliu.hotel.view.RyAdapter;
import liuliu.hotel.view.VViewHolder;

/**
 * 首页fragment
 * Created by Administrator on 2016/6/7.
 */
public class MainFragments extends BaseFragment implements IFMainView, RefreshListView.OnRefreshListener {
    @CodeNote(id = R.id.main_lv)
    RefreshListView main_lv;
    RyAdapter<CustomerModel> modelRyAdapter;
    MainSearchListener listener;

    TextView live_num_tv;
    PieChart liveing_chart;
    Button add_person_btn;

    NormalDialog dialog;//自定义dialog
    private static final int DEFAULT_ITEM_SIZE = 20;
    private static final int ITEM_SIZE_OFFSET = 20;

    private static final int MSG_CODE_REFRESH = 0;
    private static final int MSG_CODE_LOADMORE = 1;

    private static final int TIME = 1000;
    CommonAdapter mAdapter;
    private int maxPage = 1;
    View topView;
    View footerView;
    List<CodeModel> MZlist = new ArrayList<>();
    List<CodeModel> JGlist = new ArrayList<>();
    FinalDb db = null;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_mains);
        db = MainActivity.mInstance.finalDb;
    }

    @Override
    public void initEvents() {
        MZlist = db.findAllByWhere(CodeModel.class, "CodeName='MZ'");
        dialog = new NormalDialog(MainActivity.mInstance);
        listener = new MainSearchListener(MainActivity.mInstance, this);
        initTopView();
        main_lv.addHeaderView(topView);
        listener.LeavePerson(maxPage, false);
        if (modelList == null) modelList = new ArrayList<>();
        mAdapter = new CommonAdapter<CustomerModel>(MainActivity.mInstance, modelList, R.layout.item_person) {
            @Override
            public void convert(ViewHolder holder, final CustomerModel model, int position) {
                if (null == model.getHeadphoto()) {
                    holder.setImageResource(R.id.item_header, R.mipmap.item_default);
                } else {
                    holder.setImageBitmap(R.id.item_header, model.getHeadphoto());
                }
                holder.setText(R.id.num_btn, (position + 1) + "");
                holder.setText(R.id.person_name_tv, model.getName());
                holder.setCubeImage(R.id.person_iv, model.getUrl(), MainActivity.mInstance.mLoader);
                if (model.getSex().equals("2")) {
                    holder.setText(R.id.sex_tv, "女");
                } else {
                    holder.setText(R.id.sex_tv, "男");
                }
                holder.setText(R.id.nation_tv, getCodeValuebyKey(MZlist, model.getNation()));
                holder.setText(R.id.hotel_num_tvs, model.getRoomId());
                holder.setText(R.id.item_rz_time, model.getCheckInTime());
                JGlist = db.findAllByWhere(CodeModel.class, "CodeName='XZQH' AND KEY='" + model.getNative() + "'");

                if (JGlist != null) {
                    holder.setText(R.id.address_tv, JGlist.get(0).getVal());
                }

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
        main_lv.setAdapter(mAdapter);
        main_lv.setOnRefreshListener(this);
        listener.LoadMain();
        AUtils.showChart(MainActivity.mInstance, 2, 120, liveing_chart, 100, 100);//显示百分比盘
    }

    TextView bottom_ll;

    private String getCodeValuebyKey(List<CodeModel> list, String key) {
        for (CodeModel code : list) {
            if (code.getKey().equals(key)) {
                return code.getVal();
            }
        }
        return "";
    }

    /**
     *
     */
    private void initTopView() {
        topView = View.inflate(MainActivity.mInstance, R.layout.header, null);
        liveing_chart = (PieChart) topView.findViewById(R.id.liveing_chart);
        live_num_tv = (TextView) topView.findViewById(R.id.live_num_tv);
        add_person_btn = (Button) topView.findViewById(R.id.add_person_btn);
        add_person_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.IntentPost(RegPersonActivity.class);
            }
        });
    }

    /**
     * 设置底部加载内容
     */
    private void initFooterView() {
        footerView = View.inflate(MainActivity.mInstance, R.layout.item_bottom, null);
        footerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 70));
        bottom_ll = (TextView) footerView.findViewById(R.id.bottom_ll);
        bottom_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.LeavePerson(++maxPage, false);
            }
        });
        main_lv.addFooterView(footerView);
    }

    List<CustomerModel> modelList;//当前页面显示的

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
     * @param list        在住人员
     * @param isRefresh   是否为下拉刷新(false为上划加载更多)
     * @param haveRefresh 是否还有数据
     */
    @Override
    public void LoadStayPerson(List<CustomerModel> list, boolean isRefresh, String haveRefresh) {
        boolean result = true;
        if (isRefresh) {//下拉刷新
            modelList.removeAll(modelList);
            Toast.makeText(MainActivity.mInstance, "刷新成功", Toast.LENGTH_SHORT).show();
        } else {//上划加载更多
            if (modelList == null) modelList = new ArrayList<>();//为空赋值
            if (("False").equals(haveRefresh)) {
                result = false;
                bottom_ll.setClickable(false);
                bottom_ll.setText("无更多数据");
            } else {
                initFooterView();
                bottom_ll.setClickable(true);
                bottom_ll.setText("加载更多...");

            }
        }
        for (CustomerModel model : list) {
            modelList.add(model);
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 离店操作处理结果
     *
     * @param result true,离店成功。false,离店失败
     */
    @Override
    public void LeaveHotel(boolean result) {
        if (result) {
            Toast.makeText(MainActivity.mInstance, "离店成功！", Toast.LENGTH_SHORT);
            mAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(MainActivity.mInstance, "离店失败！", Toast.LENGTH_SHORT);
        }

    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    mHandler.sendEmptyMessage(REFRESH_COMPLETE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private final static int REFRESH_COMPLETE = 0;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    maxPage = 1;
                    listener.LeavePerson(maxPage, true);
                    main_lv.refreshComplete();
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
}
