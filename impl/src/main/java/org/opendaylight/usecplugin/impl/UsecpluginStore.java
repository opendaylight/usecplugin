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
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.Security;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.SecurityBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.security.SecurityElements;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.security.SecurityElementsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.security.SecurityElementsKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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
    InstanceIdentifier<Security> instanceIdentifier = InstanceIdentifier.builder(Security.class).build();
    List<SecurityElements> securityElementsList = new ArrayList<>();
    SecurityBuilder securityBuilder = new SecurityBuilder();

    public void addData(String secKey, String nodeId, String nodeConnectorId, String srcIP, String dstIP, String protocol, int srcPort, int dstPort, int packetSize, String uptime, String downtime) {
        WriteTransaction writeTransaction = dataBroker.newWriteOnlyTransaction();
        SecurityElementsBuilder securityElementsBuilder = new SecurityElementsBuilder();
        securityElementsBuilder.setSecKey(secKey);
        securityElementsBuilder.setNodeID(nodeId);
        securityElementsBuilder.setSrcIP(srcIP);
        securityElementsBuilder.setDstIP(dstIP);
        securityElementsBuilder.setProtocol(protocol);
        securityElementsBuilder.setSrcPort(srcPort);
        securityElementsBuilder.setDstPort(dstPort);
        securityElementsBuilder.setPacketSize(packetSize);
        securityElementsBuilder.setUpwardTime(uptime);
        securityElementsBuilder.setDownwardTime(downtime);
        SecurityElements securityElements = securityElementsBuilder.build();
        securityElementsList.add(securityElements);
        securityBuilder.setSecurityElements(securityElementsList);
        Security security = securityBuilder.build();
        writeTransaction.merge(LogicalDatastoreType.OPERATIONAL, instanceIdentifier, security);
        writeTransaction.commit();
        LOG.debug("Data Store Element is Created for key ", secKey);
    }

    public void delData(String secKey) {
        SecurityElementsKey secElemKey = new SecurityElementsKey(secKey);
        InstanceIdentifier<SecurityElements> secElemId = InstanceIdentifier.builder(Security.class).child(SecurityElements.class, secElemKey).toInstance();
        WriteTransaction writeTransaction = dataBroker.newWriteOnlyTransaction();
        writeTransaction.delete(LogicalDatastoreType.OPERATIONAL, secElemId);
        writeTransaction.commit();
        LOG.debug("Data Store Entry is deleted for key ", secKey);
    }

    public void addDownTime(String secKey, String downtime) {
        SecurityElementsKey secElemKey = new SecurityElementsKey(secKey);
        InstanceIdentifier<SecurityElements> secElemId = InstanceIdentifier.builder(Security.class).child(SecurityElements.class, secElemKey).toInstance();
        WriteTransaction writeTransaction = dataBroker.newWriteOnlyTransaction();
        SecurityElementsBuilder securityElementsBuilder = new SecurityElementsBuilder();
        securityElementsBuilder.setSecKey(secKey);
        securityElementsBuilder.setDownwardTime(downtime);
        SecurityElements securityElements = securityElementsBuilder.build();
        securityElementsList.add(securityElements);
        writeTransaction.merge(LogicalDatastoreType.OPERATIONAL, secElemId, securityElements);
        writeTransaction.commit();
        LOG.debug("Downward Time is added for entry with key ", secKey);
    }

}
