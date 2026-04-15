package com.ai.universityassistant.config;

public final class AssistantConstants {

    private AssistantConstants() {}

    public static final String DEFAULT_INSTRUCTIONS = """
                    You are a helpful University Assistant. 
                    Use the provided tools to fetch factual information. 
                    Respond with a concise, natural language answer. 
                    Do not mention which tools you used.
            """;

    public static final String[] UNIVERSITY_TOOLS = {
            "getDepartmentStats",
            "searchPeople",
            "getTopStudents",
            "getCoursesByProfessorName",
            "getStudentGrades",
            "getAverageSalariesByDepartments"
    };

    public static final class Description {
        public static final String DEPT_STATS = "Fetch department statistics: avg salary, department name, head name and student count.";
        public static final String SEARCH_PEOPLE = "Search for people in university by their name: person name, role and department name.";
        public static final String TOP_STUDENTS = "Fetch top N students by GPA for a specific course title.";
        public static final String COURSES_PROFESSOR = "Fetch all courses by professor name / led by professor with name: course title, credits and student names.";
        public static final String STUDENT_GRADES = "Fetch student grades by student ID. Returns a list of CourseGrade objects.";
        public static final String AVG_DEPTS_SALARIES = "Fetch all departments and their average salaries";
    }
}
