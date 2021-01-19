package org.isis.gme.bon;

import java.util.*;
import org.isis.gme.mga.*;
import org.isis.gme.meta.*;

public class JBuilderModel extends JBuilderObject
{
	protected Vector<JBuilderObject> children;
	protected Vector<JBuilderConnection> connchildren;
	protected Vector<JBuilderModel> models;
	protected Hashtable<String, Vector<?>> referenceLists;
	protected Hashtable<String, Vector<JBuilderModel>> modelLists;
	protected Hashtable<String, Vector<JBuilderAtom>> atomLists;
	protected Hashtable<String, Vector<?>> modelReferenceLists;
	protected Hashtable<String, Vector<?>> connectionLists;
	protected Vector<JBuilderSet> sets;
	protected Hashtable<String, Vector<?>> setLists;
        protected MgaMetaModel metaModel;
		@SuppressWarnings("unused")
		private JBuilderConnection o;

	
	public JBuilderModel(MgaModel iModel, JBuilderModel parent)
	{	super(iModel,parent);
		
                metaModel = new MgaMetaModel(getMeta());
                
		children = new Vector<JBuilderObject>();
		connchildren = new Vector<JBuilderConnection>();
		models = new Vector<JBuilderModel>();
			
		modelLists = new Hashtable<String, Vector<JBuilderModel>>();
		atomLists = new Hashtable<String, Vector<JBuilderAtom>>();
		referenceLists = new Hashtable<String, Vector<?>>();
		modelReferenceLists = new Hashtable<String, Vector<?>>();
		connectionLists = new Hashtable<String, Vector<?>>();
		
		sets = new Vector<JBuilderSet>();
		setLists = new Hashtable<String, Vector<?>>();
		
		kindTitle = getMeta().getDisplayedName();
		
		createModels();
		createAtoms();
		
		createReferences();
		//CreateModelReferences();
		
		createConnections();
		createSets();
		
	}
	
	protected void createModels()
	{	Vector<JBuilderModel> objectlist;
		MgaMetaRoles roles;
		roles = metaModel.getRoles();
		int roCount = roles.getCount();
		for(int i=0;i<roCount;i++)
		{	MgaMetaRole role = roles.getItem(i);
			MgaMetaFCO kind = role.getKind();
			int type = kind.getObjType();
			if(type == MgaObject.OBJTYPE_MODEL)
			{	String part = role.getName();
				objectlist = (Vector<JBuilderModel>)modelLists.get(part);
				//Support.Assert(objectlist==null,"Part name of this model is in Hashtable");
				
				objectlist = new Vector<JBuilderModel>();
				modelLists.put(part,objectlist);
			}
		}
		
		MgaFCOs chds = getIModel().getChildFCOs();
		int chCount = chds.getCount();
		for(int i=0;i<chCount;i++)
		{	MgaFCO chd = chds.getItem(i);
			int ot = chd.getObjType();
			if( ot != MgaObject.OBJTYPE_MODEL)
				continue;
			
			JBuilderModel o = JBuilderFactory.createModel(new MgaModel(chd),this);
			//JBuilderModel o = new JBuilderModel(new MgaModel(chd),this);
            objectlist = (Vector<JBuilderModel>)modelLists.get(o.getPartName());
			//Support.Assert(objectlist!=null,"Part Name not found");
			
			objectlist.addElement(o);
			children.addElement(o);
			models.addElement(o);
		}
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void createReferences()
	{	Vector<JBuilderReference> objectlist;
		MgaMetaRoles roles;
		roles = metaModel.getRoles();
		int roCount = roles.getCount();
		for(int i=0;i<roCount;i++)
		{	MgaMetaRole role = roles.getItem(i);
			MgaMetaFCO kind = role.getKind();
			int type = kind.getObjType();
			if(type == MgaObject.OBJTYPE_REFERENCE)
			{	String part = role.getName();
				objectlist = (Vector<JBuilderReference>)referenceLists.get(part);
				//Support.Assert(objectlist==null,"Part name of this model ref is in Hashtable");
				
				referenceLists.put(part,new Vector());
				modelReferenceLists.put(part,new Vector());
				
			}
		}
		
		MgaFCOs chds = getIModel().getChildFCOs();
		int chCount = chds.getCount();
		for(int i=0;i<chCount;i++)
		{	MgaFCO chd = chds.getItem(i);
			int ot = chd.getObjType();
			if( ot != MgaObject.OBJTYPE_REFERENCE)
				continue;
			
			MgaFCO r = chd;
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
			{	JBuilderReference oo = JBuilderFactory.createModelRef(new MgaReference(chd),this);
				//JBuilderReference oo = new JBuilderReference(new MgaReference(chd),this);
                objectlist = (Vector)referenceLists.get(oo.getPartName());
				//Support.Assert(objectlist!=null, "Part Name not found");
				objectlist.addElement(oo);
				children.addElement(oo);
				
				Vector mobjectlist = (Vector) modelReferenceLists.get(oo.getPartName());
				//Support.Assert(mobjectlist!=null, "Part Name not found in modelReferenceList");
				mobjectlist.addElement(oo);
			}
								
		//	else if(ot== MgaObject.OBJTYPE_ATOM)
		//	{	JBuilderReference oo = JBuilderFactory.createAtomRef(new MgaReference(chd),this);
		//		//JBuilderReference oo = new JBuilderReference(new MgaReference(chd),this);
          //                      objectlist = (Vector)referenceLists.get(oo.getPartName());
			//	//Support.Assert(objectlist!=null, "Part Name not found");
			//	objectlist.addElement(oo);
			//	children.addElement(oo);
			//	
			//	Vector aobjectlist = (Vector) atomReferenceLists.get(oo.getPartName());
			//	//Support.Assert(aobjectlist!=null, "Part Name not found in atomReferenceList");
			//	aobjectlist.addElement(oo);
			//}
			
			else
			{	
				JBuilderReference oo = JBuilderFactory.createReference(new MgaReference(chd),this);
				//JBuilderReference oo = new JBuilderReference((MgaReference)chd,this);
				objectlist = (Vector)referenceLists.get(oo.getPartName());
				//Support.Assert(objectlist!=null, "Part Name not found");
				objectlist.addElement(oo);
				children.addElement(oo);
			}
		}
		
	}
	
	
	
	protected void createAtoms()
	{	Vector<JBuilderAtom> objectlist;
		MgaMetaRoles roles;
		roles = metaModel.getRoles();
		int roCount = roles.getCount();
		for(int i=0;i<roCount;i++)
		{	MgaMetaRole role = roles.getItem(i);
			MgaMetaFCO kind = role.getKind();
			int type = kind.getObjType();
			if(type == MgaObject.OBJTYPE_ATOM)
			{	String part = role.getName();
				objectlist = (Vector<JBuilderAtom>)atomLists.get(part);
				//Support.Assert(objectlist==null,"Part name of this atom is in Hashtable");
				
				objectlist = new Vector<JBuilderAtom>();
				atomLists.put(part,objectlist);
			}
		}
		
		MgaFCOs chds = getIModel().getChildFCOs();
		int chCount = chds.getCount();
		for(int i=0;i<chCount;i++)
		{	MgaFCO chd = chds.getItem(i);
			int ot = chd.getObjType();
			if( ot != MgaObject.OBJTYPE_ATOM)
				continue;
			
            //JBuilderAtom o = new JBuilderAtom(new MgaAtom(chd),this);
			JBuilderAtom o = JBuilderFactory.createAtom(new MgaAtom(chd),this);
			objectlist =(Vector<JBuilderAtom>) atomLists.get(o.getPartName());
			//Support.Assert(objectlist!=null,"Part Name not found");
			
			objectlist.addElement(o);
			children.addElement(o);
		}
	}
	
	
	
	@SuppressWarnings("rawtypes")
	protected void createConnections()
	{	Vector<?> objectlist;
		MgaMetaRoles roles;
		roles = metaModel.getRoles();
		int roCount = roles.getCount();
		for(int i=0;i<roCount;i++)
		{	MgaMetaRole role = roles.getItem(i);
			MgaMetaFCO kind = role.getKind();
			int type = kind.getObjType();
			if(type == MgaObject.OBJTYPE_CONNECTION)
			{	String kname = role.getName();
				objectlist = (Vector<?>)connectionLists.get(kname);
				//Support.Assert(objectlist==null,"Part name of this atom is in Hashtable");
				
				objectlist = new Vector();
				connectionLists.put(kname,objectlist);
			}
		}
		
		MgaFCOs chds = getIModel().getChildFCOs();
		int chCount = chds.getCount();
		for(int i=0;i<chCount;i++)
		{	MgaFCO chd = chds.getItem(i);
			int ot = chd.getObjType();
			if( ot != MgaObject.OBJTYPE_CONNECTION)
				continue;
			
			o = addConnection(new MgaSimpleConnection(chd));
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected void createSets()
	{	Vector<?> objectlist;
		MgaMetaRoles roles;
		roles = metaModel.getRoles();
		int roCount = roles.getCount();
		for(int i=0;i<roCount;i++)
		{	MgaMetaRole role = roles.getItem(i);
			MgaMetaFCO kind = role.getKind();
			int type = kind.getObjType();
			if(type == MgaObject.OBJTYPE_SET)
			{	String part = role.getName();
				objectlist = (Vector<?>)setLists.get(part);
				//Support.Assert(objectlist==null,"Part name of this atom is in Hashtable");
				
				objectlist = new Vector();
				setLists.put(part,objectlist);
			}
		}
		
		MgaFCOs chds = getIModel().getChildFCOs();
		int chCount = chds.getCount();
		for(int i=0;i<chCount;i++)
		{	MgaFCO chd = chds.getItem(i);
			int ot = chd.getObjType();
			if( ot != MgaObject.OBJTYPE_SET)
				continue;
			
			addSet(new MgaSet(chd));
		}
	}
	
	//protected void MapPortsToAtoms()
	//{
	//}
	
	public  void resolve()
	{	int chCount = children.size();
		for(int i=0;i<chCount;i++)
		{	JBuilderObject child = (JBuilderObject)children.elementAt(i);
			child.resolve();
		}
	}
	
	@SuppressWarnings("unchecked")
	protected JBuilderConnection addConnection(MgaSimpleConnection iConnection)
	{	JBuilderConnection conn = JBuilderFactory.createConn(new MgaSimpleConnection(iConnection),this);
		//JBuilderConnection conn = new JBuilderConnection(iConnection,this);
        String kname = conn.getPartName();
		
		Object lst = connectionLists.get(kname);
		//Support.Assert(lst!=null, "hash table entry for connection "+ name +" in model "+this.name+ " not found");
		((Vector<JBuilderConnection>)lst).addElement(conn);
		
		connchildren.addElement(conn);
		children.addElement(conn);
		return conn;
	}
	
	@SuppressWarnings("unchecked")
	protected JBuilderSet addSet(MgaSet iSet)
	{	JBuilderSet set = JBuilderFactory.createSet(new MgaSet(iSet),this);
                //JBuilderSet set = new JBuilderSet(iSet, this);
            
		Object lst = setLists.get(set.getKindName());
		//Support.Assert(lst!=null, "hash table entry for set "+ name +" in model "+this.name+ " not found");
		((Vector<JBuilderSet>)lst).addElement(set);
		
		sets.addElement(set);
		children.addElement(set);
		return set;
	}
	
	@SuppressWarnings("rawtypes")
	protected Vector<?> findConnections(String name)
	{	Object lst = connectionLists.get(name);
		Vector<?> list = (Vector<?>)lst;
		if(list==null)
			list = new Vector();
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	protected Vector findSets(String name)
	{	Object lst = setLists.get(name);
		Vector list = (Vector)lst;
		if(list==null)
			list = new Vector();
		return list;
	}
	
	protected void removeReference(JBuilderReference reference)
	{	
		if(reference!=null)
		{
		
			Vector<?> list = (Vector<?>)referenceLists.get(reference.getPartName());
			if(list!=null)
			{
			
				int index = list.indexOf(reference);
				if(index!=-1)
					list.removeElementAt(index);
			}
		}
	}
	
	protected void removeModel(JBuilderModel model)
	{	
		if(model!=null)
		{
		
			Vector<?> list = (Vector<?>)modelLists.get(model.getPartName());
			if(list!=null)
			{
			
				int index = list.indexOf(model);
				if(index!=-1)
					list.removeElementAt(index);
			}
		}
	}
	
	protected void removeModelReference(JBuilderModelReference modelref)
	{	
		if(modelref!=null)
		{
			Vector<?> list = (Vector<?>)modelReferenceLists.get(modelref.getPartName());
			
			if(list!=null)
			{
				int index = list.indexOf(modelref);
				if(index!=-1)
					list.removeElementAt(index);
			}
		}
	}
	
	protected void removeAtom(JBuilderAtom atom)
	{	
		if(atom!=null)
		{
			Vector<?> list = (Vector<?>)atomLists.get(atom.getPartName());
			
			if(list!=null)
			{
			int index = list.indexOf(atom);
			if(index!=-1)
				list.removeElementAt(index);
			}
		}
	}
	
/*	protected void removeAtomReference(JBuilderAtomReference atomref)
	{	
		if(atomref!=null)
		{
		
			Vector list = (Vector)atomReferenceLists.get(atomref.getPartName());
			if(list!=null)
			{
				int index = list.indexOf(atomref);
				if(index!=-1)
					list.removeElementAt(index);
			}
		}
	} */
	
	protected void removeConnection(JBuilderConnection conn)
	{	
		if(conn!=null)
		{
			Vector<?> list = (Vector<?>)connectionLists.get(conn.getPartName());
			
			if(list!=null)
			{
				int index = list.indexOf(conn);
				if(index!=-1)
					list.removeElementAt(index);
			}
		}
	}
	
	protected void removeSet(JBuilderSet set)
	{	//Support.Assert(set!=null,"Sets to be removed is null");
		if(set!=null)
		{
			
			Vector<?> list = (Vector<?>)setLists.get(set.getPartName());
			//Support.Assert(list!=null,"No Sets found to be deleted");
			if(list!=null)
			{
				int index = list.indexOf(set);
				if(index!=-1)
					list.removeElementAt(index);
			}
		}
	}
	
	protected void removeMemberFromSets(JBuilderObject part)
	{	
		boolean exp = (part!=null) && (part.getParent() == this);
		
		if(!exp) return; //do nothing if part is null or not in this model
		
		int seCount = sets.size();
		for(int i=0;i<seCount;i++)
		{	((JBuilderSet)sets.elementAt(i)).removeMember(part);
		}
	}
	
	////////////////////////////////  For the public ///////////////////////////////////////
	
	public MgaModel getIModel()
	{	return (MgaModel)ciObject;
	}
	
	public Vector<JBuilderObject> getChildren()
	{	return children;
	}
	
	public Vector<JBuilderModel> getModels()
	{	return models;
	}
	
	@SuppressWarnings("rawtypes")
	public Vector getModels(String partName)
	{	Vector<?> modelList = (Vector<?>)modelLists.get(partName);
		if(modelList==null)
			modelList = new Vector();
		return modelList;
	}
	
	@SuppressWarnings("rawtypes")
	public Vector getAtoms(String partName)
	{	Vector atomList = (Vector)atomLists.get(partName);
		if(atomList==null)
			atomList = new Vector();
		return atomList;
	}
	
	@SuppressWarnings("rawtypes")
	public Vector getReferences(String refPartName)
	{	Vector referenceList = (Vector) referenceLists.get(refPartName);
		if(referenceList==null)
			referenceList = new Vector();
		return referenceList;
		
	}
	
	@SuppressWarnings("rawtypes")
	public Vector getModelReference(String refPartName)
	{	Vector modelReferenceList = (Vector)modelReferenceLists.get(refPartName);
		if(modelReferenceList==null)
			modelReferenceList = new Vector();
		return modelReferenceList;
	}
	
//	public Vector getAtomReferences(String refPartName)
//	{	Vector atomReferenceList = (Vector)atomReferenceLists.get(refPartName);
//		if(atomReferenceList==null)
//			atomReferenceList = new Vector();
//		return atomReferenceList;
//	}
	
	@SuppressWarnings("rawtypes")
	public Vector getConnections(String name)
	{	Vector conns = findConnections(name);
		if(conns==null)
			conns = new Vector();
		return conns;
	}
	
	@SuppressWarnings("rawtypes")
	public Vector getSets(String name)
	{	Vector sts = findSets(name);
		if(sts==null)
			sts = new Vector();
		return sts;
	}
	
	@SuppressWarnings("rawtypes")
	public Vector getSets()
	{	return sets;
	}
	
	public void open()
	{	getIModel().open(3);//3==OPEN_READWRITE	
	}
	
//	public boolean isOpened()
//	{	// to be implemented
//		return false;
//	}
	
	public boolean close(boolean withchildren)
	{	try
		{	
			getIModel().close();	// what about with children
		}
		catch(Exception e)
		{	return false;
		}
		return true;
	}
	
	public boolean close()
	{	return close(false);
	}
	
//	public void getSelection(Vector list)
//	{	//Support.Assert(list!=null,"List provided is null");
//		// to be implemented
//	}
	
//	public void selectionRemoveAll()
//	{	// to be implemented
//	}
	
//	public void selectionRemove(Vector list)
//	{	// to be implemented
//	}
	
//	public void selectionRemove(JBuilderObject object)
//	{	// to be implemented
//	}
	
//	public void selectionAdd(Vector list)
//	{	// to be implemented
//	}
	
//	public void selectionAdd(JBuilderObject object)
//	{	// to be implemented
//	}
	
//	public void selectionSet(Vector list)
//	{	SelectionRemoveAll();
//		SelectionAdd(list);
//	}
	
//	public void selectionSet(JBuilderObject object)
//	{	SelectionRemoveAll();
//		SelectionAdd(object);
//	}
	
//	public void setCurrentAspent(String aspect)
//	{	// to be implemented
//	}
	
	public String getCurrentAspent()
	{	MgaMetaAspects asps = metaModel.getAspects();
		//Support.Assert(asps!=null,"No aspects found in the meta");
		MgaMetaAspect asp = asps.getItem(1);
		String ret = asp.getName();
		return ret;
	}
	
	public void getAspectNames(Vector<String> list)
	{	//Support.Assert(list!=null,"List provided is null");
		MgaMetaAspects asps = metaModel.getAspects();
		//Support.Assert(asps!=null,"No aspects found in the meta");
		int asCount = asps.getCount();
		for(int i=0;i<asCount;i++)
		{	MgaMetaAspect asp = asps.getItem(i);
			list.addElement(asp.getName());
		}
	}
	
	public JBuilderModel createNewModel(String partName)
	{	Vector<JBuilderModel> objectlist = (Vector<JBuilderModel>)modelLists.get(partName);
		MgaMetaRole role = metaModel.getRoleByName(partName);
		
		//Support.Assert(role!=null,"Role Name not found");
		MgaFCO i;
		i = getIModel().createChildObject(role);
		JBuilderModel o = JBuilderFactory.createModel(new MgaModel(i),this);
                //JBuilderModel o = new JBuilderModel(new MgaModel(i), this);
		o.resolve();
		objectlist.addElement(o);
		children.addElement(o);
		models.addElement(o);
		return o;
	}
	
	public JBuilderAtom createNewAtom(String partName)
	{	Vector<JBuilderAtom> objectlist = (Vector<JBuilderAtom>)atomLists.get(partName);
		MgaMetaRole role = metaModel.getRoleByName(partName);
		
		//Support.Assert(role!=null,"Role Name not found");
		MgaFCO i;
		i = getIModel().createChildObject(role);
		JBuilderAtom o = JBuilderFactory.createAtom(new MgaAtom(i),this);
                //JBuilderAtom o = new JBuilderAtom(new MgaAtom(i), this);
		o.resolve();
		objectlist.addElement(o);
		children.addElement(o);
		return o;
	}
	
	public JBuilderModelReference createNewModelReference(String refPartName, JBuilderObject refTo)
	{	
		@SuppressWarnings("unchecked")
		Vector<JBuilderModelReference> objectlist = (Vector<JBuilderModelReference>)modelReferenceLists.get(refPartName);
		MgaMetaRole role = metaModel.getRoleByName(refPartName);
		
		MgaFCO i;
		i = getIModel().createReference(role, refTo.getIObject());
		JBuilderModelReference o = JBuilderFactory.createModelRef(new MgaReference(i), this);
		objectlist.addElement(o);
		children.addElement(o);
		
		return o;
	}
	
	public JBuilderConnection createNewConnection(String connName, JBuilderObject source, JBuilderObject destination)
	{	MgaFCOs srcrefs = null;
		MgaFCOs dstrefs = null;
		MgaFCO  srcfco , dstfco;
		
		JBuilderObject src = source;
		
		Class<?> JBuilderReferencePortClass;
		Class<?> JBuilderReferenceClass;
		Class<?> JBuilderModelReferenceClass;
		Class<?> JBuilderModelClass;
		@SuppressWarnings("unused")
		Class<?> JBuilderAtomClass;
		try
		{
			JBuilderReferencePortClass = Class.forName("org.isis.gme.bon.JBuilderReferencePort");
			JBuilderReferenceClass = Class.forName("org.isis.gme.bon.JBuilderReference");
			JBuilderModelClass = Class.forName("org.isis.gme.bon.JBuilderModel");
			JBuilderAtomClass = Class.forName("org.isis.gme.bon.JBuilderAtom");
			JBuilderModelReferenceClass = Class.forName("org.isis.gme.bon.JBuilderModelReference");
		}
		catch(ClassNotFoundException ex)
		{	//Support.Assert(false, "In Model, Reference Port class not found");
			return null;
		}
				
		if(JBuilderReferencePortClass.isAssignableFrom(src.getClass()))
		{	srcfco = src.getIObject();
			src = ((JBuilderReferencePort)src).getOwner();
			while(JBuilderModelReferenceClass.isAssignableFrom(src.getClass()))
			{	 srcrefs = src.getIObject().createCollection();
				 src = ((JBuilderModelReference)src).getReferred();
			}
			if(!JBuilderModelClass.isAssignableFrom(src.getClass()))
                        {
				//Support.Assert(false,"Inconsistant state of the connection");
                        }
			
			srcfco = source.getIObject();
			if(src != ((JBuilderReferencePort)source).getPortObject().getParent())
                        {	//Support.Assert(false,"Inconsistant state of the connection");
                        }
		}
		else
		{	while(JBuilderReferenceClass.isAssignableFrom(src.getClass()) && src!=null)
			{	 srcrefs = src.getIObject().createCollection();
				 src = ((JBuilderReference)src).getReferred();
			}
			srcfco = source.getIObject();
		}
		
		JBuilderObject dst = destination;
		
		if(JBuilderReferencePortClass.isAssignableFrom(dst.getClass()))
		{	dstfco = dst.getIObject();
			dst = ((JBuilderReferencePort)dst).getOwner();
			while(JBuilderModelReferenceClass.isAssignableFrom(dst.getClass()))
			{	 dstrefs = dst.getIObject().createCollection();
				 dst = ((JBuilderModelReference)dst).getReferred();
			}
			if(!JBuilderModelClass.isAssignableFrom(dst.getClass()))
                        {
				//Support.Assert(false,"Inconsistant state of the connection");
                        }
			
			dstfco = destination.getIObject();
			if(dst != ((JBuilderReferencePort)destination).getPortObject().getParent())
                        {
				//Support.Assert(false,"Inconsistant state of the connection");
                        }
		}
		else
		{	while(JBuilderReferenceClass.isAssignableFrom(dst.getClass()) && dst!=null)
			{	 dstrefs = dst.getIObject().createCollection();
				 dst = ((JBuilderReference)dst).getReferred();
			}
	  
			dstfco = destination.getIObject();
		}
			
		MgaMetaRole role = metaModel.getRoleByName(connName);
		MgaFCO iconn;
		iconn = getIModel().createSimpleConn(role,srcfco,dstfco,srcrefs,dstrefs);
		
		MgaSimpleConnection sconn = new MgaSimpleConnection(iconn);
		JBuilderConnection conn = addConnection(sconn);
		conn.resolve();
		return conn;
	}
	
	public JBuilderSet createNewSet(String condName)
	{	@SuppressWarnings("unused")
	Vector<?> objectlist = (Vector<?>)setLists.get(condName);
		MgaMetaRole role = metaModel.getRoleByName(condName);
			
		MgaFCO i;
		i = getIModel().createChildObject(role);
		if(i==null)
		{	
			return null;
		}
		MgaSet icond = (MgaSet)i;
		JBuilderSet cond = addSet(icond);
		return cond;
		
	}
	
	public void traverseModels()
	{	int moCount = models.size();
		for(int i=0; i <moCount;i++)
		{	JBuilderModel model = (JBuilderModel)models.elementAt(i);
			model.traverseModels();
		}
		
	}
	
	public void traverseChildren()
	{	int chCount = children.size();
		for(int i=0; i <chCount;i++)
		{	JBuilderObject object = (JBuilderObject)children.elementAt(i);
			object.traverseChildren();
		}
	}
	
	public void initialize()
	{	
	}
	
	
	
}
