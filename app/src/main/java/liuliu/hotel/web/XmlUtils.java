package liuliu.hotel.web;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import liuliu.hotel.model.InvokeReturn;

/**
 * Created by Administrator on 2016/5/21.
 */
public class XmlUtils {
    public static InvokeReturn parseXml(String result, String modelName) {
        InvokeReturn invokeReturn = new InvokeReturn();
        Object objectModel = null;
        List<Object> objectList = new ArrayList<Object>();
        Field[] fields = null;
        InputStream inputStream = new ByteArrayInputStream(
                result.getBytes());
        XmlPullParser parser = Xml.newPullParser(); //由android.util.Xml创建一个XmlPullParser实例
        //设置输入流 并指明编码方式
        int eventType = 0;
        try {
            parser.setInput(inputStream, "UTF-8");
            eventType = parser.getEventType();

            String attribute = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (parser.getAttributeCount() > 0) {
                    attribute = parser.getAttributeValue(0).toString();
                }
                switch (eventType) {
                    //声明List<CompanyModel>
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("Sucess")) {
                            parser.next();
                            if(parser.getText().toString().equals("true")){
                                invokeReturn.setSuccess(true);
                            }else{
                                invokeReturn.setSuccess(false);
                            }

                        } else if (parser.getName().equals("Time")) {
                            parser.next();
                            invokeReturn.setTime(parser.getText().toString());
                        } else if (parser.getName().equals(modelName) || (parser.getName().equals("Object") && (attribute.equals("ArrayOf" + modelName) || attribute.equals(modelName)))) {
//                            if (modelName != null) {
//                                if (modelName.equals("PersonnelDTO")) {
//                                    objectModel = JSONUtils.getBean("liuliu.hotel.model." + modelName.replace("DTO", "Model"));
//                                } else {
//                                    objectModel = JSONUtils.getBean("liuliu.hotel.model." + modelName.replace("DTO", "Model"));
//                                }
//                            }
//
//                            if (objectModel != null) {
//                                fields = objectModel.getClass().getDeclaredFields();
//                            }
                        } else if (parser.getName().equals("Object") && parser.getAttributeValue(0).equals("xsd:dateTime")) {
                            parser.next();
                        } else if (parser.getName().equals("Images")) {
                            parser.next();
                            if (parser.getText() != null) {
                                parser.next();
                                if (parser.getName().equals("ImageData")) {
                                    parser.next();
                                    //invokeReturn.setImages(parser.getText());
                                    System.out.println("ImageData" + parser.getText());
                                }
                            }
                        } else {
                            if (objectModel != null) {
                                String property = parser.getName();
                                if (fields != null) {
                                    for (Field field : fields) {
                                        if (field.getName().equals(property)) {
                                            parser.next();
                                            if (!property.equals("Images")) {
                                                String value = parser.getText();
                                                if (value != null) {
                                                    JSONUtils.setProperty(objectModel, property, value.toString());
                                                } else {
                                                    JSONUtils.setProperty(objectModel, property, null);
                                                }
                                            }

                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        parser.next();
                        break;
                    case XmlPullParser.END_TAG:
                        String n = parser.getName();

                        if (n.equals(modelName) || n.equals("Object")) {
                            if (objectModel != null) {

                                objectList.add(objectModel);
                                objectModel = null;
                                fields = null;
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invokeReturn;

    }
}
