/*
 * Copyright (c) 2015 Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.usecpluginaaa.impl;

import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.concurrent.Future;
import java.sql.*;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.binding.api.ReadTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.Security;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.AttemptFromIPInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.AttemptOnDateTimeInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.AttemptFromIPOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.AttemptOnDateTimeOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.AttemptOnDateTimeOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.AttemptFromIPOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.UsecpluginaaaService;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.security.Loginattempts;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.security.LoginattemptsBuilder;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.CheckedFuture;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class RPCImpl implements UsecpluginaaaService {
	private static final Logger LOG = LoggerFactory.getLogger(RPCImpl.class);

	AttemptOnDateTimeOutputBuilder attemptOnDateTimeOutputBuilder = new AttemptOnDateTimeOutputBuilder();
	AttemptFromIPOutputBuilder attemptFromIPOutputBuilder = new AttemptFromIPOutputBuilder();
	static private DataBroker dataBroker;

	public DataBroker getdataBroker() {
		return dataBroker;
	}

	public void setdataBroker(DataBroker dataBroker) {
		this.dataBroker = dataBroker;
	}

	static String fileName = "/home/tcs/Desktop/distribution-karaf-0.4.2-Beryllium-SR2/data/log/karaf.log";

	// AttemptFromSrcIP
	@Override
	public Future<RpcResult<AttemptFromIPOutput>> attemptFromIP(
			AttemptFromIPInput input) {
		Boolean value = false;
		String time = "";
		String attempt = "";
		String srcIP = "";
		String content = "";

		try {
			String InputSrcIP = input.getSrcIP();
			Path path = Paths.get(fileName);

			Scanner scanner = new Scanner(path);
			scanner = new Scanner(path);

			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line.contains("DEBUG") && line.contains("from"))

				{

					String phrase1 = line;
					String delims = "[,]+";
					String[] tokens = phrase1.split(delims);

					String phrase2 = line;
					String delims2 = "[|]+";
					String[] tokens2 = phrase2.split(delims2);
					time = tokens[0];
					attempt = tokens2[5];

					String phrase3 = line;

					String[] parts = phrase3.split(" ");
					srcIP = parts[parts.length - 1];

					
					if (srcIP.matches(InputSrcIP)) {
						value = true;
						content += attempt + "  " + "on" + " " + time + ",";
						

					}

				}
			}
			if (value == true)
				attemptFromIPOutputBuilder.setLoginAttempt(content);
			else
				attemptFromIPOutputBuilder.setLoginAttempt("No attempts");

		} catch (Exception e) {

			e.printStackTrace();
		}

		return RpcResultBuilder.success(attemptFromIPOutputBuilder.build())
				.buildFuture();

	}

	// Attempt_on_datetime
	@Override
	public Future<RpcResult<AttemptOnDateTimeOutput>> attemptOnDateTime(
			AttemptOnDateTimeInput input) {
		Boolean value = false;
		String time = "";
		String attempt = "";
		String srcIP = "";
		String content = "";
		String InputDateTime = "";
		try {
			Path path = Paths.get(fileName);
			Scanner scanner = new Scanner(path);
			scanner = new Scanner(path);
			InputDateTime = input.getDateTime();
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line.contains("DEBUG") && line.contains("from"))

				{

					String phrase1 = line;
					String delims = "[,]+";
					String[] tokens = phrase1.split(delims);

					String phrase2 = line;
					String delims2 = "[|]+";
					String[] tokens2 = phrase2.split(delims2);
					time = tokens[0];
					attempt = tokens2[5];

					String phrase3 = line;

					String[] parts = phrase3.split(" ");
					srcIP = parts[parts.length - 1];

					

					if (time.matches(InputDateTime)) {
						value = true;
						
						content += attempt + "  " + "on" + " " + time + ",";
						System.out.println("LoginAttempt:" + content);
					}

				}
			}
			if (value == true)
				attemptOnDateTimeOutputBuilder.setLoginAttempt(content);
			else
				attemptOnDateTimeOutputBuilder.setLoginAttempt("No attempts");

		} catch (Exception e) {

			e.printStackTrace();
		}

		return RpcResultBuilder.success(attemptOnDateTimeOutputBuilder.build())
				.buildFuture();

	}

}
