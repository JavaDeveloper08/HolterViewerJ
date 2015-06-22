package data;

public class Sample {
	private double signal_sample_;
	private double timestamp_;
	
	public Sample(){
		super();
		signal_sample_ = 0;
		timestamp_ = 0;
	}
	public Sample(int signal_sample, int timestamp) {
		super();
		this.signal_sample_ = signal_sample;
		this.timestamp_ = timestamp;
	}
	
	public double getSignal_sample_() {
		return signal_sample_;
	}

	public void setSignal_sample_(double signal_sample_) {
		this.signal_sample_ = signal_sample_;
	}

	public double getTimestamp_() {
		return timestamp_;
	}

	public void setTimestamp_(double timestamp_) {
		this.timestamp_ = timestamp_;
	}

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
	
	@Override
	public String toString() {
		return Double.toString(timestamp_) + "," + Double.toString(signal_sample_);
	}
}
