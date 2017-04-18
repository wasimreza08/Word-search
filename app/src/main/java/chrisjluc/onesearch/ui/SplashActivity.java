package chrisjluc.onesearch.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import chrisjluc.onesearch.R;
import chrisjluc.onesearch.base.BaseActivity;
import chrisjluc.onesearch.fragments.InstructionFragment;
import chrisjluc.onesearch.fragments.InstructionFragment2;
import chrisjluc.onesearch.fragments.InstructionFragment3;
import github.chenupt.multiplemodel.ItemEntity;
import github.chenupt.multiplemodel.ItemEntityUtil;
import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerManager;

import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;
import me.relex.circleindicator.CircleIndicator;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private final static String MENU_PREF_NAME = "menu_prefs";
    private final static String FIRST_TIME = "first_time";
    private InstructionFragment ins1 = new InstructionFragment();
    private InstructionFragment2 ins2 = new InstructionFragment2();
    private InstructionFragment3 ins3 = new InstructionFragment3();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spalsh_activity);
        viewPager = (ViewPager) findViewById(R.id.viewpager_default);
      //  SpringIndicator springIndicator = (SpringIndicator) findViewById(R.id.indicator);

        ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(), getModelPagerManager());
        viewPager.setAdapter(adapter);
        //ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(), manager);
        viewPager.setAdapter(adapter);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator_default);
        indicator.setViewPager(viewPager);
        //viewPager.fixScrollSpeed();

        // just set viewPager
        //springIndicator.setViewPager(viewPager, getIndicator());
    }

    private PagerManager getModelPagerManager(){
        List<ItemEntity> list = new ArrayList<>();
        for (int i = 0; i < getTitles().size(); i++) {
            ItemEntityUtil.create(getTitles().get(i)).setModelView(getFragments().get(i).getClass()).attach(list);
        }
        return PagerManager.begin().addFragments(list).setTitles(getTitles());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFullscreen();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.bReadySplash){
            SharedPreferences.Editor editor = getSharedPreferences(MENU_PREF_NAME,MODE_PRIVATE).edit();
            editor.putBoolean(FIRST_TIME, false);
            editor.apply();
            startActivity(new Intent(this, MenuActivity.class));
        }
    }

    protected void setFullscreen() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getActionBar();
        if (actionBar != null)
            actionBar.hide();
    }

    private ImageView[] getIndicator(){
      //  ImageView image = new ImageView(this);
        //return new ImageView[]{image,image,image};
        return new ImageView[]{null,null,null};
    }
    private List<String> getTitles(){
        return Lists.newArrayList("1", "2", "3");
    }

   /* private List<Integer> getBgRes(){
        return Lists.newArrayList(R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4);
    }*/

    private List<Fragment> getFragments(){
        return Lists.newArrayList(ins1, ins2, ins3);
    }
}
