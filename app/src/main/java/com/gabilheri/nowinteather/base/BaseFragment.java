package com.gabilheri.nowinteather.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by <a href="mailto:marcusandreog@gmail.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 9/7/15.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutResource(), container, false); // Inflate the view for this Layout
        ButterKnife.bind(this, v); // Bind the views of this Fragment
        return v; // Return the inflated and binded view
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this); // Unbind the views
    }

    /**
     *
     * Abstract method that all classes that extends BaseFragment should implement.
     * Should always return a Layout reference. Hence the @LayoutRes tag
     *
     * @return
     *      The ID of the layout to be used by this Fragment
     */
    @LayoutRes
    protected abstract int getLayoutResource();
}
