package io.cucaracha;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Cucaracha - micro Key-Value Database in 200 rows of code
 *
 * @author mhoms 2018, 2019, 2022, 2024
 */
class CucarachaMicroDbTest {

    public static final String TEST_PROPERTIES = "test.properties";

    final File file = new File(TEST_PROPERTIES);

    @BeforeEach
    void beforeTest() {
        file.delete();
    }

    @Test
    void begins_without_file() {
        assertThat(file.exists()).isFalse();
    }

    @Test
    void file_not_exists_and_cucaracha_creates() {

        assertThat(file.exists()).isFalse();

        new CucarachaMicroDb(file);

        assertThat(file.exists()).isTrue();
    }

    @Test
    void file_exists_and_is_not_overwrited() {

        assertThat(file.exists()).isFalse();

        {
            var db = new CucarachaMicroDb(file);
            db.beginTransaction();
            db.put("jou", "juas");
            db.commit();
            assertThat(file.exists()).isTrue();
        }

        // Act
        new CucarachaMicroDb(file);

        var db = new CucarachaMicroDb(file);
        db.beginTransaction();
        assertThat(db.get("jou")).isEqualTo("juas");
        db.rollback();
    }

    @Test
    void cucaracha_removes_file() {
        assertThat(file.exists()).isFalse();
        var db = new CucarachaMicroDb(file);
        assertThat(file.exists()).isTrue();

        // Act
        db.removeDatabase();

        assertThat(file.exists()).isFalse();
    }

    @Test
    void basic_store_and_retrieval() {
        var db = new CucarachaMicroDb(file);

        db.beginTransaction();
        db.put("one", "1");
        db.put("two", "2");
        db.put("three.3", "333");
        db.commit();

        db.beginTransaction();
        assertThat(db.exist("one")).isTrue();
        assertThat(db.exist("fifteen")).isFalse();
        assertThat(db.get("two")).isEqualTo("2");
        assertThat(db.find((key) -> key.startsWith("three."), (value) -> true)).hasToString("{three.3=333}");
        db.rollback();
    }

    @Test
    void exist() {
        var db = new CucarachaMicroDb(file);

        // not in active transaction
        assertThrows(RuntimeException.class, () -> db.exist("liulu"));

        db.beginTransaction();
        db.put("one", "1");
        db.commit();

        db.beginTransaction();
        assertThat(db.exist("one")).isTrue();
        assertThat(db.exist("oneJHGLKJ")).isFalse();
        db.rollback();
    }

    @Test
    void get_should_fail_when_out_of_transaction() {
        var db = new CucarachaMicroDb(file);

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> db.get("kjh")).withMessage("not in active transaction: " + TEST_PROPERTIES);
    }

    @Test
    void get_should_fail_when_not_found() {
        var db = new CucarachaMicroDb(file);

        db.beginTransaction();
        try {
            assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> db.get("kjh")).withMessage("not found: kjh; " + TEST_PROPERTIES);
        } finally {
            db.rollback();
        }
    }

    @Test
    void get_should_work_when_property_exists() {
        var db = new CucarachaMicroDb(file);

        db.beginTransaction();
        db.put("jou", "123");
        // Act
        assertThat(db.get("jou")).isEqualTo("123");
        db.commit();

        db.beginTransaction();
        // Act
        assertThat(db.get("jou")).isEqualTo("123");
        db.rollback();
    }

    @Test
    void find_should_fail_when_out_of_transaction() {
        var db = new CucarachaMicroDb(file);

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> db.find(x -> true, x -> true)).withMessage("not in active transaction: " + TEST_PROPERTIES);
    }

    @Test
    void find() {
        var db = new CucarachaMicroDb(file);
        db.executeInTransaction(() -> {
            db.put("a.b.c", "1");
            db.put("a.b.d", "2");
            db.put("a.b.e", "3");
        });

        db.executeInTransactionAsReadOnly(() ->
                assertThat(db.find(k -> k.startsWith("a.b"), v -> v.equals("2"))).hasToString("{a.b.d=2}")
        );

        db.executeInTransactionAsReadOnly(() ->
                assertThat(db.find(k -> k.startsWith("a.b"), v -> true)).hasToString("{a.b.e=3, a.b.d=2, a.b.c=1}")
        );

        db.executeInTransactionAsReadOnly(() ->
                assertThat(db.find(k -> true, v -> v.equals("2"))).hasToString("{a.b.d=2}")
        );
    }

    @Test
    void sequence_happy_path() {
        var db = new CucarachaMicroDb(file);
        db.executeInTransaction(() -> {
            assertThat(db.getSequenceCurrValue("jou")).isEqualTo(-1);
            assertThat(db.getSequenceNextValue("jou")).isEqualTo(0);
            assertThat(db.getSequenceNextValue("jou")).isEqualTo(1);
            assertThat(db.getSequenceNextValue("jou")).isEqualTo(2);
            assertThat(db.get("__seq.jou")).isEqualTo("2");
        });
        db.executeInTransactionAsReadOnly(() -> {
            assertThat(db.getSequenceCurrValue("jou")).isEqualTo(2);
            assertThat(db.getSequenceNextValue("jou")).isEqualTo(3);
        });
        db.executeInTransaction(() -> {
            assertThat(db.getSequenceCurrValue("jou")).isEqualTo(2);
        });
        db.executeInTransaction(() -> {
            assertThat(db.getSequenceNextValue("jou")).isEqualTo(3);
        });
        db.executeInTransaction(() -> {
            db.removeSequence("jou");
            assertThat(db.getSequenceCurrValue("jou")).isEqualTo(-1);
            assertThat(db.getSequenceNextValue("jou")).isEqualTo(0);
            assertThat(db.getSequenceNextValue("jou")).isEqualTo(1);

            assertThat(db.getSequenceCurrValue("jou")).isEqualTo(1);
            assertThat(db.getSequenceCurrValue("jou")).isEqualTo(1);

            assertThat(db.getSequenceNextValue("jou")).isEqualTo(2);
            assertThat(db.get("__seq.jou")).isEqualTo("2");
        });
    }
}