# message-parser
Atlassian Coding Exercise

## Single Request branch

**I figured I could do better if I had an API that would take an array of URLs as an input and return their titles in a single HTTP call, which is what I would expect from a professional application. Since my js skills are a bit rusty I decided to try this on a separate branch.
Just for fun, I put together a simple endpoint using Firebase Functions and decided to give it a go :)**

##Challenge

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


