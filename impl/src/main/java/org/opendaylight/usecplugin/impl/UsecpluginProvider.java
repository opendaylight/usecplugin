/*
 * Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.usecplugin.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.sal.binding.api.NotificationService;

import org.opendaylight.yangtools.concepts.Registration;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.controller.sal.binding.api.BindingAwareProvider;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.RpcRegistration;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.UsecpluginService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.NotificationListener;
import org.opendaylight.yangtools.yang.binding.Notification;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.UsecpluginListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.LowWaterMarkBreached;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.LowWaterMarkBreachedBuilder;


public class UsecpluginProvider implements  BindingAwareProvider, AutoCloseable{

    private static final Logger LOG = LoggerFactory.getLogger(UsecpluginProvider.class);
    private RpcRegistration<UsecpluginService> usecpluginService;
   
    LowWaterMarkBreached notification;
    
    private DataBroker dataBroker;
    
    @Override
    public void onSessionInitiated(ProviderContext session) {
    	
    	LOG.info("UsecpluginProvider Session Initiated");
    	
        System.out.println("UsecpluginProvider Session Initiated");
        
        usecpluginService = session.addRpcImplementation(UsecpluginService.class, new UsecpluginRPCImpl());
       
        NotificationService notificationService = session.getSALService(NotificationService.class);
        PacketHandler packetHandler = new PacketHandler();
      
        notificationService.registerNotificationListener(packetHandler);
        dataBroker = session.getSALService(DataBroker.class);
        packetHandler.setdataBroker(dataBroker);
        packetHandler.dbOpen();
        
        
            UsecpluginNotifImpl listener = new UsecpluginNotifImpl();
               
			listener.onLowWaterMarkBreached(notification);
               
		
       
    }

    @Override
    public void close() throws Exception {
        LOG.info("UsecpluginProvider Closed");
        if (usecpluginService != null) {
        	usecpluginService.close();
        }
       
        
    }
}
