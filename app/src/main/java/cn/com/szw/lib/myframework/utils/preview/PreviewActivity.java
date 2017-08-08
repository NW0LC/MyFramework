package cn.com.szw.lib.myframework.utils.preview;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;

import java.util.ArrayList;
import java.util.List;

import cn.com.szw.lib.myframework.R;
import cn.com.szw.lib.myframework.base.BaseActivity;
import me.drakeet.materialdialog.MaterialDialog;

import static cn.com.szw.lib.myframework.R.id.btn_save;
import static cn.com.szw.lib.myframework.R.id.mLeftImg;
import static cn.com.szw.lib.myframework.R.id.mTitle;
import static cn.com.szw.lib.myframework.R.id.preview_viewpager;
import static cn.com.szw.lib.myframework.R.id.toolbar;


/**
 * Created by 史忠文
 * on 2017/6/9.
 */

public class PreviewActivity<T extends PreviewObject> extends BaseActivity implements View.OnClickListener, PreviewFragment.LoadImageLister {
    // Content View Elements

    private Toolbar mToolbar;
    private RelativeLayout parentLay;
    private TextView mMLeft;
    private TextView mMTitle;
    private TextView mMRight;
    private ImageView mMRightImg;
    private ImageView mMLeftImg;
    private ViewPager mPreview_viewpager;
    private Button mBtn_save;

    // End Of Content View Elements

    public static final String PREVIEW_INTENT_IMAGES = "PREVIEW_INTENT_IMAGES";//图片数据  可以传List<String>也可以传List<?extend PreviewObject> 重写里面方法
    public static final String PREVIEW_INTENT_POSITION = "PREVIEW_INTENT_POSITION";//图片数据中所在的位置
    public static final String PREVIEW_INTENT_IS_CAN_DELETE = "PREVIEW_INTENT_IS_CAN_DELETE";//是否可删除
    public static final String PREVIEW_INTENT_SHOW_NUM = "PREVIEW_INTENT_SHOW_NUM";//是否显示1/4
    public static final String PREVIEW_INTENT_RESULT = "PREVIEW_INTENT_RESULT";//删除的图片下标
    private int position;
    private List<Fragment> fragments;
    private List<T> imgUrls;
    private ArrayList<String> imgUrlsStrs;
    private ArrayList resultPosition;
    private FragmentPagerAdapter mPagerAdapter;
    private boolean isStrs;


    @Override
    public boolean initToolbar() {
        mToolbar.setContentInsetsAbsolute(0, 0);
        mToolbar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.black_a7000000));
        mMTitle.setTextSize(18);
        mMTitle.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        mMRightImg.setImageResource(R.mipmap.delete_delete);
        mMRightImg.setPadding(SizeUtils.dp2px(15), SizeUtils.dp2px(15), SizeUtils.dp2px(15), SizeUtils.dp2px(15));
        setSupportActionBar(mToolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_preview;
    }

    @Override
    public void init() throws Exception {
        bindViews();
        iniEvent();
        initFragment();
        initPager();
    }

    private void iniEvent() {
        mMRightImg.setVisibility(getIntent().getBooleanExtra(PREVIEW_INTENT_IS_CAN_DELETE, false) ? View.VISIBLE : View.GONE);
        mMTitle.setVisibility(getIntent().getBooleanExtra(PREVIEW_INTENT_SHOW_NUM, false) ? View.VISIBLE : View.GONE);
        mMLeftImg.setOnClickListener(this);
        mMRightImg.setOnClickListener(this);
        mBtn_save.setOnClickListener(this);
        ObjectAnimator animator_exit = ObjectAnimator.ofFloat(parentLay, "translationY", 0, SizeUtils.dp2px(-55));
        ObjectAnimator animator_in = ObjectAnimator.ofFloat(parentLay, "translationY", SizeUtils.dp2px(-55), 0);
        LayoutTransition transition = new LayoutTransition();
        transition.setAnimator(LayoutTransition.APPEARING, animator_in);
        transition.setAnimator(LayoutTransition.DISAPPEARING, animator_exit);
        parentLay.setLayoutTransition(transition);
    }

    private void initFragment() {
        resultPosition = new ArrayList<>();
        fragments = new ArrayList<>();
        if ((getIntent().getParcelableArrayListExtra(PREVIEW_INTENT_IMAGES) != null && getIntent().getParcelableArrayListExtra(PREVIEW_INTENT_IMAGES).size() > 0) && getIntent().getParcelableArrayListExtra(PREVIEW_INTENT_IMAGES).get(0) instanceof PreviewObject) {
            imgUrls = getIntent().getParcelableArrayListExtra(PREVIEW_INTENT_IMAGES);
            isStrs = false;
            for (T imgUrl : imgUrls) {
                PreviewFragment fragment = PreviewFragment.Instance(imgUrl.getNormalUrl(), imgUrl.getThumbnailUrl());
                fragment.setLoadImageLister(this);
                fragments.add(fragment);
            }

        } else {
            imgUrlsStrs = getIntent().getStringArrayListExtra(PREVIEW_INTENT_IMAGES);
            isStrs = true;
            for (String imgUrl : imgUrlsStrs) {
                PreviewFragment fragment = PreviewFragment.Instance(imgUrl, null);
                fragment.setLoadImageLister(this);
                fragments.add(fragment);
            }
        }
    }

    private void initPager() {
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        mPreview_viewpager.setOffscreenPageLimit(fragments.size());
        mPreview_viewpager.setAdapter(mPagerAdapter);
        mPreview_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mMTitle.setText((position + 1) + "/" + fragments.size());
                PreviewActivity.this.position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mMTitle.setText(String.format("1/%s", fragments.size()));
        mPreview_viewpager.setCurrentItem(getIntent().getIntExtra(PREVIEW_INTENT_POSITION, 0), false);
    }

    private void bindViews() {
        mToolbar = (Toolbar) findViewById(toolbar);
        parentLay = (RelativeLayout) findViewById(R.id.parentLay);
        mMLeft = (TextView) findViewById(R.id.mLeft);
        mMTitle = (TextView) findViewById(mTitle);
        mMRight = (TextView) findViewById(R.id.mRight);
        mMRightImg = (ImageView) findViewById(R.id.mRightImg);
        mMLeftImg = (ImageView) findViewById(mLeftImg);
        mPreview_viewpager = (ViewPager) findViewById(preview_viewpager);
        mBtn_save = (Button) findViewById(btn_save);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == mLeftImg) {
            finish();

        } else if (i == R.id.mRightImg) {
            delete();

        } else if (i == btn_save) {
            ((PreviewFragment) mPagerAdapter.getItem(position)).saveImage();

        }
    }

    public void delete() {
        final MaterialDialog materialDialog = new MaterialDialog(this);
        materialDialog.setTitle("提示").setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.dismiss();
                Intent intent = new Intent();
                if (position < imgUrls.size()) {
                    if (isStrs) {
                        resultPosition.add(imgUrlsStrs.get(position));
                        imgUrlsStrs.remove(position);
                        intent.putStringArrayListExtra(PREVIEW_INTENT_RESULT, resultPosition);
                    } else {
                        resultPosition.add(imgUrls.get(position));
                        imgUrls.remove(position);
                        intent.putParcelableArrayListExtra(PREVIEW_INTENT_RESULT, resultPosition);
                    }
                    fragments.remove(position);
                    if (position + 1 < fragments.size()) {
                        mMTitle.setText((position + 1) + "/" + fragments.size());
                    } else {
                        mMTitle.setText(fragments.size() + "/" + fragments.size());
                    }

                    mPagerAdapter.notifyDataSetChanged();
                }
                setResult(RESULT_OK, intent);
                if (fragments.size() == 0) {
                    PreviewActivity.this.finish();
                }
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.dismiss();
            }
        }).setMessage("要删除这张照片吗？").show();
    }

    @Override
    public void complete() {
        mBtn_save.setVisibility(View.VISIBLE);
    }

    @Override
    public void failed() {
        mBtn_save.setVisibility(View.GONE);
    }

    @Override
    public void onClick() {
        mToolbar.setVisibility(mToolbar.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        mBtn_save.setVisibility(mBtn_save.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }
}
