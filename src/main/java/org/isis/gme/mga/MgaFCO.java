/*
 * Copyright (c) 2002, Vanderbilt University
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
 * Author: Gyorgy Balogh
 * Date last modified: 10/23/03
 */

package org.isis.gme.mga;

import org.isis.gme.meta.MgaMetaAttribute;
import org.isis.gme.meta.MgaMetaFCO;
import org.isis.gme.meta.MgaMetaRole;
import org.isis.jaut.Dispatch;
import org.isis.jaut.Variant;

/**
 * @author bogyom
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MgaFCO extends MgaObject
{   
    public static final String HELP_PREF                   = "help";
    public static final String COLOR_PREF                  = "color";
    public static final String CONN_SRC_END_STYLE_PREF     = "srcStyle";
    public static final String CONN_DST_END_STYLE_PREF     = "dstStyle";
    public static final String CONN_LINE_TYPE_PREF         = "lineType";
    public static final String MODEL_BACKGROUND_COLOR_PREF = "backgroundColor";
    public static final String MODEL_BORDER_COLOR_PREF     = "borderColor";
    public static final String PORT_NAME_COLOR_PREF        = "portColor";
    public static final String NAME_COLOR_PREF             = "nameColor";
    public static final String CONN_LABEL_FORMATSTR_PREF   = "labelFormatStr";
    public static final String CONN_SRC_LABEL1_PREF        = "srcLabel1";
    public static final String CONN_SRC_LABEL2_PREF        = "srcLabel2";
    public static final String CONN_DST_LABEL1_PREF        = "dstLabel1";
    public static final String CONN_DST_LABEL2_PREF        = "dstLabel2";
    public static final String NAME_POS_PREF               = "namePosition";
    public static final String AUTOROUTER_PREF             = "autorouterPref";
    public static final String ICON_PREF                   = "icon";
    public static final String PORT_ICON_PREF              = "porticon";
    public static final String DECORATOR_PREF              = "decorator";
    public static final String HOTSPOT_PREF                = "isHotspotEnabled";
              
    public MgaFCO()
    {
    }
    
    public MgaFCO( Dispatch d )
    {
        attach( d );
        changeInterface( "{32D1F3A7-D276-11D3-9AD5-00AA00B6FE26}" );
    }
    
    public MgaFCO( String compName )
    {
        //super(compName);
    }
    
        /*public int getStatus()
        {
                return Dispatch.get(this, "Status").toInt();
        }
         
        public boolean getIsWritable()
        {
                return Dispatch.get(this, "IsWritable").toBoolean();
        }
         
        public String getID()
        {
                return Dispatch.get(this, "ID").toString();
        }
         
        public String getName()
        {
                return Dispatch.get(this, "Name").toString();
        }
         
        public void setName(String lastParam)
        {
                Dispatch.CallSub(this, "Name", lastParam);
        }
         
        public MgaMetaBase getMetaBase()
        {
                return new MgaMetaBase(Dispatch.get(this, "MetaBase").toDispatch());
        }
         
        public boolean getIsEqual(MgaObject lastParam)
        {
                return Dispatch.call(this, "IsEqual", lastParam).toBoolean();
        }
         
        public void getParent(VT_PTR pVal, objtype_enum lastParam)
        {
                Dispatch.CallSub(this, "GetParent", pVal, lastParam);
        }
         
        public void getParent(VT_PTR pVal)
        {
                Dispatch.CallSub(this, "GetParent", pVal);
        }
         
        public void getParent(VT_PTR[] pVal, objtype_enum[] lastParam)
        {
                Variant vnt_pVal = new Variant();
                if( pVal == null || pVal.length == 0 )
                        vnt_pVal.noParam();
                else
                        vnt_pVal.putVT_PTRRef(pVal[0]);
         
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putobjtype_enumRef(lastParam[0]);
         
                Dispatch.CallSub(this, "GetParent", vnt_pVal, vnt_lastParam);
         
                if( pVal != null && pVal.length > 0 )
                        pVal[0] = vnt_pVal.toVT_PTR();
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toobjtype_enum();
        }
         
        public void checkProject(MgaProject lastParam)
        {
                Dispatch.CallSub(this, "CheckProject", lastParam);
        }
         
        public void destroyObject()
        {
                Dispatch.CallSub(this, "DestroyObject");
        }
         
        public void open(int lastParam)
        {
                Dispatch.CallSub(this, "Open", new Variant(lastParam));
        }
         
        public void open()
        {
                Dispatch.CallSub(this, "Open");
        }
         
        public void open(int lastParam)
        {
                Dispatch.CallSub(this, "Open", new Variant(lastParam));
         
        }
         
        public void close()
        {
                Dispatch.CallSub(this, "Close");
        }
         
        public void associate(Variant lastParam)
        {
                Dispatch.CallSub(this, "Associate", lastParam);
        }
         
        public Variant getCurrentAssociation()
        {
                return Dispatch.get(this, "CurrentAssociation");
        }
         
        public void sendEvent(int lastParam)
        {
                Dispatch.CallSub(this, "SendEvent", new Variant(lastParam));
        }
         
        public int getRelID()
        {
                return Dispatch.get(this, "RelID").toInt();
        }
         
        public void setRelID(int lastParam)
        {
                Dispatch.CallSub(this, "RelID", new Variant(lastParam));
        }
         
        public boolean getIsLibObject()
        {
                return Dispatch.get(this, "IsLibObject").toBoolean();
        }
         
        public void check()
        {
                Dispatch.CallSub(this, "Check");
        }
         
        public void checkTree()
        {
                Dispatch.CallSub(this, "CheckTree");
        }
         
        public void setExempt(boolean lastParam)
        {
                Dispatch.CallSub(this, "Exempt", new Variant(lastParam));
        }
         
        public boolean getExempt()
        {
                return Dispatch.get(this, "Exempt").toBoolean();
        }
         
        public MgaObjects getChildObjects()
        {
                return new MgaObjects(Dispatch.get(this, "ChildObjects").toDispatch());
        }
         
        public MgaObject getChildObjectByRelID(int lastParam)
        {
                return new MgaObject(Dispatch.call(this, "ChildObjectByRelID", new Variant(lastParam)).toDispatch());
        }
         
        public MgaObject getObjectByPath(String lastParam)
        {
                return new MgaObject(Dispatch.call(this, "ObjectByPath", lastParam).toDispatch());
        }*/
    
    public MgaMetaFCO getMeta()
    {
        return new MgaMetaFCO( (Dispatch)get( "Meta" ) );
    }
    
    public MgaMetaRole getMetaRole()
    {
        return new MgaMetaRole( (Dispatch)get("MetaRole") );
    }
     
    public MgaModel getParentModel()
    {
        return new MgaModel( (Dispatch)get("ParentModel") );
    }
     
    public MgaFolder getParentFolder()
    {
        return new MgaFolder( (Dispatch)get("ParentFolder") );
    }
    
    public MgaParts getParts()
    {
        return new MgaParts( (Dispatch)get( "Parts" ) );
    }
    
    /*public MgaPart getPart(MgaMetaAspect lastParam)
    {
            return new MgaPart(Dispatch.call(this, "Part", lastParam).toDispatch());
    }
     
    public MgaPart getPartByMetaPart(MgaMetaPart lastParam)
    {
            return new MgaPart(Dispatch.call(this, "PartByMetaPart", lastParam).toDispatch());
    }*/
         
    public MgaAttributes getAttributes()
    {
        return new MgaAttributes( (Dispatch)get("Attributes") );
    }
         
    public MgaAttribute getAttribute( MgaMetaAttribute metaAttr )
    {
        return new MgaAttribute((Dispatch)call("Attribute", metaAttr));
    }
     
    public Object getAttributeByName( String name )
    {
        return get("AttributeByName", name);
    }
     
    public void setAttributeByName( String name, Variant val )
    {
        put("AttributeByName", name, val );
    }
     
    public String getStrAttrByName( String name )
    {
		return get("StrAttrByName", name ).toString();
    }
     
    public void setStrAttrByName(String name, String val )
    {
        put( "StrAttrByName", name, val );
    }
     
    public int getIntAttrByName( String name )
    {
        return ((Integer)get("IntAttrByName", name)).intValue();
    }
     
    public void setIntAttrByName( String name, int val )
    {
        put( "IntAttrByName", name, new Integer(val) );
    }
     
    public float getFloatAttrByName( String name )
    {
    	Object comVal = get("FloatAttrByName", name);
    	if (comVal instanceof Float) {
			return ((Float)comVal).floatValue();
			
		}else if (comVal instanceof Double) {
			return ((Double)comVal).floatValue();
			
		}
       throw new ClassCastException("Cannot cast " + comVal.getClass() + "to " + Float.class);
    }
     
    public void setFloatAttrByName( String name, float val )
    {
        put( "FloatAttrByName", name, new Float(val) );
    }
     
    public boolean getBoolAttrByName( String name )
    {
        return ((Boolean)get("BoolAttrByName", name)).booleanValue();
    }
     
    public void setBoolAttrByName(String name, boolean lastParam)
    {
        put( "BoolAttrByName", name, new Boolean(lastParam) );
    }
     
    public MgaFCO getRefAttrByName( String attr )
    {
        return new MgaFCO( (Dispatch)get("RefAttrByName", attr) );
    }
     
    public void setRefAttrByName( String name, MgaFCO fco ) 
    {
        put( "RefAttrByName", name, fco );
    }
     
    public void clearAttrByName( String attr )
    {
        call( "ClearAttrByName", attr );
    }
        
    public MgaRegNode getRegistryNode(String path)
    {
        return new MgaRegNode( (Dispatch)get( "RegistryNode", path ) );
    }                 
         
    /*public MgaRegNodes getRegistry(boolean lastParam)
    {
            return new MgaRegNodes(Dispatch.call(this, "Registry", new Variant(lastParam)).toDispatch());
    }
     
    public MgaRegNodes getRegistry()
    {
            return new MgaRegNodes(Dispatch.get(this, "Registry").toDispatch());
    }
     
    public MgaRegNodes getRegistry(boolean lastParam)
    {
            MgaRegNodes result_of_Registry = new MgaRegNodes(Dispatch.call(this, "Registry", new Variant(lastParam)).toDispatch());
     
     
            return result_of_Registry;
    }*/
         
    public String getRegistryValue(String path)
    {
        return (String)get( "RegistryValue", path );
    }
         
    public void setRegistryValue(String path, String value)
    {
    	call("SetRegistryValueDisp", path, value);
    }
     
    /*public MgaConstraints getConstraints(boolean lastParam)
    {
            return new MgaConstraints(Dispatch.call(this, "Constraints", new Variant(lastParam)).toDispatch());
    }
     
    public void addConstraint(MgaConstraint lastParam)
    {
            Dispatch.CallSub(this, "AddConstraint", lastParam);
    }
     
    public void defineConstraint(String name, int mask, String expr, VT_PTR lastParam)
    {
            Dispatch.CallSub(this, "DefineConstraint", name, new Variant(mask), expr, lastParam);
    }
     
    public void defineConstraint(String name, int mask, String expr, VT_PTR[] lastParam)
    {
            Variant vnt_lastParam = new Variant();
            if( lastParam == null || lastParam.length == 0 )
                    vnt_lastParam.noParam();
            else
                    vnt_lastParam.putVT_PTRRef(lastParam[0]);
     
            Dispatch.CallSub(this, "DefineConstraint", name, new Variant(mask), expr, vnt_lastParam);
     
            if( lastParam != null && lastParam.length > 0 )
                    lastParam[0] = vnt_lastParam.toVT_PTR();
    }
     
    public MgaFCO getDerivedFrom()
    {
            return new MgaFCO(Dispatch.get(this, "DerivedFrom").toDispatch());
    }
     
    public MgaFCOs getDerivedObjects()
    {
            return new MgaFCOs(Dispatch.get(this, "DerivedObjects").toDispatch());
    }*/
     
    public MgaFCO getType()
    {
        return new MgaFCO((Dispatch)get("Type"));
    }
     
    public MgaFCO getBaseType()
    {
        return new MgaFCO((Dispatch)get("BaseType"));
    }
     
    public MgaFCO getArcheType()
    {
        return new MgaFCO((Dispatch)get("ArcheType"));
    }
     
    public boolean getIsInstance()
    {
        return ((Boolean)get("IsInstance")).booleanValue();
    }
     
    public boolean getIsPrimaryDerived()
    {
        return ((Boolean)get("IsPrimaryDerived")).booleanValue();
    }
     
    /*public void attachToArcheType(MgaFCO newtype, boolean lastParam)
    {
            Dispatch.CallSub(this, "AttachToArcheType", newtype, new Variant(lastParam));
    }
     
    public void detachFromArcheType()
    {
            Dispatch.CallSub(this, "DetachFromArcheType");
    }
     
    public void getAbsMetaPath(String lastParam)
    {
            Dispatch.CallSub(this, "GetAbsMetaPath", lastParam);
    }
     
    public void getAbsMetaPath(String[] lastParam)
    {
            Variant vnt_lastParam = new Variant();
            if( lastParam == null || lastParam.length == 0 )
                    vnt_lastParam.noParam();
            else
                    vnt_lastParam.putStringRef(lastParam[0]);
     
            Dispatch.CallSub(this, "GetAbsMetaPath", vnt_lastParam);
     
            if( lastParam != null && lastParam.length > 0 )
                    lastParam[0] = vnt_lastParam.toString();
    }
     
    public void getRelMetaPath(MgaFCO begfco, String relpath, MgaFCOs lastParam)
    {
            Dispatch.CallSub(this, "GetRelMetaPath", begfco, relpath, lastParam);
    }
     
    public void getRelMetaPath(MgaFCO begfco, String relpath)
    {
            Dispatch.CallSub(this, "GetRelMetaPath", begfco, relpath);
    }
     
    public void getRelMetaPath(MgaFCO begfco, String[] relpath, MgaFCOs lastParam)
    {
            Variant vnt_relpath = new Variant();
            if( relpath == null || relpath.length == 0 )
                    vnt_relpath.noParam();
            else
                    vnt_relpath.putStringRef(relpath[0]);
     
            Dispatch.CallSub(this, "GetRelMetaPath", begfco, vnt_relpath, lastParam);
     
            if( relpath != null && relpath.length > 0 )
                    relpath[0] = vnt_relpath.toString();
    }*/
     
    public MgaConnPoints getPartOfConns()
    {
        return new MgaConnPoints((Dispatch)get("PartOfConns"));
    }
     
    public MgaFCOs getMemberOfSets()
    {
        return new MgaFCOs((Dispatch)get("MemberOfSets"));
    }
     
    public MgaFCOs getReferencedBy()
    {
        return new MgaFCOs((Dispatch)get("ReferencedBy"));
    }
     
    public MgaFCOs createCollection()
    {
        Variant returned_fcos_variant = new Variant();
        returned_fcos_variant.allocate(Variant.VT_DISPATCH);
               
        Variant arguments[] = new Variant[1];
        arguments[0]        = new Variant(returned_fcos_variant);

        Variant retval = new Variant();
        retval.allocate(Variant.VT_EMPTY);

        invoke( getIDOfName("CreateCollection"), DISPATCH_METHOD, arguments, null, retval );

        return new MgaFCOs( returned_fcos_variant.getDispatch() );
    }
         
    public MgaFCO getRootFCO()
    {
        return new MgaFCO((Dispatch)get("RootFCO"));
    }
}

