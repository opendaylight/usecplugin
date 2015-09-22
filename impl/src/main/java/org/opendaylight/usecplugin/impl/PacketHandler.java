/*
 * Copyright (c) 2015 Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.usecplugin.impl;

import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketProcessingListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketReceived;

public class PacketHandler implements PacketProcessingListener {

	/**
	 * default constructor
	 */

	@Override
	public void onPacketReceived(PacketReceived notification) {
		System.out.println("Packet" + notification.getPayload());
	}

}
