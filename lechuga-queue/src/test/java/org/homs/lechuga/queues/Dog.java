package org.homs.lechuga.queues;

import java.util.Date;

public class Dog implements QueueElement {

    private Long id;
    private String name;

    private Date enqueuedTime;

    public Dog() {
    }

    public Dog(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Date getEnqueuedTime() {
        return enqueuedTime;
    }

    @Override
    public void setEnqueuedTime(Date enqueuedTime) {
        this.enqueuedTime = enqueuedTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
