package org.homs.lechuga.entity.reflect;

import org.homs.lechuga.Dog;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class BeanPropertyShould {

    public static class ExtendedDog extends Dog {
        String extraField;

        public String getExtraField() {
            return extraField;
        }

        public void setExtraField(String extraField) {
            this.extraField = extraField;
        }
    }

    String toString(List<BeanProperty> beanProperties) {
        return new TreeSet<>(
                beanProperties.stream()
                        .map(BeanProperty::getName)
                        .collect(Collectors.toSet())).toString();
    }

    @Test
    void find_the_properties_of_Dog_class() {
        var properties = BeanProperty.findBeanProperties(Dog.class);

        assertThat(toString(properties)).isEqualTo("[age, chipId, id, name]");
    }

    @Test
    void find_the_properties_of_Inherited_class() {
        var properties = BeanProperty.findBeanProperties(ExtendedDog.class);

        assertThat(toString(properties)).isEqualTo("[age, chipId, extraField, id, name]");
    }

    @Test
    void modify_the_field_values() {
        var properties = BeanProperty.findBeanProperties(ExtendedDog.class);
        var dogNameProperty = properties.stream()
                .filter(p -> p.getName().equals("name"))
                .reduce((a, b) -> {
                    throw new IllegalStateException("Multiple elements: " + a + ", " + b);
                })
                .get();
        Dog chucho = new ExtendedDog();

        // Act
        dogNameProperty.setValue(chucho, "chucho");

        assertThat(dogNameProperty.getValue(chucho)).isEqualTo("chucho");
    }
}