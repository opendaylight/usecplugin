/*
 * Copyright © 2015 tcs.com and others.  All rights reserved.
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
import java.util.ArrayList;
import java.util.List;
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

	private static final Logger LOG = LoggerFactory
			.getLogger(UsecpluginaaaProvider.class);
	private DataBroker dataBroker;
	private RpcRegistration<UsecpluginaaaService> usecpluginaaaService;

	@Override
	public void onSessionInitiated(ProviderContext session) {
		LOG.info("UsecpluginsecurityProvider Session Initiated");
		ParsingLog parsingLog = new ParsingLog();
		RPCImpl rpc = new RPCImpl();
		usecpluginaaaService = session.addRpcImplementation(
				UsecpluginaaaService.class, new RPCImpl());
		dataBroker = session.getSALService(DataBroker.class);
		parsingLog.setdataBroker(dataBroker);
		try {
			parsingLog.parse();

		} catch (IOException e) {
			e.printStackTrace();
		}

		NotifImpl listener = new NotifImpl();
		NotificationService notificationService = session
				.getSALService(NotificationService.class);
		notificationService.registerNotificationListener(listener);
		PublishNotif publishNotif = new PublishNotif();
		NotificationProviderService notificationProvider = session
				.getSALService(NotificationProviderService.class);
		publishNotif.setNotificationProviderService(notificationProvider);
		publishNotif.Notify();

	}

	@Override
	public void close() throws Exception {
		LOG.info("UsecpluginsecurityProvider Closed");

		if (usecpluginaaaService != null) {
			usecpluginaaaService.close();
		}
	}

}
