# spark-typify  <img src="https://travis-ci.org/manusant/spark-typify.svg?branch=master"></img>
A collection of Spark route extensions designed to provide marshalling/unmarshalling of *JSON* to/from Java *POJO*.

## Examples
### Handle request and transform Response to Json
```java
    .get("/", new GsonRoute() {
          @Override
          public Object handleAndTransform(Request request, Response response) {
              return ok(response, cardService.findAll());
          }
      })
```
### Handle request, transform request into a Java POJO and transform Response to Json
```java
     .put("/", new TypedGsonRoute<Card, Card>() {
            @Override
            public Card handleAndTransform(Card body, Request request, Response response) {
                return ok(response, cardService.update(11, body));
            }
        })
```

## TODO
Add support for XML marshalling/unmarshalling
