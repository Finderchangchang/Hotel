package liuliu.hotel.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
    int pageNum = 1;
    FinalDb db;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_search);
        mList = new ArrayList<>();
        dialog = new NormalDialog(MainActivity.mInstance);
        db = MainActivity.mInstance.finalDb;
    }

    @Override
    public void initEvents() {
        start_time_et.setText(Utils.getNormalTime().substring(0, 10));
        end_time_et.setText(Utils.getNormalTime().substring(0, 10));
        center_title_tv.setText("人员查询");
        mListener = new MainSearchListener(MainActivity.mInstance, this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_btn://查询按钮
                if (checkChoices()) {
                    mListener.SearchByWord(start_time_et.getText().toString(), end_time_et.getText().toString(), house_num_et.getText().toString().trim(), pageNum, false, true);
                } else {
                    MainActivity.mInstance.ToastShort("入住起始时间不可大于结束时间");
                }
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
     * 验证前时间与后时间大小
     *
     * @return
     */
    private boolean checkChoices() {
        String t_start = start_time_et.getText().toString().trim();
        String t_end = end_time_et.getText().toString().trim();
        String[] time_start = t_start.split("-");
        String[] time_end = t_end.split("-");
        boolean result = true;
        for (int i = 0; i < time_start.length; i++) {
            if (result) {
                if (Integer.parseInt(time_start[i]) > Integer.parseInt(time_end[i])) {
                    result = false;
                }
            }
        }
        return result;
    }

    List<CodeModel> JGlist;

    @Override
    public void loadPerson(List<CustomerModel> mList) {
        if (mList.size() != 0) {
            mAdapter = new CommonAdapter<CustomerModel>(MainActivity.mInstance, mList, R.layout.item_person) {
                @Override
                public void convert(ViewHolder holder, final CustomerModel model, int position) {
                    if (("anyType{}").equals(model.getCheckOutTime())) {
                        holder.setLabView(R.id.left_top_labv, false);
                        holder.setButtonText(R.id.leave_hotel_btn, "详情");
                    } else {
                        holder.setLabView(R.id.left_top_labv, true);
                    }

                    if (null == model.getHeadphoto()) {
                        holder.setImageResource(R.id.item_header, R.mipmap.item_default);
                    } else {
                        holder.setImageBitmap(R.id.item_header, AUtils.centerSquareScaleBitmap(model.getHeadphoto(),70));
                    }
                    holder.setText(R.id.person_name_tv, model.getName());
                    holder.setCubeImage(R.id.person_iv, model.getUrl(), MainActivity.mInstance.mLoader);
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
                                dialog.setMiddleMessage("确定要离店？");
                                dialog.setOnPositiveListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {//确定
//                                    listener.MakeLeaveHotel(model.getSerialId());//执行离店操作
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
            no_data_ll.setVisibility(View.GONE);
            live_lv.setVisibility(View.VISIBLE);
        } else {
            no_data_ll.setVisibility(View.VISIBLE);
            live_lv.setVisibility(View.GONE);
        }
    }

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
