/*
 * Copyright (c) 2015 Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.usecplugin.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketProcessingListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketReceived;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.SampleDataLwm;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.SampleDataHwm;

import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.UsecpluginListener;
import org.opendaylight.controller.sal.binding.api.NotificationProviderService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.LowWaterMarkBreachedBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PacketHandler implements PacketProcessingListener {

	static Integer counter1 = 0;
	
	 //static int idCount = 0;
	
	private DataBroker dataBroker;
	// Counter of Packet_Ins; Size of Packet_In
	int counter, packetSize;
	// Average Packet_In Rate
	float avgPacketInRate;
	// Calendar instance
	Calendar calendar = Calendar.getInstance();
	// Reference Time
	Long oldTime = calendar.getTimeInMillis();
	// Time at Samples number of packets arrives
	Long newTime, timeDiff;
	// Low Water Mark for Packet Received Rate
	int lowWaterMark = 1000;
	// High Water Mark for Packet Received Rate
	int highWaterMark = 2000;
	// Samples determines number of packets received
	int samplesLwm = 1000;
	int samplesHwm = 2000;

	// SourceIPAddress, DestinationIPAddress, ipProtocol, Src and Dst Mac
	// Addresses
	String srcIP, dstIP, ipProtocol, srcMac, dstMac;
	// Ether Type as String
	String stringEthType;
	// TCP or UDP Source and Destination Ports
	Integer srcPort, dstPort;
	// Reference to OpenFlow Plugin Yang DataStore
	NodeConnectorRef ingressNodeConnectorRef;
	// Ingress Switch Id
	NodeId ingressNodeId;
	// Ingress Switch Port Id from DataStore
	NodeConnectorId ingressNodeConnectorId;
	// Ingress Switch Port and Switch
	String ingressConnector, ingressNode;
	byte[] payload, srcMacRaw, dstMacRaw, srcIPRaw, dstIPRaw, rawIPProtocol, rawEthType, rawSrcPort, rawDstPort;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// Low Water Mark Breach - Upward and Downward Times
	String upwardTime, downwardTime, diffTimeString;
	// Time Interval Above LWM and Below LWM
	Long upTime, downTime, currentTime;
	// Flag for LWM Breach
	Boolean lwmBreach = false;
	Boolean hwmBreach = false;
    Boolean Data =  false;
	// Flag for LWM Breach Notification
	Boolean notificationSent = false;
	// Yang DataStore Key List
	List<String> dataBaseKeyList = new ArrayList<String>();
	// For Yang Data Store Creation
	UsecpluginStore usecpluginStore = new UsecpluginStore(dataBroker);
	Double diffTime;
	
	private static final Logger LOG = LoggerFactory.getLogger(PacketHandler.class);

	NotificationProviderService notificationProviderService;
	NotificationPublishService notificationPublishService;
	UsecpluginListener usecpluginListener;
	
	PrintWriter restoreNo;

	public DataBroker getdataBroker() {
		return dataBroker;
	}

	public void setdataBroker(DataBroker dataBroker) {
		this.dataBroker = dataBroker;
	}

	public void setNotificationProviderService(NotificationProviderService notificationProviderService) {
		this.notificationProviderService = notificationProviderService;
	}

	public void setNotificationPublisService(NotificationPublishService notificationPublisService) {
		this.notificationPublishService = notificationPublisService;
	}

	public void getLWMSample() {
		try {

			LOG.info("samplesLwm...." + samplesLwm);

			InstanceIdentifier<SampleDataLwm> identifierList = InstanceIdentifier.builder(SampleDataLwm.class).build();
			ReadOnlyTransaction tx = dataBroker.newReadOnlyTransaction();
			samplesLwm = tx.read(LogicalDatastoreType.CONFIGURATION, identifierList).checkedGet().get().getSamplesLwm();
			lowWaterMark = tx.read(LogicalDatastoreType.CONFIGURATION, identifierList).checkedGet().get()
					.getLowWaterMarkLwm();
		} catch (Exception e) {
			LOG.info("failed: LWM ", e);
		}
	}

	

	public void getHWMSample() {
		try {

			LOG.info("samplesHwm...." + samplesHwm);

			InstanceIdentifier<SampleDataHwm> identifierList = InstanceIdentifier.builder(SampleDataHwm.class).build();
			ReadOnlyTransaction tx = dataBroker.newReadOnlyTransaction();
			samplesHwm = tx.read(LogicalDatastoreType.CONFIGURATION, identifierList).checkedGet().get().getSamples();
			highWaterMark = tx.read(LogicalDatastoreType.CONFIGURATION, identifierList).checkedGet().get()
					.getHighWaterMark();
		} catch (Exception e) {
			LOG.info(" failed:HWM ", e);
		}
	}

	public void Lwm() {
		
		

		LOG.debug("Low Water Mark is ", lowWaterMark);
		counter = counter + 1;

		if ((counter % samplesLwm) == 0) {
			calendar = Calendar.getInstance();
			newTime = calendar.getTimeInMillis();
			timeDiff = newTime - oldTime;
			oldTime = newTime;
			avgPacketInRate = (samplesLwm / timeDiff) * 1000;
			LOG.debug("Average PacketIn Rate is ", avgPacketInRate);
		}

		if (avgPacketInRate > lowWaterMark) {

			if (lwmBreach.equals(false)) {
				LowWaterMarkBreachedBuilder lowWaterMarkBreachedBuilder = new LowWaterMarkBreachedBuilder();
				notificationProviderService.publish(lowWaterMarkBreachedBuilder.build());
			}

			usecpluginStore.setdataBroker(dataBroker);
			calendar = Calendar.getInstance();
			downwardTime = "0";
			lwmBreach = true;

			String databaseKey = ingressNode + "-" + srcIP;
			// To avoid repetitive addition of dataBaseKey to the
			// dataBaseKeyList
			if (!(dataBaseKeyList.contains(databaseKey))) {
				dataBaseKeyList.add(databaseKey);
				upTime = calendar.getTimeInMillis();
				upwardTime = dateFormat.format(upTime);
				usecpluginStore.addData(databaseKey, ingressNode, ingressConnector, srcIP, dstIP, ipProtocol, srcPort,
						dstPort, packetSize, upwardTime, downwardTime);
			}
			// Avoiding multiple notifications
			else if (dataBaseKeyList.contains(databaseKey) && !notificationSent) {
				notificationSent = true;
				currentTime = calendar.getTimeInMillis();
				// Avoiding runtime error of nullpointer exception for upTime.
				// Notification raised only when avg rate is above LWM for more
				// than 5 seconds
				if ((upTime != 0) && ((currentTime - upTime) > 5000)) {
					LOG.debug("lwmBreach Notification will be raised");
				}
			}
		}
		// Avg Rate goes below LWM
		else if ((avgPacketInRate < lowWaterMark) && lwmBreach) {
			lwmBreach = false;
			// Add Down Time when the rate goes below LWM to the DataStore
			for (String dbKey : dataBaseKeyList) {
				downTime = calendar.getTimeInMillis();
				downwardTime = dateFormat.format(downTime);
				usecpluginStore.addDownTime(dbKey, downwardTime);
			}
		}
		// DataStore entry is cleared only when the avg rate stays below LWM for
		// atleast 5 secs.
		else if ((avgPacketInRate < lowWaterMark) && !lwmBreach) {
			// for generation of new notifications on new LWM breach
			notificationSent = false;
			currentTime = calendar.getTimeInMillis();
			if ((downTime != null) && ((currentTime - downTime) > 5000)) {

				for (String dbKey : dataBaseKeyList) {
					diffTime = (downTime - upTime) * 0.001;
					diffTimeString = diffTime.toString();
					
					
					
					
					 String d = System.getProperty("user.home");
				
					 final File file = new File(d + File.separator+ "AttacksDataLwm");
					 file.mkdirs();// all directories down
					
					 File log = new File(file , "DataLogLwm.txt");
					 
					// System.out.println("log..." + log);
					 
							    try{
							    if(log.exists()==false){
							            LOG.info("Creating a new lwm file.");
							            log.createNewFile();
							    }
					
					 
					   PrintWriter out = new PrintWriter(new FileWriter(log, true));
						  //  out.println( "abc" );
						    
						          
				            
				           // counter1=idCount;
				            counter1++;
				            String counterString = counter1.toString();
				            counterString = "'" + counterString +"'";
				            if (Data.equals(false)) {
				            	
				           // out.append("CounterString" +"  " + "IngressNode" +"  " + "IngressConnector" +"  " + "SrcIP" +"  " + "DstIP" + "  " + "Protocol" +"  " + " SrcPort" +"  " +  "DstPort" + "  " + " PacketSize" + "  "  +  "DiffTime" + "  " + " UpwardTime" + "  "  +  "DownwardTime" +  "\n" );
					         out.append("CounterString" +"  " + "IngressNode" +"  " + "IngressConnector" +"   " + "SrcIP" +"      " + "DstIP" + "  " + "Protocol" +"  " + " SrcPort" +"  " +  "DstPort" + "  " + " PacketSize" + "  "  +  "DiffTime" + " " + " UpwardTime" + " "  +  "DownwardTime" +  "\n" );
}
				            
				            Data = true;
				            
						   // out.append("  " +counterString +"  " +ingressNode +"  " + ingressConnector +"  " + srcIP +"  " + dstIP + "  " + ipProtocol+ "  " + srcPort + "  " + dstPort + "  " + packetSize + "  " + diffTime + "  " + upwardTime + "  " + downwardTime +  "\n" );
						    out.append("    " +counterString +"        " +ingressNode +"     " + ingressConnector +"   " + srcIP +"   " + dstIP + "    " + ipProtocol+ "         " + srcPort + "    " + dstPort + "       " + packetSize + "        " + diffTime + "   " + upwardTime + "   " + downwardTime +  "\n" );
                            out.close();
						    LOG.info("done lwm");
							    }catch(IOException e){
							        System.out.println("COULD NOT LOG!!");
							    }
					
					usecpluginStore.delData(dbKey);
				}
				// clearing the dataBaseKeyList
				if (!dataBaseKeyList.isEmpty()) {
					dataBaseKeyList.clear();
					LOG.debug("DataBase Entry is Cleared");
				}
			}
		}

	}

	public void Hwm()

	{
	
		
		LOG.debug("High Water Mark is ", highWaterMark);
		counter = counter + 1;
		if ((counter % samplesHwm) == 0) {
			calendar = Calendar.getInstance();
			newTime = calendar.getTimeInMillis();
			timeDiff = newTime - oldTime;
			oldTime = newTime;
			avgPacketInRate = (samplesHwm / timeDiff) * 1000;
			LOG.debug("Average PacketIn Rate is ", avgPacketInRate);
		}

		if (avgPacketInRate > highWaterMark) {

			usecpluginStore.setdataBroker(dataBroker);
			calendar = Calendar.getInstance();
			downwardTime = "0";
			hwmBreach = true;

			String databaseKey = ingressNode + "-" + dstIP;
			if (!(dataBaseKeyList.contains(databaseKey))) {
				dataBaseKeyList.add(databaseKey);

				upTime = calendar.getTimeInMillis();
				upwardTime = dateFormat.format(upTime);
				usecpluginStore.addHWMData(databaseKey, ingressNode, ingressConnector, srcIP, dstIP, ipProtocol,
						srcPort, dstPort, packetSize, upwardTime, downwardTime);

			}

			else if (dataBaseKeyList.contains(databaseKey) && !notificationSent) {
				notificationSent = true;
				currentTime = calendar.getTimeInMillis();
				if (upTime != 0) {
					LOG.debug("HWMBreach Notification will be raised");
				}
			}
		}

		else if ((avgPacketInRate < highWaterMark) && hwmBreach) {
			hwmBreach = false;

			for (String dbKey : dataBaseKeyList) {
				downTime = calendar.getTimeInMillis();
				downwardTime = dateFormat.format(downTime);
				usecpluginStore.addHwmDownTime(dbKey, downwardTime);

			}
		}

		else if ((avgPacketInRate < highWaterMark) && !hwmBreach) {
			// for generation of new notifications on new HWM breach
			notificationSent = false;
			currentTime = calendar.getTimeInMillis();
			if (downTime != null) {
				for (String dbKey : dataBaseKeyList) {
					diffTime = (downTime - upTime) * 0.001;
					diffTimeString = diffTime.toString();

					String d = System.getProperty("user.home");
					
					 final File file = new File(d + File.separator+ "AttacksDataHwm");
					 file.mkdirs();// all directories down
					
					 File log = new File(file , "DataLogHwm.txt");
					 
					// System.out.println("log..." + log);
					 
							    try{
							    if(log.exists()==false){
							            LOG.info("Creating a new hwm file.");
							            log.createNewFile();
							    }
					
					 
					   PrintWriter out = new PrintWriter(new FileWriter(log, true));
						  //  out.println( "abc" );
						    
						          
				            
				           // counter1=idCount;
				            counter1++;
				            String counterString = counter1.toString();
				            counterString = "'" + counterString +"'";
				            if (Data.equals(false)) {
				            	
				           // out.append("CounterString" +"  " + "IngressNode" +"  " + "IngressConnector" +"  " + "SrcIP" +"  " + "DstIP" + "  " + "Protocol" +"  " + " SrcPort" +"  " +  "DstPort" + "  " + " PacketSize" + "  "  +  "DiffTime" + "  " + " UpwardTime" + "  "  +  "DownwardTime" +  "\n" );
					         out.append("CounterString" +"  " + "IngressNode" +"  " + "IngressConnector" +"   " + "SrcIP" +"      " + "DstIP" + "  " + "Protocol" +"  " + " SrcPort" +"  " +  "DstPort" + "  " + " PacketSize" + "  "  +  "DiffTime" + " " + " UpwardTime" + " "  +  "DownwardTime" +  "\n" );
}
				            
				            Data = true;
				            
						   // out.append("  " +counterString +"  " +ingressNode +"  " + ingressConnector +"  " + srcIP +"  " + dstIP + "  " + ipProtocol+ "  " + srcPort + "  " + dstPort + "  " + packetSize + "  " + diffTime + "  " + upwardTime + "  " + downwardTime +  "\n" );
						    out.append("    " +counterString +"        " +ingressNode +"     " + ingressConnector +"   " + srcIP +"   " + dstIP + "    " + ipProtocol+ "         " + srcPort + "    " + dstPort + "       " + packetSize + "        " + diffTime + "   " + upwardTime + "   " + downwardTime +  "\n" );
                           out.close();
						    LOG.info("done hwm");
							    }catch(IOException e){
							        System.out.println("COULD NOT LOG!!");
							    }

					usecpluginStore.delHwmData(dbKey);
				}

				if (!dataBaseKeyList.isEmpty()) {
					dataBaseKeyList.clear();
					LOG.debug("DataBase Entry is Cleared");
				}

			}
		}

	}

	@Override
	public void onPacketReceived(PacketReceived notification) {
		
		//Packet Parsing
		
	//	========================================================================================

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
		ipProtocol = PacketParsing.rawIPProtoToString(rawIPProtocol).toString();
		rawSrcPort = PacketParsing.extractSrcPort(payload);
		srcPort = PacketParsing.rawPortToInteger(rawSrcPort);
		rawDstPort = PacketParsing.extractDstPort(payload);
		dstPort = PacketParsing.rawPortToInteger(rawDstPort);
		
	//	============================================================================================

		Lwm();
		Hwm();

	}

}
