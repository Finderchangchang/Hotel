package liuliu.hotel.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.view.ImageEditText;
import net.tsz.afinal.view.SpinnerDialog;

import java.util.ArrayList;
import java.util.List;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;

/**
 * 旅客入住
 * Created by Administrator on 2016/5/21.
 */
public class RegPersonActivity extends BaseActivity {
    public static RegPersonActivity mInstance;
    @CodeNote(id = R.id.login_btn, click = "onClick")
    Button login_btn;
    @CodeNote(id = R.id.user_img_iv, click = "onClick")
    ImageView user_img_iv;
    @CodeNote(id = R.id.user_name_iet)
    ImageEditText user_name_iet;
    @CodeNote(id = R.id.minzu_val_tv)
    TextView minzu_val_tv;//证件
    @CodeNote(id = R.id.xingbie_val_tv)
    TextView xingbie_val_tv;//证件
    @CodeNote(id = R.id.zhengjian_val_tv)
    TextView zhengjian_val_tv;//证件
    @CodeNote(id = R.id.minzu_ll, click = "onClick")
    LinearLayout minzu_ll;//证件
    @CodeNote(id = R.id.xingbie_ll, click = "onClick")
    LinearLayout xingbie_ll;//证件
    @CodeNote(id = R.id.zhengjian_ll, click = "onClick")
    LinearLayout zhenjian_ll;//证件
    @CodeNote(id = R.id.home_num_iet)
    ImageEditText home_num_iet;
    @CodeNote(id = R.id.idcard_iet)
    ImageEditText idcard_iet;
    @CodeNote(id = R.id.address_iet)
    ImageEditText address_iet;
    SpinnerDialog dialog;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_reg_person);
        mInstance = this;
    }

    @Override
    public void initEvents() {
        dialog = new SpinnerDialog(this);
        dialog.setCanceledOnTouchOutside(true);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                break;
            case R.id.zhengjian_ll://证件照
                dialog.setListView(getData(1));
                dialog.show();
                dialog.setOnItemClick(new SpinnerDialog.OnItemClick() {
                    @Override
                    public void onClick(int position, String val) {
                        zhengjian_val_tv.setText(val);
                    }
                });
                break;
            case R.id.xingbie_ll://性别
                dialog.setListView(getData(1));
                dialog.show();
                dialog.setOnItemClick(new SpinnerDialog.OnItemClick() {
                    @Override
                    public void onClick(int position, String val) {
                        xingbie_val_tv.setText(val);
                    }
                });
                break;
            case R.id.minzu_ll://民族
                dialog.setListView(getData(4));
                dialog.show();
                dialog.setOnItemClick(new SpinnerDialog.OnItemClick() {
                    @Override
                    public void onClick(int position, String val) {
                        minzu_val_tv.setText(val);
                    }
                });
                break;
        }
    }

    private List<String> getData(int num) {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < num; i++) {
            data.add("测试数据1");
            data.add("测试数据2");
            data.add("测试数据3");
            data.add("测试数据4");
        }
        return data;
    }
}
