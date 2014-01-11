[![Build Status](https://travis-ci.org/twofortyfouram/android-test.png?branch=master)](https://travis-ci.org/twofortyfouram/android-test)
# Overview
The android-test-lib implements a variety of classes to fill in gaps in the Android test framework.  This library is intended only to be used for test purposes.


# API Reference
JavaDocs for the library are published [here](http://twofortyfouram.github.io/android-test).


# Compatibility
The library is compatible and optimized for Android API Level 8 and above.


# Download
## Gradle
The library is published as an artifact to the two forty four a.m. maven repository.  To use the library, the two forty four a.m. maven repository and the artifact need to be added to your build script.

The build.gradle repositories section would look something like the following:

    repositories {
        mavenCentral()
        maven { url 'https://dl.bintray.com/twofortyfouram/maven' }
    }

And the dependencies section would look something like this:
    
    dependencies {
        instrumentTestCompile group:'com.twofortyfouram', name:'android-test', version:'1.0.1+', ext:'aar'
    }

## Ant
Download the JAR from the Github releases page and include it in the libs/ directory of the Android Ant project.
