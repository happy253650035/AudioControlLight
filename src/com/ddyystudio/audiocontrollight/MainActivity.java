package com.ddyystudio.audiocontrollight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.umeng.analytics.MobclickAgent;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity implements SensorEventListener {
	
	private static final String LOG_TAG = "MainActivity";
	private static final int SENSOR_SHAKE = 10;
	private static final int SOS_SINGLE = 20;
	private Vibrator vibrator;
	private RelativeLayout linear;
	private boolean close = true;
	private Button myBtn;
	private Camera camera = null;
	private Parameters parameters = null;
	private ImageView ImgCompass;
	private SensorManager mSensorManager = null;
	private float currentDegree = 0f; //指南针图片转过的角度
	private Button buttonAdd;
	private Button buttonSosOff;
	private Button buttonSetting;
	private Button buttonCommit;
	private Animation animationTranslate, animationRotate;
	private LayoutParams params = new LayoutParams(0, 0);
	private static Boolean isClick = false;
	private static Boolean isSosOff = true;
	private static int width, height;
	private long time = System.currentTimeMillis();
	private Thread sosThread;
	private boolean isRun;
	private int mosCode;
	
	private Handler mhandler = new Handler(){
		public void handleMessage(Message msg) { 
    	//操作界面
			super.handleMessage(msg); 
            switch (msg.what) { 
            case SENSOR_SHAKE: 
            	lightSwitch();
                break; 
            case SOS_SINGLE: 
            	sosFlash();
                break; 
            } 
    	} 
    };	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     // 全屏设置，隐藏窗口所有装饰
// 		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
// 				WindowManager.LayoutParams.FLAG_FULLSCREEN);
 		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置屏幕显示无标题，必须启动就要设置好，否则不能再次被设置
 		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
 				WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
 		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
 				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        
        Display display = getWindowManager().getDefaultDisplay(); 
		height = display.getHeight();  
		width = display.getWidth();
                
        linear = (RelativeLayout) findViewById(R.id.LinearLayout1);
        setContentView(linear);
		
		ImgCompass = (ImageView) findViewById(R.id.imageView1);
		buttonAdd = (Button) findViewById(R.id.add);
		buttonAdd.setVisibility(View.VISIBLE);
		buttonSosOff = (Button) findViewById(R.id.sosoff);
		buttonSosOff.setVisibility(View.INVISIBLE);
		buttonSetting = (Button) findViewById(R.id.setting);
		buttonSetting.setVisibility(View.INVISIBLE);
		buttonCommit = (Button) findViewById(R.id.commit);
		buttonCommit.setVisibility(View.INVISIBLE);
        myBtn = (Button) findViewById(R.id.button1);
        
        buttonAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isClick == false)
				{
					isClick = true;
					buttonSosOff.setVisibility(View.VISIBLE);
					buttonSetting.setVisibility(View.VISIBLE);
					buttonCommit.setVisibility(View.VISIBLE);
					buttonAdd.startAnimation(animRotate(-45.0f, 0.5f, 0.45f));					
					buttonSosOff.startAnimation(animTranslate(0.0f, -220.0f, width - 100, height - 360, buttonSosOff, 400));
					buttonSetting.startAnimation(animTranslate(0.0f, -150.0f, width - 100, height - 290, buttonSetting, 400));
					buttonCommit.startAnimation(animTranslate(0.0f, -80.0f, width - 100, height - 220, buttonCommit, 400));
				}
				else
				{					
					isClick = false;
					buttonAdd.startAnimation(animRotate(90.0f, 0.5f, 0.45f));
					buttonSosOff.startAnimation(animTranslate(0.0f, 220.0f, width - 100, height - 140, buttonSosOff, 400));
					buttonSetting.startAnimation(animTranslate(0.0f, 150.0f, width - 100, height - 140, buttonSetting, 400));
					buttonCommit.startAnimation(animTranslate(0.0f, 80.0f, width - 100, height - 140, buttonCommit, 400));
				}
			}
		});
        buttonSosOff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isSosOff) {
					mosCode = 0;
					buttonSosOff.setBackgroundResource(R.drawable.sos_on);
					isSosOff = false;
					isRun = true;
					sosThread = new Thread(new Runnable() {
						@Override
						public void run() {
							long time1 = System.currentTimeMillis();
							long time2 = time1;
							// TODO Auto-generated method stub
							while (isRun) {
								time2 = System.currentTimeMillis();
								if (time2 - time1 > 200) {
									Message msg = new Message(); 
					                msg.what = SOS_SINGLE; 
					                mhandler.sendMessage(msg);
									time1 = time2;
								}
							}
						}
					});
					sosThread.start();
				} else {
					buttonSosOff.setBackgroundResource(R.drawable.sos_off);
					isSosOff = true;
					isRun = false;
					sosThread = null;
				}
			}
		});
        buttonSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, SettingActivity.class);
	    		startActivity(intent);
	    		isClick = false;
	    		buttonAdd.startAnimation(animRotate(90.0f, 0.5f, 0.45f));
	    		buttonSosOff.startAnimation(animTranslate(0.0f, 220.0f, width - 100, height - 140, buttonSosOff, 400));
				buttonSetting.startAnimation(animTranslate(0.0f, 150.0f, width - 100, height - 140, buttonSetting, 400));
				buttonCommit.startAnimation(animTranslate(0.0f, 80.0f, width - 100, height - 140, buttonCommit, 400));
			}
		});
        buttonCommit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("buttonCommit", "buttonCommit");
				showAlert("评价", "亲，感谢您使用我们的产品，快来给我们评价吧！", "确定", "取消");
			}
		});
        myBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				lightSwitch();
			}
		});
    }
	
	public void sosFlash(){
	    switch (mosCode) {
	        case 2:
	        	lightSwitch();
	            break;
	        case 4:
	        	lightSwitch();
	            break;
	        case 6:
	        	lightSwitch();
	            break;
	        case 8:
	        	lightSwitch();
	            break;
	        case 10:
	        	lightSwitch();
	            break;
	        case 12:
	        	lightSwitch();
	            break;
	        case 17:
	        	lightSwitch();
	            break;
	        case 21:
	        	lightSwitch();
	            break;
	        case 23:
	        	lightSwitch();
	            break;
	        case 27:
	        	lightSwitch();
	            break;
	        case 29:
	        	lightSwitch();
	            break;
	        case 33:
	        	lightSwitch();
	            break;
	        case 38:
	        	lightSwitch();
	            break;
	        case 40:
	        	lightSwitch();
	            break;
	        case 42:
	        	lightSwitch();
	            break;
	        case 44:
	        	lightSwitch();
	            break;
	        case 46:
	        	lightSwitch();
	            break;
	        case 48:
	        	lightSwitch();
	            break;
	        case 59:
	            mosCode = -1;
	            break;
	            
	        default:
	            break;
	    }
	    mosCode++;
	}
	
	protected void showAlert(String title,String message,String bt1,String bt2) {
		Dialog alertDialog = new AlertDialog.Builder(this). 
                setTitle(title). 
                setMessage(message).
                setIcon(R.drawable.ic_launcher).
                setPositiveButton(bt1, new DialogInterface.OnClickListener() { 
                     
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        // TODO Auto-generated method stub  
                    } 
                }).
                setNegativeButton(bt2, new DialogInterface.OnClickListener() { 
                     
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        // TODO Auto-generated method stub  
                    } 
                }).create(); 
        alertDialog.show(); 
	}
	
	protected void lightSwitch() {
		if (close) {
			linear.setBackgroundResource(R.drawable.light_on);
			myBtn.setBackgroundResource(R.drawable.on);
			parameters = camera.getParameters();
			parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);// 开启
			camera.setParameters(parameters);
			close = false;
		} else {
			linear.setBackgroundResource(R.drawable.light_off);
			myBtn.setBackgroundResource(R.drawable.off);
			parameters.setFlashMode(Parameters.FLASH_MODE_OFF);// 关闭
			camera.setParameters(parameters);
			close = true;
		}
	}

    @Override
	protected void onStart() {
    	Log.e("onStart", "onStart");
    	mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    	vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    	super.onStart();
	}

	@Override
	protected void onPause() {
		Log.e("onPause", "onPause");
		camera.release();
		mSensorManager.unregisterListener(this);
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		Log.e("onResume", "onResume");
		//注册监听器
		camera = Camera.open();
    	mSensorManager.registerListener(this
    			, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
    	mSensorManager.registerListener(this
    			, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onStop() {
		Log.e("onStop", "onStop");
		mSensorManager.unregisterListener(this);
		super.onStop();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
	
	protected Animation animRotate(float toDegrees, float pivotXValue, float pivotYValue) {
		// TODO Auto-generated method stub
		animationRotate = new RotateAnimation(0, toDegrees, Animation.RELATIVE_TO_SELF, pivotXValue, Animation.RELATIVE_TO_SELF, pivotYValue);
		animationRotate.setAnimationListener(new AnimationListener() 
		{
			
			@Override
			public void onAnimationStart(Animation animation) 
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) 
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) 
			{
				// TODO Auto-generated method stub
				animationRotate.setFillAfter(true);
			}
		});
		return animationRotate;
	}
	
	protected Animation animTranslate(float toX, float toY, final int lastX, final int lastY,
			final Button button, long durationMillis) {
		// TODO Auto-generated method stub
		animationTranslate = new TranslateAnimation(0, toX, 0, toY);
		if (isClick) {
			animationTranslate.setInterpolator(AnimationUtils.loadInterpolator(this,  
                    android.R.anim.overshoot_interpolator));
		}else{
			animationTranslate.setInterpolator(AnimationUtils.loadInterpolator(this,  
                    android.R.anim.anticipate_interpolator));
		}
		animationTranslate.setAnimationListener(new AnimationListener()
		{
						
			@Override
			public void onAnimationStart(Animation animation)
			{
				// TODO Auto-generated method stub
								
			}
						
			@Override
			public void onAnimationRepeat(Animation animation) 
			{
				// TODO Auto-generated method stub
							
			}
						
			@Override
			public void onAnimationEnd(Animation animation)
			{
				// TODO Auto-generated method stub
				params = new LayoutParams(0, 0);
				params.height = 60;
				params.width = 60;											
				params.setMargins(lastX, lastY, 0, 0);
				button.setLayoutParams(params);
				button.clearAnimation();
				if (!isClick) {
					button.setVisibility(View.INVISIBLE);
				}
						
			}
		});																								
		animationTranslate.setDuration(durationMillis);
		return animationTranslate;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		//获取触发event的传感器类型
				int sensorType = event.sensor.getType();
				
				switch(sensorType){
				case Sensor.TYPE_ORIENTATION:
					float degree = event.values[0]; //获取z转过的角度
//					Log.e("获取z转过的角度", ""+degree);
					//穿件旋转动画
					RotateAnimation ra = new RotateAnimation(currentDegree,-degree,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
					ra.setDuration(100);//动画持续时间
					ImgCompass.startAnimation(ra);
					currentDegree = -degree;
					break;
				case Sensor.TYPE_ACCELEROMETER:
					// 传感器信息改变时执行该方法 
		            float[] values = event.values; 
		            float x = values[0]; // x轴方向的重力加速度，向右为正 
		            float y = values[1]; // y轴方向的重力加速度，向前为正 
		            float z = values[2]; // z轴方向的重力加速度，向上为正 
//		            Log.i(LOG_TAG, "x轴方向的重力加速度" + x +  "；y轴方向的重力加速度" + y +  "；z轴方向的重力加速度" + z); 
		            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。 
		            int medumValue = 19;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了 
		            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
		            	if (System.currentTimeMillis()-time > 1000) {
		            		vibrator.vibrate(200); 
			                Message msg = new Message(); 
			                msg.what = SENSOR_SHAKE; 
			                mhandler.sendMessage(msg);
						}
		            	time = System.currentTimeMillis(); 
		            }
					break;
				
				}
	}
}
