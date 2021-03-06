/*
 * Copyright (c) 2012-2018 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * The Apache License v2.0 is available at
 * http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

package io.moquette.broker.subscriptions;

import io.netty.handler.codec.mqtt.MqttQoS;

import java.io.Serializable;

/**
 * Maintain the information about which Topic a certain ClientID is subscribed and at which QoS
 */
public final class Subscription implements Serializable {

    private static final long serialVersionUID = -3383457629635732794L;
    private final MqttQoS requestedQos; // max QoS acceptable
    final String clientId;
    final Topic topicFilter;
    private final boolean active;

    public Subscription(String clientId, Topic topicFilter, MqttQoS requestedQos) {
        this.requestedQos = requestedQos;
        this.clientId = clientId;
        this.topicFilter = topicFilter;
        this.active = true;
    }

    public Subscription(Subscription orig) {
        this.requestedQos = orig.requestedQos;
        this.clientId = orig.clientId;
        this.topicFilter = orig.topicFilter;
        this.active = orig.active;
    }

    /**
     * Constructor with undefined maximum QoS
     *
     * @param clientId id of client owning this subscription.
     * @param topicFilter the topic to subscribe to.
     * */
    public Subscription(String clientId, Topic topicFilter) {
        this.requestedQos = null;
        this.clientId = clientId;
        this.topicFilter = topicFilter;
        this.active = true;
    }

    public String getClientId() {
        return clientId;
    }

    public MqttQoS getRequestedQos() {
        return requestedQos;
    }

    public Topic getTopicFilter() {
        return topicFilter;
    }

    public boolean isActive() {
        return active;
    }

    public boolean qosLessThan(Subscription sub) {
        return requestedQos.value() < sub.requestedQos.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Subscription that = (Subscription) o;

        if (clientId != null ? !clientId.equals(that.clientId) : that.clientId != null)
            return false;
        return !(topicFilter != null ? !topicFilter.equals(that.topicFilter) : that.topicFilter != null);
    }

    @Override
    public int hashCode() {
        int result = clientId != null ? clientId.hashCode() : 0;
        result = 31 * result + (topicFilter != null ? topicFilter.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "[filter:%s, cliID: %s, qos: %s, active: %s]",
                this.topicFilter,
                this.clientId,
                this.requestedQos,
                this.active);
    }

    @Override
    public Subscription clone() {
        try {
            return (Subscription) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
