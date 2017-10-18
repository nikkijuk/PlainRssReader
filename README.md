# PlainRssReader

PlainRssReader is very very simple implementation which reads rss files, 
converts them to json using web api and allows browsing thru simple list

# Background

Implementation has been created as part of Coursera Android Proramming moocs Capstone Project

https://www.coursera.org/learn/aadcapstone/home/welcome

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

# Mockups

Mockups were prepared with [marvel app]

List of items

![mockup-list]

Selected item

![mockup-item]

settings

![mockup-settings]

# Architecture 

"[Software architecture] is those decisions which are both important and hard to change." - Martin Fowler


# Implementation

Browsing items is implemented using recycler view as defined in [use recycler-view]

Storing and changing settings is implemented using PreferefencesFragment as defined in [use preferences]

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

[marvel app]: https://marvelapp.com/ "Mockups for iOs, Android, Web, etc."

[Software architecture]: https://kylecordes.com/2015/fowler-software-architecture

[list]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/news-list.png "List of feeds"
[item]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/news-item.png "Feed item"
[settings]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/settings.png "Settings"
[settings-url]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/settings-url.png "Set feed url"

[mockup-list]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/marvel-mockup-list.png "Mockup: List of feeds"
[mockup-item]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/marvel-mockup-item.png "Mockup: Feed item"
[mockup-settings]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/marvel-mockup-settings.png "Mockup: Settings"
