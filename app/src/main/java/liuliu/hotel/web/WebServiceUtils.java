package liuliu.hotel.web;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.client.HttpResponseException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import liuliu.hotel.base.DES;

/**
 * 访问服务器 on 2015/7/8.
 */
public class WebServiceUtils {
    public static final String WEB_SERVER_URL = "http://hbdwkj.oicp.net:60007/Services/SignetService.asmx";
    public static final String MYURL = "http://10.0.3.2:8000/WebServices/LGXX/Mobile.asmx";
    public static String URL = "http://10.0.3.2:8000/WebServices/LGXX/Mobile.asmx";

    // 含有3个线程的线程池
    private static final ExecutorService executorService = Executors
            .newFixedThreadPool(3);

    // 命名空间
    private static final String NAMESPACE = "http://tempuri.org/";
    // private static String SOAP_ACTION = NAMESPACE + METHOD_NAME;

    /**
     * @param url                WebService服务器地址
     * @param methodName         WebService的调用方法名
     * @param properties         WebService的参数
     * @param webServiceCallBack 回调接口
     */
    public static void callWebService(String url, final String methodName,
                                      HashMap<String, String> properties,
                                      final WebServiceCallBack webServiceCallBack) {
        // 创建HttpTransportSE对象，传递WebService服务器地址
        final HttpTransportSE httpTransportSE = new HttpTransportSE(MYURL, 30000);
        // 创建SoapObject对象
        SoapObject soapObject = new SoapObject(NAMESPACE, methodName);

        // SoapObject添加参数
        if (properties != null) {
            for (Iterator<Map.Entry<String, String>> it = properties.entrySet()
                    .iterator(); it.hasNext(); ) {
                Map.Entry<String, String> entry = it.next();
                soapObject.addProperty(entry.getKey(), entry.getValue());
            }
        }
        // 实例化SoapSerializationEnvelope，传入WebService的SOAP协议的版本号
        final SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // 设置是否调用的是.Net开发的WebService
        soapEnvelope.setOutputSoapObject(soapObject);
        soapEnvelope.dotNet = true;
        httpTransportSE.debug = true;

        // 用于子线程与主线程通信的Handler
        final Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // 将返回值回调到callBack的参数中
                webServiceCallBack.callBack((SoapObject) msg.obj);

            }

        };

        // 开启线程去访问WebService
        executorService.submit(new Runnable() {

            @Override
            public void run() {
                SoapObject resultSoapObject = null;
                try {
                    httpTransportSE.call(NAMESPACE + methodName, soapEnvelope);

                    if (soapEnvelope.getResponse() != null) {
                        // 获取服务器响应返回的SoapObject
                        resultSoapObject = (SoapObject) soapEnvelope.bodyIn;
                    }
                } catch (HttpResponseException e) {
                    e.printStackTrace();
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();

                } finally {
                    try {
                        httpTransportSE.getConnection().disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // httpTransportSE.getConnection().disconnect();
                    // 将获取的消息利用Handler发送到主线程
                    mHandler.sendMessage(mHandler.obtainMessage(0,
                            resultSoapObject));
                }
            }
        });
    }

    /**
     * 回调7.8
     */
    public interface WebServiceCallBack {
        public void callBack(SoapObject result);
    }

    public static String SendDataToServer(String data, String methodName) {
        if (data == null || data.equals("")) {
            return "DataNull";
        }

        String returnstr = "";
        try {

            String DESStr = DES.encryptDES(data, "K I W I ", "K I W I ");

            SoapObject request = new SoapObject(NAMESPACE, methodName);

            request.addProperty("parameter", DESStr);// 添加参数和数据

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// 设置SOAP的版本号

            envelope.dotNet = true;

            envelope.bodyOut = request;

            envelope.setOutputSoapObject(request);

            int timeout = 20000;

            MyAndroidHttpTransport transport = new MyAndroidHttpTransport(URL,
                    timeout);

            transport.debug = true;
            System.out.println("dddddddddddddddddd" + NAMESPACE + methodName);
            transport.call(NAMESPACE + methodName, envelope);// 这里是发送请求并等待回复

            returnstr = DES.decryptDES(envelope.getResponse().toString(),
                    "K I W I ", "K I W I ");

            Log.v("gtrgtr", "result=========" + returnstr);
        } catch (IOException e) {// 超时异常
            // TODO Auto-generated catch block
            return "timeout";
        } catch (XmlPullParserException e) {// 发送请求异常
            // TODO Auto-generated catch block
            return "callerror";
        } catch (Exception e) {// 加密解密异常触发
            // TODO Auto-generated catch block
            return "deserror";
        }
        return returnstr;
    }


}
