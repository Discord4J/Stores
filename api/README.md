# Discord4J Store
This module contains common interfaces for implementing a reactive caching system for use in the core module.

Provided `StoreService` implementations:
* **MappingStoreService** to use multiple `StoreService` implementations depending on the stored value.
* **NoOpStoreService** to disable the backing store.
* **ForwardingStoreService** to delegate all store operations to an underlying `Store`.
* **ReadOnlyStoreService** to decorate an existing `Store` in order to filter out write operations.
