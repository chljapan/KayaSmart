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

import java.util.Date;

/**
 * The <code>Variant</code> class is a peer object to an uderlying <code>VARIANT</code> Automation structure.
 * It provides the basic methods to create, access, modify and release this structure. We say that a
 * Variant is a reference (or an array) if the {@link #VT_BYREF} (or the {@link #VT_ARRAY}) bit is set
 * in the variant type that {@link #getVartype} returns.
 * Every Variant that is not a reference owns all its Win32 data structures and resources
 * ({@link #VT_BSTR}, {@link #VT_DISPATCH}, {@link #VT_UNKNOWN} and {@link #VT_ARRAY}).
 * Reference Variants do not own their referred resources. Typically, these resources are owned by other Variants,
 * or obtained from other Win32 applications.
 */
public class Variant extends ApartmentObject
{
    /**
     * The no value variant type. Cannot be combined with {@link #VT_BYREF} or {@link #VT_ARRAY}.
     * This is the type of the Variant after {@link #clear()}.
     *
     * @see #getVartype
     */
    public static final int VT_EMPTY = 0;
    
    /**
     * A propagating <code>null</code> variant type. This has nothing to do with a <code>null</code>
     * pointer, rather, it is used for tri-state logic, as with SQL.
     * Cannot be combined with {@link #VT_BYREF} or {@link #VT_ARRAY}.
     *
     * @see #getVartype
     */
    public static final int VT_NULL = 1;
    
    /**
     * 2-byte integer variant type. This is mapped to <code>short</code>.
     *
     * @see #getVartype
     */
    public static final int VT_I2 = 2;
    
    /**
     * 4-byte integer variant type. This is mapped to <code>int</code>.
     *
     * @see #getVartype
     */
    public static final int VT_I4 = 3;
    
    /**
     * An IEEE 4-byte real variant type. This is mapped to <code>float</code>
     *
     * @see #getVartype
     */
    public static final int VT_R4 = 4;
    
    /**
     * An IEEE 8-byte real variant type. This is mapped to <code>double</code>.
     *
     * @see #getVartype
     */
    public static final int VT_R8 = 5;
    
    /**
     * A currency variant type. It is an 8-byte integer, scaled by 10000 to give a fixed
     * point number with 15-digits to the left of the decimal point and 4-digits to the right.
     * This is mapped to <code>long</code>.
     *
     * @see #getVartype
     * @see #getLong
     * @see #setLong
     */
    public static final int VT_CY = 6;
    
    /**
     * A variant type denoting a date. This is mapped to <code>double</code>, but
     * the correct Java {@link java.util.Date} can be retrieved and set with the methods
     * {@link #getDate} and {@link #setDate}.
     *
     * @see #getVartype
     */
    public static final int VT_DATE = 7;
    
    /**
     * A string variant type. This is mapped to {@link String}.
     *
     * @see #getVartype
     */
    public static final int VT_BSTR = 8;
    
    /**
     * A {@link Dispatch} Automation object variant type.
     *
     * @see #getVartype
     */
    public static final int VT_DISPATCH = 9;
    
    /**
     * An error code variant type. This can hold <code>HRESULT</code> error codes.
     * It is mapped to <code>int</code>.
     *
     * @see #getVartype
     */
    public static final int VT_ERROR = 10;
    
    /**
     * A boolean variant type. It is mapped to <code>boolean</code>.
     *
     * @see #getVartype
     */
    public static final int VT_BOOL = 11;
    
    /**
     * A variant type for referring to another {@link Variant} object.
     * It must be a reference, that is, combined with {@link #VT_BYREF}.
     *
     * @see #getVartype
     */
    public static final int VT_VARIANT = 12;
    
    /**
     * An <code>IUnknown</code> interface pointer variant type. This cannot be used directly;
     * one must try to obtain an <code>IDispatch</code> interface pointer via the {@link #getDispatch} method.
     *
     * @see #getVartype
     */
    public static final int VT_UNKNOWN = 13;
    
    /**
     * A decimal value variant type. This is currently not mapped, and cannot be retrieved directly.
     *
     * @see #getVartype
     */
    public static final int VT_DECIMAL = 14;
    
    /**
     * 1-byte integer variant type. This is mapped to <code>byte</code>.
     *
     * @see #getVartype
     */
    public static final int VT_I1 = 16;
    
    /**
     * 1-byte unsigned integer variant type. This is mapped to <code>byte</code>.
     *
     * @see #getVartype
     */
    public static final int VT_UI1 = 17;
    
    /**
     * 2-byte unsigned integer variant type. This is mapped to <code>short</code>.
     *
     * @see #getVartype
     */
    public static final int VT_UI2 = 18;
    
    /**
     * 4-byte unsigned integer variant type. This is mapped to <code>int</code>.
     *
     * @see #getVartype
     */
    public static final int VT_UI4 = 19;
    
    /**
     * machine dependent (4-byte) integer variant type. This is mapped to <code>int</code>.
     *
     * @see #getVartype
     */
    public static final int VT_INT = 22;
    
    /**
     * machine dependent (4-byte) unsigned integer variant type. This is mapped to <code>int</code>.
     *
     * @see #getVartype
     */
    public static final int VT_UINT = 23;
    
    /**
     * An array variant type.
     * This must be combined with one of the simple data types, except with {@link #VT_EMPTY}
     * and {@link #VT_NULL}. The array (actually, the underlying <code>SAFEARRAY</code>) is owned
     * by this Variant, and it is copied and cleared as any other basic data type.
     *
     * @see #getVartype
     */
    public static final int VT_ARRAY = 0x2000;
    
    /**
     * A reference variant type. This must be combined with one of the simple data types.
     * If combined with {@link #VT_ARRAY}, it is a reference to an array, rather than an array of references.
     *
     * @see #getVartype
     */
    public static final int VT_BYREF = 0x4000;
    
    /**
     * This holds a pointer to the underlying <code>VARIANT</code>. It is <code>0</code>, if
     * the underlying <code>VARIANT</code> is not allocated yet or already released.
     */
    public/*private*/ int pVariant = 0;
    
    /**
     * Creates a Variant with no underlying <code>VARIANT</code>. Users must call one of the allocate methods to
     * allocate the underlying <code>VARIANT</code> structure.
     * To create a Variant of type <code>VT_EMPTY</code> call <code>new Variant(null)</code>.
     */
    public Variant()
    {
    }
    
    /**
     * Creates a Variant of type {@link #VT_BOOL}.
     */
    public Variant(boolean value)
    {
        allocate(VT_BOOL);
        setBoolean(value);
    }
    
    /**
     * Creates a Variant of type {@link #VT_UI1}.
     */
    public Variant(byte value)
    {
        allocate(VT_UI1);
        setByte(value);
    }
    
    /**
     * Creates a Variant of type {@link #VT_I2}.
     */
    public Variant(short value)
    {
        allocate(VT_I2);
        setShort(value);
    }
    
    /**
     * Creates a Variant of type {@link #VT_I4}.
     */
    public Variant(int value)
    {
        allocate(VT_I4);
        setInt(value);
    }
    
    /**
     * Creates a Variant of type {@link #VT_R4}.
     */
    public Variant(float value)
    {
        allocate(VT_R4);
        setFloat(value);
    }
    
    /**
     * Creates a Variant of type {@link #VT_R8}.
     */
    public Variant(double value)
    {
        allocate(VT_R8);
        setDouble(value);
    }
    
    /**
     * Creates a Variant of type {@link #VT_BSTR}.
     */
    public Variant(String value)
    {
        allocate(VT_BSTR);
        setString(value);
    }
    
    /**
     * Creates a Variant of type {@link #VT_DATE}.
     */
    public Variant(Date value)
    {
        allocate(VT_DATE);
        setDate(value);
    }
    
    /**
     * Creates a Variant of type {@link #VT_DISPATCH}.
     */
    public Variant(Dispatch value)
    {
        allocate(VT_DISPATCH);
        setDispatch(value);
    }
    
    /**
     * Creates a Variant of a reference type.
     * If <code>value</code> is of type {@link #VT_EMPTY} or {@link #VT_NULL},
     * then a Variant of type <code>VT_VARIANT|VT_BYREF</code>,
     * otherwise a Variant of type <code>value.getVartype()|VT_BYREF</code> will be created.
     *
     * @param value the Variant holding the actual value that this Variant will refer to.
     */
    public Variant(Variant value)
    {
        int vt = value.getVartype() | VT_BYREF;       
        if( vt == (VT_EMPTY|VT_BYREF) || vt == (VT_NULL|VT_BYREF) )
            vt = VT_VARIANT|VT_BYREF;
        
        allocateReference(vt, value);
    }
    
    /**
     * Creates a Variant of from an Object.
     * If <code>value</code> is <code>null</code>, then a Variant of type {@link #VT_EMPTY} is created.
     * Otherwise, a Variant will be created with one of the constructors that accept a single parameter.
     * Note, that if <code>value</code> is a Variant, then a Variant referring to the data in <code>value</code>
     * will be created by {@link #Variant(Variant)}.
     *
     * @param value an Object
     * @throws JAutException if <code>value</code> is not one of the allowed types.
     * @see #toObject
     */
    public Variant(Object value)
    {
        if( value == null )
        {
            allocate(VT_EMPTY);
        }
        else if( value instanceof Integer )
        {
            allocate(VT_I4);
            setInt(((Integer)value).intValue());
        }
        else if( value instanceof String )
        {
            allocate(VT_BSTR);
            setString((String)value);
        }
        else if( value instanceof Dispatch )
        {
            allocate(VT_DISPATCH);
            setDispatch((Dispatch)value);
        }
        else if( value instanceof Variant )
        {
            int vt = ((Variant)value).getVartype() | VT_BYREF;
            
            if( vt == (VT_EMPTY|VT_BYREF) || vt == (VT_NULL|VT_BYREF) )
                vt = VT_VARIANT|VT_BYREF;
            
            allocateReference(vt, (Variant)value);
        }
        else if( value instanceof Double )
        {
            allocate(VT_R8);
            setDouble(((Double)value).doubleValue());
        }
        else if( value instanceof Boolean )
        {
            allocate(VT_BOOL);
            setBoolean(((Boolean)value).booleanValue());
        }
        else if( value instanceof Byte )
        {
            allocate(VT_UI1);
            setByte(((Byte)value).byteValue());
        }
        else if( value instanceof Short )
        {
            allocate(VT_I2);
            setShort(((Short)value).shortValue());
        }
        else if( value instanceof Long )
        {
            allocate(VT_CY);
            setLong(((Long)value).longValue());
        }
        else if( value instanceof Float )
        {
            allocate(VT_R4);
            setFloat(((Float)value).floatValue());
        }
        else if( value instanceof Date )
        {
            allocate(VT_DATE);
            setDate((Date)value);
        }
        else
            throw new JAutException("Invalid object " + value.getClass());
    }
    
    /**
     * Allocates the underlying <code>VARIANT</code> of a simple datatype. If an underlying <code>VARIANT</code>
     * already exists, then it is cleared, otherwise a new <code>VARIANT</code> is allocated.
     * Then the <code>VARIANT</code> is initialized and the variant type is set to <code>vartype</code>.
     *
     * @param vartype the type of the Variant to be allocated.
     *	It cannot be an array ({@link #VT_ARRAY}) or reference ({@link #VT_BYREF}).
     * @throws JAutException if <code>vartype</code> is not one of the simple types.
     * @throws ComException if the <code>VariantClear</code> function fails when clearing the
     *	the content of the previous variant.
     * @throws OutOfMemoryError if memory could not be allocated for the <code>VARIANT</code>.
     */
    public native void allocate(int vartype) throws ComException;
    
    /**
     * Creates a Variant of a simple datatype.
     *
     * @param the type of the Variant to be created. It cannot be combined with
     *	{@link #VT_ARRAY} or {@link #VT_BYREF}.
     * @return the newly created Variant object.
     * @see #allocate(int)
     */
    public static Variant create(int vartype) throws ComException
    {
        Variant var = new Variant();
        var.allocate(vartype);
        return var;
    }
    
    /**
     * Clears and releases the underlying <code>VARIANT</code>. If there is no underlying <code>VARIANT</code>,
     * then this method does nothing. Otherwise, the <code>VARIANT</code> is cleared,
     * like with {@link #clear()}, and the allocated memory for the <code>VARIANT</code> is freed.
     *
     * @throws ComException if the <code>VariantClear</code> function fails.
     */
    public native void release() throws ComException;
    
    /**
     * Clears and releases the Variant, can be called from any Thread.
     * Calls the {@link #release} or {@link #releaseInApartment} methods depending
     * on the type of the current variant.
     */
    public void finalize()
    {
        if( isCreated() )
        {
            int vt = getVartype() & ~VT_ARRAY;
            
            if( vt != VT_DISPATCH && vt != VT_UNKNOWN )
            {
                release();
                apartment.decrementObjectCount();
            }
            else
                finalizeInApartment();
        }
        else
            apartment.decrementObjectCount();
    }
    
    /**
     * Tests if the underlying <code>VARIANT</code> exists.
     *
     * @return <code>true</code> if the underlying <code>VARIANT</code> exists, <code>false</code> otherwise.
     * @see #allocate(int)
     */
    public boolean isCreated()
    {
        return pVariant != 0;
    }
    
    /**
     * Returns the type of the Variant. This is a wrapper method to access the <code>vt</code> field
     * of the underlying <code>VARIANT</code>. The returned value is of type <code>VARTYPE</code>.
     * Note that <code>VARTYPE</code> is an unsigned 2-byte integer, but we map this to <code>int</code>
     * for convenience.
     *
     * @return the type of the Variant.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated.
     */
    public native int getVartype();
    
    /**
     * Converts an object to a simple type by invoking the <code>Value</code> property.
     * The use of this flag is not recommended.
     *
     * @see #changeType
     */
    public static final int VARIANT_NOVALUEPROP = 0x01;
    
    /**
     * Converts a boolean value to a string containing either "True" or "False".
     *
     * @see #changeType
     */
    public static final int VARIANT_ALPHABOOL = 0x02;
    
    /**
     * For conversions to and from strings prevents the use of the user selected locale on the computer.
     *
     * @see #changeType
     */
    public static final int VARIANT_NOUSEROVERRIDE = 0x04;
    
    /**
     * For conversions between boolean and string, uses the language of the user selected locale on the computer.
     *
     * @see #changeType
     */
    public static final int VARIANT_LOCALBOOL = 0x10;
    
    /**
     * Converts the Variant to another type. This is a direct wrapper method around the <code>VariantChangeType</code>
     * Automation function.
     *
     * @param destination the destination Variant to hold the coerced value. If <code>destination</code> is
     *	the same as <code>this</code> or <code>null</code>, the Variant will be converted in place.
     * @param changeFlag flags controlling the coercion. Acceptable values are combined from
     *	the four <code>VARIANT_XXX</code> constants.
     * @param vartype The type to coerce to. After a successful conversion
     *	the type of the destination Variant is <code>vartype</code>.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated.
     * @throws ComException if the <code>VariantChangeType</code> function fails.
     */
    public native void changeType(Variant destination, int changeFlag, int vartype) throws ComException;
    
    /**
     * Clears the Variant and releases all owned resources other the the underlying <code>VARIANT</code>.
     * We say that the Variant is cleared if its underlying  <code>VARIANT</code> is allocated
     * and its type is {@link #VT_EMPTY}.
     * Variants of type {@link #VT_BSTR}, {@link #VT_UNKNOWN},
     * {@link #VT_DISPATCH} and {@link #VT_ARRAY} own the corresponding resources. In the case of
     * {@link #VT_ARRAY} the corresponding <code>SAFEARRAY</code> is freed and its content is released.
     * If the type of the Variant is a reference (anything with {@link #VT_BYREF}), the corresponding
     * resource is not owned by this Variant and it is not released. This method is a direct wrapper around the
     * <code>VariantClear</code> Automation function.
     * This method will fail if the type of the variant is an array and a {@link SafeArray} object
     * is attached to this Variant.
     *
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated.
     * @throws ComException if the <code>VariantClear</code> function fails.
     * @throws OutOfMemoryError if memory could not be allocated for the conversion.
     */
    public native void clear() throws ComException;
    
    /**
     * Frees the destination Variant and makes a copy of this Variant. First, the destination Variant is cleared,
     * like with {@link #clear()}. Then <code>destination</code> receives an exact copy of this Variant
     * and all of its owned resources (in the case of {@link #VT_BSTR}, {@link #VT_UNKNOWN},
     * {@link #VT_DISPATCH} and {@link #VT_ARRAY}). If the Variant is a reference (anything with
     * {@link #VT_BYREF}), the corresponding resource is not owned and is not copied.
     * This method is a direct wrapper around the <code>VariantCopy</code> Automation function.
     *
     * @param destination the destination Variant to hold the copied value.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated.
     * @throws ComException if the <code>VariantCopy</code> function fails.
     * @throws OutOfMemoryError if memory could not be allocated for the copy.
     */
    public native void copy(Variant destination) throws ComException;
    
    /**
     * Frees the destination Variant and copies the value of this Variant, performing all necessary
     * indirections for references. First, the destination Variant is cleared,
     * like with {@link #clear()}. Then <code>destination</code> receives an exact copy of
     * the value of this Variant and all of its owned resources (in the case of {@link #VT_BSTR},
     * {@link #VT_UNKNOWN}, {@link #VT_DISPATCH} and {@link #VT_ARRAY}).
     * If this Variant is a reference (anything with {@link #VT_BYREF}), then it is recursively dereferenced,
     * and the corresponding value and its resources are copied. After successful completion, the destination
     * Variant is never a reference.
     * This method is a direct wrapper around the <code>VariantCopyInd</code> Automation function.
     *
     * @param destination the destination Variant to hold the copied value.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated.
     * @throws ComException if the <code>VariantCopyInd</code> function fails.
     * @throws OutOfMemoryError if memory could not be allocated for the copy.
     */
    public native void copyInd(Variant destination) throws ComException;
    
    /**
     * Retrieves the <code>boolean</code> value in the Variant.
     * The type of the Variant must be {@link #VT_BOOL} or <code>VT_BOOL|VT_BYREF</code>.
     *
     * @return the <code>boolean</code> value in the Variant.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     */
    public native boolean getBoolean();
    
    /**
     * Sets the <code>boolean</code> value in the Variant.
     * The type of the Variant must be {@link #VT_BOOL} or <code>VT_BOOL|VT_BYREF</code>.
     *
     * @param value the new <code>boolean</code> value.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     */
    public native void setBoolean(boolean value);
    
    /**
     * Retrieves the <code>byte</code> value in the Variant.
     * The type of the Variant must be {@link #VT_UI1}, {@link #VT_I1},
     * <code>VT_UI1|VT_BYREF</code> or <code>VT_I1|VT_BYREF</code>.
     *
     * @return the <code>byte</code> value in the Variant.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     */
    public native byte getByte();
    
    /**
     * Sets the <code>byte</code> value in the Variant.
     * The type of the Variant must be {@link #VT_UI1}, {@link #VT_I1},
     * <code>VT_UI1|VT_BYREF</code> or <code>VT_I1|VT_BYREF</code>.
     *
     * @param value the new <code>byte</code> value.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     */
    public native void setByte(byte value);
    
    /**
     * Retrieves the <code>short</code> value in the Variant.
     * The type of the Variant must be {@link #VT_I2}, {@link #VT_UI1},
     * <code>VT_I1|VT_BYREF</code> or <code>VT_UI1|VT_BYREF</code>.
     *
     * @return the <code>short</code> value in the Variant.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     */
    public native short getShort();
    
    /**
     * Sets the <code>short</code> value in the Variant.
     * The type of the Variant must be {@link #VT_I2}, {@link #VT_UI2},
     * <code>VT_I2|VT_BYREF</code> or <code>VT_UI2|VT_BYREF</code>.
     *
     * @param value the new <code>short</code> value.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     */
    public native void setShort(short value);
    
    /**
     * Retrieves the <code>int</code> value in the Variant.
     * The type of the Variant must be {@link #VT_I4}, {@link #VT_UI4}, {@link #VT_ERROR}, {@link #VT_INT}, {@link #VT_UINT},
     * <code>VT_I1|VT_BYREF</code>, <code>VT_UI1|VT_BYREF</code>, <code>VT_ERROR|VT_BYREF</code>,
     * <code>VT_INT|VT_BYREF</code> or <code>VT_UINT|VT_BYREF</code>.
     *
     * @return the <code>int</code> value in the Variant.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     */
    public native int getInt();
    
    /**
     * Sets the <code>int</code> value in the Variant.
     * The type of the Variant must be {@link #VT_I4}, {@link #VT_UI4}, {@link #VT_ERROR}, {@link #VT_INT}, {@link #VT_UINT},
     * <code>VT_I1|VT_BYREF</code>, <code>VT_UI1|VT_BYREF</code>, <code>VT_ERROR|VT_BYREF</code>,
     * <code>VT_INT|VT_BYREF</code> or <code>VT_UINT|VT_BYREF</code>.
     *
     * @param value the new <code>int</code> value.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     */
    public native void setInt(int value);
    
    /**
     * Retrieves the <code>long</code> value in the Variant.
     * The type of the Variant must be {@link #VT_CY} or <code>VT_CY|VT_BYREF</code>.
     *
     * @return the <code>long</code> value in the Variant.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     * @see #VT_CY
     */
    public native long getLong();
    
    /**
     * Sets the <code>long</code> value in the Variant.
     * The type of the Variant must be {@link #VT_CY} or <code>VT_CY|VT_BYREF</code>.
     *
     * @param value the new <code>long</code> value.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     * @see #VT_CY
     */
    public native void setLong(long value);
    
    /**
     * Retrieves the <code>float</code> value in the Variant.
     * The type of the Variant must be {@link #VT_R4} or <code>VT_R4|VT_BYREF</code>.
     *
     * @return the <code>float</code> value in the Variant.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     */
    public native float getFloat();
    
    /**
     * Sets the <code>float</code> value in the Variant.
     * The type of the Variant must be {@link #VT_R4} or <code>VT_R4|VT_BYREF</code>.
     *
     * @param value the new <code>float</code> value.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     */
    public native void setFloat(float value);
    
    /**
     * Retrieves the <code>double</code> value in the Variant.
     * The type of the Variant must be {@link #VT_R8}, <code>VT_R8|VT_BYREF</code>,
     * {@link #VT_DATE} or <code>VT_DATE|VT_BYREF</code>. Note, that Win32 dates
     * are stored as doubles in a <code>VARIANT</code>.
     *
     * @return the <code>double</code> value in the Variant.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     */
    public native double getDouble();
    
    /**
     * Sets the <code>double</code> value in the Variant.
     * The type of the Variant must be {@link #VT_R8}, <code>VT_R8|VT_BYREF</code>,
     * {@link #VT_DATE} or <code>VT_DATE|VT_BYREF</code>. Note, that Win32 dates
     * are stored as doubles in a <code>VARIANT</code>.
     *
     * @param value the new <code>double</code> value.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     */
    public native void setDouble(double value);
    
    /**
     * Retrieves the <code>String</code> value in the Variant.
     * The type of the Variant must be {@link #VT_BSTR} or <code>VT_BSTR|VT_BYREF</code>.
     *
     * @return the <code>String</code> value in the Variant.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     * @throws OutOfMemoryError if memory could not be allocated for the string.
     */
    public native String getString();
    
    /**
     * Sets the <code>String</code> value in the Variant.
     * The type of the Variant must be {@link #VT_BSTR} or <code>VT_BSTR|VT_BYREF</code>.
     *
     * @param value the new <code>String</code> value.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     * @throws OutOfMemoryError if memory could not be allocated for the string.
     */
    public native void setString(String value);

    public native String[] getStringArray(); 
   
    /**
     * Dec 30 1899 is 0 in Win32 date
     * Jan 1 1970 is 0 in Java date
     * difference between the two is 70 years + 2 days
     * leap years are 1904, 1908, etc. = 17 leaps
     */
    static final int SUN_MINUS_MICROSOFT = (365 * 70) + 17 + 2;
    static final long MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
    
    /**
     * Retrieves the {@link java.util.Date} in the Variant.
     * This method performs the necessary transformation from the Win32 date to the Jave date.
     * The type of the Variant must be {@link #VT_DATE}, <code>VT_DATE|VT_BYREF</code>,
     * {@link #VT_R8} or <code>VT_R8|VT_BYREF</code>.
     *
     * @return the {@link java.util.Date} in the Variant.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     * @see #getDouble
     */
    public Date getDate()
    {
        double millis = (getDouble() - SUN_MINUS_MICROSOFT) * MILLISECONDS_IN_DAY;
        return new Date(Math.round(millis));
    }
    
    /**
     * Sets the {@link java.util.Date} in the Variant.
     * This method performs the necessary transformation from the Java date to the Win32 date.
     * The returned object is the correct Java date corresponding to the Win32 date in the <code>VARIANT</code>.
     * The type of the Variant must be {@link #VT_DATE}, <code>VT_DATE|VT_BYREF</code>,
     * {@link #VT_R8} or <code>VT_R8|VT_BYREF</code>.
     *
     * @param value the new {@link java.util.Date}.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     * @see #setDouble
     */
    public void setDate(Date value)
    {
        setDouble(((double)value.getTime()) / MILLISECONDS_IN_DAY + SUN_MINUS_MICROSOFT);
    }
    
    /**
     * Retrieves the {@link Dispatch} object in the Variant.
     * The type of the Variant must be {@link #VT_DISPATCH}, <code>VT_DISPATCH|VT_BYREF</code>,
     * {@link #VT_UNKNOWN} or <code>VT_UNKNOWN|VT_BYREF</code>.
     * The method invokes <code>QueryInterface</code> to obtain the <code>IDispatch</code> interface
     * when the type of the variant is {@link #VT_UNKNOWN} or <code>VT_UNKNOWN|VT_BYREF</code>.
     *
     * @return the {@link Dispatch} object in the Variant.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     * @throws ComException if the <code>QueryInterface</code> method fails.
     * @throws OutOfMemoryError if memory could not be allocated for the new object.
     * @see Dispatch#attach(Variant)
     */
    public Dispatch getDispatch() throws ComException
    {
        Dispatch dispatch = new Dispatch();
        dispatch.attach(this);
        return dispatch;
    }
    
    /**
     * Sets the {@link Dispatch} object in the Variant.
     * The type of the Variant must be {@link #VT_DISPATCH}, <code>VT_DISPATCH|VT_BYREF</code>,
     * {@link #VT_UNKNOWN} or <code>VT_UNKNOWN|VT_BYREF</code>.
     *
     * @param value the new {@link Dispatch} object, or <code>null</code>.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     */
    public native void setDispatch(Dispatch value);
    
    /**
     * Allocates the underlying <code>VARIANT</code> of an array type.
     * First, a new <code>SAFEARRAY</code> is created with the <code>SafeArrayCreate</code>
     * Automation function.
     * Then, if an underlying <code>VARIANT</code> already exists, it is cleared,
     * otherwise a new <code>VARIANT</code> is allocated.
     * Finally, the <code>VARIANT</code> is initialized and the variant type is set to
     * <code>vartype|VT_ARRAY</code>. The created <code>SAFEARRAY</code> is onwed by this Variant.
     *
     * @param vartype the simple datatype of the array.
     *	It cannot be {@link #VT_EMPTY}, {@link #VT_NULL},
     *	an array ({@link #VT_ARRAY}) or a reference ({@link #VT_BYREF}).
     * @param lowerBounds a vector of lower bounds, one for each dimension.
     * @param elements a vector of integers holding the number of elements in each dimension.
     * @throws JAutException if <code>vartype</code> is not one of allowed simple types,
     *	or the bounds of the array is not valid.
     * @throws ComException if the <code>VariantClear</code> function fails when clearing the
     *	the content of the previous variant.
     * @throws OutOfMemoryError if the system run out of memory.
     * @see #accessArray
     */
    public native void allocateArray(int vartype, int[] lowerBounds, int[] elements) throws ComException;
    
    /**
     * Retrieves the {@link SafeArray} object in the Variant.
     * The underlying <code>SAFEARRAY</code> structure is always owned by the Variant, the
     * {@link SafeArray} object has only access to it. {@link SafeArray} locks the <code>SAFEARRAY</code>
     * with the <code>SafeArrayLock</code> Automation function, until {@link SafeArray#release} is called.
     * While the <code>SAFEARRAY</code> is locked, the type of the variant cannot be changed, and the
     * {@link #clear} method will fail.
     * The type of the Variant must be an array ({@link #VT_ARRAY}) or a reference to an array
     * (<code>VT_ARRAY|VT_BYREF</code>).
     *
     * @return the {@link SafeArray} object in the Variant.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated, or
     *	it is not of the proper type.
     * @throws OutOfMemoryError if memory could not be allocated for the new object.
     * @see SafeArray#attach(Variant)
     * @see #clear
     */
    public SafeArray accessArray()
    {
        SafeArray array = new SafeArray();
        array.attach(this);
        return array;
    }
    
    /**
     * Allocates the underlying <code>VARIANT</code> of a reference type. If an underlying <code>VARIANT</code>
     * already exists, then it is cleared, otherwise a new <code>VARIANT</code> is allocated.
     * Then the <code>VARIANT</code> is initialized and the variant type is set to <code>vartype</code>.
     * It is the callers responsibility to guarantee that the Variant holding the target data is not released,
     * cleared or its type changed while a reference exists to it.
     *
     * @param vartype the type of the Variant to be allocated. It must be combined with {@link #VT_BYREF},
     *	it cannot be <code>VT_EMPTY|VT_BYREF</code>, <code>VT_NULL|VT_BYREF</code>, or an array ({@link #VT_ARRAY}).
     * @param target the Variant holding the data, or a reference to the data, that this Variant will refer to.
     * @throws JAutException if the underlying <code>VARIANT</code> of the target is not allocated, or
     *  if <code>vartype</code> is not one of the allowed types, or
     *	it is not compatible with the type of the target Variant.
     * @throws ComException if the <code>VariantClear</code> function fails when clearing the
     *	the content of the previous variant.
     * @throws OutOfMemoryError if memory could not be allocated for the <code>VARIANT</code>.
     */
    public native void allocateReference(int vartype, Variant target) throws ComException;
    
    /**
     * Not implemented yet.
     */
    public native void allocateReference(int vartype, SafeArray target, int index);
    
    /**
     * Holds the default change flags when coercing the value of the Variant to a native Java type
     * with the <code>toXXX()</code> methods. Acceptable values are combined from the
     * {@link #VARIANT_NOVALUEPROP}, {@link #VARIANT_ALPHABOOL}, {@link #VARIANT_NOUSEROVERRIDE}
     * and {@link #VARIANT_LOCALBOOL} constants.
     */
    public static int defaultChangeFlags = 0;
    
    /**
     * Coerces the Variant to a {@link #VT_BOOL} and returns the coerced value.
     *
     * @return the coerced <code>bool</code> value.
     * @see #changeType
     * @see #defaultChangeFlags
     */
    public boolean toBoolean() throws ComException
    {
        if( (getVartype() & ~VT_BYREF) != VT_BOOL )
            changeType(null, defaultChangeFlags, VT_BOOL);
        
        return getBoolean();
    }
    
    /**
     * Coerces the Variant to a {@link #VT_UI1} and returns the coerced value.
     *
     * @return the coerced <code>byte</code> value.
     * @see #changeType
     * @see #defaultChangeFlags
     */
    public byte toByte() throws ComException
    {
        if( (getVartype() & ~VT_BYREF) != VT_UI1 )
            changeType(null, defaultChangeFlags, VT_UI1);
        
        return getByte();
    }
    
    /**
     * Coerces the Variant to a {@link #VT_I2} and returns the coerced value.
     *
     * @return the coerced <code>short</code> value.
     * @see #changeType
     * @see #defaultChangeFlags
     */
    public short toShort() throws ComException
    {
        if( (getVartype() & ~VT_BYREF) != VT_I2 )
            changeType(null, defaultChangeFlags, VT_I2);
        
        return getShort();
    }
    
    /**
     * Coerces the Variant to a {@link #VT_I4} and returns the coerced value.
     *
     * @return the coerced <code>int</code> value.
     * @see #changeType
     * @see #defaultChangeFlags
     */
    public int toInt() throws ComException
    {
        if( (getVartype() & ~VT_BYREF) != VT_I4 )
            changeType(null, defaultChangeFlags, VT_I4);
        
        return getInt();
    }
    
    /**
     * Coerces the Variant to a {@link #VT_CY} and returns the coerced value.
     *
     * @return the coerced currency as a <code>long</code> value.
     * @see #changeType
     * @see #defaultChangeFlags
     * @see #VT_CY
     */
    public long toLong() throws ComException
    {
        if( (getVartype() & ~VT_BYREF) != VT_CY )
            changeType(null, defaultChangeFlags, VT_CY);
        
        return getLong();
    }
    
    /**
     * Coerces the Variant to a {@link #VT_R4} and returns the coerced value.
     *
     * @return the coerced <code>float</code> value.
     * @see #changeType
     * @see #defaultChangeFlags
     */
    public float toFloat() throws ComException
    {
        if( (getVartype() & ~VT_BYREF) != VT_R4 )
            changeType(null, defaultChangeFlags, VT_R4);
        
        return getFloat();
    }
    
    /**
     * Coerces the Variant to a {@link #VT_R8} and returns the coerced value.
     *
     * @return the coerced <code>double</code> value.
     * @see #changeType
     * @see #defaultChangeFlags
     */
    public double toDouble() throws ComException
    {
        if( (getVartype() & ~VT_BYREF) != VT_R8 )
            changeType(null, defaultChangeFlags, VT_R8);
        
        return getDouble();
    }
    
    /**
     * Coerces the Variant to a {@link #VT_BSTR} and returns the coerced value.
     *
     * @return the coerced <code>String</code> value.
     * @see #changeType
     * @see #defaultChangeFlags
     */
    public String toString() throws ComException
    {
        if( (getVartype() & ~VT_BYREF) != VT_BSTR )
            changeType(null, defaultChangeFlags, VT_BSTR);
        
        return getString();
    }
    
    /**
     * Coerces the Variant to a {@link #VT_DATE} and returns the coerced value.
     *
     * @return the coerced <code>Data</code> value.
     * @see #changeType
     * @see #defaultChangeFlags
     */
    public Date toDate() throws ComException
    {
        if( (getVartype() & ~VT_BYREF) != VT_DATE )
            changeType(null, defaultChangeFlags, VT_DATE);
        
        return getDate();
    }
    
    /**
     * Coerces the Variant to a {@link #VT_DISPATCH} and returns the coerced Automation object.
     *
     * @return the coerced {@link Dispatch} value.
     * @see #changeType
     * @see #defaultChangeFlags
     */
    public Dispatch toDispatch() throws ComException
    {
        if( (getVartype() & ~VT_BYREF) != VT_DISPATCH )
            changeType(null, defaultChangeFlags, VT_DISPATCH);
        
        return getDispatch();
    }
    
    /**
     * Returns the value of the Variant in a Java object.
     * If the Variant is of a simple datetype or refers to a simple datatype, then a new immutable
     * Java object is returned of the corresponding type (for example {@link java.lang.Double} for {@link #VT_R8}).
     * Otherwise, the value is already represented as an Object, and that object is returned.
     * If the type is <code>VT_EMPTY</code> or <code>VT_NULL</code>, then the returned object is <code>null</code>.
     * This method does not work for arrays and Variants of type <code>VT_VARIANT|VT_BYREF</code>.
     *
     * @return a Java object holding the value of the Variant, it cannot be an array.
     * @throws JAutException if the underlying <code>VARIANT</code> is not allocated,
     *	or cannot be represented as an Object.
     * @throws ComException if the Variant is of type <code>VT_UNKNOWN</code> and it cannot be
     *	coerced to <code>VT_DISPATCH</code>.
     * @see #Variant(Object)
     */
    public Object toObject() throws ComException
    {
        if( !isCreated() )
            throw new JAutException("The underlying VARIANT is not created");
        
        switch( getVartype() & ~VT_BYREF )
        {
            case VT_EMPTY:
            case VT_NULL:
                return null;
                
            case VT_BOOL:
                return new Boolean(getBoolean());
                
            case VT_UI1:
            case VT_I1:
                return new Byte(getByte());
                
            case VT_I2:
            case VT_UI2:
                return new Short(getShort());
                
            case VT_I4:
            case VT_UI4:
            case VT_ERROR:
            case VT_INT:
            case VT_UINT:
                return new Integer(getInt());
                
            case VT_CY:
                return new Long(getLong());
                
            case VT_R4:
                return new Float(getFloat());
                
            case VT_R8:
                return new Double(getDouble());
                
            case VT_BSTR:
                return getString();
                
            case VT_DATE:
                return getDate();
                
            case VT_BSTR | VT_ARRAY:
            	return getStringArray();

            case VT_DISPATCH:
            case VT_UNKNOWN:
                return getDispatch();
                
            default:
                throw new JAutException("Invalid variant type");
        }
    }
}
