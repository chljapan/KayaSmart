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
 * A runtime exception thrown by the COM and Automation functions.
 */
public class ComException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int E_INVALIDARG =				0x80070057;
	public static final int E_OUTOFMEMORY =				0x8007000E;
	public static final int DISP_E_BADVARTYPE =			0x80020008;
	public static final int DISP_E_OVERFLOW =			0x8002000A;
	public static final int DISP_E_TYPEMISMATCH =		0x80020005;
	public static final int CO_E_NOTINITIALIZED =		0x800401F0;

	/**
	 * The <code>HRESULT</code> value.
	 */
	protected int hResult;
	
	protected final String detail;

	/**
	 * Creates an exception for a COM error code.
	 *
	 * @param hResult the COM error code (<code>HRESULT</code> or <code>SCODE</code>).
	 */
	public ComException(int hResult)
	{
		this.hResult = hResult;
		this.detail = null;
	}

	public ComException(int hResult, String detail)
	{
		this.hResult = hResult;
		this.detail = detail;
	}

	/**
	 * Returns the error code for this exception.
	 *
	 * @return the COM error code (<code>HRESULT</code> or <code>SCODE</code>) of this exception.
	 */
	public int getErrorCode()
	{
		return hResult;
	}

	/**
	 * Returns the detailed message string for this exception.
	 *
	 * @return the detailed COM error message, if avaiable.
	 * @see #formatMessage
	 * @see #getErrorCode
	 */
	public String getMessage()
	{
		String msg = formatMessage(hResult);
		if( msg == null )
			return "COM error: 0x" + Integer.toHexString(hResult);

//		msg = msg.replaceAll("\r\n", " ");

		return msg + "(0x" + Integer.toHexString(hResult) + ")" + (detail == null ? "" : " " + detail);
	}

	/**
	 * Retrieves the error message of an error code.
	 * This function wraps the <code>FormatMessage</code> Win32 function.
	 *
	 * @param hResult the COM error code (<code>HRESULT</code> or <code>SCODE</code>).
	 * @return The unformatted error message, or <code>null</code> if no message could be found.
	 */
	public static native String formatMessage(int hResult);

	/**
	 * Load the "jaut" library.
	 */
	static
	{
		System.loadLibrary("jaut");
	}
}
