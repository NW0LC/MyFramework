package cn.com.szw.lib.myframework.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import cn.com.szw.lib.myframework.utils.RxBus;
import cn.com.szw.lib.myframework.view.CustomProgress;


public abstract class MyBaseFragment extends Fragment {
	protected View rootView;
	@Override
	public void onDestroy() {
		try {
			RxBus.get().unregister(this);
		}catch (Exception e){
			e.printStackTrace();
		}
		CustomProgress.disMiss();
		super.onDestroy();
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (rootView != null) {
			ViewGroup view = (ViewGroup) rootView.getParent();
			if (view!=null)
			view.removeView(rootView);
		}
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView();
		initEvent();
		try {
			RxBus.get().register(this);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public  abstract void initView();
	public  void initEvent(){}
}
