## NowInTheater

This tutorial aims to show the usage of some advanced java/android libraries to speed up Android development.

###### Libraries we are going to  use:
* [RxJava / RxAndroid](https://github.com/ReactiveX/RxAndroid)
* [Retrofit 1.9](http://square.github.io/retrofit/)
* [Picasso](http://square.github.io/picasso/)
* [OkHttp](http://square.github.io/okhttp/)
* [Leak Canary](https://github.com/square/leakcanary)
* [Timber](https://github.com/JakeWharton/timber)
* [Stetho](http://facebook.github.io/stetho/)
* [ButterKnife](http://jakewharton.github.io/butterknife/)
* [Parceler](https://github.com/johncarl81/parceler)

###### Things you will learn:
* Efficient network requests
* Efficient image loading
* Gracefully handle rotation changes
* Infinite Scrolling
* Design Support library animations

This tutorial assumes that you already have a good understanding of Java and some knowledge of Android
I will briefly explain most of the concepts and the code but for a deeper understanding I will redirect you to a WebPage of the library or a specific blog pos about it


### Let's get started!!

First of all we should update our

Top level build.gradle file. That is the build.gradle file that is in your project folder.

In the dependencies section we add

```groovy
// This plugins allow Android studio to understad generated code by ButterKnife and other
// Libraries, such as Parcel, Dagger, etc...
classpath 'com.neenbedankt.gradle.plugins:android-apt:1.4'
```

Now we gonna add the libraries that we need for our project:

Inside build.gradle in the **app** module add the following inside dependencies:

```groovy

// Support Libraries
compile 'com.android.support:appcompat-v7:23.0.1' // you can skip this one if you already have.
compile 'com.android.support:cardview-v7:23.0.1'
compile 'com.android.support:support-annotations:23.0.1'
compile 'com.android.support:support-v13:23.0.1'
compile 'com.android.support:support-v4:23.0.1'
compile 'com.android.support:design:23.0.1'
compile 'com.android.support:recyclerview-v7:23.0.1'

// Data handling & Network
compile 'io.reactivex:rxandroid:1.0.1'
compile 'io.reactivex:rxjava:1.0.14'
compile 'com.squareup.picasso:picasso:2.5.2'
compile 'com.squareup.retrofit:retrofit:1.9.0'
compile 'com.squareup.okhttp:okhttp-urlconnection:2.0.0'
compile 'com.squareup.okhttp:okhttp:2.0.0'

// Debug
compile 'com.jakewharton.timber:timber:3.1.0'
debugCompile 'com.squareup.leakcanary:leakcanary-android:1.3.1'
releaseCompile 'com.squareup.leakcanary:leakcanary-android-no-op:1.3.1'
compile 'com.facebook.stetho:stetho:1.1.1'
compile 'com.facebook.stetho:stetho-okhttp:1.1.1'

// UI
compile 'org.sufficientlysecure:html-textview:1.2'

// Code Generators
compile 'com.jakewharton:butterknife:7.0.1'
compile "org.parceler:parceler-api:1.0.3"
apt "org.parceler:parceler:1.0.3"

```

If you see a yellow ribbon on your gradle file click on **sync now** otherwise find the button **Sync Project with Gradle Files** on the toolbar.

Now that we have the setup done let's go to [Step 2](https://github.com/fnk0/NowInTheater/step2.md) and actually start coding!!

