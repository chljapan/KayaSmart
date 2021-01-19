/*
 * Copyright (c) 2008, Institute for Software Integrated Systems Vanderbilt University
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without written agreement is
 * hereby granted, provided that the above copyright notice, the following
 * two paragraphs and the author appear in all copies of this software.
 *
 * IN NO EVENT SHALL THE VANDERBILT UNIVERSITY BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
 * OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE VANDERBILT
 * UNIVERSITY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * THE VANDERBILT UNIVERSITY SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
 * ON AN "AS IS" BASIS, AND THE VANDERBILT UNIVERSITY HAS NO OBLIGATION TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 * Author: Andras Nadas
 * Contact: Andras Nadas (nadand@isis.vanderbilt.edu)
 */ 

package org.isis.gme.mga;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.isis.gme.meta.MgaMetaModel;
import org.isis.gme.meta.MgaMetaRole;
import org.isis.jaut.Dispatch;

public class MgaUtil {
	public static final int ATTRIBUTE_STATUS_OBJECT = 0;
	public static final int ATTRIBUTE_STATUS_METADEFAULT = -1;
	public static final int ATTRIBUTE_STATUS_UNDEFINED = -2;
	public static final int ATTRIBUTE_STATUS_INVALID = -3;
	
	
	public static List<MgaAtom> getChildAtomList(MgaModel mgaModel, String role){
		return getChildFCOList(mgaModel, role,MgaAtom.class);
	}
	
	public static List<MgaModel> getChildModelList(MgaModel mgaModel, String role){
		return getChildFCOList(mgaModel, role,MgaModel.class);
	}
	public static List<MgaFCO> getChildFCOList(MgaModel mgaModel, String role){
		return getChildFCOList(mgaModel, role, MgaFCO.class);
	}	
	
	public static List<MgaReference> getChildReferenceList(MgaModel mgaModel, String role){
		return getChildFCOList(mgaModel, role, MgaReference.class);
	}
	
	public static List<MgaConnection> getChildConnectionList(MgaModel mgaModel, String role){
		return getChildFCOList(mgaModel, role, MgaConnection.class);
	}
	
	private static <T extends MgaObject> List<T> getChildFCOList(MgaModel mgaModel, String role, Class<T> objectType){
		List<T> fcoList = new ArrayList<T>();
		
		MgaFCOs chds = mgaModel.getChildFCOs();
		int chCount = chds.getCount();
		for(int i=0;i<chCount;i++)
		{	MgaFCO chd = chds.getItem(i);
			
			try{
				if(!chd.getMetaRole().getName().equals(role)) continue;
				Constructor<T> cnst = objectType.getConstructor(Dispatch.class);
				T instance = cnst.newInstance(chd);
				fcoList.add(instance);
			}catch(Exception e){
				continue;
			}
			
		}
		return fcoList;
	}

	
	public static MgaReference getChildReferencebyName(MgaModel mgaModel, String role, String name){
		
		return getChildFCObyName(mgaModel, role, name, MgaReference.class);
	}
	
	public static MgaModel getChildModelbyName(MgaModel mgaModel, String role, String name){
		
		return getChildFCObyName(mgaModel, role, name, MgaModel.class);
	}
	
	public static MgaFCO getChildFCObyName(MgaModel mgaModel, String role, String name){
		
		return getChildFCObyName(mgaModel, role, name, MgaFCO.class);
	}
	
	public static MgaAtom getChildAtombyName(MgaModel mgaModel, String role, String name){
		
		return getChildFCObyName(mgaModel, role, name, MgaAtom.class);
	}
	
	public static MgaConnection getChildConnectionbyName(MgaModel mgaModel, String role, String name){
		
		return getChildFCObyName(mgaModel, role, name, MgaConnection.class);
	}
	
	private static <T extends MgaObject> T getChildFCObyName(MgaModel mgaModel, String role, String name, Class<T> objectType){
		T instance = null;
		MgaFCOs chds = mgaModel.getChildFCOs();
		int chCount = chds.getCount();
		for(int i=0;i<chCount;i++)
		{	MgaFCO chd = chds.getItem(i);
			
			try{
				if(role != null && !chd.getMetaRole().getName().equals(role)) continue;
				if(!chd.getName().equals(name)) continue;
				Constructor<T> cnst = objectType.getConstructor(Dispatch.class);
				instance = cnst.newInstance(chd);
				break;
			}catch(Exception e){
				continue;
			}
			
		}
		return instance;
	}
	
	public static MgaModel createNewModel(MgaModel parent, String partName){
		MgaMetaRole role = parent.getMetaModel().getRoleByName(partName);
		MgaFCO i = parent.createChildObject(role);
		return new MgaModel(i);
	}
	
	public static MgaReference createNewReference(MgaModel parent,MgaFCO target, String partName){
		MgaMetaRole role = new MgaMetaModel(parent.getMeta()).getRoleByName(partName);
		return new MgaReference(parent.createReference(role, target));
	}
	
	public static MgaModel getContainer(MgaFCO fco, String containerType){
		MgaModel currentParent = fco.getParentModel();
		while(!currentParent.getMeta().getName().equals(containerType) && currentParent!=null){
			currentParent = currentParent.getParentModel();
		}
		
		if(currentParent.getMeta().getName().equals(containerType)){
			return currentParent;
		}else{
			return null;
		}
	}
	
	public static MgaAttribute getAttributeByName(MgaFCO parent, String name){
		MgaAttributes atts = parent.getAttributes();
		for(MgaAttribute att:atts.getAll()){
			if(att.getMeta().getName().equals(name))
				return att;
		}
		return null;
	}
	
	public static List<MgaFCO> getConnectionEndpoints(MgaConnection connection){
		ArrayList<MgaFCO> endpoints = new ArrayList<MgaFCO>();
		
		try{
			MgaSimpleConnection conn = new MgaSimpleConnection(connection);
			endpoints.add(conn.getDst());
			endpoints.add(conn.getSrc());
		}catch (Exception e) {

		}
		
		
		return endpoints;
		
	}
	
	public static List<MgaFCO> getConnectionOppositeEndpointsList(List<MgaConnection> connectionList, MgaFCO object){
		ArrayList<MgaFCO> endpoints = new ArrayList<MgaFCO>();
		for(MgaConnection connection : connectionList){
			
			try{
				MgaSimpleConnection conn = new MgaSimpleConnection(connection);

				if(conn.getSrc().equals(object)) endpoints.add(conn.getDst());
				if(conn.getDst().equals(object)) endpoints.add(conn.getSrc());
			}catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		
		return endpoints;
		
	}
	
	public static List<MgaFCO> getConnectionOppositeSourceList(List<MgaConnection> connectionList, MgaFCO object){
		ArrayList<MgaFCO> endpoints = new ArrayList<MgaFCO>();
		for(MgaConnection connection : connectionList){
			
			try{
				MgaSimpleConnection conn = new MgaSimpleConnection(connection);
				if(conn.getDst().equals(object)) endpoints.add(conn.getSrc());
			}catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		
		return endpoints;
		
	}
	
	public static List<MgaFCO> getConnectionOppositeDestinationsList(List<MgaConnection> connectionList, MgaFCO object){
		ArrayList<MgaFCO> endpoints = new ArrayList<MgaFCO>();
		for(MgaConnection connection : connectionList){
			
			try{
				MgaSimpleConnection conn = new MgaSimpleConnection(connection);

				if(conn.getSrc().equals(object)) endpoints.add(conn.getDst());
			}catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		
		return endpoints;
		
	}
	
	public static List<MgaAtom> getChildAtomList(MgaFolder mgaFolder, String role){
		return getChildFCOList(mgaFolder, role,MgaAtom.class);
	}
	
	public static List<MgaModel> getChildModelList(MgaFolder mgaFolder, String role){
		return getChildFCOList(mgaFolder, role,MgaModel.class);
	}
	public static List<MgaFCO> getChildFCOList(MgaFolder mgaFolder, String role){
		return getChildFCOList(mgaFolder, role, MgaFCO.class);
	}	
	
	public static List<MgaReference> getChildReferenceList(MgaFolder mgaFolder, String role){
		return getChildFCOList(mgaFolder, role, MgaReference.class);
	}
	
	public static List<MgaConnection> getChildConnectionList(MgaFolder mgaFolder, String role){
		return getChildFCOList(mgaFolder, role, MgaConnection.class);
	}
	
	private static <T extends MgaObject> List<T> getChildFCOList(MgaFolder mgaFolder, String role, Class<T> objectType){
		List<T> fcoList = new ArrayList<T>();
		
		MgaFCOs chds = mgaFolder.getChildFCOs();
		int chCount = chds.getCount();
		for(int i=0;i<chCount;i++)
		{	MgaFCO chd = chds.getItem(i);
			
			try{
				if(!chd.getMeta().getName().equals(role)) continue;
				Constructor<T> cnst = objectType.getConstructor(Dispatch.class);
				T instance = cnst.newInstance(chd);
				fcoList.add(instance);
			}catch(Exception e){
				continue;
			}
			
		}
		return fcoList;
	}
	
	public static MgaReference getChildReferencebyName(MgaFolder mgaFolder, String role, String name){
		
		return getChildFCObyName(mgaFolder, role, name, MgaReference.class);
	}
	
	public static MgaModel getChildModelbyName(MgaFolder mgaFolder, String role, String name){
		
		return getChildFCObyName(mgaFolder, role, name, MgaModel.class);
	}
	
	public static MgaAtom getChildAtombyName(MgaFolder mgaFolder, String role, String name){
		
		return getChildFCObyName(mgaFolder, role, name, MgaAtom.class);
	}
	
	public static MgaFCO getChildFCObyName(MgaFolder mgaFolder, String role, String name){
		
		return getChildFCObyName(mgaFolder, role, name, MgaFCO.class);
	}
	
	public static MgaConnection getChildConnectionbyName(MgaFolder mgaFolder, String role, String name){
		
		return getChildFCObyName(mgaFolder, role, name, MgaConnection.class);
	}
	
	private static <T extends MgaObject> T getChildFCObyName(MgaFolder mgaFolder, String role, String name, Class<T> objectType){
		T instance = null;
		MgaFCOs chds = mgaFolder.getChildFCOs();
		int chCount = chds.getCount();
		for(int i=0;i<chCount;i++)
		{	MgaFCO chd = chds.getItem(i);
			
			try{
				if(role != null && !chd.getMetaRole().getName().equals(role)) continue;
				if(!chd.getName().equals(name)) continue;
				Constructor<T> cnst = objectType.getConstructor(Dispatch.class);
				instance = cnst.newInstance(chd);
				break;
			}catch(Exception e){
				continue;
			}
			
		}
		return instance;
	}
	
}
