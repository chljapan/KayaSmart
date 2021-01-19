/*
 * Copyright (c) 2004, Vanderbilt University
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
 * Date last modified: 02/11/04
 */

package org.isis.gme.bon;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JOptionPane;

import org.isis.gme.mga.MgaFCO;
import org.isis.gme.mga.MgaFCOs;
import org.isis.gme.mga.MgaProject;
import org.isis.jaut.Apartment;
import org.isis.jaut.Dispatch;

public final class ComponentInvoker
{
    private static void reportError(Exception e)
    {
        String trace = new String();
        StackTraceElement[] s = e.getStackTrace();
        for(int i=0; i<s.length; ++i)
            trace += s[i].toString() + "\n"; 
        JOptionPane.showMessageDialog(null, "Exception in java interpreter: " + 
            e.toString() + "\nStack trace:\n" + trace, "Java Interpreter Exception", 
            JOptionPane.ERROR_MESSAGE);        
    }

    /**
     * Main entry point of java interpreter invocation. GME calls this method when the user
     * starts a java interpreter
     * 
     * @param classPath     ClassPath of the user defined interpreter
     * @param jclass        Class of the user defined interpreter
     * @param iproject      MgaProject COM interface pointer
     * @param icurrentObj   The current MgaFCO COM object pointer
     * @param iselectedObjs MGAFCOs COM object pointer containing the selected object
     * @param param         custom parameter for interpreter
     */
    public static void invokeEx(String classPath, String jclass, int iproject, 
                                int icurrentObj, int iselectedObjs, int param )
    {
        MgaProject     project       = null;
        MgaFCO         currentObj    = null;
        MgaFCOs        selectedObjs  = null;
        JBuilder       builder       = null;
        JBuilderObject currentBONObj = null;
        Collection<JBuilderObject>     selected      = null;
        
        boolean entered = false; 
                
        try
        {
            Apartment.enter(false);
            entered = true;
                       
            if(iproject == 0)
                throw new IllegalArgumentException("Given MGAProject pointer is NULL");
        
            // load class
            String paths[] = classPath.split(System.getProperty("path.separator"));
            URL[] urls = new URL[paths.length];
            for( int i=0; i<paths.length; ++i )
                urls[i] = new File(paths[i]).toURI().toURL();

            ClassLoader cl   = new URLClassLoader(urls);
            Class<?>       cls  = cl.loadClass(jclass);
            
            // create low level objects
            project      = new MgaProject(new Dispatch(iproject));
            currentObj   = null;
            selectedObjs = null;
            if(icurrentObj != 0)
                currentObj = new MgaFCO(new Dispatch(icurrentObj));
            if(iselectedObjs != 0)
                selectedObjs = new MgaFCOs(new Dispatch(iselectedObjs));
                
            // create interpreter
            Object comp = cls.newInstance(); 
                                         
            // invoke component           
            if( comp instanceof Component)
            {
                ((Component)comp).invokeEx(project, currentObj, selectedObjs, param);
            }
            else if(comp instanceof BONComponent)
            {
                JBuilderFactory.setClassLoader(cl);
                builder = new JBuilder(project,(BONComponent)comp);
                currentBONObj = null;
                if(currentObj != null)
                    currentBONObj = builder.findObject(currentObj);                    
                if( selectedObjs != null )
                {
                    selected = new ArrayList<JBuilderObject>();                    
                    int n = selectedObjs.getCount();
                    for( int i=0; i<n; ++i )
                        selected.add(builder.findObject(selectedObjs.getItem(i)));
                }
                else
                    selected = null;
                                                                       
                ((BONComponent)comp).invokeEx(builder, currentBONObj, selected, param);
            }
            else
            {
                extracted();            
            }            
        }
        catch(Exception e)
        {
            reportError(e);                                   
        }
        finally
        {
            // free all objects
            project       = null;
            currentObj    = null;
            selectedObjs  = null;
            builder       = null;
            currentBONObj = null;
            selected      = null;
            if( entered )
                Apartment.leave();
        }
    }

	private static void extracted() {
		throw new IllegalArgumentException("Java interpreter must implement Component or BONComponent interface.");
	}
}
