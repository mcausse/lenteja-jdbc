package io.martung;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class TryShould {

    void voidSqlOperation() throws SQLException {
    }

    void voidSqlOperationThatFails() throws SQLException {
        throw new SQLException("jou");
    }

    long sqlOperation() throws SQLException {
        return 123L;
    }

    long sqlOperationThatFails() throws SQLException {
        throw new SQLException("jou2");
    }

    @Test
    void void_success() {

        Result<Void> r = Try.forResult(this::voidSqlOperation);

        assertThat(r.isSuccess()).isTrue();
        assertThat(r.isFailed()).isFalse();
        assertThat(r.getSuccessResult()).isNull();
        assertThat(r.getFailureException()).isNull();
    }

    @Test
    void void_failure() {

        Result<Void> r = Try.forResult(this::voidSqlOperationThatFails);

        assertThat(r.isSuccess()).isFalse();
        assertThat(r.isFailed()).isTrue();
        assertThat(r.getSuccessResult()).isNull();
        assertThat(r.getFailureException()).isNotNull().isInstanceOf(SQLException.class).hasMessage("jou");
    }

    @Test
    void non_void_success() throws Exception {

        Result<Long> r = Try.forResult(this::sqlOperation);

        assertThat(r.isSuccess()).isTrue();
        assertThat(r.isFailed()).isFalse();
        assertThat(r.getSuccessResult()).isEqualTo(123L);
        assertThat(r.getFailureException()).isNull();

        assertThat(r.getOrElseThrow()).isEqualTo(123L);
        assertThat(r.getOrElseThrow(e -> null)).isEqualTo(123L);
    }

    @Test
    void non_void_failure() {

        Result<Long> r = Try.forResult(this::sqlOperationThatFails);

        assertThat(r.isSuccess()).isFalse();
        assertThat(r.isFailed()).isTrue();
        assertThat(r.getSuccessResult()).isNull();
        assertThat(r.getFailureException()).isNotNull().isInstanceOf(SQLException.class).hasMessage("jou2");

        try {
            r.getOrElseThrow();
            fail("");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(SQLException.class).hasMessage("jou2");
        }

        try {
            r.getOrElseThrow(e -> new RuntimeException("juas", e));
            fail("");
        } catch (RuntimeException e) {
            assertThat(e)
                    .isInstanceOf(RuntimeException.class).hasMessage("juas")
                    .getCause()
                    .isInstanceOf(Exception.class).hasMessage("jou2");
        }
    }
}