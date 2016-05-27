package liuliu.hotel.ui.activity;

import android.view.View;
import android.widget.Button;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.view.ImageEditText;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.model.DBLGInfo;
import liuliu.hotel.model.DBTZTGInfo;

/**
 * Created by Administrator on 2016/5/27.
 */
public class ChangePasswordActivity extends BaseActivity {
    @CodeNote(id = R.id.chage_psw_old)
    ImageEditText oldPsw;
    @CodeNote(id = R.id.change_psw_new)
    ImageEditText newPsw;
    @CodeNote(id = R.id.chage_psw_newpsw)
    ImageEditText newPsw2;
    @CodeNote(id = R.id.chage_psw_save)
    Button save;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_change_password);
    }

    @Override
    public void initEvents() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBLGInfo info = finalDb.findAll(DBLGInfo.class).get(0);
                //旧密码是否正确
                if (info.getLoginPwd().equals(oldPsw.getText().trim())) {
                    //正确  修改
                    if (newPsw.getText().trim().equals(newPsw2.getText().trim())) {
                        info.setLoginPwd(newPsw.getText().trim());
                        finalDb.update(info);
                    } else {
                        ToastShort("密码不一致，请重新填写");
                    }
                } else {
                    //不正确 提示错误
                    ToastShort("密码错误");
                }


            }
        });
    }
}
