/*
 * Copyright (c) 2015 Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.usecplugin.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketProcessingListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketReceived;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PacketHandler implements PacketProcessingListener {

	private DataBroker dataBroker;
	int counter, packetSize; 									//Counter of Packet_Ins; Size of Packet_In
	float avgPacketInRate; 										//Average Packet_In Rate
	Calendar calendar = Calendar.getInstance(); 				//Calendar instance
	Long oldTime = calendar.getTimeInMillis(); 					//Reference Time
	Long newTime, timeDiff;										//Time at Samples number of packets arrives
	int lowWaterMark = 1000;									//Low Water Mark for Packet Received Rate
	int highWaterMark = 50000; 									//High Water Mark for Packet Received Rate
	int samples = 1000; 										//Samples determines number of packets received
	String srcIP, dstIP, IPProtocol, srcMac, dstMac;			//SourceIPAddress, DestinationIPAddress, IPProtocol, Src and Dst Mac Addresses
	String stringEthType;										//Ether Type as String
	Integer srcPort, dstPort;									//TCP or UDP Source and Destination Ports
	NodeConnectorRef ingressNodeConnectorRef;					//Reference to OpenFlow Plugin Yang DataStore
	NodeId ingressNodeId;										//Ingress Switch Id
	NodeConnectorId ingressNodeConnectorId;						//Ingress Switch Port Id from DataStore
	String ingressConnector, ingressNode;						//Ingress Switch Port and Switch
	byte[] payload, srcMacRaw, dstMacRaw, srcIPRaw, dstIPRaw, rawIPProtocol, rawEthType,rawSrcPort, rawDstPort;
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	String upwardTime, downwardTime, diffTimeString;							//Low Water Mark Breach - Upward and Downward Times
	Long upTime, downTime, currentTime;							//Time Interval Above LWM and Below LWM
	Boolean LWMBreach = false;									//Flag for LWM Breach
	Boolean notificationSent = false;							//Flag for LWM Breach Notification
	List<String> dataBaseKeyList = new ArrayList<String>();		//Yang DataStore Key List
	UsecpluginStore usecpluginStore = new UsecpluginStore();	//For Yang Data Store Creation
	Double diffTime;
	UsecpluginDataBase usecpluginDataBase = new UsecpluginDataBase();
	private static final Logger LOG = LoggerFactory.getLogger(PacketHandler.class);

	public DataBroker getdataBroker() {
		return dataBroker;
	}

	public void setdataBroker(DataBroker dataBroker) {
		this.dataBroker = dataBroker;
	}

	public void dbOpen(){ usecpluginDataBase.dbOpen(); }

	@Override
	public void onPacketReceived(PacketReceived notification) {

		LOG.debug("Low Water Mark is ", lowWaterMark);
		counter = counter + 1;

		if ((counter % samples) == 0) {
			calendar = Calendar.getInstance();
			newTime = calendar.getTimeInMillis();
			timeDiff = newTime - oldTime;
			oldTime = newTime;
			avgPacketInRate = (samples / timeDiff) * 1000; 		//Average Rate of PacketIns; time_diff in ms;
			LOG.debug("Average PacketIn Rate is ", avgPacketInRate);
		}

		if (avgPacketInRate > lowWaterMark) {

			usecpluginStore.setdataBroker(dataBroker);
			calendar = Calendar.getInstance();
			downwardTime = "0";
			LWMBreach = true;

//////////////////////////////////////////////////////////////
			//Packet Parsing
			ingressNodeConnectorRef = notification.getIngress();
			ingressNodeConnectorId = InventoryUtility.getNodeConnectorId(ingressNodeConnectorRef);
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
			IPProtocol = PacketParsing.rawIPProtoToString(rawIPProtocol).toString();
			rawSrcPort = PacketParsing.extractSrcPort(payload);
			srcPort = PacketParsing.rawPortToInteger(rawSrcPort);
			rawDstPort = PacketParsing.extractDstPort(payload);
			dstPort = PacketParsing.rawPortToInteger(rawDstPort);
//////////////////////////////////////////////////////////////////////////////

			String databaseKey = ingressNode + "-" + srcIP;
			// To avoid repetitive addition of dataBaseKey to the dataBaseKeyList
			if (!(dataBaseKeyList.contains(databaseKey))) {
				dataBaseKeyList.add(databaseKey);

				upTime = calendar.getTimeInMillis();
				upwardTime = dateFormat.format(upTime); //for storing in DataStore
				usecpluginStore.addData(databaseKey, ingressNode, ingressConnector, srcIP, dstIP,
						IPProtocol, srcPort, dstPort, packetSize, upwardTime, downwardTime);
//				LOG.debug("Information Stored in Data Store is ", ingressNode, ingressConnector, srcIP, dstIP,IPProtocol, srcPort, dstPort, packetSize, upwardTime, downwardTime);
				System.out.println("Information Stored in Data Store is "+ ingressNode+ ingressConnector+ srcIP+ dstIP+
						IPProtocol+ srcPort+ dstPort+ packetSize+ upwardTime+ downwardTime);

			}

			//Avoiding multiple notifications
			else if (dataBaseKeyList.contains(databaseKey) && !notificationSent) {
				notificationSent = true;
				currentTime =  calendar.getTimeInMillis();
				//Avoiding runtime error of nullpointer exception for upTime.
				//Notification raised only when avg rate is above LWM for more than 5 seconds
				if ((upTime != 0) && ((currentTime - upTime) > 5000)) {
					LOG.debug("LWMBreach Notification will be raised");
				}
			}
		}
		//Avg Rate goes below LWM
		else if ((avgPacketInRate < lowWaterMark) && LWMBreach) {
			LWMBreach = false;
		//Add Down Time when the rate goes below LWM to the DataStore
			for (String dbKey : dataBaseKeyList) {
				downTime = calendar.getTimeInMillis();
				downwardTime = dateFormat.format(downTime);
				usecpluginStore.addDownTime(dbKey, downwardTime);
			}
		}
		//DataStore entry is cleared only when the avg rate stays below LWM for atleast 5 secs.
		else if ((avgPacketInRate < lowWaterMark) && !LWMBreach) {
			notificationSent = false; //for generation of new notifications on new LWM breach
			currentTime = calendar.getTimeInMillis();
			if (downTime != null) {
				if ((currentTime - downTime) > 5000) {
					for (String dbKey : dataBaseKeyList) {
						diffTime = (downTime - upTime) * 0.001;
						diffTimeString = diffTime.toString();
						usecpluginDataBase.dbWrite(ingressNode, ingressConnector, srcIP, dstIP,
								IPProtocol, srcPort, dstPort, packetSize, diffTimeString, upwardTime, downwardTime);
						usecpluginStore.delData(dbKey);

					}
					//clearing the dataBaseKeyList
					if (!dataBaseKeyList.isEmpty()) {
						dataBaseKeyList.clear();
						LOG.debug("DataBase Entry is Cleared");
					}
				}
			}
		}
	}
}
