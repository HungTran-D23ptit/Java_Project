package StudentManagement;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String id;
    private String name;
    private String gender;
    private int age;
    private float gpa;
    private List<Subject> subjects; // Danh sách môn học

    // Constructor không khởi tạo GPA
    public Student(String id, String name, String gender, int age) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.subjects = new ArrayList<>(); // Khởi tạo danh sách môn học
        this.gpa = 0.0f; // Mặc định GPA là 0
    }

    // Getter và Setter
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public float getGpa() {
        return gpa; 
    }
    

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGpa(float gpa) {
        this.gpa = gpa;
    }
    

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    // Thêm môn học vào danh sách
    public void addSubject(Subject subject) {
        this.subjects.add(subject);
    }

    // Tính GPA trung bình từ các môn học
    public float calculateGPA() {
        int totalCredits = 0;
        float weightedSum = 0;

        for (Subject subject : subjects) {
            totalCredits += subject.getCredits();
            weightedSum += subject.getGrade() * subject.getCredits(); // Tính tổng điểm có trọng số
        }

        // Nếu không có môn học, trả về GPA là 0
        return totalCredits == 0 ? 0 : weightedSum / totalCredits;
    }

    // Phương thức trả về xếp loại của sinh viên theo GPA
    public String getGrade() {
        // Tính lại GPA mỗi lần xếp loại
        this.gpa = calculateGPA();

        if (gpa >= 3.6) {
            return "Xuất sắc"; 
        } else if (gpa >= 3.2) {
            return "Giỏi"; 
        } else if (gpa >= 2.8) {
            return "Khá"; 
        } else if (gpa >= 2.0) {
            return "Trung Bình"; 
        } else {
            return "Yếu"; 
        }
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %d | GPA: %.2f", id, name, gender, age, gpa);
    }
}
