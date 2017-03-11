package array.list;

import java.util.ArrayList;

public class My_Message_Group {

	private String name, email, phone, response_id, ad_id, msg, date, cat_id;
	ArrayList<MyMessagesChild> child;

	public My_Message_Group(String name, String phone, String email, String ad_id, String response_id, String message,
			String date, String cat_id, ArrayList<MyMessagesChild> ch_list1) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.ad_id = ad_id;
		this.response_id = response_id;
		this.child = ch_list1;
		this.msg = message;
		this.date = date;
		this.cat_id = cat_id;
	}

	public String getCat_id() {
		return cat_id;
	}

	public void setCat_id(String cat_id) {
		this.cat_id = cat_id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getResponse_id() {
		return response_id;
	}

	public void setResponse_id(String response_id) {
		this.response_id = response_id;
	}

	public String getAd_id() {
		return ad_id;
	}

	public void setAd_id(String ad_id) {
		this.ad_id = ad_id;
	}

	public ArrayList<MyMessagesChild> getChild() {
		return child;
	}

	public void setChild(ArrayList<MyMessagesChild> child) {
		this.child = child;
	}

}
