/*
 * Copyright (c) 2015 Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.usecplugin.impl;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Future;
import java.sql.*;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketReceived;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.UsecpluginService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.AttackIDInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.AttackIDOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.AttackIDOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.AttacksFromIPInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.AttacksFromIPOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.AttacksFromIPOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.AttacksToIPInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.AttacksToIPOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.AttacksToIPOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.AttacksInTimeInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.AttacksInTimeOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.rev150105.AttacksInTimeOutputBuilder;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsecpluginRPCImpl implements UsecpluginService {
    private static final Logger LOG = LoggerFactory.getLogger(UsecpluginRPCImpl.class);
    Connection connection = null;
    Statement stmt = null;
    static boolean Testvalue=false;
    AttackIDOutputBuilder attackBuilder = new AttackIDOutputBuilder();
    AttacksFromIPOutputBuilder attack1Builder = new AttacksFromIPOutputBuilder();
    AttacksToIPOutputBuilder attack2Builder = new AttacksToIPOutputBuilder();

    // AttackID
    @Override
    public Future<RpcResult<AttackIDOutput>> attackID(AttackIDInput input) {
        LOG.info("AttackID_RPC Implementation Initiated");
        try {
            if(Testvalue == true)
	    {
        //Edit this loop with the path to the usecplugin:db in your system
	    connection = DriverManager.getConnection("jdbc:sqlite:usecplugin:db");
	    }
	    else
	    {
	    Testvalue = false;
	    connection = DriverManager.getConnection("jdbc:sqlite:usecplugin:db");       
	    }
            
            connection.setAutoCommit(false);
            LOG.info("Opened DB Successfully");
            stmt = connection.createStatement();
            String nodeID = input.getNodeID();
            String s = "Select  NodeID, Count(*) as ATTACKS from UsecPluginLWM WHERE NodeID = '" + nodeID + "' " ;
            ResultSet rs = stmt.executeQuery(s);
            while(rs.next()){
            attackBuilder.setNoOfAttacks(rs.getInt("ATTACKS"));
            } 
        stmt.close();
        connection.commit();
        connection.close();
       }
       catch (Exception e) {
    	   System.err.println("Error Occured..." + e.getClass().getName() + ": " + e.getMessage());
    	   e.printStackTrace();
       }
       return RpcResultBuilder.success(attackBuilder.build()).buildFuture();  
    }
 
    //AttacksFromIP
    
    @Override
    public Future<RpcResult<AttacksFromIPOutput>> attacksFromIP(AttacksFromIPInput input) {
    	LOG.info("AttacksFromIP_RPC Implementation Initiated");
        try {
            if(Testvalue == true)
            {
            //Edit this loop with the path to the usecplugin:db in your system
            connection = DriverManager.getConnection("jdbc:sqlite:usecplugin:db");
            }
            else
            {
            Testvalue = false;
            connection = DriverManager.getConnection("jdbc:sqlite:usecplugin:db");   
            }
            connection.setAutoCommit(false);
            LOG.info("Opened DB Successfully");
            stmt = connection.createStatement();
            String srcIP = input.getSrcIP();
            String s1 = "SELECT  SrcIP, Count(*) as ATTACKS FROM UsecPluginLWM WHERE SrcIP = '" + srcIP + "'  ";
            ResultSet rs = stmt.executeQuery(s1);
            while(rs.next()){                                                    
       	        attack1Builder.setNoOfAttacks(rs.getInt("ATTACKS"));
            } 
            stmt.close();
            connection.commit();
            connection.close();
        }
        catch (Exception e) {
            System.err.println("Error Occured..." + e.getClass().getName() + ": " + e.getMessage());
    	    e.printStackTrace();
        }
        return RpcResultBuilder.success(attack1Builder.build()).buildFuture();
    }
    //AttacksToIP
    
    @Override
    public Future<RpcResult<AttacksToIPOutput>> attacksToIP(AttacksToIPInput input) {
      	LOG.info("AttacksToIP_RPC Implementation Initiated");
        try {
            if(Testvalue == true)
            {
            //Edit this loop with the path to the usecplugin:db in your system
            connection = DriverManager.getConnection("jdbc:sqlite:usecplugin:db");
            }
            else
            {
            Testvalue = false;
            connection = DriverManager.getConnection("jdbc:sqlite:usecplugin:db");   
            }
            connection.setAutoCommit(false);
            LOG.info("Opened DB Successfully");
            stmt = connection.createStatement();
       
            String dstIP = input.getDstIP();
       
            String s1 = "SELECT  DstIP, Count(*) as ATTACKS FROM UsecPluginLWM WHERE DstIP = '" + dstIP + "'  ";
            ResultSet rs = stmt.executeQuery(s1);
            while(rs.next()) {                                                      
    	        attack2Builder.setNoOfAttacks(rs.getInt("ATTACKS"));
            } 
            stmt.close();
            connection.commit();
            connection.close();
           }
       catch (Exception e) {
    	   System.err.println("Error Occured..." + e.getClass().getName() + ": " + e.getMessage());
    	   e.printStackTrace();
    	   }
       return RpcResultBuilder.success(attack2Builder.build()).buildFuture();
    }
    
    //AttacksInTime
/*====Commented by Rafat.Will be uncommented later======================*/

    @Override
    public Future<RpcResult<AttacksInTimeOutput>> attacksInTime(AttacksInTimeInput input) {
		return null;
    	
    }

}

