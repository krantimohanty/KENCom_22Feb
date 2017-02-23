package com.swash.kencommerce;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.swash.kencommerce.adapter.GiftsCatAdapter;
import com.swash.kencommerce.crashreport.ExceptionHandler;
import com.swash.kencommerce.fragments.OneFragment;

import java.util.ArrayList;
import java.util.List;

public class MainDetailsActivity extends AppCompatActivity {

    AppBarLayout appbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public ImageView img_tab;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TabLayout.Tab tab;
    Toolbar toolbar;
    ExpandableHeightGridView gridview;
    ArrayList<String> arrCatName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(MainDetailsActivity.this));

        img_tab = (ImageView) findViewById(R.id.backdrop);
        gridview=(ExpandableHeightGridView)findViewById(R.id.gridview);
        arrCatName=new ArrayList<>();
        arrCatName.add("Test1");
        arrCatName.add("Test2");
        arrCatName.add("Test3");
        arrCatName.add("Test4");
        arrCatName.add("Test5");
        arrCatName.add("Test6");
        arrCatName.add("Test7");
        arrCatName.add("Test8");
        arrCatName.add("Test9");

        gridview.setAdapter(new GiftsCatAdapter(MainDetailsActivity.this, arrCatName));
        gridview.setExpanded(true);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
               /* Toast.makeText(DrinkCategoriesActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();*/
                //getDialogCoverage();
                Intent intent = new Intent(MainDetailsActivity.this, MainDetailsActivity.class);
                startActivity(intent);
            }
        });
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tabLayout = (TabLayout) findViewById(R.id.detail_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getSelectedTabPosition();
        tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#048bcd")));
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OneFragment(), "ONE");
        adapter.addFrag(new OneFragment(), "TWO");
        adapter.addFrag(new OneFragment(), "THREE");
        adapter.addFrag(new OneFragment(), "FOUR");
        adapter.addFrag(new OneFragment(), "FIVE");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
