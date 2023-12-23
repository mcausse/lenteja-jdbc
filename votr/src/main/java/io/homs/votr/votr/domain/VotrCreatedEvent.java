package io.homs.votr.votr.domain;

public class VotrCreatedEvent {

    private final Votr votr;

    public VotrCreatedEvent(Votr votr) {
        this.votr = votr;
    }

    public Votr getVotr() {
        return votr;
    }
}
