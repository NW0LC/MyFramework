package cn.com.szw.lib.myframework.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用方法的工具类
 *
 * @author zhn
 */
public class Utils {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        PercentRelativeLayout.LayoutParams params = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ((displayMetrics.widthPixels)/100)*8);
//        params.setMargins(0,DialogUtils.getStatusHeight(this),0,0);
//        relativeLayout1.setLayoutParams(params);
//    }

    /**
     * @param price 金钱
     * @return 去掉.00
     */
    public static String price(String price) {
        return price.replace(".00", "");
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * @param path
     * @return
     * @throws Exception
     * 绝对路径转base 64
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }



    /**
     * @param sourceStr
     * @return md5加密 32位小写
     */
    public static String getMD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            System.out.println("result: " + result);//32位的加密
//            System.out.println("result: " +
//                    buf.toString().substring(8, 24));//16位的加密
        } catch (NoSuchAlgorithmException e) {
//TODO Auto-generated catch block e.printStackTrace();
        }
        return result;
    }
    /**
     * 集合转化为base64 字符串
     *
     * @param <T>
     * @param SceneList
     * @return
     * @throws IOException
     */
    public static <T> String SceneList2String(List<T> SceneList) throws IOException {
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(SceneList);
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        String SceneListString = new String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        // 关闭objectOutputStream
        objectOutputStream.close();
        return SceneListString;

    }
    public static void showImg(String url, SimpleDraweeView iv) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(url)
                .setAutoPlayAnimations(true)
                .build();
        iv.setController(controller);
    }

    /**
     * base64 字符串转化为集合
     *
     * @param <T>
     * @param <T>
     * @param SceneListString
     * @return
     * @throws StreamCorruptedException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> List<T> String2SceneList(String SceneListString)
            throws IOException,
            ClassNotFoundException {
        byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
                Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        List<T> SceneList = (List<T>) objectInputStream.readObject();
        objectInputStream.close();
        return SceneList;
    }


    /**
     * bitmap存入本地
     */
    public static void saveCroppedImage(Context context, Bitmap bmp, String name) {
        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/itemei");
        if (!path.exists())
            path.mkdirs();

        File file = new File(path, name + ".png");

        if (file.exists()) {

            Toast.makeText(context, "保存成功" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache", Toast.LENGTH_SHORT).show();
            return;
        }


        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(context, "保存成功"+ Environment.getExternalStorageDirectory().getAbsolutePath() + "/itemei", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, "保存失败" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * bitmap存入本地
     */
    public static void saveCroppedImage(Bitmap bmp, String name) {
        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/itemei/cache");
        if (!path.exists())
            path.mkdirs();

        File file = new File(path, name);

        if (file.exists()) {

            return;
        }
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
        }

    }


    /**
     * 获取压缩之后的file路径
     */
    public static String getCroppedImage(String url) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/itemei/cache/" + url.split("/")[url.split("/").length - 1];
        File file = new File(path);
        if (file.exists())
            return path;
        else
            return url;
    }
    public static void startActivity(Context mContext,Class mClass){
        Intent intent=new Intent(mContext,mClass);
        mContext.startActivity(intent);
    }
    public static void startActivityForResult(Activity mContext,Class mClass,int requestCode){
        Intent intent=new Intent(mContext,mClass);
        mContext.startActivityForResult(intent,requestCode);
    }





    /**
     * 状态栏高度算法
     *
     * @param activity
     * @return
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }


    /**
     * 删除文件
     *
     * @param files
     */
    public static void deleteFile(List<File> files) {
        for (File file : files
                ) {
            if (file.exists()) { // 判断文件是否存在
                if (file.isFile()) { // 判断是否是文件
                    file.delete(); // 删除文件;
                }
            }
        }
    }


    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void RecursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }
    //此方法只是关闭软键盘
    public static void hintKbTwo(Activity context) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&context.getCurrentFocus()!=null){
            if (context.getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
