package chrisjluc.onesearch.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.google.common.collect.Lists;

import java.util.List;

import chrisjluc.onesearch.R;
import chrisjluc.onesearch.base.BaseActivity;
import chrisjluc.onesearch.fragments.InstructionFragment;
import chrisjluc.onesearch.fragments.InstructionFragment2;
import chrisjluc.onesearch.fragments.InstructionFragment3;
import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;

public class SplashActivity extends AppCompatActivity {
    ScrollerViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spalsh_activity);
        viewPager = (ScrollerViewPager) findViewById(R.id.view_pager);
        SpringIndicator springIndicator = (SpringIndicator) findViewById(R.id.indicator);

        PagerModelManager manager = new PagerModelManager();
        manager.addCommonFragment(getFragments());
        ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(), manager);
        viewPager.setAdapter(adapter);
        viewPager.fixScrollSpeed();

        // just set viewPager
        springIndicator.setViewPager(viewPager, getIndicator());
    }

    private ImageView[] getIndicator(){
      //  ImageView image = new ImageView(this);
        //return new ImageView[]{image,image,image};
        return new ImageView[]{null,null,null};
    }

    private List<Fragment> getFragments(){
        return Lists.newArrayList(new InstructionFragment(), new InstructionFragment2(), new InstructionFragment3());
    }
}
