package awesome.app.data;

public class StudentData {

	private static final String UNKNOWN_VALUE = "Unknown";
	private String username;
	private String name;
	private String status;
	private String cm;
	private String room;
	private String phone;
	private String department;

	public StudentData() {
		username = UNKNOWN_VALUE;
		name = UNKNOWN_VALUE;
		status = UNKNOWN_VALUE;
		cm = UNKNOWN_VALUE;
		room = UNKNOWN_VALUE;
		phone = UNKNOWN_VALUE;
		department = UNKNOWN_VALUE;
	}

	public String getEntitledUsername() {
		return "Username: " + username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEntitledName() {
		return "Full Name: " + name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEntitledStatus() {
		return "Status: " + status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEntitledCm() {
		return "CM " + cm;
	}

	public void setCm(String cm) {
		this.cm = cm;
	}

	public String getEntitledRoom() {
		return "Room: " + room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getEntitledPhone() {
		return "Phone: " + phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEntitledDepartment() {
		return "Department: " + department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}

}
