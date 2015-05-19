package data;

public class Patient {
	private String name_;
	private String last_name_;
	private Double ID_num_;
	
	public Patient(){
		name_ = null;
		last_name_ = null;
		ID_num_ = 0.0;
	}
	
	public Patient(String name, String last_name, Double ID_num, Boolean sex){
		name_ = name;
		last_name_ = last_name;
		ID_num_ = ID_num;
	}
	
	public String getName_() {
		return name_;
	}

	public void setName_(String name_) {
		this.name_ = name_;
	}
	public String getLast_name_() {
		return last_name_;
	}
	public void setLast_name_(String last_name_) {
		this.last_name_ = last_name_;
	}
	public Double getID_num_() {
		return ID_num_;
	}
	public void setID_num_(Double iD_num_) {
		ID_num_ = iD_num_;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Patient other = (Patient) obj;
		if (ID_num_ == null) {
			if (other.ID_num_ != null)
				return false;
		} else if (!ID_num_.equals(other.ID_num_))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name_ + "," + last_name_ + "," + ID_num_;
	}
	
}
