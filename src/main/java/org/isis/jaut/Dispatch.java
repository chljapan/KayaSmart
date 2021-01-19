/**
 * Copyright (C) 2002, Vanderbilt University
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 *
 * 1.   Redistributions of source code must retain the above copyright 
 *      notice, this list of conditions and the following disclaimer.
 *
 * 2.   Redistributions in binary form must reproduce the above copyright 
 *      notice, this list of conditions and the following disclaimer in 
 *      the documentation and/or other materials provided with the 
 *      distribution.
 *
 * 3.   Neither the name of Vanderbilt University nor the names of its 
 *      contributors may be used to endorse or promote products derived 
 *      from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR 
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY 
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Miklos Maroti
 */

package org.isis.jaut;

/**
 * The <code>Dispatch</code> class enables Java programs to invoke methods
 * and access properties of an Automation object. 
 * Embedded in the class, there is an <code>IDispatch</code> interface pointer. 
 * The methods of this class should be called always from the same thread (or from the same Apartment).
 */
public class Dispatch extends ApartmentObject
{
	/**
	 * This holds an <code>IDispatch</code> interface pointer.
	 */
	public int pDispatch = 0;
    
	/**
	 * Creates a <code>NULL</code> Dispatch object. The interface pointer of the Dispatch object
	 * must be set to a non-<code>NULL</code> value before any other method can be invoked.
	 *
	 * @see #attach(Variant)
	 * @see #attach(Dispatch)
	 */
	public Dispatch()
	{
	}
    
    /**
     * Creates a Dispatch object pointing to the given target interface pointer.
     * @param pointer  Interface pointer to attach
     */
    public Dispatch( int target )
    {
        pDispatch = 0;
        if( target != 0 )
            attach(target);
    }

	/**
	 * Creates a Dispatch object from a {@link Variant}.
	 *
	 * @param target a Variant holding an interface pointer to an Automation object.
	 * @see Variant#getDispatch
	 * @see #attach(Variant)
	 */
	public Dispatch(Variant target) throws ComException
	{
		if( target != null )
			attach(target);
	}

	/**
	 * Tests if the embedded <code>IDispatch</code> interface pointer is <code>NULL</code>.
	 *
	 * @return <code>true</code> if the embedded <code>IDispatch</code> interface pointer is <code>NULL</code>,
	 *	<code>false</code> otherwise.
	 * @see #attach(Variant)
	 * @see #attach(Dispatch)
	 */
	public boolean isNull()
	{
		return pDispatch == 0;
	}

	/**
	 * Sets the interface pointer of the Dispatch object to the interface pointer stored in the Variant. 
	 * If the target Variant holds an <code>IUnknown</code> interface pointer, the 
	 * <code>IUnknown::QueryInterface</code> method is used to get the <code>IDispatch</code> pointer. 
	 * COM object references are managed according to the COM rules 
	 * using the <code>IUnknown::AddRef</code> and <code>IUnknown::Release</code> methods.
	 *
	 * @param target a Variant of type <code>VT_DISPATCH</code>, <code>VT_DISPATCH|VT_BYREF</code>,
	 *	<code>VT_UNKNOWN</code> or <code>VT_UNKNOWN|VT_BYREF</code> holding the new interface pointer.
	 * @throws ComException is the <code>IUnknown::QueryInterface</code> method fails.
	 * @throws JAutException if the target Variant does not have its underlying <code>VARIANT</code> created,
	 *	or it is not of the proper type.
	 */
	public native void attach(Variant target) throws ComException;

	/**
	 * Sets the interface pointer of the Dispatch object to the interface pointer stored in <code>target</code>.
	 * COM object references are managed according to the COM rules 
	 * using the <code>IUnknown::AddRef</code> and <code>IUnknown::Release</code> methods.
	 * 
	 * @param target a Dispatch object holding the new interface pointer.
	 */
	public native void attach(Dispatch target);
    
    
    /**
     * Sets the interface pointer of the given interface pointer stored in <code>target</code>.
     * COM object references are managed according to the COM rules 
     * using the <code>IUnknown::AddRef</code> and <code>IUnknown::Release</code> methods.
     * @param target an integer representation if the new interface pointer.
     */
    public native void attach(int target);

	/**
	 * Releases the interface pointer of the Dispatch object. If the interface pointer is not <code>NULL</code>,
	 * it is released using the <code>IDispatch::Release</code> method. Then the embedded interface pointer
	 * is set to <code>NULL</code>.
	 */
	public native void release();

	/**
	 * Clears and releases the Dispatch object, can be called from any Thread.
	 * Calls the {@link #releaseInApartment} method if the underlying interface
	 * pointer is not <code>NULL</code>.
	 */
	public void finalize()
	{
		if( ! isNull() )
			finalizeInApartment();
        else
            Apartment.currentApartment().decrementObjectCount();
	}

	/**
	 * Returns a clone of this object. The copy will have the same <code>IDispatch</code> interface pointer,
	 * and the reference count is incremented using the <code>IDispatch::AddRef</code> method.
	 *
	 * @return the clone of this object, which is of type <code>Dispatch</code>.
	 */
	public Object clone()
	{
		Dispatch disp = new Dispatch();
		disp.attach(this);
		return disp;
	}

	/**
	 * Returns a hash code value for the object. The actual returned value is the <code>IUnknown</code>
	 * interface pointer of this Dispatch object, obtained by using the </code>IUnknown::QueryInterface</code> 
	 * method.
	 *
	 * @return a hash code value for this object.
	 */
	public native int hashCode();

	/**
	 * Tests if <code>target</code> is a Dispatch object referring to the same Automation object as this object.
	 * 
	 * @return <code>true</code> if <code>target</code> is a Dispatch object referring to the same Automation 
	 *	object as this object, <code>false</code> otherwise.
	 */
	public boolean equals(Object target)
	{
		if( !(target instanceof Dispatch) )
			return false;

		return ((Dispatch)target).hashCode() == hashCode();
	}

	/**
	 * Changes the embedded interface pointer to another interface pointer on the same Automation object.
	 * This method uses the <code>IIDFromString</code> and <code>IUnknown::QueryInterface</code> Automation 
	 * functions.
	 *
	 * @param iID the interface ID of the new interface, in the format of "{XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX}".
	 *	The retrieved interface must be derived from <code>IDispatch</code>.
	 * @throws JAutException if the current interface pointer of this object is <code>NULL</code>.
	 * @throws ComException if either <code>IIDFromString</code> or <code>IUnknown::QueryInterface</code> fails.
	 */
	public native void changeInterface(String iID) throws ComException;

	/**
	 * Returns a new Dispatch object with another interface pointer to the same Automation object.
	 *
	 * @param iID the interface ID of the new interface, in the format of "{XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX}".
	 *	The retrived interface must be derived from <code>IDispatch</code>.
	 * @throws JAutException if the current interface pointer of this object is <code>NULL</code>.
	 * @throws ComException if either <code>IIDFromString</code> or <code>IUnknown::QueryInterface</code> fails.
	 * @see #changeInterface
	 */
	public Dispatch queryInterface(String iID) throws ComException
	{
		Dispatch disp = new Dispatch();
		disp.attach(this);
		disp.changeInterface(iID);
		return disp;
	}

	/**
	 * A class context allowing in-process servers.
	 * The code that creates and manages objects of this class run in the same process as the caller.
	 *
	 * @see #attachNewInstance
	 */
	public static final int CLSCTX_INPROC_SERVER = 1;

	/**
	 * A class context allowing in-process handlers.
	 * An in-process DLL is managing the remote access of objects of this class.
	 *
	 * @see #attachNewInstance
	 */
	public static final int CLSCTX_INPROC_HANDLER = 2;

	/**
	 * A class context allowing local servers.
	 * The code that creates and manages objects of this class is an executable EXE running in a separate process
	 * on the same machine.
	 *
	 * @see #attachNewInstance
	 */
	public static final int CLSCTX_LOCAL_SERVER = 4;

	/**
	 * Creates an Automation object of a given program ID, and attaches this Dispatch object to it.
	 * First, the existing interface pointer is released.
	 * Then the program ID is transformed to the class ID (<code>CLSID</code>) of the COM class that will create
	 * the Automation object using the <code>CLSIDFromProgID</code> Automation function. Finally,
	 * the <code>CoCreateInstance</code> Automation function is invoked.
	 *
	 * @param progID the program ID of the Automation object to be created.
	 * @param context the in which the code that manages the newly created object will run.
	 *	Acceptable values are combined from the <code>CLSCTX_XXX</code> constants.
	 * @throws ComException if either <code>CLSIDFromProgID</code> or <code>CoCreateInstance</code> fails.
	 * @see #createInstance
	 */
	public native void attachNewInstance(String progID, int context) throws ComException;

	/**
	 * Creates an Automation object of a given program ID and returns the new Dispatch object.
	 *
	 * @param progID the program ID of the Automation object to be created.
	 * @param context the in which the code that manages the newly created object will run.
	 *	Acceptable values are combined from the <code>CLSCTX_XXX</code> constants.
	 * @see #attachNewInstance
	 */
	public static Dispatch createInstance(String progID, int context) throws ComException
	{
		Dispatch disp = new Dispatch();
		disp.attachNewInstance(progID, context);
		return disp;
	}

	/**
	 * Sets the interface pointer of the Dispatch object to a running object. First, the existing interface pointer
	 * is release. Second, the program ID is transformed to a class ID using the <code>CLSIDFromProgID</code> 
	 * Automation function. Then the <code>GetActiveObject</code> and <code>IUnknown::QueryInterface</code>
	 * methods are called to retrieve the <code>IDispatch</code> interface pointer of the active object.
	 * 
	 * @param progID the program ID of the active object that has been registered.
	 * @throws ComException if one of the Automation functions fails.
	 */
	public native void attachActiveObject(String progID) throws ComException;

	/**
	 * The dispatch ID of the standard "Value" property.
	 */
	public static final int DISPID_VALUE = 0;
	
	/**
	 * The dispatch ID of the return value named argument.
	 */
	public static final int DISPID_PROPERTYPUT = -3;

	/**
	 * The dispatch ID of the standard "Evaluate" property.
	 */
	public static final int DISPID_EVALUATE = -5;

	/**
	 * Maps a single member and an optional set of argument names to the corresponding list of integer dispatch IDs.
	 * This method is a direct wrapper around the <code>IDispatch::GetIDsOfNames</code> Automation function.
	 *
	 * @param names a list of names, the first is the name of an interface member (property or method), 
	 *	and the rest are parameters names.
	 * @return a list of dispatch IDs, one for each of the names. The dispatch IDs of interface members are relative to 
	 *	the corresponding Dispatch object, while the dispatch IDs of parameters are relative to the interface member.
	 * @throws JAutException if the current interface pointer of this object is <code>NULL</code>.
	 * @throws ComException if <code>IDispatch::GetIDsOfNames</code> fails.
	 * @see #getIDOfName
	 */
	public native int[] getIDsOfNames(String[] names);

	/**
	 * Maps a member name to a dispatch ID.
	 * This method is a direct wrapper around the <code>IDispatch::GetIDsOfNames</code> Automation function.
	 *
	 * @param name the name of an interface member (property or method).
	 * @return the dispatch ID of the interface member.
	 * @throws JAutException if the current interface pointer of this object is <code>NULL</code>.
	 * @throws ComException if <code>IDispatch::GetIDsOfNames</code> fails.
	 * @see #getIDsOfNames
	 */
	public native int getIDOfName(String name);

	/**
	 * The member is invoked as a method. If a property has the same name,
	 * both this and {@link #DISPATCH_PROPERTYGET} flag may be set.
	 *
	 * @see #invoke(int, int, Variant[], int[], Variant) invoke
	 */
	public static final int DISPATCH_METHOD = 0x1;

	/**
	 * The member is retrieved as a property or data member. 
	 * If a method has the same name, both this and {@link #DISPATCH_METHOD} flag may be set.
	 *
	 * @see #invoke(int, int, Variant[], int[], Variant) invoke
	 */
	public static final int DISPATCH_PROPERTYGET = 0x2;

	/**
	 * The member is changed as a property or data member. 
	 *
	 * @see #invoke(int, int, Variant[], int[], Variant) invoke
	 */
	public static final int DISPATCH_PROPERTYPUT = 0x4;

	/**
	 * The member is changed by a reference assignment.
	 *
	 * @see #invoke(int, int, Variant[], int[], Variant) invoke
	 */
	public static final int DISPATCH_PROPERTYPUTREF = 0x8;

	/**
	 * Invokes a method, or sets or retrieves a property of an Automation object.
	 * This method is a direct wrapper around the <code>IDispatch::Invoke</code> Automation function.
	 *
	 * @param dispID the dispatch ID of the method or property to be invoked.
	 * @param dispFlags flags selecting the type of access. Acceptable values are 
	 * {@link #DISPATCH_METHOD}, {@link #DISPATCH_PROPERTYGET}, {@link #DISPATCH_PROPERTYPUT}
	 * and {@link #DISPATCH_PROPERTYPUTREF}.
	 * @param arguments the arguments of the method or property. This can be <code>null</code>
	 *	if there are no arguments. If an element of the <code>arguments</code> list is
	 *	null, or its underlying <code>VARIANT</code> is not created, then that parameter
	 *	will be passed as an <code>DISP_E_PARAMNOTFOUND</code> value of type <code>VT_ERROR</code>.
	 * @param namedArgDispIDs a list of dispatch IDs for named arguments. This can be <code>null</code>
	 *	indicating that there are no named arguments.
	 * @param retval the Variant where the result is to be stored. This can be <code>null</code>
	 *	if there is no return value, or the caller is not interested in the return value.
	 *
	 * @throws JAutException if the current interface pointer of this object is <code>NULL</code>.
	 * @throws ComException if <code>IDispatch::Invoke</code> fails.
	 *	Note, that when the COM error code is <code>DISP_E_PARAMNOTFOUND</code> or <code>DISP_E_TYPEMISMATCH</code>,
	 *	then the index of the illegal argument is not returned to the invoker.
	 *
	 * @see #getIDsOfNames
	 */
	public native void invoke(int dispID, int dispFlags, Variant[] arguments, int[] namedArgDispIDs, Variant retval);

	/**
	 * Invokes a method, or retrieves a property of an Automation object.
	 * First, the the non-<code>null</code> arguments are converted to Variants 
	 * with the {@link Variant#Variant(Object)} constructor, 
	 * then {@link #invoke(int, int, Variant[], int[], Variant)} is called.
	 * Note, that if an argument is of type {@link Variant}, then 
	 * the invoked method will receive a reference created by {@link Variant#Variant(Variant)}.
	 *
	 * @param dispName the name of the method or property.
	 * @param dispFlags the type of access.
	 * @param arguments the list of arguments.
	 * @param namedArgDispIDs a list of dispatch IDs for named arguments.
	 * @return the returned value converted to an Object.
	 */
	public Object invoke(String dispName, int dispFlags, Object[] arguments, int[] namedArgDispIDs) throws ComException
	{
		int dispID = getIDOfName(dispName);

		Variant[] args = null;
		if( arguments != null )
		{
			args = new Variant[arguments.length];
			for(int i = 0; i < arguments.length; ++i)
			{
				if( arguments[i] != null )
					if (arguments[i] instanceof Variant)
						args[i] = (Variant) arguments[i];
					else
						args[i] = new Variant(arguments[i]);
				else
					args[i] = Variant.create(Variant.VT_EMPTY);
			}
		}

		Variant retval = new Variant();
		retval.allocate(Variant.VT_EMPTY);

		invoke(dispID, dispFlags, args, namedArgDispIDs, retval);

		Object obj = retval.toObject();
		retval.release();

		return obj;
	}

	/**
	 * Invokes a subroutine, or sets a property of an Automation object.
	 * First, the the non-<code>null</code> arguments are converted to Variants 
	 * with the {@link Variant#Variant(Object)} constructor, 
	 * then {@link #invoke(int, int, Variant[], int[], Variant)} is called.
	 * Note, that if an argument is of type {@link Variant}, then 
	 * the invoked method will receive a reference created by {@link Variant#Variant(Variant)}.
	 *
	 * @param dispName the name of the method or property.
	 * @param dispFlags the type of access.
	 * @param arguments the list of arguments.
	 * @param namedArgDispIDs a list of dispatch IDs for named arguments.
	 */
	public void invokeSub(String dispName, int dispFlags, Object[] arguments, int[] namedArgDispIDs)
	{
		int dispID = getIDOfName(dispName);

		Variant[] args = null;
		if( arguments != null )
		{
			args = new Variant[arguments.length];
			for(int i = 0; i < arguments.length; ++i)
			{
				if( arguments[i] != null )
				{
					if (arguments[i] instanceof Variant)
						args[i] = (Variant)arguments[i];
					else
						args[i] = new Variant(arguments[i]);
				}
			}
		}

		invoke(dispID, dispFlags, args, namedArgDispIDs, null);
	}

	/**
	 * Invokes an Automation method with no argument.
	 * @see #invoke(String, int, Object[], int[])
	 */
	public Object call(String dispName)
	{
		return invoke(dispName, DISPATCH_METHOD, null, null);
	}

	/**
	 * Invokes an Automation method with one argument.
	 * @see #invoke(String, int, Object[], int[])
	 */
	public Object call(String dispName, Object arg0)
	{
		return invoke(dispName, DISPATCH_METHOD, new Object[] { arg0 }, null);
	}

	/**
	 * Invokes an Automation method with two arguments.
	 * @see #invoke(String, int, Object[], int[])
	 */
	public Object call(String dispName, Object arg0, Object arg1)
	{
		return invoke(dispName, DISPATCH_METHOD, new Object[] { arg0, arg1 }, null);
	}

	/**
	 * Invokes an Automation method with three arguments.
	 * @see #invoke(String, int, Object[], int[])
	 */
	public Object call(String dispName, Object arg0, Object arg1, Object arg2)
	{
		return invoke(dispName, DISPATCH_METHOD, new Object[] { arg0, arg1, arg2 }, null);
	}
    
    /**
     * Invokes an Automation method with four arguments.
     * @see #invoke(String, int, Object[], int[])
     */
    public Object call( String dispName, Object arg0, Object arg1, Object arg2, Object arg3 )
    {
        return invoke(dispName, DISPATCH_METHOD, new Object[] { arg0, arg1, arg2, arg3 }, null);
    }

	/**
	 * Invokes an Automation method with a list of arguments.
	 * @see #invoke(String, int, Object[], int[])
	 */
	public Object call(String dispName, Object[] args)
	{
		return invoke(dispName, DISPATCH_METHOD, args, null);
	}

	/**
	 * Invokes an Automation subrutine with no argument.
	 * @see #invokeSub(String, int, Object[], int[])
	 */
	public void callSub(String dispName)
	{
		invokeSub(dispName, DISPATCH_METHOD, null, null);
	}

	/**
	 * Invokes an Automation subroutine with one argument.
	 * @see #invokeSub(String, int, Object[], int[])
	 */
	public void callSub(String dispName, Object arg0)
	{
		invokeSub(dispName, DISPATCH_METHOD, new Object[] { arg0 }, null);
	}

	/**
	 * Invokes an Automation subroutine with two arguments.
	 * @see #invokeSub(String, int, Object[], int[])
	 */
	public void callSub(String dispName, Object arg0, Object arg1)
	{
		invokeSub(dispName, DISPATCH_METHOD, new Object[] { arg0, arg1 }, null);
	}

	/**
	 * Invokes an Automation subroutine with three arguments.
	 * @see #invokeSub(String, int, Object[], int[])
	 */
	public void callSub(String dispName, Object arg0, Object arg1, Object arg2)
	{
		invokeSub(dispName, DISPATCH_METHOD, new Object[] { arg0, arg1, arg2 }, null);
	}

	/**
	 * Invokes an Automation subroutine with a list of arguments.
	 * @see #invokeSub(String, int, Object[], int[])
	 */
	public void callSub(String dispName, Object[] args)
	{
		invokeSub(dispName, DISPATCH_METHOD, args, null);
	}

	/**
	 * Retrieves an Automation property.
	 * @see #invoke(String, int, Object[], int[])
	 */
	public Object get(String dispName)
	{
		return invoke(dispName, DISPATCH_PROPERTYGET, null, null);
	}

	/**
	 * Retrieves an Automation property with one argument.
	 * @see #invoke(String, int, Object[], int[])
	 */
	public Object get(String dispName, Object arg0)
	{
		return invoke(dispName, DISPATCH_PROPERTYGET, new Object[] { arg0 }, null);
	}

	private static int[] DISPID_PROPERTYPUT_ARG = new int[] { DISPID_PROPERTYPUT };

	/**
	 * Sets an Automation property.
	 * @see #invokeSub(String, int, Object[], int[])
	 */
	public void put(String dispName, Object value)
	{
		invokeSub(dispName, DISPATCH_PROPERTYPUT, new Object[] { value }, DISPID_PROPERTYPUT_ARG);
	}

	/**
	 * Sets an Automation property with one argument.
	 * @see #invokeSub(String, int, Object[], int[])
	 */
	public void put(String dispName, Object arg0, Object value)
	{
		invokeSub(dispName, DISPATCH_PROPERTYPUT, new Object[] { arg0, value }, DISPID_PROPERTYPUT_ARG);
	}

	/**
	 * Sets an Automation property by reference.
	 * @see #invokeSub(String, int, Object[], int[])
	 */
	public void putRef(String dispName, Object value)
	{
		invokeSub(dispName, DISPATCH_PROPERTYPUTREF, new Object[] { value }, DISPID_PROPERTYPUT_ARG);
	}

	/**
	 * Sets an Automation property with one argument by reference.
	 * @see #invokeSub(String, int, Object[], int[])
	 */
	public void putRef(String dispName, Object arg0, Object value)
	{
		invokeSub(dispName, DISPATCH_PROPERTYPUTREF, new Object[] { value, arg0 }, DISPID_PROPERTYPUT_ARG);
	}
}
