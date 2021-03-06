# Thunder

This project is composed of:
- thunder-sdk : The source code of the SDK
- thunder-sdk-release: The generated aar package from the SDK source code. Generated from the thunder-sdk module by executing thunder-sdk:assembleRelease
- app : The sample app to demonstrate how to implement the SDK

# How to use the Thunder SDK

1. Download the aar file: [thunder-sdk-release](https://github.com/SebRenon/Thunder/raw/master/thunder-sdk-release/thunder-sdk-release.aar)

2. From Android Studio: 
	- add a new module
	- import an existing AAR package
	- select the thunder-sdk-release.aar file you just downloaded
	- sync Gradle

3. In your app/build.gradle add:
  
  ```
  // Dependency to the new module
  compile project(':thunder-sdk-release')
  ```
  
2. In your activity/application class, initialize the SDK by putting the following code:
  
  ```
	ThunderSdk.init(
                "YOUR_SITE_KEY",
                "YOUR_API_KEY",
                "YOUR_LOGIN_ID",
                "YOUR_TOUCHPOINT",
                "YOUR_SHARED_SECRET");
  ```
                
3. Access the initialized SDK with:
  
  ```
  ThunderSdk.getInstance()
  ```
  
# How to send an interaction and get back the optimizations

1. [As shown here](https://github.com/SebRenon/Thunder/blob/master/app/src/main/java/com/srenon/thunder/MainActivity.java#L77), after initializing the SDK, you can send interaction by using the following code:

  ```
  ThunderSdk.getInstance().sendInteraction(interaction, new InteractionCallback() {
    @Override
    public void onSuccess(InteractionResponse response) {
        // Update UI with successful message
    }

    @Override
    public void onError(String error) {
       // Update UI with error message
    }
  });
  ```
        
2. For a successful request, you will get back an InteractionResponse object, from it, you can get the Optimizations [(example here)](https://github.com/SebRenon/Thunder/blob/master/app/src/main/java/com/srenon/thunder/DetailsActivity.java#L58):
  
  ```
  for (InteractionResponse.Optimizations optimization : interactionResponse.getOptimizations()) {
  	optimization.getDirectives;
  }
  ```

# Structure of the SDK

The SDK module is composed of multiple layers:
 - Domain: This is where the business logic resides. For example, we hanlde the TID requirement there.
 - Data: Reponsible to trigger the correct Data Source in order to execute the request
 - Network: Manage the connection to the backend and defines the endpoint
 - ThunderSDKActions is the interface shared to the Client
	
In terms of technologies the module uses:
 - RxJava for multithreading and business logic operations
 - Dagger to manage dependencies injections
 - Retrofit to define and execute the requests to the backend

The module is an Android Library because I imagined in the future, we might want to provide UI components or Android specific features... If not, making it a pure Java module wouldn't take too long.

I left comments in the code, but let me know if there is any questions!
