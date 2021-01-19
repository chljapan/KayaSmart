package org.isis.gme.bon;

import org.isis.gme.mga.*;

public class JBuilderReferencePort extends JBuilderObject 
{	
	protected JBuilderObject portobj;
	protected JBuilderModelReference owner;
	
	public JBuilderReferencePort(MgaFCO iPort, JBuilderModelReference o)
	{	super(iPort, o.getParent(),false);
		o.setPortRef(iPort,this);
	}
	
	public MgaFCO getIObject()
	{	return ciObject;
	}
	
	public void resolve()
	{	JBuilderObject o = JBuilder.theInstance.findObject(getIObject());
		
		portobj = o;
	}
	
	public JBuilderModelReference getOwner()
	{	return owner;
	}
	
	public JBuilderObject getPortObject()
	{	return portobj;	
	}
	
	public void initialize()
	{
	}
	
}
