## Example for bind service with IPC (Inter-Process Communication)

This example will show how to use an Android bind service between two applications. Every application is running on a separate process. To communicatate between two 
processes we use IPC (Inter-Process Communication) on Android.

### Server and client apllication

For IPC we need to activate AIDL (Android Interface Definition Language). Add in gradle the aidl feature:

```
android {
    buildFeatures {
        aidl = true
    }
}
```

If you activated AIDL, created (File->New->AIDL) and modified the [AIDL file](https://github.com/mkett/android-client-server-bind-service-example/blob/main/ServerProvidesService/app/src/main/aidl/com/example/server/provides/service/IAidlRandomNumber.aidl), run "Rebuild Project". It will create all nessarry java files with Stub and Proxy. 
This AIDL Interface must also be added later on client side. Take care you copy or create the same interface with equal package name.

```
package com.example.server.provides.service;

interface IAidlRandomNumber {
    int getRandomNumber();
}
```

### Only server apllication

[Create your service](https://github.com/mkett/android-client-server-bind-service-example/blob/main/ServerProvidesService/app/src/main/java/com/example/server/provides/service/RandomNumberService.kt) (File->New->Service) on server application, provide your AIDL Stub in *onBinder* function as IBinder and make your service visible for other applications (**exported=„true“**) on [manifext.xml](https://github.com/mkett/android-client-server-bind-service-example/blob/main/ServerProvidesService/app/src/main/AndroidManifest.xml):

```
<application>
    ...
    <service
        android:name=".RandomNumberService"
        android:enabled="true"
        android:exported=„true“>
        <intent-filter>
            <action android:name="RandomNumberService"></action>
        </intent-filter>
    </service>
</application>
```

### Only client apllication

To use the service on clinet side, you need to define your query on [manifest.xml](https://github.com/mkett/android-client-server-bind-service-example/blob/main/ClientBindsToService/app/src/main/AndroidManifest.xml)

```
<queries>
    <package android:name="PACKAGE_NAME_OF_SERVER_SERVICE" />
</queries>
```

[Bind to the service](https://github.com/mkett/android-client-server-bind-service-example/blob/main/ClientBindsToService/app/src/main/java/com/example/client/service/MainActivity.kt) on your client application and receive data. Take care your service name is equal with the intent filter name defined in manifext.xml on server application
and your package is equal with the package name in AIDL Interface file.

```
Intent("RandomNumberService").also { intent ->
    intent.setPackage(„com.example.server.provides.service“)
    bindService(intent, connection, BIND_AUTO_CREATE)
}
```

If you are interested on an example how custom dangerous permissions can be defined and requested on Android, then check out the branch [add-custom-permission](https://github.com/mkett/android-client-server-bind-service-example/tree/feature/add-custom-permission)
