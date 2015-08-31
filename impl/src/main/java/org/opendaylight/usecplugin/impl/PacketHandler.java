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
