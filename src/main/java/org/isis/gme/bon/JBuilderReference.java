package org.isis.gme.bon;

import org.isis.gme.mga.*;

public class JBuilderReference extends JBuilderObject 
{	
	protected JBuilderObject ref;
	
	public JBuilderReference(MgaReference iRef, JBuilderModel parent)
	{	super(iRef,parent);
		ref = null;
		
	}
	
	public void resolve()
	{	MgaFCO i;
		try
		{
			i = getIRef().getReferred();
		}
		catch(Exception e)//handle when the referred is null
		{
			ref = null;
			return;
		}		
			ref = JBuilder.theInstance.findObject(i);
			
	}

	public void initialize()
	{
	}
	
	
	///////////////////////////////For the users ///////////////////////////////////
	
	public MgaReference getIRef()
	{	return (MgaReference)ciObject;
	}
	
	public JBuilderObject getReferred()
	{	return ref;
	}
	
	boolean putReferred(JBuilderObject nref)
	{	ref = nref;
		return false;
	}
	
	int getReferredType()
	{	if(ref==null) return MgaObject.OBJTYPE_NULL;
		return ref.getObjType();
	}
	
	
	
}
