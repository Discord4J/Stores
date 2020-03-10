/*
 * This file is part of Discord4J.
 *
 * Discord4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Discord4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Discord4J. If not, see <http://www.gnu.org/licenses/>.
 */

package discord4j.store.redis;

import discord4j.store.api.Store;
import discord4j.store.api.util.LongLongTuple2;
import discord4j.store.api.util.WithinRangePredicate;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.reactive.RedisClusterReactiveCommands;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Map;

public class ByteArrayRedisStore<K extends Comparable<K>, V> implements Store<K, V> {

    private final RedisClusterReactiveCommands<String, byte[]> commands;
    private final RedisSerializer<V> valueSerializer;
    private final String storeName;

    public ByteArrayRedisStore(StatefulRedisConnection<String, byte[]> connection,
                               RedisSerializer<V> valueSerializer, String storeName) {
        this.commands = connection.reactive();
        this.valueSerializer = valueSerializer;
        this.storeName = storeName;
    }

    public ByteArrayRedisStore(StatefulRedisClusterConnection<String, byte[]> connection,
                               RedisSerializer<V> valueSerializer, String storeName) {
        this.commands = connection.reactive();
        this.valueSerializer = valueSerializer;
        this.storeName = storeName;
    }

    private String createKey(K key) {
        if (key instanceof LongLongTuple2) {
            LongLongTuple2 tuple = (LongLongTuple2) key;
            return tuple.getT1() + ":" + tuple.getT2();
        }
        return key.toString();
    }

    @Override
    public Mono<Void> save(K key, V value) {
        return Mono.defer(() -> commands.hset(storeName, createKey(key), valueSerializer.serialize(value)).then());
    }

    @Override
    public Mono<Void> save(Publisher<Tuple2<K, V>> entryStream) {
        return Flux.from(entryStream).flatMap(tuple -> save(tuple.getT1(), tuple.getT2())).then();
    }

    @Override
    public Mono<V> find(K id) {
        return Mono.defer(() -> commands.hget(storeName, createKey(id)).map(valueSerializer::deserialize));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Flux<V> findInRange(K start, K end) {
        WithinRangePredicate<K> predicate = new WithinRangePredicate<>(start, end);
        return Flux.defer(() -> commands.hgetall(storeName))
                .flatMap(map -> Flux.fromIterable(map.entrySet()))
                .filter(entry -> predicate.test((K) entry.getKey()))
                .map(Map.Entry::getValue)
                .map(valueSerializer::deserialize);
    }

    @Override
    public Mono<Long> count() {
        return Mono.defer(() -> commands.hlen(storeName));
    }

    @Override
    public Mono<Void> delete(K id) {
        return Mono.defer(() -> commands.hdel(storeName, createKey(id)).then());
    }

    @Override
    public Mono<Void> delete(Publisher<K> ids) {
        return Mono.defer(() -> Flux.from(ids).flatMap(this::delete).then());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Void> deleteInRange(K start, K end) {
        WithinRangePredicate<K> predicate = new WithinRangePredicate<>(start, end);
        return Flux.defer(() -> commands.hkeys(storeName))
                .filter(key -> predicate.test((K) key))
                .flatMap(key -> Mono.defer(() -> commands.hdel(storeName, key).then()))
                .then();
    }

    @Override
    public Mono<Void> deleteAll() {
        return Mono.defer(() -> commands.del(storeName).then());
    }

    @Override
    public Flux<K> keys() {
        return Flux.defer(() -> commands.hkeys(storeName)).map(ByteArrayRedisStore::cast);
    }

    @Override
    public Flux<V> values() {
        return Flux.defer(() -> commands.hvals(storeName)).map(valueSerializer::deserialize);
    }

    @Override
    public Mono<Void> invalidate() {
        return deleteAll();
    }

    @SuppressWarnings("unchecked")
    private static <V> V cast(Object o) {
        return (V) o;
    }
}
