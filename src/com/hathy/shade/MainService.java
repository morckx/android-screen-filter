package com.hathy.shade;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.util.Log;

/**
* This is the class that is responsible for adding the filter on the
* screen: It works as a service, so that the view persists across all
* activities.
*
* @author Hathibelagal
*/
public class MainService extends Service {
  public final static String TAG = "shade.MainService";
	LinearLayout mView;
	SharedMemory shared;
	
	public static int STATE;
	
	public static final int INACTIVE=0;
	public static final int ACTIVE=0;	
	
	static{
		STATE=INACTIVE;
	}
	
	@Override
	public IBinder onBind(Intent i) {
		return null;
	}

	@Override
	public void onCreate() {
	        super.onCreate();
        	shared=new SharedMemory(this);
	        mView = new LinearLayout(this);   
        	mView.setBackgroundColor(shared.getColor());
	        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                	WindowManager.LayoutParams.MATCH_PARENT,
        	        WindowManager.LayoutParams.MATCH_PARENT,
	                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                	WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
									| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
									| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, /* dim statusbar, too */
        	        PixelFormat.TRANSLUCENT);        
					if(shared.getBblOff()) {
							/* from https://github.com/TomTasche/ScreenFilter.git */
							try {
									WindowManager.LayoutParams.class.getField("buttonBrightness").set(params, Integer.valueOf(0));
							}
							catch (Exception ex) {
									Log.w(TAG, "Cannot set button brightness");
									ex.printStackTrace();
							}
					}
	        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        	wm.addView(mView, params);
	}
	
	@Override
	public void onDestroy() {			
		super.onDestroy();
		if(mView!=null){
			WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
			wm.removeView(mView);
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {		
		mView.setBackgroundColor(shared.getColor());
		return super.onStartCommand(intent, flags, startId);
	}
}
