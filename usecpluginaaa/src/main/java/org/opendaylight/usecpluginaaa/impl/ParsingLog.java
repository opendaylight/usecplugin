/*
 * Copyright (c) 2015 Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.usecpluginaaa.impl;

import java.sql.*;

import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.binding.api.ReadTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.rmi.server.LogStream.log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import org.opendaylight.controller.sal.binding.api.NotificationService;
import org.opendaylight.controller.sal.binding.api.NotificationProviderService;
//import org.opendaylight.yang.data.api.InstanceIdentifier;

import static java.util.regex.Pattern.quote;

import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.InvalidloginattemptBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.Security;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.UsecpluginaaaData;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.security.Loginattempts;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.security.LoginattemptsBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.security.LoginattemptsKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.CheckedFuture;

public class ParsingLog {

	private static final Logger LOG = null;

	static private DataBroker dataBroker;

	public DataBroker getdataBroker() {
		return dataBroker;
	}

	public void setdataBroker(DataBroker dataBroker) {
		this.dataBroker = dataBroker;
	}

	UsecpluginAAAStore usecpluginAAAstore = new UsecpluginAAAStore();

	static String line;
	static String time = "";
	static String attempt = "";
	static String srcIP = "";

	public void parse() throws IOException {

		usecpluginAAAstore.setdataBroker(dataBroker);

		String key = "";

		try {

			String fileName = "/home/tcs/Desktop/distribution-karaf-0.4.2-Beryllium-SR2/data/log/karaf.log";
			Path path = Paths.get(fileName);
			Scanner scanner = new Scanner(path);

			while (scanner.hasNext()) {
				line = scanner.nextLine();
				if (line.contains("DEBUG") && line.contains("from"))

				{
					System.out.println(line);
					String phrase1 = line;
					String delims = "[,]+";
					String[] tokens = phrase1.split(delims);
					System.out.println("Time=" + tokens[0]);

					String phrase2 = line;
					String delims2 = "[|]+";
					String[] tokens2 = phrase2.split(delims2);
					time = tokens[0];
					attempt = tokens2[5];

					String phrase3 = line;

					String[] parts = phrase3.split(" ");
					srcIP = parts[parts.length - 1];

					key = srcIP;

					usecpluginAAAstore.addData(key, time, attempt);

					System.out.println("Information Stored in Data Store is "
							+ key + time + attempt);

				}

			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

	}
}
