package array.list;

public class MyMessagesChild {
	
	
	private String date;
	private String message;
	private String response_id, name;
	
	public MyMessagesChild(String date, String msg, String response_id, String name) {
		// TODO Auto-generated constructor stub
		this.message = msg;
		this.date = date;
		this.response_id = response_id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getResponse_id() {
		return response_id;
	}

	public void setResponse_id(String response_id) {
		this.response_id = response_id;
	}
	
	
	
}
