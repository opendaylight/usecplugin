/*
 * Copyright (c) 2015 Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.usecplugin.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.LWM;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.LWMBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.lwm.Lowwatermark;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.lwm.LowwatermarkBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.lwm.LowwatermarkKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.HWM;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.HWMBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.hwm.Highwatermark;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.hwm.HighwatermarkBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.hwm.HighwatermarkKey;


/**
 * Created by user on 9/9/15.
 */
public class UsecpluginStore {
    private static final Logger LOG = LoggerFactory.getLogger(UsecpluginStore.class);
    private DataBroker dataBroker;
    public DataBroker getdataBroker() {
        return dataBroker;
    }
    public void setdataBroker(DataBroker dataBroker) {
        this.dataBroker = dataBroker;
    }
    
    public UsecpluginStore(DataBroker dataBroker){
		this.dataBroker = dataBroker;
	}
    
    InstanceIdentifier<LWM> instanceIdentifier = InstanceIdentifier.builder(LWM.class).build();
    List<Lowwatermark> lwmList = new ArrayList<>();
    LWMBuilder lwmBuilder = new LWMBuilder();

    InstanceIdentifier<HWM> instanceIdentifier1 = InstanceIdentifier.builder(HWM.class).build();
    List<Highwatermark> hwmList = new ArrayList<>();
    HWMBuilder hwmBuilder = new HWMBuilder();

    public void addData(String secKey, String nodeId, String nodeConnectorId, String srcIP, String dstIP, String protocol, int srcPort, int dstPort, int packetSize, String uptime, String downtime) {
        WriteTransaction writeTransaction = dataBroker.newWriteOnlyTransaction();
        LowwatermarkBuilder lowWaterMarkBuilder = new LowwatermarkBuilder();
        lowWaterMarkBuilder.setSecKey(secKey);
        lowWaterMarkBuilder.setNodeID(nodeId);
        lowWaterMarkBuilder.setSrcIP(srcIP);
        lowWaterMarkBuilder.setDstIP(dstIP);
        lowWaterMarkBuilder.setProtocol(protocol);
        lowWaterMarkBuilder.setSrcPort(srcPort);
        lowWaterMarkBuilder.setDstPort(dstPort);
        lowWaterMarkBuilder.setPacketSize(packetSize);
        lowWaterMarkBuilder.setUpwardTime(uptime);
        lowWaterMarkBuilder.setDownwardTime(downtime);


        Lowwatermark lwm = lowWaterMarkBuilder.build();
        lwmList.add(lwm);
        lwmBuilder.setLowwatermark(lwmList);
        LWM lwmsec = lwmBuilder.build();
        writeTransaction.merge(LogicalDatastoreType.OPERATIONAL, instanceIdentifier, lwmsec);
        writeTransaction.commit();
        LOG.debug("Data Store Element is Created for key ", secKey);
    }



public void addHWMData(String secKey, String nodeId, String nodeConnectorId, String srcIP, String dstIP, String protocol, int srcPort, int dstPort, int packetSize, String uptime, String downtime) {
     WriteTransaction writeTransaction = dataBroker.newWriteOnlyTransaction();
     HighwatermarkBuilder highWaterMarkBuilder = new HighwatermarkBuilder();
     highWaterMarkBuilder.setSecKey(secKey);
     highWaterMarkBuilder.setNodeID(nodeId);
     highWaterMarkBuilder.setProtocol(protocol);
     highWaterMarkBuilder.setSrcPort(srcPort);
     highWaterMarkBuilder.setDstPort(dstPort);
     highWaterMarkBuilder.setPacketSize(packetSize);
     highWaterMarkBuilder.setUpwardTime(uptime);
     highWaterMarkBuilder.setDownwardTime(downtime);
     highWaterMarkBuilder.setSrcIP(srcIP);
     highWaterMarkBuilder.setDstIP(dstIP);
     Highwatermark hwm = highWaterMarkBuilder.build();
     hwmList.add(hwm);
     hwmBuilder.setHighwatermark(hwmList);
     HWM hwmsec = hwmBuilder.build();
     writeTransaction.merge(LogicalDatastoreType.OPERATIONAL, instanceIdentifier1, hwmsec);
     writeTransaction.commit();
     LOG.debug("Data Store Element is Created for key ", secKey);
 }


public void delData(String secKey) {
    LowwatermarkKey seclwmKey = new LowwatermarkKey(secKey);
    InstanceIdentifier<Lowwatermark> secLwmId = InstanceIdentifier.builder(LWM.class).child(Lowwatermark.class, seclwmKey).toInstance();
    WriteTransaction writeTransaction = dataBroker.newWriteOnlyTransaction();
    writeTransaction.delete(LogicalDatastoreType.OPERATIONAL, secLwmId);
    writeTransaction.commit();
    LOG.debug("Data Store Entry is deleted for key ", secKey);
}


   public void delHwmData(String secKey) {
    	HighwatermarkKey sechwmKey = new HighwatermarkKey(secKey);
        InstanceIdentifier<Highwatermark> secHwmId = InstanceIdentifier.builder(HWM.class).child(Highwatermark.class, sechwmKey).toInstance();
        WriteTransaction writeTransaction = dataBroker.newWriteOnlyTransaction();
        writeTransaction.delete(LogicalDatastoreType.OPERATIONAL, secHwmId);
        writeTransaction.commit();
        LOG.debug("Data Store Entry is deleted for key ", secKey);
    }

   public void addDownTime(String secKey, String downtime) {
       LowwatermarkKey seclwmKey = new LowwatermarkKey(secKey);
       InstanceIdentifier<Lowwatermark> secLwmId = InstanceIdentifier.builder(LWM.class).child(Lowwatermark.class, seclwmKey).toInstance();
       WriteTransaction writeTransaction = dataBroker.newWriteOnlyTransaction();
       LowwatermarkBuilder lowWaterMarkBuilder = new LowwatermarkBuilder();
       lowWaterMarkBuilder.setSecKey(secKey);
       lowWaterMarkBuilder.setDownwardTime(downtime);
       Lowwatermark lwmElements = lowWaterMarkBuilder.build();
       lwmList.add(lwmElements);
       writeTransaction.merge(LogicalDatastoreType.OPERATIONAL, secLwmId, lwmElements);
       writeTransaction.commit();
       LOG.debug("Downward Time is added for entry with key ", secKey);
   }


   public void addHwmDownTime(String secKey, String downtime) {
    	HighwatermarkKey sechwmKey = new HighwatermarkKey(secKey);
    	InstanceIdentifier<Highwatermark> secHwmId = InstanceIdentifier.builder(HWM.class).child(Highwatermark.class, sechwmKey).toInstance();
    	WriteTransaction writeTransaction = dataBroker.newWriteOnlyTransaction();
        HighwatermarkBuilder highWaterMarkBuilder = new HighwatermarkBuilder();
        highWaterMarkBuilder.setSecKey(secKey);
        highWaterMarkBuilder.setDownwardTime(downtime);
        Highwatermark hwmElements = highWaterMarkBuilder.build();
        hwmList.add(hwmElements);
        writeTransaction.merge(LogicalDatastoreType.OPERATIONAL, secHwmId, hwmElements);
        writeTransaction.commit();
        LOG.debug("Downward Time is added for entry with key ", secKey);
    }
    

}
