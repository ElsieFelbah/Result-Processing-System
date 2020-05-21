package models;



public class Course {
	

	private Integer id;
	

	private String name;
	

	private String courseCode;
	
	
	
	public Course() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Course(String name, String courseCode) {
		this.name = name;
		this.courseCode = courseCode;
	}


	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", courseCode=" + courseCode + "]";
	}

	public Course(Integer id, String name, String courseCode) {
		this.id = id;
		this.name = name;
		this.courseCode = courseCode;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
}
