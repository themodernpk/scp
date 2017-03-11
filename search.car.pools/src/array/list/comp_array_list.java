package array.list;

public class comp_array_list {
	public int company_id;
	public String company_name;
	public String company_add;
	public comp_array_list(int company_id,String company_name,String company_add) {
		  super();
		 
		  this.company_id=company_id;
		  this.company_name=company_name;
		  this.company_add=company_add;
		 		  	 }
	public int getCompany_id() {
		return company_id;
	}
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getCompany_add() {
		return company_add;
	}
	public void setCompany_add(String company_add) {
		this.company_add = company_add;
	}
}
