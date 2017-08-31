package com.tc.activity.item;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import com.global.Utils;


public class BaseActivity extends Activity {
	String ip;
	int port;
	String account;
	String password;
	private PowerManager pm;
	private WakeLock wl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ip = Utils.getIp(BaseActivity.this);
		port = Utils.getPort(BaseActivity.this);
		account = Utils.getAccount(BaseActivity.this);
		String source = Utils.getPassword(BaseActivity.this);
		password = getMD5(source.getBytes());
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "kaer");
	}

	private void releaseWakeLock() {
		if (wl != null && wl.isHeld()) {
			wl.release();
			wl = null;
		}
	}

	protected String getEInfoByCode(int code) {

//
//		String str = "登陆失败";
//		switch (code) {
//		case 0:
//			str = "账号登陆成功";
//			break;
//		case 1:
//			str = "服务器未应答";
//			break;
//		case 2:
//			str = "登陆失败";
//			break;
//		case 3:
//			str = "密码错误,登陆失败";
//			break;
//		case 4:
//			str = "服务器未连接 ";
//			break;
//		case 8:
//			str = "用户不存在,登陆失败 ";
//			break;
//		case 17:
//			str = "用户被限制登陆";
//			break;
//		default:
//			break;
//		}
		//return CardCode.errorCodeDescription(code);
		return "";
	}

	public String getMD5(byte[] source) {
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		releaseWakeLock();

	}
}
