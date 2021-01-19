package org.isis.gme.bon;

import java.util.*;
import org.isis.gme.mga.*;

public class JBuilderSet extends JBuilderObject 
{	
	Vector<JBuilderObject> members;
	
	public JBuilderSet(MgaSet iCond, JBuilderModel own)
	{
		super(iCond,own);
		members = new Vector<JBuilderObject>();
	}
	
//	protected void insertMember(JBuilderObject member)
//	{
//	}
	
	public void resolve()
	{	MgaFCOs psa = ((MgaSet)ciObject).getMembers();
		int psCount = psa.getCount();
		for(int i=0;i<psCount;i++)
		{	MgaFCO iter = psa.getItem(i);
			addMemberInternal(JBuilder.theInstance.findObject(iter));
		}
	}
	
        // used internally only, called by resolve()
        protected boolean addMemberInternal(JBuilderObject mem)    
        {
                if(members.contains(mem))
                {       return false;
                }
                members.addElement(mem);
                return true;
               
        } 

//	public long getObjId()
//	{	return -1;
//	}
	
	public JBuilderModel getOwner()
	{	return getParent();
	}
	
	public Vector<JBuilderObject> getMembers()
	{	return members;
	}
	
	public boolean addMember(JBuilderObject mem)	
	{	//Support.Assert(mem!=null && mem.GetParent() == this.parent,"JBuilderSet AddMember");
		
		if(members.contains(mem))
		{	return false;
		}
		((MgaSet)ciObject).addMember(mem.getIObject());
		members.addElement(mem);
		return true;
		
	}
	
	public boolean removeMember(JBuilderObject mem)
	{	//Support.Assert(mem!=null && mem.GetParent() == this.parent,"JBuilderSet AddMember");
		
		if(!members.contains(mem))
		{	return false;
		}
		((MgaSet)ciObject).removeMember(mem.getIObject());
		members.removeElement(mem);
		return true;
		
		
	}
	public void initialize()
	{
	}
	
	
}
