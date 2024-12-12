package StudentManagement;

import java.util.ArrayList;
import java.util.List;

public class DepartmentManager {
    private List<Department> departments;

    public DepartmentManager() {
        this.departments = new ArrayList<>();
    }

    // Thêm khoa
    public void addDepartment(Department department) {
        departments.add(department);
    }

    // Lấy danh sách các khoa
    public List<Department> getDepartments() {
        return departments;
    }
    public List<String> getDepartmentIds() {
        return departments.stream()
                .map(Department::getId)
                .toList();
    }
    
}
