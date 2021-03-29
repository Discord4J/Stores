# Discord4J Store
This module contains common interfaces for implementing a reactive caching system for use in the core module.

Provided `StoreService` implementations:
* **MappingStoreService** to use multiple `StoreService` implementations depending on the stored value.
* **NoOpStoreService** to disable the backing store.
* **ForwardingStoreService** to delegate all store operations to an underlying `Store`.
* **ReadOnlyStoreService** to decorate an existing `Store` in order to filter out write operations.

## Usage under Discord4J 3.2.0-M2 and higher

`StoreService` instances are considered "legacy store layouts" starting from 3.2.0-M2 and need to be configured
accordingly. If before you would set a service using:

```java
DiscordClientBuilder.create(System.getenv("token"))
        .build()
        .gateway()
        .setStoreService(storeService)
        .withGateway(client -> client.on(ReadyEvent.class)
            .doOnNext(ready -> log.info("Logged in as {}", ready.getSelf().getUsername()))
            .then())
        .block();
```

Now you would need to wrap this service:

```java
DiscordClientBuilder.create(System.getenv("token"))
        .build()
        .gateway()
        .setStore(Store.fromLayout(LegacyStoreLayout.of(storeService)))
        .withGateway(client -> client.on(ReadyEvent.class)
            .doOnNext(ready -> log.info("Logged in as {}", ready.getSelf().getUsername()))
            .then())
        .block();
```
