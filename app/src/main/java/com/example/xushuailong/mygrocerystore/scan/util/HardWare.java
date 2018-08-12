package com.example.xushuailong.mygrocerystore.scan.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.xushuailong.mygrocerystore.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HardWare {
 	private final static String TAG = "HardWare";

	private static volatile HardWare instance;

	private static int screenHeight = 0;
	private static int screenWidth = 0;
	private static int densityDpi = 0;

	private static int status_bar_height = -1;
	private static String udid = "";
	private static String newUdid = "";
	private static String mac = "";
	private static String launchTimeStamp = "";//统计用


	private SparseArray<Handler> handlers = null;

	public final static int kNetType = 0;
	public final static int kWapType = 1;
	private final static int kWapType_172 = 3;
	private final static int kWapType_200 = 5;

	public static int netTryNum = 4;
	private static int netTryIndex = 0;
	private static int netTypeArray[] = new int[4];
	private static boolean netAvailable = false;
	public static boolean isWiFi = true;

 	private boolean forceLocation = true;
 //   private BroadcastReceiver mScreenActionReceiver;
    private static long ScreenOffTime = 0;

    private static MediaPlayer mediaPlayer;
	private static final float BEEP_VOLUME = 0.10f;
	private static final long VIBRATE_DURATION = 200L;

	private HardWare(Context context) {
		if (WccConstant.DEBUG)
			Log.d(TAG, "construct hardware");
		forceLocation = true;
//		disableConnectionReuseIfNecessary();
//		FileManager.mkDirs();
		setScreenScale(context);

		handlers = new SparseArray<Handler>();


		if (context != null) {
			mac = getMacAddress(context);
			getDeviceInfo(context);
			checkNetworkStatus(context);
		}
	}

	public static HardWare getInstance(Context wccApp) {
		if (instance == null) {
			synchronized(HardWare.class) {
				if (instance == null)
					instance = new HardWare(wccApp);
			}
		}
		return instance;
	}

	/**
	 *
	 * @param app
	 * @return height or -1;
	 */
	@SuppressWarnings("rawtypes")
	public static int getStatusBarHeight(Context app) {
		if(status_bar_height == -1)
			try{
				Class c = Class.forName("com.android.internal.R$dimen");
				Object obj = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = Integer.parseInt(field.get(obj).toString());
				status_bar_height = app.getResources().getDimensionPixelSize(x);
			}catch(Exception e){

			}
		 return status_bar_height;
	}

  //--------------------------------------- for-qrcode------------------------------------------------//
	public static void setScreenWidth(int width) {
		screenWidth = width;
	}

	public static void setScreenHeight(int height) {
		screenHeight = height;
	}

	private static void getScreenSize(Context con) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager manager = (WindowManager)con.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		densityDpi = metrics.densityDpi;
	}

	public static int getScreenWidth(Context app) {
		if (screenWidth == 0 && app != null) {
			getScreenSize(app);
		}
		return screenWidth;
	}

	public static int getScreenHeight(Context app) {
		if (screenHeight == 0 && app != null) {
			getScreenSize(app);
		}
		return screenHeight;
	}

	public static int getDensityDpi() {
		return densityDpi;
	}

	/**
	 *
	 * @param app
	 * @return scale[](width, height)
	 */
    public static int[] getScreenScale(Context app) {
    	if(screenWidth == 0 && app!=null) {
    		getScreenSize(app);
    	}
    	return new int[] {screenWidth, screenHeight};
    }

	private void setScreenScale(Context context) {
		if (screenHeight == 0 && context != null) {
			getScreenSize(context);
			if (WccConstant.DEBUG)
				Log.d(TAG, "ScreenScale, width="+screenWidth+", height="+screenHeight+", densityDpi="+densityDpi);
		}
	}

	private void Destroy() {

		if (handlers != null)
			handlers.clear();
		handlers = null;
	}

	public static void free() {
		if (instance != null)
			instance.Destroy();
		instance = null;
	}

	/**
	 * want get msg more comfortable and sharable, you must register a handler
	 * at assigned position using ACT_TAG and, <br>
	 * When you quit from an activity, you should remember to unregister the act
	 * handler
	 *
	 * @param TargetHandler
	 * @param ActTag
     */
	public void RegisterHandler(Handler TargetHandler, int ActTag) {
		if (handlers != null) {
			handlers.put(ActTag, TargetHandler);
		}
	}

	/**
	 * when you quit from an activity, you should remember to unregister the act
	 * handler
	 *
	 * @param ActTag
     */
	public void UnRegisterHandler(int ActTag) {
		if (handlers != null) {
			handlers.remove(ActTag);
		}
	}

	public static int[] getScale(double screenRatio) {
		int width = (int) (screenWidth * screenRatio);
		int height = (int) (screenHeight * screenRatio);
		return new int[] { width, height };
	}

	/**
	 *
	 * @param screenWidthRatio
	 * @param HeightWidthRatio Height/Width
	 * @return
	 */
	public static int[] getScale(double screenWidthRatio, double HeightWidthRatio) {
		int width = (int) (screenWidth * screenWidthRatio);
		int height = (int) (width * HeightWidthRatio);
		return new int[] { width, height };
	}

	/**
	 *
	 * @param Columns 列数
	 * @return
	 */
	public static int getCommonGridViewSpace(int Columns) {
		int space = 2;
		if(2 == Columns) {
			if(screenWidth>480)
				space = 18;
			else if(screenWidth>320)
				space = 16;
			else if(screenWidth>240)
				space = 10;
			else
				space = 8;
		}else if(3 == Columns) {
			if(screenWidth>480)
				space = 14;
			else if(screenWidth>320)
				space = 12;
			else if(screenWidth>240)
				space = 6;
			else
				space = 4;
		}
		return space;
	}

	public static int[] getCommonGalleryScale(double num, double SpaceWidthRatio, double HeightWidthRatio) {
		int width;
		int height;
		int space;
		width = (int)(screenWidth / num * (1-SpaceWidthRatio));
		height = (int)(width * HeightWidthRatio);
		space = (int)(screenWidth / num * SpaceWidthRatio);

		return new int[] { width, height, space };
	}



	public static int[] getSquareScale(double screenWidthRatio) {
		if(WccConstant.DEBUG) Log.i(TAG,"getSquareScale :: screenWidth = "+screenWidth);
		int width = (int) (screenWidth * screenWidthRatio);
		int height = width;
		return new int[] { width, height };
	}

	//为了确保‘自购、导购商品详情图标不显示(scales[0]=0,scales[1]=0)’的bug不出现
	public static int[] getSquareScale(Context con, double screenWidthRatio) {
		if(WccConstant.DEBUG) Log.i(TAG,"getSquareScale :: screenWidth = "+screenWidth);
		int width = (int) (getScreenWidth(con) * screenWidthRatio);
		int height = width;
		return new int[] { width, height };
	}


	private void checkNetworkStatus(Context context) {
		netTryIndex = 0;
		netTryNum = 4;
		netTypeArray[0] = kNetType;
		netTypeArray[1] = kWapType_172;
		netTypeArray[2] = kNetType;
		netTypeArray[3] = kWapType_200;

		if (isNetworkAvailableBySystem(context)) {
			netAvailable = true;
		}
		else {
			netAvailable = false;
		}
	}

	public static boolean isNetworkAvailable(Context context) {
		return netAvailable;
	}

	public static void setNetworkAvailable(boolean v) {
		netAvailable = v;
	}
	/**
	 * it is not always actual, but we are sure to return true. so, call it carefully
	 * @param context
	 * @return
	 */
	private static boolean isNetworkAvailableBySystem(Context context) {
		 try {
			 NetworkInfo info = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
			 if (info == null)
		    	 return false;

		     if (info.isAvailable() == false)
		         return false;
		     else
		    	 return true;
		 }
		 catch (Exception e) {
			 e.printStackTrace();
			 return false;
		 }
	}

	public static boolean isSIMCardAvailable(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		int simState = tm.getSimState();
		if (simState != TelephonyManager.SIM_STATE_ABSENT)
			return true;
		else
			return false;
	}

	public static boolean isSDCardAvailable() {
		boolean result = false;
		try {
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	/**
	 *
	 * @return false if remain space <= 50kb
	 */
	@SuppressWarnings("deprecation")
	public static boolean isSDCardFull() {
		if (isSDCardAvailable() == true) {
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs statFs = new StatFs(path.getPath());
				long blockSize = statFs.getBlockSize();
				long availableBlocks = statFs.getAvailableBlocks();
				if (availableBlocks * blockSize <= 50 * 1024) {
					return true;
				}
			} catch (Exception e) {
			}
		}
		return false;
	}

	/**
	 *
	 * @param needSpace  bytes
	 * @return
	 */
	public static boolean isSDCardEnoughSpace(long needSpace) {
		boolean isEnough = false;
		if (isSDCardAvailable()) {
			try {
				File path = Environment.getExternalStorageDirectory();
				long available = getFreeSpace(path.getPath());
				if (available >= (10 * 1024 + needSpace)) {
					isEnough = true;
				}
				if(isEnough == false) {
					// release some space 
				}
				return isEnough;
			} catch (Exception e) {
			}
		}
		return false;
	}

	/**
	 *
	 * @param path
	 * @return  in bytes
	 */
	@SuppressWarnings("deprecation")
	private static long getFreeSpace(String path) {
		StatFs stat = new StatFs(path);
		long blockSize = stat.getBlockSize();
		long freeBlocks = stat.getAvailableBlocks();
		return blockSize * freeBlocks;
	}

	/**
	 *
	 * @return in bytes
	 */
	public static long getFreeExternalSpace() {
		if (isSDCardAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			return getFreeSpace(path.getPath());
		}
		else {
			return -1;
		}
	}


	/**
	 *
	 * @return in bytes
	 */
	public static long getFreeInternalSpace() {
		File path = Environment.getDataDirectory();
		return getFreeSpace(path.getPath());
	}

	public static boolean isWifiAvailable(Context wcc) {
		WifiManager mWifiManager = (WifiManager) wcc.getSystemService(Context.WIFI_SERVICE);
		if (mWifiManager != null) {
			WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
			if (wifiInfo != null) {
				int ipAddress = wifiInfo.getIpAddress();
				if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
					isWiFi = true;
					return true;
				}
			}
		}
		isWiFi = false;
		return false;
	}


	public static void initBeepSound(Activity activity) {
		if ( mediaPlayer == null) {
			activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(null);
			AssetFileDescriptor file = activity.getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME,BEEP_VOLUME);
				mediaPlayer.prepare();
			}catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}
	public static  void playBeepSoundAndVibrate(Activity activity) {
	        try {
	            if (WccConfigure.getIsPlayBeep(activity)) {
	                initBeepSound(activity);
	                mediaPlayer.start();
	            }
	            if (WccConfigure.getIsVibrate(activity)) {
	                Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
	                vibrator.vibrate(VIBRATE_DURATION);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	public static  void releaseMediaPlayer(){
	  if(mediaPlayer!=null){
      	mediaPlayer.release();
      	mediaPlayer = null;
	  }
	}
	/**
	 *
	 * @return MAC or empty
	 */
	public static String getMacAddress(Context con) {
		try {
			WifiManager mWifiManager = (WifiManager) con.getSystemService(Context.WIFI_SERVICE);
			if (mWifiManager != null) {
				WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
				if (wifiInfo != null) {
					return wifiInfo.getMacAddress();
				}
			}
		} catch (Exception e) {
		}
		return "";
	}

	private static boolean checkUdidZero(String id) {
		try {
			int val = Integer.parseInt(id);
			if (val == 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	private static boolean checkUdidValid(String id) {
		if (Validator.isEffective(id) && id.length() >= 10 && !checkUdidZero(id) && !"9774d56d682e549c".equals(id)) { // SDK version 2.2, some devices have the same id
			return true;
		} else
			return false;
	}

	public static String getUdid(Context con) {
		if (checkUdidValid(udid) == false) {
			SharedPreferences sharepre = PreferenceManager.getDefaultSharedPreferences(con);
			udid = sharepre.getString(Constant.KeyDeviceID, "");
			if (checkUdidValid(udid) == false) {
				try {
					TelephonyManager tm = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
					udid = tm.getDeviceId();
				}
				catch (Exception e) {
					udid = "";
				}

				if (checkUdidValid(udid) == false) {
					if (Validator.isEffective(mac))
						udid = "MAC" + mac.replace(':', '0').replace('.', '0');
					else
						udid = "";
					if (checkUdidValid(udid) == false) {
						try {
							udid = Settings.Secure.getString(con.getContentResolver(), Settings.Secure.ANDROID_ID);
						}
						catch (Exception e) {
							udid = "";
						}
						if (checkUdidValid(udid) == false) {
							udid = getRandomUdidFromFile(con);
						}
					}
				}

				if (checkUdidValid(udid) == true) {
					Editor editor = sharepre.edit();
					editor.putString(Constant.KeyDeviceID, udid);
					editor.commit();
				}
			}
		}

		if (checkUdidValid(newUdid) == false) {
			SharedPreferences sharepre = PreferenceManager.getDefaultSharedPreferences(con);
			newUdid = sharepre.getString(Constant.KeyNewDeviceID, "");
			if (checkUdidValid(newUdid) == false) {
				String imei_id;
				String android_id;
				String mac_id;

				try {
					TelephonyManager tm = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
					imei_id = tm.getDeviceId();
				} 
				catch (Exception e) {
					imei_id = "";
				}

				if (Validator.isEffective(mac))
					mac_id = mac;
				else
					mac_id = "";
				
				try {
					android_id = Settings.Secure.getString(con.getContentResolver(), Settings.Secure.ANDROID_ID);
				} 
				catch (Exception e) {
					android_id = "";
				}
				
				newUdid = imei_id + mac_id + android_id;
				if (checkUdidValid(newUdid) == false) {
					newUdid = getRandomUdidFromFile(con);
				}

				newUdid = DataConverter.getMD5(newUdid.getBytes());
				if (checkUdidValid(newUdid) == true) {
					Editor editor = sharepre.edit();
					editor.putString(Constant.KeyNewDeviceID, newUdid);
					editor.commit();
				}
			}
		}
		
		return newUdid;
	}
	
	public static void getDeviceId(Context con, HashMap<String, String> map) {
		if (map == null) return;
		
		getUdid(con);
		
		map.put("udid", udid);
		map.put("newudid", newUdid);
		if (isWiFi)
			map.put("connectnet", "wifi");
		else
			map.put("connectnet", "mobile");
		if (Validator.isEffective(mac))
			map.put("mac", DataConverter.urlEncode(mac));
	}
	
	/**
	 * 
	 * @param con
	 * @return uuid + connectnet + mac
	 */
	public static String getDeviceId(Context con) {
		getUdid(con);

		String ret;
		if (isWiFi)
			ret = udid + "&newudid=" + newUdid + "&connectnet=wifi";
		else
			ret = udid + "&newudid=" + newUdid + "&connectnet=mobile";
		if (Validator.isEffective(mac))
			return ret + "&mac=" + DataConverter.urlEncode(mac);
		else
			return ret;
	}

	private synchronized static String getRandomUdidFromFile(Context context) {
		String id = "";
		File installation = new File(context.getFilesDir(), "INSTALLATION");
		try {
			if (!installation.exists())
				writeInstallationFile(installation);
			id = readInstallationFile(installation);
		} catch (Exception e) {
			id = "";
		}
		
		return id;
	}

	
	
	
	private static String readInstallationFile(File installation)
			throws IOException {
		RandomAccessFile f = new RandomAccessFile(installation, "r");
		byte[] bytes = new byte[(int) f.length()];
		f.readFully(bytes);
		f.close();
		return new String(bytes);
	}

	private static void writeInstallationFile(File installation)
			throws IOException {
		FileOutputStream out = new FileOutputStream(installation);
		String id = UUID.randomUUID().toString();
		out.write(id.getBytes());
		out.close();
	}

	public static String getPhoneImsi(Context context) {
		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			return tm.getSubscriberId();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * get DeviceInfo info responded<br>
	 * organized as &dos=....., should be attached suffix
	 */
	@SuppressWarnings("deprecation")
	public void getDeviceInfo(Context con) {
		getUdid(con);

		if (WccConstant.DEBUG)
			Log.e(TAG, " device info: BOARD=" + Build.BOARD + ", BRAND=" + Build.BRAND + ", DEVICE=" + Build.DEVICE
					+ ", DISPLAY=" + Build.DISPLAY
					// +", HARDWARE="+Build.HARDWARE
					+ ", ID=" + Build.ID + ", MANUFACTURER=" + Build.MANUFACTURER + ", MODEL=" + Build.MODEL
					+ ", PRODUCT=" + Build.PRODUCT + ", TYPE=" + Build.TYPE
					+ ", VERSION=" + Build.VERSION.SDK);
	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException ex) {
		}
		return null;
	}

	public static String getCPUSerial() {
		String cpuAddress = "0000000000000000";
		try {
			// 读取CPU信息
			Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			// 查找CPU序列号
			for (int i = 1; i < 100; i++) {
				String str = input.readLine();
				if (str != null) {
					// 查找到序列号所在行
					if (str.indexOf("Serial") > -1) {
						// 提取序列号
						String strCPU = str.substring(str.indexOf(":") + 1,
								str.length());
						// 去空格
						cpuAddress = strCPU.trim();
						break;
					}
				} else {
					// 文件结尾
					break;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			ex.printStackTrace();
		}
		return cpuAddress;
	}

	public static boolean isBackground(Context context) {
		try {
	 		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
			for (RunningAppProcessInfo appProcess : appProcesses) {
				if (appProcess.processName.equals(context.getPackageName())) {
					if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND
							&& appProcess.importance != RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
						return true;
					} else {
						return false;
					}
				}
			}
		}
		catch (Exception e) {
		}
		return false;
	}

	public static boolean isForeground(Context context) {
		try {
	 		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
			for (RunningAppProcessInfo appProcess : appProcesses) {
				if (appProcess.processName.equals(context.getPackageName())) {
					if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
							|| appProcess.importance == RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
						return true;
					} else {
						return false;
					}
				}
			}
		}
		catch (Exception e) {
			if (WccConstant.DEBUG)
				e.printStackTrace();
		}
		return false;
	}

	/**
	 * if you want to use this function, you have to register handlers at first
	 *
	 * @param what
     */
	public void sendMessage(int what) {
		if (handlers != null) {
			Message message;
			int size = handlers.size();

			for (int i = 0; i < size; i++) {
				Handler handler = handlers.valueAt(i);
				if (handler != null) {
					message = Message.obtain(handler, what);
					message.sendToTarget();
				}
			}
		}
	}

	/**
	 * if you want to use this function, you have to register handlers at first
	 *
	 * @param what
	 * @param obj
     */
	public void sendMessage(int what, Object obj) {
		if (handlers != null) {
			Message message;
			int size = handlers.size();

			for (int i = 0; i < size; i++) {
				Handler handler = handlers.valueAt(i);
				if (handler != null) {
					message = Message.obtain(handler, what, obj);
					message.sendToTarget();
				}
			}
		}
	}


	/**
	 * if you want to use this function, you have to register handlers at first
	 *
	 * @param what
	 * @param arg1
	 * @param arg2
     */
	public void sendMessage(int what, int arg1, int arg2) {
		if (handlers != null) {
			Message message;
			int size = handlers.size();

			for (int i = 0; i < size; i++) {
				Handler handler = handlers.valueAt(i);
				if (handler != null) {
					message = Message.obtain(handler, what, arg1, arg2);
					message.sendToTarget();
				}
			}
		}
	}

	/**
	 * if you want to use this function, you have to register handlers at first
	 *
	 * @param what
	 * @param arg1
	 * @param arg2
     * @param obj
     */
	public void sendMessage(int what, int arg1, int arg2, Object obj) {
		if (handlers != null) {
			Message message;
			int size = handlers.size();

			for (int i = 0; i < size; i++) {
				Handler handler = handlers.valueAt(i);
				if (handler != null) {
					message = Message.obtain(handler, what, arg1, arg2, obj);
					message.sendToTarget();
				}
			}
		}
	}

	public static void sendMessage(Handler targetHandler, int what, int arg1, int arg2, Object obj) {
//		try {
		Log.e("xsl","cccc");
			if (targetHandler != null) {
				Message message = Message.obtain(targetHandler, what, arg1, arg2, obj);
				message.sendToTarget();
			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public static void sendMessage(Handler targetHandler, int what, int arg1, int arg2) {
		try {
			if (targetHandler != null) {
				Message message = Message.obtain(targetHandler, what, arg1, arg2);
				message.sendToTarget();
			}
		} catch (Exception e) {
		}
	}

	public static void sendMessage(Handler targetHandler, int what, Object obj) {
		try {
			if (targetHandler != null) {
				Message message = Message.obtain(targetHandler, what, obj);
				message.sendToTarget();
			}
		} catch (Exception e) {
		}
	}

	public static void sendMessage(Handler targetHandler, int what) {
		try {
			if (targetHandler != null) {
				Message message = Message.obtain(targetHandler, what);
				message.sendToTarget();
			}
		} catch (Exception e) {
		}
	}

	public static void sendMessageDelayed(Handler targetHandler, int what, long delayMillis) {
		if(delayMillis <= 0) {
			sendMessage(targetHandler, what);
			return;
		}

		try {
			if (targetHandler != null) {
				Message message = Message.obtain(targetHandler, what);
				targetHandler.sendMessageDelayed(message, delayMillis);
			}
		} catch (Exception e) {
		}
	}


	public static void sendMessageDelayed(Handler targetHandler, int what, Object obj, long delayMillis) {
		if(delayMillis <= 0) {
			sendMessage(targetHandler, what, obj);
			return;
		}
		try {
			if (targetHandler != null) {
				Message message = Message.obtain(targetHandler, what, obj);
				targetHandler.sendMessageDelayed(message, delayMillis);
			}
		} catch (Exception e) {
		}
	}

	public static final int ACTION_DIAL = 0;
	public static final int ACTION_SMS = 1;

	public boolean checkSIM(Context context, int type) {
		if (isSIMCardAvailable(context) == false) {
			AlertDialog.Builder b = new AlertDialog.Builder(context);
			b.setTitle("提示");
			if (type == ACTION_DIAL)
				b.setMessage("未检测到SIM卡，无法拨打电话!");
			else
				b.setMessage("未检测到SIM卡，无法发送短信!");
			b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			b.show();
			return false;
		}
		return true;
	}


//	public static boolean needRotate180Layout() {
//    	if (Build.MANUFACTURER.equals("HTC") && Build.MODEL.equals("HTC A810e"))
//    		return true;
//    	else
//    		return false;
//    }

    public static void unbindDrawables(View view) {
		if(view == null) return;
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			try {
				((ViewGroup) view).removeAllViews();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}



	public static void ToastShort(Context context, String msg) {
		if(Validator.isEffective(msg)){
			try{
//				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
				JCToast.getInstance(context).toastShort(msg);
			}catch(Exception ex){}
		}
	}


	public static void ToastLong(Context context, String msg) {
		if(Validator.isEffective(msg)){
			try{
//				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
				JCToast.getInstance(context).toastLong(msg);
			}catch(Exception ex){}
		}
	}


	/**
	 * will be reset after calling
	 *
	 * @return
     */
	public static long resetScreenOffElapseTime() {
		if(WccConstant.DEBUG)
 			Log.e(TAG, "resetScreenOffElapseTime, ScreenOffTime="+ScreenOffTime);

		if(ScreenOffTime != 0) {
			long elapseTime = System.currentTimeMillis() - ScreenOffTime;
			ScreenOffTime = 0;
			return elapseTime;
		}
		return 0;

	}

	/**
	 *
	 * @param context
	 * @return the uid or -1;
	 */
	public static int getUidByName(Context context, String processName){
		if(processName == null)
			return -1;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
 		List<RunningAppProcessInfo> runnings = am.getRunningAppProcesses();
		for(RunningAppProcessInfo runningAppProcessInfo : runnings) {
		     if(processName.equals(runningAppProcessInfo.processName))
		        	return  runningAppProcessInfo.uid;
		}
        return -1;
	}

	/**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
    	DisplayMetrics dm = context.getResources().getDisplayMetrics();
		// update HardWare.densityDpi
		densityDpi = dm.densityDpi;
		final float scale = dm.density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
    	DisplayMetrics dm = context.getResources().getDisplayMetrics();
		// update HardWare.densityDpi
		densityDpi = dm.densityDpi;
		final float scale = dm.density;
        return (int) (pxValue / scale + 0.5f);
    }

    private static boolean invokeIsDocumentUri(Context context, Uri uri) {
    	try {
    		Class<?> ownerClass = Class.forName("android.provider.DocumentsContract");
    		Method method = ownerClass.getMethod("isDocumentUri", Context.class, Uri.class);
    		return (Boolean)(method.invoke(null, context, uri));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
    }

    private static String invokeGetDocumentId(Uri uri) {
    	try {
    		Class<?> ownerClass = Class.forName("android.provider.DocumentsContract");
    		Method method = ownerClass.getMethod("getDocumentId", Uri.class);
    		return (String)(method.invoke(null, uri));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "";
		}
    }
    
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= 19;
        
        boolean isDocumentUri = false;
        String documentId = "";
        isDocumentUri = invokeIsDocumentUri(context, uri);
        documentId = invokeGetDocumentId(uri);
        // DocumentProvider
        if (isKitKat && isDocumentUri) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = documentId;
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = documentId;
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = documentId;
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    
    public static void setViewLayoutParams(View view, double screenWidthRatio, double HeightWidthRatio) {
		if(view != null) {
			int[] scale = getScale(screenWidthRatio, HeightWidthRatio);
			view.getLayoutParams().width = scale[0];
			view.getLayoutParams().height = scale[1];
		}
	}
    
    public static View getDivideLine(Context context) {
    	View view = new View(context);
    	LayoutParams params = new LayoutParams(getScreenWidth(context), 1);
    	view.setLayoutParams(params);
    	view.setBackgroundColor(context.getResources().getColor(R.color.wcc_color_2));
    	return view;
    }
    
	/**
	 * 拼接接口参数时从这里读取，避免每次读取sharedpreference
	 * 
	 * @param context
	 * @return
	 */
    public static String getLaunchTimeStamp(Context context) {
    	if (!Validator.isEffective(launchTimeStamp)) {
    		launchTimeStamp = WccConfigure.getHisTimeStamp(context);
		}
		return launchTimeStamp.replace(",", ".");	
    }
    
	/**
	 * 启动后设置时间后，在这里赋值一次
	 * 
	 * @param context
	 */
    public static void setLaunchTimeStamp(Context context) {
    	launchTimeStamp = WccConfigure.getHisTimeStamp(context);
    }

	/**
	 *
	 * @param context
	 * @return 返回true，则表示输入法打开
     */
	public static boolean getKeyboardStatus(Context context){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.isActive();
	}

	/**
	 * 如果输入法在窗口上已经显示，则隐藏，反之则显示
	 * @param context
     */
	public static void showOrHideKeyBoard(Context context){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 强制显示
	 * @param context
	 * @param view
     */
	public static void showKeyboard(Context context, View view){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}


	/**
	 * 强制隐藏
	 * @param context
	 * @param view
     */
	public static void hideKeyboard(Context context, View view){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
}
