package StudentManagement;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends JFrame {
    private List<Department> departments;
    private JTable studentTable;
    private JComboBox<String> departmentBox;

    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnSearch;
    private JButton btnDetails;
    private JButton btnShow;
    private JButton btnSort;
    private JButton btnStats;
    private JButton btnFilterGPA;
    private JButton btnExport;
    private JButton btnLogout;

    public Main() {
       // Tạo danh sách các khoa
        departments = new ArrayList<>();

        // Khoa Công nghệ thông tin
        Subject subject1 = new Subject("Chủ nghĩa xã hội khoa học", 2);
        Subject subject2 = new Subject("Tiếng Anh (Course 2)", 4);
        Subject subject3 = new Subject("Ngôn ngữ lập trình C++", 3);
        Subject subject4 = new Subject("Toán rời rạc 1", 3);
        Subject subject5 = new Subject("Xử lý tín hiệu số", 2);
        Subject subject6 = new Subject("Xác suất thống kê", 3);

       
        Department cnDepartment = new Department("CN", "Công nghệ thông tin");
        cnDepartment.addDefaultSubject(subject1);
        cnDepartment.addDefaultSubject(subject2);
        cnDepartment.addDefaultSubject(subject3);
        cnDepartment.addDefaultSubject(subject4);
        cnDepartment.addDefaultSubject(subject5);
        cnDepartment.addDefaultSubject(subject6);


        // Khoa Khoa học máy tính
        Subject subject7 = new Subject("Vật lý 3 và thí nghiệm", 4);

        Department khDepartment = new Department("KH", "Khoa học máy tính");
        khDepartment.addDefaultSubject(subject1);
        khDepartment.addDefaultSubject(subject2);
        khDepartment.addDefaultSubject(subject3);
        khDepartment.addDefaultSubject(subject4);
        khDepartment.addDefaultSubject(subject5);
        khDepartment.addDefaultSubject(subject7);

        // Khoa Khoa học máy tính
        Subject subject8 = new Subject("Triết học Mác-Lênin", 3);
        Subject subject9 = new Subject("Lập trình hướng đối tượng", 3);
        Subject subject10 = new Subject("Lập trình web", 3);

        Department ccDepartment = new Department("CC", "Công nghệ thông tin (định hướng ứng dụng)");
        ccDepartment.addDefaultSubject(subject8);
        ccDepartment.addDefaultSubject(subject9);
        ccDepartment.addDefaultSubject(subject10);
        ccDepartment.addDefaultSubject(subject2);
        ccDepartment.addDefaultSubject(subject4);

        // Thêm khoa vào danh sách departments
        departments.add(cnDepartment);
        departments.add(khDepartment);
        departments.add(ccDepartment);

        // Cài đặt giao diện
        setTitle("Quản lý sinh viên theo khoa");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Giao diện chính
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Combo box chọn khoa
        departmentBox = new JComboBox<>();
        for (Department dept : departments) {
            departmentBox.addItem(dept.getName());
        }

        // Bảng hiển thị danh sách sinh viên
        String[] columnNames = {"Mã SV", "Tên SV", "Giới tính", "Tuổi", "GPA", "Xếp loại"};
        DefaultTableModel tableModel = new DefaultTableModel(new Object[0][0], columnNames);
        studentTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(studentTable);

        // Panel chứa các nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnAdd = new JButton("Thêm SV");
        btnEdit = new JButton("Sửa SV");
        btnDelete = new JButton("Xóa SV");
        btnSearch = new JButton("Tìm kiếm SV");
        btnDetails = new JButton("Xem chi tiết");
        btnShow = new JButton("Hiển thị danh sách");
        btnSort = new JButton("Sắp xếp theo điểm");
        btnStats = new JButton("Thống kê");
        btnFilterGPA = new JButton("Lọc theo GPA");
        btnExport = new JButton("Xuất ra CSV");
        btnLogout = new JButton("Đăng xuất");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnDetails);
        buttonPanel.add(btnShow);
        buttonPanel.add(btnSort);
        buttonPanel.add(btnStats);
        buttonPanel.add(btnFilterGPA);
        buttonPanel.add(btnExport);
        buttonPanel.add(btnLogout);

        mainPanel.add(departmentBox, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Sự kiện các nút
        btnShow.addActionListener(e -> showStudentList());
        btnAdd.addActionListener(e -> addStudentDialog());
        btnEdit.addActionListener(e -> editStudentDialog());
        btnDelete.addActionListener(e -> deleteStudentDialog());
        btnSearch.addActionListener(e -> searchStudentDialog());
        btnDetails.addActionListener(e -> viewStudentDetailsDialog());
        btnSort.addActionListener(e -> sortStudentsByGpa());
        btnStats.addActionListener(e -> showStatisticsWithCharts());
        btnFilterGPA.addActionListener(e -> filterByGpa());
        btnExport.addActionListener(e -> exportToCSV());
        btnLogout.addActionListener(e -> logoutAction()); 
    }

    // Hiển thị danh sách sinh viên
    private void showStudentList() {
        int selectedIndex = departmentBox.getSelectedIndex();
        if (selectedIndex >= 0) {
            Department department = departments.get(selectedIndex);
            updateTable(department.getStudents());
        }
    }

    // Thêm sinh viên
    private void addStudentDialog() {
        int selectedIndex = departmentBox.getSelectedIndex();
        if (selectedIndex >= 0) {
            Department department = departments.get(selectedIndex);
    
            JTextField idField = new JTextField();
            JTextField nameField = new JTextField();
            String[] genders = {"Nam", "Nữ"};
            JComboBox<String> genderBox = new JComboBox<>(genders);
            JTextField ageField = new JTextField();
    
            // Lấy danh sách môn học mặc định từ department
            List<Subject> subjects = new ArrayList<>();
            JPanel subjectPanel = new JPanel(new GridLayout(0, 2));
    
            // Tạo giao diện nhập điểm cho các môn học mặc định
            for (Subject subject : department.getDefaultSubjects()) {
                JTextField gradeField = new JTextField();
                subject.setGradeField(gradeField); // Gán trường nhập điểm cho môn học
                subjectPanel.add(new JLabel(subject.getName() + " (Tín chỉ: " + subject.getCredits() + ")"));
                subjectPanel.add(gradeField);
                subjects.add(subject);
            }
    
            Object[] fields = {
                "Mã SV:", idField,
                "Tên SV:", nameField,
                "Giới tính:", genderBox,
                "Tuổi:", ageField,
                "Nhập điểm các môn học:", subjectPanel
            };
    
            int option = JOptionPane.showConfirmDialog(this, fields, "Thêm sinh viên", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String id = idField.getText();
                    String name = nameField.getText();
                    String gender = (String) genderBox.getSelectedItem();
                    int age = Integer.parseInt(ageField.getText());
    
                    if (id.isEmpty() || name.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Mã sinh viên và tên sinh viên không thể để trống!");
                        return;
                    }
    
                    if (age <= 17) {
                        JOptionPane.showMessageDialog(this, "Tuổi không hợp lệ!");
                        return;
                    }
    
                    // Cập nhật điểm vào các môn học
                    for (Subject subject : subjects) {
                        JTextField gradeField = subject.getGradeField();
                        float grade = Float.parseFloat(gradeField.getText());
                        if (grade < 0 || grade > 4) {
                            JOptionPane.showMessageDialog(this, "Điểm phải trong khoảng từ 0 đến 4!");
                            return;
                        }
                        subject.setGrade(grade); // Cập nhật điểm môn học
                    }
    
                // Tạo sinh viên mới với GPA mặc định là 0
                Student student = new Student(id, name, gender, age);

                // Gán danh sách môn học cho sinh viên
                student.setSubjects(subjects);

                // Tính toán GPA và cập nhật lại
                student.setGpa(student.calculateGPA());  // Tính và cập nhật GPA mới
                
                // Thêm sinh viên vào khoa
                String result = department.addStudent(student);
                if ("SUCCESS".equals(result)) {
                    showStudentList(); // Cập nhật lại giao diện với GPA đã tính
                    JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm sinh viên thất bại! " + result);
                }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Tuổi và điểm phải là số hợp lệ!");
                }
            }
        }
    }
     
    // Sửa sinh viên
    private void editStudentDialog() {
        int selectedIndex = departmentBox.getSelectedIndex();
        if (selectedIndex >= 0) {
            Department department = departments.get(selectedIndex);
            String id = JOptionPane.showInputDialog(this, "Nhập mã sinh viên cần sửa:");
    
            if (id != null) {
                Student student = department.getStudents().stream()
                        .filter(s -> s.getId().equals(id))
                        .findFirst()
                        .orElse(null);
    
                if (student != null) {
                    JTextField nameField = new JTextField(student.getName());
                    JTextField ageField = new JTextField(String.valueOf(student.getAge()));
                    JComboBox<String> genderBox = new JComboBox<>(new String[]{"Nam", "Nữ"});
                    genderBox.setSelectedItem(student.getGender());
    
                    // Tạo panel cho điểm môn học
                    JPanel subjectPanel = new JPanel(new GridLayout(0, 2, 10, 10));
                    List<Subject> subjects = student.getSubjects();
                    List<JTextField> gradeFields = new ArrayList<>(); // Danh sách JTextField lưu điểm
    
                    // Hiển thị điểm cũ của các môn học
                    for (Subject subject : subjects) {
                        JLabel subjectLabel = new JLabel(subject.getName() + " (Tín chỉ: " + subject.getCredits() + ")");
                        JTextField gradeField = new JTextField(String.valueOf(subject.getGrade())); // Điền điểm cũ vào
                        gradeFields.add(gradeField);
                        subjectPanel.add(subjectLabel);
                        subjectPanel.add(gradeField);
                    }
    
                    // Tạo giao diện nhập liệu
                    Object[] fields = {
                            "Tên SV:", nameField,
                            "Tuổi:", ageField,
                            "Giới tính:", genderBox,
                            "Chỉnh sửa điểm các môn học:", subjectPanel
                    };
    
                    int option = JOptionPane.showConfirmDialog(this, fields, "Sửa thông tin sinh viên", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        try {
                            student.setName(nameField.getText());
                            student.setAge(Integer.parseInt(ageField.getText()));
                            student.setGender((String) genderBox.getSelectedItem());
    
                            // Cập nhật điểm của các môn học
                            for (int i = 0; i < subjects.size(); i++) {
                                float newGrade = Float.parseFloat(gradeFields.get(i).getText());
                                if (newGrade < 0 || newGrade > 4) {
                                    JOptionPane.showMessageDialog(this, "Điểm của môn học phải từ 0 đến 4!");
                                    return;
                                }
                                subjects.get(i).setGrade(newGrade); // Cập nhật điểm môn học
                            }
    
                            // Tính lại GPA
                            student.setGpa(student.calculateGPA());
    
                            showStudentList(); // Hiển thị lại danh sách sinh viên
                            JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Dữ liệu nhập vào không hợp lệ!");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên!");
                }
            }
        }
    }    

    
    // Xóa sinh viên
    private void deleteStudentDialog() {
        int selectedIndex = departmentBox.getSelectedIndex();
        if (selectedIndex >= 0) {
            Department department = departments.get(selectedIndex);

            String id = JOptionPane.showInputDialog(this, "Nhập mã sinh viên cần xóa:");
            if (id != null) {
                Student student = department.getStudents().stream()
                        .filter(s -> s.getId().equals(id))
                        .findFirst()
                        .orElse(null);

                if (student != null) {
                    // Xóa sinh viên
                    department.getStudents().remove(student);
                    JOptionPane.showMessageDialog(this, "Sinh viên đã được xóa thành công!");
                    showStudentList();  // Cập nhật lại bảng sinh viên
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên!");
                }
            }
        }
    }
    // Tìm kiếm sinh viên
    private void searchStudentDialog() {
        int selectedIndex = departmentBox.getSelectedIndex();
        if (selectedIndex >= 0) {
            Department department = departments.get(selectedIndex);

            String name = JOptionPane.showInputDialog(this, "Nhập tên sinh viên cần tìm:");
            if (name != null) {
                List<Student> foundStudents = department.getStudents().stream()
                        .filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                        .collect(Collectors.toList());

                updateTable(foundStudents);
            }
        }
    }

    // Xem chi tiết sinh viên
    private void viewStudentDetailsDialog() {
        int selectedIndex = departmentBox.getSelectedIndex();
        if (selectedIndex >= 0) {
            Department department = departments.get(selectedIndex);
    
            // Hộp thoại nhập mã sinh viên
            String studentId = JOptionPane.showInputDialog(this, "Nhập mã sinh viên cần xem:");
    
            if (studentId != null && !studentId.isEmpty()) {
                // Tìm kiếm sinh viên theo mã
                Student student = department.getStudents().stream()
                        .filter(s -> s.getId().equals(studentId))
                        .findFirst()
                        .orElse(null);
    
                if (student != null) {
                    // Hiển thị chi tiết sinh viên
                    showStudentDetails(student);
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên với mã: " + studentId);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Mã sinh viên không được để trống!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khoa trước!");
        }
    }

    private void showStudentDetails(Student student) {
        JDialog detailDialog = new JDialog(this, "Thông tin chi tiết sinh viên", true);
        detailDialog.setSize(600, 400);
        detailDialog.setLayout(new BorderLayout());
    
        // Panel hiển thị thông tin chi tiết
        JPanel infoPanel = new JPanel(new GridLayout(0, 2));
        infoPanel.add(new JLabel("Mã sinh viên:"));
        infoPanel.add(new JLabel(student.getId()));
        infoPanel.add(new JLabel("Tên sinh viên:"));
        infoPanel.add(new JLabel(student.getName()));
        infoPanel.add(new JLabel("Giới tính:"));
        infoPanel.add(new JLabel(student.getGender()));
        infoPanel.add(new JLabel("Tuổi:"));
        infoPanel.add(new JLabel(String.valueOf(student.getAge())));
        infoPanel.add(new JLabel("GPA:"));
        infoPanel.add(new JLabel(String.format("%.2f", student.getGpa())));
    
        // Tạo email tự động
        String email = student.generateEmail();
        infoPanel.add(new JLabel("Email:"));
        infoPanel.add(new JLabel(email));
    
        // Đồ thị điểm các môn học
        JPanel chartPanel = createBarChart(student);
    
        // Thêm vào cửa sổ
        detailDialog.add(infoPanel, BorderLayout.NORTH);
        detailDialog.add(chartPanel, BorderLayout.CENTER);
    
        detailDialog.setVisible(true);
    }
    

    private JPanel createBarChart(Student student) {
        // Khởi tạo dataset mới
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
        // Duyệt qua tất cả các môn học của sinh viên và thêm điểm vào dataset
        for (Subject subject : student.getSubjects()) {
            System.out.println("Môn học: " + subject.getName() + ", Điểm: " + subject.getGrade());
            // Thêm điểm của mỗi môn học vào dataset
            dataset.addValue(subject.getGrade(), "Điểm", subject.getName());
        }
    
        // Tạo biểu đồ cột với dataset chứa điểm của các môn học
        JFreeChart barChart = ChartFactory.createBarChart(
                "Điểm các môn học",  // Tiêu đề biểu đồ
                "Môn học",            // Trục X (Tên môn học)
                "Điểm",               // Trục Y (Điểm các môn học)
                dataset,              // Dữ liệu
                PlotOrientation.VERTICAL,
                false,                 // Không cần chú thích
                true,                  // Hiển thị tooltip
                false                  // Không cần URL
        );

        barChart.getCategoryPlot().getRangeAxis().setRange(0, 4);
    
        // Đóng gói biểu đồ trong ChartPanel
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(500, 300));  // Kích thước của biểu đồ
    
        // Trả về panel chứa biểu đồ
        return chartPanel;
    }
         

    // Sắp xếp sinh viên theo GPA
    private void sortStudentsByGpa() {
        int selectedIndex = departmentBox.getSelectedIndex();
        if (selectedIndex >= 0) {
            Department department = departments.get(selectedIndex);
            department.getStudents().sort(Comparator.comparingDouble(Student::getGpa).reversed());
            showStudentList();
        }
    }

    // Thống kê sinh viên với đồ thị
    private void showStatisticsWithCharts() {
        int selectedIndex = departmentBox.getSelectedIndex();
        if (selectedIndex >= 0) {
            Department department = departments.get(selectedIndex);

            // Biểu đồ cột - Số lượng sinh viên theo xếp loại
            JFreeChart barChart = createBarChart(department);

            // Biểu đồ tròn - Tỷ lệ sinh viên theo khoa
            JFreeChart pieChart = createPieChart(department);

            // Tạo cửa sổ mới hiển thị đồ thị
            JFrame chartFrame = new JFrame("Thống kê sinh viên");
            chartFrame.setLayout(new GridLayout(1, 2));
            chartFrame.setSize(1000, 600);
            chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            chartFrame.setLocationRelativeTo(null);

            // Thêm panel chứa biểu đồ vào cửa sổ
            chartFrame.add(new ChartPanel(barChart));
            chartFrame.add(new ChartPanel(pieChart));

            chartFrame.setVisible(true);
        }
    }

    // Tạo biểu đồ cột cho số lượng sinh viên theo xếp loại
    private JFreeChart createBarChart(Department department) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        long excellent = department.getStudents().stream().filter(s -> s.getGpa() >= 3.6).count();
        long good = department.getStudents().stream().filter(s -> s.getGpa() >= 3.2 && s.getGpa() < 3.6).count();
        long rather = department.getStudents().stream().filter(s -> s.getGpa() >= 2.8 && s.getGpa() < 3.2).count();
        long average = department.getStudents().stream().filter(s -> s.getGpa() >= 2.0 && s.getGpa() < 2.8).count();
        long weak = department.getStudents().stream().filter(s -> s.getGpa() < 2.0).count();

        dataset.addValue(excellent, "Xếp loại", "Xuất sắc");
        dataset.addValue(good, "Xếp loại", "Giỏi");
        dataset.addValue(rather, "Xếp loại", "Khá");
        dataset.addValue(average, "Xếp loại", "Trung Bình");
        dataset.addValue(weak, "Xếp loại", "Yếu");

        return ChartFactory.createBarChart(
                "Số lượng sinh viên theo xếp loại", // Tiêu đề biểu đồ
                "Xếp loại", // Nhãn trục X
                "Số lượng", // Nhãn trục Y
                dataset, // Dữ liệu
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true, // Hiển thị chú thích
                true, // Hiển thị tooltip
                false // Không hiển thị URL
        );
    }

    // Tạo biểu đồ tròn cho tỷ lệ sinh viên theo khoa
    private JFreeChart createPieChart(Department department) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        // Thêm tỷ lệ sinh viên của khoa hiện tại vào biểu đồ tròn
        dataset.setValue(department.getName(), department.getStudents().size());
    
        // Thêm tỷ lệ sinh viên của các khoa khác vào biểu đồ tròn
        departments.stream()
                   .filter(dept -> !dept.equals(department)) // Loại trừ khoa hiện tại
                   .forEach(dept -> dataset.setValue(dept.getName(), dept.getStudents().size()));
    
        // Tạo biểu đồ tròn
        return ChartFactory.createPieChart(
                "Tỷ lệ sinh viên theo khoa", // Tiêu đề biểu đồ
                dataset,                     // Dữ liệu
                true,                         // Hiển thị chú thích
                true,                         // Hiển thị tooltip
                false                         // Không hiển thị URL
        );
    }
    
    // Cập nhật bảng sinh viên
    private void updateTable(List<Student> students) {
        DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
        model.setRowCount(0);
        for (Student student : students) {
            model.addRow(new Object[]{
                    student.getId(),
                    student.getName(),
                    student.getGender(), 
                    student.getAge(),
                    student.getGpa(),
                    student.getGrade(),
                    
            });
        }
    }
    

    // Lọc sinh viên theo GPA
    private void filterByGpa() {
        int selectedIndex = departmentBox.getSelectedIndex();
        if (selectedIndex >= 0) {
            Department department = departments.get(selectedIndex);
            String gpaStr = JOptionPane.showInputDialog(this, "Nhập GPA tối thiểu để lọc:");
            if (gpaStr != null) {
                try {
                    double minGpa = Double.parseDouble(gpaStr);
                    List<Student> filteredStudents = department.getStudents().stream()
                            .filter(s -> s.getGpa() >= minGpa)
                            .collect(Collectors.toList());
                    updateTable(filteredStudents);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "GPA không hợp lệ!");
                }
            }
        }
    }


    private List<User> users;

    private void initUsers() {
        users = new ArrayList<>();
        users.add(new User("admin", "admin123", "admin")); // Tài khoản admin
        users.add(new User("user", "user123", "user"));  // Tài khoản user
    }

    // Phương thức đăng nhập
    private User loggedInUser;

    private void showLoginDialog() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        boolean loggedIn = false;  // Biến để kiểm tra nếu đăng nhập thành công

    while (!loggedIn) {  // Vòng lặp cho đến khi người dùng đăng nhập thành công
        Object[] fields = {
            "Tên đăng nhập:", usernameField,
            "Mật khẩu:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Đăng nhập", JOptionPane.OK_CANCEL_OPTION);

        // Kiểm tra nếu người dùng nhấn Cancel hoặc đóng hộp thoại
        if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
            System.exit(0); // Thoát chương trình nếu người dùng nhấn Cancel
        } 

        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Xác thực người dùng
            loggedInUser = users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);

            if (loggedInUser != null) {
                updateInterfaceBasedOnRole(); // Cập nhật giao diện theo vai trò
                loggedIn = true;  // Đánh dấu đăng nhập thành công
            } else {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không đúng!");
                // Không thoát chương trình, tiếp tục yêu cầu người dùng thử lại
            }
        }
    }
}


    private void updateInterfaceBasedOnRole() {
        if (loggedInUser.isAdmin()) {
            JOptionPane.showMessageDialog(this, "Bạn đang đăng nhập với vai trò Admin. Tất cả chức năng được bật.");
        } else {
            JOptionPane.showMessageDialog(this, "Bạn đang đăng nhập với vai trò User. Một số chức năng sẽ bị hạn chế.");
            btnAdd.setEnabled(false);     
            btnEdit.setEnabled(false);   
            btnDelete.setEnabled(false);  
        }
    }

    // Xuất dữ liệu
    private void exportToCSV() {
        int selectedIndex = departmentBox.getSelectedIndex();
        if (selectedIndex >= 0) {
            Department department = departments.get(selectedIndex);
    
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu file CSV");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
    
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                try {
                    // Lấy file do người dùng chọn
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    if (!filePath.endsWith(".csv")) {
                        filePath += ".csv";
                    }
    
                    // Tạo file CSV
                    FileWriter fileWriter = new FileWriter(filePath);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    
                    // Viết tiêu đề cột
                    String[] columnHeaders = {"Mã SV", "Tên SV", "Tuổi", "GPA", "Xếp loại"};
                    bufferedWriter.write(String.join(",", columnHeaders));
                    bufferedWriter.newLine();
    
                    // Ghi dữ liệu từng sinh viên
                    for (Student student : department.getStudents()) {
                        String studentData = String.format("%s,%s,%d,%.2f,%s",
                                student.getId(),
                                student.getName(),
                                student.getAge(),
                                student.getGpa(),
                                student.getGrade());
                        bufferedWriter.write(studentData);
                        bufferedWriter.newLine();
                    }
    
                    // Đóng file
                    bufferedWriter.close();
                    fileWriter.close();
    
                    JOptionPane.showMessageDialog(this, "Xuất dữ liệu thành công!");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xuất file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khoa để xuất dữ liệu!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Phương thức đăng xuất
    private void logoutAction() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose(); // Đóng giao diện chính
            Main mainFrame = new Main(); // Tạo lại giao diện chính
            mainFrame.initUsers(); // Khởi tạo danh sách người dùng
            mainFrame.showLoginDialog(); // Hiển thị form đăng nhập
            mainFrame.setVisible(true); // Hiển thị lại chương trình
        }
    }

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main mainFrame = new Main();
            mainFrame.initUsers(); // Khởi tạo danh sách người dùng
            mainFrame.showLoginDialog(); // Hiển thị form đăng nhập
            mainFrame.setVisible(true);
        });
    }
}
