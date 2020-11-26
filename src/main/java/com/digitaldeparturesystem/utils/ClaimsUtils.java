package com.digitaldeparturesystem.utils;

import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.pojo.Clerk;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClaimsUtils {

    private static final String CLERK_ID = "clerk_id";
    private static final String CLERK_ACCOUNT = "clerk_account";
    private static final String CLERK_NAME = "clerk_name";
    private static final String CLERK_PHOTO = "clerk_photo";
    private static final String CLERK_EMAIL = "clerk_email";
    private static final String DEPARTMENT = "department";
    private static final String AUTHORITIES = "authorities";

    public static Map<String,Object> clerk2Claims(Clerk clerk){
        Map<String,Object> claims = new HashMap<>();
        claims.put(CLERK_ID, clerk.getClerkID());
        claims.put(CLERK_ACCOUNT, clerk.getClerkAccount());
        claims.put(CLERK_NAME, clerk.getClerkName());
        claims.put(CLERK_PHOTO, clerk.getClerkPhoto());
        claims.put(CLERK_EMAIL, clerk.getClerkEmail());
        claims.put(DEPARTMENT, clerk.getDepartment());
        claims.put(AUTHORITIES, clerk.getAuthorities());
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
        clerk.setAuthorities((List<? extends GrantedAuthority>) claims.get(AUTHORITIES));
        return clerk;
    }

}
