package array.list;

import java.util.ArrayList;

public class Group {

	private String Name;
	private String date;
	private ArrayList<Child> Items;

	public Group(String Name, String date, ArrayList<Child> Items) {
		this.Items = Items;
		this.Name = Name;
		this.date = date;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		this.Name = name;
	}

	public ArrayList<Child> getItems() {
		return Items;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setItems(ArrayList<Child> Items) {
		this.Items = Items;
	}

}
