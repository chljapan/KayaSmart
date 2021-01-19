/*
 * Copyright (c) 2007,2008 Vanderbilt University
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
 */

/**
 * @author Andras Nadas
 * @last modified 04/22/2008
 */

package org.isis.gme;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JOptionPane;

import org.isis.gme.GMEOLEApp;
import org.isis.gme.bon.JBuilder;
import org.isis.gme.mga.MgaClient;
import org.isis.gme.mga.MgaClients;
import org.isis.gme.mga.MgaProject;
import org.isis.jaut.Apartment;
import org.isis.jaut.Dispatch;

public class ConsoleWriter {
	
	private static ConsoleWriter _instance = null;
	private static GMEOLEApp gmeApp = null;
	private static AreaPrinter errorPrinter = null;
	private static AreaPrinter stdPrinter = null;
	
	private ConsoleWriter(JBuilder builder)
	{
		this(builder.getProject());
	}
	
	private ConsoleWriter(MgaProject project)
	{
		Object clients = project.get("Clients");
		MgaClients mgaClients = new MgaClients((Dispatch)clients);
		for(MgaClient c: mgaClients.getAll()){
			if(c.getName().equals("GME.Application")){
				gmeApp = new GMEOLEApp(c.getOLEServer());
				stdPrinter = new AreaPrinter(GMEOLEApp.MSG_NORMAL);
				errorPrinter = new AreaPrinter(GMEOLEApp.MSG_ERROR);
				return;
			}			
		}
		JOptionPane.showMessageDialog(null,"Unable to redirect streams");
	}
	
	public static void redirectStreamsToConsole(JBuilder builder){
		_instance = new ConsoleWriter(builder);
		System.setOut(new PrintStream(stdPrinter));
		System.setErr(new PrintStream(errorPrinter));
	}
	
	public static void redirectStreamsToConsole(MgaProject project){
		_instance = new ConsoleWriter(project);
		System.setOut(new PrintStream(stdPrinter));
		System.setErr(new PrintStream(errorPrinter));
	}
	
	public static ConsoleWriter getInstance(){
		return _instance;
	}

	public static void flushConsole(){
		if(stdPrinter != null)
			stdPrinter.flushContent();
		if(errorPrinter != null)
			errorPrinter.flushContent();
	}
	
	public static void clearConsole(){
		if(gmeApp != null){
			gmeApp.consoleClear();
		}
	}
	
	private class AreaPrinter extends OutputStream implements Runnable{

		private int type;
		private String line = "";
		
		private Thread writerThread = null;
		
		public AreaPrinter(int type){
			this.type = type;
			//writerThread = new Thread(AreaPrinter.this);
			//writerThread.start();
		}
		
		@Override
		public void write(int b) throws IOException {

			// "<" or ">" requires explicit "&lt" or "&gt"
			synchronized(line) {
				line += "" + (char)b;	
			}			
		}
		
		public void flushContent(){
			if(line.length() > 0){
				for(String l:line.split("\n")){
					gmeApp.consoleMessage(l, type);
				}
				line = "";
			}
		}

		public void run() {
			Apartment.enter(false);
			String l = "";
			while(true){
				int iln = -1;
				synchronized (line) {
					iln = line.indexOf("\n");
					if(iln != -1){
						l = line.substring(0, iln);
						line = line.substring(iln+1);
					}
				}
			
				if(iln != -1){
					gmeApp.consoleMessage(l, type);
				}else{
					try {
						synchronized(writerThread){
							if(writerThread.isAlive())
								writerThread.wait(10);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
			}
		}
		
	}
}
