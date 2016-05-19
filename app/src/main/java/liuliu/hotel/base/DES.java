package liuliu.hotel.base;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DES {

	/**
	 * DES加密
	 * 参数1：加密的内容
	 * 参数2：加密密钥
	 * 参数3：加密向量
	 * 返回值：DES加密后的内容
	 * */
	public static String encryptDES(String encryptString, String encryptKey,
									String encryptiv) throws Exception {

		byte[] encryptByte = encryptString.getBytes();
		byte[] keyByte = encryptKey.getBytes();

		byte[] byteTemp = new byte[8];

		for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
			byteTemp[i] = keyByte[i];
		}

		byte[] ivbyte = encryptiv.getBytes();

		byte[] tempivbyte = new byte[8];

		for (int i = 0; i < ivbyte.length && i < tempivbyte.length; i++) {
			tempivbyte[i] = ivbyte[i];
		}

		IvParameterSpec zeroIv = new IvParameterSpec(tempivbyte);
		SecretKeySpec key = new SecretKeySpec(byteTemp, "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
		byte[] encryptedData = cipher.doFinal(encryptByte);


		return Base64Util.encode(encryptedData);
	}

	/**
	 * DES解密
	 * 参数1：加密的内容
	 * 参数2：加密密钥
	 * 参数3：加密向量
	 * 返回值：DES解密后的内容
	 * */
	public static String decryptDES(String decryptString, String decryptKey,
									String decryptiv) throws Exception {

		byte[] keyByte = decryptKey.getBytes();

		byte[] byteTemp = new byte[8];

		for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
			byteTemp[i] = keyByte[i];
		}

		byte[] ivbyte = decryptiv.getBytes();

		byte[] tempivbyte = new byte[8];

		for (int i = 0; i < ivbyte.length && i < tempivbyte.length; i++) {
			tempivbyte[i] = ivbyte[i];
		}

		byte[] byteMi = Base64Util.decode(decryptString);
		IvParameterSpec zeroIv = new IvParameterSpec(tempivbyte);
		// IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
		SecretKeySpec key = new SecretKeySpec(byteTemp, "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
		byte decryptedData[] = cipher.doFinal(byteMi);

		return new String(decryptedData);
	}
}