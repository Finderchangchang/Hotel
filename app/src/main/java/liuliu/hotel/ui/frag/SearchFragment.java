package liuliu.hotel.ui.frag;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.model.CodeModel;
import net.tsz.afinal.utils.AUtils;
import net.tsz.afinal.utils.CommonAdapter;
import net.tsz.afinal.utils.ViewHolder;
import net.tsz.afinal.view.DatePickerDialog;
import net.tsz.afinal.view.NormalDialog;
import net.tsz.afinal.view.TotalListView;

import java.util.ArrayList;
import java.util.List;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseApplication;
import liuliu.hotel.base.BaseFragment;
import liuliu.hotel.config.Key;
import liuliu.hotel.control.IFSearchView;
import liuliu.hotel.control.MainSearchListener;
import liuliu.hotel.model.CustomerModel;
import liuliu.hotel.ui.activity.MainActivity;
import liuliu.hotel.ui.activity.PersonDetailActivity;
import liuliu.hotel.utils.Utils;

/**
 * Created by Administrator on 2016/5/24.
 */
public class SearchFragment extends BaseFragment implements IFSearchView {
    @CodeNote(id = R.id.title_name_tv)
    TextView center_title_tv;
    @CodeNote(id = R.id.start_time_et)
    EditText start_time_et;
    @CodeNote(id = R.id.end_time_et)
    EditText end_time_et;
    @CodeNote(id = R.id.house_num_et)
    EditText house_num_et;
    @CodeNote(id = R.id.search_btn, click = "onClick")
    Button search_btn;
    @CodeNote(id = R.id.live_lv)
    ListView live_lv;
    @CodeNote(id = R.id.rili_left_ll, click = "onClick")
    LinearLayout rili_left_ll;
    @CodeNote(id = R.id.rili_right_ll, click = "onClick")
    LinearLayout rili_right_ll;
    @CodeNote(id = R.id.no_data_ll)
    LinearLayout no_data_ll;
    CommonAdapter<CustomerModel> mAdapter;
    List<CustomerModel> mList;
    DatePickerDialog datePickerDialog;
    NormalDialog dialog;
    MainSearchListener mListener;
    int pageNum = 0;
    FinalDb db;
    Dialog dialogProgress;
    private DisplayMetrics dis;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_search);
        mList = new ArrayList<>();
        dialog = new NormalDialog(MainActivity.mInstance);
        db = MainActivity.mInstance.finalDb;
        dis = MainActivity.mInstance.getResources().getDisplayMetrics();
    }

    @Override
    public void initEvents() {
        start_time_et.setText(Utils.getNormalTime().substring(0, 10));
        end_time_et.setText(Utils.getNormalTime().substring(0, 10));
        center_title_tv.setText("人员查询");
        mListener = new MainSearchListener(MainActivity.mInstance, this);
        int layoutId = R.layout.item_person;
        if (dis.widthPixels > 770) {//适配宽屏
            layoutId = R.layout.item_person_big;
        }
        mAdapter = new CommonAdapter<CustomerModel>(MainActivity.mInstance, mList, layoutId) {
            @Override
            public void convert(ViewHolder holder, final CustomerModel model, final int position) {
                String time;
                if (("anyType{}").equals(model.getCheckOutTime())) {//在住
                    holder.setLabView(R.id.left_top_labv, true);
                    holder.setButtonText(R.id.leave_hotel_btn, "离店");
                } else {//离店
                    holder.setLabView(R.id.left_top_labv, false);
                    holder.setButtonText(R.id.leave_hotel_btn, "详情");
                }
                if (null == model.getHeadphoto()) {
                    holder.setImageResource(R.id.item_header, R.mipmap.item_default);
                } else {
//                    holder.setImageBitmap(R.id.item_header, model.getHeadphoto());
                    holder.setCubeBitmap(R.id.person_iv, model.getHeadphoto());
                }
                holder.setText(R.id.person_name_tv, model.getName());
//                holder.setCubeBitmap(R.id.person_iv, model.getHeadphoto());

                if (model.getSex().equals("2")) {
                    holder.setText(R.id.sex_tv, "女");
                } else {
                    holder.setText(R.id.sex_tv, "男");
                }
                List<CodeModel> code = db.findAllByWhere(CodeModel.class, "CodeName='MZ' and KEY='" + model.getNation() + "'");
                if (code != null) {
                    if (code.size() > 0) {
                        holder.setText(R.id.nation_tv, code.get(0).getVal());
                    }
                }

                holder.setText(R.id.hotel_num_tvs, model.getRoomId());
                holder.setText(R.id.item_rz_time, model.getCheckInTime());
                JGlist = db.findAllByWhere(CodeModel.class, "CodeName='XZQH' AND KEY='" + model.getNative() + "'");

                if (JGlist != null) {
                    if (JGlist.size() > 0) {
                        holder.setText(R.id.address_tv, JGlist.get(0).getVal());
                    }
                }

                holder.setOnClickListener(R.id.leave_hotel_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button btn = (Button) v;
                        if (btn.getText().equals("离店")) {
                            if (Utils.isNetworkConnected()) {
                                dialog.setMiddleMessage("确定要离店？");
                                dialog.setOnPositiveListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {//确定
                                        mListener.MakeLeaveHotel(model.getSerialId());//执行离店操作
                                        leave_position = position;//获得当前点击离店的项
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
                            } else {
                                MainActivity.mInstance.ToastShort(Utils.getString(R.string.check_online));
                            }
                        } else {
                            IntentDetails(model);
                        }

                    }
                });
                holder.setOnClickListener(R.id.total_ll, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentDetails(model);
                    }
                });
            }
        };
        live_lv.setAdapter(mAdapter);
        initFooterView();
    }

    boolean isSearch = false;//点击查询按钮标志
    String start_time = "";//查询开始时间
    String end_time = "";//查询结束时间
    String house_num = "";//房号
    int leave_position = 0;//当前点击离店的位置

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_btn://查询按钮
                pageNum = 0;
                isSearch = true;
                loadPerson(true);
                break;
            case R.id.rili_left_ll://起始时间
                datePickerDialog = new DatePickerDialog(MainActivity.mInstance, start_time_et.getText().toString());
                datePickerDialog.datePickerDialog(start_time_et);
                break;
            case R.id.rili_right_ll://结束时间
                datePickerDialog = new DatePickerDialog(MainActivity.mInstance, end_time_et.getText().toString());
                datePickerDialog.datePickerDialog(end_time_et);
                break;
        }
    }

    /**
     * @param isSearchs true,查询。
     *                  false，删除成功刷新。
     */
    private void loadPerson(boolean isSearchs) {
        if (isSearchs) {
            start_time = start_time_et.getText().toString();
            end_time = end_time_et.getText().toString();
            house_num = house_num_et.getText().toString().trim();
            dialogProgress = Utils.ProgressDialog(MainActivity.mInstance, dialog, "查询中，请稍候...", false);
        }
        //验证起始时间小于结束时间
        if (Utils.DateCompare(start_time_et.getText().toString().trim(), end_time_et.getText().toString().trim(), false)) {
            if (Utils.isNetworkConnected()) {//联网状态
                if (isSearchs) {
                    dialogProgress.show();
                }
                mListener.SearchByWord(start_time, end_time, house_num, ++pageNum, false, true);
            } else {//未联网
                MainActivity.mInstance.ToastShort(Utils.getString(R.string.check_online));
            }
        } else {
            MainActivity.mInstance.ToastShort("入住起始时间不可大于结束时间");
        }
    }

    List<CodeModel> JGlist;

    /**
     * @param val
     * @param haveLoading 还有数据可以加载
     */
    @Override
    public void loadPerson(List<CustomerModel> val, String haveLoading) {
        dialogProgress.dismiss();
        if (val.size() != 0) {
            if (isSearch) {
                mList.removeAll(mList);
                if (val.size() == 20) {
                    footerView.setVisibility(View.VISIBLE);
                }
            }
            for (CustomerModel model : val) {
                mList.add(model);
            }
            mAdapter.notifyDataSetChanged();
            no_data_ll.setVisibility(View.GONE);
            live_lv.setVisibility(View.VISIBLE);
            if (("True").equals(haveLoading)) {
                bottom_ll.setClickable(true);
                bottom_ll.setText(Utils.getString(R.string.loading_more));
            } else {
                bottom_ll.setClickable(false);
                bottom_ll.setText(Utils.getString(R.string.no_more_data));
            }
        } else {
            no_data_ll.setVisibility(View.VISIBLE);
            live_lv.setVisibility(View.GONE);
        }
    }

    /**
     * @param result       true,离店成功。false,离店失败
     * @param checkOutTime 离店时间
     */
    @Override
    public void LeaveHotel(boolean result, String checkOutTime) {
        if (result) {
            MainActivity.mInstance.ToastShort("离店成功！");
            CustomerModel model = mList.get(leave_position);
            model.setCheckOutTime(checkOutTime);//添加离店时间
            mList.remove(leave_position);
            mList.add(leave_position, model);
            mAdapter.notifyDataSetChanged();
        } else {
            MainActivity.mInstance.ToastShort("离店失败！");
        }
    }

    View footerView;
    TextView bottom_ll;

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
                if (Utils.isNetworkConnected()) {//联网状态，加载页面
                    isSearch = false;
                    loadPerson(true);
                } else {
                    MainActivity.mInstance.ToastShort(Utils.getString(R.string.check_online));
                }
            }
        });
        live_lv.addFooterView(footerView);
        footerView.setVisibility(View.GONE);
    }

    /**
     * 跳转到详细信息页面
     *
     * @param model 入住旅客详细信息model
     */
    private void IntentDetails(CustomerModel model) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.mInstance, PersonDetailActivity.class);
        intent.putExtra("image", Utils.encodeBitmap(model.getHeadphoto()));
        Bundle bundle = new Bundle();
        model.setHeadphoto(null);
        bundle.putSerializable(Key.Person_Detail_Model, model);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
