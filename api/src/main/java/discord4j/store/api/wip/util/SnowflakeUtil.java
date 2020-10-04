package discord4j.store.api.wip.util;

public class SnowflakeUtil {

    private SnowflakeUtil() {
        throw new AssertionError();
    }

    /**
     * Converts a Discord snowflake utilizing an <i>unsigned</i> ID from a String to a long.
     *
     * @param id The String representing the <i>unsigned</i> ID of a snowflake. Must be non-null.
     * @return A long representing the <i>unsigned</i> ID.
     * @throws NumberFormatException If {@code id} is not an <i>unsigned</i> ID.
     */
    public static long asLong(final String id) {
        return Long.parseUnsignedLong(id);
    }
}
