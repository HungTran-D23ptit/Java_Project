package StudentManagement;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private String id; 
    private String name; 
    private List<Student> students; 

    public Department(String id, String name) {
        this.id = id;
        this.name = name;
        this.students = new ArrayList<>();
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

    // Kiểm tra định dạng mã sinh viên
    public boolean isValidStudentId(String studentId) {
        // Mã SV phải bắt đầu bằng mã khoa (CN, KT, ...) và theo sau là số
        return studentId.matches("^" + this.id + "\\d+$");
    }

    // Phương thức thêm sinh viên
    public boolean addStudent(Student student) {
        if (!isValidStudentId(student.getId())) {
            System.out.println("Mã sinh viên không hợp lệ! Mã SV phải bắt đầu bằng: " + id);
            return false;
        }

        for (Student s : students) {
            if (s.getId().equals(student.getId())) {
                System.out.println("Mã sinh viên đã tồn tại!");
                return false;
            }
        }
        students.add(student);
        return true;
    }

}
