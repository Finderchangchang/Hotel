package liuliu.hotel.http;

/**
 * Created by Administrator on 2016/9/4.
 */
public interface ReqCallBack {
    /**
     * 响应成功
     */
    void onReqSuccess(String result);

    /**
     * 响应失败
     */
    void onReqFailed(String errorMsg);
}
