package processing;

import java.util.Arrays;

public class IIRfilter{
	
	private double inputBuffer[]; 
	private double outputBuffer[];
	protected int head = 0;

	private double[] inputCoefficient;
	private double[] outputCoefficient;
	
	
	/**
	 * Constructor, which sets all input and output coefficients to 1/window
	 * @param window - length of coefficient array
	 */
	public IIRfilter(int window) {
		inputBuffer = new double[window];
		outputBuffer = new double[window];
		
		inputCoefficient = new double[window];
		Arrays.fill(inputCoefficient, 1);
			
		outputCoefficient = new double[window];
		Arrays.fill(outputCoefficient, 1);
		outputCoefficient[0] = (window-2)/3;
	}
	
	/**
	 * Constructor.
	 * @param size - filter length
	 * @param inputCoefficient - input coefficient array
	 * @param outputCoefficient - output coefficient array
	 */
	public IIRfilter(int size, double[] inputCoefficient, double[] outputCoefficient) {
		outputBuffer = new double[size];
		this.inputCoefficient = inputCoefficient;
		this.outputCoefficient = outputCoefficient;
		
		inputBuffer = new double[inputCoefficient.length];
		outputBuffer = new double[inputCoefficient.length];
	}
	
	/**
	 * @methods get()
	 * @brief getting filter output
	 * @return filter output value
	 */
	public double get() {
		return outputBuffer[0];
	}
	
	/**
	 * @methods add() 
	 * @brief add value to filter, and calculate output
	 * @param data - input sample
	 * @return out - filter output
	 */
	public double add(double data) {

		double out = 0;
		
		for(int i = inputBuffer.length-1; i>0; i--) {
			inputBuffer[i] = inputBuffer[i-1]; 
			outputBuffer[i] = outputBuffer[i-1];
		}
		
		
		inputBuffer[0] = data;
		
		for (int i = 0; i < inputCoefficient.length; i++) {
			out += inputBuffer[i] * inputCoefficient[i];
		}
		
		for (int i = 1; i < inputCoefficient.length; i++) {
			out -= outputBuffer[i] * outputCoefficient[i];
		}
		
		out /= outputCoefficient[0];
		outputBuffer[0] = out;
		return outputBuffer[0];
		
	}
}
