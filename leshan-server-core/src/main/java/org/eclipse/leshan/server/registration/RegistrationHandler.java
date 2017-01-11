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

<<<<<<< HEAD
    private RegistrationServiceImpl registrationService;
    private Authorizer authorizer;

    public RegistrationHandler(RegistrationServiceImpl registrationService, Authorizer authorizer) {
        this.registrationService = registrationService;
        this.authorizer = authorizer;
=======
    private SecurityStore securityStore;
    private ClientRegistry clientRegistry;
    //zyj add begin
    private Map<String, String> rulebyObjectid;
    private int maxClients;

    public RegistrationHandler(ClientRegistry clientRegistry, SecurityStore securityStore, Map<String, String> rulebyObjectid) {
        this.clientRegistry = clientRegistry;
        this.securityStore = securityStore;
        this.rulebyObjectid = rulebyObjectid;
        this.maxClients = 1000;//default 1000
    }
    //zyj add end
    public RegistrationHandler(ClientRegistry clientRegistry, SecurityStore securityStore) {
        this.clientRegistry = clientRegistry;
        this.securityStore = securityStore;
>>>>>>> 6010b9d8a266a3552c4602d1369a6e679e423926
    }

    public RegisterResponse register(Identity sender, RegisterRequest registerRequest, InetSocketAddress serverEndpoint) {

        if (registerRequest.getEndpointName() == null || registerRequest.getEndpointName().isEmpty() || sender == null) {
            return RegisterResponse.badRequest(null);
        }

<<<<<<< HEAD
        Registration.Builder builder = new Registration.Builder(RegistrationHandler.createRegistrationId(),
=======
        //zyj add begin
        //If the client is reach the max number, refuse the new client to register
        int num = clientRegistry.numberOfRegistClient();
        LOG.info("registered client number:{}", num);
        if(num >= this.maxClients){
            LOG.info("reach ve max number, refuse client: {} to register", registerRequest.getEndpointName());
            return RegisterResponse.forbidden(null);
        }
        //zyj add end
        // We must check if the client is using the right identity.
        if (!isAuthorized(registerRequest.getEndpointName(), sender)) {
            return RegisterResponse.forbidden(null);
        }
        //zyj add begin
        if (!isAlinketAuthorized(registerRequest.getObjectLinks(),registerRequest.getEndpointName())) {
            return RegisterResponse.forbidden(null);
        }
        //zyj add end
        Client.Builder builder = new Client.Builder(RegistrationHandler.createRegistrationId(),
>>>>>>> 6010b9d8a266a3552c4602d1369a6e679e423926
                registerRequest.getEndpointName(), sender.getPeerAddress().getAddress(), sender.getPeerAddress()
                        .getPort(), serverEndpoint);

        builder.lwM2mVersion(registerRequest.getLwVersion()).lifeTimeInSec(registerRequest.getLifetime())
                .bindingMode(registerRequest.getBindingMode()).objectLinks(registerRequest.getObjectLinks())
                .smsNumber(registerRequest.getSmsNumber()).registrationDate(new Date()).lastUpdate(new Date())
                .additionalRegistrationAttributes(registerRequest.getAdditionalAttributes());

        Registration registration = builder.build();

        // We must check if the client is using the right identity.
        if (!authorizer.isAuthorized(registerRequest, registration, sender)) {
            return RegisterResponse.forbidden(null);
        }

        if (registrationService.registerClient(registration)) {
            LOG.debug("New registered client: {}", registration);
            return RegisterResponse.success(registration.getId());
        } else {
            return RegisterResponse.forbidden(null);
        }
    }

    public UpdateResponse update(Identity sender, UpdateRequest updateRequest) {

        if (sender == null) {
            return UpdateResponse.badRequest(null);
        }

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
        if (sender == null) {
            return DeregisterResponse.badRequest(null);
        }

        // We must check if the client is using the right identity.
        Registration registration = registrationService.getById(deregisterRequest.getRegistrationID());
        if (registration == null) {
            return DeregisterResponse.notFound();
        }
        if (!authorizer.isAuthorized(deregisterRequest, registration, sender)) {
            // TODO replace by Forbidden if https://github.com/OpenMobileAlliance/OMA_LwM2M_for_Developers/issues/181 is
            // closed.
            return DeregisterResponse.badRequest("forbidden");
        }

        Registration unregistered = registrationService.deregisterClient(deregisterRequest.getRegistrationID());
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
=======

    /**
     * Return true if the client with the given lightweight M2M endPoint is authorized to communicate with the given
     * security parameters.
     * 
     * @param lwM2mEndPointName the lightweight M2M endPoint name
     * @param clientIdentity the identity at TLS level
     * @return true if device get authorization
     */
    private boolean isAuthorized(String lwM2mEndPointName, Identity clientIdentity) {
        // do we have security information for this client?
        SecurityInfo expectedSecurityInfo = securityStore.getByEndpoint(lwM2mEndPointName);
        return SecurityCheck.checkSecurityInfo(lwM2mEndPointName, clientIdentity, expectedSecurityInfo);
    }
//zyj add begin
    private boolean isAlinketAuthorized(LinkObject[] objectLinks, String lwM2mEndPointName) {
        boolean findandmatch = false;
        for (LinkObject link : objectLinks) {
            if (link == null) {
                continue;
            }
            for (String key : rulebyObjectid.keySet()) {
                if (link.toString().contains(key)) {
                    if (lwM2mEndPointName.matches(rulebyObjectid.get(key))) {
                        LOG.info("client:{} objectid:{} matches the pattern", lwM2mEndPointName, key);
                        findandmatch = true;
                    } else
                        LOG.info("client:{} objectid:{} not matches the pattern", lwM2mEndPointName, key);
                }
            }
        }
        LOG.info("isAlinketAuthorized return:{} client:{} ", findandmatch, lwM2mEndPointName);
        return findandmatch;
    }

    public void setAlinketRule(Map<String, String> rulebyObjectid, int maxclients) {
        this.rulebyObjectid = rulebyObjectid;
        this.maxClients = maxclients;
    }
//zyj add end
>>>>>>> 6010b9d8a266a3552c4602d1369a6e679e423926
}
