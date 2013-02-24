package com.ddyystudio.audiocontrollight;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	private static final String LOG_TAG = "MainActivity";
	private RelativeLayout linear;
	private boolean close = true;
	private Button myBtn;
	private MediaRecorder mRecorder = null;
	private static String mFileName = null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     // 全屏设置，隐藏窗口所有装饰
 		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
 				WindowManager.LayoutParams.FLAG_FULLSCREEN);
 		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置屏幕显示无标题，必须启动就要设置好，否则不能再次被设置
 		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
 				WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
 		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
 				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecord.3gp";
        
        linear = (RelativeLayout) findViewById(R.id.LinearLayout1);
        setContentView(linear);
        myBtn = (Button) findViewById(R.id.button1);
        myBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (close) {
					linear.setBackgroundResource(R.drawable.light_on);
					myBtn.setBackgroundResource(R.drawable.on);
					startRecording();
					close = false;
				} else {
					linear.setBackgroundResource(R.drawable.light_off);
					myBtn.setBackgroundResource(R.drawable.off);
					stopRecording();
					close = true;
				}
			}
		});
    }

    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
	
	private void startRecording() {
		mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mRecorder.start();
	}
	
	private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
}
