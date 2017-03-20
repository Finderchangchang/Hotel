package liuliu.hotel.http;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/9/4.
 */
public class OkHttpUtils {
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("application/json; charset=UTF-8");
    // String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJDb21wYW55SUQiOiIxMzAxMzMwMDAyIiwiQXV0aG9yaXplZFR5cGUiOiJBUFAiLCJFeHBpcmF0aW9uVGltZSI6IjIwMTctMDktMjggMDA6MDA6MDAifQ.h-30WeTD2W9dVKjNxUq2sgnouDo77UY6qZH13hsQjAM";
    String token = "";

    public OkHttpUtils() {

    }

    public <T> Call requestGetByAsyn(String port, String method, String json, final ReqCallBack callBack) {
        String requestUrl = String.format("http://%s/api/values?action=%s&pattern=APP&token=" + token, port, method);
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, json);
        Request request = new Request.Builder().url(requestUrl).post(body).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                callBack.onReqFailed("网络错误");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    callBack.onReqSuccess(str);
                } else {
                    callBack.onReqFailed("网络错误");
                }
            }
        });
        return call;
    }
}
