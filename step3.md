## Step 3: Coding the base classes

Now that we have our layouts in place we are going to code the base classes. Create a package called base and create the following classes.

* BaseActivity
* BaseFragment
* BaseRecyclerListFragment
* **interface** ItemCallback
* **interface** RxCallback
* **interface** RxSubscriber

#### BaseActivity

Our BaseActivity class will hold some of the basics that will be common to all our Activities such as a reference to the FragmentManager, the Toolbar and other stuff.

Here is the code of the BaseActivity (Heavily commented). For a detailed explanation of ButterKnife [click here](http://jakewharton.github.io/butterknife/)

```java
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * FragmentManager that this activity uses
     */
    protected FragmentManager mFragmentManager = null;

    @Nullable
    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;

    @Nullable
    @Bind(R.id.container)
    protected FrameLayout mContainerLayout;

    // Wheter this Activity has a Back arrow or not
    protected boolean mIsBackNav = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        ButterKnife.bind(this); // Bind the views of this Activity

        if(mToolbar != null) {
            // Sets the SupportActionBar
            setSupportActionBar(mToolbar);
        }

        if(mFragmentManager == null) {
            mFragmentManager = getFragmentManager(); // Gets a reference to the FragmentManager
        }
    }

    /**
     * This method will take care of displaying a Back arrow to the previous activity in the stack
     */
    protected void enableBackNav() {
        if(getSupportActionBar() != null) {
            mIsBackNav = true;
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    /**
     * Sets the status bar color if the device is on Lollipop or more
     * @param color
     *      The color to be used
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void setStatusBarColor(@ColorRes int color) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }
    }

    /**
     * Sets the title of the current toolbar
     * @param title
     *      The title for the toolbar
     */
    public void setTitle(@NonNull String title) {
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home && mIsBackNav) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this); // Unbind the views of this Activity to avoid Memory leaks
    }

    /**
     * This method should be Overriden if the Activity needs to use a different XML layout
     * @return
     *      The integer with the ID of the layout resource
     */
    @LayoutRes
    protected int getLayoutResource() {
        return R.layout.activity_base;
    }

    /**
    * Adds a fragment to the Container of this activity otherwise will throw a exception
    * The tag of a fragment can be used in some situations. The most common used cases are:
    *
    * 1. Retrieving a fragment from the BackStack
    * 2. Using the FragmentManager to retrieve retained fragments
    *
    * @param fragment
    *      The Fragment to be added
    * @param tag
    *      The string to be used as a Tag for this Fragment.
    */
   protected void addFragmentToContainer(@NonNull Fragment fragment, String tag) {
       FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
       fragmentTransaction.replace(R.id.container, fragment, tag).commit();
   }
}
```

#### BaseFragment

The BaseFragment class is much simpler than the Activity one (at least for a simple app like this)

```java
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
```

#### BaseRecyclerListFragment

This is a convenient class that we can use to display a List inside a RecyclerView in a Fragment.

Before we add the code for this fragment we are going to take a detour. Inside **res/values** create a file called integers.xml

Inside integers.xml add the following code:
```xml
<integer name="num_cols">2</integer>
```

Now inside **res**, create a folder named **values-land** and copy the integers.xml file inside. Change the value of num_cols for landscape to 3.

```java
public abstract class BaseRecyclerListFragment extends BaseFragment {

    @Bind(R.id.recyclerview)
    protected RecyclerView mRecyclerView; // Reference to our RecyclerView

    @Nullable
    protected GridLayoutManager mGridLayoutManager;

    @Nullable
    protected LinearLayoutManager mLinearLayoutManager;

    /**
     * Displays a list using a GridLayout with the number of columns specified in our xml file
     * @param adapter
     *       The adapter to be used by this List
     */
    protected void initGridCardsList(RecyclerView.Adapter adapter) {
        int numCols = getResources().getInteger(R.integer.num_cols);
        mGridLayoutManager = new GridLayoutManager(mRecyclerView.getContext(), numCols);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * Displays a list of items 1 item per row
     * @param adapter
     *      The adapter to be used by this list
     */
    protected void initLinearList(RecyclerView.Adapter adapter) {
        mLinearLayoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.list_fragment; // The fragment containing the RecyclerView list
    }
}
```

#### Our interface Callbacks

1. ItemCallback: This interface will be used to handle clicks passed form a List to a fragment/activity

```java
public interface ItemCallback<T> {
    void onItemClick(T t);
}
```

2. RxCallback: We use this interface to simplify our Subscriber code for RxJava.

```java
public interface RxCallback<T> {

    /**
     * This method is called each time a item is emitted by our Observable
     *
     * @param data
     *      The emitted data
     */
    void onDataReady(T data);

    /**
     * Called when there is a error on our stream
     *
     * @param e
     *      The exception throw
     */
    void onDataError(Throwable e);
}
```

#### The RxSubscriber

This is just a convenience class that I like to use when dealing with RxJava. Is not necessary but convenient.
Makes me not forget to unsubscribe a Subscriber and also automatically logs

```java
public class RxSubscriber<T> extends Subscriber<T> {

    RxCallback<T> callback;

    public RxSubscriber(RxCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    public void onCompleted() {
        // onCompleted is called after all the Observables of this Subscription have finished
        // We unsubscribe to not hold a reference to our activity and avoid Memory leaks
        unsubscribe();
    }

    @Override
    public void onError(Throwable e) {
        Timber.e(e, String.format("Error on the subscriber: %s", e.getMessage()));
        callback.onDataError(e);
    }

    @Override
    public void onNext(T data) {
        callback.onDataReady(data);
    }
}
```

If you are a little confused of what this is... don't worry! When we actually use RxJava everything will make sense!

###### Yay! we are done with our base package! Now let's start working with our Data Api! Head over to [Step 4](https://github.com/fnk0/NowInTheater/blob/master/step4.md)