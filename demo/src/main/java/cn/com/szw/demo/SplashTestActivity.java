package cn.com.szw.demo;

import com.umeng.message.inapp.InAppMessageManager;
import com.umeng.message.inapp.UmengSplashMessageActivity;

public class SplashTestActivity extends UmengSplashMessageActivity {
  @Override
  public boolean onCustomPretreatment() {
     InAppMessageManager mInAppMessageManager = InAppMessageManager.getInstance(this);
     //设置应用内消息为Debug模式
     mInAppMessageManager.setInAppMsgDebugMode(true);
     //参数为Activity的完整包路径，下面仅是示例代码，请按实际需求填写
     mInAppMessageManager.setMainActivityPath("cn.com.szw.demo.MainActivity");
     return super.onCustomPretreatment(); 
  }
}