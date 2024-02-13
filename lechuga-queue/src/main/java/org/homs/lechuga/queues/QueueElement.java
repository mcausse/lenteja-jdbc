package org.homs.lechuga.queues;

import java.util.Date;

public class QueueElement {

    private Date enqueuedTime;

    public Date getEnqueuedTime() {
        return enqueuedTime;
    }

    public void setEnqueuedTime(Date enqueuedTime) {
        this.enqueuedTime = enqueuedTime;
    }
}