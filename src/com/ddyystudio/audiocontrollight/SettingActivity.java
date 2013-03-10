package com.ddyystudio.audiocontrollight;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Display;
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
	static int audioMode = 2;
	private LayoutParams mbtBackParams = new LayoutParams(0, 0);
	private LayoutParams radio1Params = new LayoutParams(0, 0);
	private LayoutParams radio2Params = new LayoutParams(0, 0);
	private LayoutParams radio3Params = new LayoutParams(0, 0);
	private static int width, height;
	private float screenWidthFactor;
	private float screenHeightFactor;
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
 		
 		Display display = getWindowManager().getDefaultDisplay();
		height = display.getHeight();
		width = display.getWidth();
		
		screenWidthFactor = (float)width/480;
		screenHeightFactor = (float)height/854;
		
		float factor = (float)854*width/(480*height);
 		
		setContentView(R.layout.setting);
		mbtBackParams.width = (int) (70*screenWidthFactor);
		mbtBackParams.height = (int) (70*screenHeightFactor);
		mbtBackParams.setMargins(10, 0, 0, 0);
		mbtBack = (Button) findViewById(R.id.back);
		mbtBack.setLayoutParams(mbtBackParams);
		radio1Params.width = (int) (70*screenWidthFactor);
		radio1Params.height = (int) (70*screenHeightFactor);
		radio1Params.setMargins((int)(5*screenWidthFactor), (int)(105*screenHeightFactor*factor), 0, 0);
		radio1 = (Button) findViewById(R.id.radio1);
		radio1.setLayoutParams(radio1Params);
		radio2Params.width = (int) (70*screenWidthFactor);
		radio2Params.height = (int) (70*screenHeightFactor);
		radio2Params.setMargins((int)(5*screenWidthFactor), (int)(185*screenHeightFactor*factor), 0, 0);
		radio2 = (Button) findViewById(R.id.radio2);
		radio2.setLayoutParams(radio2Params);
		radio3Params.width = (int) (70*screenWidthFactor);
		radio3Params.height = (int) (70*screenHeightFactor);
		radio3Params.setMargins((int)(5*screenWidthFactor), (int)(270*screenHeightFactor*factor), 0, 0);
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
