package com.digitaldeparturesystem.utils;

import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Student;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

public class ClaimsUtils {

    private static final String CLERK_ID = "clerk_id";
    private static final String CLERK_ACCOUNT = "clerk_account";
    private static final String CLERK_NAME = "clerk_name";
    private static final String CLERK_PHOTO = "clerk_photo";
    private static final String CLERK_EMAIL = "clerk_email";
    private static final String DEPARTMENT = "department";

    private static final String STUDENT_ID = "student_id";
    private static final String STUDENT_NUMBER = "student_number";
    private static final String STUDENT_NAME = "student_name";
    private static final String STUDENT_DEPT = "student_dept";
    private static final String STUDENT_CLASS = "student_class";
    private static final String STUDENT_CONTACT = "student_contact";
    private static final String STUDENT_SEX = "student_sex";
    private static final String STUDENT_STATUS = "student_status";
    private static final String STUDENT_IN_DATA = "student_in_data";
    private static final String STUDENT_OUT_DATA = "student_out_data";
    private static final String STUDENT_ADDRESS = "student_address";
    private static final String STUDENT_SPECIALTY = "student_specialty";
    private static final String STUDENT_NATION = "student_nation";
    private static final String STUDENT_FEATURE = "student_feature";
    private static final String STUDENT_TYPE = "student_type";
    private static final String STUDENT_CREDIT = "student_credit";
    private static final String STUDENT_PHONE_NUMBER = "student_phone_number";
    private static final String STUDENT_PHOTO = "student_photo";

    public static Map<String,Object> clerk2Claims(Clerk clerk){
        Map<String,Object> claims = new HashMap<>();
        claims.put(CLERK_ID, clerk.getClerkID());
        claims.put(CLERK_ACCOUNT, clerk.getClerkAccount());
        claims.put(CLERK_NAME, clerk.getClerkName());
        claims.put(CLERK_PHOTO, clerk.getClerkPhoto());
        claims.put(CLERK_EMAIL, clerk.getClerkEmail());
        claims.put(DEPARTMENT, clerk.getDepartment());
        return claims;
    }

    public static Map<String,Object> student2Claims(Student student){
        Map<String,Object> claims = new HashMap<>();
        claims.put(STUDENT_ID, student.getStuId());
        claims.put(STUDENT_NUMBER, student.getStuNumber());
        claims.put(STUDENT_NAME, student.getStuName());
        claims.put(STUDENT_DEPT, student.getStuDept());
        claims.put(STUDENT_CLASS, student.getStuClass());
        claims.put(STUDENT_CONTACT, student.getStuContact());
        claims.put(STUDENT_SEX, student.getStuSex());
        claims.put(STUDENT_STATUS, student.getStuStatus());
        claims.put(STUDENT_IN_DATA, student.getStuInDate());
        claims.put(STUDENT_OUT_DATA, student.getStuOutDate());
        claims.put(STUDENT_ADDRESS, student.getStuAddress());
        claims.put(STUDENT_SPECIALTY, student.getStuSpecialty());
        claims.put(STUDENT_NATION, student.getStuNation());
        claims.put(STUDENT_FEATURE, student.getStuFeature());
        claims.put(STUDENT_TYPE, student.getStuType());
        claims.put(STUDENT_CREDIT, student.getStuCredit());
        claims.put(STUDENT_PHONE_NUMBER, student.getStuPhoneNumber());
        claims.put(STUDENT_PHOTO, student.getStuPhoto());
        return claims;
    }
    
    public static Clerk claims2Clerk(Claims claims){
        Clerk clerk = new Clerk();
        String id = (String)claims.get(CLERK_ID);
        clerk.setClerkID(id);
        String clerkAccount = (String)claims.get(CLERK_ACCOUNT);
        clerk.setClerkAccount(clerkAccount);
        String clerkName = (String)claims.get(CLERK_NAME);
        clerk.setClerkName(clerkName);
        String clerkPhoto = (String)claims.get(CLERK_PHOTO);
        clerk.setClerkPhoto(clerkPhoto);
        String clerkEmail = (String)claims.get(CLERK_EMAIL);
        clerk.setClerkEmail(clerkEmail);
        String department = (String)claims.get(DEPARTMENT);
        clerk.setDepartment(department);
        return clerk;
    }

    public static Student claims2Student(Claims claims){
        Student student = new Student();
        student.setStuId((String) claims.get(STUDENT_ID));
        student.setStuNumber((String) claims.get(STUDENT_NUMBER));
        student.setStuName((String) claims.get(STUDENT_NAME));
        student.setStuDept((String) claims.get(STUDENT_DEPT));
        student.setStuClass((String) claims.get(STUDENT_CLASS));
        student.setStuContact((String) claims.get(STUDENT_CONTACT));
        student.setStuSex((String) claims.get(STUDENT_SEX));
        student.setStuStatus((String) claims.get(STUDENT_STATUS));
        student.setStuInDate(new Date((Long) claims.get(STUDENT_IN_DATA)));
        student.setStuOutDate(new Date((Long)  claims.get(STUDENT_OUT_DATA)));
        student.setStuAddress((String) claims.get(STUDENT_ADDRESS));
        student.setStuSpecialty((String) claims.get(STUDENT_SPECIALTY));
        student.setStuNation((String) claims.get(STUDENT_NATION));
        student.setStuFeature((String) claims.get(STUDENT_FEATURE));
        student.setStuType((String) claims.get(STUDENT_TYPE));
        student.setStuCredit((String) claims.get(STUDENT_CREDIT));
        student.setStuPhoneNumber((String) claims.get(STUDENT_PHONE_NUMBER));
        student.setStuPhoto((String) claims.get(STUDENT_PHOTO));
        return student;
    }

}
