package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.AdminMapper;
import com.digitaldeparturesystem.mapper.AuthoritiesMapper;
import com.digitaldeparturesystem.mapper.SettingsMapper;
import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Settings;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.IAdminService;
import com.digitaldeparturesystem.utils.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private ClerkUtils clerkUtils;

    /**
     * 获取用户所拥有的权限对应的菜单项
     * @return
     */
    @Override
    public ResponseResult findAuditMenu() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        AuthoritiesMapper authoritiesMapper = sqlSession.getMapper(AuthoritiesMapper.class);
        List<Authorities> menus;
        //判断是否是后门用户
        if(clerkUtils.hasRole("ROLE_ADMIN")){
            //查询所有菜单，子菜单可以通过父级菜单的映射得到
            menus = authoritiesMapper.findByParentIsNullOrderByIndex();
            findChildrenByParentId(authoritiesMapper, menus);
        }else{
            //获取此用户对应的菜单权限
            List<Authorities> tempMenu = authoritiesMapper.findByParentIsNullOrderByIndex();
            findChildrenByParentId(authoritiesMapper, tempMenu);
            menus = auditMenu(tempMenu);
        }
        //根据parentId，找出children
        sqlSession.close();
        return ResponseResult.SUCCESS("查找成功").setData(menus);
    }

    /**
     * 找到菜单下面的子菜单
     * @param authoritiesMapper
     * @param tempMenu
     */
    private void findChildrenByParentId(AuthoritiesMapper authoritiesMapper, List<Authorities> tempMenu) {
        for (Authorities menu : tempMenu) {
            menu.setChildren(authoritiesMapper.findChildrenByParentId(menu.getParentId()));
        }
    }

    //根据用户的菜单权限对菜单进行过滤
    private List<Authorities> auditMenu(List<Authorities> menus) {
        List<Authorities> list = new ArrayList<>();
        for(Authorities menu: menus){
            String name = menu.getName();
            //判断此用户是否有此菜单权限
            if(clerkUtils.hasRole(name)){
                list.add(menu);
                //递归判断子菜单
                if(menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                    menu.setChildren(auditMenu(menu.getChildren()));
                }
            }
        }
        return list;
    }

    public Authorities findByName(String name) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        AuthoritiesMapper authoritiesMapper = sqlSession.getMapper(AuthoritiesMapper.class);
        Authorities authority = authoritiesMapper.findByName(name);
        sqlSession.close();
        return authority;
    }
}
