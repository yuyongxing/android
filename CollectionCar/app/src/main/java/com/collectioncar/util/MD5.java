package com.collectioncar.util;


/**
 *
 */


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * 华夏二手车加密使用
 *
 */
public class MD5 {

	public static final String  SALT="HKUUKOIIKJKLLI";
	private final static char[] hexDigits = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 进行MD5
	 *
	 * @param s
	 * @return 字符串
	 */
	public static final String md5(String s) {
		byte[] h;

		if(s == null) {
			return null;
		} else {
			try {
				h= s.getBytes("UTF-8");

				StringBuilder resultSb = new StringBuilder();

				for (int i = 0; i < h.length; ++i) {
					int t = h[i] & 0xFF;
					resultSb.append(hexDigits[(t >> 4) & 0x0F]);
					resultSb.append(hexDigits[t & 0x0F]);
				}

				return resultSb.toString();
			} catch (UnsupportedEncodingException var3) {

			}
		}
		//byte[] h = DigestUtils.md5(s);

/*		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(s.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10) hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();*/
		return null;
	}

	public final static String getMessageDigest(byte[] buffer) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(buffer);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

}
