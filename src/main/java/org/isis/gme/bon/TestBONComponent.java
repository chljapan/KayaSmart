/*
 * Created on Feb 23, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.isis.gme.bon;

import java.util.*;
import org.isis.gme.mga.MgaObject;

import javax.swing.JOptionPane;

/**
 * @author BrianW
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TestBONComponent implements BONComponent {

	/**
	 * 
	 */
	public TestBONComponent() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.isis.gme.bon.BONComponent#invokeEx(org.isis.gme.bon.JBuilder, org.isis.gme.bon.JBuilderObject, java.util.Collection, int)
	 */
	public void invokeEx(
		JBuilder builder,
		JBuilderObject focus,
		Collection<?> selected,
		int param) 
	{
		String msg = new String();
	
		if(focus == null || selected.size() == 0)
		{
			msg = "This interpreter must be run with objects selected";
		}
		else
		for(Iterator<?> selectedIter = selected.iterator();selectedIter.hasNext();)
		{
			JBuilderObject selection = (JBuilderObject)selectedIter.next();
			if(selection.getObjType()==MgaObject.OBJTYPE_SET)
			{
				msg = msg + "Set " + selection.getName() + " selected. \n Contains: ";
				Vector<?> setMembers = ((JBuilderSet)selection).getMembers();
				int size = setMembers.size();
				if(size == 0)
					msg = msg + "<EMPTY SET>\n";
				else
					msg = msg + "\n";
				
				for(int i=0; i<size; i++)
				{
					msg = msg + "     " + ((JBuilderObject)setMembers.elementAt(i)).getName() + "\n";
				}
			}
			if(selection.getObjType()==MgaObject.OBJTYPE_REFERENCE)
			{
				msg = msg + "Reference " + selection.getName() + " selected. \n Refers to: ";
				JBuilderObject referred = ((JBuilderReference)selection).getReferred();
				if(referred == null)
					msg = msg + "NULL\n";
				else
				{
					msg = msg + referred.getName() + "\n";
					if(selection.getClass().getName().equals("org.isis.gme.bon.JBuilderModelReference"))
					{
						Vector<?> ports = ((JBuilderModelReference)selection).getReferencePorts();
						msg = msg + "     " + ports.size() + " Reference Ports.\n";
						for(int j=0; j<ports.size(); j++)
						{
							JBuilderReferencePort port = (JBuilderReferencePort)ports.elementAt(j);
							msg = msg + "     " + port.getName() + " connected to:";
							msg = msg + getConnectedNames(port);
							msg = msg + getConnectedNames(port.getPortObject());
							msg = msg + "\n";
						}
					}
				}
			}
			
		}
		if(msg.length() == 0)
			msg = "No sets or references selected.";
		JOptionPane.showMessageDialog(null,msg);

	}

	/* (non-Javadoc)
	 * @see org.isis.gme.bon.BONComponent#registerCustomClasses()
	 */
	public void registerCustomClasses() {
		// TODO Auto-generated method stub

	}

	private String getConnectedNames(JBuilderObject obj)
	{
		String msg = new String();
		Hashtable<?, ?> conns = obj.getInConnections();
		for(Enumeration<?> connEnum = conns.elements(); connEnum.hasMoreElements();)
		{
			Vector<?> sources = (Vector<?>)connEnum.nextElement();
			for(Enumeration<?> srcEnum = sources.elements(); srcEnum.hasMoreElements();)
			{
				JBuilderObject src = ((JBuilderConnection)srcEnum.nextElement()).getSource();
				msg = msg + " " + src.getName();	
			}
		}
		conns = obj.getOutConnections();
		for(Enumeration<?> connEnum = conns.elements(); connEnum.hasMoreElements();)
		{
			Vector<?> destinations = (Vector<?>)connEnum.nextElement();
			for(Enumeration<?> dstEnum = destinations.elements(); dstEnum.hasMoreElements();)
			{
				JBuilderObject dst = ((JBuilderConnection)dstEnum.nextElement()).getDestination();
				msg = msg + " " + dst.getName();
			}
		}
		return msg;
	}
}
