package cn.com.szw.lib.myframework.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.KeyboardUtils;
import com.common.controls.dialog.CommonDialogFactory;
import com.common.controls.dialog.DialogType103;
import com.common.controls.dialog.DialogType104;
import com.common.controls.dialog.DialogUtil;
import com.common.controls.dialog.ICommonDialog;
import com.yanzhenjie.permission.AndPermission;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.R;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.listener.OnNumListener;
import cn.com.szw.lib.myframework.listener.OnTextListener;

import static android.Manifest.permission.CALL_PHONE;
import static cn.com.szw.lib.myframework.config.Constants.Permission.Phone;

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
                if (ActivityCompat.checkSelfPermission(context, CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    // 申请权限。
                    Log.i("Tag", "申请权限.");
                    AndPermission.with(context)
                            .callback(context)
                            .requestCode(Phone)
                            .permission(CALL_PHONE)
                            .start();
                    return;
                }
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public static void PayBack(final Activity context) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_103);
        dialog.setTitleText("返回");
        dialog.setContentText("您确定放弃支付?");
        dialog.setCancelBtn("支付", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOkBtn("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                context.finish();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
 public static void PayNoPwd(final Activity context,final View.OnClickListener listener) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_103);
        dialog.setTitleText("啊哦");
        dialog.setContentText("未设置支付密码！");
        dialog.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOkBtn("去设置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onClick(v);
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public static void Delete(final Context context, final View.OnClickListener listener) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_103);
        dialog.setTitleText("删除");
        dialog.setContentText("确定删除？");
        dialog.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOkBtn("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onClick(((DialogType103)dialog).getmOkTv());
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
    public static void DeleteSearch(final Context context, final View.OnClickListener listener) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_103);
        dialog.setTitleText("删除");
        dialog.setContentText("确定清除记录？");
        dialog.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOkBtn("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onClick(((DialogType103)dialog).getmOkTv());
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    } public static void ChangeState(final Context context,String str, final View.OnClickListener listener) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_103);
        dialog.setTitleText("提示");
        dialog.setContentText(str);
        dialog.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOkBtn("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onClick(((DialogType103)dialog).getmOkTv());
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public static void Update(final Activity context, final String url) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_103);
        dialog.setTitleText("版本更新");
        dialog.setContentText("有新的版本");
        dialog.setCancelBtn("稍后", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOkBtn("立即更新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public static void Warning(final Context context, String str) {
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

    public static void WarningForListener(final Context context,String str, final View.OnClickListener listener) {
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


    public static void ChangeNum(final Context context,int count, final OnNumListener onNumListener) {
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
                countIndex=1;
            }
        });
        dialog.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                countIndex=1;
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                KeyboardUtils.toggleSoftInput();
            }
        });
        ((DialogType104)dialog).setOnBeforeDismiss(new DialogType104.OnBeforeDismissListener() {
            @Override
            public boolean onBeforeDismiss() {
                KeyboardUtils.hideSoftInput(context,viewHolder.count);
                return true;
            }
        });
        dialog.show();
    }

    public static void GetText(final Context context,final OnTextListener onTextListener) {
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
//                    if (trim.length()>4) {
//                        onNumListener.onNum(999);
//                    }else
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
        ((DialogType104)dialog).setOnBeforeDismiss(new DialogType104.OnBeforeDismissListener() {
            @Override
            public boolean onBeforeDismiss() {
                KeyboardUtils.hideSoftInput(context,editText);
                return true;
            }
        });
        dialog.show();
    }

    private static int countIndex=1;
    static class ViewHolder {
        @BindView(R.id.minus)
        ImageView minus;
        @BindView(R.id.count)
        EditText count;
        @BindView(R.id.add)
        ImageView add;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            count.setSelection(count.getText().length());
        }
        @OnClick({R.id.minus, R.id.add})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.minus:
                    countIndex = countIndex <= 1 ? 1 : --countIndex;
                    break;
                case R.id.add:
                    countIndex += 1;
                    break;
            }
            count.setText(String.format("%s", countIndex));
            count.setSelection(count.getText().length());
        }
    }
}
