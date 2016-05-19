package liuliu.hotel.base;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.CodeNote;

import liuliu.hotel.R;

/**
 * BaseActivity声明相关通用方法
 * <p>
 * Created by LiuWeiJie on 2015/7/22 0022.
 * Email:1031066280@qq.com
 */
public abstract class BaseActivity extends FinalActivity {
    public FinalDb finalDb;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finalDb = FinalDb.create(this, false);
        initViews();
        initEvents();
    }

    public abstract void initViews();

    public abstract void initEvents();

    public void ToastShort(String mes) {
        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show();
    }
}
