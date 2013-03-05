package com.ddyystudio.audiocontrollight;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;

import com.umeng.analytics.MobclickAgent;

public class SettingActivity extends Activity {
	private Button mbtBack;
	private Button radio1,radio2,radio3;
	private static boolean isradio1 = true;
	private static boolean isradio2 = true;
	private static boolean isradio3 = true;
	static int audioMode = 1;
	private LayoutParams mbtBackParams = new LayoutParams(0, 0);
	private LayoutParams radio1Params = new LayoutParams(0, 0);
	private LayoutParams radio2Params = new LayoutParams(0, 0);
	private LayoutParams radio3Params = new LayoutParams(0, 0);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.e("onCreateonCreate", "onCreateonCreate");
		super.onCreate(savedInstanceState);
		
		 // 全屏设置，隐藏窗口所有装饰
// 		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
// 				WindowManager.LayoutParams.FLAG_FULLSCREEN);
 		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置屏幕显示无标题，必须启动就要设置好，否则不能再次被设置
 		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
 				WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
 		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
 				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
 		
		setContentView(R.layout.setting);
		mbtBackParams.width = 70;
		mbtBackParams.height = 70;
		mbtBackParams.setMargins(10, 0, 0, 0);
		mbtBack = (Button) findViewById(R.id.back);
		mbtBack.setLayoutParams(mbtBackParams);
		radio1Params.width = 70;
		radio1Params.height = 70;
		radio1Params.setMargins(5, 105, 0, 0);
		radio1 = (Button) findViewById(R.id.radio1);
		radio1.setLayoutParams(radio1Params);
		radio2Params.width = 70;
		radio2Params.height = 70;
		radio2Params.setMargins(5, 185, 0, 0);
		radio2 = (Button) findViewById(R.id.radio2);
		radio2.setLayoutParams(radio2Params);
		radio3Params.width = 70;
		radio3Params.height = 70;
		radio3Params.setMargins(5, 270, 0, 0);
		radio3 = (Button) findViewById(R.id.radio3);
		radio3.setLayoutParams(radio3Params);
		
		mbtBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		radio1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isradio1) {
					radio1.setBackgroundResource(R.drawable.radio_down);
					radio2.setBackgroundResource(R.drawable.radio_up);
					radio3.setBackgroundResource(R.drawable.radio_up);
					isradio1 = false;
					isradio2 = true;
					isradio3 = true;
					audioMode = 1;
				}
			}
		});
		radio2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isradio2) {
					radio2.setBackgroundResource(R.drawable.radio_down);
					radio1.setBackgroundResource(R.drawable.radio_up);
					radio3.setBackgroundResource(R.drawable.radio_up);
					isradio2 = false;
					isradio1 = true;
					isradio3 = true;
					audioMode = 2;
				}
			}
		});
		radio3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isradio3) {
					radio3.setBackgroundResource(R.drawable.radio_down);
					radio1.setBackgroundResource(R.drawable.radio_up);
					radio2.setBackgroundResource(R.drawable.radio_up);
					isradio3 = false;
					isradio1 = true;
					isradio2 = true;
					audioMode = 3;
				}
			}
		});
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.e("onPauseonPause", "onPauseonPause");
		super.onPause();
		MobclickAgent.onPause(this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		switch (audioMode) {
		case 1:
			radio1.setBackgroundResource(R.drawable.radio_down);
			radio2.setBackgroundResource(R.drawable.radio_up);
			radio3.setBackgroundResource(R.drawable.radio_up);
			break;
		case 2:
			radio2.setBackgroundResource(R.drawable.radio_down);
			radio1.setBackgroundResource(R.drawable.radio_up);
			radio3.setBackgroundResource(R.drawable.radio_up);
			break;
		case 3:
			radio3.setBackgroundResource(R.drawable.radio_down);
			radio1.setBackgroundResource(R.drawable.radio_up);
			radio2.setBackgroundResource(R.drawable.radio_up);
			break;

		default:
			break;
		}
		Log.e("onResumeonResume", "onResumeonResume");
		super.onResume();
		MobclickAgent.onResume(this);
	}
}
