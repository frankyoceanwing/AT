# AT

AT is a development tool for mocking location.

### Intent requests

In order to launch AT with an intent you must first create an `Intent` object, specifying its **action**, and **Uri**.

- **action**

  - start service
    - `com.oceanwing.at.action.START`
  - stop service
    - `com.oceanwing.at.action.STOP`

- **Uri**

  - Scheme: `mockgps`

  - Host: `com.oceanwing.at`

  - Path: `path`

  - Query Parameters:

    - **api** (**required**, string): routing API
      - `api=HREE`
      - `api=Google`
    - **speed** (**required**, float): 0 - 200 kilometers per hour
      - `speed=60.0`
    - **origin** (**required**): latitude,longitude value
      - `origin=37.7256676,-122.4496459`
    - *destination** (**required**): latitude/longitude value
      - `destination=36.1633689,-115.1444786`
    - run (optional, string):
      - `run=Once`
      - `run=Forever`

    ​

After creating the `Intent`, you can request that the system launch AT.

A common method is to pass the Intent to the `startActivity()` method.

```java
// Create a Uri
Uri atIntentUri = Uri.parse("mockgps://com.oceanwing.at/path?api=HERE&speed=60.0&run=Once&origin=37.7256676,-122.4496459&destination=36.1633689,-115.1444786");

// Create an Intent and set the action
Intent atIntent = new Intent("com.oceanwing.at.action.START", atIntentUri);

// Attempt to start an activity that can handle the Intent
startActivity(atIntent);
```

It's similar but more simpler to stop AT.

```java
// Stop AT
Intent atIntent = new Intent("com.oceanwing.at.action.STOP");
startActivity(atIntent);
```



### Tips

If the system cannot identify an app that can respond to the intent, your app may **crash**. 

For this reason, you should first verify that a receiving application is installed before you call `startActivity()`.
To verify that an app is available to receive the intent, call `resolveActivity()` on your Intent object.
If the result is non-null, there is at least one app that can handle the intent and it's safe to call `startActivity()`.

```java
if (atIntent.resolveActivity(getPackageManager()) != null) {
    // ...
}
```
