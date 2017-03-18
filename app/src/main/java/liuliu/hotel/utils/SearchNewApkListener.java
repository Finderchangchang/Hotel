package liuliu.hotel.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.gson.Gson;

import liuliu.hotel.control.IFMainView;
import liuliu.hotel.http.OkHttpUtils;
import liuliu.hotel.http.ReqCallBack;
import liuliu.hotel.model.APKVersionsModel;
import liuliu.hotel.model.ResultModel;

/**
 * 作者：zz on 2016/7/3 16:31
 */
public class SearchNewApkListener {
    private Context mContext;
    private Utils utils;
    private String versionStr;
    IFMainView view;
    public SearchNewApkListener(Context context, IFMainView v) {
        view=v;
        mContext = context;
    }
    //外部接口让主Activity调用
    public void checkApkUrl(){
        utils = new Utils(mContext);
        //ip地址
        String stringIP = utils.ReadString("IP");
        //版本号
        String stringVersions = getVersion();

        String str = "'{\"versions\":\"" + getVersion() + "\",\"file\":\"SystemOne\"}'";
        OkHttpUtils okHttpUtils = new OkHttpUtils();
        okHttpUtils.requestGetByAsyn(stringIP, "GetAPKGetAPKVersions", str, new ReqCallBack() {
            @Override
            public void onReqSuccess(String result) {
                Gson gson = new Gson();
                ResultModel model = gson.fromJson(result, ResultModel.class);

                if(model.isSuccess()){
                    APKVersionsModel apkVersionsModel = gson.fromJson(model.getData(),APKVersionsModel.class);
                    versionStr = apkVersionsModel.getVersions();
                    view.checkVersion(versionStr);
                }else{
                    view.checkVersion("");
                }
            }
            @Override
            public void onReqFailed(String errorMsg) {
                view.checkVersion("");
            }
        });
    }


    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            String version = info.versionName;
            return   version;
        } catch (Exception e) {
            e.printStackTrace();
            return "获取版本号失败";
        }
    }
}
