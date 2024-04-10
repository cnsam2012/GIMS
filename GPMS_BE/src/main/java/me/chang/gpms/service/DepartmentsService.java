package me.chang.gpms.service;

import lombok.extern.slf4j.Slf4j;
import me.chang.gpms.dao.DepartmentsMapper;
import me.chang.gpms.pojo.Departments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
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
//        return departmentsMapper.insertDepartment(
//                departments
//        );
        int insert = departmentsMapper.insert(departments);
        int departmentsId = departments.getId();
        log.info("DEPARTMENT INSERTED! mapper return={} --- departmentsId={}", insert, departmentsId);
        return departmentsId;
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

    public int selectDepartmentsRows(int userId) {
        return departmentsMapper.selectDepartmentsRows(userId);
    }

    public List<Departments> getAllDepartmentsByFuzzyKeywords(String keywords, int offset, int limit) {
        return departmentsMapper.selectDepartmentsByFuzzyKeywords(keywords, offset, limit);
    }

    public int selectAllDepartmentsByFuzzyKeywordsRows(String keywords) {
        return departmentsMapper.selectDepartmentsByFuzzyKeywordsRows(keywords);
//        return 0;
    }
}
