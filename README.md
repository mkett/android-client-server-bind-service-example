## Example for a custom permission

This example will show how to define a custom dangerous permission and request it. The definition will be on a server appication and the request on client application.

### Define custom permission on server application

A permission has to be defined in [manifest.xml](https://github.com/mkett/android-client-server-bind-service-example/blob/feature/add-custom-permission/ServerProvidesService/app/src/main/AndroidManifest.xml):

```
<permission
    android:description="@string/permission_description"
    android:icon="@drawable/ic_launcher_foreground"
    android:label="@string/permission_label"
    android:name="com.example.permission.RANDOM_NUMBER_PERMISSION"
    android:protectionLevel="dangerous"/>
```

Add the new defined permission to our service in [manifest.xml](https://github.com/mkett/android-client-server-bind-service-example/blob/feature/add-custom-permission/ServerProvidesService/app/src/main/AndroidManifest.xml).

```
android:permission="com.example.permission.RANDOM_NUMBER_PERMISSION"
```

### Request permission on client application

Add the necessary custom permission you will use to [manifest.xml](https://github.com/mkett/android-client-server-bind-service-example/blob/feature/add-custom-permission/ClientBindsToService/app/src/main/AndroidManifest.xml) on client side. 

```
<uses-permission android:name="com.example.permission.RANDOM_NUMBER_PERMISSION"/>
```

Now [verify the persmission in your view](https://github.com/mkett/android-client-server-bind-service-example/blob/feature/add-custom-permission/ClientBindsToService/app/src/main/java/com/example/client/service/MainActivity.kt) and let the user grant it. Thurther Informations are available on [Android ducumentation](https://developer.android.com/training/permissions/requesting)

```
val dangPermToRequest: List<String> = checkMissingPermissions()
if (dangPermToRequest.isNotEmpty()) {
    requestPermissions(dangPermToRequest)
    return
}
```
