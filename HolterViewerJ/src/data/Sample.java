package data;

/**
 * @class Sample
 * @brief class representing signal sample object contains signal value and timestamp value
 */
public class Sample {
	private double signal_sample_;
	private int timestamp_;
	
	public Sample(){
		signal_sample_ = 0;
		timestamp_ = 0;
	}
	
	public Sample(double signal_sample, int timestamp) {
		this.signal_sample_ = signal_sample;
		this.timestamp_ = timestamp;
	}
	
	/**
	 * Getters and setters to class fields
	 */
	public double getSignal_sample_() {
		return signal_sample_;
	}

	public void setSignal_sample_(double signal_sample_) {
		this.signal_sample_ = signal_sample_;
	}

	public int getTimestamp_() {
		return timestamp_;
	}

	public void setTimestamp_(int timestamp_) {
		this.timestamp_ = timestamp_;
	}

	/**
	 * @methods equals()
	 * @brief method for compare sample
	 * @param general object
	 * @return true if equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sample other = (Sample) obj;
		if (signal_sample_ != other.signal_sample_)
			return false;
		if (timestamp_ != other.timestamp_)
			return false;
		return true;
	}
	
	/**
	 * @methods toString()
	 * @brief method return object representation in string
	 * @return string representation of object
	 */
	@Override
	public String toString() {
		return Integer.toString(timestamp_) + "," + Double.toString(signal_sample_);
	}
}
