package com.gabilheri.nowinteather.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gabilheri.nowinteather.Const;
import com.gabilheri.nowinteather.base.BaseActivity;

public class MainActivity extends BaseActivity {

    MoviesFragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Movies");

        mFragment = (MoviesFragment) mFragmentManager.findFragmentByTag(Const.MOVIE);

        if (mFragment == null) {
            mFragment = MoviesFragment.newInstance();
        }

        addFragmentToContainer(mFragment, Const.MOVIE);
    }
}
