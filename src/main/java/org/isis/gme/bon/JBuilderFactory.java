/*
 * Copyright (c) 2004, Vanderbilt University
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without written agreement is
 * hereby granted, provided that the above copyright notice, the following
 * two paragraphs and the author appear in all copies of this software.
 * 
 * IN NO EVENT SHALL THE VANDERBILT UNIVERSITY BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
 * OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE VANDERBILT
 * UNIVERSITY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * THE VANDERBILT UNIVERSITY SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
 * ON AN "AS IS" BASIS, AND THE VANDERBILT UNIVERSITY HAS NO OBLIGATION TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 * Author: Brian W Williams 
  */

package org.isis.gme.bon;

import java.lang.reflect.*;
import java.util.*;
import org.isis.gme.mga.*;


public class JBuilderFactory
{	
	static Hashtable<String, String> customModels;
	static Hashtable<String, String> customModelRefs;
	static Hashtable<String, String> customAtoms;
	static Hashtable<String, String> customAtomRefs;
	static Hashtable<String, String> customConns;
	static Hashtable<String, String> customSets;
	
	static ClassLoader BONComponentClassLoader = null;
	
	public static void setClassLoader(ClassLoader loader)
	{
		BONComponentClassLoader = loader;
	}

	public static void Initialize()
	{	
        customModels    = new Hashtable<String, String>();
		customModelRefs = new Hashtable<String, String>();
		customAtoms     = new Hashtable<String, String>();
		customAtomRefs  = new Hashtable<String, String>();
		customConns     = new Hashtable<String, String>();
		customSets      = new Hashtable<String, String>();
	}
	
	public static void addCustomModel(String kindName, String className)
	{	
        customModels.put(kindName,className);
	}
	
	public static void addCustomModelRef(String kindName, String className)
	{	
        customModelRefs.put(kindName,className);
	}
	
	public static void addCustomAtom(String kindName, String className)
	{	
        customAtoms.put(kindName,className);
	}
	
	public static void addCustomAtomRef(String kindName, String className)
	{	
        customAtomRefs.put(kindName,className);
	}
	
	public static void addCustomConnection(String kindName, String className)
	{	
        customConns.put(kindName,className);
	}
	
	public static void addCustomSet(String kindName, String className)
	{	
        customSets.put(kindName,className);
	}
	
	public static JBuilderModel createModel(MgaModel iModel, JBuilderModel parent) throws BONException
	{	
        String kindName = iModel.getMeta().getName();
		String className = (String)customModels.get(kindName);
		JBuilderModel o=null;
		if(className==null)
		{	className = (String)customModels.get("*");
			if(className==null)
			{	o = new JBuilderModel(iModel,parent);
			}
		}
		if(className!=null)
		{	Class<?> ModelClass;
			Class<?> argsType[] = new Class[2];
				
			try
			{	
				ModelClass = Class.forName(className,true,BONComponentClassLoader);
				argsType[0] = Class.forName("org.isis.gme.mga.MgaModel");
				argsType[1] = Class.forName("org.isis.gme.bon.JBuilderModel");
					
				Object args[] = new Object[2];
				args[0] = iModel;
				args[1] = parent;
				Constructor<?> ModelConst = ModelClass.getDeclaredConstructor(argsType);
				o = (JBuilderModel)ModelConst.newInstance(args);
			}
			catch(ClassNotFoundException e)
			{	
				throw new BONException("User Custom Class not found in JBuilderFactory.createModel");
			}
			catch(NoSuchMethodException e)
			{	
				throw new BONException("User Custom Class Constructor not found in JBuilderFactory.createModel");
			}
			catch(InstantiationException e)
			{	
				throw new BONException("User Custom Class Instantiation falied in JBuilderFactory.createModel");
			}
			catch(IllegalAccessException e)
			{	
				throw new BONException("User Custom Class Security access failed in CreateModel");
			}
			catch(InvocationTargetException e)
			{	
				throw new BONException("User Custom Class Invocation target exception in CreateModel");
			}
				
		}
		
		if(o==null)throw new BONException("Could not Instantiate Model");
		return o;
	}
	
	public static JBuilderReference createReference(MgaReference iRef, JBuilderModel parent) throws BONException
	{	
		String kindName = iRef.getMeta().getName();
		String className = (String)customAtomRefs.get(kindName);
		JBuilderReference o = null;
		if(className==null)
		{	className = (String)customAtomRefs.get("*");
			if(className==null)
				o = new JBuilderReference(iRef,parent);
		}
		if(className!=null)
		{	
			Class<?> RefClass;
			Class<?> argsType[] = new Class[2];
				
			try
			{	RefClass = Class.forName(className,true,BONComponentClassLoader);
				argsType[0] = Class.forName("org.isis.gme.mga.MgaReference");
				argsType[1] = Class.forName("org.isis.gme.bon.JBuilderModel");
					
				Object args[] = new Object[2];
				args[0] = iRef;
				args[1] = parent;
				Constructor<?> RefConst = RefClass.getDeclaredConstructor(argsType);
				o = (JBuilderReference)RefConst.newInstance(args);
			}
			catch(ClassNotFoundException e)
			{	
				throw new BONException("User Custom Class not found in JBuilderFactory.createReference");
			}
			catch(NoSuchMethodException e)
			{	
				throw new BONException("User Custom Class Constructor not found in JBuilderFactory.createReference");
			}
			catch(InstantiationException e)
			{	
				throw new BONException("User Custom Class Instantiation falied in JBuilderFactory.createReference");
			}
			catch(IllegalAccessException e)
			{	
				throw new BONException("User Custom Class Security access failed in CreateReference");
			}
			catch(InvocationTargetException e)
			{	
				throw new BONException("User Custom Class Invocation target exception in CreateReference");
			}
				
		}
		
		if(o==null)throw new BONException("Could not Instantiate Reference");
		return o;
	}
	
	public static JBuilderModelReference createModelRef(MgaReference iModelRef, JBuilderModel parent) throws BONException
	{	
        String kindName = iModelRef.getMeta().getName();
		String className = (String)customModelRefs.get(kindName);
		JBuilderModelReference o=null;
		if(className==null)
		{	className = (String)customModelRefs.get("*");
			if(className==null)
				o = new JBuilderModelReference(iModelRef,parent);
		}
		if(className!=null)
		{	Class<?> ModelRefClass;
			Class<?> argsType[] = new Class[2];
				
			try
			{	ModelRefClass = Class.forName(className,true,BONComponentClassLoader);
				argsType[0] = Class.forName("org.isis.gme.mga.MgaReference");
				argsType[1] = Class.forName("org.isis.gme.bon.JBuilderModel");
					
				Object args[] = new Object[2];
				args[0] = iModelRef;
				args[1] = parent;
				Constructor<?> ModelRefConst = ModelRefClass.getDeclaredConstructor(argsType);
				o = (JBuilderModelReference)ModelRefConst.newInstance(args);
			}
			catch(ClassNotFoundException e)
			{	
				throw new BONException("User Custom Class not found in JBuilderFactory.createModel");
			}
			catch(NoSuchMethodException e)
			{	
				throw new BONException("User Custom Class Constructor not found in JBuilderFactory.createModel");
			}
			catch(InstantiationException e)
			{	
				throw new BONException("User Custom Class Instantiation falied in JBuilderFactory.createModel");
			}
			catch(IllegalAccessException e)
			{	
				throw new BONException("User Custom Class Security access failed in CreateModel");
			}
			catch(InvocationTargetException e)
			{	
				throw new BONException("User Custom Class Invocation target exception in CreateModel");
			}
				
		}
		
		if(o==null) throw new BONException("Could not Instantiate Model Reference");
		return o;
	}
	
	public static JBuilderAtom createAtom(MgaAtom iAtom, JBuilderModel parent) throws BONException
	{	
        String kindName = iAtom.getMeta().getName();
		String className = (String)customAtoms.get(kindName);
		JBuilderAtom o=null;
		if(className==null)
		{	className = (String)customAtoms.get("*");
			if(className==null)
				o = new JBuilderAtom(iAtom,parent);
		}
		if(className!=null)
		{	Class<?> AtomClass;
			Class<?> argsType[] = new Class[2];
			try
			{	AtomClass = Class.forName(className,true,BONComponentClassLoader);
				argsType[0] = Class.forName("org.isis.gme.mga.MgaAtom");
				argsType[1] = Class.forName("org.isis.gme.bon.JBuilderModel");
					
				Object args[] = new Object[2];
				args[0] = iAtom;
				args[1] = parent;
				Constructor<?> AtomConst = AtomClass.getDeclaredConstructor(argsType);
				o = (JBuilderAtom)AtomConst.newInstance(args);
			}
			catch(ClassNotFoundException e)
			{	
				throw new BONException("User Custom Class not found in JBuilderFactory.createModel");
			}
			catch(NoSuchMethodException e)
			{	
				throw new BONException("User Custom Class Constructor not found in JBuilderFactory.createModel");
			}
			catch(InstantiationException e)
			{	
				throw new BONException("User Custom Class Instantiation falied in JBuilderFactory.createModel");
			}
			catch(IllegalAccessException e)
			{	
				throw new BONException("User Custom Class Security access failed in CreateModel");
			}
			catch(InvocationTargetException e)
			{	
				throw new BONException("User Custom Class Invocation target exception in CreateModel");
			}
				
		}
		
		if(o==null) throw new BONException("Could not Instantiate Atom");
		return o;
    }
	
 
	
    public static JBuilderConnection createConn(MgaSimpleConnection iConn, JBuilderModel parent) throws BONException
    {	
        String kindName = iConn.getMeta().getName();
    	String className = (String)customConns.get(kindName);
    	JBuilderConnection o=null;
    	if(className==null)
    	{	className = (String)customConns.get("*");
    		if(className==null)
    			o = new JBuilderConnection(iConn,parent);
    	}
    	else
    	{	Class<?> ConnClass;
    		Class<?> argsType[] = new Class[2];
    		try
    		{	ConnClass = Class.forName(className,true,BONComponentClassLoader);
    			argsType[0] = Class.forName("org.isis.gme.mga.MgaSimpleConnection");
    			argsType[1] = Class.forName("org.isis.gme.bon.JBuilderModel");
    					
    			Object args[] = new Object[2];
    			args[0] = iConn;
    			args[1] = parent;
    			Constructor<?> ConnConst = ConnClass.getDeclaredConstructor(argsType);
    			o = (JBuilderConnection)ConnConst.newInstance(args);
    		}
			catch(ClassNotFoundException e)
			{	
				throw new BONException("User Custom Class not found in JBuilderFactory.createModel");
			}
			catch(NoSuchMethodException e)
			{	
				throw new BONException("User Custom Class Constructor not found in JBuilderFactory.createModel");
			}
			catch(InstantiationException e)
			{	
				throw new BONException("User Custom Class Instantiation falied in JBuilderFactory.createModel");
			}
			catch(IllegalAccessException e)
			{	
				throw new BONException("User Custom Class Security access failed in CreateModel");
			}
			catch(InvocationTargetException e)
			{	
				throw new BONException("User Custom Class Invocation target exception in CreateModel");
			}
				
		}
		
		if(o==null) throw new BONException("Could not Instantiate Connection");
		return o;
    }
	
    public static JBuilderSet createSet(MgaSet iSet, JBuilderModel parent) throws BONException
    {	
        String kindName = iSet.getMeta().getName();
    	String className = (String)customSets.get(kindName);
    	JBuilderSet o=null;
    	if(className==null)
    	{	className = (String)customSets.get("*");
    		if(className==null)
    			o = new JBuilderSet(iSet,parent);
    	}
    	if(className!=null)
    	{	Class<?> SetClass;
    		Class<?> argsType[] = new Class[2];
    		try
    		{	SetClass = Class.forName(className,true,BONComponentClassLoader);
    			argsType[0] = Class.forName("org.isis.gme.mga.MgaSet");
    			argsType[1] = Class.forName("org.isis.gme.bon.JBuilderModel");
    					
    			Object args[] = new Object[2];
    			args[0] = iSet;
    			args[1] = parent;
    			Constructor<?> SetConst = SetClass.getDeclaredConstructor(argsType);
    			o = (JBuilderSet)SetConst.newInstance(args);
    		}
			catch(ClassNotFoundException e)
			{	
				throw new BONException("User Custom Class not found in JBuilderFactory.createModel");
			}
			catch(NoSuchMethodException e)
			{	
				throw new BONException("User Custom Class Constructor not found in JBuilderFactory.createModel");
			}
			catch(InstantiationException e)
			{	
				throw new BONException("User Custom Class Instantiation falied in JBuilderFactory.createModel");
			}
			catch(IllegalAccessException e)
			{	
				throw new BONException("User Custom Class Security access failed in CreateModel");
			}
			catch(InvocationTargetException e)
			{	
				throw new BONException("User Custom Class Invocation target exception in CreateModel");
			}
				
		}
		
		if(o==null)throw new BONException("Could not Instantiate Set");
		return o;
    }	
}
