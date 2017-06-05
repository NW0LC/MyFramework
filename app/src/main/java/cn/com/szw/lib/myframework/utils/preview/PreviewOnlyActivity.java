package cn.com.szw.lib.myframework.utils.preview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.szw.lib.myframework.R;
import cn.com.szw.lib.myframework.base.BaseActivity;
import me.drakeet.materialdialog.MaterialDialog;

public class PreviewOnlyActivity extends BaseActivity implements View.OnClickListener {
    TextView tvTitle;
    ImageView rightImage;
    RelativeLayout inActionBar;
    private ViewPager viewPager;

    private TextView pager_num;//页码

    private Button btn_save;//保存按钮

    ArrayList<Integer> dele_position = new ArrayList<Integer>();

    private ArrayList<String> resultList = new ArrayList<String>();
    public static PreviewOnlyActivity previewOnlyActivity;
    MyPagerAdapter adapter;
    List<Fragment> fragments;
    int p = 0;
    boolean isDelete = false;//是否显示删除按钮，true为不显示

    /**
     * 返回键
     *
     * @param v
     */
    public void back(View v) {
        finish();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.perview_only_viewpager);
        pager_num = (TextView) findViewById(R.id.pager_num);
        pager_num.setText(String.format("1/%s", resultList.size()));
        tvTitle.setText(String.format("1/%s", resultList.size()));
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        rightImage.setOnClickListener(this);
        rightImage.setVisibility(View.VISIBLE);
    }

    /**
     * viewpager滑动监听以及adapter
     */
    private void initPager() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                pager_num.setText((position + 1) + "/" + resultList.size());
                tvTitle.setText((position + 1) + "/" + resultList.size());
                //根据滑动之后图片加载状态设置保存按钮是否可以点击
//                btn_save.setEnabled(((MyPagerAdapter) viewPager.getAdapter()).previewOnlyFragment.flag);
                p = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(adapter);

    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_save) {
            ((MyPagerAdapter) viewPager.getAdapter()).previewOnlyFragment.saveImage();
        } else if (i == R.id.rightImage) {
            delete();
        }
    }

    @Override
    public boolean initToolbar() {
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_preview_only;
    }

    @Override
    public void init() throws Exception {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        rightImage = (ImageView) findViewById(R.id.rightImage);
        inActionBar = (RelativeLayout) findViewById(R.id.in_action_bar);
        previewOnlyActivity = this;
        resultList = getIntent().getStringArrayListExtra("images");
        if (resultList==null){
            resultList=new ArrayList<String>();
        }
        p = getIntent().getIntExtra("position", 0);
        isDelete = getIntent().getBooleanExtra("isDelete", false);
        initView();
        if (isDelete) {
            rightImage.setVisibility(View.GONE);
        }

        fragments = new ArrayList<Fragment>();
        for (int i = 0; i < resultList.size(); i++) {
            PreviewOnlyFragment fragment = new PreviewOnlyFragment();
            Bundle args = new Bundle();
            args.putString("img_url", resultList.get(i));
            args.putInt("click",1);
            fragment.setArguments(args);
            fragment.initListener(new PreviewOnlyFragment.LoadImageLister() {
                @Override
                public void complete() {
                    btn_save.setEnabled(true);
                }

                @Override
                public void failed() {
                    btn_save.setEnabled(false);
                }
            });
            fragments.add(fragment);
        }
        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        initPager();
        viewPager.setCurrentItem(p, false);
    }

    /**
     * viewpager适配器
     */
    class MyPagerAdapter extends FragmentStatePagerAdapter {

        List<Fragment> fragments;

        PreviewOnlyFragment previewOnlyFragment;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        public void setList(List<Fragment> fragments) {
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            previewOnlyFragment = (PreviewOnlyFragment) object;
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    public void click() {
        if (inActionBar.getVisibility() == View.VISIBLE) {
            inActionBar.setVisibility(View.GONE);
        } else {
            inActionBar.setVisibility(View.VISIBLE);
        }
    }


    public void delete() {
        final MaterialDialog materialDialog = new MaterialDialog(this);
        materialDialog.setTitle("提示").setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.dismiss();
                Intent intent = new Intent();
                intent.putIntegerArrayListExtra("dele_position", dele_position);
                if (p < resultList.size()) {
                    resultList.remove(p);
                    fragments.remove(p);
                    adapter.setList(fragments);
                    if (p + 1 < resultList.size()) {
                        tvTitle.setText((p + 1) + "/" + resultList.size());
                    } else {
                        tvTitle.setText(resultList.size() + "/" + resultList.size());
                    }
                    dele_position.add(p);
                    adapter.notifyDataSetChanged();
                }
                setResult(RESULT_OK, intent);
                if (fragments.size() == 0) {
                    PreviewOnlyActivity.this.finish();
                }
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.dismiss();
            }
        }).setMessage("要删除这张照片吗？").show();
    }

}
