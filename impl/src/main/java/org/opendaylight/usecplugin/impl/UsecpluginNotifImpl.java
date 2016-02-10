/*
 * Copyright (c) 2015 Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsecpluginNotifImpl implements UsecpluginListener {
    private static final Logger LOG = LoggerFactory.getLogger(UsecpluginNotifImpl.class);

    @Override
    public void onLowWaterMarkBreached(LowWaterMarkBreached notification) {
    LOG.info("NotificationReceived");
    }
}
