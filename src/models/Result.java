
package models;


public class Result {


	private Integer id;


	private float attendance;


	private float assignmentScore;


	private float projectScore;


	private float midsemScore;


	private float examScore;


	private Student students;


	private Course courses;

	public Result() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Result(Integer id) {
		this.id = id;
	}

	public Result(Integer id, float attendance, float assignmentScore, float projectScore, float midsemScore, float examScore) {
		this.id = id;
		this.attendance = attendance;
		this.assignmentScore = assignmentScore;
		this.projectScore = projectScore;
		this.midsemScore = midsemScore;
		this.examScore = examScore;
	}

	public Result(float attendance, float assignmentScore, float projectScore, float midsemScore, float examScore) {
		this.attendance = attendance;
		this.assignmentScore = assignmentScore;
		this.projectScore = projectScore;
		this.midsemScore = midsemScore;
		this.examScore = examScore;
	}

	@Override
	public String toString() {
		return "Result [id=" + id + ", attendance=" + attendance + ", assignmentScore=" + assignmentScore
				+ ", projectScore=" + projectScore + ", midsemScore=" + midsemScore + ", examScore=" + examScore
				+ ", students=" + students + ", courses=" + courses + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public float getAttendance() {
		return attendance;
	}

	public void setAttendance(float attendance) {
		this.attendance = attendance;
	}

	public float getAssignmentScore() {
		return assignmentScore;
	}

	public void setAssignmentScore(float assignmentScore) {
		this.assignmentScore = assignmentScore;
	}

	public float getProjectScore() {
		return projectScore;
	}

	public void setProjectScore(float projectScore) {
		this.projectScore = projectScore;
	}

	public float getMidsemScore() {
		return midsemScore;
	}

	public void setMidsemScore(float midsemScore) {
		this.midsemScore = midsemScore;
	}

	public float getExamScore() {
		return examScore;
	}

	public void setExamScore(float examScore) {
		this.examScore = examScore;
	}

	public Student getStudents() {
		return students;
	}

	public void setStudents(Student students) {
		this.students = students;
	}

	public Course getCourses() {
		return courses;
	}

	public void setCourses(Course courses) {
		this.courses = courses;
	}



}
