package org.isis.gme.bon;

import java.util.*;
import org.isis.gme.mga.*;
import org.isis.gme.meta.*;
import org.isis.jaut.Dispatch;

public class JBuilderFolder
{	
	protected MgaFolder ciFolder;
	protected Vector<JBuilderModel> rootModels;
	protected Vector<JBuilderObject> rootFCOs;
	protected Vector<JBuilderFolder> subfolders;
	protected JBuilderFolder parentfolder;
	protected String name;
	
	
	public JBuilderFolder(MgaFolder iFolder, JBuilderFolder parent) throws BONException
	{	
		ciFolder = iFolder;
		parentfolder = parent;
		if(ciFolder==null) throw new BONException("Tried to instantiate a Folder with a null reference");
		
		rootModels =  new Vector<JBuilderModel>();
		subfolders = new Vector<JBuilderFolder>();
		rootFCOs = new Vector<JBuilderObject>();
		name = ciFolder.getName();
		
		JBuilder.theInstance.folders.addElement(this);
		
		
		
		MgaFCOs psa = ciFolder.getChildFCOs();
        //MgaObjects psa = ciFolder.getChildObjects();           
		
		int psaCount = psa.getCount();
        //if(psaCount == 0) System.out.println("no objects");
		for(int i=0;i<psaCount;i++)
		{	MgaFCO item = psa.getItem(i);
            if(item.getObjType() == MgaObject.OBJTYPE_MODEL)
			{	
                MgaModel rootModel = new MgaModel((Dispatch)item);
                JBuilderModel m = JBuilderFactory.createModel(rootModel,null);
				rootModels.addElement(m);
				rootFCOs.addElement(m);
			}
			//any FCO can reside in a folder
			if(item.getObjType() == MgaObject.OBJTYPE_ATOM)
			{
				MgaAtom rootAtom = new MgaAtom((Dispatch)item);
				JBuilderAtom a = JBuilderFactory.createAtom(rootAtom,null);
				rootFCOs.addElement(a);
			}
			if(item.getObjType() == MgaObject.OBJTYPE_CONNECTION)
			{
				MgaSimpleConnection rootConn = new MgaSimpleConnection((Dispatch)item);
				JBuilderConnection c = JBuilderFactory.createConn(rootConn,null);
				rootFCOs.addElement(c);
			}
			if(item.getObjType() == MgaObject.OBJTYPE_SET)
			{
				MgaSet rootSet = new MgaSet((Dispatch)item);
				JBuilderSet s = JBuilderFactory.createSet(rootSet,null);
				rootFCOs.addElement(s);
			}
			if(item.getObjType() == MgaObject.OBJTYPE_REFERENCE)
			{
				MgaReference rootRef = new MgaReference((Dispatch)item);
				
				MgaFCO r = item;
				int ot = item.getObjType();
				while(ot == MgaObject.OBJTYPE_REFERENCE)
				{	
					try
					{
						MgaFCO rr;
						rr = (new MgaReference(r)).getReferred();
						r = rr;
					}
					catch(Exception e)//handle when the referred is null
					{
						r = null;
					}
					if(r==null)	break;
					ot = r.getObjType();
				}
				if(ot== MgaObject.OBJTYPE_MODEL)
				{	
					JBuilderModelReference oo = JBuilderFactory.createModelRef(rootRef,null);
					rootFCOs.addElement(oo);					
				}
						
				else
				{	
					JBuilderReference oo = JBuilderFactory.createReference(rootRef,null);
					rootFCOs.addElement(oo);
				}
			}
		}
		
		
		
		MgaFolders sfs = ciFolder.getChildFolders();
		int sfsCount = sfs.getCount();
		for(int i=0;i<sfsCount;i++)
		{	MgaFolder subFolder = sfs.getItem(i);
			subfolders.addElement(new JBuilderFolder(subFolder,this));
		}														  
			
		
	}
		
	public void resolve()
	{	//int rootCount = rootModels.size();
		//for(int i=0;i<rootCount;i++)
		//{	JBuilderModel rootModel = (JBuilderModel)rootModels.elementAt(i);
		//	rootModel.resolve();
		//}
		int rootCount = rootFCOs.size();
		for(int i=0;i<rootCount;i++)
		{
			JBuilderObject rootObject = (JBuilderObject)rootFCOs.elementAt(i);
			rootObject.resolve();
		}
		
		int subCount = subfolders.size();
		for(int i=0;i<subCount;i++)
		{	JBuilderFolder subFolder = (JBuilderFolder)subfolders.elementAt(i);
			subFolder.resolve();
		}
		
	}
	
	protected void removeRootModel(JBuilderModel model)
	{	
		if(model==null) return;
		int index = rootModels.indexOf(model);
		if(index!=-1)
			rootModels.removeElementAt(index);
	}
	
	public MgaFolder getIFolder()
	{	return ciFolder;
	}
	
	public String getName() 
	{	return name;
	}
	
	public Vector<JBuilderModel> getRootModels()
	{	return rootModels;
	}
	
	public Vector<JBuilderFolder> getSubFolders()
	{	return subfolders;
	}
	
	public Vector<JBuilderObject> getRootFCOs()
	{
		return rootFCOs;
	}
	
	public JBuilderModel getRootModel(String name)
	{	int rootCount = rootModels.size();
		for(int i=0;i<rootCount;i++)
		{	JBuilderModel rootModel = (JBuilderModel)rootModels.elementAt(i);	
			if(rootModel.getName().equals(name))
			   return rootModel;
		}
		return null;
	}
	
	public JBuilderModel createNewModel(String kindName)
	{	MgaFCO i;
		MgaMetaFolder fmeta;
		MgaMetaFCO cmeta;
		
		fmeta = ciFolder.getMetaFolder();
		cmeta = fmeta.getLegalRootObjectByName(kindName);
		
		if(cmeta==null) return null; // couldn't get MetaFCO
		//Support.Assert(cmeta!=null,"Meta FCO not found while creating new Model");
		i = ciFolder.createRootObject(cmeta);
		if(i==null) return null; // couldn't create the new model
		//Support.Assert(i !=null,"Could not create new Model");
		MgaModel im = new MgaModel(i);	
		JBuilderModel o = JBuilderFactory.createModel(im,null);
        //JBuilderModel o = new JBuilderModel(im,null);
		rootModels.add(o);
		return o;
	}
	
}
