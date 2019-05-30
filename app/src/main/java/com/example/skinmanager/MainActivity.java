package com.example.skinmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.skin_core.SkinManager;
import com.example.skinmanager.fragment.MusicFragment;
import com.example.skinmanager.fragment.RadioFragment;
import com.example.skinmanager.fragment.VideoFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * 换肤
 * 颜色: colors.xml 配置需要替换的颜色name 为不同的颜色值
 * 图片： 同上
 * 选择器：同上 (如 颜色选择器，皮肤包中的颜色选择器会使用皮肤包中的颜色)
 * 字体：strings.xml 配置 typeface 路径指向 assets 目录下字体文件
 * 自定义View 需要实现SkinViewSupport接口自行实现换肤逻辑(包括support中的View )
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        List<Fragment> list = new ArrayList<>();
        list.add(new MusicFragment());
        list.add(new VideoFragment());
        list.add(new RadioFragment());
        List<String> listTitle = new ArrayList<>();
        listTitle.add("音乐");
        listTitle.add("视频");
        listTitle.add("电台");


        int curItem;
        for (curItem = 0; curItem < listTitle.size(); ++curItem) {
            tabLayout.addTab(tabLayout.newTab().setText(listTitle.get(curItem)), false);
        }

        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(this, list);
        viewPager.setAdapter(myFragmentPagerAdapter);
        final TabLayout.TabLayoutOnPageChangeListener tabLayoutOnPageChangeListener = new TabLayout.TabLayoutOnPageChangeListener(tabLayout);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabLayoutOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                tabLayoutOnPageChangeListener.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                tabLayoutOnPageChangeListener.onPageScrollStateChanged(state);
            }
        });

        SkinManager.getInstance().applyUpdate(this);

    }

    /**
     * 进入换肤
     *
     * @param view
     */
    public void skinSelect(View view) {
        startActivity(new Intent(this, SkinActivity.class));
    }
}
