package com.digitaldeparturesystem.utils;

public interface Constants {

    /**
     * 这里是部分管理员
     */
    interface Clerk{
        String POWER_ADMIN = "power_admin";
        String DEFAULT_PHOTO = "https://upload.jianshu.io/users/upload_avatars/15748212/9e38ce4e-dcb8-49cf-8eaf-8bdd8ba99f21.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240";
        String DEFAULT_STATUS = "1";
        //redis的key
        String KEY_CAPTCHA_CONTENT = "key_captcha_content_";
        String KEY_EMAIL_CODE_CONTENT = "key_email_code_content_";
        String KEY_EMAIL_SEND_IP = "key_email_send_ip_";
        String KEY_EMAIL_SEND_ADDRESS = "key_email_send_address_";
        String KEY_TOKEN = "key_token_";
        String COOKIE_TOKEN_KEY = "digital_department_system_token";
        String KEY_AUTHORITY_CONTENT = "key_authority_content";
    }

    interface Setting{
        String ADMIN_ACCOUNT_INIT_STATUS = "admin_account_init_status";
    }

    //分页查询
    interface Page{
        int DEFAULT_PAGE = 1;
        int MIN_SIZE = 10;
    }



    /**
     * 单位是毫秒
     */
    interface TimeValueInMillions {
        long MIN = 60 * 1000;
        long HOUR = 60 * MIN;
        long HOUR_2 = 60 * MIN * 2;
        long DAY = 24 * HOUR;
        long WEEK = 7 * DAY;
        long MONTH = 30 * DAY;
    }

    /**
     * 单位是秒
     */
    interface TimeValueInSecond {
        int MIN = 60;
        int HOUR = 60 * MIN;
        int HOUR_2 = 60 * MIN * 2;
        int DAY = 24 * HOUR;
        int WEEK = 7 * DAY;
        int MONTH = 30 * DAY;
    }

}
