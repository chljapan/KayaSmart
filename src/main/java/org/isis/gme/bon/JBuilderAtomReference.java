package org.isis.gme.bon;

import org.isis.gme.mga.*;

public class JBuilderAtomReference extends JBuilderReference 
{
	//protected JBuilderObject ref;
	
	public JBuilderAtomReference(MgaReference iAtomRef, JBuilderModel parent)
	{	super(iAtomRef,parent);
	}
	
	public JBuilderObject getReferred()
	{	return ref;
	}
	
	public MgaReference getIAtomRef()
	{	return (MgaReference)ciObject;
	}
	
	public void initialize()
	{
	}
	
	
}
