package array.list;

import java.util.ArrayList;

public class Header {

	private String date;
	
	private ArrayList<ad_array_list> Items;
	
	public Header(String date, ArrayList<ad_array_list> Items){
		this.date = date;
//		this.year = year;
		this.Items = Items;
	}
	
	public String getdate() {
		return date;
	}
	public void setdate(String date) {
		this.date = date;
	}
	
	public ArrayList<ad_array_list> getItems() {
		return Items;
	}
	public void setItems(ArrayList<ad_array_list> items) {
		Items = items;
	}
	
	
	
}
