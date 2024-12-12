package StudentManagement;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
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
    private JButton btnShow;
    private JButton btnSort;
    private JButton btnStats;
    private JButton btnFilterGPA;
    private JButton btnExport;
    private JButton btnLogout;

    public Main() {
        // Tạo danh sách các khoa
        departments = new ArrayList<>();
        departments.add(new Department("CN", "Công nghệ thông tin"));
        departments.add(new Department("KH", "Khoa học máy tính"));
        departments.add(new Department("KT", "Kế toán"));
        departments.add(new Department("QTKD", "Quản trị kinh doanh"));
        departments.add(new Department("CC", "Công nghệ thông tin - Định hướng ứng dụng"));
        departments.add(new Department("AT", "An toàn thông tin"));
        departments.add(new Department("TDH", "Tự động hóa"));
        departments.add(new Department("VT", "Điện tử viễn thông"));
        departments.add(new Department("DPG", "Thiết kế và phát triển game"));
        departments.add(new Department("DPT", "Công nghệ đa phương tiện"));
        departments.add(new Department("FT", "Công nghệ tài chính"));
        departments.add(new Department("MR", "Marketing"));

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
        btnAdd = new JButton("Thêm sinh viên");
        btnEdit = new JButton("Sửa sinh viên");
        btnDelete = new JButton("Xóa sinh viên");
        btnSearch = new JButton("Tìm kiếm sinh viên");
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
            JTextField gpaField = new JTextField();
    
    
            Object[] fields = {
                    "Mã SV:", idField,
                    "Tên SV:", nameField,
                    "Giới tính:", genderBox,
                    "Tuổi:", ageField,
                    "GPA:", gpaField,
            };
    
            int option = JOptionPane.showConfirmDialog(this, fields, "Thêm sinh viên", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String id = idField.getText();
                    String name = nameField.getText();
                    String gender = (String) genderBox.getSelectedItem();
                    int age = Integer.parseInt(ageField.getText());  // Kiểm tra tuổi là số nguyên
                    float gpa = Float.parseFloat(gpaField.getText());  // Kiểm tra GPA là số thực
                   
    
                    // Kiểm tra giá trị GPA hợp lệ
                    if (age <= 17) {
                        JOptionPane.showMessageDialog(this, "Tuổi không hợp lệ!");
                        return;
                    }

                    if (gpa < 0 || gpa > 4.0) {
                        JOptionPane.showMessageDialog(this, "Điểm GPA phải nằm trong khoảng từ 0 đến 4.0!");
                        return;
                    }
    
                    // Kiểm tra các trường đầu vào
                    if (id.isEmpty() || name.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Mã sinh viên và tên sinh viên không thể để trống!");
                        return;
                    }
    
                    // Tạo đối tượng sinh viên và thêm vào khoa
                    Student student = new Student(id, name, gender, age, gpa); 
                    if (department.addStudent(student)) {
                        showStudentList();  // Hiển thị lại danh sách sinh viên
                    } else {
                        JOptionPane.showMessageDialog(this, "Thêm sinh viên thất bại! Vui lòng kiểm tra mã SV.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ! Hãy chắc chắn rằng tuổi và GPA là số.");
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
                    JTextField gpaField = new JTextField(String.valueOf(student.getGpa()));
    
                    // Thêm JComboBox cho giới tính
                    String[] genders = {"Nam", "Nữ"};
                    JComboBox<String> genderBox = new JComboBox<>(genders);
                    genderBox.setSelectedItem(student.getGender()); // Chọn giới tính hiện tại của sinh viên
    
                    Object[] fields = {
                            "Tên SV:", nameField,
                            "Tuổi:", ageField,
                            "GPA:", gpaField,
                            "Giới tính:", genderBox
                    };
    
                    int option = JOptionPane.showConfirmDialog(this, fields, "Sửa sinh viên", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        try {
                            student.setName(nameField.getText());
                            student.setAge(Integer.parseInt(ageField.getText()));
                            student.setGpa(Float.parseFloat(gpaField.getText()));
                            student.setGender((String) genderBox.getSelectedItem()); // Cập nhật giới tính
    
                            showStudentList();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
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
