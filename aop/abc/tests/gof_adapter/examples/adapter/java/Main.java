package examples.adapter.java;

/* -*- Mode: Java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This file is part of the design patterns project at UBC
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * either http://www.mozilla.org/MPL/ or http://aspectj.org/MPL/.
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is ca.ubc.cs.spl.patterns.
 *
 * Contributor(s):   
 */

/**
 * Implements the driver for the Adapter design pattern example. <p> 
 *
 * Intent: <i> Convert the interface of a class into another interface clients
 * expect. Adapter lets classes work together that couldn't otherwise because
 * incompatible interfaces.</i><p>
 *
 * Experimental setup: <code>Screen</code> is the <i>Adaptee</i> that can print 
 * strings to <code>System.out</code>. <code>Printer</code> is the 
 * <i>Target</i> (interface), <code>PrinterScreenAdapter</code> is the 
 * <i>Adapter</i> that allows to access <code>Screen</code>'s functionality
 * via <code>Printer</code>'s interface.<p>   
 *
 * <i>This is the Java implementation.</i><p>
 *
 * The implementation is that of an <i>object adapter</i> (NOT class adapter),
 * as the latter requires multiple inheritance which Java does not provide. 
 *
 * @author Jan Hannemann
 * @author Gregor Kiczales
 * @version 1.0, 05/13/02
 *
 * @see Printer
 * @see Screen
 * @see PrinterScreenAdapter
 */

public class Main { 
	
    /**
     * the Adapter in the scenario
     */
     	
	private static Printer adapter; 

    /**
     * the Adaptee in the scenario
     */
     	
	private static Screen screen; 

    /**
     * Implements the driver. It creates a <code>PrinterScreenAdapter</code>
     * that implements <code>Printer</code>'s interface and acts as an 
     * <i>Adapter</i> for a <code>Screen</code> object. It then calls 
     * <code>print(String)</code> on it. The <code>PrinterScreenAdapter</code>
     * forwards the call to it's <code>Screen</code>'s <code>
     * printToStdOut(String)</code> method.
     *
     * @param args required for a main method, but ignored
     */
     
	public static void main(String[] args) {

		System.out.println("Creating the Adaptee...");
		screen = new Screen();

	    System.out.println("Creating the Adapter...");
		adapter = new PrinterScreenAdapter(screen); 

		System.out.println("Adapter and Adaptee are the same object: "+(adapter.equals(screen)));

		System.out.println("Issuing the request() to the Adapter...");
		adapter.print("Test successful."); 
		
	}
}