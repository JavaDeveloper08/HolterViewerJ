package data;

/**
 * @class Patient
 * @brief class representing patient object contains personal information (name, surname, ID number)
 */
public class Patient {
	private String name_;
	private String last_name_;
	private String ID_num_;
	
	public Patient(){
		name_ = null;
		last_name_ = null;
		ID_num_ = null;
	}
	
	public Patient(String name, String last_name, String ID_num, Boolean sex){
		name_ = name;
		last_name_ = last_name;
		ID_num_ = ID_num;
	}
	
	/**
	 * Getters and setters to class fields
	 */
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
	public String getID_num_() {
		return ID_num_;
	}
	public void setID_num_(String iD_num_) {
		ID_num_ = iD_num_;
	}
	
	/**
	 * @methods equals()
	 * @brief method for compare patient object by ID number
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
		Patient other = (Patient) obj;
		if (ID_num_ == null) {
			if (other.ID_num_ != null)
				return false;
		} else if (!ID_num_.equals(other.ID_num_))
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
		return name_ + "," + last_name_ + "," + ID_num_;
	}
}
