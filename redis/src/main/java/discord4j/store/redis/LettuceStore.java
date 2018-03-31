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

import discord4j.store.Store;
import discord4j.store.common.RSA;
import discord4j.store.util.WithinRangePredicate;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.codec.RedisCodec;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;
import reactor.util.function.Tuple2;

import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.Map;

public class LettuceStore<K extends Comparable<K>, V extends Serializable> implements Store<K, V> {

    private static final Logger log = Loggers.getLogger(LettuceStore.class);

    private final RedisReactiveCommands<Object, Object> commands;
    private final String storeName;

    public LettuceStore(RedisClient client, String storeName) {
        String disableCrypto = System.getenv("D4J_CRYPTO_DISABLE");
        RedisCodec<Object, Object> codec = new SerializedObjectCodec();
        if (!Boolean.parseBoolean(disableCrypto)) {
            String privateKeyPath = System.getenv("D4J_CRYPTO_PRIVATE_KEY_PATH");
            String publicKeyPath = System.getenv("D4J_CRYPTO_PUBLIC_KEY_PATH");
            try {
                codec = new SecureSerializedObjectCodec(
                        RSA.getPrivateKey(privateKeyPath),
                        RSA.getPublicKey(publicKeyPath));
            } catch (IOException | GeneralSecurityException e) {
                log.warn("Unable to instantiate secure keys", e);
            }
        }
        this.commands = client.connect(codec).reactive();
        this.storeName = storeName;
    }

    private Object createKey(K key) {
        return key.toString();
    }

    @SuppressWarnings("unchecked")
    private static <V> V cast(Object o) {
        return (V) o;
    }

    @Override
    public Mono<Void> save(K key, V value) {
        return Mono.defer(() -> commands.hset(storeName, createKey(key), value).then());
    }

    @Override
    public Mono<Void> save(Iterable<Tuple2<K, V>> entries) {
        return Flux.fromIterable(entries).flatMap(tuple -> save(tuple.getT1(), tuple.getT2())).then();
    }

    @Override
    public Mono<Void> save(Publisher<Tuple2<K, V>> entryStream) {
        return Flux.from(entryStream).flatMap(tuple -> save(tuple.getT1(), tuple.getT2())).then();
    }

    @Override
    public Mono<V> find(K id) {
        return Mono.defer(() -> commands.hget(storeName, createKey(id)).map(LettuceStore::cast));
    }

    @Override
    public Mono<Boolean> exists(K id) {
        return find(id).map(v -> true).switchIfEmpty(Mono.just(false));
    }

    @Override
    public Mono<Boolean> exists(Publisher<K> ids) {
        return Flux.from(ids).flatMap(this::find).all(v -> true);
    }

    @Override
    public Flux<V> findAll() {
        return Flux.defer(() -> commands.hvals(storeName).map(LettuceStore::cast));
    }

    @Override
    public Flux<V> findAll(Iterable<K> ids) {
        return Flux.defer(() -> Flux.fromIterable(ids)).map(this::find).map(LettuceStore::cast);
    }

    @Override
    public Flux<V> findAll(Publisher<K> ids) {
        return Flux.defer(() -> Flux.from(ids)).map(this::find).map(LettuceStore::cast);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Flux<V> findInRange(K start, K end) {
        WithinRangePredicate<K> predicate = new WithinRangePredicate<>(start, end);
        return Flux.defer(() -> commands.hgetall(storeName))
                .flatMap(map -> Flux.fromIterable(map.entrySet()))
                .filter(entry -> predicate.test((K) entry.getKey()))
                .map(Map.Entry::getValue)
                .map(LettuceStore::cast);
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
    public Mono<Void> delete(Tuple2<K, V> entry) {
        return Mono.defer(() -> find(entry.getT1())
                .flatMap(v -> {
                    if (v.equals(entry.getT2())) {
                        return Mono.defer(() -> commands.hdel(storeName, createKey(entry.getT1()))).then();
                    } else {
                        return Mono.empty();
                    }
                }));
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
    public Mono<Void> deleteAll(Iterable<Tuple2<K, V>> entries) {
        return Flux.fromIterable(entries).flatMap(this::delete).then();
    }

    @Override
    public Mono<Void> deleteAll(Publisher<Tuple2<K, V>> entries) {
        return Flux.from(entries).flatMap(this::delete).then();
    }

    @Override
    public Mono<Void> deleteAll() {
        return Mono.defer(() -> commands.del(storeName).then());
    }

    @Override
    public Flux<K> keys() {
        return Flux.defer(() -> commands.hkeys(storeName)).map(LettuceStore::cast);
    }

    @Override
    public Flux<V> values() {
        return Flux.defer(() -> commands.hvals(storeName)).map(LettuceStore::cast);
    }
}
