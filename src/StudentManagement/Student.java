package StudentManagement;

public class Student {
    private String id;
    private String name;
    private String gender;
    private int age;
    private float gpa;

    public Student(String id, String name, String gender, int age, float gpa) {
        this.id = id;
        this.name = name;
        this.gender = gender; 
        this.age = age;
        this.gpa = gpa;
    }

    // Getter và Setter cho các thuộc tính
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

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %d | %.2f", id, name, gender, age, gpa);
    }
}
