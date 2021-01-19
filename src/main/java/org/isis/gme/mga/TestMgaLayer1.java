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

/*
 * TestMgaLayer1.java
 *
 * Created on September 17, 2003, 10:08 AM
 */

package org.isis.gme.mga;

import org.isis.gme.meta.MgaMetaFolder;
import org.isis.jaut.Apartment;

public class TestMgaLayer1
{    
    /**
     * Print out item names recursively 
     * @param obj
     * @param prefix
     */
    static void printName( MgaObject obj , String prefix )
    {
        System.out.println( prefix + obj.getName() );
        
        int type = obj.getObjType();
        if( type == MgaObject.OBJTYPE_FOLDER || type == MgaObject.OBJTYPE_MODEL )
        {
            MgaObjects children = obj.getChildObjects();
            for( int i=0; i<children.getCount(); i++ )
                printName( children.getItem(i) , prefix + "  " );
        }
    }
    
    static void printOutStructureTest()
    {
        Apartment.enter(true);       
        
        // create and open mga project
        MgaProject project = MgaProject.createInstance();
        project.open( "MGA=C:\\Temp\\1.mga" );

        project.beginTransaction( null );

        // print out structure
        printName( new MgaObject( project.getRootFolder() ), "" );               

        project.commitTransaction();
    }
        
    /**
     * Creates a project with two folders
     */
    static void createProjectTest()
    {
        try
        {
            Apartment.enter(false);

            MgaProject project = MgaProject.createInstance();
            project.create( "MGA=C:\\Temp\\servece.mga", "SF" );
            project.beginTransaction(null);
            
            MgaFolder dest_root = project.getRootFolder();
                                   
            MgaMetaFolder meta_folder = project.getRootMeta().getRootFolder().getDefinedFolders().getItem(0);

            MgaFolder folder = dest_root.createFolder(meta_folder);
            folder.setName("created_folder1");            
            folder = dest_root.createFolder(meta_folder);
            folder.setName("created_folder2");            
            
            project.commitTransaction();
            project.save();
            
            Apartment.leave();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }

    
    public static void main(String[] args)
    {
        //printOutStructureTest();
        createProjectTest();
    }
    
}
