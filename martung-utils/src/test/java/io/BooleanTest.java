package io;

public class BooleanTest {

    static class Acks {
        Integer comSent;
        Integer appSent;
        Integer comReceived;
        Integer appReceived;

        public boolean validate() {
            return (comSent == null && appSent == null) != (comReceived == null && appReceived == null);
        }
    }
}
