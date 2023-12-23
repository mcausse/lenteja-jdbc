package io.martung.observer;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ObserverTest {

    List<Integer> ints;

    @Test
    void name() {

        var model = new Model();
        var view = new View(model);
        model.addListener(view::showInts);
        model.addListener(e -> this.ints = e);

        view.triggerInt(1);
        view.triggerInt(2);
        view.triggerInt(3);

        assertThat(ints).hasToString("[1, 2, 3]");
    }

    static class View {

        final Model model;

        public View(Model model) {
            this.model = model;
        }

        public void triggerInt(int value) {
            this.model.addNewInt(value);
        }

        public void showInts(List<Integer> e) {
            System.out.println(e);
        }
    }

    static class Model {

        final List<Integer> ints = new ArrayList<>();
        final Notifier<List<Integer>, Listener<List<Integer>>> intCreated = new Notifier<>();

        public void addListener(Listener<List<Integer>> listener) {
            intCreated.addListener(listener);
        }

        public void addNewInt(int value) {
            this.ints.add(value);
            intCreated.notify(this.ints);
        }
    }
}
