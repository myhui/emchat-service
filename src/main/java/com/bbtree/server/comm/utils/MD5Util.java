package com.bbtree.server.comm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Util {

	private static final Logger logger = LoggerFactory.getLogger(MD5Util.class);
	
	private static final String UTF8 = "utf-8";

	/**
	 * MD5数字签名
	 * 
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static String md5Digest(String src) {
		try {
			// 定义数字签名方法, 可用：MD5, SHA-1
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] b = md.digest(src.getBytes(UTF8));
			return byte2HexStr(b);
		} catch (Exception e) {
			logger.error("md5Digest error");
		}
		return null;
	}

    /**
     * 字节数组转化为大写16进制字符串
     *
     * @param b
     * @return
     */
    private static String byte2HexStr(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String s = Integer.toHexString(b[i] & 0xFF);
            if (s.length() == 1) {
                sb.append("0");
            }
            sb.append(s);
        }
        return sb.toString();
    }


//	//静态方法，便于作为工具类
//	public static String getMd5(String plainText) {
//		try {
//			MessageDigest md = MessageDigest.getInstance("MD5");
//			md.update(plainText.getBytes());
//			byte b[] = md.digest();
//
//			int i;
//
//			StringBuffer buf = new StringBuffer("");
//			for (int offset = 0; offset < b.length; offset++) {
//				i = b[offset];
//				if (i < 0)
//					i += 256;
//				if (i < 16)
//					buf.append("0");
//				buf.append(Integer.toHexString(i));
//			}
//			//32位加密
//			return buf.toString();
//			// 16位的加密
//			//return buf.toString().substring(8, 24);
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//			return null;
//		}
//
//	}
//
//	public static void main(String[] args) {
//		//测试
//		System.out.println(MD5Util.md5Digest("hyww_4285611_123456"));
//	}
}