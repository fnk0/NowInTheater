## Step 6. The DetailActivity

This is a extra step.. so I will go into less details here...

1. Create a class name DetailActivity
2. Add the following to the Manifest

```xml
<!-- the android:name="" may change... depending on where you created your file -->
<activity
    android:name=".ui.detail.DetailActivity"
    android:label="@string/app_name" />
```

3. Make DetailActivity extend BaseActivity

DetailActivity code:

```java

public class DetailActivity extends BaseActivity {

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @Bind(R.id.item_bg)
    ImageView mItemBackground;

    @Bind(R.id.item_rating_bar)
    AppCompatRatingBar mRatingBar;

    @Bind(R.id.movie_title)
    AppCompatTextView mTitle;

    @Bind(R.id.synopsis)
    AppCompatTextView mSynopsis;

    Movie movie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackNav();
        mCollapsingToolbar.setTitle(""); // We set the title to nothing so we don't overlap our image
        setTitle(""); // same here

        // get our movie object from our extras bundle
        movie = Parcels.unwrap(getIntent().getExtras().getParcelable(Const.MOVIE));

        // Loads the movie into the UI
        loadMovie();
    }

    // Loads a movie obect into the UI
    // This UI is fairly simple... If you take a look into the Movie object
    // There's a LOT more information... But I will leave this as an exercise
    // For you to do later
    void loadMovie() {
        loadPicture(movie.getPosters().getOriginal());
        mTitle.setText(movie.getTitle());
        mSynopsis.setText(movie.getSynopsis());
        mRatingBar.setProgress(movie.getRatings().getAudienceScore());
    }

    /**
     * Loads a picture from the internet into the parallax Background ImageView
     * @param url
     *      the URL to be loaded
     */
    void loadPicture(String url) {
        Picasso.with(this)
                .load(MovieUtils.getHighResPicUrl(url))
                .error(R.drawable.poster_default_thumb)
                .fit()
                .centerCrop()
                .into(mItemBackground);
    }

    /**
     * Allows the user to share this movie with a friend through whatever provider they choose
     */
    @OnClick(R.id.fab_share)
    public void share(View v) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this movie!!\n" + movie.getLinks().getAlternate());
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share Movie"));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_detail;
    }
}
```

### Responding to events inside MoviesFragment

Now open MainActivity again and find the **onItemClick(View v)** method and make it look like this:

```java
@Override
public void onItemClick(View v) {
    ImageView imageView = ButterKnife.findById(v, R.id.item_image);
    Movie movie = (Movie) v.getTag(R.id.movie);
    Intent intent = new Intent(getActivity(), DetailActivity.class);
    intent.putExtra(Const.MOVIE, Parcels.wrap(movie));
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        // This will animate the transition in between the list and the DetailActivity
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), imageView, Const.TRANSITION_IMAGE);
        getActivity().startActivity(intent, options.toBundle());
    } else {
        getActivity().startActivity(intent);
    }
}
```