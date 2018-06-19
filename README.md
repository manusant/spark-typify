# spark-typify  <img src="https://travis-ci.org/manusant/spark-typify.svg?branch=master"></img>
A collection of Spark route extensions designed to provide marshalling/unmarshalling of *JSON* to/from Java *POJO*.

## Examples
### Handle request and transform Response to Json
```java
    .get("/", new Route() {
          @Xml
          public Object onRequest(Request request, Response response) {
              return ok(response, cardService.findAll());
          }
      })
```
### Handle request, transform request into a Java POJO and transform Response to Json
```java
     .put("/", new TypedRoute<Card, Card>() {
            @Json
            public Card onRequest(Card body, Request request, Response response) {
                return ok(response, cardService.update(11, body));
            }
        })
```
## Specify Ignore/Exclude spec
```java
    TypifyProvider.setUp(IgnoreSpec.newBuilder()
               .withIgnoreAnnotated(JsonIgnore.class)
               .withIgnoreTypes(First.class,Second.class)
               ::build);
```
## TODO
Add support for XML marshalling/unmarshalling

## Add to your project
### Gradle
Add this entry to your *build.gradle* file
```groovy
 repositories {
    maven {
        url "https://packagecloud.io/manusant/beerRepo/maven2"
    }
}
```
And add the dependency
```groovy
  compile 'com.beerboy.spark:spark-typify:1.0.0.3'
```
### Maven
Add this to *dependencyManagement* section of your *pom.xml* 
```xml
<repositories>
  <repository>
    <id>manusant-beerRepo</id>
    <url>https://packagecloud.io/manusant/beerRepo/maven2</url>
    <releases>
      <enabled>true</enabled>
    </releases>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>
```
And add the dependency
```xml
 <dependency>
  <groupId>com.beerboy.spark</groupId>
  <artifactId>spark-typify</artifactId>
  <version>1.0.0.3</version>
 </dependency>
```
