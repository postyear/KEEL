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

package keel.Algorithms.RE_SL_Postprocess.Post_A_TS_Lateral_FRBSs;
import java.util.*;
//import keel.Dataset.*;
//import ecm.*;
import org.core.*;
/**
 * The population class
 * @author Diana Arquillos
 *
 */
public class Poblacion {

	private int Trials;//trials contador
	private double THRESHOLD;
	//private double THRESHOLDR;
	private int Genes;//numero interpretado de genes
	private int Popsize;//tam poblacion
	private int POPSIZE;
	private int BITS_GEN;
	private Cromosoma BEST_CROM;
	private Gen [] Gene;
	private Cromosoma [] Poblacion;
	private double Best,Worst;  
	private int [] sample;
	private Funciones F;
	private Ecm E;
	private static int flag=1;
	private static int flag2=1;
	private static int flag3=1;
	private static char String1[];
	private static char String2[];
	private static char tmpstring[];
	private static int posiciones[];
	private static  char aux1[];
	private static  char aux2[];
//	private int Reiniciado;
	//private double reduccionIni;
	//private double reduccionR;
    private int n_reglas_total;
    //private int GenesA;
    //private int Ajuste;
    //private int noCruce;
    //private int noCruceR;
	//Randomize.Randomize r;
	int contador=0;
	int contador2=0;
	
	/**
	 * It compares 2 values which is better
	 * @param p1 one value to compare
	 * @param p2 the other value to compare
	 * @return it returns true if the first is better tha the second one
	 */
	public Boolean Better(Double p1, Double p2){
		if(p1>p2) return false;
		else return true;
		
	}
	
	
	/**
	 * It is the constructor of the class poblacion
	 * @param pop it contains the size of population
	 * @param bits it contains the number of bits
	 */
	public Poblacion(int pop, int bits){
		Popsize= pop;
		BITS_GEN=bits;
		
	}
	/**
	 * It initialize the population
	 * @param tam1 It contains the size of the table of training
	 * @param v1 It contains the training input values
	 * @param s1 It contains the training output values
	 * @param tam2 It contains the size of the table of test
	 * @param v2 It contains the test input values
	 * @param s2 It contains the test output values
	 * @param n_variables It contains the number of variables
	 * @param reglas It contains the number of rules
	 * @param var It contains the number of state variables
	 * @param sal It contains the exit value
	 * @param v It contains the values of data base
	 * @param semilla It contains the value of the seed
	 */ 
	public void Initialize(int tam1, double [][]v1, double[]s1,int tam2,double [] []v2,double[] s2,int n_variables,int reglas, int var, double sal,double []v, long semilla){

		/* INICIALIZAR VARIABLES */
		
		int i, j, pos;
		contador=contador2=0;
		Randomize.setSeed(semilla);
		E = new Ecm(tam1,v1,s1,tam2,v2,s2,n_variables,reglas, var, sal,v);
		Poblacion = new Cromosoma[2*Popsize]; 
		Trials = 0;
		
		Genes = E.base().getN_reglas()* E.base().getN_variables();
		Gene = new Gen[Genes];
		n_reglas_total=reglas;
		THRESHOLD = (double)(Genes*BITS_GEN+n_reglas_total)/4.0;
		
		Gen aux= new Gen();
		aux.set_min(0.0);
		aux.set_max(1.0);
		
		for (i=0; i<Genes; i++) {
			Gene[i]=aux;
		 }
		for(i=0;i<2*Popsize;i++){
			Poblacion[i]=new Cromosoma(Genes,n_reglas_total);
		}
		sample = new int [Popsize];
		BEST_CROM= new Cromosoma(Genes,n_reglas_total);
		
		if (E.tipoBC()==0) {
			for (j=0; j<Genes; j++){
				BEST_CROM.set_gene(j, Gene[j].min() + (Gene[j].max() - Gene[j].min())/2.0);
				Poblacion[0].set_gene(j,BEST_CROM.gene(j));
			}
		}
		else {
			for (i=0, pos=0; i<n_reglas_total; i++)
				for (j=0; j<n_variables; j++, pos++){
					BEST_CROM.set_gene(pos,  E.base().BaseC_desp[E.base().getIndex(i)][j] + 0.5);
					Poblacion[0].set_gene (pos,BEST_CROM.gene(j));
				}	
		}

		for (j = 0; j < n_reglas_total; j++){
		   	BEST_CROM.set_geneR(j,(char) 1);
			Poblacion[0].set_geneR(j, (char)1);
		}
			for (i=1; i < Popsize; i++){
		     for (j = 0; j < Genes; j++)
		    	Poblacion[i].set_gene(j,Gene[j].min() + (Gene[j].max() - Gene[j].min())*   Randomize.Rand());
				for (j=0; j<n_reglas_total; j++){  
		        	int auxi = Randomize.Randint(0,1);
		        		Poblacion[i].set_geneR(j, (char)auxi);
				}	
			}
			F = new Funciones();
	
	}
	
	
	/**
	 * Function which restart the population
	 */
	public void ReStart(){
		
		int i, j, i_mejor;
		
		if (BEST_CROM.entrado()==1) {
			/* BUSCAR EL MEJOR ELEMENTO */
			for (i=i_mejor=0; i<Popsize; i++)
			    if (Better(Poblacion[i].perf(), Poblacion[i_mejor].perf())) 
			    	i_mejor=i;

			/* COLOCAR EL MEJOR EN LA PRIMERA POSICION */
			for (j=0; j<Genes; j++)
			     Poblacion[0].set_gene(j,Poblacion[i_mejor].gene(j));
			for (j=0; j<n_reglas_total; j++)  
				Poblacion[0].set_geneR(j, Poblacion[i_mejor].geneR(j));
			
			Poblacion[0].set_perf(Poblacion[i_mejor].perf());
			i=1;
		}
		else i=0;
		for (i=1; i<Popsize; i++){
		    for (j=0; j<Genes; j++){
		    	Poblacion[i].set_gene(j,Gene[j].min() + (Gene[j].max() - Gene[j].min())*   Randomize.Rand());
		    }
		    for (j=0; j<n_reglas_total; j++){  
		        	double auxi = Randomize.Randint(0,1);
		        	if(auxi < 0.5)
		        		Poblacion[i].set_geneR(j, (char)0);
		        	else
		    		Poblacion[i].set_geneR(j, (char)1);
				}	
	
	}	    
	}
	
	/**
	 *Function which evaluates the population 
	 * @param inicio it contains the beginning of the evaluation
	 * @param fin it contains the end of the evaluation
	 */
	public void Evaluate(int inicio, int fin){	
		int i;
		double performance=0.;
       for (i=inicio; i<fin; i++) {
    	   Poblacion[i].set_perf (E.eval_EC(Poblacion[i].Gene(),Poblacion[i].GeneR(),n_reglas_total)); //eval(Poblacion.elementAt(i).Gene);
    	   performance = Poblacion[i].perf();
	     	Trials++;
			if (Trials == 1)
	      	this.Best=this.Worst=performance;
	      if (Better(performance, Best))
	      	this.Best = performance;
			if (Better(Worst, performance))
	      	this.Worst = performance;
	   }
    }
	
	/**
	 * Function which selects the elements of the population 
	 */
	public void Select(){

	   int i, j;

	   /* BUSCAR LOS Popsize MEJORES Y COLOCARLOS AL PRINCIPIO */
	   for(i=0; i<Popsize; i++)
	      for(j=i+1; j<POPSIZE; j++)
	         if (Better(Poblacion[j].perf(), Poblacion[i].perf()))
	            INTERCAMBIAR(i, j);

		if (Poblacion[0].perf() < BEST_CROM.perf()) {
			for (j = 0; j < Genes; j++)	
		   	BEST_CROM.set_gene(j,Poblacion[0].gene(j));
			  for (j=0; j<n_reglas_total; j++)  
				  BEST_CROM.set_geneR(j, Poblacion[0].geneR(j));
			BEST_CROM.set_perf(Poblacion[0].perf());
	        BEST_CROM.set_entrado(1);
	   }
	}
	
	/**
	 * It exchanges 2 values
	 * @param i1 it is the first position for the exchange
	 * @param i2 it is the second position for the exchange
	 */
	
private	void INTERCAMBIAR(int i1, int i2){
	   double tempc;
	   char tempcR;
	   int i, temphe;
	 
	   for(i=0; i<Genes; i++)
	      {
	      tempc=Poblacion[i1].gene(i); 
	      Poblacion[i1].set_gene(i,Poblacion[i2].gene(i)); 
	      Poblacion[i2].set_gene(i,tempc);
	      }
	   for(i=0; i<n_reglas_total; i++)
	      {
	      tempcR=Poblacion[i1].geneR(i);
	      Poblacion[i1].set_geneR(i,Poblacion[i2].geneR(i));
	      Poblacion[i2].set_geneR(i,tempcR);
	      }
	   tempc =Poblacion[i1].perf();
	   Poblacion[i1].set_perf(Poblacion[i2].perf());
	   Poblacion[i2].set_perf(tempc);

	   temphe =Poblacion[i1].entrado();
	   Poblacion[i1].set_entrado(Poblacion[i2].entrado());
	   Poblacion[i2].set_entrado(temphe);
	}
	/**
	 * It calculates the hamming distance
	 * @param Cr_1 a vector which is need for calculate the distance
	 * @param Cr_2 the other vector which compare with Cr_1 to get the distance
	 * @return the number of different values between the vectors
	 */
	private	int DistHam(char [] Cr_1, char [] Cr_2,int genes){

	  int i, dist;
	  for (i=0, dist=0; i<genes; i++) {
		  if (Cr_1[i]!=Cr_2[i]) 
			  dist++;
	  }
	  return dist ;
	}

	/**
	  * It calculates PCBLX
	  * @param d it multiplies the module of the difference of the parents  
	  * @param P1 it is a father to cross
	  * @param P2 it is the other father to do the cross
	  * @param Hijo1 the son obtained
	  * @param Hijo2 the other obtained
	  * @param gens the size of the vector
	  */
	private void xPC_BLX(double d, double []P1, double []P2,
			double [] Hijo1, double [] Hijo2, int gens){

		double I, A1, C1;
		int i;
		
		for (i=0; i< gens; i++)
		{
		
		I= d * Math.abs(P1[i]- P2[i]);
		A1=P1[i]-I; if (A1<Gene[i].min()) A1=Gene[i].min();
		C1=P1[i]+I; if (C1>Gene[i].max()) C1=Gene[i].max();
		double aux1= Randomize.Rand();
		Hijo1[i]= A1 +    aux1*(C1-A1);
		A1=P2[i]-I; if (A1<Gene[i].min()) A1=Gene[i].min();
		C1=P2[i]+I; if (C1>Gene[i].max()) C1=Gene[i].max();
		Hijo2[i] = A1 +     Randomize.Rand()*(C1-A1);
		
		}
	}
	/**
	 * It calculates the cross HUX
	 * @param Padre1 it is a father to cross
	 * @param Padre2 it is the other father to do the cross
	 * @param Hijo1 the son obtained
	 * @param Hijo2 the other obtained
	*/
	void HUX(char []Padre1, char []Padre2, char []Hijo1,  char []Hijo2){

		int j, distintos, pos, intercambios;
		char px, py, temp;
	   

		if (flag3==1) {
			posiciones = new int [n_reglas_total];
			flag3 = 0;
	   }

		distintos = 0;
		for (j=0; j < n_reglas_total; j++) {
	   	px=Padre1[j];
	   	py=Padre2[j];
	      Hijo1[j]=px;
	      Hijo2[j]=py;
	      if (px != py) {
	      	posiciones[distintos] = j;
				distintos ++;
	      }
	   }
		
	   intercambios = distintos / 2;
	   
	  
		for (j = 0; j < intercambios; j++) {
			pos = Randomize.Randint (0, --distintos);
	      temp = Hijo1[posiciones[pos]];
	      Hijo1[posiciones[pos]] = Hijo2[posiciones[pos]];
	      Hijo2[posiciones[pos]] = temp;
			posiciones[pos] = posiciones[distintos];
		}
	}

	/**
	 * It converts double to char by the gray's code
	 * @param ds the vector which is goint to change
	 * @param length the size of the vector
	 * @return the changed vector
	 */
		private char [] StringRep(double[] ds,  int length){

		  int i;
		 double n;
		  int pos;
		  double INCREMENTO;
		  char []  Cad_sal;
		  Cad_sal= new char [Genes*BITS_GEN+1];
		  if (flag==1) {
			  tmpstring = new char[Genes*BITS_GEN];
			  flag = 0;
		  }

		  pos = 0;
		  for (i=0; i < length; i++)
		    {
			  INCREMENTO=(Gene[i].max()-Gene[i].min())/(Math.pow(2.0, (double) BITS_GEN) - 1.0);
		      
		      n = (((ds[i] - Gene[i].min()) / INCREMENTO) + 0.5);
		      tmpstring=F.Itoc((int)n, BITS_GEN);
		      
		      F.Gray (tmpstring,Cad_sal,BITS_GEN,pos);
		      pos += BITS_GEN;
		     
		    }
		  return Cad_sal;
		}

		/**
		 * Function which cross the population
		 */ 
		 public	void Cruce(int contador){

		  int i, j,n; 
		  int temp, mom, dad;// cruce;
		  /************************************************************************/
		  /* BARRAJAR POSICIONES DE CROMOSOMAS A CRUZAR */
		  int aux;
		  aux = (Genes*BITS_GEN)+n_reglas_total;
		
		  if (flag2==1) {
			  
		  	  String1 = new char [aux];
		      String2 = new char [aux];
			  flag2 = 0;
			  aux1 = new char[aux];
			  aux2 = new char[aux];	  
		      
		  }
		 
		  //System.out.println("tam "+(Genes*BITS_GEN)+n_reglas_total+" "+aux);
		  for (i=0; i<Popsize; i++) 
			  sample[i]= i;

		  for (i=0; i<Popsize; i++)
		    {
			  
		      j= Randomize.Randint(i,Popsize-1);
		      //System.out.print("j "+j+", ");
			  temp = sample[j];
		      sample[j]=sample[i];
		      sample[i]= temp;
		    }
		  for (i=0; i<2*Popsize; i++) 
			  Poblacion[i].set_entrado(0);

		  /************************************************************************/
		  /*  DISPARAR CRUCE */
		  POPSIZE=Popsize;
		  for (i=0; i<Popsize/2; i++)
		    {
		      mom=sample[2*i]; 
		      dad=sample[2*i+1];
		      /* COMPROBAR DISTANCIA HAMMING ENTRE LOS PADRES */
		      String1=StringRep(Poblacion[mom].Gene(), Genes);
		      String2=StringRep(Poblacion[dad].Gene(), Genes);
		      
		      for(n=0;n<Genes*BITS_GEN;n++){
		    	  aux1[n]=String1[n];
		    	  aux2[n]=String2[n];
		      }
		      int k;
		      for (j = Genes*BITS_GEN, k = 0; j < Genes*BITS_GEN+n_reglas_total; j++,k++) {
		    	 
		    	  aux1[j] = Poblacion[mom].geneR(k);
					aux2[j] = Poblacion[dad].geneR(k);
			  }
		      
		      if (DistHam(aux1, aux2,Genes*BITS_GEN+n_reglas_total)/2.0 > THRESHOLD) {
		    	
		    	 xPC_BLX(1, Poblacion[mom].Gene(),Poblacion[dad].Gene(),Poblacion[POPSIZE].Gene(),Poblacion[POPSIZE+1].Gene(),Genes);
		     
		    	
		    	
		          HUX(Poblacion[mom].GeneR(), Poblacion[dad].GeneR(), Poblacion[POPSIZE].GeneR(), Poblacion[POPSIZE+1].GeneR());

		         
		          Poblacion[POPSIZE].set_entrado(1);
				  Poblacion[POPSIZE+1].set_entrado(1);
				  POPSIZE = POPSIZE+2;
		    	}
		    }   
	}
 /************************************************************/

public int getTrials() {
	return Trials;
}
public void setTrials(int trials) {
	Trials = trials;
}
public double getTHRESHOLD() {
	return THRESHOLD;
}
public void setTHRESHOLD(double threshold) {
	THRESHOLD = threshold;
}

/*public double getTHRESHOLDR() {
	return THRESHOLDR;
}
public void setTHRESHOLDR(double thresholdr) {
	THRESHOLDR = thresholdr;
}
*/public int getGenes() {
	return Genes;
}
public void setGenes(int genes) {
	Genes = genes;
}
public int getPopsize() {
	return Popsize;
}
public void setPopsize(int popsize) {
	Popsize = popsize;
}
public int getPOPSIZE() {
	return POPSIZE;
}
public void setPOPSIZE(int popsize) {
	POPSIZE = popsize;
}
public int getBITS_GEN() {
	return BITS_GEN;
}
public void setBITS_GEN(int bits_gen) {
	BITS_GEN = bits_gen;
}
public Gen[] getGene() {
	return Gene;
}
public void setGene(Gen[] gene) {
	Gene = gene;
}
public Cromosoma[] getPoblacion() {
	return Poblacion;
}
public void setPoblacion(Cromosoma[] poblacion) {
	Poblacion = poblacion;
}
public double getBest() {
	return Best;
}
public void setBest(double best) {
	Best = best;
}
public double getWorst() {
	return Worst;
}
public void setWorst(double worst) {
	Worst = worst;
}
public int[] getSample() {
	return sample;
}
public void setSample(int[] sample) {
	this.sample = sample;
}
public Funciones getF() {
	return F;
}
public void setF(Funciones f) {
	F = f;
}
public Ecm getE() {
	return E;
}
public void setE(Ecm e) {
	E = e;
}
public int getNreglasTotal(){
	return n_reglas_total;
}
public Cromosoma getPoblacion(int i){
	return Poblacion[i];
}
}

