package com.digitaldeparturesystem.utils;

import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Student;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class TokenUtils {

    public static Clerk parseClerkByTokenKey(RedisUtils redisUtils, String tokenKey) {
        //记得加前缀，通过前面保存的(tokenKey,token)拿到token
        String token = (String) redisUtils.get(Constants.Clerk.KEY_TOKEN + tokenKey);
        if (token != null) {
            try {
                //说明有token，解析token
                Claims claims = JwtUtil.parseJWT(token);
                return ClaimsUtils.claims2Clerk(claims);
            } catch (Exception e) {
                //过期了
                log.info("parseByTokenKey ==> " + tokenKey + " ========== 过期了");
                return null;
            }
        }
        return null;
    }

    public static Student parseStudentByTokenKey(RedisUtils redisUtils, String tokenKey) {
        //记得加前缀，通过前面保存的(tokenKey,token)拿到token
        String token = (String) redisUtils.get(Constants.Clerk.KEY_TOKEN + tokenKey);
        if (token != null) {
            try {
                //说明有token，解析token
                Claims claims = JwtUtil.parseJWT(token);
                return ClaimsUtils.claims2Student(claims);
            } catch (Exception e) {
                //过期了
                log.info("parseByTokenKey ==> " + tokenKey + " ========== 过期了");
                return null;
            }
        }
        return null;
    }
}
