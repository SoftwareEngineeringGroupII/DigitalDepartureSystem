package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentMapperTest {



    @Test
    public void getStudentList(){
        //第一步：获取SqlSession对象
        SqlSession sqlSession = null;
        //建议这种try-finally格式，因为必须要关闭sqlSession，不然浪费资源
        try {
            sqlSession = MybatisUtils.getSqlSession();
            //第二步:执行sql
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            List<Student> studentList = mapper.getStudentList();

            for (Student student : studentList) {
                System.out.println(student.getStuName());
            }
            System.out.println("studentList size ==> " + studentList.size());
        } finally {
            //关闭sqlSession
            sqlSession.close();
        }
    }

    @Test
    public void getUserById(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        System.out.println(mapper.getStudentById("001").getStuName());
        System.out.println(mapper.getStudentById("001").getStuAddress());

        sqlSession.close();
    }

    @Test
    public void addStudent(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        Student student = new Student();
        student.setStuId("001");
        student.setStuNumber("2018110427");
        student.setStuName("感同身受");
        student.setStuPwd("123456");
        student.setStuDept("计算机科学学院");
        student.setStuClass("2018级4班");
        student.setStuPhoneNumber("15328755505");
        student.setStuPhoto("照片");
        student.setStuSex("男");
        student.setStuStatus("1");
        student.setStuInDate(new Date());
        student.setStuOutData(new Date());
        student.setStuAddress("xxxx");
        int result = mapper.addStudent(student);
        if (result>0) {
            System.out.println("插入成功");
        }
        //提交事务
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void updateStudent2(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        Map<String,Object> map = new HashMap<>();
        map.put("id","001");
        map.put("name","感同身受");
        map.put("password","123456");
        map.put("phone","15328755505");
        map.put("address","成都龙泉驿");
        mapper.updateStudent2(map);
        //提交事务
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void updateStudent(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        Student student = new Student();
        student.setStuId("001");
        student.setStuNumber("2018110427");
        student.setStuName("感同身受");
        student.setStuPwd("123456");
        student.setStuDept("计算机科学学院");
        student.setStuClass("2018级4班");
        student.setStuPhoneNumber("15328755505");
        student.setStuPhoto("照片");
        student.setStuSex("男");
        student.setStuStatus("1");
        student.setStuInDate(new Date());
        student.setStuOutData(new Date());
        student.setStuAddress("四川仁寿");
        mapper.updateStudent(student);
        //提交事务
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void deleteStudent(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        mapper.deleteStudent("001");
        //提交事务
        sqlSession.commit();

        sqlSession.close();
    }
}
