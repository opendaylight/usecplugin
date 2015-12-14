/*
 * Copyright (c) 2015 Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.usecplugin.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsecpluginDatabaseHWM {
	static Integer counter = 0;
	Connection connection = null;
	Statement stmt = null;

	public void dbHwm() {
		try {
			System.out.println("Creating HWM DB");
			Class.forName("org.sqlite.JDBC", true, Thread.currentThread()
					.getContextClassLoader());
			connection = DriverManager
					.getConnection("jdbc:sqlite:usecpluginhwm:db");
			connection.setAutoCommit(false);
			System.out.println("Opened HWM DB Successfully");
			stmt = connection.createStatement();
			DatabaseMetaData metaData = connection.getMetaData();
			ResultSet tableInfo = metaData.getTables(null, null, "%", null);
			List<String> tableList = new ArrayList<String>();
			while (tableInfo.next()) {
				tableList.add(tableInfo.getString(3));
				System.out.println("Table info " + tableList.get(0));
			}

			if (!tableList.contains("UsecPluginHWM")) {
				String sql = "CREATE TABLE UsecPluginHWM"
						+ "(ID INT PRIMARY    KEY     NOT NULL, "
						+ " NodeID            TEXT    NOT NULL, "
						+ " NodeConnectorID   TEXT    NOT NULL, "
						+ " SrcIP             TEXT    NOT NULL, "
						+ " DstIP             TEXT    NOT NULL, "
						+ " Protocol          TEXT    NOT NULL, "
						+ " SrcPort           INT     NOT NULL, "
						+ " DstPort           INT     NOT NULL, "
						+ " PacketSize        INT     NOT NULL, "
						+ " DiffTime          TEXT    NOT NULL, "
						+ " UpwardTime        TEXT    NOT NULL, "
						+ " DownwardTime      TEXT    NOT NULL)";
				stmt.executeUpdate(sql);

				stmt.close();
				connection.commit();
				connection.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException se2) {
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
			}
		}

	}

	public void dbWrite(String nodeId, String nodeConnectorId, String srcIP,
			String dstIP, String protocol, int srcPort, int dstPort,
			int packetSize, String diffTime, String upwardTime,
			String downwardTime) {
		try {
			Class.forName("org.sqlite.JDBC", true, Thread.currentThread()
					.getContextClassLoader());
			connection = DriverManager
					.getConnection("jdbc:sqlite:usecpluginhwm:db");
			connection.setAutoCommit(false);
			System.out.println("Opened HWM DB Successfully");
			stmt = connection.createStatement();

			int IDCount;
			IDCount = 0;
			ResultSet rs;
			rs = stmt
					.executeQuery("SELECT ID AS id FROM UsecPluginHWM WHERE ID=(SELECT MAX (ID) FROM UsecPluginHWM);");
			while (rs.next()) {
				IDCount = rs.getInt("id");
			}
			counter = IDCount;

			counter++;

			String counterString = counter.toString();
			counterString = "'" + counterString + "'";
			nodeId = "'" + nodeId + "'";
			nodeConnectorId = "'" + nodeConnectorId + "'";
			srcIP = "'" + srcIP + "'";
			dstIP = "'" + dstIP + "'";
			protocol = "'" + protocol + "'";
			diffTime = "'" + diffTime + "'";
			upwardTime = "'" + upwardTime + "'";
			downwardTime = "'" + downwardTime + "'";

			String sqlite = "INSERT INTO UsecPluginHWM "
					+ "(ID, NodeID, NodeConnectorID, SrcIP, DstIP, "
					+ "Protocol, SrcPort, DstPort, PacketSize, DiffTime, UpwardTime, DownwardTime)"
					+ "VALUES ("
					+ counterString
					+ ", "
					+ nodeId
					+ ", "
					+ nodeConnectorId
					+ ", "
					+ srcIP
					+ ", "
					+ dstIP
					+ ", "
					+ protocol
					+ ", "
					+ srcPort
					+ ", "
					+ dstPort
					+ ", "
					+ packetSize
					+ ", "
					+ diffTime + ", " + upwardTime + ", " + downwardTime + ");";

			stmt.executeUpdate(sqlite);
			stmt.close();
			rs.close();
			connection.commit();
			connection.close();
		} catch (Exception e) {
			System.err.println("Error Occured" + e.getClass().getName() + ": "
					+ e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException se2) {
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
			}
		}

	}
}
