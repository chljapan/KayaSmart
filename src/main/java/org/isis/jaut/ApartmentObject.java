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
 * The ApartmentObject allows derived objects to release their resources from the same {@link Apartment}
 * when finalized. This class is abstract, derived classes must implement the {@link #release} method,
 * and call {@link #releaseInApartment} from their finalize method if a resource cannot be freed from the current Thread.
 */
public abstract class ApartmentObject
{
	/**
	 * Holds the {@link Apartment} this object was created in.
	 */
	protected Apartment apartment;

	/**
	 * Retrieves the {@link Apartment} in which this object was created in.
	 */
	public Apartment getApartment()
	{
		return apartment;
	}

	/**
	 * Creates an ApartmentObject in the current {@link Apartment}.
	 *
	 * @throws JAutException if this Thread did not enter an Apartment. If you receive this exception,
	 *	then probably you forgot to initialize COM by calling {@link Apartment#enter}.
	 * @see Apartment#currentApartment
	 * @see Apartment#enter
	 */
	protected ApartmentObject()
	{
		apartment = Apartment.currentApartment();
		if( apartment == null )
			throw new JAutException("This thread is not part of an apartment. Call Apartment.enter() to initialize COM");
            
        apartment.incrementObjectCount();
	}

	/**
	 * This method is called from the {@link Apartment#leave} when the last Thread leaves the Apartment.
	 * Derived classes must override this to release all Apartment specific resources (like interface pointers).
	 * It is guaranteed that this method will be called from a Thread that is part of the Apartment this object
	 * was created in.
	 */
	public abstract void release();

	/**
	 * Calls the {@link #release} method from a Thread that belongs to the Apartment of this object.
	 * Call this method from the derived object finalizer. If the current Thread belongs to the Apartment
	 * of this object, then {@link #release} is called immediately, otherwise the {@link #release} will be
	 * called later.
	 */
	protected final void finalizeInApartment()
	{
		if( Apartment.currentApartment() == apartment )
        {
			release();
            apartment.decrementObjectCount();
        }
		else
			apartment.registerRelease(this);
	}

	/**
	 * Load the "jaut" library.
	 */
	static
	{
		System.loadLibrary("jaut");
	}
}
