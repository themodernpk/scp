package array.list;

public class ad_array_list {
	public int id;
	public String post;
	public String category;
	public String from;
	public String to;
	public String route;
	public String user_name;
	public String user_profession;
	public String desc;
	public String email;
	public String type, gender;

	public ad_array_list(int id, String type, String post, String category, String from, String to,
			String route, String user_name, String user_profession, String desc, String email, String gender) {
		super();
		this.id = id;
		this.post = post;
		this.category = category;
		this.from = from;
		this.to = to;
		this.route = route;
		this.user_name = user_name;
		this.user_profession = user_profession;
		this.desc = desc;
		this.email = email;
		this.type = type;
		this.gender = gender;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_profession() {
		return user_profession;
	}

	public void setUser_profession(String user_profession) {
		this.user_profession = user_profession;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}