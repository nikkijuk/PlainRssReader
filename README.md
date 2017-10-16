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

** use the Junit testing framework to implement unit and integration tests for their application.
** tests must cover the functionality of the app. 
** Support persistent storage of information in an app’s content provider.

* Have well documented source code and a short video that shows how your app works when it's run.

# Current state

Current implementation doesn't cover settings, but support will be added later
- Feed address is hardcoded

No persistence implemented
. Feed isn't saved locally, but is always retrieved from server

Snapshots: 

List of items

![list][list]

Selected item

![item][item]

[list]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/news-list.png "List of feeds"
[item]: https://github.com/nikkijuk/PlainRssReader/blob/master/docs/news-item.png "Feed item"


