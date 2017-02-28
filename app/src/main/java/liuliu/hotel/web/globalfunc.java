package liuliu.hotel.web;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import liuliu.hotel.base.DES;


public class globalfunc extends Application {



	String ServerAddress = "";// 用户设置的服务器地址

	String companyID = ""; // 企业代码

	String RegeditAppCode = ""; // 注册码

	String companyName = "";// gongsimingcheng

	String companyAddress = "";// 公司地址

	String HotelArea = ""; // 旅馆区域范围

	String SerialNum = ""; // 流水号，用于查询、修改、引用用户信息

	int changeorquoteCustomInfo = 0;// 默认为0，设置为1表示修改信息，设置为2表示引用信息
	// 在客户信息页面点击修改时该值设置为1，引用时该值设置为2

	String TempPhotoPath = "";
	String NormalPhotoPath = "";
	String UpLoadPhotoPath = "";
	String FontSize = "21.0";
	String BlueToothName = "";

	float CriterionFontSize = 0.0f;

	String SaveDataMonth = "1";

	private static String METHOD_NAME = "GeneralInvoke";
	private static String NAMESPACE = "http://tempuri.org/";
	private static String SOAP_ACTION = NAMESPACE + METHOD_NAME;
	// URL = "http://219.148.138.188:8010/WebServices/LGXX/Mobile.asmx";
	// http://219.148.138.188:8010/WebServices/LGXX/Mobile.asmx
	public static String TEMPURL = "http://ServerAddress/WebServices/LGXX/Mobile.asmx";
	public static String URL = "http://219.148.138.188:8050/WebServices/LGXX/Mobile.asmx";

	// public static String
	// myUrl="http://192.168.1.113:8090/Service/Mobile.asmx";
	public static String myUrl = "http://192.168.1.113:8090/Service/Mobile.asmx";

	/** BASE64编码表 */
	public static final byte[] encodingTable = { (byte) 'A', (byte) 'B',
			(byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9',
			(byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L',
			(byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z',
			(byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V',
			(byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z', (byte) 'a',
			(byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u',
			(byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G',
			(byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k',
			(byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q',
			(byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f',
			(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4',
			(byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p',
			(byte) '+', (byte) '/' };// <br/>
	/** BASE64解码表 */
	public static final byte[] decodingTable;
	// 对解码表decodingTable进行初始化
	static {
		decodingTable = new byte[128];
		for (int i = 0; i < encodingTable.length; i++) {
			decodingTable[encodingTable[i]] = (byte) i;
		}
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		// CrashHandler crashHandler = CrashHandler.getInstance();
		// crashHandler.init(getApplicationContext());
	}





	/**
	 * 检测性别 参数：身份证号18位 返回值1=女，0=男，-1=身份证错误
	 * */
	public int CheckCardNumSex(String IDCardNum) {
		if (IDCardNum.length() < 18 || IDCardNum.length() > 18)
			return -1;
		int flag = Integer.valueOf(IDCardNum.substring(16, 17));

		if (flag % 2 == 0)
			return 1;
		return 0;
	}

	/**
	 * 检测身份证是否有效 参数：输入的身份证号码 返回值 有效返回true 无效返回false
	 * */
	//
	public boolean CheckIDCard(String IDCardNum) {
		try {
			int sum = 0;
			int iw[] = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5,
					8, 4, 2 };
			String lastCode = "10X98765432";
			for (int i = 0; i < 17; i++) {

				sum += Integer.parseInt(IDCardNum.substring(i, i + 1)) * iw[i];
			}

			int iy = sum % 11;

			String ai = lastCode.substring(iy, iy + 1);

			String aj = IDCardNum.substring(17, 18);

			aj = aj.toUpperCase();
			if (aj.equals(ai)) {
				return true;
			} else {
				return false;
			}

			// IDCardNum.substring(17, 18).toUpperCase().equals(ai);

		} catch (Exception e) {
			return false;
		}
	}

	// 15位转18位
	public String IDCard15To18(String IDCardNum) {

		try {

			int iS = 0;

			// 加权因子常数
			int iW[] = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5,
					8, 4, 2 };
			// 校验码常数
			String LastCode = "10X98765432";
			// 新身份证号
			String perIDNew = "";

			perIDNew = IDCardNum.substring(0, 6);

			// 填在第6位及第7位上填上‘1’，‘9’两个数字

			if (Integer.parseInt(IDCardNum.substring(6, 2)) < 30)
				perIDNew += "20";
			else
				perIDNew += "19";

			perIDNew += IDCardNum.substring(6, 9);

			for (int i = 0; i < 17; i++) {

				iS += Integer.parseInt(IDCardNum.substring(i, 1)) * iW[i];
			}

			int iy = iS % 11;

			// 从LastCode中取得以模为索引号的值，加到身份证的最后一位，即为新身份证号。
			perIDNew += LastCode.substring(iy, 1);
			return perIDNew;

		} catch (Exception e) {
			return "";
		}

	}

	// 检查数据库是否有效
	private boolean checkDataBase(String path, String name) {
		File file = new File(path, name);
		return file.exists();
	}

	/**
	 * 拷贝文件 srcFile为assets目录下的源文件名称 。 destPath为要拷贝到的目录。
	 *
	 * 拷贝后的文件名称和源文件名称一致
	 *
	 * 参数1：文件名称 参数2：目标路径
	 */
	public boolean copyfile(String srcFile, String destPath) {
		OutputStream dbOut;
		InputStream assetsDB;
		try {
			File dbDir = new File(destPath);
			if (!dbDir.exists()) // 如果不存在该目录则创建
			{
				if (dbDir.mkdirs()) {
					assetsDB = this.getAssets().open(srcFile);// strln是assets文件夹下的文件名
					dbOut = new FileOutputStream(destPath + srcFile);

					byte[] buffer = new byte[1024];
					int length;
					while ((length = assetsDB.read(buffer)) > 0) {
						dbOut.write(buffer, 0, length);
					}

					dbOut.flush();
					dbOut.close();
					assetsDB.close();
					return true;
				} else {
					// 目录创建失败
					return false;
				}
			} else {
				if (checkDataBase(destPath, srcFile)) {
					// 数据库已经存在 直接退出
					Log.i("copyfailed", "database exists");
					return true;
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// strout是你要保存的文件名
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 核对密码是否正确 参数：用户输入的密码 返回值 密码正确返回true 不正确返回 false
	 * */
	public boolean CheckPassword(String paswd) {
		Calendar calendar = Calendar.getInstance();
		int monthOfYear = calendar.get(Calendar.MONTH) + 1;
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

		String sumMonthDay = String.valueOf(monthOfYear + dayOfMonth);

		// 判断是否是一位，存在1月1日相加等于2的情况，这个时候要在前面补0
		if (sumMonthDay.length() == 1) {
			sumMonthDay = "0" + sumMonthDay;
		}

		String Password = "hotel" + sumMonthDay;

		if (Password.equals(paswd))
			return true;
		return false;
	}

	// 格式化身份证号中的生日 格式为1988-01-01
	public String FormatBirthday(String birthday) {
		if (birthday.length() < 8)
			return "";
		String _birthday = "";
		_birthday = birthday.substring(0, 4);
		_birthday += "-";
		_birthday += birthday.substring(4, 6);
		_birthday += "-";
		_birthday += birthday.substring(6, 8);
		return _birthday;
	}

	/**
	 * 检测SD卡是否存在
	 *
	 * 存在返回true 不存在返回false
	 * */

	public boolean BCheckSDCardReady() {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			return false;
		}
		return true;
	}

	/**
	 * 获取系统的当前时间，格式为YYYY-MM-DD HH:MM:SS
	 * */
	public String getSystemNowTime() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		String year_str = String.valueOf(year);

		int monthOfYear = calendar.get(Calendar.MONTH) + 1;
		String month_str = String.valueOf(monthOfYear);
		if (month_str.length() < 2) {
			month_str = "0" + month_str;
		}

		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		String day_str = String.valueOf(dayOfMonth);
		if (day_str.length() < 2) {
			day_str = "0" + day_str;
		}

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		String hour_str = String.valueOf(hour);
		if (hour_str.length() < 2) {
			hour_str = "0" + hour_str;
		}
		int minute = calendar.get(Calendar.MINUTE);
		String minute_str = String.valueOf(minute);
		if (minute_str.length() < 2) {
			minute_str = "0" + minute_str;
		}
		int second = calendar.get(Calendar.SECOND);
		String second_str = String.valueOf(second);
		if (second_str.length() < 2) {
			second_str = "0" + second_str;
		}
		return year_str + "-" + month_str + "-" + day_str + " " + hour_str
				+ ":" + minute_str + ":" + second_str;
	}

	/**
	 * 格式化月份或者天数。 比如月份是1，调用此函数后返回01，比如月份是12，调用此函数后不变 day 同上
	 * */
	public String FormatMonthOrDay(String monthorday) {
		String NewMonthOrday = monthorday;
		if (NewMonthOrday.equals("") || NewMonthOrday == null) {
			return "";
		}
		if (NewMonthOrday.length() < 2) {
			NewMonthOrday = "0" + NewMonthOrday;
		}
		return NewMonthOrday;
	}

	/**
	 * 获取系统的当前日期，格式为YYYYMMDD
	 * */
	public String getSystemNowDate() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int monthOfYear = calendar.get(Calendar.MONTH) + 1;
		String monthStr = String.valueOf(monthOfYear);
		if (monthStr.length() < 2) {
			monthStr = "0" + monthStr;
		}
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		String dayStr = String.valueOf(dayOfMonth);
		if (dayStr.length() < 2) {
			dayStr = "0" + dayStr;
		}
		return String.valueOf(year) + monthStr + dayStr;
	}

	/**
	 * 获取系统的当前日期，格式为YYYY-MM-DD
	 * */
	public String getSystemNowDate1() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int monthOfYear = calendar.get(Calendar.MONTH) + 1;
		String monthStr = String.valueOf(monthOfYear);
		if (monthStr.length() < 2) {
			monthStr = "0" + monthStr;
		}
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		String dayStr = String.valueOf(dayOfMonth);
		if (dayStr.length() < 2) {
			dayStr = "0" + dayStr;
		}
		return String.valueOf(year) + "-" + monthStr + "-" + dayStr;
	}



	/** 检查设备是否提供摄像头 */

	public boolean checkCamera(Context context) {

		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {

			// 摄像头存在

			return true;

		} else {

			// 摄像头不存在

			return false;

		}

	}


	/**
	 * 加载本地图片
	 */
	public Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
//
//	/**
//	 * 获取流水号中的顺序号 首次获取数据库中没有数据的时候先插入初始值9501并返回9501 之后每次获取都是累加
//	 * */
//	public String[] getDBSerialNum() {
//		String returnstr[] = new String[] { "", "" };
//		SQLiteDatabase db = idcardHelper.getReadableDatabase();
//		String sqlstr = "select * from SerialNumber";
//
//		try {
//			Cursor cursor = db.rawQuery(sqlstr, null);
//
//			if (cursor != null) {
//				if (cursor.moveToFirst()) {
//					returnstr[0] = cursor.getString(0);
//					returnstr[1] = cursor.getString(1);
//					// Log.i("SerialNumber", returnstr);
//
//				} else {
//					// 首次插入9501 后直接返回9501
//					String LastUserDate = getSystemNowDate();
//					sqlstr = "insert into SerialNumber(SerialNum,LastUserDate) values(?,?)";
//					Object sqlob[] = new Object[] { "9501", LastUserDate };
//
//					if (executeSqlStr(sqlstr, sqlob)) {
//						cursor.close();
//						db.close();
//						returnstr[0] = "9501";
//						returnstr[1] = LastUserDate;
//						return returnstr;
//					}
//				}
//
//				cursor.close();
//			}
//		} catch (Exception e) {
//		}
//
//		db.close();
//		return returnstr;
//	}

//	/**
//	 * 创建流水号
//	 * */
//	public String CreateSerialNum() {
//		String companyID = "";
//		String date = "";
//		String serialnum[] = null;
//
//		companyID = getCompanyID();
//		date = getSystemNowDate();
//
//		serialnum = getDBSerialNum();
//
//		// 如果日期相同是当天
//		if (date.equals(serialnum[1])) {
//			Log.i("CreateSerialNum", companyID + date + serialnum[0]);
//			return companyID + date + serialnum[0];
//		}
//		// 日期不同从9501开始，并更新日期
//		UpdateSerialNum("9501", date);
//
//		Log.i("CreateSerialNum", companyID + date + "9501");
//		return companyID + date + "9501";
//
//		// UpdateSerialNum(serialnum);
//
//	}
//
//	/**
//	 * 更新流水号和日期的值
//	 * */
//	private boolean UpdateSerialNum(String _Num, String LastUserDate) {
//		if (_Num == null || _Num.equals(""))
//			return false;
//
//		if (LastUserDate == null || LastUserDate.equals(""))
//			return false;
//
//		String sqlstr = "";
//		Object ob[] = null;
//
//		sqlstr = "update SerialNumber set SerialNum=?,LastUserDate=?";
//		ob = new Object[] { _Num, LastUserDate };
//
//		if (executeSqlStr(sqlstr, ob)) {
//			return true;
//		}
//		return false;
//	}
//
//	/**
//	 * 更新流水号的值
//	 * */
//	public boolean UpdateSerialNum(String _Num) {
//		if (_Num == null || _Num.equals(""))
//			return false;
//
//		int num = Integer.valueOf(_Num);
//		num += 1;
//
//		String sqlstr = "update SerialNumber set SerialNum=?";
//		Object ob[] = new Object[] { String.valueOf(num) };
//
//		if (executeSqlStr(sqlstr, ob)) {
//			return true;
//		}
//		return false;
//	}

	public void SetChangeOrQuoteCustomInfo(int Flag) {
		this.changeorquoteCustomInfo = Flag;
	}

	public int getChangeOrQuoteCustomInfo() {
		return this.changeorquoteCustomInfo;
	}

	public void setSerialNum(String num) {
		this.SerialNum = num;
	}

	public String getSerialNum() {
		return this.SerialNum;
	}


	public String getServerAddress() {
		return this.ServerAddress;
	}

	public String getCompanyID() {
		return this.companyID;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public String getCompanyAddress() {
		return this.companyAddress;
	}

	public String getHotelArea() {
		return this.HotelArea;
	}

	public void SetHotelArea(String area) {
		this.HotelArea = area;
	}

	public String getRegeditAppCode() {
		return this.RegeditAppCode;
	}

	public void SetCriterionFontSize(float f) {
		this.CriterionFontSize = f;
	}

	public float GetCriterionFontSize() {
		return this.CriterionFontSize;
	}

	public void SetFontSizeValue(float f) {

		this.FontSize = String.valueOf(f);
	}

	public int GetSaveDataMonth() {
		if (SaveDataMonth == null || SaveDataMonth.equals(""))
			return 500;
		return Integer.valueOf(SaveDataMonth);
	}

	public float GetFontSizeValue() {
		if (this.FontSize == null || this.FontSize.equals("")) {
			return GetCriterionFontSize() + 5.0f;
		}
		return Float.valueOf(this.FontSize);
	}

	public String GetBlueToothName() {
		return this.BlueToothName;
	}



//
//	/**
//	 * 程序每次启动的时候调用 删除当前日期前X月的数据
//	 * */
//	public void DeleteDBData() {
//		int SaveMonth = GetSaveDataMonth();
//
//		String sqlstr = "select * from TravellerInfo where Flag=0 order by RegeditTime desc limit "
//				+ SaveMonth + ",-1";
//
//		ArrayList<HashMap<String, String>> deleteData = getDeleteCustomInfo(sqlstr);
//
//		for (int i = 0; i < deleteData.size(); i++) {
//
//			String deleteSqlstr = "delete from TravellerInfo where SerialNum=?";
//			Object ob[] = new Object[] { deleteData.get(i).get("SerialNum") };
//
//			if (executeSqlStr(deleteSqlstr, ob)) {
//				File deleteFile = new File(deleteData.get(i).get("PhotoPath"));
//				if (deleteFile.exists()) {
//					deleteFile.delete();
//				}
//			}
//		}
//	}

	public String getPhotoFileTime(String FileName) {
		int index = FileName.indexOf("hotelImage_");
		if (index == -1)
			return "";

		String tempstr = FileName.substring(index + "hotelImage_".length());

		tempstr = tempstr.replace(".jpg", "");
		// YYYYMMDDHHMMSS
		if (tempstr.length() < 14)
			return "";

		String formatstr = tempstr.substring(0, 4) + "/";

		formatstr += tempstr.substring(4, 6) + "/";

		formatstr += tempstr.substring(6, 8) + " ";

		formatstr += tempstr.substring(8, 10) + ":";

		formatstr += tempstr.substring(10, 12) + ":";

		formatstr += tempstr.substring(12, 14);
		return formatstr;
	}




	public String getAssetsFileData(String FileName) {
		String str = "";
		try {
			InputStream is = getAssets().open(FileName);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			str = new String(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;

	}

	public byte[] serialize(String s) {
		try {
			ByteArrayOutputStream mem_out = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(mem_out);

			out.writeObject(s);

			out.close();
			mem_out.close();

			byte[] bytes = mem_out.toByteArray();
			return bytes;
		} catch (IOException e) {
			return null;
		}
	}

	public static String deserialize(byte[] bytes) {
		try {
			String returnstr = "";
			ByteArrayInputStream mem_in = new ByteArrayInputStream(bytes);
			ObjectInputStream in = new ObjectInputStream(mem_in);

			returnstr = (String) in.readObject();

			in.close();
			mem_in.close();

			return returnstr;
		} catch (StreamCorruptedException e) {
			return "";
		} catch (ClassNotFoundException e) {
			return "";
		} catch (IOException e) {
			return "";
		}
	}

	// 小胡
	public String MySendDataToServers(String myMethod, String zklsh, boolean sfyi){
		String returnstr = "";
		try {
			SoapObject request = new SoapObject(NAMESPACE, myMethod);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);// 设置SOAP的版本号
			if(sfyi){
				request.addProperty("zklsh", zklsh);
				request.addProperty("sfyi",sfyi);
			}
			envelope.dotNet = true;
			envelope.bodyOut = request;
			envelope.setOutputSoapObject(request);
			HttpTransportSE transport = new HttpTransportSE(myUrl);
			transport.debug = true;
			transport.call(NAMESPACE+myMethod, envelope);// 这里是发送请求并等待回复
			System.out.println("by:"+envelope.bodyIn);
			returnstr=envelope.getResponse().toString();
		} catch (IOException e) {// 超时异常
			return "timeout";
		} catch (Exception e) {// 加密解密异常触发
			return "deserror";
		}
		return returnstr;
	}

	public String SendDataToServer(String data) {
		if (data == null || data.equals("")) {
			return "DataNull";
		}

		String returnstr = "";
		try {

			String DESStr = DES.encryptDES(data, "K I W I ", "K I W I ");

			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

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
			System.out.println("dddddddddddddddddd" + SOAP_ACTION);
			transport.call(SOAP_ACTION, envelope);// 这里是发送请求并等待回复

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

	/** 获取当前应用的版本号： */

	public String getVersionName() {
		// 获取packagemanager的实例
		String Version = "[Version:num]-[Registe:Mobile]";
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			String version = packInfo.versionName;
			return Version.replace("num", version);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Version.replace("num", "1.0");
	}

	public void sendbrocast(String action) {
		Intent intent = new Intent();

		intent.setAction(action);
		this.sendBroadcast(intent);
	}

	private ArrayList<String> getFileNameList(File path) {
		ArrayList<String> FileNameList = new ArrayList<String>();
		// 如果是文件夹的话
		if (path.isDirectory()) {
			// 什么也不做
		}

		// 如果是文件的话直接加入
		else {
			// Log.i(TAG, path.getAbsolutePath());
			// 进行文件的处理
			String filePath = path.getAbsolutePath();
			// 文件名
			String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
			// 添加
			FileNameList.add(fileName);
		}

		return FileNameList;
	}

	public String SendDataToServer(String data, String MethName) {
		String returnstr = "";
		try {

			SoapObject request = new SoapObject(NAMESPACE, MethName);
			// byte[] testbyte = new byte[]{1,2,3,4,5,6};
			request.addProperty("parameter", data);// 添加参数和数据
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);// 设置SOAP的版本号
			envelope.dotNet = true;
			envelope.bodyOut = request;
			// envelope.encodingStyle="utf-8";
			envelope.setOutputSoapObject(request);
			int timeout = 20000; // set timeout 15s
			MyAndroidHttpTransport transport = new MyAndroidHttpTransport(URL,
					timeout);

			// HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,
			// 25000);
			transport.debug = true;
			transport.call(NAMESPACE + MethName, envelope);// 这里是发送请求并等待回复
			returnstr = envelope.getResponse().toString();
			Log.v("gtrgtr", "result=========" + returnstr);
		} catch (IOException e) {// 超时异常
			// TODO Auto-generated catch block
			return "timeout";
		} catch (XmlPullParserException e) {// 发送请求异常
			// TODO Auto-generated catch block
			return "callerror";
		}
		return returnstr;
	}

	/**
	 * 当前时间和1970/1/1的时间查毫秒数 参数：无 返回值：当前时间和1970/1/1比较的时间查毫秒数
	 */

	public boolean NowTimeTomillisecond(String FileTime, String comparisonTime) {

		SimpleDateFormat myFormatter = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss");
		boolean returnBoolean = false;
		try {
			java.util.Date mydate = myFormatter.parse(FileTime);
			java.util.Date mytime = myFormatter.parse(comparisonTime);

			returnBoolean = mytime.getTime() >= mydate.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		return returnBoolean;

	}

	/**
	 * 清空数据信息，并把流水号置为初始值
	 * */
//	public boolean ClearAllData() {
//		String sqlstr = "delete  from TravellerInfo";
//		Object ob[] = new Object[] {};
//		if (!executeSqlStr(sqlstr, ob)) {
//			return false;
//		}
//
//		if (!UpdateSerialNum("9501", getSystemNowDate())) {
//			return false;
//		}
//		return true;
//	}

}
