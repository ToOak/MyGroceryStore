package com.wochacha.scan.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.WindowManager;

import com.example.xushuailong.mygrocerystore.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

public class HardWare {

	private static volatile HardWare instance;

	private static int screenHeight = 0;
	private static int screenWidth = 0;
	private static int densityDpi = 0;

	private static String udid = "";
	private static String newUdid = "";
	private static String mac = "";


	private SparseArray<Handler> handlers = null;


    private static MediaPlayer mediaPlayer;
	private static final float BEEP_VOLUME = 0.10f;
	private static final long VIBRATE_DURATION = 200L;

	private HardWare(Context context) {
		setScreenScale(context);

		handlers = new SparseArray<>();


		if (context != null) {
			mac = getMacAddress(context);
			getDeviceInfo(context);
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


	private void setScreenScale(Context context) {
		if (screenHeight == 0 && context != null) {
			getScreenSize(context);
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


	/**
	 * get DeviceInfo info responded<br>
	 * organized as &dos=....., should be attached suffix
	 */
	@SuppressWarnings("deprecation")
	public void getDeviceInfo(Context con) {
		getUdid(con);

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






}
