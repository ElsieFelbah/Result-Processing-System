package models;

import java.util.List;


public class Student {


	private Integer id;


	private String firstName;


	private String lastName;


	private String idNumber;


	private String PIN;

	private String gender;

	private String role;


	private List<Course> courses;


	public Student() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Student(Integer id, String firstName, String lastName, String idNumber, String PIN, String gender, List<Course> courses, String role) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.idNumber = idNumber;
		this.PIN = PIN;
		this.gender = gender;
		this.courses = courses;
		this.role = role;
	}

	public Student(Integer id, String firstName, String lastName, String idNumber, String PIN, String gender) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.idNumber = idNumber;
		this.PIN = PIN;
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "Student{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", idNumber='" + idNumber + '\'' +
				", PIN='" + PIN + '\'' +
				", gender='" + gender + '\'' +
				", courses=" + courses + '\''+
				", role = " + role +
				'}';
	}

	public Student(String firstName, String lastName, String idNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.idNumber = idNumber;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getPIN() {
		return PIN;
	}

	public void setPIN(String PIN) {
		this.PIN = PIN;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
