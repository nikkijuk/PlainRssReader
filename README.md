# PlainRssReader

PlainRssReader is very very simple implementation which reads rss files, 
converts them to json using web api and allows browsing thru simple list

# Background

Implementation has been created as part of Coursera Android Proramming moocs Capstone Project

https://www.coursera.org/learn/aadcapstone/home/welcome

# Documentation

I have prepared README.md to explains application at a high level. 

Readme contains references to further architecture, design and implementation artifacts like mockups, uml diagrams, screenshots, etc.

# Repository

Intead of having private GitLab repository as Mooc suggested I've decided to go with publig github account to share my experience with larger community.

Repositorys docs directory contains resources which accompany this documentation, otherwise repository contains solely application artifacts.

# Requirements 

Requirements for projects are 

- Interact with at least one remotely-hosted web service over the network via HTTP.
- Allow users to navigate between at least two different user interface screens at runtime.

Example given was 

a hypothetical RSS/Atom reader app might have multiple screens, such as

* a ListView showing all RSS Feed Stories,
* a detail View showing a single Feed Story, and
* a Settings view for configuring information about the App’s settings.
  - use the Junit testing framework to implement unit and integration tests for their application.
  - tests must cover the functionality of the app. 
  - Support persistent storage of information in an app’s content provider.
* Have well documented source code and a short video that shows how your app works when it's run.

# Architecture 

"[Software architecture] is those decisions which are both important and hard to change." - Martin Fowler.

I have decided to start with simple architecure with more or less vanilla android components and API-level 16.

I'll try later to expand application with selected [Android architecture components], escpecially [Room] for persistence.

# Design

# Classes

# Mockups

Mockups were prepared with [marvel app]

List of items

![mockup-list]

Selected item

![mockup-item]

settings

![mockup-settings]


# Implementation

Rss feed reading is proxied thru [rss2json] service, which converts feed to json on the fly.

When no feed is defined [Default feed] is used.

Browsing items is implemented using recycler view as defined in [use recycler-view]

Storing and changing settings is implemented using PreferefencesFragment as defined in [use preferences]

Due to limiations and interoperability issues with [Room] annotation processors [AutoValue] and [Lombok] aren't used to reduce boilerplate code, see [AutoValue issue] and [Room issue] for deeper discussion.

# Missing features

No persistence implemented
- Feed isn't saved locally, but is always retrieved from server

No junit tests for functionality

# Known bugs

User given url is not checked, and when trying to use wrong url during startup app will crash. Sorry. No safety net there.

# Snapshots of current implementation

List of items

![list]

Selected item

![item]

settings

![settings]

change url dialog

![settings-url]

Helpful articles

[use recycler-view]: https://willowtreeapps.com/ideas/android-fundamentals-working-with-the-recyclerview-adapter-and-viewholder-pattern/ "how to use recycler view, adapter and holder"

[use preferences]: http://www.cs.dartmouth.edu/~campbell/cs65/lecture12/lecture12.html "how to use preferences fragment"

[Default feed]: http://rss.nytimes.com/services/xml/rss/nyt/Science.xml "Ny Times science feed"

[rss2json]: https://rss2json.com "rss xml to json converter"

[marvel app]: https://marvelapp.com/ "Mockups for iOs, Android, Web, etc."

[Software architecture]: https://kylecordes.com/2015/fowler-software-architecture

[Android architecture components]: https://developer.android.com/topic/libraries/architecture/index.html "Android architecure components by Google"

[Room]: https://developer.android.com/topic/libraries/architecture/room.html "Room persistence library"

[AutoValue]: https://github.com/google/auto/tree/master/value "AutoValue for generation of Value objects

[AutoValue issue]: https://developer.android.com/topic/libraries/architecture/room.html "AutoValue Room integration blocker"

[Lombok]: https://projectlombok.org/features/Data "Lombok data annotation"

[Lombok issue]: https://github.com/googlesamples/android-architecture-components/issues/120 "Lombok Room integration blocker"

[list]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/news-list.png "List of feeds"
[item]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/news-item.png "Feed item"
[settings]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/settings.png "Settings"
[settings-url]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/settings-url.png "Set feed url"

[mockup-list]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/marvel-mockup-list.png "Mockup: List of feeds"
[mockup-item]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/marvel-mockup-item.png "Mockup: Feed item"
[mockup-settings]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/marvel-mockup-settings.png "Mockup: Settings"
