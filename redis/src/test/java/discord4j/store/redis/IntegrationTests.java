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

import discord4j.store.api.service.StoreService;
import discord4j.store.tck.StoreVerification;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import org.junit.Before;
import org.junit.Rule;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class IntegrationTests extends StoreVerification {

    private RedisStoreService service;

    @Rule
    public GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7.2.5-alpine"))
            .withExposedPorts(6379);

    @Before
    public void setUp() {
        service = RedisStoreService.builder()
                .redisClient(RedisClient.create(RedisURI.builder()
                        .withHost(redis.getHost())
                        .withPort(redis.getFirstMappedPort())
                        .build()))
                .build();
    }

    @Override
    public StoreService getStoreService() {
        return service;
    }
}
