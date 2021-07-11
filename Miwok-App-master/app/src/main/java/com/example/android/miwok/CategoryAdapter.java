package com.example.android.miwok;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

public class CategoryAdapter extends FragmentPagerAdapter {

    Context mcontext;
    public CategoryAdapter(Context mcontext, FragmentManager fm) {
        super(fm);
        this.mcontext = mcontext;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new NumbersFragement();
            case 1:
                return new FamilyFragement();
            case 2:
                return new ColorsFragment();
            case 3:
                return new PhrasesFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return mcontext.getString(R.string.category_numbers);
            case 1:
                return mcontext.getString(R.string.category_family);
            case 2:
                return mcontext.getString(R.string.category_colors);
            case 3:
                return mcontext.getString(R.string.category_phrases);
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return 4;
    }
}
