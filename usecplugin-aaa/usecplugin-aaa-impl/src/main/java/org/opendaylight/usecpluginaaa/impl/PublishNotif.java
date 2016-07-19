/*
 * Copyright (c) 2016 Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.usecpluginaaa.impl;

import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.UsecpluginaaaListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.Invalidloginattempt;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.sal.binding.api.NotificationService;
import org.opendaylight.controller.sal.binding.api.NotificationProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.security.Loginattempts;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.security.LoginattemptsBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;

import static java.rmi.server.LogStream.log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.Security;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.AttemptFromIPInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.AttemptOnDateTimeInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.AttemptFromIPOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.AttemptOnDateTimeOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.AttemptOnDateTimeOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.AttemptFromIPOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.UsecpluginaaaService;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.InvalidloginattemptBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.InvalidloginattemptBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

public class PublishNotif {
	private static final Logger LOG = LoggerFactory
			.getLogger(PublishNotif.class);
	public static DataBroker dataBroker;
	public static long fileLength = 0;
	public static NotificationProviderService notificationProviderService;
	public static File file = null;

	static String fromFile = "";
	static long filePointer = 0;

	static private boolean tailing = false;

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

	// publish notification for invalid login attempt
	public void Notify() {
		try {
			readFromRandomAccessFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void readFromRandomAccessFile() {
		long lengthBefore = 0;
		long length = 0;
		file = ParsingLog.infile;
		try {
			if ((length = file.length()) > lengthBefore) {
				RandomAccessFile reader = new RandomAccessFile(file, "r");
				reader.seek(filePointer);
				lengthBefore = length;
				List<String> records = new ArrayList<String>();
				String workingDir = System.getProperty("user.dir");
				String linetoread = null;
				while (!((linetoread = reader.readLine()) == null)) {
					if (linetoread.contains("DEBUG")
							&& linetoread.contains("from")) {
						records.add(linetoread);
					}
				}
				for (int i = 0; i < records.size(); i++) {
					if (records.get(i).contains(
							"Unsuccessful authentication attempt")) {
						InvalidloginattemptBuilder invalidloginattemptBuilder = new InvalidloginattemptBuilder();
						notificationProviderService
								.publish(invalidloginattemptBuilder.build());
					}
				}
				filePointer = reader.getFilePointer();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
     }
}
