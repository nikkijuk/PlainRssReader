# PlainRssReader

PlainRssReader is very very simple implementation which reads rss files, 
converts them to json using web api and allows browsing thru simple list

# Background

Implementation has been created as part of Coursera Android Proramming moocs Capstone Project

https://www.coursera.org/learn/aadcapstone/home/welcome

# Documentation

I have prepared README.md to explain application at a high level. Readme contains references to further architecture, design and implementation artifacts like mockups, uml diagrams, screenshots, etc.

# Repository

Instead of having private GitLab repository as Mooc suggested I've decided to go with publig github account to share my experience with larger community.

Repositorys docs directory contains resources which accompany this documentation, otherwise repository contains solely application artifacts.

# Requirements 

Requirements for projects are 

```
- Interact with at least one remotely-hosted web service over the network via HTTP.
- Allow users to navigate between at least two different user interface screens at runtime.
```

Example given was 

a hypothetical RSS/Atom reader app might have multiple screens, such as

```
* a ListView showing all RSS Feed Stories,
* a detail View showing a single Feed Story, and
* a Settings view for configuring information about the App’s settings.
  - use the Junit testing framework to implement unit and integration tests for their application.
  - tests must cover the functionality of the app. 
  - Support persistent storage of information in an app’s content provider.
* Have well documented source code and a short video that shows how your app works when it's run.
```

As I've wanted to test technical abilities more than implement novel solutions I've decided to implement stripped down RSS reader as suggested.

# Architecture 

"[Software architecture] is those decisions which are both important and hard to change." - Martin Fowler.

Development is done using [Android Studio], which is currently at 3.0 RC1. RC is used 'cos it has [Java 8 support] and lambdas are pretty nice.

I have decided to start with simple architecure with more or less vanilla android components working on [API-level 16] or [API-level 19]. Java 8 Streams are not used, since they doesn't exist before API-level 24. [Android devices] with API-level 24 are still not commonly in used.

XML to Json conversion is done using [rss2json] service. [Gson] is used to marshall returned JSON to Plain Java Pojos.

I'll try later to expand application with selected [Android architecture components], escpecially [Room] for persistence. 

I'm considering to use
- Dagger 2
- Retrofit
- Android annotations

But it will be of highest priority to keep code working, not to polish it with unnecessary features and add code and libraries without real benefits. For this reason Guava is not yet in use, even if I see it as useful tool.

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

It's tempting to think that there's a way to know all details in advance and one could start work once plan in finished. I have taken different stance. Design evolves as I have more information, knowledge or time, and implementation follows design immendiately. Some call this iterative software development.

# Design Decisions

Requirements of Mooc dictate how app should be implemented by stating:

```
Comprise at least one instance each of the following fundamental Android component categories covered in the four previous Content MOOCs in the AAD Specialization:
- Activity
- BroadcastReceiver
- Service (Bound or Started)
- ContentProvider.
```

Content providers won't be used, as stated on [Sample content provider] code, "ou don't need to implement a ContentProvider unless you want to expose the data outside your process or your application already uses a ContentProvider.". Implementing content provider would add complexity without any gains, so it's easier, faster and better to use Sql-Lite directly or using [Room].

Needless to say, that same applies to BroadcastReveiver and Service components, if there's no real need to use them. 

BroadcastReceivers could be possibly used to listen network state, so that calls external services would be only allowed when network is present and status of network would be updated based on system notifications. This needs still to be investigated.

Services are most propably not needed. App doesn't expose services to other applications, and thus there's no need to separater processes which could be used from external apps. Neither calling possibly unreliable http api's do not need services, as simply async task does the trick, and allows calling http endpoint outside of UI thread. 

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

Due to limiations and interoperability issues with [Room] annotation processors [AutoValue] and [Lombok] aren't used to reduce boilerplate code of model classes, see [AutoValue issue] and [Room issue] for deeper discussion.

# Snapshots of current implementation

List of items

![list]

Selected item

![item]

settings

![settings]

change url dialog

![settings-url]

# Missing features

No persistence implemented
- Feed isn't saved locally, but is always retrieved from server

No junit tests for real functionality
- Only single dummy test present

# Known bugs

User given url is not checked, and when trying to use wrong url during startup app will crash. Sorry. No safety net there. 
- This bug is pretty annoying, since after giving false URL one needs to manually clear setting of App to get it starting again.

[yEd]: https://www.yworks.com/products/yed "yEd diagramming software"

[use recycler-view]: https://willowtreeapps.com/ideas/android-fundamentals-working-with-the-recyclerview-adapter-and-viewholder-pattern/ "how to use recycler view, adapter and holder"

[use preferences]: http://www.cs.dartmouth.edu/~campbell/cs65/lecture12/lecture12.html "how to use preferences fragment"

[Android studio]: https://developer.android.com/studio/preview/index.html "Android studio 3.0 RC1"

[Java 8 support]: https://developer.android.com/studio/write/java8-support.html "Android studio java 8 support"

[API-level 16]: https://developer.android.com/about/versions/android-4.1.html "Android 4.1 / Api-level 16"

[API-level 19]: https://developer.android.com/about/versions/android-4.4.html "Android 4.4 / Api-level 19"

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

[Room]: https://developer.android.com/topic/libraries/architecture/room.html "Room persistence library"

[Sample content provider]: https://github.com/googlesamples/android-architecture-components/blob/master/PersistenceContentProviderSample/app/src/main/java/com/example/android/contentprovidersample/provider/SampleContentProvider.java "Sample content provider"

[GSON]: https://github.com/google/gson "Googles serialization library"

[AutoValue]: https://github.com/google/auto/tree/master/value "AutoValue for generation of Value objects"

[AutoValue issue]: https://developer.android.com/topic/libraries/architecture/room.html "AutoValue Room integration blocker"

[Lombok]: https://projectlombok.org/features/Data "Lombok data annotation"

[Lombok issue]: https://github.com/googlesamples/android-architecture-components/issues/120 "Lombok Room integration blocker"

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
