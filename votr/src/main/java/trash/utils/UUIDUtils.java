package trash.utils;

import java.util.UUID;

public class UUIDUtils {

    public static String createUUID(String... values) {
        return UUID.nameUUIDFromBytes(String.join("-", values).getBytes()).toString();
    }
}
