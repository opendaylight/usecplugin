/*
 * Copyright (c) 2016 Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.usecpluginaaa.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.rmi.server.LogStream.log;



import java.io.File;
import java.util.Arrays;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.controller.sal.binding.api.BindingAwareProvider;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.RpcRegistration;
import org.opendaylight.controller.sal.binding.api.NotificationProviderService;
import org.opendaylight.yangtools.concepts.Registration;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

import org.opendaylight.controller.sal.binding.api.NotificationService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.InvalidloginattemptBuilder;
import org.opendaylight.controller.sal.binding.api.NotificationService;
import org.opendaylight.controller.sal.binding.api.NotificationProviderService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.UsecpluginaaaListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.UsecpluginaaaService;

public class UsecpluginaaaProvider implements BindingAwareProvider,
		AutoCloseable {
	ParsingLog parsingLog = new ParsingLog();
	private static final Logger LOG = LoggerFactory
			.getLogger(UsecpluginaaaProvider.class);
	public static DataBroker dataBroker;
	private RpcRegistration<UsecpluginaaaService> usecpluginaaaService;
	static NotificationProviderService notificationProvider;
    public void setdataBroker(DataBroker dataBroker) {
		this.dataBroker = dataBroker;
	}

	@Override
	public void onSessionInitiated(ProviderContext session) {
		LOG.info("UsecpluginsecurityProvider Session Initiated");

		RPCImpl rpc = new RPCImpl();
		usecpluginaaaService = session.addRpcImplementation(
				UsecpluginaaaService.class, rpc);
		dataBroker = session.getSALService(DataBroker.class);
		parsingLog.setdataBroker(dataBroker);
		rpc.setdataBroker(dataBroker);
		try {
			parsingLog.parse();
		} catch (Exception e) {
			e.printStackTrace();
		}
        NotifImpl listener = new NotifImpl();
		NotificationService notificationService = session
				.getSALService(NotificationService.class);
		notificationService.registerNotificationListener(listener);
		PublishNotif publishNotif = new PublishNotif();
		publishNotif.setdataBroker(dataBroker);
		notificationProvider = session.getSALService(NotificationProviderService.class);
		publishNotif.setNotificationProviderService(notificationProvider);
		}

	@Override
	public void close() throws Exception {
		LOG.info("UsecpluginsecurityProvider Closed");
		
		if (usecpluginaaaService != null) {
			usecpluginaaaService.close();
		}
	}
}
