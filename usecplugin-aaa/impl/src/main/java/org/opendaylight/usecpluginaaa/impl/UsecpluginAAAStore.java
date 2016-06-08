/*
 * Copyright (c) 2015 Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.usecpluginaaa.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.Security;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.SecurityBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.security.Loginattempts;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.security.LoginattemptsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecpluginaaa.rev150105.security.LoginattemptsKey;

import com.google.common.util.concurrent.CheckedFuture;

/**
 * Created by user on 5/5/16.
 */
public class UsecpluginAAAStore {
	private static final Logger LOG = LoggerFactory
			.getLogger(UsecpluginAAAStore.class);
	static private DataBroker dataBroker;

	public DataBroker getdataBroker() {
		return dataBroker;
	}

	public void setdataBroker(DataBroker dataBroker) {
		this.dataBroker = dataBroker;
	}

	List<Loginattempts> loginattemptlist = new ArrayList<>();
	SecurityBuilder securitybuilder = new SecurityBuilder();

	public void addData(String srcIP, String time, String attempt) {
		LoginattemptsBuilder loginattemptsBuilder = new LoginattemptsBuilder();
		LoginattemptsKey key = new LoginattemptsKey(srcIP);
		loginattemptsBuilder.setKey(key);
		InstanceIdentifier instanceIdentifier = InstanceIdentifier
				.builder(Security.class)
				.child(Loginattempts.class, loginattemptsBuilder.getKey())
				.build();

		WriteTransaction writeTransaction = dataBroker
				.newWriteOnlyTransaction();

		loginattemptsBuilder.setTime(time);
		loginattemptsBuilder.setAttempt(attempt);

		Loginattempts loginattempt = loginattemptsBuilder.build();
		loginattemptlist.add(loginattempt);
		securitybuilder.setLoginattempts(loginattemptlist);
		Security security = securitybuilder.build();

		writeTransaction.put(LogicalDatastoreType.OPERATIONAL,
				instanceIdentifier, loginattempt);
		writeTransaction.commit();

		LOG.debug("Data Store Element is Created for key ", srcIP);

		for (Loginattempts loginattempts : loginattemptlist) {
			System.out.println(loginattempts.getTime() + " "
					+ loginattempts.getSrcIP() + loginattempts.getAttempt());
			LOG.info(" " + loginattempts.getTime() + " "
					+ loginattempts.getSrcIP() + loginattempts.getAttempt());
		}
		LOG.info("[END]writeLoginattempts");
	}

}
