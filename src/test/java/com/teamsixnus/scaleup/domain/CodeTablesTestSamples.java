package com.teamsixnus.scaleup.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CodeTablesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CodeTables getCodeTablesSample1() {
        return new CodeTables()
            .id(1L)
            .category("category1")
            .codeKey("codeKey1")
            .codeValue("codeValue1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static CodeTables getCodeTablesSample2() {
        return new CodeTables()
            .id(2L)
            .category("category2")
            .codeKey("codeKey2")
            .codeValue("codeValue2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static CodeTables getCodeTablesRandomSampleGenerator() {
        return new CodeTables()
            .id(longCount.incrementAndGet())
            .category(UUID.randomUUID().toString())
            .codeKey(UUID.randomUUID().toString())
            .codeValue(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
