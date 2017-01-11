/*******************************************************************************
 * Copyright (c) 2013-2015 Sierra Wireless and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 * 
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
package org.eclipse.leshan.server.observation;

import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.response.ObserveResponse;

public interface ObservationListener {

    void newObservation(Observation observation);

    void cancelled(Observation observation);

    /**
     * Called on new notification.
     * 
     * @param observation the observation for which new data are received
     * @param reponse the lwm2m response received
     */
<<<<<<< HEAD:leshan-server-core/src/main/java/org/eclipse/leshan/server/observation/ObservationListener.java
    void newValue(Observation observation, ObserveResponse response);
=======
    void newValue(Observation observation, LwM2mNode mostRecentValue, List<TimestampedLwM2mNode> timestampedValues);
    void newValue(Observation observation, byte[] payload);
>>>>>>> 6010b9d8a266a3552c4602d1369a6e679e423926:leshan-server-core/src/main/java/org/eclipse/leshan/server/observation/ObservationRegistryListener.java
}
