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
package discord4j.store.api.util;

import discord4j.store.api.service.StoreService;

import java.util.Map;

/**
 * This is a simple context object, containing various information about the environment the store is being
 * invoked in.
 */
public class StoreContext {

    private final Map<String, Object> hints;

    public StoreContext(Map<String, Object> hints) {
        this.hints = hints;
    }

    /**
     * Return the map of hints provided as context so {@link StoreService} implementations can use it in their
     * {@link StoreService#init(StoreContext)} method.
     *
     * @return a map for context
     */
    public Map<String, Object> getHints() {
        return hints;
    }
}
