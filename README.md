# message-parser
## Atlassian Coding Exercise
**Note: The master branch uses the Google Custom Search API to individually fetch each url title on a different HTTP request, which is not optimal. Just for fun, I wanted to see if I could come up with an endpoint that would receive an array of URLs as input and return their titles in order. I implemented the endpoint using Firebase Functions, I can provide the js code for this but please don't judge me based on that ( ⚆ _ ⚆ )
As my Javascript skills a quite rusty and as I have already ran out of time for this challenge, I implemented this on a separate branch: @see single_request for more info**


## Challenge:

Please write a solution that takes a chat message string and returns a JSON string containing information about its contents. Special content to look for includes:

1. @mentions - A way to mention a user. Always starts with an '@' and ends when hitting a non-alphanumeric character. For example, "Hey @andy, are you free for lunch tomorrow?".

2. Emoticons - For this exercise, you only need to consider 'custom' emoticons which are alphanumeric strings, no longer than 15 characters, contained in parenthesis. You can assume that anything matching this format is an emoticon. (https://www.hipchat.com/emoticons)

3. Links - Any URLs contained in the message, along with the page's title.

For example, calling your function with the following inputs should result in the corresponding return values.

```Input: "@chris you around?"
Return (string):

{ "mentions": [ "chris" ] }

Input: "Good morning! (megusta) (coffee)"
Return (string):

{ "emoticons": [ "megusta", "coffee" ] }

Input: "Olympics are starting soon; http://www.nbcolympics.com"
Return (string):

{"links": [

{ "url": "http://www.nbcolympics.com", "title": "NBC Olympics | 2014 NBC Olympics in Sochi Russia" }

]
}

Input: "@bob @john (success) such a cool feature; https://twitter.com/jdorfman/status/430511497475670016"
Return (string):
{
"mentions": [
"bob",
"john"
],
"emoticons": [
"success"
],
"links": [

{ "url": "https://twitter.com/jdorfman/status/430511497475670016", "title": "Twitter / jdorfman: nice @littlebigdetail from ..." }

]
}


