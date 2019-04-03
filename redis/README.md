# Stores-Redis

A store implementation backed by [Lettuce](https://lettuce.io/), advanced Java Redis client for thread-safe sync, async, and reactive usage. Supports Cluster, Sentinel, Pipelining, and codecs.

Work in progress!

## Installation
Replace VERSION with the following: [![Maven Central](https://img.shields.io/maven-central/v/com.discord4j/stores-redis.svg?style=flat-square)](https://search.maven.org/artifact/com.discord4j/stores-redis)
### Gradle
```groovy
repositories {
  mavenCentral()
}

dependencies {
  implementation 'com.discord4j:stores-redis:VERSION'
}
```
### Maven
```xml
<dependencies>
  <dependency>
    <groupId>com.discord4j</groupId>
    <artifactId>stores-redis</artifactId>
    <version>VERSION</version>
  </dependency>
</dependencies>
```

### SBT
```scala
libraryDependencies ++= Seq(
  "com.discord4j" % "stores-redis" % "VERSION"
)
```

## Quick Example

This module can be **auto-discovered** by Discord4J if it's on the classpath. Refer to this example for customization:

```java
final DiscordClient client = new DiscordClientBuilder("token")
        .setStoreService(new RedisStoreService(RedisClient.create("redis://localhost")))
        .build();

client.getEventDispatcher().on(ReadyEvent.class)
        .subscribe(ready -> System.out.println("Logged in as " + ready.getSelf().getUsername()));

client.getEventDispatcher().on(MessageCreateEvent.class)
        .map(MessageCreateEvent::getMessage)
        .filter(msg -> msg.getContent().map("!ping"::equals).orElse(false))
        .flatMap(Message::getChannel)
        .flatMap(channel -> channel.createMessage("Pong!"))
        .subscribe();

client.login().block();
```
