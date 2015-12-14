/*
 * Copyright (c) 2015 Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.usecplugin.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketProcessingListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketReceived;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.SampleDataHwm;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PacketHandler_HWM implements PacketProcessingListener {

	private DataBroker dataBroker;

	String srcIP, dstIP, IPProtocol, srcMac, dstMac;
	int counter, packetSize;
	float avgPacketInRate;
	Calendar calendar = Calendar.getInstance();
	Long oldTime = calendar.getTimeInMillis();
	Long newTime, timeDiff;

	int highWaterMark = 2000;
	int samples = 2000;
	String stringEthType;
	Integer srcPort, dstPort;
	NodeConnectorRef ingressNodeConnectorRef;
	NodeId ingressNodeId;
	NodeConnectorId ingressNodeConnectorId;
	String ingressConnector, ingressNode;
	byte[] srcMacRaw, dstMacRaw, rawIPProtocol, rawEthType, rawSrcPort,
			rawDstPort, payload, srcIPRaw, dstIPRaw;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String upwardTime, downwardTime, diffTimeString;
	Long upTime, downTime, currentTime;
	Boolean notificationSent = false;
	Double diffTime;
	Boolean HWMBreach = false;

	List<String> dataBaseKeyList = new ArrayList<String>();

	UsecpluginStore usecpluginStore = new UsecpluginStore();
	UsecpluginDatabaseHWM databaseHwm = new UsecpluginDatabaseHWM();
	private static final Logger LOG = LoggerFactory
			.getLogger(PacketHandler_HWM.class);

	public DataBroker getdataBroker() {
		return dataBroker;
	}

	public void setdataBroker(DataBroker dataBroker) {
		this.dataBroker = dataBroker;

	}

	public void dbHwm() {
		databaseHwm.dbHwm();
	}
	
	public  void getHWMSample()	{			
	   try {	 		
			 
	        InstanceIdentifier<SampleDataHwm> identifierList = InstanceIdentifier.builder(SampleDataHwm.class).build();
	        ReadOnlyTransaction tx = dataBroker.newReadOnlyTransaction();
            samples = tx.read(LogicalDatastoreType.CONFIGURATION, identifierList).checkedGet().get().getSamples();
            highWaterMark = tx.read(LogicalDatastoreType.CONFIGURATION, identifierList).checkedGet().get().getHighWaterMark();
	        } catch (Exception e) {
	            LOG.error(" failed:HWM ", e);
	        }		
	}

	@Override
	public void onPacketReceived(PacketReceived notification) {
		LOG.debug("High Water Mark is ", highWaterMark);
		counter = counter + 1;
		if ((counter % samples) == 0) {
			calendar = Calendar.getInstance();
			newTime = calendar.getTimeInMillis();
			timeDiff = newTime - oldTime;
			oldTime = newTime;
			avgPacketInRate = (samples / timeDiff) * 1000;
			LOG.debug("Average PacketIn Rate is ", avgPacketInRate);
		}

		if (avgPacketInRate > highWaterMark) {

			usecpluginStore.setdataBroker(dataBroker);
			calendar = Calendar.getInstance();
			downwardTime = "0";
			HWMBreach = true;

			// ////////////////////////////////////////////////////////////
			// Packet Parsing

			ingressNodeConnectorRef = notification.getIngress();
			ingressNodeConnectorId = InventoryUtility
					.getNodeConnectorId(ingressNodeConnectorRef);
			ingressConnector = ingressNodeConnectorId.getValue();
			ingressNodeId = InventoryUtility.getNodeId(ingressNodeConnectorRef);
			ingressNode = ingressNodeId.getValue();
			payload = notification.getPayload();
			packetSize = payload.length;
			srcMacRaw = PacketParsing.extractSrcMac(payload);
			dstMacRaw = PacketParsing.extractDstMac(payload);
			srcMac = PacketParsing.rawMacToString(srcMacRaw);
			dstMac = PacketParsing.rawMacToString(dstMacRaw);
			rawEthType = PacketParsing.extractEtherType(payload);
			stringEthType = PacketParsing.rawEthTypeToString(rawEthType);
			dstIPRaw = PacketParsing.extractDstIP(payload);
			srcIPRaw = PacketParsing.extractSrcIP(payload);
			dstIP = PacketParsing.rawIPToString(dstIPRaw);
			srcIP = PacketParsing.rawIPToString(srcIPRaw);
			rawIPProtocol = PacketParsing.extractIPProtocol(payload);
			IPProtocol = PacketParsing.rawIPProtoToString(rawIPProtocol)
					.toString();
			rawSrcPort = PacketParsing.extractSrcPort(payload);
			srcPort = PacketParsing.rawPortToInteger(rawSrcPort);
			rawDstPort = PacketParsing.extractDstPort(payload);
			dstPort = PacketParsing.rawPortToInteger(rawDstPort);

			// ////////////////////////////////////////////////////////////////////////////
			String databaseKey = ingressNode + "-" + dstIP;
			if (!(dataBaseKeyList.contains(databaseKey))) {
				dataBaseKeyList.add(databaseKey);

				upTime = calendar.getTimeInMillis();
				upwardTime = dateFormat.format(upTime);
				usecpluginStore.addHWMData(databaseKey, ingressNode,
						ingressConnector, srcIP, dstIP, IPProtocol, srcPort,
						dstPort, packetSize, upwardTime, downwardTime);

				System.out.println("Information Stored in HWM Data Store is "
						+ ingressNode + ingressConnector + srcIP + dstIP
						+ IPProtocol + srcPort + dstPort + packetSize
						+ upwardTime + downwardTime);

			}

			else if (dataBaseKeyList.contains(databaseKey) && !notificationSent) {
				notificationSent = true;
				currentTime = calendar.getTimeInMillis();
				if (upTime != 0) {
					LOG.debug("HWMBreach Notification will be raised");
				}
			}
		}

		else if ((avgPacketInRate < highWaterMark) && HWMBreach) {
			HWMBreach = false;

			for (String dbKey : dataBaseKeyList) {
				downTime = calendar.getTimeInMillis();
				downwardTime = dateFormat.format(downTime);
				usecpluginStore.addHwmDownTime(dbKey, downwardTime);

			}
		}

		else if ((avgPacketInRate < highWaterMark) && !HWMBreach) {
			notificationSent = false; // for generation of new notifications on
										// new HWM breach
			currentTime = calendar.getTimeInMillis();
			if (downTime != null) {
				for (String dbKey : dataBaseKeyList) {
					diffTime = (downTime - upTime) * 0.001;
					diffTimeString = diffTime.toString();

					databaseHwm.dbWrite(ingressNode, ingressConnector, srcIP,
							dstIP, IPProtocol, srcPort, dstPort, packetSize,
							diffTimeString, upwardTime, downwardTime);
					usecpluginStore.delHwmData(dbKey);
				}

				if (!dataBaseKeyList.isEmpty()) {
					dataBaseKeyList.clear();
					LOG.debug("DataBase Entry is Cleared");
				}

			}
		}
	}
}

