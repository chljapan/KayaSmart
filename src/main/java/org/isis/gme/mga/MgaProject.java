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

import org.isis.gme.meta.*;
import org.isis.jaut.*;

/**
 * @author bogyom
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MgaProject extends Dispatch
{
    // this is temporal, should go to another class (factory)
    public static MgaProject createInstance()
    {
        MgaProject project = new MgaProject();
        project.attachNewInstance( "Mga.MgaProject", Dispatch.CLSCTX_INPROC_SERVER );
        return project;
    }
    
    public MgaProject()
    {
    }
    
    public MgaProject( Dispatch d )
    {
        attach( d );
        changeInterface( "{270B4F92-B17C-11D3-9AD1-00AA00B6FE26}" );
    }
    
    public void create( String projectConnection, String paradigmName )
    {
        call( "Create", projectConnection, paradigmName );
    }
    
/*	public void createEx( String projectConnection, String paradigmName, byte[] paradigmGUID )
        {
                SafeArray array = new SafeArray(Variant.VariantByte, 16);
                array.fromByteArray(paradigmGUID);
 
                Variant v = new Variant();
                v.putSafeArray(array);
 
                call( "CreateEx", projectConnection, paradigmName, v );
        }*/
    
    public boolean open( String projectConnection )
    {
        Variant readonly     = new Variant( false );
        Variant readonly_ref = new Variant( readonly );
        
        call( "Open", projectConnection, readonly_ref );
        
        return readonly.getBoolean();
    }
    
/*	public void openEx(String projectConnection, String newParadigmName, byte[] newParadigmGUID)
        {
                Variant v = new Variant();
 
                if( newParadigmGUID != null )
                {
                        SafeArray array = new SafeArray(Variant.VariantByte, 16);
                        array.fromByteArray(newParadigmGUID);
                        v.putSafeArray(array);
                }
 
                call( "OpenEx", projectConnection, newParadigmName, v, new Variant(""));
        }*/
    
    public void close()
    {
        call( "Close" );
    }
    
    public void close( boolean abortTransaction )
    {
        call( "Close", new Variant(abortTransaction) );
    }
    
    public void save()
    {
        call( "Save" );
    }
    
    public void save( String newProjectConnection, boolean forgetIt )
    {
        call( "Save", newProjectConnection, new Variant(forgetIt) );
    }
    
    public void checkLocks( String projectConnection, boolean clearLocks )
    {
        call( "CheckLocks", projectConnection, new Variant(clearLocks) );
    }
    
    public void setPreferences( int flags )
    {
        put( "Preferences", new Integer(flags) );
    }
    
    public int getPreferences()
    {
        return ((Integer)get( "Preferences" )).intValue();
    }
    
    public void setOperationsMask(int lastParam)
    {
        put( "OperationsMask", new Integer(lastParam) );
    }
    
    public int getOperationsMask()
    {
        return ((Integer)get( "OperationsMask" )).intValue();
    }
  
    public MgaTerritory createTerritory( Dispatch handler, Dispatch rwhandler )
    {
       	Variant returned_territory_variant = new Variant();
        
    	Variant arguments[] = new Variant[3];
        arguments[0]        = Variant.create(Variant.VT_NULL);
        arguments[1]        = new Variant(returned_territory_variant);
        arguments[2]        = Variant.create(Variant.VT_NULL);
         
        Variant retval = new Variant();
        retval.allocate(Variant.VT_EMPTY);

        invoke( getIDOfName("CreateTerritory"), DISPATCH_METHOD, arguments, null, retval );

        return new MgaTerritory( returned_territory_variant.getDispatch() );
    }
    
    public MgaTerritory createTerritoryWithoutSink()
    {
    	Variant returned_territory_variant = new Variant();
    	returned_territory_variant.allocate(Variant.VT_DISPATCH);
    	Variant arguments[] = new Variant[1]; 
    	arguments[0]        = new Variant(returned_territory_variant);
        
    	Variant retval = new Variant();
        retval.allocate(Variant.VT_EMPTY);

        invoke( getIDOfName("CreateTerritoryWithoutSink"), DISPATCH_METHOD, arguments, null, retval );

        return new MgaTerritory( returned_territory_variant.getDispatch() );
    }
    
    
    
/*	[ helpstring("method CreateTerritory (call outside transaction)")]
                        HRESULT CreateTerritory([in] IMgaEventSink *handler, [out] IMgaTerritory **terr, [in, defaultvalue(0)] IMgaEventSink *rwhandler);
        [ helpstring("method CreateAddOn (call outside transaction)")] HRESULT CreateAddOn([in] IMgaEventSink *handler, [out] IMgaAddOn **addon);
 
        [ propget, helpstring("property AddOns")]
                                HRESULT AddOns([out, retval] IMgaAddOns **addons);
        [ propget, helpstring("property Territories")]
                                HRESULT Territories([out, retval] IMgaTerritories **terrs);
        [ helpstring("property EnableAllAutoAddOns (by default auto addon loading disabled)")]
                                HRESULT EnableAutoAddOns([in] VARIANT_BOOL bEnable);
        [ propget, helpstring("property AddOnComponents")]
                                HRESULT AddOnComponents([out, retval] IMgaComponents **comps);
        [ propget, helpstring("property ActiveTerritory: the one which opened a transaction")]
                                HRESULT ActiveTerritory([out, retval] IMgaTerritory **aterr);
 
        [ helpstring("method BeginTransaction ")]
                                HRESULT BeginTransaction([in] IMgaTerritory *terr,
                                                                        [in, defaultvalue(TRANSACTION_GENERAL)] transactiontype_enum mode);
        [ propget, helpstring("property ProjectStatus mask bits: 0:is open? 1:open r/o? 2:has changed? 3:in transaction?, 4:transaction r/o?, 31:error?")]
                                HRESULT ProjectStatus([out, retval] long *stat);
 
        [ helpstring("method Notify: priority currently unused")] HRESULT Notify([in] long priority);
        [ helpstring("method CommitTransaction")] HRESULT CommitTransaction();
        [ helpstring("method AbortTransaction")] HRESULT AbortTransaction();
 
        [ helpstring("method CheckSupress")] HRESULT CheckSupress([in] VARIANT_BOOL mode);
        [ helpstring("method Undo (call outside transaction)")] HRESULT Undo();
        [ helpstring("method Redo (call outside transaction)")] HRESULT Redo();
        [ helpstring("method UndoRedoSize")] HRESULT UndoRedoSize([out] short *undosize, [out] short *redosize);
        [ helpstring("method FlushUndoQueue")] HRESULT FlushUndoQueue();*/
    
    public MgaTerritory getActvieTerritory()
    {   
    	Dispatch d = (Dispatch)get("ActiveTerritory");
    	if( d.isNull() )
    		return null;
    	else
    		return new MgaTerritory(d);
    }
    
    public void beginTransaction( MgaTerritory territory )
    {
        if( territory == null )
            territory = new MgaTerritory();
        call( "BeginTransaction", territory );
    }
    
    public void beginTransaction( MgaTerritory territory, int transactionType )
    {
        if( territory == null )
            territory = new MgaTerritory();
        call( "BeginTransaction", territory, new Integer( transactionType ) );
    }
    
    public void commitTransaction()
    {
        call( "CommitTransaction" );
    }
    
    public void abortTransaction()
    {
        call( "AbortTransaction" );
    }
    
    public int getProjectStatus()
    {
        return ((Integer)get( "ProjectStatus" )).intValue();
    }
    
    public void checkSupress( boolean mode )
    {
        call( "CheckSupress", new Variant(mode) );
    }
    
    public void undo()
    {
        call( "Undo" );
    }
    
    public void redo()
    {
        call( "Redo" );
    }
    
    public int getUndoSize()
    {
        Variant undoSize = new Variant( (short)0 );
        Variant redoSize = new Variant( (short)0 );
        
        call( "UndoRedoSize", undoSize, redoSize);
        
        return undoSize.toShort();
    }
    
    public int getRedoSize()
    {
        Variant undoSize = new Variant( (short)0 );
        Variant redoSize = new Variant( (short)0 );
        
        call( "UndoRedoSize", undoSize, redoSize);
        
        return redoSize.toShort();
    }
    
    public void flushUndoQueue()
    {
        call( "FlushUndoQueue" );
    }
    
    public MgaMetaProject getRootMeta()
    {
        return new MgaMetaProject( (Dispatch)get("RootMeta") );
    }
    
    public MgaFolder getRootFolder()
    {
        return new MgaFolder( (Dispatch)get("RootFolder") );
    }
    
    public MgaFilter createFilter()
    {
        return new MgaFilter( (Dispatch)call( "CreateFilter") );
    }
    
/*	public void createFilter(VT_PTR lastParam)
        {
                call( "CreateFilter", lastParam);
        }
 
        public void createFilter(VT_PTR[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putVT_PTRRef(lastParam[0]);
 
                call( "CreateFilter", vnt_lastParam);
 
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toVT_PTR();
        }
 
        public void allFCOs(MgaFilter filter, VT_PTR lastParam)
        {
                call( "AllFCOs", filter, lastParam);
        }
 
        public void allFCOs(MgaFilter filter, VT_PTR[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putVT_PTRRef(lastParam[0]);
 
                call( "AllFCOs", filter, vnt_lastParam);
 
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toVT_PTR();
        }
 
        public void getObjectByID(String iD, VT_PTR lastParam)
        {
                call( "GetObjectByID", iD, lastParam);
        }
 
        public void getObjectByID(String iD, VT_PTR[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putVT_PTRRef(lastParam[0]);
 
                call( "GetObjectByID", iD, vnt_lastParam);
 
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toVT_PTR();
        }
 
        public void getFCOByID(String iD, VT_PTR lastParam)
        {
                call( "GetFCOByID", iD, lastParam);
        }
 
        public void getFCOByID(String iD, VT_PTR[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putVT_PTRRef(lastParam[0]);
 
                call( "GetFCOByID", iD, vnt_lastParam);
 
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toVT_PTR();
        }
 
        public void getFCOsByName(String name, VT_PTR lastParam)
        {
                call( "GetFCOsByName", name, lastParam);
        }
 
        public void getFCOsByName(String name, VT_PTR[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putVT_PTRRef(lastParam[0]);
 
                call( "GetFCOsByName", name, vnt_lastParam);
 
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toVT_PTR();
        }
 
        public void getFolderByPath(String path, VT_PTR lastParam)
        {
                call( "GetFolderByPath", path, lastParam);
        }
 
        public void getFolderByPath(String path, VT_PTR[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putVT_PTRRef(lastParam[0]);
 
                call( "GetFolderByPath", path, vnt_lastParam);
 
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toVT_PTR();
        }
 
        public void enumExtReferences(MgaFCOs fcos, VT_PTR lastParam)
        {
                call( "EnumExtReferences", fcos, lastParam);
        }
 
        public void enumExtReferences(MgaFCOs fcos, VT_PTR[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putVT_PTRRef(lastParam[0]);
 
                call( "EnumExtReferences", fcos, vnt_lastParam);
 
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toVT_PTR();
        }
 
        public MgaObject getObjectByPath(String lastParam)
        {
                return new MgaObject(Dispatch.call(this, "ObjectByPath", lastParam).toDispatch());
        }
 
        public void getStatistics(String lastParam)
        {
                call( "GetStatistics", lastParam);
        }
 
        public void getStatistics(String[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putStringRef(lastParam[0]);
 
                call( "GetStatistics", vnt_lastParam);
 
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toString();
        }*/
 
    public String getName()
    {
        return (String)get("Name");
    }
     
    public void setName( String name )
    {
        call( "Name", name );
    }
 
        /*public Variant getMetaGUID()
        {
                return Dispatch.get(this, "MetaGUID");
        }
 
        public Variant getGUID()
        {
                return Dispatch.get(this, "GUID");
        }
 
        public void setGUID(Variant lastParam)
        {
                call( "GUID", lastParam);
        }
 
        public String getCreateTime()
        {
                return Dispatch.get(this, "CreateTime").toString();
        }
 
        public String getChangeTime()
        {
                return Dispatch.get(this, "ChangeTime").toString();
        }
 
        public String getAuthor()
        {
                return Dispatch.get(this, "Author").toString();
        }
 
        public void setAuthor(String lastParam)
        {
                call( "Author", lastParam);
        }
 
        public String getComment()
        {
                return Dispatch.get(this, "Comment").toString();
        }
 
        public void setComment(String lastParam)
        {
                call( "Comment", lastParam);
        }*/
 
    public String getProjectConnStr()
    {
        return (String)get( "ProjectConnStr" );
    }
 
    public String getParadigmConnStr()
    {
        return (String)get( "ParadigmConnStr" ); 
    }
 
        /*public MgaMetaBase getMetaObj(int lastParam)
        {
                return new MgaMetaBase(Dispatch.call(this, "MetaObj", new Variant(lastParam)).toDispatch());
        }
 
        public void queryProjectInfo(String projectName, int mgaversion, String paradigmName, Variant paradigmGUID, boolean lastParam)
        {
                call( "QueryProjectInfo", projectName, new Variant(mgaversion), paradigmName, paradigmGUID, new Variant(lastParam));
        }
 
        public void queryProjectInfo(String projectName, int[] mgaversion, String[] paradigmName, Variant[] paradigmGUID, boolean[] lastParam)
        {
                Variant vnt_mgaversion = new Variant();
                if( mgaversion == null || mgaversion.length == 0 )
                        vnt_mgaversion.noParam();
                else
                        vnt_mgaversion.putIntRef(mgaversion[0]);
 
                Variant vnt_paradigmName = new Variant();
                if( paradigmName == null || paradigmName.length == 0 )
                        vnt_paradigmName.noParam();
                else
                        vnt_paradigmName.putStringRef(paradigmName[0]);
 
                Variant vnt_paradigmGUID = new Variant();
                if( paradigmGUID == null || paradigmGUID.length == 0 )
                        vnt_paradigmGUID.noParam();
                else
                        vnt_paradigmGUID.putVariantRef(paradigmGUID[0]);
 
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putBooleanRef(lastParam[0]);
 
                call( "QueryProjectInfo", projectName, vnt_mgaversion, vnt_paradigmName, vnt_paradigmGUID, vnt_lastParam);
 
                if( mgaversion != null && mgaversion.length > 0 )
                        mgaversion[0] = vnt_mgaversion.toInt();
                if( paradigmName != null && paradigmName.length > 0 )
                        paradigmName[0] = vnt_paradigmName.toString();
                if( paradigmGUID != null && paradigmGUID.length > 0 )
                        paradigmGUID[0] = vnt_paradigmGUID.toVariant();
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toBoolean();
        }*/
    
}
