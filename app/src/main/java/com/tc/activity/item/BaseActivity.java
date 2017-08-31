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
//		String str = "��½ʧ��";
//		switch (code) {
//		case 0:
//			str = "�˺ŵ�½�ɹ�";
//			break;
//		case 1:
//			str = "������δӦ��";
//			break;
//		case 2:
//			str = "��½ʧ��";
//			break;
//		case 3:
//			str = "�������,��½ʧ��";
//			break;
//		case 4:
//			str = "������δ���� ";
//			break;
//		case 8:
//			str = "�û�������,��½ʧ�� ";
//			break;
//		case 17:
//			str = "�û������Ƶ�½";
//			break;
//		default:
//			break;
//		}
		//return CardCode.errorCodeDescription(code);
		return "";
	}

	public String getMD5(byte[] source) {
		String s = null;
		char hexDigits[] = { // �������ֽ�ת���� 16 ���Ʊ�ʾ���ַ�
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 �ļ�������һ�� 128 λ�ĳ�������
			// ���ֽڱ�ʾ���� 16 ���ֽ�
			char str[] = new char[16 * 2]; // ÿ���ֽ��� 16 ���Ʊ�ʾ�Ļ���ʹ�������ַ���
			// ���Ա�ʾ�� 16 ������Ҫ 32 ���ַ�
			int k = 0; // ��ʾת������ж�Ӧ���ַ�λ��
			for (int i = 0; i < 16; i++) { // �ӵ�һ���ֽڿ�ʼ���� MD5 ��ÿһ���ֽ�
				// ת���� 16 �����ַ���ת��
				byte byte0 = tmp[i]; // ȡ�� i ���ֽ�
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // ȡ�ֽ��и� 4 λ������ת��,
				// >>> Ϊ�߼����ƣ�������λһ������
				str[k++] = hexDigits[byte0 & 0xf]; // ȡ�ֽ��е� 4 λ������ת��
			}
			s = new String(str); // ����Ľ��ת��Ϊ�ַ���

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
