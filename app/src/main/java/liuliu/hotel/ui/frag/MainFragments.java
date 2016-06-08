package liuliu.hotel.ui.frag;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.lhh.ptrrv.library.footer.loadmore.BaseLoadMoreView;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.utils.AUtils;
import net.tsz.afinal.view.NormalDialog;

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
public class MainFragments extends BaseFragment implements IFMainView {
    @CodeNote(id = R.id.main_lv)
    PullToRefreshRecyclerView main_lv;
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
    PersonAdapter mAdapter;
    private int maxPage = 1;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_mains);
    }

    @Override
    public void initEvents() {
        main_lv.setSwipeEnable(true);
        listener = new MainSearchListener(MainActivity.mInstance, this);
        main_lv.setEmptyView(View.inflate(MainActivity.mInstance, R.layout.empty_view, null));
        DemoLoadMoreView loadMoreView = new DemoLoadMoreView(MainActivity.mInstance, main_lv.getRecyclerView());
        loadMoreView.setLoadmoreString("加载更多...");
        loadMoreView.setLoadMorePadding(100);
        main_lv.setLayoutManager(new LinearLayoutManager(MainActivity.mInstance));
        main_lv.getRecyclerView().addItemDecoration(new DividerItemDecoration(MainActivity.mInstance,
                DividerItemDecoration.VERTICAL_LIST));
        View view = View.inflate(MainActivity.mInstance, R.layout.header, null);
        liveing_chart = (PieChart) view.findViewById(R.id.liveing_chart);
        live_num_tv = (TextView) view.findViewById(R.id.live_num_tv);
        add_person_btn = (Button) view.findViewById(R.id.add_person_btn);
        add_person_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.IntentPost(RegPersonActivity.class);
            }
        });
        main_lv.addHeaderView(view);
        main_lv.setLoadMoreFooter(loadMoreView);
        main_lv.setPagingableListener(new PullToRefreshRecyclerView.PagingableListener() {
            @Override
            public void onLoadMoreItems() {//加载更多
                listener.LeavePerson(++maxPage, false);
            }
        });
        main_lv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {//刷新
                maxPage = 1;
                listener.LeavePerson(maxPage, true);
            }
        });
        listener.LeavePerson(maxPage, false);
        if (modelList == null) modelList = new ArrayList<>();
        mAdapter = new PersonAdapter<CustomerModel>(modelList, R.layout.item_person) {
            @Override
            public void convert(VViewHolder holder, final CustomerModel model, int position) {
                holder.setText(R.id.num_btn, (position + 1) + "");
                holder.setText(R.id.person_name_tv, model.getName());
                holder.setCubeImage(R.id.person_iv, model.getUrl(), MainActivity.mInstance.mLoader);
                if (model.getSex().equals("2")) {
                    holder.setText(R.id.sex_tv, "女");
                }
                holder.setText(R.id.nation_tv, model.getNation());
                holder.setText(R.id.hotel_num_tv, model.getRoomId());
                holder.setText(R.id.address_tv, model.getNation());
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
        listener.LoadMain();
        AUtils.showChart(MainActivity.mInstance, 2, 120, liveing_chart, 100, 100);//显示百分比盘
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
            modelList = new ArrayList<>();
            Toast.makeText(MainActivity.mInstance, "刷新成功", Toast.LENGTH_SHORT).show();
        } else {//上划加载更多
            if (modelList == null) modelList = new ArrayList<>();//为空赋值
        }
        for (CustomerModel model : list) {
            modelList.add(model);
        }
        if (("False").equals(haveRefresh) && !isRefresh) {
            result = false;
            Toast.makeText(MainActivity.mInstance, "无更多数据", Toast.LENGTH_SHORT).show();
        }
        mAdapter.notifyDataSetChanged();
        main_lv.onFinishLoading(result, false);
    }

    /**
     * 离店操作处理结果
     *
     * @param result true,离店成功。false,离店失败
     */
    @Override
    public void LeaveHotel(boolean result) {

    }
}
