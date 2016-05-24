package liuliu.hotel.ui.activity;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.IntegerRes;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.view.ImageEditText;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.utils.Utils;

/**
 * 旅客入住
 * Created by Administrator on 2016/5/21.
 */
public class RegPersonActivity extends BaseActivity {
    private PopupWindow popupWindow;
    private View view;
    @CodeNote(id = R.id.login_btn, click = "onClick")
    Button login_btn;
    @CodeNote(id = R.id.user_img_iv, click = "onClick")
    ImageView user_img_iv;
    @CodeNote(id = R.id.user_name_iet)
    ImageEditText user_name_iet;
    @CodeNote(id = R.id.minzu_val_tv)
    ImageEditText minzu_val_tv;//证件
    @CodeNote(id = R.id.xingbie_val_tv)
    ImageEditText xingbie_val_tv;//证件
    @CodeNote(id = R.id.zhengjian_val_tv)
    ImageEditText zhengjian_val_tv;//证件
    @CodeNote(id = R.id.minzu_ll, click = "onClick")
    ImageEditText minzu_ll;//证件
    @CodeNote(id = R.id.xingbie_ll, click = "onClick")
    ImageEditText xingbie_ll;//证件
    @CodeNote(id = R.id.zhengjian_ll, click = "onClick")
    ImageEditText zhenjian_ll;//证件
    @CodeNote(id = R.id.home_num_iet)
    ImageEditText home_num_iet;
    @CodeNote(id = R.id.idcard_iet)
    ImageEditText idcard_iet;
    @CodeNote(id = R.id.address_iet)
    ImageEditText address_iet;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_reg_person);
    }

    @Override
    public void initEvents() {

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                if (popupWindow == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = layoutInflater.inflate(R.layout.pop_spinner, null);
                    popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });
                }
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);//在外部点击关闭
                popupWindow.setBackgroundDrawable(new BitmapDrawable());//点击返回也可关闭，且不影响北京
                //显示的位置
                int y = (int) user_name_iet.getY();
                popupWindow.showAsDropDown(user_name_iet, 0, -y - user_name_iet.getHeight());
                break;
            case R.id.zhengjian_ll://证件照
                break;
            case R.id.xingbie_ll://性别
                break;
            case R.id.minzu_ll://民族（内容较多的跳页）
                break;
        }
    }
}
