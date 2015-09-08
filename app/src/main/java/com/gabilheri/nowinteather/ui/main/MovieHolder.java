package com.gabilheri.nowinteather.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gabilheri.nowinteather.R;
import com.gabilheri.nowinteather.base.ItemCallback;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by <a href="mailto:marcusandreog@gmail.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 9/7/15.
 */
public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ItemCallback mCallback;

    @Bind(R.id.item_title)
    TextView movieTitle;

    @Bind(R.id.item_image)
    ImageView itemThumbnail;

    @Bind(R.id.item_rating_bar)
    RatingBar ratingBar;

    public MovieHolder(View itemView, ItemCallback mCallback) {
        this(itemView);
        this.mCallback = mCallback;
    }

    public MovieHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mCallback.onItemClick(v);
    }
}
