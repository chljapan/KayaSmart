package org.isis.gme.bon;

import org.isis.gme.mga.*;

public class JBuilderAtom extends JBuilderObject
{	
	
	public JBuilderAtom(MgaAtom iAtom, JBuilderModel parent)
	{	super(iAtom,parent);
		kindTitle = getMeta().getDisplayedName();
	}
	
	public MgaAtom getIAtom()
	{	return (MgaAtom)ciObject;
	}
	
	public void resolve()
	{
	}
	
	public void initialize()
	{
	}
	
}
