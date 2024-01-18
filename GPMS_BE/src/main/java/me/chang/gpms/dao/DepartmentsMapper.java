package me.chang.gpms.dao;

import me.chang.gpms.pojo.Departments;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DepartmentsMapper {

    List<Departments> selectDepartments(int offset, int limit);

    Departments selectById(Integer id);

    Departments selectByName(String name);

    int insertDepartment(Departments departments);

    int updateType(Integer id, Integer type);

    int updateContent(Integer id, String content);
}
