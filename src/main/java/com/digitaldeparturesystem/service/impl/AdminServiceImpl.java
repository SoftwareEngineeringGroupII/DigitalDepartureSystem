package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.AdminMapper;
import com.digitaldeparturesystem.mapper.SettingsMapper;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Settings;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IAdminService;
import com.digitaldeparturesystem.utils.Constants;
import com.digitaldeparturesystem.utils.IdWorker;
import com.digitaldeparturesystem.utils.MybatisUtils;
import com.digitaldeparturesystem.utils.TextUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseResult initManagerAccount(Clerk clerk) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        //检查是否有初始化
        SettingsMapper settingsMapper = sqlSession.getMapper(SettingsMapper.class);
        Settings adminAccountStatus = settingsMapper.findOneByKey(Constants.Setting.ADMIN_ACCOUNT_INIT_STATUS);
        if (adminAccountStatus != null){
            return ResponseResult.FAILED("管理员账号已初始化");
        }
        //检查数据
        if (TextUtils.isEmpty(clerk.getClerkAccount())){
            return ResponseResult.FAILED("账号不能为空");
        }
        if (TextUtils.isEmpty(clerk.getClerkName())){
            return ResponseResult.FAILED("姓名不能为空");
        }
        if (TextUtils.isEmpty(clerk.getClerkPwd())){
            return ResponseResult.FAILED("密码不能为空");
        }
        if (TextUtils.isEmpty(clerk.getDepartment())){
            return ResponseResult.FAILED("部门不能为空");
        }
        //补充数据
        clerk.setClerkID(String.valueOf(idWorker.nextId()));
        clerk.setClerkPower(Constants.Clerk.POWER_ADMIN);
        clerk.setClerkPhoto(Constants.Clerk.DEFAULT_PHOTO);
        clerk.setClerkStatus("1");
        //对密码进行加密
        //原密码
        String password = clerk.getClerkPwd();
        //加密之后的密码
        String encode = bCryptPasswordEncoder.encode(password);
        clerk.setClerkPwd(encode);

        //保存到数据库
        AdminMapper adminMapper = sqlSession.getMapper(AdminMapper.class);
        adminMapper.addClerk(clerk);
        //提交事务
        sqlSession.commit();
        //更新已经添加的标记
        //肯定没有的
        Settings settings = new Settings();
        settings.setId(String.valueOf(idWorker.nextId()));
        settings.setCreateTime(new Date());
        settings.setUpdateTime(new Date());
        settings.setKey(Constants.Setting.ADMIN_ACCOUNT_INIT_STATUS);
        settings.setValue("1");
        settingsMapper.addSetting(settings);
        //提交数事务
        sqlSession.commit();
        sqlSession.close();
        return ResponseResult.SUCCESS("初始化成功");
    }



}
