/*
 * Copyright (c) 2015 Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.usecpluginaaa.impl;

import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.UsecpluginaaaListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.Invalidloginattempt;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.sal.binding.api.NotificationService;
import org.opendaylight.controller.sal.binding.api.NotificationProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.rmi.server.LogStream.log;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.InvalidloginattemptBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.InvalidloginattemptBuilder;

public class PublishNotif {
	private static final Logger LOG = LoggerFactory
			.getLogger(PublishNotif.class);
	private DataBroker dataBroker;
	NotificationProviderService notificationProviderService;

	public DataBroker getdataBroker() {
		return dataBroker;
	}

	public void setdataBroker(DataBroker dataBroker) {
		this.dataBroker = dataBroker;
	}

	public void setNotificationProviderService(
			NotificationProviderService notificationProviderService) {
		this.notificationProviderService = notificationProviderService;
	}

	public void Notify() {

		List<String> records = new ArrayList<String>();

		try {

			String fileName = "/home/tcs/Desktop/distribution-karaf-0.4.2-Beryllium-SR2/data/log/karaf.log";
			Path path = Paths.get(fileName);
			Scanner scanner = new Scanner(path);
			scanner = new Scanner(path);

			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line.contains("DEBUG")) {

					records.add(line);
				}
			}

			for (int i = 0; i < records.size(); i++) {
				if (records.get(i).contains(
						"Unsuccessful authentication attempt"))

				{
					System.out.println("Invalid login attempts: "
							+ records.get(i));
					InvalidloginattemptBuilder invalidloginattemptBuilder = new InvalidloginattemptBuilder();
					notificationProviderService
							.publish(invalidloginattemptBuilder.build());
				}
			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
