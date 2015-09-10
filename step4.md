## Step 4: Coding the Data API

We are going to be using a library called Retrofit for the network calls. Retrofit is probably the most used Android libraries.

To read more about Retrofit [click here](http://square.github.io/retrofit/)

The basic concept is that when we make a network call a giant blob of JSON our XML is returned. And the task of parsing that data is often tedious and repetitive.
Retrofit lets us create POJO's (Plain Old Java Objects) and specify then as a result from our Network calls and parsing them automatically for us.
By default it uses Gson but we can define any mechanism for parsing the data as we wish. Such as Jackson Json/XML or any other one written by us If our data is not a common format.

Here is a basic chart of what is going to happen under the hood:

![retrofit](https://github.com/fnk0/NowInTheater/blob/master/images/retrofit.png)

### Creating or Pojo's

Creating Pojos is a repetitive and tedious task. That's why I'm going to show you a trick to automate it.

Head over to [this url](http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?apikey=7ue5rxaj9xn4mhbmsuexug54&page=1&page_limit=10)

That is the URL Response that we are expecting from the Rotten tomatoes API. The basics of the URL Is:

* http://api.rottentomatoes.com/api/public/ => This is our Endpoint
* /v1.0/ => The version of the API we are using
* /lists/ => Specify that we are retrieving a List rather than a single item
* /movies/ => The type of data we want
* /in_theaters.json/ => The data we want and the format
* Query parameters
    * apiKey => String with the key
    * page => Integer representing the page that we want
    * page_limit => How many items we want in each page

Now copy the entire response from the API and head over to [http://www.jsonschema2pojo.org/](http://www.jsonschema2pojo.org/). We are going to generate our Pojos to be used by the API

1. Paste the JSON response
2. Specify the package name of your app ```com.myapp.data.endpoints```
3. Class name: MovieResponse
4. Select JSON as a the **Source type**
5. For Annotation style select: Gson (which is the default for retrofit)
6. Click on Preview
7. Click on Jar to download a jar file with your classes
8. Change the file extension to .zip
9. Unzip and copy all the generated classes into your endpoints package.
10. You are ready to go! Take a look on the generated classes and think for a second how many hours of typing you just saved!


### Coding the Api

One of the best things of Retrofit is the ability to choose which Network client we want to use for our calls. If no client is specified it defaults to the Default HttpClient in Android.

For this project we are going to  use OkHttp which is a HttpClient made by Square the same company who made Retrofit. As of Android 5.0 OkHttp is the Default HttpClient used by Android (Yes... OkHttp is so good that Google decided to use it themselves)
But we are going to specify it ourselves so it is compatible with older versions of Android.

##### 1. The NetworkClient

```java
public class NetworkClient {

    // This is the Cache size that we are going to use for our HttpClient
    private static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

    /**
     * Only used internally
     * Initializes the OkHttpClient and sets up the cache
     *
     * @param context
     *      The context used to get the Android cache directory
     * @return
     *      A new instance of the HttpClient
     */
    private static OkHttpClient initOkHttpClient(Context context) {
        OkHttpClient client = new OkHttpClient();

        if (BuildConfig.DEBUG) {
            client.networkInterceptors().add(new StethoInterceptor());
        }

        // Install an HTTP cache in the application cache directory.
        try {
            File cacheDir = new File(context.getCacheDir(), "http");
            Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
            client.setCache(cache);
        } catch (IOException e) {
            Timber.e(e, "Unable to install disk cache.");
        }

        return client;
    }

    /**
     * To specify the OkHttpClient we need to wrap it in a OkClient first to be used by Retrofit
     *
     * @param context
     *      The context to be passed to the OkHttpClient initializer
     * @return
     *      THe OkClient instance
     *
     */
    public static Client getOkClient(Context context) {
        return new OkClient(initOkHttpClient(context));
    }
}
```

##### 2. The Api

Here is our Api. (Yes this is all we need)

```java
public interface Api {
    /**
     *
     * A lot of Magic is going on here... First of all we specify Retrofit that we are going to make
     * a GET request to the API with 3 Query Parameters
     *
     * Retrofit makes the Call to the API and parses the GSON into our MovieResponse
     *
     * @param key
     *      The Api Key for the API
     * @param page
     *      The page that we want to retrieve data from
     * @param page_limit
     *      The page limit of items
     * @return
     *      A RxJava Observable with the MovieResponse from the API
     */
    @GET("/lists/movies/in_theaters.json")
    Observable<MovieResponse> getMovies(
            @Query("apikey") String key,
            @Query("page") int page,
            @Query("page_limit") int page_limit
    );
}
```

##### 3. The ApiClient

```java
public class ApiClient {

    public static final String API_VERSION = "v1.0";
    public static final String API_BASE_URL = "http://api.rottentomatoes.com/api/public";

    /**
     * Using the Application class which is a Singleton by nature guarantees this client to be a
     * singleton. Will also keep the activities from holding references to it avoiding memory leaks
     *
     * @param application
     *      Application on which this client should be initialized
     * @return
     *      The Api on which to make calls
     */
    public static Api initApi(Application application) {
        return new RestAdapter.Builder()
                // Sets the base Endpoint of this client
                .setEndpoint(buildApiEndpointURL())
                // If we are in a Debug build them we set the LogLevel to FULL
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                // The HttpClient that we are going to use to by the Api
                .setClient(NetworkClient.getOkClient(application))
                // Builds the RestAdapter that we is going to be used to create the API
                .build()
                // Creates the API
                // Note: this should be the Api.class that we made
                .create(Api.class);
    }

    /**
     * @return
     *      The string with our Endpoint URL
     */
    private static String buildApiEndpointURL() {
        return String.format("%s/%s", API_BASE_URL, API_VERSION);
    }
}
```

### Adding a reference to the API inside the Application class

You may have noticed that all the code inside NetworkClient and ApiClient just return new instances
Those objects are fairly large and memory consuming, so we don't want to be instantiating them all the time
Instead we use the Singleton Application class to hold a instance of our API that will be used across the application

Open the MoviesApp class and add the following code:

```java
// Holds a reference to our API
Api mApi;

/**
 * If the api is null them we make a new instance
 * Otherwise we just return it
 * This technique is called Lazy Instantiation allowing us to only instantiate singleton
 * Objects when we actually need them rather than keeping objects not used in Memory
 *
 * @return
 *      A instance of the Api
 */
public Api api() {
    if(mApi == null) {
        mApi = ApiClient.initApi(this);
    }
    return mApi;
}

```


###### We are done with the Api code! Head over to [Step 5](https://github.com/fnk0/NowInTheater/blob/master/step5.md) to implement the UI of our App.