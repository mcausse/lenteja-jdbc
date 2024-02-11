package org.homs.lechuga.queues;

import java.util.Date;

public interface QueueElement {

    Date getEnqueuedTime();

    void setEnqueuedTime(Date d);
}