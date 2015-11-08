package org.opendaylight.usecplugin.impl;


import java.sql.DriverManager;
import java.sql.ResultSet;

import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.UsecpluginService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.UsecpluginListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.LowWaterMarkBreached;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.LowWaterMarkBreachedBuilder;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.NotificationListener;
import org.opendaylight.yangtools.yang.binding.Notification;



public class UsecpluginNotifImpl implements UsecpluginListener {
	
	@Override
	public void onLowWaterMarkBreached(LowWaterMarkBreached notification) {
		
    System.out.println(" Inside UsecpluginNotifImpl ");
	      
			
		}
		
        
    }
	
	
	

	
	
	

