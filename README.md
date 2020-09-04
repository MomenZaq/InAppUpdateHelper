# InAppUpdateHelper
Keeping your app up-to-date on your users’ devices.

A user experience that provides background download and installation with graceful state monitoring. This UX is appropriate when it’s acceptable for the user to use the app while downloading the update. For example, you want to urge users to try a new feature that’s not critical to the core functionality of your app.

In-app updates works only with devices running Android 5.0 (API level 21) or higher, and requires you to use Play Core library 1.5.0 or higher. Additionally, in-app updates support apps running on only Android mobile devices and tablets, and Chrome OS devices.

This class will use the Flexible in-app update "dialog that user can accept or ignore"


![Image of flexible_flow](https://developer.android.com/images/app-bundle/flexible_flow.png)

## How to use this class?
1- add this line to your gradle 

```java
implementation 'com.google.android.play:core:1.8.0'
```
2- copy the class to your project

3- call this method from your main activity
```java
inAppUpdateHelper.checkUpdate(this, containerView)
```

If there is a new update, the update dialog will display.

You can test it using [internal app sharing](https://support.google.com/googleplay/android-developer/answer/9303479).


For more details [Support in-app updates](https://developer.android.com/guide/playcore/in-app-updates).
