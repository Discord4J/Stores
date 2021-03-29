# Stores-Redis

A store implementation backed by [Lettuce](https://lettuce.io/), advanced Java Redis client for thread-safe sync, async, and reactive usage.

Dependencies:

* [discord-json](https://github.com/Discord4J/discord-json)

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

## Usage under Discord4J 3.2.0-M2 and higher

`RedisStoreService` is considered a legacy store layout and must be configured accordingly at `setStore`:

```java
DiscordClientBuilder.create(System.getenv("token"))
        .build()
        .gateway()
        .setStore(Store.fromLayout(LegacyStoreLayout.of(RedisStoreService.builder().build()))) // connects to localhost:6379, configure the builder to change
        .withGateway(client -> client.on(ReadyEvent.class)
            .doOnNext(ready -> log.info("Logged in as {}", ready.getSelf().getUsername()))
            .then())
        .block();
```
