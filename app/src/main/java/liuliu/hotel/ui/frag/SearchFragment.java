package liuliu.hotel.ui.frag;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;
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
    TotalListView live_lv;
    @CodeNote(id = R.id.rili_left_ll, click = "onClick")
    LinearLayout rili_left_ll;
    @CodeNote(id = R.id.rili_right_ll, click = "onClick")
    LinearLayout rili_right_ll;
    CommonAdapter<CustomerModel> mAdapter;
    List<CustomerModel> mList;
    DatePickerDialog datePickerDialog;
    NormalDialog dialog;
    MainSearchListener mListener;
    int pageNum = 1;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_search);
        mList = new ArrayList<>();
        dialog = new NormalDialog(MainActivity.mInstance);
    }

    @Override
    public void initEvents() {
        start_time_et.setText(Utils.getNormalTime().substring(0,10));
        end_time_et.setText(Utils.getNormalTime().substring(0,10));
        center_title_tv.setText("人员查询");
        mListener = new MainSearchListener(MainActivity.mInstance, this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_btn://查询按钮
                mListener.SearchByWord(start_time_et.getText().toString(), end_time_et.getText().toString(), house_num_et.getText().toString().trim(), pageNum);
                break;
            case R.id.rili_left_ll://起始时间
                datePickerDialog = new DatePickerDialog(MainActivity.mInstance, "");
                datePickerDialog.datePickerDialog(start_time_et);
                break;
            case R.id.rili_right_ll://结束时间
                datePickerDialog = new DatePickerDialog(MainActivity.mInstance, "");
                datePickerDialog.datePickerDialog(end_time_et);
                break;
        }
    }

    @Override
    public void loadPerson(List<CustomerModel> mList) {
        if(null!=mList||mList.size()==0) {


            mAdapter = new CommonAdapter<CustomerModel>(MainActivity.mInstance, mList, R.layout.item_person) {
                @Override
                public void convert(ViewHolder holder, final CustomerModel model, final int position) {
                    holder.setText(R.id.num_btn, (position + 1) + "");
                    //图片
                    holder.setText(R.id.person_name_tv, model.getName());
                    if (model.getSex().equals("2")) {
                        holder.setText(R.id.sex_tv, "女");
                    }
                    holder.setText(R.id.nation_tv, model.getNation());
                    holder.setText(R.id.hotel_num_tv, model.getRoomId());
                    holder.setOnClickListener(R.id.leave_hotel_btn, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.setMiddleMessage("确定要离店？");
                            dialog.setOnPositiveListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {//确定
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
            live_lv.setAdapter(mAdapter);
            AUtils.setListViewHeight(live_lv);
        }else{

        }

    }
}
