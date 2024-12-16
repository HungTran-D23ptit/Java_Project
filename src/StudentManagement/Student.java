package StudentManagement;

import java.util.ArrayList;
import java.util.List;

import java.text.Normalizer;
import java.util.regex.Pattern;

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
    
        // Tính toán tổng điểm có trọng số và tổng số tín chỉ
        for (Subject subject : subjects) {
            totalCredits += subject.getCredits();
            weightedSum += subject.getGrade() * subject.getCredits();  // Tính tổng điểm có trọng số
        }
    
        // Tính GPA và trả về giá trị
        if (totalCredits > 0) {
            return weightedSum / totalCredits;  // Trả về GPA tính toán
        } else {
            return 0.0f;  // Nếu không có môn học, GPA là 0
        }
    }
    

    // Phương thức trả về xếp loại của sinh viên theo GPA
    public String getGrade() {
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

    public static String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
    }

    public String generateEmail() {
        // Chuyển tên thành dạng email
        String[] nameParts = this.name.split(" ");
        String lastName = nameParts[nameParts.length - 1]; // Họ cuối cùng
        StringBuilder initials = new StringBuilder();
        for (int i = 0; i < nameParts.length - 1; i++) {
            initials.append(Character.toUpperCase(nameParts[i].charAt(0))); // Lấy ký tự đầu của các từ đầu
        }
        // Loại bỏ dấu tiếng Việt
        lastName = removeDiacritics(lastName);
        return lastName + initials + "." + this.id + "@stu.ptit.edu.vn";
    }
    
    @Override
    public String toString() {
        return String.format("%s | %s | %s | %d | GPA: %.2f", id, name, gender, age, gpa);
    }
}
