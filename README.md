TweetMap
==============
##Overview
TweetMap is an app that shows the coordinates of Tweets and the heatmap of them. It feeds user using both data from database and real-time streamed data.

Demo: [TweetMap](http://tweetmapenviron-jrkpqgkewm.elasticbeanstalk.com/)

![alt text](https://github.com/MonkeyLeeT/TweetMap/blob/master/preview/preview1.png?raw=true)

##Usage

###General

A tweet timeline windown will keep feeding user with latest tweets streamed from Twitter regardless of modes.

###Normal Mode
1. You can view clustered tweets as scatter points or heat map.
2. You can click each marker in scatter view to see embedded tweets, and profit.
3. Slide the time range to specify your desired time interval of tweets.

###Real Time Mode
1. Click the real time mode button to enter.
2. Real time tweets will pop up all over the world!
3. Like scatter view, you can click each marker in the scatter view to see embedded tweets.


## Design
### Tweets Collection
com.twitter.hbc library is used to collect streamed tweets from Twitter Streaming API.

### Tweets Database
RDS is serving as the database for storing up to 100 MB of tweets.

### Server
WebSocket is used to make the magic and the streaming logic can be found in [**another project**](https://github.com/MonkeyLeeT/TweetServer).

### Front End
Google Map API, Bootstrap, jQuery(and some libraries), Websocket.

