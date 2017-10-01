package app.numFacts;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.numFacts.Fragments.AdvancedFragment;
import app.numFacts.Fragments.RandomFragment;
import app.numFacts.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mainBinding;
    String[] tabs = {"Basic", "Advanced"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getTabs();
        mainBinding.tabLayout.setupWithViewPager(mainBinding.viewPager);
        mainBinding.viewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));
    }

    void getTabs() {
        for (int i = 0; i < tabs.length; i++) {
            mainBinding.tabLayout.addTab(mainBinding.tabLayout.newTab().setText(tabs[i]));
        }
    }

    class TabsPagerAdapter extends FragmentPagerAdapter {
        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment frag = null;
            switch (position) {
                case 0:
                    frag = new RandomFragment();
                    break;
                case 1:
                    frag = new AdvancedFragment();
                    break;
            }
            return frag;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }
}
