package liuliu.hotel.ui.frag;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.utils.CommonAdapter;
import net.tsz.afinal.utils.ViewHolder;
import net.tsz.afinal.view.TotalListView;

import java.util.ArrayList;
import java.util.List;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseFragment;
import liuliu.hotel.config.Key;
import liuliu.hotel.model.CustomerModel;
import liuliu.hotel.model.PersonModel;
import liuliu.hotel.ui.activity.MainActivity;
import liuliu.hotel.ui.activity.PersonDetailActivity;
import liuliu.hotel.ui.activity.RegPersonActivity;
import liuliu.hotel.utils.Utils;

/**
 * 首页详细内容
 * Created by Administrator on 2016/5/24.
 */
public class MainFragment extends BaseFragment {
    @CodeNote(id = R.id.add_person_btn, click = "onClick")
    Button add_person_btn;
    @CodeNote(id = R.id.live_lv)
    TotalListView live_lv;
    CommonAdapter<CustomerModel> mAdapter;
    List<CustomerModel> mList;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_main);
        mList = new ArrayList<>();
    }

    /**
     *
     */
    private void initPerson() {
        mList.add(new CustomerModel("柳伟杰", "1", "汉族", "河北省保定市新市区茂业中心1205室", "105"));
        mList.add(new CustomerModel("柳伟杰", "1", "汉族", "河北省保定市新市区茂业中心1205室", "105"));
        mList.add(new CustomerModel("柳伟杰", "1", "汉族", "河北省保定市新市区茂业中心1205室", "105"));
        mList.add(new CustomerModel("柳伟杰", "1", "汉族", "河北省保定市新市区茂业中心1205室", "105"));
        mList.add(new CustomerModel("柳伟杰", "1", "汉族", "河北省保定市新市区茂业中心1205室", "105"));
    }

    @Override

    public void initEvents() {
        initPerson();
        mAdapter = new CommonAdapter<CustomerModel>(MainActivity.mInstance, mList, R.layout.item_person) {
            @Override
            public void convert(ViewHolder holder, CustomerModel model, int position) {
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

                    }
                });
            }
        };
        live_lv.setAdapter(mAdapter);
        live_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Utils.IntentPost(PersonDetailActivity.class, new Utils.putListener() {
                    @Override
                    public void put(Intent intent) {
                        intent.putExtra(Key.Person_Detail_SerialId, mList.get(position).getSerialId());
                    }
                });
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_person_btn://点击添加按钮触发事件
                Utils.IntentPost(RegPersonActivity.class);
                break;
        }
    }
}
