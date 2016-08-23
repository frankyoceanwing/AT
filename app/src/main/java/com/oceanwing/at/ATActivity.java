package com.oceanwing.at;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.oceanwing.at.model.Task;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ATActivity extends AppCompatActivity {

    @BindView(R.id.task_new)
    FloatingActionButton mNewTask;

    @OnClick(R.id.task_new)
    void newTask() {
        Intent intent = new Intent(this, TaskActivity.class);
        intent.setAction(Task.ACTION_NEW);
        startActivity(intent);
    }

    @BindArray(R.array.tabs)
    String[] mTabs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at);
        ButterKnife.bind(this);

        getSupportActionBar().setElevation(0);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.app_tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.app_viewpager);

        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    public static void start(Context c) {
        c.startActivity(new Intent(c, ATActivity.class));
    }

    class TabsAdapter extends FragmentPagerAdapter {
        public TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mTabs.length;
        }

        @Override
        public Fragment getItem(int i) {
            return TaskFileFragment.newInstance(Task.Type.values()[i]);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs[position];
        }
    }

}
