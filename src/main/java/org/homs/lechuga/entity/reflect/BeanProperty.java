package org.homs.lechuga.entity.reflect;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BeanProperty {

    final Class<?> beanClass;

    final PropertyDescriptor propertyDescriptor;
    final String name;
    final Class<?> type;

    final Field field;
    final Method getter;
    final Method setter;

    protected BeanProperty(Class<?> beanClass, PropertyDescriptor propertyDescriptor) {
        this.beanClass = beanClass;
        this.propertyDescriptor = propertyDescriptor;
        this.name = propertyDescriptor.getName();
        this.type = propertyDescriptor.getPropertyType();
        this.field = findField(beanClass, name);
        this.getter = propertyDescriptor.getReadMethod();
        this.setter = propertyDescriptor.getWriteMethod();
    }

    protected Field findField(Class<?> beanClass, String fieldName) {
        Class<?> o = beanClass;
        while (!Object.class.equals(o)) {
            try {
                return o.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                //
            }
            o = o.getSuperclass();
        }
        throw new RuntimeException("field not found: " + beanClass.getName() + "#" + fieldName);
    }

    public static List<BeanProperty> findBeanProperties(Class<?> beanClass) {
        BeanInfo info;
        try {
            info = Introspector.getBeanInfo(beanClass);
        } catch (IntrospectionException e) {
            throw new RuntimeException("describing " + beanClass.getName(), e);
        }

        var r = new ArrayList<BeanProperty>();
        PropertyDescriptor[] pds = info.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            if (pd.getName().equals("class") || pd.getName().contains("$")) {
                continue;
            }

            r.add(new BeanProperty(beanClass, pd));
        }
        return r;
    }

    public void setValue(Object bean, Object value) {
        try {
            this.setter.invoke(bean, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getValue(Object bean) {
        try {
            return this.getter.invoke(bean);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
        return field.isAnnotationPresent(annotationType);
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return field.getAnnotation(annotationType);
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public PropertyDescriptor getPropertyDescriptor() {
        return propertyDescriptor;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public Field getField() {
        return field;
    }

    public Method getGetter() {
        return getter;
    }

    public Method getSetter() {
        return setter;
    }

    @Override
    public String toString() {
        return beanClass.getName() + "#" + name;
    }

}