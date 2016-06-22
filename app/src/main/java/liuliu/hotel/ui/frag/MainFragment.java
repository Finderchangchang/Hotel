package liuliu.hotel.ui.frag;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;

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
import liuliu.hotel.model.DBLGInfo;
import liuliu.hotel.ui.activity.MainActivity;
import liuliu.hotel.ui.activity.PersonDetailActivity;
import liuliu.hotel.ui.activity.RegPersonActivity;
import liuliu.hotel.utils.Utils;
import liuliu.hotel.view.RyAdapter;

/**
 * 首页fragment
 * Created by Administrator on 2016/6/7.
 */
public class MainFragment extends BaseFragment implements IFMainView, RefreshListView.OnRefreshListener {
    @CodeNote(id = R.id.main_lv)
    RefreshListView main_lv;
    RyAdapter<CustomerModel> modelRyAdapter;
    MainSearchListener listener;

    TextView live_num_tv;
    PieChart liveing_chart;
    Button add_person_btn;
    TextView hotel_name_tv;
    @CodeNote(id = R.id.no_data_tv)
    TextView no_data_tv;
    NormalDialog dialog;//自定义dialog
    private static final int DEFAULT_ITEM_SIZE = 20;
    private static final int ITEM_SIZE_OFFSET = 20;

    private static final int MSG_CODE_REFRESH = 0;
    private static final int MSG_CODE_LOADMORE = 1;

    private static final int TIME = 1000;
    CommonAdapter mAdapter;
    private int maxPage = 0;
    View topView;
    View footerView;
    List<CodeModel> MZlist = new ArrayList<>();
    List<CodeModel> JGlist = new ArrayList<>();
    FinalDb db = null;
    TextView bottom_ll;
    private DisplayMetrics dis;
    int layoutId = R.layout.item_person;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_main);
        db = MainActivity.mInstance.finalDb;
    }

    @Override
    public void initEvents() {
        initFooterView();
        dis = MainActivity.mInstance.getResources().getDisplayMetrics();
        MZlist = db.findAllByWhere(CodeModel.class, "CodeName='MZ'");
        dialog = new NormalDialog(MainActivity.mInstance);//初始化dialog
        listener = new MainSearchListener(MainActivity.mInstance, this);
        initTopView();
        main_lv.addHeaderView(topView);
        if (dis.widthPixels > 700) {//适配宽屏
            hotel_name_tv.setTextSize(26);
        }
        if (dis.widthPixels > 770) {
            layoutId = R.layout.item_person_big;
        }
        if (Utils.isNetworkConnected()) {//首次进入页面
            listener.LeavePerson(++maxPage, false);
            listener.LoadMain();
        } else {//无网状态
            initNoDataView();
        }
        mAdapter = new CommonAdapter<CustomerModel>(MainActivity.mInstance, modelList, layoutId) {
            @Override
            public void convert(ViewHolder holder, final CustomerModel model, int position) {
                if (null == model.getHeadphoto()) {
                    holder.setImageResource(R.id.item_header, R.mipmap.item_default);
                } else {
                    //holder.setCubeImage(R.id.person_iv, model.getHeadphoto(), MainActivity.mInstance.mLoader);
//                    Utils.centerSquareScaleBitmap(model.getHeadphoto(), 70);
                    holder.setImageBitmap(R.id.item_header, Utils.centerSquareScaleBitmap(model.getHeadphoto(), 70));
                }
                holder.setText(R.id.person_name_tv, model.getName());

                if (model.getSex().equals("2")) {
                    holder.setText(R.id.sex_tv, "女");
                } else {
                    holder.setText(R.id.sex_tv, "男");
                }
                holder.setText(R.id.nation_tv, getCodeValuebyKey(MZlist, model.getNation()));
                holder.setText(R.id.hotel_num_tvs, model.getRoomId());
                holder.setText(R.id.item_rz_time, model.getCheckInTime());
                JGlist = db.findAllByWhere(CodeModel.class, "CodeName='XZQH' AND KEY='" + model.getCardId().substring(0, 6) + "'");
                if (JGlist != null) {
                    String address = "未知";
                    if (JGlist.size() > 0) {
                        address = JGlist.get(0).getVal();
                    }
                    holder.setText(R.id.address_tv, address);

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
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.mInstance, PersonDetailActivity.class);
                        Bitmap bitmap = model.getHeadphoto();
                        intent.putExtra("image", Utils.encodeBitmap(bitmap));
                        Bundle bundle = new Bundle();

                        model.setHeadphoto(null);
                        bundle.putSerializable(Key.Person_Detail_Model, model);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        model.setHeadphoto(bitmap);
                        bitmap = null;
                    }
                });
            }
        };
        main_lv.setAdapter(mAdapter);
        main_lv.setOnRefreshListener(this);//开启刷新
        AUtils.showChart(MainActivity.mInstance, 2, 120, liveing_chart, 100, 100);//显示百分比盘
        hotel_name_tv.setText(MainActivity.mInstance.finalDb.findAll(DBLGInfo.class).get(0).getLGMC());
    }


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
        hotel_name_tv = (TextView) topView.findViewById(R.id.hotel_name_tv);
        add_person_btn = (Button) topView.findViewById(R.id.add_person_btn);
        add_person_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.mInstance, RegPersonActivity.class);
                startActivityForResult(intent, 14);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 14 && resultCode == -1) {
            listener.LoadMain();
            maxPage = 0;
            listener.LeavePerson(++maxPage, true);
        }
    }

    /**
     * 设置底部加载内容
     */
    private void initFooterView() {
        footerView = View.inflate(MainActivity.mInstance, R.layout.item_bottom, null);
        footerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 100));
        bottom_ll = (TextView) footerView.findViewById(R.id.bottom_ll);
        bottom_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkConnected()) {//联网状态，加载页面
                    listener.LeavePerson(++maxPage, false);
                } else {
                    MainActivity.mInstance.ToastShort(Utils.getString(R.string.check_online));
                }
            }
        });
        main_lv.addFooterView(footerView);
    }

    List<CustomerModel> modelList = new ArrayList<>();//当前页面显示的

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
            if (list.size() == 0) {
                initNoDataView();
            } else {
                modelList.removeAll(modelList);
                MainActivity.mInstance.ToastShort("刷新成功");
                if (("True").equals(haveRefresh)) {
                    if (list.size() == 20) {
                        bottom_ll.setVisibility(View.VISIBLE);
                        bottom_ll.setClickable(true);
                        bottom_ll.setText("加载更多...");
                    }
                }
            }
        } else {//上划加载更多
            if (list.size() == 0) {
                initNoDataView();
            } else {
                no_data_tv.setVisibility(View.GONE);
                if (("False").equals(haveRefresh)) {
                    result = false;
                    bottom_ll.setClickable(false);
                    bottom_ll.setText("无更多数据");
                } else {
                    if (list.size() == 20) {
                        bottom_ll.setClickable(true);
                        bottom_ll.setText("加载更多...");
                    }
                }
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
            listener.LoadMain();
            listener.LeavePerson(maxPage, true);
        } else {
            Toast.makeText(MainActivity.mInstance, "离店失败！", Toast.LENGTH_SHORT);
        }
    }

    /**
     *
     */
    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Utils.isNetworkConnected()) {
                    mHandler.sendEmptyMessage(REFRESH_COMPLETE);
                } else {
                    mHandler.sendEmptyMessage(NO_ONLINE);
                }
            }
        }).start();
    }

    private final static int REFRESH_COMPLETE = 0;
    private final static int NO_ONLINE = 1;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    maxPage = 1;
                    listener.LeavePerson(maxPage, true);
                    listener.LoadMain();
                    main_lv.refreshComplete();
                    mAdapter.notifyDataSetChanged();
                    break;
                case NO_ONLINE:
                    MainActivity.mInstance.ToastShort(Utils.getString(R.string.check_online));
                    main_lv.refreshComplete();//关闭顶部下拉动画
                    //首次无网状态刷新
                    if (modelList.size() == 0) initNoDataView();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 加载底部无网络
     */
    private void initNoDataView() {
        bottom_ll.setVisibility(View.VISIBLE);
        bottom_ll.setClickable(true);
        bottom_ll.setText(Utils.getString(R.string.no_online));
        MainActivity.mInstance.ToastShort(Utils.getString(R.string.check_online));
    }
}
