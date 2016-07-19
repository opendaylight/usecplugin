/*
 * Copyright (c) 2016 Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.usecpluginaaa.impl;

import java.sql.*;
import java.util.TimerTask;
import java.io.*;
import java.util.*;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.binding.api.ReadTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.rmi.server.LogStream.log;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import static java.util.regex.Pattern.quote;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.InvalidloginattemptBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.Security;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.UsecpluginaaaData;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.security.Loginattempts;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.security.LoginattemptsBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.security.LoginattemptsKey;
import static java.nio.file.StandardWatchEventKinds.*;
import com.google.common.base.Optional;
import com.google.common.util.concurrent.CheckedFuture;
import org.apache.commons.io.comparator.LastModifiedFileComparator;

public class ParsingLog extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(ParsingLog.class);
	public static DataBroker dataBroker;
    public DataBroker getdataBroker() {
		return dataBroker;
	}
    public void setdataBroker(DataBroker dataBroker) {
		this.dataBroker = dataBroker;
	}

	static RandomAccessFile reader = null;
	static private long sampleInterval = 5000;
	static File[] files;
	static File file = null;
	static File theNewestFile = null;
	static String workingDir;
	static String line;
	static String linetoRead;
	static String time = "";
	static String attempt = "";
	static String srcIP = "";
	static File infile;
	static String RevOrder_ofFiles = "";
	static String karafFiles = "";
	static String fileName;
	static Path karaffile;
	static String filetoread;
	static String WatchedDir;
	static long lengthBefore = 0;
	static long length = 0;
	private static boolean startAtBeginning = false;
	private static boolean tailing = false;
	UsecpluginAAAStore usecpluginAAAstore = new UsecpluginAAAStore();

	public void parse() {
		usecpluginAAAstore.setdataBroker(dataBroker);
		try {
			getTheNewestFile();
			ParsingLog parsinglog = new ParsingLog();
			parsinglog.start();

		} catch (Exception ioe) {
			ioe.printStackTrace();
		}

	}

	public File getTheNewestFile() {

		workingDir = System.getProperty("user.dir");
		String fileName = workingDir + File.separator + "data" + File.separator
				+ "log" + File.separator + "karaf.log";
		WatchedDir = workingDir + File.separator + "data" + File.separator
				+ "log";

		try {
			LOG.info("log enabled ...");
			File dir = new File(WatchedDir);
			Thread.sleep(10000);
			files = dir.listFiles();
			Arrays.sort(files,
					LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
			for (int i = 0; i < files.length; i++) {
				file = files[i];
				karafFiles += file.getName();
			}
			LOG.info("karaf files:" + karafFiles);
			Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
			for (int i = 0; i < files.length; i++) {
				file = files[i];
				RevOrder_ofFiles += file.getName();
			}
			LOG.info("list of karaf File in reverse order" + " "
					+ RevOrder_ofFiles);
			theNewestFile = files[0];
			LOG.info("Latest karaf File" + theNewestFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return theNewestFile;
	}

	public void run() {
		long lengthBefore = 0;
		long length = 0;

		try {

			long filePointer = 0;
            if (this.startAtBeginning) {
				filePointer = 0;
			} else {
				filePointer = this.theNewestFile.length();
			}
			this.tailing = true;
			reader = new RandomAccessFile(theNewestFile, "r");
			while (this.tailing) {
				long fileLength = this.theNewestFile.length();
				if (fileLength < filePointer) {

					reader = new RandomAccessFile(theNewestFile, "r");
					filePointer = 0;
				}
				if ((length = theNewestFile.length()) > lengthBefore) {
					reader.seek(lengthBefore);
					lengthBefore = length;
					while (!((linetoRead = reader.readLine()) == null)) {
						callData();
					}
					filePointer = reader.getFilePointer();
				}
				sleep(this.sampleInterval);
				reader.close();
			}
		} catch (Exception e) {
		}

	}

	public void callData() {
		workingDir = System.getProperty("user.dir");
		String fileName = workingDir + File.separator + "data" + File.separator
				+ "log" + File.separator + "karaf.log";
		WatchedDir = workingDir + File.separator + "data" + File.separator
				+ "log";
		try {

			LOG.info("log enabled ...");
			Thread.sleep(10000);
			infile = new File(theNewestFile.toString());
			LOG.info("karaf file ..." + theNewestFile);
			Thread.sleep(1000);
			LOG.info("parsing karaf file ...");
			Thread.sleep(9000);
			Scanner scanner = new Scanner(infile);

			while (scanner.hasNext()) {
				line = scanner.nextLine();
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
					usecpluginAAAstore.addData(time, srcIP, attempt);
					LOG.info("Information stored in datastore is" + time + " "
							+ srcIP + " " + attempt);
				}
			}
			PublishNotif publishNotif = new PublishNotif();
			publishNotif.setdataBroker(dataBroker);
			publishNotif.Notify();

		} catch (Exception e) {

			e.printStackTrace();
		}
}
}
