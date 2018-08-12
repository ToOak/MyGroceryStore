package com.example.xushuailong.mygrocerystore.scan.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.CharBuffer;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Locale;

public class FileManager {
    private final static String TAG = "FileManager";
    private static final String rootEx = Environment.getExternalStorageDirectory() + "/WccJC/";
    private static final String CacheRootEx = Environment.getExternalStorageDirectory() + "/WccJCCache/";
    private static final String rootIn = "/data/data/com.wochacha.johnsoncounter/files/";
    public static final String DbRoot = "/data/data/com.wochacha.johnsoncounter/databases/";
    private static final String webviewCache = "/data/data/com.wochacha.johnsoncounter/cache/";

    private static final String DownloadDirEx = rootEx + "Download/";
//	private static final String InspectImagesDirEx = rootEx + "Report/";

    private static final String imagesDirEx = CacheRootEx;
    private static final String EternalimagesDirEx = CacheRootEx + "other/";
    private static final String BrandImagesDirEx = CacheRootEx + "brand/";
    private static final String AudioCacheDirEx = CacheRootEx + "audio/";
    private static final String PosterImagesDirEx = CacheRootEx + "poster/";
    private static final String CardImagesDirEx = CacheRootEx + "card/";
    private static final String UploadDirEx = CacheRootEx + "upload/";
    private static final String LuxuryDirEx = CacheRootEx + "luxury/";

    private static final String imagesDirIn = rootIn + "Images/";
    private static final String tmpDirIn = rootIn + "tmp/";
    private static final String tmpDirEx = CacheRootEx + "tmp/";
    private static final String tmpAudioDirIn = tmpDirIn + "audio/";
    private static final String tmpAudioDirEx = tmpDirEx + "audio/";
    private static final String LogsPath = rootIn + "Logs/";

    private static final String chatDirEx = rootEx + "chat/";
    private static final String chatImagesEx = chatDirEx + "images/";
    private static final String chatAudioEx = chatDirEx + "audio/";

    private static final String ExposureCategoryLog = LogsPath + "epc_.wcc";

    private static final String TmpImgEx = CacheRootEx + "wcc_tmp.jpg";
    private static final String TmpImgExCommAll = CacheRootEx + "wcc_tmp_commall.jpg";
    private static final String TmpImgExCommIntro = CacheRootEx + "wcc_tmp_commintro.jpg";
    private static final String TmpImgExComm1 = CacheRootEx + "wcc_tmp_comm1.jpg";
    private static final String TmpImgExComm2 = CacheRootEx + "wcc_tmp_comm2.jpg";
    private static final String TmpImgExComm3 = CacheRootEx + "wcc_tmp_comm3.jpg";
    private static final String TmpImgExComm4 = CacheRootEx + "wcc_tmp_comm4.jpg";
    private static final String TmpImgExCommInvoice = CacheRootEx + "wcc_tmp_comminvoice.jpg";

    private static final String TmpImgIn = rootIn + "wcc_tmp.jpg";
    private static final String appfilePrefix = "wochacha_update_";

    public static final String BOUNDARY = "7cd4a6d158c";
    static final String MP_BOUNDARY = "--" + BOUNDARY;

    public static boolean mkdir(String dir) {
        try {
            return new File(dir).mkdir();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * default make 1)ex root,images;2)in images,logs
     */
    public static void mkDirs() {
//		try {
//			new File(rootEx).mkdir();
//			new File(CacheRootEx).mkdir();
//			new File(imagesDirEx).mkdir();
//			new File(EternalimagesDirEx).mkdir();
//			new File(tmpDirEx).mkdir();
//			new File(chatDirEx).mkdir();
//			new File(DownloadDirEx).mkdir();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
        try {
            new File(rootIn).mkdir();
            new File(imagesDirIn).mkdir();
            new File(LogsPath).mkdir();
            new File(tmpDirIn).mkdir();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isFileExist(String filename) {
        if (filename == null) return false;
        File file = new File(filename);
        return file.exists();
    }

    /**
     * ignore case
     */
    public static class FileNameFilter2 implements FileFilter {
        String Prefix;
        int MIMEType;

        /**
         * @param prefix ignore case
         * @param Type   0 all, 1 image
         */
        public FileNameFilter2(String prefix, int mimetype) {
            Prefix = prefix.toLowerCase(Locale.ENGLISH);
            MIMEType = mimetype;
        }

        public boolean accept(File file) {
            String filename = file.getName().toLowerCase(Locale.ENGLISH);

            if (filename.startsWith(Prefix)) {
                switch (MIMEType) {
                    case 1:
                        if (file.isDirectory() || filename.endsWith(".jpg") || filename.endsWith(".jpeg")
                                || filename.endsWith(".png"))
                            return true;
                        else
                            return false;
                    default:
                        return true;
                }
            }

            return false;
        }
    }

    public static String getRootIn() {
        mkdir(rootIn);
        return rootIn;
    }

    public static String getRootEx() {
        mkdir(rootEx);
        return rootEx;
    }

    public static String getCacheRootEx() {
        mkdir(CacheRootEx);
        return CacheRootEx;
    }

    public static String getExDownLoadPath() {
        mkdir(rootEx);
        mkdir(DownloadDirEx);
        return DownloadDirEx;
    }

    public static String getExImagesPath() {
        mkdir(imagesDirEx);
        return imagesDirEx;
    }

    public static String getExBrandImagesPath() {
        mkdir(CacheRootEx);
        mkdir(BrandImagesDirEx);
        return BrandImagesDirEx;
    }


    public static String getExPosterImagesPath() {
        mkdir(CacheRootEx);
        mkdir(PosterImagesDirEx);
        return PosterImagesDirEx;
    }

    public static String getExCardImagesPath() {
        mkdir(CacheRootEx);
        mkdir(CardImagesDirEx);
        return CardImagesDirEx;
    }


    public static String getExUploadPath() {
        mkdir(CacheRootEx);
        mkdir(UploadDirEx);
        return UploadDirEx;
    }

    public static String getExLuxuryPath() {
        mkdir(CacheRootEx);
        mkdir(LuxuryDirEx);
        return LuxuryDirEx;
    }

    public static String getExWccAudioPath() {
        mkdir(CacheRootEx);
        mkdir(AudioCacheDirEx);
        return AudioCacheDirEx;
    }

//	public static String getExInspectImagesPath() {
//		mkdir(rootEx);
//		mkdir(InspectImagesDirEx);
//		return InspectImagesDirEx;
//	}

    public static String getExEternalImagesPath() {
        mkdir(CacheRootEx);
        mkdir(EternalimagesDirEx);
        return EternalimagesDirEx;
    }

    public static String getInnerImagesPath() {
        mkdir(imagesDirIn);
        return imagesDirIn;
    }

    public static String getTmpDirPath() {
        mkdir(tmpDirIn);
        return tmpDirIn;
    }

    public static String getExTmpDirPath() {
        mkdir(tmpDirEx);
        return tmpDirEx;
    }

    public static String getInTmpAudioDirPath() {
        mkdir(tmpDirIn);
        mkdir(tmpAudioDirIn);
        return tmpAudioDirIn;
    }

    public static String getExTmpAudioDirPath() {
        mkdir(tmpDirEx);
        mkdir(tmpAudioDirEx);
        return tmpAudioDirEx;
    }

    public static String getExChatImagesPath() {
        mkdir(chatDirEx);
        mkdir(chatImagesEx);
        return chatImagesEx;
    }

    public static String getExChatAudioPath() {
        mkdir(chatDirEx);
        mkdir(chatAudioEx);
        return chatAudioEx;
    }


//	public static String getLogPath() {
//		mkdir(LogsPath);
//		return LogsPath;
//	}

    public static String getTempHtmlPath(boolean odd) {
        return getTempHtmlPath("" + odd);
    }

    public static String getTempHtmlPath(String tag) {
        return getTmpDirPath() + tag + "_tmp.html";
    }

    public static String getExposureCategoryLogPath() {
        return ExposureCategoryLog;
    }


    public static String getExTempImgPath() {
        return TmpImgEx;
    }

    public static String getExTempCommAllImgPath() {
        return TmpImgExCommAll;
    }

    public static String getExTempCommIntroImgPath() {
        return TmpImgExCommIntro;
    }

    public static String getExTempComm1ImgPath() {
        return TmpImgExComm1;
    }

    public static String getExTempComm2ImgPath() {
        return TmpImgExComm2;
    }

    public static String getExTempComm3ImgPath() {
        return TmpImgExComm3;
    }

    public static String getExTempComm4ImgPath() {
        return TmpImgExComm4;
    }

    public static String getExTempCommInvoiceImgPath() {
        return TmpImgExCommInvoice;
    }

    public static String getTempImgPath() {
        return TmpImgIn;
    }

    public static String getUpdateFilePrefix() {
        return appfilePrefix;
    }

    public static boolean renameFile(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        if (newFile.exists()) {
            newFile.delete();
        }
        boolean result = oldFile.renameTo(newFile);
        if (result) {
            oldFile.delete();
        }
        return result;
    }

    /**
     * get file name from download url
     *
     * @param url
     * @return
     */
    public static String getFileName(String url) {
        if (!Validator.isEffective(url))
            return "";
        String[] s = url.split("/");
        if (s == null) {
            return "";
        }
        return s[s.length - 1];
    }

    /**
     * recursively delete files and dirs, could be interrupted
     *
     * @param file
     */
    private static void deleteAllFiles(File file) {
        if (file == null)
            return;
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                int len = files.length;
                for (int i = 0; i < len; i++) {
                    if (Thread.currentThread().isInterrupted())
                        return;
                    deleteAllFiles(files[i]);
                }
            }
            file.delete();
        } else {
        }
    }

    /**
     * will delete all files and dirs in it ,including itself; could be interrupted
     *
     * @param dir
     */
    public static void deleteDir(String dir) {
        File file = new File(dir);
        deleteAllFiles(file);

    }

    /**
     * will delete all files in it ,excluding itself;
     *
     * @param dir
     */
    public static void deleteFilesInDir(String dir) {
        deleteFiles(new File(dir).listFiles(), null);
    }

    public static boolean deleteFile(String dir) {
        try {
            File file = new File(dir);
            return file.delete();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param OutOfHours
     */
    public static void deleteOldImageFiles(int OutOfHours) {
        deleteDirs(new String[]{getExImagesPath()}, new FileTimeFilter(OutOfHours));
    }

    /**
     * @param OutOfHours
     */
    public static void deleteOldBrandImageFiles(int OutOfHours) {
        deleteDirs(new String[]{getExBrandImagesPath()}, new FileTimeFilter(OutOfHours));
    }

    /**
     * @param OutOfHours
     */
    public static void deleteOldFiles(String path, int OutOfHours) {
        deleteDirs(new String[]{path}, new FileTimeFilter(OutOfHours));
    }

    /**
     * will delete outDate files of poster
     *
     * @param OutOfDay (yesterday is 1)
     */
    public static void deleteOldPosterImageFiles(int OutOfDay) {

        File[] files = new File(getExPosterImagesPath()).listFiles(new FileNameAsDateFilter(OutOfDay));
        if (files == null)
            return;
        int len = files.length;
        for (int i = 0; i < len; i++) {
            if (Thread.currentThread().isInterrupted())
                return;
            deleteDir(files[i].getPath());
        }
    }

    /**
     * will delete outDate files of chat
     *
     * @param OutOfDay (yesterday is 1)
     */
    public static void deleteOldChatFiles(int OutOfDay) {
        deleteDirs(new String[]{getExChatImagesPath(), getExChatAudioPath()}, new FileTimeFilter(OutOfDay * 24));
    }

    /**
     * @param OutOfHours
     */
    public static void deleteInnerImageFiles(int OutOfHours) {
        deleteDirs(new String[]{getInnerImagesPath()}, new FileTimeFilter(OutOfHours));
    }

    public static void deleteWebCache() {
        deleteDir(webviewCache);
        FileManager.deleteFile(FileManager.DbRoot + "webview.db");
        FileManager.deleteFile(FileManager.DbRoot + "webviewCookiesChromium.db");
    }

    public static void deleteUserProfiles() {
        deleteDirs(new String[]{getInnerImagesPath(), getExImagesPath()}, new FileNameFilter("user"));
    }

    public static void deleteTmpDir() {
        deleteDirs(new String[]{getTmpDirPath(), getExTmpDirPath()}, null);
    }

    public static void deleteTmpAudios() {
        deleteDirs(new String[]{getInTmpAudioDirPath(), getExTmpAudioDirPath()}, null);
    }

    /**
     * could be interrupted<br>
     * not recursion, just the first level
     *
     * @param dirs
     * @param filter
     */
    private static void deleteDirs(String[] dirs, FileFilter filter) {
        if (dirs == null)
            return;
        int len = dirs.length;
        for (int i = 0; i < len; i++) {
            if (Thread.currentThread().isInterrupted())
                return;
            deleteFiles(new File(dirs[i]).listFiles(filter), filter);
        }
    }


    /**
     * could be interrupted<br>
     * not recursion, just the first level
     *
     * @param files
     */
    private static void deleteFiles(File[] files, FileFilter filter) {
        if (files == null)
            return;
        int len = files.length;
        for (int i = 0; i < len; i++) {
            if (Thread.currentThread().isInterrupted())
                return;
            files[i].delete();

        }
    }

    /**
     * @param filename
     * @return the length of this file in bytes. Returns 0 if the file does not
     * exist. The result for a directory is not defined.
     */
    public static long getFileSize(String filename) {
        if (filename == null)
            return 0;
        File file = new File(filename);
        return file.length();
    }

    /**
     * write and override the file with given content
     *
     * @param filename where the content will be stored
     * @param content
     */
    public static boolean wOverride(String filename, String content) {
        try {
            // write override
            File a = new File(filename);
            a.createNewFile();
            FileWriter writer = new FileWriter(filename);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * read entire file as strings
     *
     * @param filename
     * @return result or 255
     */

    public static String r(String filename) {
        try {
            FileReader reader = new FileReader(filename);
            StringBuilder sb = new StringBuilder();
            CharBuffer buf = CharBuffer.allocate(1024);
            while (reader.read(buf) >= 0) {
                buf.flip();
                sb.append(buf.toString());
            }
            reader.close();
            return sb.toString();
        } catch (Exception e) {
            return "255";
        }
    }

    public static void changeMod(String Mod, String FilePath) {
        Process p;
        int status;
        try {
            p = Runtime.getRuntime().exec("chmod " + Mod + " " + FilePath);
            status = p.waitFor();
            if (status == 0) {

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean copyFile(String destfilePath, InputStream source) {
        if (source == null) {
            return false;
        }
        Process p;
        try {
            p = Runtime.getRuntime().exec("chmod 777 " + destfilePath);
            p.waitFor();

        } catch (Exception e) {
        }

        try {
            FileOutputStream fos = new FileOutputStream(destfilePath);
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = source.read(buffer)) > 0) {
                fos.write(buffer, 0, count);

            }
            fos.close();
            source.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean copyFile(String Destdir, String Destfilename, InputStream source) {
        if (source == null) {
            return false;
        }
        String filepath = Destdir + "/" + Destfilename;
        File d = new File(Destdir);

        if (!d.exists())
            d.mkdir();
        return copyFile(filepath, source);
    }

    /**
     * Equivalent to copyFile(Destdir,Destfilename,new
     * FileInputStream(SrcfilePath) ).
     *
     * @param Destdir
     * @param Destfilename
     * @param SrcfilePath
     */
    public static boolean copyFile(String destFilePath, String srcFilePath) {
        try {
            return copyFile(destFilePath, new FileInputStream(srcFilePath));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Equivalent to copyFile(Destdir,Destfilename,new
     * FileInputStream(SrcfilePath) ).
     *
     * @param Destdir
     * @param Destfilename
     * @param SrcfilePath
     */
    public static boolean copyFile(String Destdir, String Destfilename, String SrcfilePath) {
        try {
            return copyFile(Destdir, Destfilename, new FileInputStream(SrcfilePath));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Equivalent to appendBlock(DestFilePath,new FileInputStream(SrcfilePath)
     * ).
     *
     * @param DestFilePath
     * @param SrcfilePath
     */
    public static boolean appendBlock(String DestFilePath, String SrcfilePath) {
        try {
            return appendBlock(DestFilePath, new FileInputStream(SrcfilePath));
        } catch (Exception e) {
            // TODO Auto-generated catch block

        }
        return false;
    }

    /**
     * append block from source to destFilePath
     *
     * @param DestFilePath
     * @param source
     * @return
     */
    public static boolean appendBlock(String DestFilePath, InputStream source) {
        if (source == null) {
            return false;
        }

        try {
            FileOutputStream fos = new FileOutputStream(DestFilePath, true);
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = source.read(buffer)) > 0) {
                fos.write(buffer, 0, count);

            }
            fos.close();
            source.close();

        } catch (Exception e) {

            return false;
        }
        return true;
    }

    /**
     * @param FilePath
     * @return empty or MD5
     */
    public static String getMD5(String FilePath) {
        try {
            byte[] buf = new byte[1024];
            MessageDigest digester = MessageDigest.getInstance("MD5");
            FileInputStream in = new FileInputStream(FilePath);
            int byteCount;
            while ((byteCount = in.read(buf)) > 0) {
                digester.update(buf, 0, byteCount);
            }
            buf = null;
            try {
                in.close();
                in = null;
            } catch (Exception e) {
                in = null;
            }
            String digest = DataConverter.convertMD5(digester.digest());
            return digest;
        } catch (Exception e) {

        }
        return "";
    }

    /**
     * check file.lastModified
     */
    static class FileTimeFilter implements FileFilter {
        int hours;
        long time;

        public FileTimeFilter(int OutOfHours) {
            hours = OutOfHours;
            time = new Date().getTime();
        }

        public boolean accept(File file) {
            long m = file.lastModified();
            long n = hours * 3600L * 1000L;
            if ((time - m) > n)
                return true;
            else
                return false;
        }
    }


    static class FileNameAsDateFilter implements FileFilter {
        int OutOfDay;
        String CurDay;

        public FileNameAsDateFilter(int outOfDay) {
            OutOfDay = outOfDay;
            CurDay = VeDate.getCurDay();
        }

        public boolean accept(File file) {
            String filename = file.getName();
            if (VeDate.DaysBetween(CurDay, filename) > OutOfDay)
                return true;
            else
                return false;

        }
    }

    static class FileNameFilter implements FileFilter {
        String Key;

        public FileNameFilter(String key) {
            Key = key;
        }

        public boolean accept(File file) {
            String filename = file.getName();
            if (filename.contains(Key))
                return true;
            else
                return false;
        }
    }

    public static class ImageFileFilter implements FileFilter {
        public boolean accept(File file) {
            String filename = file.getName().toLowerCase(Locale.ENGLISH);
            if (file.isDirectory() || filename.endsWith(".jpg")
                    || filename.endsWith(".jpeg") || filename.endsWith(".png"))
                return true;
            else
                return false;
        }
    }

    public static class ForceImageFileFilter implements FileFilter {
        public boolean accept(File file) {
            String filename = file.getName().toLowerCase(Locale.ENGLISH);
            if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png"))
                return true;
            else
                return false;
        }
    }

    public static boolean isApk(String file) {
        if (file == null)
            return false;
        if (file.endsWith(".apk"))
            return true;
        return false;
    }

    public static boolean isVideo(String file) {
        if (file == null)
            return false;
        if (file.endsWith(".mp4"))
            return true;
        if (file.endsWith(".avi"))
            return true;
        if (file.endsWith(".rm"))
            return true;
        if (file.endsWith(".rmvb"))
            return true;
        if (file.endsWith(".wmv"))
            return true;
        if (file.endsWith(".mkv"))
            return true;
        if (file.endsWith(".mpe"))
            return true;
        if (file.endsWith(".mpg"))
            return true;
        if (file.endsWith(".mpg4"))
            return true;
        if (file.endsWith(".mpga"))
            return true;
        if (file.endsWith(".mpeg"))
            return true;
        if (file.endsWith(".mov"))
            return true;
        if (file.endsWith(".3gp"))
            return true;
        if (file.endsWith(".m4v"))
            return true;
        if (file.endsWith(".m4u"))
            return true;
        if (file.endsWith(".flv"))
            return true;
        if (file.endsWith(".asf"))
            return true;
        return false;
    }

    public static boolean isAudio(String file) {
        if (file == null)
            return false;
        if (file.endsWith(".mp2"))
            return true;
        if (file.endsWith(".mp3"))
            return true;
        if (file.endsWith(".m4a"))
            return true;
        if (file.endsWith(".m4b"))
            return true;
        if (file.endsWith(".m4p"))
            return true;

        if (file.endsWith(".ape"))
            return true;
        if (file.endsWith(".ogg"))
            return true;
        if (file.endsWith(".wma"))
            return true;
        if (file.endsWith(".flac"))
            return true;
        if (file.endsWith(".m3u"))
            return true;
        return false;

    }

    public static boolean isImage(String file) {
        if (file.endsWith(".jpg"))
            return true;
        if (file.endsWith(".jpeg"))
            return true;
        if (file.endsWith(".png"))
            return true;
        if (file.endsWith(".gif"))
            return true;
        return false;
    }

    public static boolean isDocument(String file) {
        if (file == null)
            return false;
        if (file.endsWith(".pdf"))
            return true;
        if (file.endsWith(".doc"))
            return true;
        if (file.endsWith(".docx"))
            return true;
        if (file.endsWith(".ppt"))
            return true;
        if (file.endsWith(".pptx"))
            return true;
        if (file.endsWith(".xls"))
            return true;
        if (file.endsWith(".xlsx"))
            return true;
        if (file.endsWith(".unkown"))
            return true;
        return false;

    }

    public static boolean isTxt(String file) {
        if (file == null)
            return false;
        if (file.endsWith(".txt"))
            return true;
        if (file.endsWith(".csv"))
            return true;
        if (file.endsWith(".xml"))
            return true;
        if (file.endsWith(".csv"))
            return true;
        if (file.endsWith(".csv"))
            return true;
        if (file.endsWith(".csv"))
            return true;

        return false;
    }

    public static boolean isZip(String file) {
        if (file == null)
            return false;
        if (file.endsWith(".rar"))
            return true;
        if (file.endsWith(".zip"))
            return true;
        return false;
    }

    public static String getFormat(String file) {
        if (WccConstant.DEBUG) Log.i(TAG, "getFormat :: file = " + file);
        if (!Validator.isEffective(file))
            return null;
        String name = "";
        if (file.contains("/")) {
            String[] tempAry = file.split("/");
            if (tempAry != null && tempAry.length > 0) {
                name = tempAry[tempAry.length - 1];
            }
        } else {
            name = file;
        }
        if (WccConstant.DEBUG) Log.i(TAG, "getFormat :: name = " + name);
        if (name.contains(".")) {
            String[] nameAry = name.split("\\.");
            if (WccConstant.DEBUG) Log.i(TAG, "getFormat :: nameAry = " + nameAry);
            if (nameAry != null) {
                if (WccConstant.DEBUG)
                    Log.i(TAG, "getFormat :: nameAry.length = " + nameAry.length);
                if (nameAry.length > 0) {
                    return nameAry[nameAry.length - 1];
                }
            }
        }
        return name;
    }

    public static String makeHtmlForImageShowAsWebdoc(String imgPath, String protocol, int w, int h) {
        StringBuffer content = new StringBuffer();

        content.append("<html><head><meta name=\"viewport\" content=\"width=device-width, inital-scale=1.0, user-scalable=no\" /><style> * { margin:0; padding:0; } </style></head><body>");
        if (Validator.isEffective(protocol)) {
            content.append("<a href=\"");
            content.append(protocol);
            content.append("\">");
        }

        content.append("<img src=\"");
        content.append(imgPath);
        content.append("\" width=\"100%");
        content.append("\" height=\"100%");
        content.append("\"/>");

        if (Validator.isEffective(protocol)) {
            content.append("</a>");
        }
        content.append("</body></html>");

        return content.toString();
    }
}
