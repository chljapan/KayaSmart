package org.isis.gme.bon;

import java.util.*;
import org.isis.gme.mga.*;

public class JBuilder
{
	protected MgaFolder  ciRootFolder;
	protected MgaProject ciGme;
	protected Vector<Object> folders;
	protected JBuilderFolder rootFolder;
	protected Hashtable<String, JBuilderObject> objectmap;
	protected Hashtable<String, JBuilderConnection> connectionmap;
	
	public static JBuilder theInstance;
	
		
	public JBuilder(MgaProject iGme, BONComponent interpreter) throws BONException
	{	
        if(iGme==null) throw new BONException("Project Pointer is NULL");
		ciGme = iGme;
		
        JBuilderFactory.Initialize();
        interpreter.registerCustomClasses();
                
		ciRootFolder = ciGme.getRootFolder();
		if(ciRootFolder==null) throw new BONException("Root Folder is NULL");
		theInstance = this;
		objectmap = new Hashtable<String, JBuilderObject>();
		connectionmap = new Hashtable<String, JBuilderConnection>();
		folders = new Vector<Object>();
		rootFolder = new JBuilderFolder(ciRootFolder,null);
		
		rootFolder.resolve();
	}
	
	public MgaProject getProject()
	{	return ciGme;
	}
	
	public void setObjectRef(MgaFCO i, JBuilderObject o)
	{	String strId = i.getID();
		JBuilderObject ob = (JBuilderObject)objectmap.get(strId);
		if(ob!=null)
		{
			//Support.Assert(o==ob,"Two references");
		}
		else
			objectmap.put(strId,o);
			
	}
	
	public void setConnectionRef(MgaSimpleConnection i, JBuilderConnection o)
	{	String strId = i.getID();
		JBuilderConnection ob = (JBuilderConnection)connectionmap.get(strId);
		if(ob!=null)
                {	
                	//Support.Assert(o==ob,"Two references");
                }
                else
			connectionmap.put(strId,o);
		
	}
	
	public JBuilderObject findObject(MgaFCO i)
	{	String strId = i.getID();
		Object ob = objectmap.get(strId);
		if(ob!=null)
		{	if(((JBuilderObject)ob)!=null)
				return (JBuilderObject)ob;
		}
		//throw new BONException("Object not Found");
		return null;
	}
	
	public JBuilderConnection findConnection(MgaSimpleConnection i) 
	{	String strId = i.getID();
		Object ob = connectionmap.get(strId);
		if(ob!=null)
		{	if(((JBuilderConnection)ob)!=null)
				return (JBuilderConnection)ob;
						
		}
		//throw new BONException("Object not Found");
		return null;
	}
	
	public void forgetObjectRef(MgaFCO i)
	{	objectmap.remove(i);
	}
	
	public void forgetConnection(MgaSimpleConnection i)
	{	connectionmap.remove(i);
	}
	
	public Vector<Object> getFolders()
	{	return folders;
	}
	
	public JBuilderFolder getFolder(String name)
	{	int Size = folders.size(); 
		for(int i=0;i<Size;i++)
		{	JBuilderFolder folder = (JBuilderFolder)folders.elementAt(i);
			if(folder != null)
			if(folder.getName().equals(name))
				return folder;
		}
		return null;
	}
	
	public JBuilderFolder getRootFolder()
	{	return rootFolder;
	}
				
	public String getProjectName()
	{	return ciGme.getName();
	}
	
}
