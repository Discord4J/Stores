# Stores-Redis

A store implementation backed by [Lettuce](https://lettuce.io/), advanced Java Redis client for thread-safe sync, async, and reactive usage.

Dependencies:

* [discord-json](https://github.com/Discord4J/discord-json)

## Installation
Replace VERSION with the following: [![Maven Central](https://img.shields.io/maven-central/v/com.discord4j/stores-redis/3.1.svg?style=flat-square)](https://search.maven.org/artifact/com.discord4j/stores-redis)
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

This module can be **auto-discovered** by Discord4J if it's on the classpath.

```java
DiscordClientBuilder.create(System.getenv("token"))
        .build()
        .gateway()
        .setStoreService(RedisStoreService.builder().build()) // connects to localhost:6379, configure the builder to change
        .withGateway(client -> client.on(ReadyEvent.class)
            .doOnNext(ready -> log.info("Logged in as {}", ready.getSelf().getUsername()))
            .then())
        .block();
```
