# Thunder

This project is composed of the Thunder SDK project and a sample app project:
- the sample app is just a demo about how to use the Thunder-SDK.
- the Thunder SDK is a library to communicate with ONE

# How to use the Thunder SDK
As shown in the [sample app](https://github.com/SebRenon/Thunder/blob/master/app/src/main/java/com/srenon/thunder/MainActivity.java#L63) , to use the SDK you simply follow these steps:

1. Download the aar file: [thunder-sdk-release](https://github.com/SebRenon/Thunder/raw/master/thunder-sdk-release/thunder-sdk-release.aar)

2. From Android Studio: 
	- add a new module
	- import an existing AAR package
	- select the thunder-sdk-release.aar file you just downloaded
	- sync Gradle

3. In your app/build.gradle add:
  
  ```
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
  
#How to send an interaction and get back the optimizations

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
