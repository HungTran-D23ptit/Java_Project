package StudentManagement;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private String id;
    private String name;
    private List<Student> students;
    private List<Subject> defaultSubjects; // Danh sách môn học mặc định

    public Department(String id, String name) {
        this.id = id;
        this.name = name;
        this.students = new ArrayList<>();
        this.defaultSubjects = new ArrayList<>();
    }


    // Getter cho mã khoa
    public String getId() {
        return id;
    }

    // Getter cho tên khoa
    public String getName() {
        return name;
    }

    // Getter cho danh sách sinh viên
    public List<Student> getStudents() {
        return students;
    }

    // Thêm môn học mặc định
    public void addDefaultSubject(Subject subject) {
        defaultSubjects.add(subject);
    }

    // Lấy danh sách môn học mặc định
    public List<Subject> getDefaultSubjects() {
        return defaultSubjects;
    }

    // Kiểm tra định dạng mã sinh viên
    public boolean isValidStudentId(String studentId) {
        return studentId.matches("^" + this.id + "\\d+$");
    }

   // Trong lớp addStudent:
    public String addStudent(Student student) {
        if (!isValidStudentId(student.getId())) {
            return "Mã sinh viên không hợp lệ! Mã SV phải bắt đầu bằng: " + id;
        }

        for (Student s : students) {
            if (s.getId().equals(student.getId())) {
                return "Mã sinh viên đã tồn tại!";
            }
        }

        // Gán danh sách môn học mặc định cho sinh viên
        List<Subject> subjects = new ArrayList<>();
        for (Subject defaultSubject : defaultSubjects) {
            Subject subject = new Subject(defaultSubject.getName(), defaultSubject.getCredits());
            subjects.add(subject); // Điểm mặc định là 0
        }

        student.setSubjects(subjects);
        students.add(student);
        return "SUCCESS";
    }

}    
