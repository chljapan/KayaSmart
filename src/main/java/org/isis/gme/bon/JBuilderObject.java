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

package org.isis.gme.bon;

import org.isis.gme.mga.*;
import org.isis.gme.meta.*;
//import core.*;
import org.isis.jaut.Variant;
import java.util.*;

public abstract class JBuilderObject extends Object
{	
	protected MgaFCO ciObject;
	protected JBuilderModel parent=null;
	protected Hashtable<String, Vector<JBuilderConnection>> inConnections;
	protected Hashtable<String, Vector<JBuilderConnection>> outConnections;
	protected String name;
	protected String kindName;
	protected String kindTitle;
	protected String partName;
	
	public abstract void initialize();
		
	public JBuilderObject(MgaFCO iObject, JBuilderModel parent, boolean globalregister) throws BONException
	{	ciObject = iObject;
		this.parent=parent;
		
		if(ciObject==null) throw new BONException("Tried to instantiate JBuilderObject with null reference.");
                
		name = ciObject.getName();
		kindName = getMeta().getName();
		kindTitle = kindName;
                try
                {
					MgaMetaRole rmeta = ciObject.getMetaRole();
                	partName = rmeta.getName();
                }
                catch(Exception e)
                {
                    partName = "";
                }
		
		inConnections = new Hashtable<String, Vector<JBuilderConnection>>();
		outConnections = new Hashtable<String, Vector<JBuilderConnection>>();
		
		if(globalregister)
			JBuilder.theInstance.setObjectRef(iObject,this);
				
	}
	
	public JBuilderObject(MgaFCO iObject, JBuilderModel parent)
	{	
		this(iObject, parent, true);
	}
	
	public void addInConnection(String name, JBuilderConnection conn)
	{	Vector<JBuilderConnection> list = findInConnections(name);
		if(list==null)
		{	list = new Vector<JBuilderConnection>();
			inConnections.put(name,list);
		}
		list.addElement(conn);
		
	}
	
	public void addOutConnection(String name, JBuilderConnection conn)
	{	Vector<JBuilderConnection> list = findOutConnections(name);
		if(list==null)
		{	list = new Vector<JBuilderConnection>();
			outConnections.put(name,list);
		}
		list.addElement(conn);
	}
	
	public Vector<JBuilderConnection> findInConnections(String name)
	{	Vector<JBuilderConnection> list = (Vector<JBuilderConnection>)inConnections.get(name);
		return list;
	}
	
	public Vector<JBuilderConnection> findOutConnections(String name)
	{	Vector<JBuilderConnection> list = (Vector<JBuilderConnection>)outConnections.get(name);
		return list;
	}
	
	public void removeInConnection(JBuilderConnection conn)
	{	
		if(conn!=null)
		{
			Vector<?> list = (Vector<?>)inConnections.get(conn.getKindName());
			if(list!=null)
			{
			
				int index = list.indexOf(conn);
				if(index!=-1)
					list.removeElementAt(index);
			}
		}	
	}
	
	public void removeOutConnection(JBuilderConnection conn)
	{	
		if(conn!=null)
		{
			Vector<?> list = (Vector<?>)outConnections.get(conn.getKindName());
			
			if(list!=null)
			{
			
				int index = list.indexOf(conn);
				if(index!=-1)
					list.removeElementAt(index);
			}
		}
	}
		
	public abstract void resolve();
	
	public MgaFCO getIObject()
	{	
		return ciObject;
	}
	
	public int getObjType()
	{	
		return ciObject.getObjType();
	}
	
    public String getObjID()
    {
        String strID = "";
        try
        {
            strID = ciObject.getID();
        }
        catch(Exception e)
        {
            return "";
        }
        return strID;
    }
        
	/* public long GetObjId()
	{	long ret = -1;
		String strId;
		try
		{	strId = ciObject.getID();
		}
		catch(Exception e)
		{	//Support.Assert(false,"Object Id not found");
			return -1;
		}
		String strNum1 = strId.substring(3,7);
		String strNum2 = strId.substring(8,16);
		long num1,num2;
		try
		{	num1 = Long.parseLong(strNum1,16);
			num2 = Long.parseLong(strNum2,16);
		}
		catch(NumberFormatException e)
		{	//Support.Assert(false,"Id Parsing error");
			return -1;
		}
		long id = 100000000 * (num1-100) + num2;
		return id;
	} */
	
	public String getName()
	{	return name;
	}
	
	public String getKindName()
	{	return kindName;
	}
	
	public String getKindTitle()
	{	return kindTitle;
	}
	
	public String getPartName()
	{	return partName;
	}
	
	public String getNamePath()
    {    return this.getExtendedName("","#",true);
    }
   
    public String getExtendedName(String extName, String seperator, boolean startWithRoot)
    {   
        if(!startWithRoot)
            extName += getName();
        JBuilderObject parent = this.getParent();
        if(parent!=null)
        {    if(!startWithRoot)
                extName += seperator;
            parent.getExtendedName(extName,seperator,startWithRoot);
            if(startWithRoot)
                extName += seperator;
        }
        if(startWithRoot)
            extName += getName();
        return extName;
    }
	
	public JBuilderModel getParent()
	{	return parent;
	}
	
	//public boolean GetLocation(String aspectName, Rectange loc)
	//public boolean SetLocation(String aspectName, Point loc)
	
//	public short GetNamePosition()
//	{	//Support.Assert(ciObject!=null,"This Object doesnt exist");
//		short pos = 0;
		// To be implemented
//		return pos;
//	}
	
//	public void SetNamePosition(short pos)
//	{	//Support.Assert(ciObject!=null,"This Object doesnt exist");
		// To be implemented
//	}
	
	public JBuilderFolder getFolder()
	{	if(parent!=null)
			return parent.getFolder();		
		Vector<?> folders = JBuilder.theInstance.getFolders();
		
		int fdCount = folders.size();
		for(int i=0;i<fdCount;i++)
		{	JBuilderFolder category = (JBuilderFolder)folders.elementAt(i);
			Vector<?> models = category.getRootModels();
			int mdCount = models.size();
			for(int j=0;j<mdCount;j++)
			{	if(models.elementAt(j).equals(this))
					return category;
			}
		}
		return null;
	}
	
//	public void DisplayError(String msg)
//	{	//Support.MessageBox(msg);
//	}
	
	//public void
	
	// Traverse Childeren
	
	public boolean getAttribute(String name, String val[])
	{	
			try
                {
                     val[0] = ciObject.getStrAttrByName(name);
                }
                catch(Exception e)
                {
                    return false;
                }
                return true;
	}
	
	public String getStringAttribute(String name)
	{	
		String ret = null;
		try
            {
                 ret = ciObject.getStrAttrByName(name);
            }
            catch(Exception e)
            {
                return null;
            }
            return ret;
	}
	
	public boolean getAttribute(String name, int val[])
	{	
		try
		{	val[0] = ciObject.getIntAttrByName(name);
		}
		catch(Exception e)
		{	return false;
		}
		return true; 
	}
	
	public boolean getAttribute(String name, boolean val[])
	{	
		try
		{	val[0] = ciObject.getBoolAttrByName(name);
		}
		catch(Exception e)
		{	return false;
		}
		return true;	
	}
	
	public boolean getAttribute(String name, double val[])
	{	
		try
		{	val[0] = ciObject.getFloatAttrByName(name);
		}
		catch(Exception e)
		{	return false;
		}
		return true;	
	}
	
	public boolean setAttribute(String name, String val)
	{	 
		try
		{	ciObject.setStrAttrByName(name,val);
		}
		catch(Exception e)
		{	return false;
		}
		return true; 
	}
	
	public boolean setAttribute(String name, int val)
	{	
		Variant varVal = new Variant(Integer.toString(val));
		try
		{	ciObject.setAttributeByName(name,varVal);
		}
		catch(Exception e)
		{	return false;
		}
		return true; 
	}
	
	public boolean setAttribute(String name, boolean val)
	{	 try
		{	ciObject.setBoolAttrByName(name,val);
		}
		catch(Exception e)
		{	return false;
		}
		return true; 
	}
	
	public boolean setAttribute(String name, float val)
	{	 try
		{	ciObject.setFloatAttrByName(name,val);
		}
		catch(Exception e)
		{	return false;
		}
		return true; 
	}
	
	
	public Vector<String> getAttributeNames(int type, int secType)
	{	Vector<String> attrList = new Vector<String>();
		MgaMetaAttributes mattrs;
		mattrs = getMeta().getAttributes();
		int maCount = mattrs.getCount();
		for(int i=0;i<maCount;i++)
		{	MgaMetaAttribute mattr = mattrs.getItem(i);
			int t = mattr.getValueType();
			if(t==type || t==secType)
			{	String attrName = mattr.getName();
				attrList.addElement(attrName);
			}
		}
		return attrList;
	}
	
	public Vector<?> getAttributeNames(int type)
	{	return getAttributeNames(type , MgaObject.ATTVAL_NULL);
	}
	
	
	public Vector<?> getStrAttributeNames()
	{	return getAttributeNames(MgaObject.ATTVAL_STRING);
	}
	
	public Vector<?> getIntAttributeNames()
	{	return getAttributeNames(MgaObject.ATTVAL_INTEGER);
	}
	
	public Vector<?> getBoolAttributeNames()
	{	return getAttributeNames(MgaObject.ATTVAL_BOOLEAN);
	}
	
	public Vector<?> getFloatAttributeNames()
	{	return getAttributeNames(MgaObject.ATTVAL_DOUBLE);
	}
	
	public Vector<?> getRefAttributeNames()
	{	return getAttributeNames(MgaObject.ATTVAL_REFERENCE);
	}
									
	public Vector<JBuilderObject> getReferencedBy()
	{	MgaFCOs refList;
		Vector<JBuilderObject> list = new Vector<JBuilderObject>();
		refList = ciObject.getReferencedBy();
		int rfCount = refList.getCount();
		for(int i=0;i<rfCount;i++)
		{	MgaFCO refItem = refList.getItem(i);
			JBuilderObject object = JBuilder.theInstance.findObject(refItem);
			//Support.Assert(object!=null, "Null reference");
			list.addElement(object);
		}
		return list;
	}
	
	public boolean setName(String newname)
	{	try
		{	ciObject.setName(newname);
		}
		catch(Exception e)
		{	return false;
		}
		name = newname;
		return true;
	}
	
	public Vector<?> getInConnections(String name)
	{	Vector<?> list = findInConnections(name);
		if(list==null)
			list = new Vector<Object>();	
		return list;
	}
	
	public Vector<?> getOutConnections(String name)
	{	Vector<?> list = findOutConnections(name);
		if(list==null)
			list = new Vector<Object>();	
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public boolean getInConnectedObjects(String name, @SuppressWarnings("rawtypes") Vector list[])
	{	Vector<?> conns = getInConnections(name);
		list[0] = new Vector<Object>();
		if(conns==null)
			return false;
		int coCount = conns.size();
		for(int i=0;i<coCount;i++)
		{	JBuilderConnection conn = (JBuilderConnection)conns.elementAt(i);
			list[0].addElement(conn.getSource());
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public boolean getOutConnectedObjects(String name, @SuppressWarnings("rawtypes") Vector list[])
	{	Vector<?> conns = getOutConnections(name);
		list[0] = new Vector<Object>();
		if(conns==null)
			return false;
		int coCount = conns.size();
		for(int i=0;i<coCount;i++)
		{	JBuilderConnection conn = (JBuilderConnection)conns.elementAt(i);
			list[0].addElement(conn.getDestination());
		}
		return true;
	}
	
	// Get Direct Connections
	
	public Hashtable<String, Vector<JBuilderConnection>> getInConnections()
	{	return inConnections;
	}
	
	public Hashtable<String, Vector<JBuilderConnection>> getOutConnections()
	{	return outConnections;
	}
	
	public MgaMetaFCO getMeta()
	{	MgaMetaFCO iMeta;
		try
		{	iMeta = ciObject.getMeta();
		}
		catch(Exception e)
		{	
			return null;
		}
		return iMeta;
	}
	
	public boolean isInstance()
	{	boolean ret = false;
		try
		{	ret = ciObject.getIsInstance();
		}
		catch(Exception e)
		{	return false;
		}
		return ret;
	}
	
	public JBuilderObject getType()
	{	if(!isInstance())
			return null;
		
		MgaFCO type;
		try
		{	type = ciObject.getType();
		}
		catch(Exception e)
		{	
			return null;
		}
		
		return JBuilder.theInstance.findObject(type);
		
	}
	
	public void traverseChildren()
	{
	}

	public static class Attribute<t extends Object>{
		
	}
	
}
