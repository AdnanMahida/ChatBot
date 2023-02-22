# ChatBot

An android app built using [Jetpack (ViewModel)](https://developer.android.com/jetpack?gclid=EAIaIQobChMIhqv0uOGp_QIVyrmWCh2eCw96EAAYASAAEgKu_PD_BwE&gclsrc=aw.ds), [Material Design](https://developer.android.com/develop/ui/views/theming/look-and-feel) and [MVVM architecture](https://developer.android.com/topic/architecture). consumes [BrainShop API](https://brainshop.ai) for chatbot response.


## Download
Go to the [Releases](https://github.com/AdnanMahida/ChatBot/tree/master/apk) to download the latest APK.

<img src="/screenshot/Screenshot_1.png" width="280" height="600" />

## Tech stack & Open-source libraries
- Minimum SDK level 24
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) for asynchronous.
- Jetpack
 - Lifecycle: Observe Android lifecycles and handle UI states upon the lifecycle changes.
 - ViewModel: Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
 - ViewBinding: Binds UI components in your layouts.
 - MVVM Architecture (View  - ViewModel - Model)
 - Repository Pattern
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit): Construct the REST APIs and paging network data.


## Setup Requirements
First, obtain your API key from [BrainShop](https://brainshop.ai/) and add it in a file named `gradle.properties` within the root directory:

```bash
BRAIN_ID="****"
API_KEY="****"
```

Then, replace it in the `build.gradle(:app)` :

```bash
...
buildConfigField("String", "BRAIN_ID", BRAIN_ID)
buildConfigField("String", "API_KEY", API_KEY)
```

Finally, rebuild the project for changes to take effect

Done <3 Happy Coding
