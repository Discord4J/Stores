# Stores-Chronicle

A store implementation backed by [Chronicle Map](https://github.com/OpenHFT/Chronicle-Map), replicate your Key Value Store across your network, with consistency, persistence and performance.

## Installation
### Gradle
```groovy
repositories {
  mavenCentral()
}

dependencies {
  implementation 'com.discord4j:stores-chronicle:1.0.0'
}
```
### Maven
```xml
<dependencies>
  <dependency>
    <groupId>com.discord4j</groupId>
    <artifactId>stores-chronicle</artifactId>
    <version>1.0.0</version>
  </dependency>
</dependencies>
```

### SBT
```scala
libraryDependencies ++= Seq(
  "com.discord4j" % "stores-chronicle" % "1.0.0"
)
```

## Quick Example

This module can be **auto-discovered** by Discord4J if it's on the classpath. Refer to this example for customization:

```java
final DiscordClient client = new DiscordClientBuilder("token")
        .setStoreService(new ChronicleStoreService(false))
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
