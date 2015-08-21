package data;

/**
 * @class Time
 * @brief class representing time object contains: year, month, day, hour, minute, second, milisecond
 */
public class Time {
	private int msecond_;
	private int second_;
	private int minute_;
	private int hour_;
	private int day_;
	private int month_;
	private int year_;
	
	public Time(){
		msecond_ = 0;
		second_ = 0;
		minute_ = 0;
		hour_ = 0;
		day_ = 1;
		month_ = 1;
		year_ = 1970;
	}

	public Time(int ms, int s, int min, int h, int d, int m, int y){
		msecond_ = ms;
		second_ = s;
		minute_ = min;
		hour_ = h;
		day_ = d;
		month_ = m;
		year_ = y;
	}

	/**
	 * Getters and setters to class fields
	 */
	public int getMsecond_() {
		return msecond_;
	}

	public void setMsecond_(int msecond_) {
		this.msecond_ = msecond_;
	}

	public int getSecond_() {
		return second_;
	}

	public void setSecond_(int second_) {
		this.second_ = second_;
	}

	public int getMinute_() {
		return minute_;
	}

	public void setMinute_(int minute_) {
		this.minute_ = minute_;
	}

	public int getHour_() {
		return hour_;
	}

	public void setHour_(int hour_) {
		this.hour_ = hour_;
	}

	public int getDay_() {
		return day_;
	}

	public void setDay_(int day_) {
		this.day_ = day_;
	}

	public int getMonth_() {
		return month_;
	}

	public void setMonth_(int month_) {
		this.month_ = month_;
	}

	public int getYear_() {
		return year_;
	}

	public void setYear_(int year_) {
		this.year_ = year_;
	}

	/**
	 * @methods equals()
	 * @brief method for compare time
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
		Time other = (Time) obj;
		if (day_ != other.day_)
			return false;
		if (hour_ != other.hour_)
			return false;
		if (minute_ != other.minute_)
			return false;
		if (month_ != other.month_)
			return false;
		if (msecond_ != other.msecond_)
			return false;
		if (second_ != other.second_)
			return false;
		if (year_ != other.year_)
			return false;
		return true;
	}
	
	/**
	 * @methods getTime()
	 * @brief getting time(hour, minute, second) in string representation 
	 * @return time in string
	 */
	public String getTime() {
		return hour_ + ":" + minute_ + ":" + second_;
	}
	
	/**
	 * @methods getDate()
	 * @brief getting date(year, month, day) in string representation 
	 * @return date in string
	 */
	public String getDate() {
		return year_ + "/" + month_ + "/" + day_;
	}
	
	/**
	 * @methods getFullTime()
	 * @brief getting full time object(year, month, day, hours, minutes, seconds) in string representation 
	 * @return full time in string
	 */
	public String getFullTime() {
		return year_ + "/" + month_ + "/" + day_ + " " + hour_ + ":" + minute_ + ":" + second_;
	}

	/**
	 * @methods toString()
	 * @brief method return object representation in string
	 * @return string representation of object
	 */
	@Override
	public String toString() {
		return year_ + "," + month_ + "," + day_ + "," + hour_ + "," + minute_;
	}
}
