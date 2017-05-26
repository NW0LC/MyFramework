package cn.com.szw.lib.myframework.observer;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

//用来观察系统里短消息的数据库变化  ”表“内容观察者,只要信息数据库发生变化，都会触发该ContentObserver 派生类
public class SmsContentObserver extends ContentObserver {
    private static String TAG = "SMSContentObserver";

    private static int MSG_OUTBOX_CONTENT = 2;

    public  Context mContext;
    private Handler mHandler;   //更新UI线程

    public SmsContentObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    /**
     * 当所监听的Uri发生改变时，就会回调此方法
     * _id 一个自增字段，从1开始; thread_id 序号，同一发信人的id相同; address 发件人手机号码; person 联系人列表里的序号，陌生人为null
     * date 发件日期; protocol 协议，分为： 0 SMS_RPOTO, 1 MMS_PROT; read 是否阅读 0未读，1已读; status 状态 -1接收，0 complete, 64 pending, 128 failed
     * content://sms/inbox 收件箱
     * content://sms/sent 已发送
     * content://sms/draft 草稿
     * content://sms/outbox 发件箱
     * content://sms/failed 发送失败
     * content://sms/queued 待发送列表
     *
     * @param selfChange 此值意义不大 一般情况下该回调值false
     */
    @Override
    public void onChange(boolean selfChange) {
        Log.i(TAG, "the sms table has changed");

        startQuery();
//        UIStartQuery();
    }

    /**
     * 在主线程中查询数据库
     */
    public void UIStartQuery() {
        //查询发件箱里的内容
        Uri outSMSUri = Uri.parse("content://sms/inbox");

        String where = "address = 95511 read = 0 AND date >  " + (System.currentTimeMillis() - 60 * 1000);//一分钟之内且未读短信
        Cursor c;
        try {
            c = mContext.getContentResolver().query(outSMSUri, null, where, null, "date desc");
        } catch (Exception e) {//这里可能会出现读取短信的权限没有打开，从而引发异常
            AlertDialog.Builder dlg = new AlertDialog.Builder(mContext);
            dlg.setTitle("出错了！");
//            dlg.setMessage("原因: " + e.getMessage());
            dlg.setMessage("原因: 读取短信权限没有打开，点击确定前往设置打开权限");
            dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startAppSettings(mContext);
                }
            });
            dlg.setCancelable(false);
            dlg.show();
            return;
        }

        if (c != null && c.getCount() > 0) {

            Log.i(TAG, "the number of send is" + c.getCount());

            StringBuilder sb = new StringBuilder();
            //循环遍历
            while (c.moveToNext()) {
                int number = c.getInt(c.getColumnIndex("address"));
                String name = c.getString(c.getColumnIndex("person"));
                String body = c.getString(c.getColumnIndex("body"));
                sb.append("发件人手机号码: ").append(number).append("发件人：").append(name).append("信息内容: ").append(body).append("\n");
                System.out.println(">>>>>>>>>>>>>>>>手机号：" + number);
                System.out.println(">>>>>>>>>>>>>>>>联系人姓名列表：" + name);
                System.out.println(">>>>>>>>>>>>>>>>短信的内容：" + body);
            }
            c.close();
            mHandler.obtainMessage(MSG_OUTBOX_CONTENT, sb.toString()).sendToTarget();
        }
    }

    /**
     * 在子线程中查询数据库
     */
    private void startQuery() {
        QueryHandler mQueryHandler = new QueryHandler(this,mContext.getContentResolver());
        //查询发件箱里的内容
        Uri outSMSUri = Uri.parse("content://sms/inbox");
        //参数一:相当于Message.what 参数二:相当于Message.obj 参数三:URL 参数四:指定查询的列
        //参数五:指定查询的条件 参数六:指定查询条件中的参数 参数七:指定按那列排序
        String where = "read = 0 AND date >  " + (System.currentTimeMillis() - 60 * 1000);//一分钟之内且未读短信
        mQueryHandler.startQuery(0, null, outSMSUri, null, where, null, "date desc");

    }
////读取收件箱中指定号码的短信
//    cursor = managedQuery(Uri.parse("content://sms/inbox"), new String[]{"_id", "address", "read", "body"},
//            " address=? and read=?", new String[]{"1065811201", "0"}, "_id desc");//按id排序，如果按date排序的话，修改手机时间后，读取的短信就不准了
//  MyLog.l("cursor.isBeforeFirst() " + cursor.isBeforeFirst() + " cursor.getCount() " + cursor.getCount());
//  if (cursor != null && cursor.getCount() > 0) {
//        ContentValues values = new ContentValues();
//        values.put("read", "1");  //修改短信为已读模式
//        cursor.moveToNext();
//        int smsbodyColumn = cursor.getColumnIndex("body");
//        String smsBody = cursor.getString(smsbodyColumn);
//        MyLog.v("smsBody = " + smsBody);
//
//        edtPassword.setText(MatchesUtil.getDynamicPassword(smsBody));
//
//    }
//
//    //在用managedQuery的时候，不能主动调用close()方法， 否则在Android 4.0+的系统上， 会发生崩溃
//  if(Build.VERSION.SDK_INT < 14) {
//        cursor.close();
//    }
    // 写一个异步查询类

    private  static final class QueryHandler extends AsyncQueryHandler {
        private SmsContentObserver mContext;
        private QueryHandler(SmsContentObserver mContext,ContentResolver cr) {
            super(cr);
            this.mContext =mContext;
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            Log.d(TAG, "onQueryComplete:");
            if (cursor == null && cookie == null) {//游标为null可能是读取短信的权限没有打开
                AlertDialog.Builder dlg = new AlertDialog.Builder(mContext.mContext);
                dlg.setTitle("出错了！");
//            dlg.setMessage("原因: " + e.getMessage());
                dlg.setMessage("原因: 读取短信权限没有打开，点击确定前往设置打开权限");
                dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings(mContext.mContext);
                    }
                });
                dlg.setCancelable(false);
                dlg.show();
            } else if (cursor != null && cursor.getCount() > 0) {

                Log.i(TAG, "the number of send is" + cursor.getCount());

                StringBuilder sb = new StringBuilder();
                //循环遍历
                while (cursor.moveToNext()) {
                    int number = cursor.getInt(cursor.getColumnIndex("address"));
                    String name = cursor.getString(cursor.getColumnIndex("person"));
                    String body = cursor.getString(cursor.getColumnIndex("body"));
                    sb.append("发件人手机号码: ").append(number).append("发件人：").append(name).append("信息内容: ").append(body).append("\n");
                    System.out.println(">>>>>>>>>>>>>>>>手机号：" + number);
                    System.out.println(">>>>>>>>>>>>>>>>联系人姓名列表：" + name);
                    System.out.println(">>>>>>>>>>>>>>>>短信的内容：" + body);
                }
                mContext.mHandler.obtainMessage(MSG_OUTBOX_CONTENT, sb.toString()).sendToTarget();
            }

        }
    }

    // 启动应用的设置
    private static void startAppSettings(Context mContext) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        mContext.startActivity(intent);
    }

}