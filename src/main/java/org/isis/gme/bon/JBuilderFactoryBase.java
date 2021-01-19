package org.isis.gme.bon;

import java.lang.reflect.*;
import java.util.*;
import org.isis.gme.mga.*;

public class JBuilderFactoryBase
{	
	static Hashtable<String, String> customModels;
	static Hashtable<String, String> customModelRefs;
	static Hashtable<String, String> customAtoms;
	static Hashtable<String, String> customAtomRefs;
	static Hashtable<String, String> customConns;
	static Hashtable<String, String> customSets;

	public static void Initialize()
	{	
            
            
        customModels = new Hashtable<String, String>();
		customModelRefs = new Hashtable<String, String>();
		customAtoms = new Hashtable<String, String>();
		customAtomRefs = new Hashtable<String, String>();
		customConns = new Hashtable<String, String>();
		customSets = new Hashtable<String, String>();
		
		// Add Custom implementations here
		
		
		
	}
	
	public static void AddCustomModel(String kindName, String className)
	{	customModels.put(kindName,className);
	}
	
	public static void AddCustomModelRef(String kindName, String className)
	{	customModelRefs.put(kindName,className);
	}
	
	public static void AddCustomAtom(String kindName, String className)
	{	customAtoms.put(kindName,className);
	}
	
	public static void AddCustomAtomRef(String kindName, String className)
	{	customAtomRefs.put(kindName,className);
	}
	
	public static void AddCustomConnection(String kindName, String className)
	{	customConns.put(kindName,className);
	}
	
	public static void AddCustomSet(String kindName, String className)
	{	customSets.put(kindName,className);
	}
	
	public static JBuilderModel CreateModel(MgaModel iModel, JBuilderModel parent)
	{	String kindName = iModel.getMeta().getName();
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
			{	ModelClass = Class.forName(className);
				argsType[0] = Class.forName("org.isis.gme.mga.MgaModel");
				argsType[1] = Class.forName("org.isis.gme.bon.JBuilderModel");
					
				Object args[] = new Object[2];
				args[0] = iModel;
				args[1] = parent;
				Constructor<?> ModelConst = ModelClass.getDeclaredConstructor(argsType);
				o = (JBuilderModel)ModelConst.newInstance(args);
			}
			catch(ClassNotFoundException e)
			{	Support.Assert(false,"User Class not found in CreateModel");
				return null;
			}
			catch(NoSuchMethodException e)
			{	Support.Assert(false,"User Class Constructor not found in CreateModel");
				return null;
			}
			catch(InstantiationException e)
			{	Support.Assert(false,"User Class Instanciation failed in CreateModel");
				return null;
			}
			catch(IllegalAccessException e)
			{	Support.Assert(false,"User Class Security access failed in CreateModel");
				return null;
			}
			catch(InvocationTargetException e)
			{	Support.Assert(false,"User Class Invocation target exception in CreateModel");
				return null;
			}
				
		}
		
		Support.Assert(o!=null,"Could Not instantiate the Model");
		return o;
	}
	
	/*public static JBuilderReference CreateReference(MgaReference iRef, JBuilderModel parent)
	{	int objType = iRef.getObjType();
		if(objType == objtype_enum.OBJTYPE_ATOM)
			return JBuilderFactory.CreateAtomRef(iRef,parent);
		if(objType == objtype_enum.OBJTYPE_MODEL)
			return JBuilderFactory.CreateModelRef(iRef,parent);
		JBuilderReference o = new JBuilderReference(iRef,parent);
		Support.Assert(o!=null, "Could Not instanciate the Reference");
		return o;
	}*/
	
	public static JBuilderModelReference CreateModelRef(MgaReference iModelRef, JBuilderModel parent)
	{	String kindName = iModelRef.getMeta().getName();
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
			{	ModelRefClass = Class.forName(className);
				argsType[0] = Class.forName("org.isis.gme.mga.MgaReference");
				argsType[1] = Class.forName("org.isis.gme.bon.JBuilderModel");
					
				Object args[] = new Object[2];
				args[0] = iModelRef;
				args[1] = parent;
				Constructor<?> ModelRefConst = ModelRefClass.getDeclaredConstructor(argsType);
				o = (JBuilderModelReference)ModelRefConst.newInstance(args);
			}
			catch(ClassNotFoundException e)
			{	Support.Assert(false,"User Class not found in CreateModelRef");
				return null;
			}
			catch(NoSuchMethodException e)
			{	Support.Assert(false,"User Class Constructor not found in CreateModelRef");
				return null;
			}
			catch(InstantiationException e)
			{	Support.Assert(false,"User Class Instanciation failed in CreateModelRef");
				return null;
			}
			catch(IllegalAccessException e)
			{	Support.Assert(false,"User Class Security access failed in CreateModelRef");
				return null;
			}
			catch(InvocationTargetException e)
			{	Support.Assert(false,"User Class Invocation target exception in CreateModelRef");
				return null;
			}
				
		}
		
		Support.Assert(o!=null,"Could Not instanciate the ModelRef");
		return o;
	}
	
	public static JBuilderAtom CreateAtom(MgaAtom iAtom, JBuilderModel parent)
	{	String kindName = iAtom.getMeta().getName();
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
			{	AtomClass = Class.forName(className);
				argsType[0] = Class.forName("org.isis.gme.mga.MgaAtom");
				argsType[1] = Class.forName("org.isis.gme.bon.JBuilderModel");
					
				Object args[] = new Object[2];
				args[0] = iAtom;
				args[1] = parent;
				Constructor<?> AtomConst = AtomClass.getDeclaredConstructor(argsType);
				o = (JBuilderAtom)AtomConst.newInstance(args);
			}
			catch(ClassNotFoundException e)
			{	Support.Assert(false,"User Class not found in CreateAtom");
				return null;
			}
			catch(NoSuchMethodException e)
			{	Support.Assert(false,"User Class Constructor not found in CreateAtom");
				return null;
			}
			catch(InstantiationException e)
			{	Support.Assert(false,"User Class Instanciation failed in CreateAtom");
				return null;
			}
			catch(IllegalAccessException e)
			{	Support.Assert(false,"User Class Security access failed in CreateAtom");
				return null;
			}
			catch(InvocationTargetException e)
			{	Support.Assert(false,"User Class Invocation target exception in CreateAtom");
				return null;
			}
				
		}
	
	Support.Assert(o!=null,"Could Not instantiate the Atom");
	return o;
}
	
public static JBuilderAtomReference CreateAtomRef(MgaReference iAtomRef, JBuilderModel parent)
{	String kindName = iAtomRef.getMeta().getName();
	String className = (String)customAtomRefs.get(kindName);
	JBuilderAtomReference o=null;
	if(className==null)
	{	className = (String)customAtomRefs.get("*");
		if(className==null)
			o = new JBuilderAtomReference(iAtomRef,parent);
	}
	if(className!=null)
	{	Class<?> AtomRefClass;
		Class<?> argsType[] = new Class[2];
				
		try
		{	AtomRefClass = Class.forName(className);
			argsType[0] = Class.forName("org.isis.gme.mga.MgaReference");
			argsType[1] = Class.forName("org.isis.gme.bon.JBuilderModel");
					
			Object args[] = new Object[2];
			args[0] = iAtomRef;
			args[1] = parent;
			Constructor<?> AtomRefConst = AtomRefClass.getDeclaredConstructor(argsType);
			o = (JBuilderAtomReference)AtomRefConst.newInstance(args);
		}
		catch(ClassNotFoundException e)
		{	//Support.Assert(false,"User Class not found in CreateAtomRef");
			return null;
		}
		catch(NoSuchMethodException e)
		{	//Support.Assert(false,"User Class Constructor not found in CreateAtomRef");
			return null;
		}
		catch(InstantiationException e)
		{	//Support.Assert(false,"User Class Instanciation failed in CreateAtomRef");
			return null;
		}
		catch(IllegalAccessException e)
		{	//Support.Assert(false,"User Class Security access failed in CreateAtomRef");
			return null;
		}
		catch(InvocationTargetException e)
		{	//Support.Assert(false,"User Class Invocation target exception in CreateAtomRef");
			return null;
		}
				
	}
	
	//Support.Assert(o!=null,"Could Not instanciate the AtomRef");
	return o;
}
	
public static JBuilderConnection CreateConn(MgaSimpleConnection iConn, JBuilderModel parent)
{	String kindName = iConn.getMeta().getName();
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
		{	ConnClass = Class.forName(className);
			argsType[0] = Class.forName("org.isis.gme.mga.MgaSimpleConnection");
			argsType[1] = Class.forName("org.isis.gme.bon.JBuilderModel");
					
			Object args[] = new Object[2];
			args[0] = iConn;
			args[1] = parent;
			Constructor<?> ConnConst = ConnClass.getDeclaredConstructor(argsType);
			o = (JBuilderConnection)ConnConst.newInstance(args);
		}
		catch(ClassNotFoundException e)
		{	//Support.Assert(false,"User Class not found in CreateConn");
			return null;
		}
		catch(NoSuchMethodException e)
		{	//Support.Assert(false,"User Class Constructor not found in CreateConn");
			return null;
		}
		catch(InstantiationException e)
		{	//Support.Assert(false,"User Class Instanciation failed in CreateConn");
			return null;
		}
		catch(IllegalAccessException e)
		{	//Support.Assert(false,"User Class Security access failed in CreateConn");
			return null;
		}
		catch(InvocationTargetException e)
		{	//Support.Assert(false,"User Class Invocation target exception in CreateConn");
			return null;
		}
				
	}
		
	//Support.Assert(o!=null,"Could Not instanciate the Connection");
	return o;
}
	
public static JBuilderSet CreateSet(MgaSet iSet, JBuilderModel parent)
{	String kindName = iSet.getMeta().getName();
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
		{	SetClass = Class.forName(className);
			argsType[0] = Class.forName("org.isis.gme.mga.MgaSet");
			argsType[1] = Class.forName("org.isis.gme.bon.JBuilderModel");
					
			Object args[] = new Object[2];
			args[0] = iSet;
			args[1] = parent;
			Constructor<?> SetConst = SetClass.getDeclaredConstructor(argsType);
			o = (JBuilderSet)SetConst.newInstance(args);
		}
		catch(ClassNotFoundException e)
		{	//Support.Assert(false,"User Class not found in CreateSet");
			return null;
		}
		catch(NoSuchMethodException e)
		{	//Support.Assert(false,"User Class Constructor not found in CreateSet");
			return null;
		}
		catch(InstantiationException e)
		{	//Support.Assert(false,"User Class Instanciation failed in CreateSet");
			return null;
		}
		catch(IllegalAccessException e)
		{	//Support.Assert(false,"User Class Security access failed in CreateSet");
			return null;
		}
		catch(InvocationTargetException e)
		{	//Support.Assert(false,"User Class Invocation target exception in CreateSet");
			return null;
		}
				
	}
	
	//Support.Assert(o!=null,"Could Not instanciate the Set");
	return o;
}
	
}
