## Step 2: The Basics

We all know that java is a OOP language and one of the perks of using a OOP language is the ability to use inheritance and composition to abstract our code.
Android uses the same concepts. To build any android app you will at least extend Activity.

#### The application class

In android the Application class is by default a [Singleton](https://en.wikipedia.org/wiki/Singleton_pattern) making it perfect to hold any references for something we are planing to use through the application, avoiding extra initialization and memory leaks.
The application class is also the 1st thing that gets instantiated by the App. Making it a perfect place to start some of our libraries/

###### Note: DO NOT put a LOT of code inside onCreate... the more code you have there the longer will take for the app to boot.

We are going to use the Application class to instantiate some of our libraries. Create a class called **MoviesApp** and extend **Application**
Inside OnCreate we are going to instantiate LeakCanary, Timber, Stetho and also expose a reference to a instance of our Application.

```java
public class MoviesApp extends Application {

    private static MoviesApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                            .build());
        }
    }

    public static MoviesApp instance() {
        return mInstance;
    }
}
```

Our application class should look somewhat like this now.

##### Making our app using our custom application class

1. Open AndroidManifest.xml
2. Inside the application tag add the following: ```android:name=".MoviesApp"```

While we are here... let's add the Internet permission that we will need later.

Add the following permission to the app:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### Constants

We are going to need some constants.. create a file named **Const** or whatever you want and add the following constants

```java
public static final String API_KEY = "7ue5rxaj9xn4mhbmsuexug54"; // We need this to make APi Calls
public static final String MOVIE = "movie";
public static final String PIC_INIT_URL = "dkpu1ddg7pbsk";
public static final String TRANSITION_IMAGE = "imageTransition";
public static final String LIST_POSITION = "listPosition";
public static final String CURRENT_PAGE = "currentPage";
```

### Utils:

Most apps have utilities class which holds some Static methods used across the application. Since the rotten tomatoes API only gives us VERY small thumbnail images
We are going to have a method here to modify/hijack the URL to get a full size image that will look good in the DetailActivity later on.

Create a class named MovieUtils and add the following code:

```java
// final makes this class not be able to be extended
public final class MovieUtils {

    // Makes this class not be able to be Instantiated
    // i.e new MovieUtils()
    private MovieUtils() {}

    /**
     * Modifies the URL to get a full size image rather than a VERY small image
     * @param url
     *      The image URL
     * @return
     *      The modified URL
     */
    public static String getHighResPicUrl(String url) {
        String[] splitUrl = url.split(Const.PIC_INIT_URL);
        if(splitUrl.length > 1) {
            return String.format("http://%s%s", Const.PIC_INIT_URL, splitUrl[1]);
        } else {
            return url;
        }
    }
}
```

###### Note: If you want your own Api Key [Click here](http://developer.rottentomatoes.com/member/register). I can not guarantee this key working past September 10th.


## Adding some Assets:

To speed up this tutorial I'm providing you with the assets used in the app. Simple download [this file](https://github.com/fnk0/NowInTheater/blob/master/app/src/main/res/assets.zip), unzip and paste inside res. Select override for all

## Colors

Create a file named colors.xml inside res/values

and add the following (Feel free to put any other color you want):

```xml
<color name="primary">#259b24</color>
<color name="primary_dark">#0a7e07</color>
<color name="accent_color">#e84e40</color>
<color name="window_background">#eeeeee</color>
```

### Defining our Styles:

Open res/styles.xml and paste the following code:

```xml

<!-- Base application theme. -->
<style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
    <item name="colorPrimary">@color/primary</item>
    <item name="colorPrimaryDark">@color/primary_dark</item>
    <item name="colorAccent">@color/accent_color</item>
    <item name="android:windowBackground">@color/window_background</item>
</style>

<!-- Style for our RateBar showing the ratings of each movie -->
<style name="RateBarSmall" parent="Widget.AppCompat.RatingBar" >
    <item name="android:progressDrawable">@drawable/ic_rating</item>
    <item name="android:numStars">5</item>
    <item name="android:stepSize">20</item>
    <item name="android:layout_width">wrap_content</item>
    <item name="android:layout_height">wrap_content</item>
    <item name="android:minHeight">16dp</item>
    <item name="android:maxHeight">16dp</item>
    <item name="android:layout_marginLeft">4dip</item>
    <item name="android:layout_marginTop">4dip</item>
</style>

<!-- Style for the Title of each List item -->
<style name="ListItemTitle">
    <item name="android:layout_marginBottom">8dp</item>
    <item name="android:paddingLeft">8dp</item>
    <item name="android:paddingRight">8dp</item>
    <item name="android:paddingTop">4dp</item>
    <item name="android:textAppearance">?textAppearanceListItem</item>
</style>
```

###### Create a folder named values-v21

Create a file named styles.xml and add the following code.
This is just important for some cool transition animations and translucent status bar on Lollipop. This will not be used by older versions of Android

```xml
<resources>

  <!-- if an app have Lollipop installed will use this Style -->

  <!-- Base application theme. -->
  <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
    <!-- Customize your theme here. -->
    <item name="colorPrimary">@color/primary</item>
    <item name="colorPrimaryDark">@color/primary_dark</item>
    <item name="colorAccent">@color/accent_color</item>
    <item name="android:windowBackground">@color/window_background</item>

    <item name="android:windowDrawsSystemBarBackgrounds">true</item>
    <item name="android:statusBarColor">@android:color/transparent</item>

    <item name="android:windowContentTransitions">true</item>
    <!-- enable overlapping of exiting and entering activities-->
    <item name="android:windowAllowEnterTransitionOverlap">true</item>
    <item name="android:windowAllowReturnTransitionOverlap">true</item>

  </style>
</resources>
```

### Adding the Layouts:

[Click here](https://github.com/fnk0/NowInTheater/tree/master/app/src/main/res/layout) and grab layouts.zip or copy each file manually. To understand more about some of those new layouts such as CoordinatorLayout and AppBarLayout [click here](http://android-developers.blogspot.com/2015/05/android-design-support-library.html)

###### Now let's go to [Step 3](https://github.com/fnk0/NowInTheater/blob/master/step3.md) and code base classes!