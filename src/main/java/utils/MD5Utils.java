package utils;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @author 寒洲
 * @description
 * @date 2020/8/31
 */
public class MD5Utils {
	public static String md5Encode(String source) {
		String result = null;
		try {
			//获取MD5算法对象
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			//对明文进行加密操作
			md5.update(source.getBytes());
			result = new BigInteger(md5.digest(source.getBytes())).toString(16);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
