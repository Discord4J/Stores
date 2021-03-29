# Stores-Caffeine

A store implementation backed by [Caffeine](https://github.com/ben-manes/caffeine), a high performance caching library for Java 8.

## Installation
Replace VERSION with the following: [![Maven Central](https://img.shields.io/maven-central/v/com.discord4j/stores-caffeine.svg?style=flat-square)](https://search.maven.org/artifact/com.discord4j/stores-caffeine)
### Gradle
```groovy
repositories {
  mavenCentral()
}

dependencies {
  implementation 'com.discord4j:stores-caffeine:VERSION'
}
```
### Maven
```xml
<dependencies>
  <dependency>
    <groupId>com.discord4j</groupId>
    <artifactId>stores-caffeine</artifactId>
    <version>VERSION</version>
  </dependency>
</dependencies>
```

### SBT
```scala
libraryDependencies ++= Seq(
  "com.discord4j" % "stores-caffeine" % "VERSION"
)
```

## Usage under Discord4J 3.2.0-M2 and higher

`CaffeineStoreService` is considered a legacy store layout and must be configured accordingly at `setStore`:

```java
DiscordClientBuilder.create(System.getenv("token"))
        .build()
        .gateway()
        .setStore(Store.fromLayout(LegacyStoreLayout.of(new CaffeineStoreService(builder -> builder.maximumSize(10_000)
                                                                    .expireAfterWrite(5, TimeUnit.MINUTES)
                                                                    .refreshAfterWrite(1, TimeUnit.MINUTES)))))
        .withGateway(client -> client.on(ReadyEvent.class)
            .doOnNext(ready -> log.info("Logged in as {}", ready.getSelf().getUsername()))
            .then())
        .block();
```
