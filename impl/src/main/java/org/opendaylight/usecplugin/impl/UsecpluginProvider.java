/*
 * Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.usecplugin.impl;
import org.opendaylight.controller.sal.binding.api.NotificationService;
import org.opendaylight.yangtools.concepts.Registration;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.controller.sal.binding.api.BindingAwareProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsecpluginProvider implements BindingAwareProvider, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(UsecpluginProvider.class);

    @Override
    public void onSessionInitiated(ProviderContext session) {
        LOG.info("UsecpluginProvider Session Initiated");
        System.out.println("UsecpluginProvider Session Initiated");

       NotificationService notificationService = session.getSALService(NotificationService.class);
       PacketHandler packetHandler = new PacketHandler();
       Registration packetInRegistration = notificationService.registerNotificationListener(packetHandler);
    }

    @Override
    public void close() throws Exception {
        LOG.info("UsecpluginProvider Closed");
    }

}
