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

package keel.Algorithms.Neural_Networks.NNEP_Common.mutators.parametric;



import java.util.Hashtable;

import keel.Algorithms.Neural_Networks.NNEP_Common.NeuralNetIndividual;
import keel.Algorithms.Neural_Networks.NNEP_Common.algorithm.NeuralNetAlgorithm;
import keel.Algorithms.Neural_Networks.NNEP_Common.mutators.NeuralNetMutator;
import keel.Algorithms.Neural_Networks.NNEP_Common.neuralnet.ExpNeuron;
import keel.Algorithms.Neural_Networks.NNEP_Common.neuralnet.INeuralNet;
import keel.Algorithms.Neural_Networks.NNEP_Common.neuralnet.LinearNeuron;
import keel.Algorithms.Neural_Networks.NNEP_Common.neuralnet.Link;
import keel.Algorithms.Neural_Networks.NNEP_Common.neuralnet.LinkedLayer;
import keel.Algorithms.Neural_Networks.NNEP_Common.neuralnet.LinkedNeuron;
import keel.Algorithms.Neural_Networks.NNEP_Common.neuralnet.SigmNeuron;
import keel.Algorithms.Neural_Networks.NNEP_Common.problem.ProblemEvaluator;
import net.sf.jclec.IConfigure;
import net.sf.jclec.fitness.SimpleValueFitness;
import net.sf.jclec.util.range.Interval;

import org.apache.commons.configuration.Configuration;


/**  
 * <p>
 * @author Written by Pedro Antonio Gutierrez Penia (University of Cordoba) 16/7/2007
 * @author Written by Aaron Ruiz Mora (University of Cordoba) 16/7/2007
 * @param <I> Type of individuals to mutate
 * @version 0.1
 * @since JDK1.5
 * </p>
 */

public abstract class ParametricMutator<I extends NeuralNetIndividual> extends NeuralNetMutator<I> implements IConfigure
{
	
	/**
	 * <p>
	 * Parametric mutator for neural nets, mutate the weights of the neural nets mutated.
	 * IMPORTANT NOTE: Parametric mutator works directly with  he individuals instead
     *                 of returning a mutated copy of them. This is for performance 
     *                 reasons. If you want to use another mutator you have to consider 
     *                 that individuals will be changed when you use parametric mutation
     * </p>
	 */
	
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////
	
	/** Generated by Eclipse */
	
	private static final long serialVersionUID = 4622620840640398781L;
	
	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////
	
	/** Temperature exponent for the mutations */
	
	protected double temperExponent;
	
	/** Amplitude coefficient for allowed weights */
	
	protected double amplitude;
	
	/** Selective mutator */
	
	protected boolean selective;
	
	/** Initial alpha coeficient for the input weigthts */
	
	protected double initialAlphaInput;
	
	/** Initial alpha coeficient for the output weigthts */
	
	protected double initialAlphaOutput;
	
	/** 
	 * Difference between two fitnesses that we consider
     * enoung to say that the fitness has improved
     */
	
	protected double fitDif;
	
	/** Parametric Mutators of a specific neuron */
	
	protected Hashtable<String,INeuronParametricMutator> neuronParametricMutators = new Hashtable<String,INeuronParametricMutator>();
		
	/////////////////////////////////////////////////////////////////
	// -----------------------------  Variables for the alpha control
	/////////////////////////////////////////////////////////////////
	
	/** Current alpha coeficient for the input weigths */
	
	protected double alphaInput;
	
	/** Current alpha coeficient for the output weigths */
	
	protected double alphaOutput;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------ Auxiliary variables
	/////////////////////////////////////////////////////////////////
	
	/** Temperature of the individual that is being mutated */
	
	double temper;
	
	/////////////////////////////////////////////////////////////////
	// -------------------------------------------------- Constructor
	/////////////////////////////////////////////////////////////////
	
    /**
     * <p>
     * Empty Constructor
     * </p>
     */
    
    public ParametricMutator() {
        super();
    }
    
	/////////////////////////////////////////////////////////////////
	// ------------------------------- Setting and getting Attributes
	/////////////////////////////////////////////////////////////////
    
    /**
     * <p>
	 * Returns the temperature exponent to be used in the mutations
	 * </p>
	 * @return double Temperature exponent
	 */
    
    public double getTemperExponent() {
        return temperExponent;
    }
    
    /**
     * <p>
	 * Sets the temperature exponent to be used in the mutations
	 * </p>
	 * @param temperExponent New temperature exponent
	 */
    
    public void setTemperExponent(double temperExponent) {
        this.temperExponent = temperExponent;
    }
    
    /**
     * <p>
	 * Returns the amplitude coefficient for the weights in mutations
	 * </p>
	 * @return double Amplitude coefficient
	 */
    
    public double getAmplitude() {
        return amplitude;
    }
    
    /**
     * <p>
	 * Sets the amplitude coefficient for the weights in mutations
	 * </p>
	 * @param amplitude New amplitude coefficient
	 */
    
    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }
    
    /**
     * <p>
	 * Returns a boolean indicating if the mutator is selective
	 * </p>
	 * @return true if the mutator is selective (not all the neurons mutated)
	 */
    
    public boolean isSelective() {
        return selective;
    }
    
    /**
     * <p>
	 * Sets a boolean indicating if the mutator is selective
	 * </p>
	 * @param selective New selective boolean value
	 */
    
    public void setSelective(boolean selective) {
        this.selective = selective;
    }
    
    /**
     * <p>
	 * Returns the alpha coeficient for the input weigths
	 * </p>
	 * @return double Alpha coefficient for the input weigths
	 */
    
    public double getAlphaInput() {
        return alphaInput;
    }
    
    /**
     * <p>
	 * Returns the alpha coeficient for the output weigths
	 * </p>
	 * @return double Alpha coefficient for the output weigths
	 */
    
    public double getAlphaOutput() {
        return alphaOutput;
    }
    
    /**
     * <p>
	 * Returns the initial alpha coeficient for the input weigths
	 * </p>
	 * @return double Initial alpha coeficient for the input weigthts
	 */
    
    public double getInitialAlphaInput() {
		return initialAlphaInput;
	}

	/**
	 * <p>
	 * Sets the initial alpha coeficient for the input weigths
	 * </p>
	 * @param initialAlphaInput Initial alpha coeficient for the input weigths
	 */
    
	public void setInitialAlphaInput(double initialAlphaInput) {
		this.initialAlphaInput = initialAlphaInput;
	}

    /**
     * <p>
	 * Returns the initial alpha coeficient for the input weigths
	 * </p>
	 * @return double Initial alpha coeficient for the input weigths
	 */
	
	public double getInitialAlphaOutput() {
		return initialAlphaOutput;
	}

	/**
	 * <p>
	 * Sets the initial alpha coeficient for the output weigths
	 * </p>
	 * @param initialAlphaOutput Initial alpha coeficient for the output weigths
	 */
	
	public void setInitialAlphaOutput(double initialAlphaOutput) {
		this.initialAlphaOutput = initialAlphaOutput;
	}
	
    /**
     * <p>
	 * Returns the difference between two fitnesses that we consider
	 * enough to say that the fitness has improved
	 * </p>
	 * @return double Significative fitness difference
	 */
    
    public double getFitDif() {
        return fitDif;
    }

	/**
	 * <p>
	 * Sets the difference between two fitnesses that we consider
	 * enough to say that the fitness has improved
	 * </p>
	 * @param fitDif New significative fitness difference
	 */
    
    public void setFitDif(double fitDif) {
        this.fitDif = fitDif;
    }
    
	/////////////////////////////////////////////////////////////////
	// -------------------------- Overwriting AbstractMutator methods
	/////////////////////////////////////////////////////////////////
    
	/**
	 * <p>
	 * Prepares the mutation and actualize the alpha value
	 * </p>
	 */
    
    @Override
	protected void prepareMutation(){
		super.prepareMutation();
		
		alphaUpdate(((NeuralNetAlgorithm<I>) context).getCurrentBest());
	}
	
	/**
	 * <p>
	 * Mutates the next individual
	 * </p>
	 */
	
	@SuppressWarnings("unchecked")
	@Override    
    public void mutateNext() {
		
		//Copy individual genotype
        I lastNnind = parentsBuffer.get(parentsCounter);
        I result = (I) lastNnind.copy();
        INeuralNet neuralNet = result.getGenotype();
        
        double fitness = ((SimpleValueFitness) result.getFitness()).getValue();
        temper = Math.pow(1-fitness, temperExponent);
        
        //System.out.println("-------------------\nAntes --> " + result);

		//Select a random layer to make the mutation
		int noflayer;
		do{
			noflayer = randgen.choose(0, neuralNet.getNofhlayers());
		}while(neuralNet.getHlayer(noflayer).neuronsEmpty());
		
		//Number of neurons to mutate
        int nofneurons = neuralNet.getHlayer(noflayer).getNofneurons();
        
	    // Obtain next layer to know if it is biased
	    LinkedLayer nextLayer;
	    if(noflayer == neuralNet.getNofhlayers()-1)
	        nextLayer = neuralNet.getOutputLayer();
	    else
	        nextLayer = neuralNet.getHlayer(noflayer+1);
        
        // If nextLayer is biased in this layer an additional neuron is considered
	    if(nextLayer.isBiased())
	    	nofneurons++;
	    
        if(selective)
            nofneurons = (int)(1+ (randgen.raw()*temper*nofneurons));

        //For each neuron
        for(int i = 0; i<nofneurons; i++) {
        	
		    //Selected layer
		    LinkedLayer layer = neuralNet.getHlayer(noflayer);
			
		    // Obtain next layer
		    if(noflayer == neuralNet.getNofhlayers()-1)
		        nextLayer = neuralNet.getOutputLayer();
		    else
		        nextLayer = neuralNet.getHlayer(noflayer+1);
		    
        	// Number of neuron selected to be mutated
            int selectedNeuron;
	        if(selective) {
	        	if(nextLayer.isBiased())
	        		selectedNeuron = randgen.choose(0, layer.getNofneurons()+1);
	        	else
	        		selectedNeuron = randgen.choose(0, layer.getNofneurons());
	        }
	        else
	            selectedNeuron = i;
		    
	        // If selected neuron is not the bias neuron
	        if(selectedNeuron != layer.getNofneurons()) {
	        	// Selected neuron
	        	LinkedNeuron neuron = layer.getNeuron(selectedNeuron);
	        
	        	// Obtain the parametric mutation of the specific neuron
	        	INeuronParametricMutator neuronParametricMutator = neuronParametricMutators.get( neuron.getClass().getCanonicalName() );
	        	
	        	// If specific mutator is null add the neuron parametric mutation
	        	if(neuronParametricMutator==null)
	        		neuronParametricMutator = addNeuronParametricMutator(neuron);
	        
	        	// Performe the parametric mutation of the specific neuron
	        	if(neuronParametricMutator!=null)
	        		neuronParametricMutator.parametricMutation(neuron, layer, nextLayer, selectedNeuron, alphaInput, alphaOutput, temper);
	        }
	        else 
	        	mutateBias(nextLayer);
	        
			//Evaluate the individual
			((ProblemEvaluator<I>)context.getEvaluator()).evaluate(result);

	        //System.out.println("Cambio a --> " + result);
			double newFitness = ((SimpleValueFitness) result.getFitness()).getValue();
			
			//System.out.println( " New Fitness " + newFitness + " Fitness " + fitness);
			
			alphaControlParametersUpdate(newFitness, fitness);
			if(newFitness<fitness){
			    double fitnessIncrement = newFitness - fitness;
			    double acceptValue = Math.exp(fitnessIncrement/temper);
			    
			    //Non accepted change
			    if(acceptValue >= randgen.raw()){
			        //System.out.println("Cambio no aceptado");
			        result = lastNnind;
			        neuralNet = result.getGenotype();
					newFitness = fitness;
			    }
			}
			
			lastNnind = (I) result.copy();
			fitness = newFitness;
			temper = Math.pow(1-fitness, temperExponent);
		}

        result.getGenotype().keepRelevantLinks(fitDif);
        //System.out.println("Despues --> " + result);
        sonsBuffer.add(result);
    }
    
	/////////////////////////////////////////////////////////////////
	// -------------------------------------- Public abstract methods
	/////////////////////////////////////////////////////////////////
	
	/**
	 * <p>
	 * Init the values of alpha parameters used in the mutations
	 * </p>
	 */
    
    public abstract void alphaInit();
    
    
    
	/////////////////////////////////////////////////////////////////
	// ----------------------------------- Protected abstract methods
	/////////////////////////////////////////////////////////////////
    
	/**
	 * <p>
	 * Updates the values of alpha parameters at the beginning
	 * of a generation
	 * </p>
	 * @param bestFitness Best fitness of this generation
	 */
    
    protected abstract void alphaUpdate(double bestFitness);
    
	/**
	 * <p>
	 * Updates alpha control parameters at the end of each
	 * neuron mutation, if neccesary
	 * </p>
	 * @param newFitness Result fitness of the mutation
	 * @param fitness Previous fitness befor making the mutation
	 */
    
    protected abstract void alphaControlParametersUpdate(double newFitness, double fitness);
    
    
	/////////////////////////////////////////////////////////////////
	// ---------------------------------------------- Private methods
	/////////////////////////////////////////////////////////////////
    
	/**
	 * <p>
	 * Adds neuron parametric mutator obtaining the instant 
	 * according to the type of the neuron
	 * </p>
	 * @param neuron Neuron that is going to be mutated, needed to know the type of neuron
	 * 
	 * @return INeuronParametricMutator Specific neuron parametric mutator
	 */
    
	private INeuronParametricMutator addNeuronParametricMutator(LinkedNeuron neuron) {
		
		// If it is a Sigmoidal neuron
		if(neuron instanceof SigmNeuron) {
			SigmNeuronParametricMutator neuronParametricMutator = new SigmNeuronParametricMutator();
			// Set the random generator and the amplitude value
			neuronParametricMutator.setRandgen(randgen);
			neuronParametricMutator.setAmplitude(amplitude);
			neuronParametricMutators.put(neuron.getClass().getCanonicalName(), neuronParametricMutator);
			return neuronParametricMutator;
		}
		// If it is a Linear neuron
		else if(neuron instanceof LinearNeuron) {
			LinearNeuronParametricMutator neuronParametricMutator = new LinearNeuronParametricMutator();
			// Set the random generator and the amplitude value
			neuronParametricMutator.setRandgen(randgen);
			neuronParametricMutator.setAmplitude(amplitude);
			neuronParametricMutators.put(neuron.getClass().getCanonicalName(), neuronParametricMutator);
			return neuronParametricMutator;
		}
		// If it is a Exponential neuron
		else if(neuron instanceof ExpNeuron) {
			ExpNeuronParametricMutator neuronParametricMutator = new ExpNeuronParametricMutator();
			// Set the random generator and the amplitude value
			neuronParametricMutator.setRandgen(randgen);
			neuronParametricMutator.setAmplitude(amplitude);
			neuronParametricMutators.put(neuron.getClass().getCanonicalName(), neuronParametricMutator);
			return neuronParametricMutator;
		}
		
		return null;
	}
    
	/**
	 * <p>
	 * Do the parametric mutation over the links that represent the bias
	 * in the next layer
	 * </p>
	 * @param nextLayer Next layer
	 */

    private void mutateBias(LinkedLayer nextLayer) {
        //For each output link
	    for(int i=0; i<nextLayer.getNofneurons(); i++){
	        
	        //Obtain a neuron
	    	LinkedNeuron linkedNeuron = nextLayer.getNeuron(i);
	    	Link [] links = linkedNeuron.getLinks();
	    	
	    	//Weight increment
		    double weigthIncrement = randgen.gaussian(alphaOutput*temper);
		    
			//Apply the mutation
	    	links[links.length-1].setWeight(links[links.length-1].getWeight()+weigthIncrement);
	    	
            //Control the weigth value
            if(!linkedNeuron.getWeightRange().contains(links[links.length-1].getWeight())){
            	Interval interval = linkedNeuron.getWeightRange();
            	if(links[links.length-1].getWeight() > (interval.getRight()*amplitude))
            		links[links.length-1].setWeight(interval.getRight()*amplitude);
            	if(links[links.length-1].getWeight() < (interval.getLeft()*amplitude))
            		links[links.length-1].setWeight(interval.getLeft()*amplitude);
            }
	    }
    }
    
	/////////////////////////////////////////////////////////////////
	// ---------------------------- Implementing IConfigure interface
	/////////////////////////////////////////////////////////////////
	
	/**
	 * <p>
	 * Configuration parameters for ParametricMutator are:
	 * </p>
	 * <ul>
	 * <li>
	 * <code>[@selective] boolean (default=false)</code></p>
	 * If this parameter is set to <code>true</true> only certain randomly
	 * selected nodes are parametrically mutated.
	 * </li>
	 * <li>
	 * <code>temperature-exponent[@value] double (default=1)</code></p>
	 * Temperature exponent to be used for obtaining temperature
	 * of each indivual mutated.
	 * </li>
	 * <li>
	 * <code>amplitude[@value] double (default=5)</code></p>
	 * Amplitude factor to increase the range of parametric variations
	 * of mutated weights.
	 * </li>
	 * <li>
	 * <code>fitness-difference[@value] double (default=0.0000001)</code></p>
	 * Difference between two fitnesses that we consider
     * enoung to say that the fitness has improved
	 * </li>
	 * <li>
	 * <code>initial-alpha-values: complex</code></p> 
	 * Initial values of alpha parameters.
	 * <ul>
	 * 		<li>
	 * 		<code>initial-alpha-values[@input] double (default=0.5)</code></p>
	 * 		Initial value of alpha parameter used for input weights.
	 * 		</li>
	 * 		<li>
	 * 		<code>initial-alpha-values[@ouput] double (default=1)</code></p>
	 * 		Initial value of alpha parameter used for output weights.
	 * 		</li>
	 * </ul> 
	 * </li>
	 * </ul>
	 */
	
	public void configure(Configuration settings)
	{
		// Setup selective
		selective = settings.getBoolean("[@selective]", false);
		
		// Setup temperExponent
		temperExponent = settings.getDouble("temperature-exponent[@value]", 1);
		
		// Setup amplitude
		amplitude = settings.getDouble("amplitude[@value]", 5);
		
		// Setup fitDif
		fitDif = settings.getDouble("fitness-difference[@value]", 0.0000001);
		
		// Setup alphaInput
		initialAlphaInput = settings.getDouble("initial-alpha-values[@input]", 0.5);
		
		// Setup alphaOutput
		initialAlphaOutput = settings.getDouble("initial-alpha-values[@output]", 1);		
		
	}
	
}
