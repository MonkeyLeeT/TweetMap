TweetMap
==============
##Overview
TweetMap is an app that shows the coordinates of Tweets and the heatmap of them. It feeds user using both data from database and real-time streamed data.

Demo: <To be a url>

##Usage
To discuss how to test and use this app.

## Design
### Tweets Collection
com.twitter.hbc library is used to collected streamed tweets from Twitter Streaming API.

### Tweets Database
RDS is serving as the database for storing up to 100 MB of tweets.

### Streaming Server
Websocket is used to make the magic and the streaming logic can be found in another project.

### Front End
Google Map API, Bootstrap, jQuery, Websocket.

