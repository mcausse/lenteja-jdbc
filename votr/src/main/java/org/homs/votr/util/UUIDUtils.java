package org.homs.votr.util;

import java.util.UUID;

public class UUIDUtils {

    public String createUUID(String... values) {
        return UUID.nameUUIDFromBytes(String.join("-", values).getBytes()).toString();
    }
}
