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

## Quick Example

This module can be **auto-discovered** by Discord4J if it's on the classpath. Refer to this example for customization:

```java
final DiscordClient client = new DiscordClientBuilder("token")
        .setStoreService(new CaffeineStoreService(builder -> builder.maximumSize(10_000)
                                                                        .expireAfterWrite(5, TimeUnit.MINUTES)
                                                                        .refreshAfterWrite(1, TimeUnit.MINUTES)))
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
