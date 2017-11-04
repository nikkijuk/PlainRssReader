# PlainRssReader

PlainRssReader is very very simple app which reads rss files, 
converts them to json using web api and allows browsing thru simple list

# Background

App has been created as part of [Coursera Android Programming Capstone] Project. 

# Documentation

README.md contains architecture, design and implementation artifacts like mockups, uml diagrams, screenshots, etc. Code comments document low level decisions and mechanisms.

# Repository

Instead of having private GitLab repository I've decided to go with public Github account to share my experience with larger community.

Repositorys docs directory contains resources which accompany this documentation, otherwise repository contains solely application artifacts.

# Architectural requirements 

Requirements for apps architecture and structure are 

```
- Interact with at least one remotely-hosted web service over the network via HTTP.
- Allow users to navigate between at least two different user interface screens at runtime.
```

# User facing functionality

Requirements for apps user interface are 

```
a hypothetical RSS/Atom reader app might have multiple screens, such as

* a ListView showing all RSS Feed Stories,
* a detail View showing a single Feed Story, and
* a Settings view for configuring information about the App’s settings.
  - use the Junit testing framework to implement unit and integration tests for their application.
  - tests must cover the functionality of the app. 
  - Support persistent storage of information in an app’s content provider.
* Have well documented source code and a short video that shows how your app works when it's run.
```

To study technical abilities of Android more than implement novel solution I've decided to implement stripped down RSS reader as suggested.

# External services

I've decided to go with http protocol with json payload. For this reason I'll convert Rss XML using external service.

XML to Json conversion is done using [rss2json] service. [Gson] is used to marshall returned JSON to Plain Java Pojos.

Example url of background service. rss_url query parameter defines source of rss feed.

```
 https://api.rss2json.com/v1/api.json?rss_url=https%3A%2F%2Fwww.theguardian.com%2Finternational%2Frss
```
 
# Software Architecture 

"[Software architecture] is those decisions which are both important and hard to change." - Martin Fowler.

Development is done using [Android Studio], which is currently at 3.0 RC1. RC is used 'cos it has [Java 8 support] and lambdas are pretty nice.

I have decided to start with simple architecure with more or less vanilla android components working on [API-level 16] or [API-level 19]. Java 8 Streams are not used, since they doesn't exist before [API-level 24]. [Android devices] with API-level 24 are still not commonly in use, but 19 is safe choice for most users.

I'll try later to expand application with selected [Android architecture components], escpecially [Room] for persistence. 

I'm considering to use
- [Dagger]
- [Retrofit]
- [Android annotations]

But it will be of highest priority to keep code working, not to polish it with unnecessary features and add code and libraries without real benefits. For this reason [Guava] is not yet in use, even if I see it as useful tool.

I try to keep in mind that [64K DEX limit] can possibly make my life hard as I'm not willing to invest in learning bytecode optimization tricks with [ProGuard] in this phase of development.

# Architecture diagrams

[PlantUml] is used to illustrate high level structure of application. UML diagrams are written using textual DSL. 

![UML component diagram of app]

It's easy to write uml models with [PlantUml] when you have practiced it a bit. Here's source of components diagram.

```
@startuml
package "PlainRssReader" {
  [SettingsActivity]
  [FeedActivity] 
  [RecyclerView]
  [PreferenceFragment]
}

package "AndroidServices" {
  [AndroidBrowser] 
}

cloud {
  [RssToJsonConverter]
}

cloud {
  [RssFeedProducer]
}

[FeedActivity] -up-> [SettingsActivity]:configure rss feeds url

[FeedActivity] - [RecyclerView]:browse feed
[SettingsActivity] - [PreferenceFragment]:edit url

[FeedActivity] --> [AndroidBrowser]:ask browser to show article 
[AndroidBrowser] --> [RssFeedProducer]:get single artice as html

[FeedActivity] --> [RssToJsonConverter]:get rss feed as json
[RssToJsonConverter] --> [RssFeedProducer]:get rss feed as xml
@enduml
```
Use [PlantUml testbench] if you want to experiment with given source.

# Design

"there's no way to design the software in advance. Instead, you must design your software based on its current needs, and evolve the software design as the requirements change. This process is called [evolutionary design]." - James Shore.

It's tempting to think that there's a way to know all details in advance and one could start work once plan in finished. I have taken different stance. Design evolves as I have more information, knowledge or time, and implementation follows design immendiately. Some call this iterative software development or emergent design in contrast to Big Front Up or Plan Driven Desing. Simply put: I try to defer decisions to last responsible moment, but still steer my work with current knowledge.

# Design Discussion 

Requirements of Mooc dictate how app should be implemented by stating

```
Comprise at least one instance each of the following fundamental Android component categories 
covered in the four previous Content MOOCs in the AAD Specialization:
- Activity
- BroadcastReceiver
- Service (Bound or Started)
- ContentProvider.
```

Without such rigid restrictions of techniques to use application could have looked like [LifecycleAwareRssReader].

## Role of Content provider

Content providers shouldn't be used, as stated on [Sample content provider] code, "you don't need to implement a ContentProvider unless you want to expose the data outside your process or your application already uses a ContentProvider.". 

Please also refer to official documentation of [Content Provider], which says "Use content providers if you plan to share data. If you don’t plan to share data, you may still use them because they provide a nice abstraction, but you don’t have to."

Implementing [Content provider] would add complexity without any gains, so it's easier, faster and better to use [SQLite] directly or using [Room].

NOTE: THERE IS READ ONLY CONTENT PROVIDER IMPLEMENTED! This was necessary, as one needs to get full points and use all components to get further in capstone project. I feel this bit daunting, but so are rules ..

```
Try again
You earned 25 points, but you need to earn at least 30 / 30 points to pass. Review your feedback below, improve your submission, and resubmit when you're ready. 
```

Please see here implementation of [ArticleContentProvider]

## Role of Services and Broadcast Receivers

Needless to say, that pain vs. gain or complexity conciderations apply to BroadcastReceiver and Service components also.

BroadcastReceivers could be used to listen network state, so that calls external services would be only allowed when network is present and status of network would be updated based on system notifications. Need is not very clear, since network state can be checked before each call to http service.

BroadcastReceivers are also able to process return values from IntentServices or other asynchronously working background operations, and thus it would be possible to use local broadcasts to application internal communication if this is needed.

Reader doesn't expose services to other applications, and thus there's no need for separate processes, which could be used from external apps. Calling possibly unreliable http api's do not need services if simply async task does the trick and allows calling http endpoint outside of UI thread as current version of Reader is doing. 

It seems that services have some benefits over AsyncTasks, which are bound to UI, and are gone is Activity is killed as part of configuration change due to device orientation change. If AsyncTasks compromise reliability of app, it might mean that IntentService is needed for syncing rss feeds, and thus also local broadcasts to return results of sync.

There's pro's and cons on using simple IntentService or AsyncTasks, but are experiences of others like [IntentService-vs-AsyncTask 1], [IntentService-vs-AsyncTask 1] or [AsyncTask problems] really relevant? And recommendations? Use this, use that, use [Loaders]? Or use [Volley]?

App contains sample flow which starts background operation inside IntentService and listens notifications sent by service using broadcast receiver. 

I might also implement simple flow using [Volley]. See [Volley tutorial] for more info.

## Why implementing offline browsing of articles

Motivation for buffering results for offline use can be seen at [Next Billion Users]

# Use cases

[yEd] was used to draw simple diagram of use cases.

![UML use case diagram of app]

# Classes

[Reverse engineering UML model with Andoid studio] was done with [SimpleUMLCe].

Model classes store retrieved articles

![UML class diagram of feed model] 

It wouldn't be necessary to store all attributes of rss feed, but it's done here for completeness.

# Mockups

Mockups were prepared with [marvel app]

List of items

![mockup-list]

Selected item

![mockup-item]

settings

![mockup-settings]

I did have trial versio in use, and for this reason I needed to take snapshots from screens instead of neatly exporting results to files.

# Implementation

Rss feed reading is proxied thru [rss2json] service, which converts feed to json on the fly.

Feed url is read from preferences. When no feed is defined [Default feed] is used.

Browsing items is implemented using RecyclerView as defined in [use recycler-view]

Storing and changing settings is implemented using PreferefencesFragment as defined in [use preferences]

Due to limitations and interoperability issues with [Room] annotation processors [AutoValue] and [Lombok] aren't used to reduce boilerplate code of model classes, see [AutoValue issue] and [Room issue] for deeper discussion.

Planned: In app asynchronous processing and communication is documented at [Background processing best practices].

## calling http endpoints

It's possible to use [HttpUrlConnection] for getting json data. As implementation will be eventually hard to read it's better to look for stable library instead.

As API is actually just normal get, which has nothing to do with Rest, using [Retrofit] seems overkill - so I won't try it even if it looks very handy.

To allow calling using simple api [okHttp] seems to be optimal. It doesn't hide IO errors, but simplifies creating connection, closing connection, parsing response body, etc. See [okHttp documentation] for more.

Note that [OkHttp] uses [okIo] to optimize usage of javas IO system, which should lead to higher performance and reliability.

There's at least one more way of calling http service and working on results. Consider operations as queue and use [Volley] like in this example of [Http get with Volley]. Listeners are run on queued operations result, either successful or error, and refresh ui directly.

## persisting feeds to temporaty file

I have tried to persist results of feed reading in intent service to temporary file and then reading results at broadcast receiver. While this works feed is just big single CLOB which needs to be read to memory for parsing. Location of file needs to be sent from intent service to broadcast receiver using extras of intent.

## persisting articles to SQLite

[SQLite] database is used as follows

Intent Service 
- reads feed from http endpoint as json
- converts feed json to pojos
- pojos are converted to article database objects and saved to [SQLite]
- content ready event is sent

Broadcast Receiver
- reads articles from [SQLite]
- creates new articles adapter for Recycler View

Broadcast Receiver is here observer, and in charge of updating model. Intent service is representing observable entities. It would be possible to schedule Intent Service to poll http endpoint and notify Broadcast Receiver when there's new content.

Intent Service / Broadcast Receiver design works, but has a lot of moving parts developer needs to get right - and might get wrong. [Android Architecture Components] are set of components and conventions which are worth to study to get glimpse of how this could be done easier. Notify especially [LiveData] as source of evemts - clever move towards reactive programming.

[Room] is used as ORM, it is pretty powerful, but usage is here really limited. [Room testing] explains how to test database operations.

Read [Room tutorial] and work thru [Room codelab] and [Android lifecycles codelab] for more details. When you have got this far you might be interested to read [LiveData patterns and antipatterns] and play with [Android arhitecture blueprints]. And don't miss [Android architecture guide].

# Snapshots of current implementation

List of items

![list]

Selected item

![item]

settings

![settings]

change url dialog

![settings-url]

# Videos

LogCat logging and apps's usage can be seen as screencast [PlainRssReader in YouTube] and [PlainRssReader Mp4]

# Known bugs

User given url is not checked (syntax, existence). When app uses incorrect url app is rendering empty list. User has hard time of knowing what went wrong, so it would be nice to verify url when given and show error message when url can't be reached.

[Coursera Android Programming Capstone]: https://www.coursera.org/learn/aadcapstone/home/welcome "Coursera Android Capstone"

[yEd]: https://www.yworks.com/products/yed "yEd diagramming software"

[use recycler-view]: https://willowtreeapps.com/ideas/android-fundamentals-working-with-the-recyclerview-adapter-and-viewholder-pattern/ "how to use recycler view, adapter and holder"

[use preferences]: http://www.cs.dartmouth.edu/~campbell/cs65/lecture12/lecture12.html "how to use preferences fragment"

[Background processing best practices]: https://developer.android.com/training/best-background.html "Background processing"

[Android studio]: https://developer.android.com/studio/preview/index.html "Android studio 3.0 RC1"

[Java 8 support]: https://developer.android.com/studio/write/java8-support.html "Android studio java 8 support"

[API-level 16]: https://developer.android.com/about/versions/android-4.1.html "Android 4.1 / Api-level 16"

[API-level 19]: https://developer.android.com/about/versions/android-4.4.html "Android 4.4 / Api-level 19"

[API-level 24]: https://developer.android.com/about/versions/nougat/android-7.0.html "Android 7.0 / Api-level 24"

[Android devices]: https://developer.android.com/about/dashboards/index.html "Android devices in use"

[Default feed]: http://rss.nytimes.com/services/xml/rss/nyt/Science.xml "Ny Times science feed"

[rss2json]: https://rss2json.com "rss xml to json converter"

[marvel app]: https://marvelapp.com/ "Mockups for iOs, Android, Web, etc."

[Software architecture]: https://kylecordes.com/2015/fowler-software-architecture

[evolutionary design]: http://www.jamesshore.com/In-the-News/Evolutionary-Design-Illustrated-Video.html "Evolutionary desing"

[PlantUml]: http://plantuml.com/ "Fantastic text based modeling tool"

[PlantUml testbench]: www.plantuml.com/plantuml/ "simple service to verify PlantUml markup"

[Reverse engineering UML model with Andoid studio]: https://stackoverflow.com/questions/17123384/how-to-generate-class-diagram-uml-on-android-studio/36823007#36823007 "Reverse engineering UML model with Andoid studio"

[SimpleUMLCe]: https://plugins.jetbrains.com/plugin/4946-simpleumlce "very simple uml diagramming tool"

[Android architecture components]: https://developer.android.com/topic/libraries/architecture/index.html "Android architecure components by Google"

[LiveData patterns and antipatterns]: https://medium.com/google-developers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54

[Android arhitecture blueprints]: https://github.com/googlesamples/android-architecture

[Android architecture guide]: https://developer.android.com/topic/libraries/architecture/guide.html

[Room]: https://developer.android.com/topic/libraries/architecture/room.html "Room persistence library"

[Room tutorial]: http://www.vogella.com/tutorials/AndroidSQLite/article.html

[Room codelab]: https://codelabs.developers.google.com/codelabs/android-persistence/

[Room testing]: https://commonsware.com/AndroidArch/previews/testing-room

[Android lifecycles codelab]: https://codelabs.developers.google.com/codelabs/android-lifecycles/

[LiveData]: https://developer.android.com/topic/libraries/architecture/livedata.html

[Dagger]: https://google.github.io/dagger/ "dependency injection done with generated classes"

[HttpUrlConnection]: https://stackoverflow.com/questions/8654876/http-get-using-android-httpurlconnection "low level http usage"

[okHttp documentation]: http://square.github.io/okhttp/ "documentation of okHttp"

[okHttp]: https://github.com/square/okhttp "simple api for http operations"

[okIo]: https://github.com/square/okio "extension of java io libraries"

[Retrofit]: http://square.github.io/retrofit/ "simplify http api's"

[Volley]: https://developer.android.com/training/volley/index.html "operations as queue"

[Volley tutorial]: https://www.sitepoint.com/volley-a-networking-library-for-android/

[Http get with Volley]: https://developer.android.com/training/volley/simple.html

[Android annotations]: http://androidannotations.org/ "annotation processor and code generator for boilerplate code"

[Guava]: https://github.com/google/guava "Google guava is handy toolbox"

[64K DEX limit]: https://developer.android.com/studio/build/multidex.html "Method table limit on Dalvik Executables"

[ProGuard]: https://www.guardsquare.com/en/proguard "Bytecode optimization and obfuscation tools"

[Content provider]: https://developer.android.com/guide/topics/providers/content-providers.html "Content provider documentation"

[Sample content provider]: https://github.com/googlesamples/android-architecture-components/blob/master/PersistenceContentProviderSample/app/src/main/java/com/example/android/contentprovidersample/provider/SampleContentProvider.java "Sample content provider"

[ArticleContentProvider]: https://github.com/nikkijuk/PlainRssReader/blob/master/app/src/main/java/com/jukkanikki/plainrssreader/contentprovider/ArticleContentProvider.java

[SQLite]: https://www.sqlite.org/ "Low footprint embedded database"

[GSON]: https://github.com/google/gson "Googles serialization library"

[AutoValue]: https://github.com/google/auto/tree/master/value "AutoValue for generation of Value objects"

[AutoValue issue]: https://developer.android.com/topic/libraries/architecture/room.html "AutoValue Room integration blocker"

[Lombok]: https://projectlombok.org/features/Data "Lombok data annotation"

[Lombok issue]: https://github.com/googlesamples/android-architecture-components/issues/120 "Lombok Room integration blocker"

[Configuration chages]: https://developer.android.com/guide/topics/resources/runtime-changes.html "When activity is killed and recreated"

[IntentService-vs-AsyncTask 1]: https://android.jlelse.eu/using-intentservice-vs-asynctask-in-android-2fec1b853ff4 "how to run background processes"

[IntentService-vs-AsyncTask 2]: https://medium.com/@skidanolegs/asynctask-vs-intentservice-1-example-without-code-5250bea6bdae "how to run background processes"

[AsyncTask problems]: http://bon-app-etit.blogspot.de/2013/04/the-dark-side-of-asynctask.html

[Loaders]: https://developer.android.com/guide/components/loaders.html

[Next Billion Users]: https://www.youtube.com/watch?v=70WqJxymPr8&list=PL5G-TQp5op5qVE2mKqFlBnvbwsWFEwkFs&index=8 "Online / Offline problem"

[UML component diagram of app]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/PlainRssReader-components.png "Apps components"

[UML use case diagram of app]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/PlainRssReader-UseCases.png "Apps use cases"

[UML class diagram of feed model]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/feed-model.png "Feeds model classes"

[list]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/news-list.png "List of feeds"
[item]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/news-item.png "Feed item"
[settings]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/settings.png "Settings"
[settings-url]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/settings-url.png "Set feed url"

[mockup-list]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/marvel-mockup-list.png "Mockup: List of feeds"
[mockup-item]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/marvel-mockup-item.png "Mockup: Feed item"
[mockup-settings]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/marvel-mockup-settings.png "Mockup: Settings"

[LifecycleAwareRssReader]: https://github.com/nikkijuk/LifecycleAwareRssReader

[PlainRssReader in YouTube]: https://youtu.be/tw-Dy23_uuU

[PlainRssReader Mp4]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/PlainRssReader.mp4

