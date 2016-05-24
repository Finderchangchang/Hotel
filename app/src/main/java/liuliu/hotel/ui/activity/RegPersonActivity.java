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
    @CodeNote(id = R.id.card_type_iet, click = "onClick")
    ImageEditText card_type_iet;
    private PopupWindow popupWindow;
    private View view;
    @CodeNote(id = R.id.login_btn, click = "onClick")
    Button login_btn;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_reg_person);
    }

    @Override
    public void initEvents() {

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_type_iet:
                ToastShort("pop");
                break;
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
                int y = (int) card_type_iet.getY();
                popupWindow.showAsDropDown(card_type_iet, 0, -y - card_type_iet.getHeight());
                break;
        }
    }
}
