## Step 5. The UI

We already defined the XML layouts in a previous step and also made our Base UI classes.

### Implementing the ScrollListener

Our app is going to implement a technique often called "Infinite Scrolling". So.. What is this? Every time we make a call to the Api we are only going to retrieve 10 items and as the user scrolls we are going to append those items to the list.
To implement this I will give you 2 classes to handle this.

###### 1. The Callback: Create a interface named OnScrolledCallback

```java
public interface OnScrolledCallback {

    /**
     * This method will get called every time we hit the bottom of the screen and need to call
     * the Api for more data
     *
     * @param page
     *      The current page to be called
     */
    void onScrolled(int page);
}
```

###### 2. The ScrollListener: Create a class named ScrollListener

```java
public class ScrollListener extends RecyclerView.OnScrollListener {

    int mPreviousTotal = 0; // The total number of items in the dataset after the last load
    boolean loading = true; // True if we are still waiting for the last set of data to load.
    int visibleThreshold = 4; // The minimum amount of items to have below your current scroll position before loading more.
    int mFirstVisibleItem = 0;
    int mVisibleItemCount = 0;
    int mTotalItemCount = 0;
    private int mCurrentPage = 1;
    private GridLayoutManager mGridLayoutManager;
    private OnScrolledCallback mCallback;

    public ScrollListener(GridLayoutManager mLinearLayoutManager, OnScrolledCallback mCallback) {
        this.mGridLayoutManager = mLinearLayoutManager;
        this.mCallback = mCallback;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        mVisibleItemCount = recyclerView.getChildCount();
        mTotalItemCount = mGridLayoutManager.getItemCount();
        mFirstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (mTotalItemCount > mPreviousTotal) {
                loading = false;
                mPreviousTotal = mTotalItemCount;
            }
        }
        if (!loading && (mTotalItemCount - mVisibleItemCount) <= (mFirstVisibleItem + visibleThreshold)) {
            mCurrentPage++;
            mCallback.onScrolled(mCurrentPage);
            loading = true;
        }
    }
}
```

### The ViewHolder pattern.

To display items in a list very efficiently Android uses a pattern called ViewHolder. Basically Android only instantiates as many views it can fit in the screen and 2 extra rows of items. 1 before and 1 after the loading for a smooth scrolling.
A RecyclerView forces us to use a ViewHolder for efficient scrolling and memory. What happens is that every time we scroll it looks if a View already exists. And if exists will swap the Data for that view and reuse the same View.
The ViewHolder is basically a class with reference to a how bunch o Views. No data binding is done by the ViewHolder itself. The binding happens within the Adapter.

Enough talk! Let's write some more code! Create the MovieHolder class.

```java
public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ItemCallback<View> mCallback;

    @Bind(R.id.item_title)
    TextView movieTitle;

    @Bind(R.id.item_image)
    ImageView itemThumbnail;

    @Bind(R.id.item_rating_bar)
    RatingBar ratingBar;

    public MovieHolder(View itemView, ItemCallback<View> mCallback) {
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
```

### The Movies Adapter

An Adapter is a class that will Adapt a list of items and bind it to the Views of our ListView/RecyclerView
The default Adapter interface has a method ```getItemCount()``` which returns how many Items this Adapter holds. In our case will be items from a List<Movies>

```java
public class MoviesAdapter extends RecyclerView.Adapter<MovieHolder> {

    ItemCallback<View> mCallback;
    List<Movie> movies;

    /**
     *
     * @param movies
     *      The list of movies to be used by This Adapter
     * @param callback
     *      The callback that will receive a call when a Item is clicked on the list
     */
    public MoviesAdapter(@NonNull List<Movie> movies, @NonNull ItemCallback<View> callback) {
        this.movies = movies;
        this.mCallback = callback;
    }

    // onCreateViewHolder only gets called when we are instantiating a new View
    // When we are recycling views this will not be called
    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflates our .xml layout that we are going to be used by our ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieHolder(view, mCallback); // Returns a new instance of the ViewHolder
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        Movie movie = movies.get(position); // Retrieve the Movie from our list

        holder.ratingBar.setProgress(movie.getRatings().getAudienceScore());
        holder.movieTitle.setText(movie.getTitle());

        Picasso.with(holder.itemView.getContext())
                .load(MovieUtils.getHighResPicUrl(movie.getPosters().getThumbnail())) // Loads the Thumbnail
                .error(R.drawable.poster_default_thumb) // We fall back to the default Thumbnail in case of page load
                .fit() // Resizes the image to fit the ImageView and save memory
                .centerCrop() // Centers the image in the ImageView and crop extra elements
                .into(holder.itemThumbnail); // The ImageView on which load the image

        // The Tag object will be used by our onItemClick to pass data from the Adapter to the Fragment
        holder.itemView.setTag(R.id.movie, movie);
    }

    /**
     * Adds all the items to our current list (usually when we are finished loading a page)
     * @param movies
     *      The list of movies to be added to this Adapter
     */
    public void addAll(List<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public List<Movie> getMovies() {
        return movies;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
```
