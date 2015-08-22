[![Build Status](https://travis-ci.org/twofortyfouram/android-test.png?branch=master)](https://travis-ci.org/twofortyfouram/android-test)
# Overview
The android-test-lib implements a variety of classes to fill in gaps in the Android test framework.  This library is intended only to be used for test purposes.


# API Reference
JavaDocs for the library are published [here](http://twofortyfouram.github.io/android-test).


# Compatibility
The library is compatible and optimized for Android API Level 8 and above.


# Download
## Gradle
The library is published as an artifact to jCenter.  To use the library, the jCenter repository and the artifact need to be added to your build script.

The build.gradle repositories section would look something like the following:

    repositories {
        jcenter()
    }

And the dependencies section would look something like this:

    dependencies {
        androidTestCompile group:'com.twofortyfouram', name:'android-test', version:'[3.0.0,4.0['
    }


# History
* 1.0.0: Initial release
* 1.0.1: Disable running ProGuard, to fix RuntimeInvisibleParameterAnnotations error
* 1.0.2: Update Android Gradle plugin, which changed the generated BuildConfig
* 1.0.5: Reupload artifacts with source and JavaDoc for inclusion in jCenter
* 2.0.0: Changed interface of ActivityTestUtil for the new AndroidJUnitRunner
* 3.0.0: Changed interface of FeatureContextWrapper to support Android Marshmallow