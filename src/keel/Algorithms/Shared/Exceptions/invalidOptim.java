/***********************************************************************

	This file is part of KEEL-software, the Data Mining tool for regression, 
	classification, clustering, pattern mining and so on.

	Copyright (C) 2004-2010
	
	F. Herrera (herrera@decsai.ugr.es)
    L. S�nchez (luciano@uniovi.es)
    J. Alcal�-Fdez (jalcala@decsai.ugr.es)
    S. Garc�a (sglopez@ujaen.es)
    A. Fern�ndez (alberto.fernandez@ujaen.es)
    J. Luengo (julianlm@decsai.ugr.es)

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program.  If not, see http://www.gnu.org/licenses/
  
**********************************************************************/

/** 
* <p> 
* @author Written by Luciano S�nchez (University of Oviedo) 03/03/2004
* @author Modified by Enrique A. de la Cal (University of Oviedo) 13/12/2008  
* @version 1.0 
* @since JDK1.4 
* </p> 
*/

package keel.Algorithms.Shared.Exceptions;

public class invalidOptim extends Exception{
	/**
	  * <p>
	  * This exception is to report an invalid optimization method.
	  * </p>
	  */
	 /**
	  * <p>
	  * Creates an new invalidMutation object calling the super() method();
	  * 
	  * </p>	
	  */
 public  invalidOptim() { super(); }
 /**
  * <p>
  * 
  * Creates an new invalidOptim object calling the super(s) method() with the report string s.
  * </p>	
  *
  * @param s the report String.
  */
 public  invalidOptim(String s) { super(s); }
}

