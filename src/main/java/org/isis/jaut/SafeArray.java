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

public class SafeArray
{
	/**
	 * This holds a <code>SAFEARRAY</code> pointer, or <code>0</code> if not attached.
	 * The underlying <code>SAFEARRAY</code> is node owned by the object, but only locked.
	 */
	private int pSafeArray = 0;

	public SafeArray() { }

	public native void attach(Variant target);
	public native void release();

	public native int getDimension();
	public native int getLowerBound(int dim) throws ComException;
	public native int getUpperBound(int dim) throws ComException;
	public native void redimension(int lowerBound, int upperBound) throws ComException;
	public native short getVartype() throws ComException;
	public native int getTotalSize();

	// VT_BOOL 
	public native void getBooleans(int index, boolean[] values);
	public native void setBooleans(int index, boolean[] values);

	// VT_UI1 or VT_I1 
	public native void getBytes(int index, byte[] values);
	public native void setBytes(int index, byte[] values);

	// VT_I2 or VT_UI2 
	public native void getShorts(int index, short[] values);
	public native void setShorts(int index, short[] values);

	// VT_I4 or VT_UI4 or VT_ERROR 
	public native void getIntegers(int index, int[] values);
	public native void setIntegers(int index, int[] values);

	// VT_R4 
	public native void getFloats(int index, float[] values);
	public native void setFloats(int index, float[] values);

	// VT_R8 or VT_DATE
	public native void getDoubles(int index, double[] values);
	public native void setDoubles(int index, double[] values);

	// VT_BSTR
	public native void getStrings(int index, String[] values);
	public native void setStrings(int index, String[] values);

	// VT_DATE or VT_R8
	public native void setDates(int index, java.util.Date[] values);
	public native void getDates(int index, java.util.Date[] values);

	// VT_DISPATCH or VT_UNKNOWN
	public native void getDispatches(int index, Dispatch[] values);
	public native void setDispatches(int index, Dispatch[] values);
}
