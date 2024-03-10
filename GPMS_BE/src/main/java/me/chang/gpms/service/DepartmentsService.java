package me.chang.gpms.service;

import me.chang.gpms.dao.DepartmentsMapper;
import me.chang.gpms.pojo.Departments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentsService {
    private DepartmentsMapper departmentsMapper;

    @Autowired
    public DepartmentsService(DepartmentsMapper departmentsMapper) {
        this.departmentsMapper = departmentsMapper;
    }

    public Departments getDepartmentById(Integer id) {
        return departmentsMapper.selectById(id);
    }

    public Departments getDepartmentByName(String name) {
        return departmentsMapper.selectByName(name);
    }

    public int insertDepartment(Departments departments) {
        return departmentsMapper.insertDepartment(
                departments
        );
    }

    public int updateType(Departments departments, int newType) {
        return departmentsMapper.updateType(
                departments.getId(),
                newType
        );
    }

    public int updateContent(Departments departments, String newContent) {
        return departmentsMapper.updateContent(
                departments.getId(),
                newContent
        );
    }

    public List<Departments> getAllDepartments(int offset, int limit) {
        return departmentsMapper.selectDepartments(offset, limit);
    }

    public int deleteDepartmentsById(int id) {
        return departmentsMapper.deleteDepartmentsById(id);
    }

    public boolean updateDepartment(int departmentId, Departments departments) {
        return departmentsMapper.updateDepartments(departmentId, departments);
    }
}
