package array.list;

public class Child {
	private int ad_id;
	private String from;
	private String phone;
	private String email;
	private String date;
	private String message;
	private String response_id, cat_id;

	public Child(String from, String phone, String email, String date, String message, int ad_id, String response_id,
			String cat_id) {
		super();
		this.ad_id = ad_id;
		this.from = from;
		this.phone = phone;
		this.email = email;
		this.date = date;
		this.message = message;
		this.response_id = response_id;
		this.cat_id = cat_id;
	}

	public String getCat_id() {
		return cat_id;
	}

	public void setCat_id(String cat_id) {
		this.cat_id = cat_id;
	}

	public String getFrom() {
		return from;
	}

	public String getResponse_id() {
		return response_id;
	}

	public void setResponse_id(String response_id) {
		this.response_id = response_id;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public int getAd_id() {
		return ad_id;
	}

	public void setAd_id(int ad_id) {
		this.ad_id = ad_id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
