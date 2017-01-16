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
package org.eclipse.leshan.server.registration;

import java.net.InetSocketAddress;
import java.util.Date;
//zyj add begin
import java.util.Map;
import org.eclipse.leshan.LinkObject;
//zyj add end
import org.eclipse.leshan.core.request.DeregisterRequest;
import org.eclipse.leshan.core.request.Identity;
import org.eclipse.leshan.core.request.RegisterRequest;
import org.eclipse.leshan.core.request.UpdateRequest;
import org.eclipse.leshan.core.response.DeregisterResponse;
import org.eclipse.leshan.core.response.RegisterResponse;
import org.eclipse.leshan.core.response.UpdateResponse;
import org.eclipse.leshan.server.client.Registration;
import org.eclipse.leshan.server.client.RegistrationService;
import org.eclipse.leshan.server.client.RegistrationUpdate;
import org.eclipse.leshan.server.impl.RegistrationServiceImpl;
import org.eclipse.leshan.server.security.Authorizer;
import org.eclipse.leshan.util.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handle the client registration logic. Check if the client is allowed to register, with the wanted security scheme.
 * Create the {@link Registration} representing the registered client and add it to the {@link RegistrationService}
 */
public class RegistrationHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RegistrationHandler.class);

    private RegistrationServiceImpl registrationService;
    private Authorizer authorizer;

    public RegistrationHandler(RegistrationServiceImpl registrationService, Authorizer authorizer) {
        this.registrationService = registrationService;
        this.authorizer = authorizer;
    }

    public RegisterResponse register(Identity sender, RegisterRequest registerRequest, InetSocketAddress serverEndpoint) {

<<<<<<< HEAD
        if (registerRequest.getEndpointName() == null || registerRequest.getEndpointName().isEmpty() || sender == null) {
            return RegisterResponse.badRequest(null);
        }

=======
>>>>>>> e11bf35657fa8e2abbd90aed2097f9058abd4897
        Registration.Builder builder = new Registration.Builder(RegistrationHandler.createRegistrationId(),
                registerRequest.getEndpointName(), sender.getPeerAddress().getAddress(), sender.getPeerAddress()
                        .getPort(), serverEndpoint);

        builder.lwM2mVersion(registerRequest.getLwVersion()).lifeTimeInSec(registerRequest.getLifetime())
                .bindingMode(registerRequest.getBindingMode()).objectLinks(registerRequest.getObjectLinks())
                .smsNumber(registerRequest.getSmsNumber()).registrationDate(new Date()).lastUpdate(new Date())
                .additionalRegistrationAttributes(registerRequest.getAdditionalAttributes());

        Registration registration = builder.build();
<<<<<<< HEAD

        // We must check if the client is using the right identity.
        if (!authorizer.isAuthorized(registerRequest, registration, sender)) {
            return RegisterResponse.forbidden(null);
        }

=======

        // We must check if the client is using the right identity.
        if (!authorizer.isAuthorized(registerRequest, registration, sender)) {
            return RegisterResponse.forbidden(null);
        }

>>>>>>> e11bf35657fa8e2abbd90aed2097f9058abd4897
        if (registrationService.registerClient(registration)) {
            LOG.debug("New registered client: {}", registration);
            return RegisterResponse.success(registration.getId());
        } else {
            return RegisterResponse.forbidden(null);
        }
    }

    public UpdateResponse update(Identity sender, UpdateRequest updateRequest) {

        // We must check if the client is using the right identity.
        Registration registration = registrationService.getById(updateRequest.getRegistrationId());
        if (registration == null) {
            return UpdateResponse.notFound();
        }
        if (!authorizer.isAuthorized(updateRequest, registration, sender)) {
            // TODO replace by Forbidden if https://github.com/OpenMobileAlliance/OMA_LwM2M_for_Developers/issues/181 is
            // closed.
            return UpdateResponse.badRequest("forbidden");
        }

        registration = registrationService.updateRegistration(new RegistrationUpdate(updateRequest.getRegistrationId(), sender
                .getPeerAddress().getAddress(), sender.getPeerAddress().getPort(), updateRequest.getLifeTimeInSec(),
                updateRequest.getSmsNumber(), updateRequest.getBindingMode(), updateRequest.getObjectLinks()));
        if (registration == null) {
            return UpdateResponse.notFound();
        } else {
            return UpdateResponse.success();
        }
    }

    public DeregisterResponse deregister(Identity sender, DeregisterRequest deregisterRequest) {

        // We must check if the client is using the right identity.
<<<<<<< HEAD
        Registration registration = registrationService.getById(deregisterRequest.getRegistrationID());
=======
        Registration registration = registrationService.getById(deregisterRequest.getRegistrationId());
>>>>>>> e11bf35657fa8e2abbd90aed2097f9058abd4897
        if (registration == null) {
            return DeregisterResponse.notFound();
        }
        if (!authorizer.isAuthorized(deregisterRequest, registration, sender)) {
            // TODO replace by Forbidden if https://github.com/OpenMobileAlliance/OMA_LwM2M_for_Developers/issues/181 is
            // closed.
            return DeregisterResponse.badRequest("forbidden");
        }

<<<<<<< HEAD
        Registration unregistered = registrationService.deregisterClient(deregisterRequest.getRegistrationID());
=======
        Registration unregistered = registrationService.deregisterClient(deregisterRequest.getRegistrationId());
>>>>>>> e11bf35657fa8e2abbd90aed2097f9058abd4897
        if (unregistered != null) {
            return DeregisterResponse.success();
        } else {
            LOG.debug("Invalid deregistration");
            return DeregisterResponse.notFound();
        }
    }

    private static String createRegistrationId() {
        return RandomStringUtils.random(10, true, true);
    }
<<<<<<< HEAD

    //zyj add begin
    private boolean isAlinketAuthorized(LinkObject[] objectLinks, String lwM2mEndPointName) {
        boolean findandmatch = false;
//        for (LinkObject link : objectLinks) {
//            if (link == null) {
//                continue;
//            }
//            for (String key : rulebyObjectid.keySet()) {
//                if (link.toString().contains(key)) {
//                    if (lwM2mEndPointName.matches(rulebyObjectid.get(key))) {
//                        LOG.info("client:{} objectid:{} matches the pattern", lwM2mEndPointName, key);
//                        findandmatch = true;
//                    } else
//                        LOG.info("client:{} objectid:{} not matches the pattern", lwM2mEndPointName, key);
//                }
//            }
//        }
        LOG.info("isAlinketAuthorized return:{} client:{} ", findandmatch, lwM2mEndPointName);
        return findandmatch;
    }

    public void setAlinketRule(Map<String, String> rulebyObjectid, int maxclients) {
//        this.rulebyObjectid = rulebyObjectid;
//        this.maxClients = maxclients;
    }
//zyj add end
=======
>>>>>>> e11bf35657fa8e2abbd90aed2097f9058abd4897
}
