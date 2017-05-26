package cn.com.szw.lib.myframework.view.pwd.popWin;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.blankj.utilcode.util.ScreenUtils;

import cn.com.szw.lib.myframework.R;
import cn.com.szw.lib.myframework.view.pwd.widget.OnPasswordInputFinish;
import cn.com.szw.lib.myframework.view.pwd.widget.PasswordPopView;
import razerdp.basepopup.BasePopupWindow;

public class PwdPop extends BasePopupWindow {


    private PasswordPopView mPasswordView;
    private Activity context;

    public PwdPop(Activity context, OnPasswordInputFinish onPasswordInputFinish) {
        super(context);
        this.context = context;
        mPasswordView= (PasswordPopView) findViewById(R.id.mPasswordView);
        mPasswordView.setOnFinishInput(onPasswordInputFinish);
        mPasswordView.getVirtualKeyboardView().getLayoutBack().setVisibility(View.GONE);
        mPasswordView.getViewForgetPwd().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                PwdPop.this.context.startActivity(new Intent(PwdPop.this.context, PwdGetCodeActivity.class));
            }
        });
        mPasswordView.getImgCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setPrice(String price) {
        mPasswordView.getViewPrice().setText(price);
    }


    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return View.inflate(getContext(), R.layout.pwd_pop, null);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.mPasswordView);
    }

    @Override
    protected Animation initShowAnimation() {
        Animation shakeAnimate = new TranslateAnimation(0, 0, ScreenUtils.getScreenHeight(), 0);
        shakeAnimate.setDuration(300);
        return shakeAnimate;
    }

    @Override
    protected Animation initExitAnimation() {
        Animation shakeAnimate = new TranslateAnimation(0, 0, 0, ScreenUtils.getScreenHeight());
        shakeAnimate.setDuration(300);
        return shakeAnimate;
    }


}