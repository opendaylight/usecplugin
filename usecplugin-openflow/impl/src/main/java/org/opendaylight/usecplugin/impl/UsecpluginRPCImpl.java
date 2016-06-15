/*
 * Copyright (c) 2015 Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.usecplugin.impl;

import java.util.Scanner;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsecpluginRPCImpl implements UsecpluginService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UsecpluginRPCImpl.class);
	
	AttackIDOutputBuilder attackBuilder = new AttackIDOutputBuilder();
	AttacksFromIPOutputBuilder attack1Builder = new AttacksFromIPOutputBuilder();
	AttacksToIPOutputBuilder attack2Builder = new AttacksToIPOutputBuilder();

	// AttackID
	@Override
	public Future<RpcResult<AttackIDOutput>> attackID(AttackIDInput input) {
		LOG.info("AttackID_RPC Implementation Initiated");
		
		int  lines = 0;
		
        String nodeID = input.getNodeID();
		
		 String d = System.getProperty("user.home");
		
		 
		 final File file = new File(d + File.separator+ "AttacksDataLwm");
		 
		 File log = new File(file , "DataLogLwm.txt");
		 
		 if(log.exists()==true){
		 
		 BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(log));
		
		 Scanner fileIn = new Scanner(new FileReader(log));
		
		while (reader.readLine() != null)
		{
			
			String abc =  fileIn.nextLine();
		
			String a = abc.substring(0, 25);
		String pattern = "(.*)(" + nodeID + ")(.*)";
			
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(a);
			
			
			if (m.find( )) {
				
				lines++;
				
		         attackBuilder.setNoOfAttacks(lines);
		        
		      } 
			
			else {
				
				attackBuilder.setNoOfAttacks(0);
		        
		      }
		
	
		}
		
		
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		 }
		 
 else 
			 
		 {
			 
			 String d1 = System.getProperty("user.home");

			 
			 final File file1 = new File(d1 + File.separator+ "AttacksDataHwm");
			// file1.mkdirs();
			 File log1 = new File(file1 , "DataLogHwm.txt");
		 
			 BufferedReader reader;
				try {
					reader = new BufferedReader(new FileReader(log1));
				
				 Scanner fileIn = new Scanner(new FileReader(log1));
				
				while (reader.readLine() != null)
				{
					
					String abc =  fileIn.nextLine();
				
					String a = abc.substring(0, 25);
				String pattern = "(.*)(" + nodeID + ")(.*)";
					
					Pattern r = Pattern.compile(pattern);
					Matcher m = r.matcher(a);
					
					
					if (m.find( )) {
						
						lines++;
						
				         attackBuilder.setNoOfAttacks(lines);
				      
				      } else {
				    	  attackBuilder.setNoOfAttacks(0);
				       
				      }
				
			
				}
				
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				 }
		 
		 
		return RpcResultBuilder.success(attackBuilder.build()).buildFuture();
	}
		
	// AttacksFromIP

	@Override
	public Future<RpcResult<AttacksFromIPOutput>> attacksFromIP(AttacksFromIPInput input) {
		LOG.info("AttacksFromIP_RPC Implementation Initiated");
		
		int  lines = 0;
		
		String srcIP = input.getSrcIP();
	
		 String d = System.getProperty("user.home");
		
		 
		 final File file = new File(d + File.separator+ "AttacksDataLwm");
		// file.mkdirs();
		 File log = new File(file , "DataLogLwm.txt");
		
		 if(log.exists()==true){
		
		 BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(log));
		
		 Scanner fileIn = new Scanner(new FileReader(log));
		
		while (reader.readLine() != null)
		{
			
			
			
			String abc =  fileIn.nextLine();
		
			String a = abc.substring(0, 53);
		String pattern = "(.*)(" + srcIP + ")(.*)";
			
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(a);
			
			
			if (m.find( )) {
				
				lines++;
				
		         attack1Builder.setNoOfAttacks(lines);
		       
		      } else {
		    	  attack1Builder.setNoOfAttacks(0);
		        
		      }
		
	
		}
		
		
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		 }
		 
		 else 
			 
		 {
			 
			 String d1 = System.getProperty("user.home");

			 
			 final File file1 = new File(d1 + File.separator+ "AttacksDataHwm");
			// file1.mkdirs();
			 File log1 = new File(file1 , "DataLogHwm.txt");
		 
			 BufferedReader reader;
				try {
					reader = new BufferedReader(new FileReader(log1));
				
				 Scanner fileIn = new Scanner(new FileReader(log1));
				
				while (reader.readLine() != null)
				{
					
					String abc =  fileIn.nextLine();
				
					String a = abc.substring(0, 53);
				String pattern = "(.*)(" + srcIP + ")(.*)";
					
					Pattern r = Pattern.compile(pattern);
					Matcher m = r.matcher(a);
					
					
					if (m.find( )) {
						
						lines++;
						
				         attack1Builder.setNoOfAttacks(lines);
				      
				      } else {
				    	  attack1Builder.setNoOfAttacks(0);
				       
				      }
				
			
				}
				
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				 }
			
		return RpcResultBuilder.success(attack1Builder.build()).buildFuture();
	}
	// AttacksToIP

	@Override
	public Future<RpcResult<AttacksToIPOutput>> attacksToIP(AttacksToIPInput input) {
		LOG.info("AttacksToIP_RPC Implementation Initiated");
		
		int  lines = 0;
		
			String dstIP = input.getDstIP();

			 String d = System.getProperty("user.home");

			 
			 final File file = new File(d + File.separator+ "AttacksDataLwm");
			// file.mkdirs();
			 File log = new File(file , "DataLogLwm.txt");
			 
			 if(log.exists()==true){
				 
			
			 BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(log));
			
			 Scanner fileIn = new Scanner(new FileReader(log));
			
			while (reader.readLine() != null)
			{
				
				String abc =  fileIn.nextLine();
			
				String a = abc.substring(53, 64);
			String pattern = "(.*)(" + dstIP + ")(.*)";
				
				Pattern r = Pattern.compile(pattern);
				Matcher m = r.matcher(a);
				
				
				if (m.find( )) {
					
					lines++;
					
			         attack2Builder.setNoOfAttacks(lines);
			      
			      } else {
			    	  attack2Builder.setNoOfAttacks(0);
			       
			      }
			
		
			}
			
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			 }
			 
			 else 
			 
			 {
				 
				 String d1 = System.getProperty("user.home");

				 
				 final File file1 = new File(d1 + File.separator+ "AttacksDataHwm");
				 //file1.mkdirs();
				 File log1 = new File(file1 , "DataLogHwm.txt");
			 
				 BufferedReader reader;
					try {
						reader = new BufferedReader(new FileReader(log1));
					
					 Scanner fileIn = new Scanner(new FileReader(log1));
					
					while (reader.readLine() != null)
					{
						
						String abc =  fileIn.nextLine();
					
						String a = abc.substring(53, 64);
					String pattern = "(.*)(" + dstIP + ")(.*)";
						
						Pattern r = Pattern.compile(pattern);
						Matcher m = r.matcher(a);
						
						
						if (m.find( )) {
							
							lines++;
							
					         attack2Builder.setNoOfAttacks(lines);
					      
					      } else {
					    	  attack2Builder.setNoOfAttacks(0);
					       
					      }
					
				
					}
					
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					 }
				 
			return RpcResultBuilder.success(attack2Builder.build()).buildFuture();
		}

	// AttacksInTime
	/* ====Commented by Rafat.Will be uncommented later====================== */

	@Override
	public Future<RpcResult<AttacksInTimeOutput>> attacksInTime(AttacksInTimeInput input) {
		return null;

	}

}
