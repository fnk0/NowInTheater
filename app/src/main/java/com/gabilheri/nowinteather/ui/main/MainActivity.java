package com.gabilheri.nowinteather.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gabilheri.nowinteather.Const;
import com.gabilheri.nowinteather.base.BaseActivity;

public class MainActivity extends BaseActivity {

    // Reference to our moviesFragment
    MoviesFragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Calls the BaseActivity method setTitle to set the Toolbar title
        setTitle("Movies");

        // We try to retrieve a instance of the fragment from the FragmentManager
        // If a fragment is already in memory
        mFragment = (MoviesFragment) mFragmentManager.findFragmentByTag(Const.MOVIE);

        // If the fragment is null we make a new Instance
        if (mFragment == null) {
            mFragment = MoviesFragment.newInstance();
        }

        // We call the method from the BaseActivity and add our fragment
        // Notice that the TAG is Const.MOVIE the same one that we used to retrieve the
        // Fragment from the FragmentManager
        addFragmentToContainer(mFragment, Const.MOVIE);
    }
}
