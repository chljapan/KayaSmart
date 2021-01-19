package org.isis.gme.bon;

import org.isis.gme.mga.*;

public class JBuilderConnection extends JBuilderObject
{	
	protected JBuilderModel owner;
	protected JBuilderObject src;
	protected JBuilderObject dst;
		
	public JBuilderConnection(MgaSimpleConnection iConn, JBuilderModel parent)
	{	super(iConn,parent);
		owner = this.parent;
		
	}
		
	public void resolve()
	{	
		
		MgaFCOs isrcrefs;
		MgaFCOs idstrefs;
		MgaFCO isrc, idst;
		
		isrc = ((MgaSimpleConnection)ciObject).getSrc();
		isrcrefs = ((MgaSimpleConnection)ciObject).getSrcReferences();
		
		Class<?> JBuilderModelReferenceClass;
		try
		{
			JBuilderModelReferenceClass = Class.forName("org.isis.gme.bon.JBuilderModelReference");
		}
		catch(ClassNotFoundException ex)
		{	
			return;
		}
		
		int isCount = isrcrefs.getCount();
		if(isCount>0)
		{	MgaFCO fr = isrcrefs.getItem(0);
			src = JBuilder.theInstance.findObject(fr);
				
			if(JBuilderModelReferenceClass.isAssignableFrom(src.getClass()))
			{	
				src = ((JBuilderModelReference)src).findPortRef(isrc);
			}
		}
		else
		{	
			src = JBuilder.theInstance.findObject(isrc);
		}
		
		idst = ((MgaSimpleConnection)ciObject).getDst();
		idstrefs = ((MgaSimpleConnection)ciObject).getDstReferences();
		
		int idCount = idstrefs.getCount();
		if(idCount>0)
		{	MgaFCO fr = idstrefs.getItem(0);
			dst = JBuilder.theInstance.findObject(fr);
			if(JBuilderModelReferenceClass.isAssignableFrom(dst.getClass()))
			{	
				dst = ((JBuilderModelReference)dst).findPortRef(idst);
			}
		}
		else
		{	dst = JBuilder.theInstance.findObject(idst);
		}
		
		src.addOutConnection(kindName,this);
		dst.addInConnection(kindName,this);
	}
	
	public JBuilderModel getOwner()
	{	return owner;
	}
	
	public JBuilderObject getSource()
	{	return src;
	}
	
	public JBuilderObject getDestination()
	{	return dst;
	}
	
	public MgaSimpleConnection getIConnection()
	{	return (MgaSimpleConnection)ciObject;
	}
	
	public void initialize()
	{
	}
	
	
}