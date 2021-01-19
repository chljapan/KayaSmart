/**
 * Copyright (C) 2002, 2003 Vanderbilt University
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

import java.util.*;

/**
 * The Apartment class represents a COM Apartment, and its Threads, in a Java application.
 */
public class Apartment
{
	/**
	 * A flag for apartment-threaded concurrency.
	 *
	 * @see #coInitialize
	 */
	protected static final int COINIT_APARTMENTTHREADED = 0x2;

	/**
	 * A flag for multi-threaded concurrency.
	 *
	 * @see #coInitialize
	 */
	protected static final int COINIT_MULTITHREADED = 0x0;

	/**
	 * A flag that disables DDE for OLE1.
	 *
	 * @see #coInitialize
	 */
	protected static final int COINIT_DISABLE_OLE1DDE = 0x4;

	/**
	 * A flag for trading memory for speed, if possible.
	 *
	 * @see #coInitialize
	 */
	protected static final int COINIT_SPEED_OVER_MEMORY = 0x8;

	/**
	 * Initialize the COM library for the calling Thread. It sets the Thread concurrency model.
	 * This function is a direct wrapper around the <code>CoInitializeEx</code> COM function.
	 * Every successful invocation must be matched by a {@link #coUninitialize} call.
	 * Do not use <code>coInitialize</code> directly, use {@link #enter} instead.
	 *
	 * @param coInit the requested concurrency model. Acceptable values are combined
	 *	from the COINIT_XXX constants.
	 * @throws ComException if <code>CoInitializeEx</code> fails.
	 * @see #coUninitialize
	 * @see #enter
	 */
	protected static native void coInitialize(int coInit) throws ComException;

	/**
	 * Uninitialize the COM library for the calling Thread.
	 * This function is a direct wrapper around the <code>CoUninitialize</code> COM function.
	 * Do not use <code>coUninitialize</code> directly, use {@link #leave} instead.
	 *
	 * @see #coInitialize
	 * @see #leave
	 */
	protected static native void coUninitialize();

	/**
	 * This class holds thread local values, 
	 * like the apartment this thread has entered.
	 */
	private static class ThreadVars
	{
		Apartment apartment;
	};

	private static ThreadLocal<?> threadLocal = new ThreadLocal<Object>()
	{
		protected Object initialValue()
		{
			return new ThreadVars();
		}
	};

	/**
	 * Returns the current Apartment for this Thread.
	 *
	 * @return the Apartment that this Thread entered, or <code>null</code>
	 *	if this Thread is not part of any Apartment.
	 */
	public static Apartment currentApartment()
	{
		return ((ThreadVars)threadLocal.get()).apartment;
	}

	/**
	 * The list of threads that entered this apartment.
	 */
	protected LinkedList<Thread> threads = new LinkedList<Thread>();

	/**
	 * Returns the list of threads in this Apartment.
	 *
	 * @return the list of threads that entered this Apartment.
	 */
	public LinkedList<Thread> getThreads()
	{
		return threads;
	}

	/**
	 * The number of Automation objects that were created in this
	 * apartment. We verify that this number is zero when the last 
	 * thread of this apartment leaves. 
	 */
	int objectCount = 0;

    synchronized void incrementObjectCount()
    {
        ++objectCount;
    }

    synchronized void decrementObjectCount()
    {
        --objectCount;
    }

	/**
	 * Constructs an Apartment. Use the public {@link #enter} method the create
	 * and enter apartments.
	 */
	protected Apartment()
	{
	}

	/**
	 * Makes the current Thread enter an Apartment. If <code>multiThreaded</code> is <code>true</code>,
	 * then this thread enters the only multi threaded apartment of the process. 
	 * Otherwise, a new single threaded apartment is created. A Thread can enter at most one
	 * Apartment. If a Thread is not part of any Apartment, then it cannot call any Automation
	 * functions. It is the responsibility of the caller to make sure that all Automation objects
	 * created by threads of this Apartment are finalized (garbage collected), and then to call {@link #leave}
	 * before the last Thread of this Apartment dies. Otherwise,
	 * some COM references and other resources might not get released.
	 *
	 * @param multiThreaded <code>true</code> if the multi threaded apartment is requested
	 *	({@link #COINIT_MULTITHREADED}), <code>false</code> otherwise ({@link #COINIT_APARTMENTTHREADED}).
	 * @return the Apartment this Thread entered.
	 * @throws JAutException if this Thread is already part of an apartment
	 * @throws ComException if {@link #coInitialize} fails. This indicates that
	 *	this thread is already part of a COM apartment (entered without using this method),
	 *	and the current apartment uses a different concurrency model than the one requested now.
	 * @see #leave
	 * @see #coInitialize
	 */
	public synchronized static Apartment enter(boolean multiThreaded)
	{
		ThreadVars threadVars = (ThreadVars)threadLocal.get();
		if( threadVars.apartment != null )
			throw new JAutException("This thread is already part of an apartment");

		coInitialize(multiThreaded ? COINIT_MULTITHREADED : COINIT_APARTMENTTHREADED);

		// what if something fails after this?

		Apartment apartment;
		if( multiThreaded )
		{
			if( multiThreadedApartment == null )
				multiThreadedApartment = new Apartment();

			apartment = multiThreadedApartment;
		}
		else
			apartment = new Apartment();

		threadVars.apartment = apartment;
		apartment.threads.add(Thread.currentThread());

		return apartment;
	}

	/**
	 * Makes the current Thread leave its Apartment. If this Thread is the last one in the Apartment,
	 * calls the garbage collector to try to finalize the remaining {@link ApartmentObject} objects.
	 * Then calls {@link #releaseObjects}. This method makes the best effort
	 * to free all COM resources before calling {@link #coUninitialize}.
	 *
	 * @throws JAutException if the current Thread is not part of an Apartment.
	 * @throws JAutException if this thread is the last thread of its Apartment,
	 * 	and there are Automation objects created in this thread that are not yet finalized. 
	 * @see #enter
	 * @see ApartmentObject#release
	 */
	public synchronized static void leave()
	{
		ThreadVars threadVars = (ThreadVars)threadLocal.get();
		Apartment apartment = threadVars.apartment;

		if( apartment == null )
			throw new JAutException("This thread is not part of an apartment");

		apartment.threads.remove(Thread.currentThread());

		if( apartment.threads.isEmpty() )
		{
			System.gc();
            releaseObjects();

			/*if( apartment.objectCount != 0 )
				throw new JAutException("There are " + apartment.objectCount + " non-finalized Automation objects in this apartment " );*/
				
			if( apartment == multiThreadedApartment )
				multiThreadedApartment = null;
		}
        else
    		releaseObjects();

		threadVars.apartment = null;
		coUninitialize();
	}

	private LinkedList<ApartmentObject> dyingObjects = new LinkedList<ApartmentObject>();

	/**
	 * Releases all COM resources that were finalized from Threads that are not in the current Apartment.
	 * Calls the {@link ApartmentObject#release} method on all objects that were registered by
	 * {@link #registerRelease}.
	 */
	public static void releaseObjects()
	{
		Apartment apartment = ((ThreadVars)threadLocal.get()).apartment;

		if( apartment == null )
			throw new JAutException("This thread is not part of an apartment");

		synchronized(apartment)
		{
			Iterator<ApartmentObject> iter = apartment.dyingObjects.iterator();
			while( iter.hasNext() )
            {
				iter.next().release();
                apartment.decrementObjectCount();                
            }

			apartment.dyingObjects.clear();
		}
	}

	/**
	 * Registers the {@link ApartmentObject#release release} method of an {@link ApartmentObject} to be called
	 * from a Thread that belongs to this Apartment.
	 *
	 * @see #releaseObjects
	 * @see ApartmentObject#releaseInApartment
	 */
	synchronized final void registerRelease(ApartmentObject object)
	{
		dyingObjects.add(object);
	}

	/**
	 * The single multi threaded apartment of the process, if any.
	 *
	 * @see #getMultiThreadedApartment
	 */
	protected static Apartment multiThreadedApartment;

	/**
	 * Retrieves the multi threaded apartment (MTA) of the process.
	 * 
	 * @return the single multi threaded apartment of the process,
	 *	or <code>null</code> if no Java thread is part of the MTA.
	 */
	public static Apartment getMultiThreadedApartment()
	{
		return multiThreadedApartment;
	}

	/**
	 * Load the "jaut" library.
	 */
	static
	{
		System.loadLibrary("jaut");
	}
}
