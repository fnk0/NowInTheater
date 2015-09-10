## Coding the Data API

We are going to be using a library called Retrofit for the network calls. Retrofit is probably the most used Android libraries.

To read more about Retrofit [click here](http://square.github.io/retrofit/)

The basic concept is that when we make a network call a giant blob of JSON our XML is returned. And the task of parsing that data is often tedious and repetitive.
Retrofit lets us create POJO's (Plain Old Java Objects) and specify then as a result from our Network calls and parsing them automatically for us.
By default it uses Gson but we can define any mechanism for parsing the data as we wish. Such as Jackson Json/XML or any other one written by us If our data is not a common format.

The basic idea is:
