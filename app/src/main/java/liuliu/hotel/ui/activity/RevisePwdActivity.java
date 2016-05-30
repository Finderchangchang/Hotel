package liuliu.hotel.ui.activity;

import android.view.View;
import android.widget.Button;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.view.ImageEditText;

import java.util.List;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.model.DBLGInfo;
import liuliu.hotel.model.DBTZTGInfo;

/**
 * Created by Administrator on 2016/5/27.
 */
public class RevisePwdActivity extends BaseActivity {
    @CodeNote(id = R.id.old_pwd_iet)
    ImageEditText old_pwd_iet;
    @CodeNote(id = R.id.new_pwd_iet)
    ImageEditText new_pwd_iet;
    @CodeNote(id = R.id.reg_new_pwd_iet)
    ImageEditText reg_new_pwd_iet;
    @CodeNote(id = R.id.save_btn, click = "onClick")
    Button save_btn;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_revise_pwd);
        setTitleBar("修改密码");
    }

    @Override
    public void initEvents() {

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_btn:
                DBLGInfo info = finalDb.findAll(DBLGInfo.class).get(0);
                //旧密码是否正确
                if (checkText()) {
                    info.setLoginPwd(new_pwd_iet.getText().trim());
                    finalDb.update(info);
                    finish();//关闭当前页面
                }
                break;
        }
    }

    /**
     * 验证输入内容是否符合规定
     *
     * @return true, 可以执行修改操作。false，不能执行修改操作
     */
    private boolean checkText() {
        String old_pwd = old_pwd_iet.getText().toString().trim();
        String new_pwd = new_pwd_iet.getText().toString().trim();
        String reg_new_pwd = reg_new_pwd_iet.getText().toString().trim();
        if (old_pwd.equals("")) {
            ToastShort("原密码不能为空");
            return false;
        } else if (new_pwd.equals("")) {
            ToastShort("新密码不能为空");
            return false;
        } else if (reg_new_pwd.equals("")) {
            ToastShort("确认密码不能为空");
            return false;
        } else {
            String pwd = finalDb.findAll(DBLGInfo.class).get(0).getLoginPwd();
            if (!pwd.equals(old_pwd)) {
                ToastShort("原密码输入不正确，请重新输入");
                return false;
            } else {
                if (!new_pwd.equals(reg_new_pwd)) {
                    ToastShort("输入的新密码不一致，请重新输入");
                    return false;
                } else {
                    ToastShort("修改成功！");
                    return true;
                }
            }
        }
    }
}
