package cn.com.szw.lib.myframework.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.KeyboardUtils;
import com.common.controls.dialog.CommonDialogFactory;
import com.common.controls.dialog.DialogUtil;
import com.common.controls.dialog.ICommonDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.szw.lib.myframework.R;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.listener.OnNumListener;
import cn.com.szw.lib.myframework.listener.OnTextListener;


/**
 * Created by Swain
 * on 2016/11/18.
 */

public class DialogUtils {
    public static ICommonDialog dialog;

    public static void Call(final BaseActivity context, String str) {
        if (TextUtils.isEmpty(str)) {
            Toast.makeText(context, "暂无电话", Toast.LENGTH_SHORT).show();
            return;
        }
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        final String str1 = m.replaceAll("").trim();

        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_1);
        dialog.setTitleText(TextUtils.isEmpty(str1) ? "暂无电话" : str1);
        dialog.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOkBtn("拨打", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(str1)) {
                    Toast.makeText(context, "暂无可拨打号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(
                        Intent.ACTION_CALL,
                        Uri.parse("tel:" + str1));
                context.callPhoneWithCheck(intent);
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
    public static void reminder(@NonNull Context context){
        reminder(context,"","",null);
    }
    public static void reminder(@NonNull Context context,@NonNull String title){
        reminder(context,title,"",null);
    }
    public static void reminder(@NonNull Context context,@NonNull String title, @NonNull String content){
        reminder(context,title,content,null);
    }
    public static void reminder(@NonNull Context context,@NonNull String title, @NonNull String content, @NonNull String ok,@NonNull String cancel){
        reminder(context,title,content,null,ok,cancel);
    }

    public static void reminder(@NonNull Context context,@NonNull String title, @NonNull String content, @NonNull String ok,@NonNull String cancel,@NonNull final View.OnClickListener listener){
        reminder(context,title,content,listener,ok,cancel);
    }
    public static void reminder(Context context, String title, String content, final View.OnClickListener listener, String... btnStr) {
        String cancel = "取消";
        String ok = "确定";
        if (btnStr != null) {
            if (btnStr.length >= 1)
                ok = btnStr[0];
            if (btnStr.length >= 2)
                cancel = btnStr[1];
        }
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_103);
        dialog.setTitleText(TextUtils.isEmpty(title)?"提示":title);
        dialog.setContentText(TextUtils.isEmpty(content)?"确定要这样做吗？":content);
        dialog.setCancelBtn(cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOkBtn(ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public static void Warning(@NonNull Context context,@NonNull String str) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_4);
        dialog.setTitleText(TextUtils.isEmpty(str) ? "空空如也" : str);
        dialog.setOkBtn("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public static void WarningWithListener(@NonNull Context context,@NonNull String str,@NonNull final View.OnClickListener listener) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_4);
        dialog.setTitleText(TextUtils.isEmpty(str) ? "空空如也" : str);
        dialog.setOkBtn("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onClick(v);
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    public static void ChangeNum(final Context context, int count, final OnNumListener onNumListener) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_104);
        LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dialog_change_num, null);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.count.setText(String.format("%s", count));
        viewHolder.count.setSelection(viewHolder.count.getText().length());
        countIndex = Integer.valueOf(viewHolder.count.getText().toString());
        dialog.setTitleText("修改数量");
        dialog.setContentView(view);
        dialog.setOkBtn("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = viewHolder.count.getText().toString().trim();
                if (!TextUtils.isEmpty(trim)) {
//                    if (trim.length()>4) {
//                        onNumListener.onNum(999);
//                    }else
                    onNumListener.onNum(Integer.parseInt(trim));
                }
                dialog.dismiss();
                countIndex = 1;
            }
        });
        dialog.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                countIndex = 1;
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                KeyboardUtils.toggleSoftInput();
            }
        });
        ((DialogType104) dialog).setOnBeforeDismiss(new DialogType104.OnBeforeDismissListener() {
            @Override
            public boolean onBeforeDismiss() {
                KeyboardUtils.hideSoftInput(context, viewHolder.count);
                return true;
            }
        });
        dialog.show();
    }

    public static void GetText(final Context context, final OnTextListener onTextListener) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_104);
        LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dialog_edit_text, null);
        final EditText editText = (EditText) view.findViewById(R.id.editText);
        dialog.setTitleText("");
        dialog.setContentView(view);
        dialog.setOkBtn("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(trim)) {
                    onTextListener.onText(trim);
                }
                dialog.dismiss();
            }
        });
        dialog.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                KeyboardUtils.toggleSoftInput();
            }
        });
        ((DialogType104) dialog).setOnBeforeDismiss(new DialogType104.OnBeforeDismissListener() {
            @Override
            public boolean onBeforeDismiss() {
                KeyboardUtils.hideSoftInput(context, editText);
                return true;
            }
        });
        dialog.show();
    }

    private static int countIndex = 1;

    private static class ViewHolder implements View.OnClickListener{
        ImageView minus;
        EditText count;
        ImageView add;

        ViewHolder(View view) {
            minus= (ImageView) view.findViewById(R.id.minus);
            add= (ImageView) view.findViewById(R.id.add);
            count= (EditText) view.findViewById(R.id.count);
            minus.setOnClickListener(this);
            add.setOnClickListener(this);
            count.setSelection(count.getText().length());
        }

        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.minus) {
                countIndex = countIndex <= 1 ? 1 : --countIndex;
            } else if (i == R.id.add) {
                countIndex += 1;
            }
            count.setText(String.format("%s", countIndex));
            count.setSelection(count.getText().length());
        }
    }
}
