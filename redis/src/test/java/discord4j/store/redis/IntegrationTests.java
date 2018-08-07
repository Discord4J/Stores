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
import org.junit.Before;
import org.junit.BeforeClass;
import redis.embedded.RedisServer;

import java.io.IOException;

public class IntegrationTests extends StoreVerification {

    private RedisStoreService service;

    @BeforeClass
    public static void setUpTests() throws IOException {
        RedisServer redisServer = new RedisServer(6379);
        redisServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(redisServer::stop));
    }

    @Before
    public void setUp() {
        service = new RedisStoreService();
    }

    @Override
    public StoreService getStoreService() {
        return service;
    }
}
