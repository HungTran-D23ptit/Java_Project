package StudentManagement;

import javax.swing.JTextField;

public class Subject {
    private String name;        // Tên môn học
    private int credits;        // Số tín chỉ
    private float grade;        // Điểm môn học (0-4)
    private transient JTextField gradeField; // Trường JTextField tạm thời để nhập điểm

    // Constructor
    public Subject(String name, int credits) {
        this.name = name;
        this.credits = credits;
        this.grade = 0.0f; // Mặc định điểm là 0 khi tạo môn học
    }

    // Getter và Setter cho tên môn học
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter và Setter cho số tín chỉ
    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    // Getter và Setter cho điểm môn học
    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        if (grade >= 0.0f && grade <= 4.0f) { // Đảm bảo điểm nằm trong khoảng 0 - 4
            this.grade = grade;
        } else {
            throw new IllegalArgumentException("Điểm phải nằm trong khoảng từ 0 đến 4.0!");
        }
    }

    // Getter và Setter cho JTextField nhập điểm
    public JTextField getGradeField() {
        return gradeField;
    }

    public void setGradeField(JTextField gradeField) {
        this.gradeField = gradeField;
    }

    // Phương thức đọc điểm từ JTextField
    public void updateGradeFromField() {
        if (gradeField != null) {
            try {
                float inputGrade = Float.parseFloat(gradeField.getText());
                setGrade(inputGrade); // Gán điểm sau khi kiểm tra hợp lệ
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Dữ liệu nhập vào không hợp lệ! Điểm phải là số thực.");
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%s | Tín chỉ: %d | Điểm: %.2f", name, credits, grade);
    }
}
