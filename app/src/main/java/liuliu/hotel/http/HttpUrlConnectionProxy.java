package liuliu.hotel.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.TimeoutException;

/**
 * Created by Administrator on 2016/9/4.
 */
public class HttpUrlConnectionProxy {

    /**
     * 通过HttpURLConnection模拟post表单提交
     *
     * @param path
     * @param params 例如"{"name":"zhangsan","age"="21"}"
     * @return
     * @throws Exception
     */
    public static String sendPostRequestByForm(String path, String method, String params) {
        URL url = null;
        try {
            url = new URL("http://" + path + "/api/values?action=" + method + "&token=APP");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");// 提交模式
            conn.setConnectTimeout(10000);//连接超时 单位毫秒
            conn.setReadTimeout(2000);//读取超时 单位毫秒
            conn.setDoOutput(true);// 是否输入参数
            conn.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            byte[] bypes = params.getBytes();
            conn.getOutputStream().write(bypes);// 输入参数
            InputStream inStream = conn.getInputStream();
            return new String(readInputStream(inStream), "utf-8");
        }catch (TimeoutException e){
            e.printStackTrace();
            return "";
        } catch(MalformedURLException e) {
            e.printStackTrace();
            return "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        } catch (ProtocolException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 从输入流中读取数据
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();//网页的二进制数据
        outStream.close();
        inStream.close();
        return data;
    }
}
