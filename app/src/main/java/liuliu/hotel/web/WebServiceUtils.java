package liuliu.hotel.web;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.client.HttpResponseException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import liuliu.hotel.base.DES;
import liuliu.hotel.config.SaveKey;
import liuliu.hotel.method.key;
import liuliu.hotel.utils.Utils;

/**
 * 访问服务器 on 2016.5.24 胡海珍
 */
public class WebServiceUtils {
    public static final String WEB_SERVER_URL = "http://hbdwkj.oicp.net:60007/Services/SignetService.asmx";
    public static final String MYURL = "http://" + Utils.ReadString(SaveKey.KEY_IP) + ":" + Utils.ReadString(SaveKey.KEY_PORT) + "/WebServices/LGXX/Mobile.asmx";
    //    public static final String MYURL = "http://192.168.1.113:5005/WebServices/LGXX/Mobile.asmx";
    // 命名空间
    private static final String NAMESPACE = "http://tempuri.org/";
//    private static final String NAMESPACE = "http://www.GCHost.LGY.HubServer.com/";
    //    public static String URL = "http://10.0.3.2:8000/WebServices/LGXX/Mobile.asmx";
    // 含有3个线程的线程池
    private static final ExecutorService executorService = Executors
            .newFixedThreadPool(3);
    boolean isTrue = true;

    // private static String SOAP_ACTION = NAMESPACE + METHOD_NAME;
    public static void callWebService(final String methodName,
                                      final WebServiceCallBack webServiceCallBack) {
        callWebService(methodName, null, webServiceCallBack);
    }

    public static void callWebService(final String methodName,
                                      HashMap<String, String> properties,
                                      final WebServiceCallBack webServiceCallBack) {
        callWebService(true, methodName, null, webServiceCallBack);
    }

    /**
     * @param head               1.LGInfo,Mobile,NBInfo
     * @param methodName
     * @param webServiceCallBack
     */
    public static void getMethod(int head, final String methodName,
                                 final WebServiceCallBack webServiceCallBack) {
        getMethod(head, methodName, null, webServiceCallBack);
    }

    /**
     * @param head               WebService服务器地址(LGInfo,Mobile,NBInfo)
     * @param methodName         WebService的调用方法名
     * @param properties         WebService的参数
     * @param webServiceCallBack 回调接口
     */
    public static void getMethod(int head, final String methodName,
                                 HashMap<String, String> properties,
                                 final WebServiceCallBack webServiceCallBack) {
        String url = "http://" + key.INSTANCE.getIp() + "/WebServices/LGXX/";
        switch (head) {
            case 1:
                url += "LGInfo.asmx";
                break;
            case 2:
                url += "Mobile.asmx";
                break;
            default:
                url += "NBInfo.asmx";
                break;
        }
        final HttpTransportSE httpTransportSE = new HttpTransportSE(url, 15000);
        SoapObject soapObject = new SoapObject(NAMESPACE, methodName);
        if (properties != null) {
            for (Iterator<Map.Entry<String, String>> it = properties.entrySet()
                    .iterator(); it.hasNext(); ) {
                Map.Entry<String, String> entry = it.next();
                soapObject.addProperty(entry.getKey(), entry.getValue());
            }
        }
        final SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        soapEnvelope.setOutputSoapObject(soapObject);
        soapEnvelope.dotNet = true;
        httpTransportSE.debug = true;
        Element[] header = new Element[1];
        header[0] = new Element().createElement(NAMESPACE, "LGSoapHeader");
        Element element = new Element().createElement(NAMESPACE, "LGDM");
        element.addChild(Node.TEXT, Utils.ReadString(SaveKey.KEY_Hotel_Id));
        Element psw = new Element().createElement(NAMESPACE, "QQM");
        psw.addChild(Node.TEXT, "Kiwi:" + Utils.getQINGQIUMA());
        header[0].addChild(Node.ELEMENT, element);
        header[0].addChild(Node.ELEMENT, psw);
        soapEnvelope.headerOut = header;
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
     * @param isHearder          WebService服务器地址
     * @param methodName         WebService的调用方法名
     * @param properties         WebService的参数
     * @param webServiceCallBack 回调接口
     */
    public static void callWebService(boolean isHearder, final String methodName,
                                      HashMap<String, String> properties,
                                      final WebServiceCallBack webServiceCallBack) {
        String url = "http://" + key.INSTANCE.getIp() + "/WebServices/LGXX/LGInfo.asmx";
        final HttpTransportSE httpTransportSE = new HttpTransportSE(url, 15000);
        SoapObject soapObject = new SoapObject(NAMESPACE, methodName);
        if (properties != null) {
            for (Iterator<Map.Entry<String, String>> it = properties.entrySet()
                    .iterator(); it.hasNext(); ) {
                Map.Entry<String, String> entry = it.next();
                soapObject.addProperty(entry.getKey(), entry.getValue());
            }
        }
        final SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        soapEnvelope.setOutputSoapObject(soapObject);
        soapEnvelope.dotNet = true;
        httpTransportSE.debug = true;
        if (isHearder) {
            Element[] header = new Element[1];
            header[0] = new Element().createElement(NAMESPACE, "LGSoapHeader");
            Element element = new Element().createElement(NAMESPACE, "LGDM");
            element.addChild(Node.TEXT, Utils.ReadString(SaveKey.KEY_Hotel_Id));
            Element psw = new Element().createElement(NAMESPACE, "QQM");
            psw.addChild(Node.TEXT, "Kiwi:" + Utils.getQINGQIUMA());
            header[0].addChild(Node.ELEMENT, element);
            header[0].addChild(Node.ELEMENT, psw);
            soapEnvelope.headerOut = header;
        }
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
        void callBack(SoapObject result);
    }

    public interface WebServiceCallBackString {
        void callBack(String result);
    }

    public static void SendDataToServer(String data, final String methodName, final WebServiceCallBackString webServiceCallBack) {
//        if (data == null || data.equals("")) {
//            return "DataNull";
//        }
        String url = "http://" + key.INSTANCE.getIp() + "/WebServices/LGXX/Mobile.asmx";
        final Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // 将返回值回调到callBack的参数中
                webServiceCallBack.callBack((String) msg.obj);

            }

        };
        try {

            String DESStr = DES.encryptDES(data, "K I W I ", "K I W I ");

            final SoapObject request = new SoapObject(NAMESPACE, methodName);

            request.addProperty("parameter", DESStr);// 添加参数和数据

            final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// 设置SOAP的版本号

            envelope.dotNet = true;

            envelope.bodyOut = request;

            envelope.setOutputSoapObject(request);

            int timeout = 15000;
            final MyAndroidHttpTransport transport = new MyAndroidHttpTransport(url,
                    timeout);

            transport.debug = true;
            // 用于子线程与主线程通信的Handler

            executorService.submit(new Runnable() {
                String returnstr = "";

                @Override
                public void run() {
                    try {
                        transport.call(NAMESPACE + methodName, envelope);// 这里是发送请求并等待回复
                        if (envelope.getResponse() != null) {
                            // 获取服务器响应返回的SoapObject
                            System.out.println(envelope.getResponse().toString());
                            returnstr = DES.decryptDES(envelope.getResponse().toString(),
                                    "K I W I ", "K I W I ");
                            Log.v("gtrgtr", "result=========" + returnstr);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            transport.getConnection().disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendMessage(mHandler.obtainMessage(0,
                                returnstr));
                        // httpTransportSE.getConnection().disconnect();
                        // 将获取的消息利用Handler发送到主线程
                    }
                }
            });


        } catch (IOException e) {// 超时异常
            // TODO Auto-generated catch block
        } catch (XmlPullParserException e) {// 发送请求异常
        } catch (Exception e) {// 加密解密异常触发
            e.printStackTrace();
        }
    }


}
