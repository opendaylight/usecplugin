/*
 * Copyright (c) 2016 Tata Consultancy Services and others.  All rights reserved.
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

	UsecpluginAAAStore usecpluginAAAStore = new UsecpluginAAAStore();
	List<Loginattempts> loginattempts = new ArrayList<Loginattempts>();

	// AttemptFromSrcIP
    @Override
	public Future<RpcResult<AttemptFromIPOutput>> attemptFromIP(
			AttemptFromIPInput input) {
		Boolean value = false;
		String time = "";
		String attempt = "";
		String content = "";
		String listOfSource_IP = "";
		try {

			usecpluginAAAStore.setdataBroker(dataBroker);
			InstanceIdentifier<Security> instanceIdentifier = InstanceIdentifier
					.create(Security.class);
			ReadOnlyTransaction readTransaction = dataBroker
					.newReadOnlyTransaction();
			loginattempts = readTransaction
					.read(LogicalDatastoreType.OPERATIONAL, instanceIdentifier)
					.checkedGet().get().getLoginattempts();
			String inputSrcIP = input.getSrcIP();

			for (Loginattempts loginattempt : loginattempts) {
				listOfSource_IP = loginattempt.getSrcIP();
				if (listOfSource_IP.matches(inputSrcIP)) {
					value = true;
					content += loginattempt.getAttempt() + "  " + "on" + " "
							+ loginattempt.getTime() + ",";
				}
			}
			if (value == true) {
				  attemptFromIPOutputBuilder.setLoginAttempt(content);
                                  LOG.info(content);
			} else
				{
                                     attemptFromIPOutputBuilder.setLoginAttempt("No Attempts");
                                     LOG.info("No Attempts from given IP");
                                }
		} catch (Exception e) {
			System.out.print("Caught the NullPointerException");
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
		String content = "";
		String listOfStoredTime = "";
		try {
			usecpluginAAAStore.setdataBroker(dataBroker);
			InstanceIdentifier<Security> instanceIdentifier = InstanceIdentifier
					.create(Security.class);
			ReadOnlyTransaction readTransaction = dataBroker
					.newReadOnlyTransaction();
			loginattempts = readTransaction
					.read(LogicalDatastoreType.OPERATIONAL, instanceIdentifier)
					.checkedGet().get().getLoginattempts();
			String inputDateTime = input.getDateTime();
			for (Loginattempts loginattempt : loginattempts) {
				listOfStoredTime = loginattempt.getTime();
				if (listOfStoredTime.matches(inputDateTime)) {
					value = true;
					content += loginattempt.getAttempt() + "  " + "on" + " "
							+ loginattempt.getTime();

				}
			}
			if (value == true)
				{
                                    attemptOnDateTimeOutputBuilder.setLoginAttempt(content);
                                    LOG.info(content);
                                }

			else
		             {
                              attemptOnDateTimeOutputBuilder.setLoginAttempt("No Attempts");
                              LOG.info("No Attempts on given dateTime");
                             }

		} catch (Exception e) {
			e.printStackTrace();
		}
		return RpcResultBuilder.success(attemptOnDateTimeOutputBuilder.build())
				.buildFuture();
}
}
