package me.chang.gpms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.chang.gpms.pojo.Departments;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DepartmentsMapper extends BaseMapper<Departments> {

    List<Departments> selectDepartments(int offset, int limit);

    Departments selectById(Integer id);

    Departments selectByName(String name);

    int insertDepartment(Departments departments);

    int updateType(Integer id, Integer type);

    int updateContent(Integer id, String content);

    int deleteDepartmentsById(Integer id);

    boolean updateDepartments(int departmentId, Departments departments);

    int selectDepartmentsRows(int userId);

    List<Departments> selectDepartmentsByFuzzyKeywords(String keywords, int offset, int limit);

    int selectDepartmentsByFuzzyKeywordsRows(String keywords);
}
