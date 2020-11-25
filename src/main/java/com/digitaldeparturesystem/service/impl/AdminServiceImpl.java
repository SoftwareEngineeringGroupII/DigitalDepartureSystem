package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.*;
import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Role;
import com.digitaldeparturesystem.pojo.Settings;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.response.ResponseState;
import com.digitaldeparturesystem.service.IAdminService;
import com.digitaldeparturesystem.utils.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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

    @Override
    public ResponseResult registerClerk(Clerk clerk, HttpServletRequest request) {
        //第一步：检查当前用户名、姓名、部门是否已经注册
        String clerkAccount = clerk.getClerkAccount();
        if (TextUtils.isEmpty(clerkAccount)) {
            return ResponseResult.FAILED("用户名不可以为空");
        }
        if (TextUtils.isEmpty(clerk.getClerkName())){
            return ResponseResult.FAILED("姓名不可以为空");
        }
        if (TextUtils.isEmpty(clerk.getDepartment())){
            return ResponseResult.FAILED("部门不可以为空");
        }
        //数据库配置
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        SectorMapper sectorMapper = sqlSession.getMapper(SectorMapper.class);
        AdminMapper adminMapper = sqlSession.getMapper(AdminMapper.class);
        Clerk clerkFromDbByUserName = sectorMapper.findOneByClerkAccount(clerkAccount);
        if (clerkFromDbByUserName != null) {
            return ResponseResult.FAILED("该用户已经注册");
        }
        //第二步：检查邮箱格式是否正确
        String email = clerk.getClerkEmail();
        if (TextUtils.isEmpty(email)) {
            return ResponseResult.FAILED("邮箱地址不能为空");
        }
        if (!TextUtils.isEmailAddressOk(email)) {
            return ResponseResult.FAILED("邮箱地址格式不正确");
        }
        //第三步：检查邮箱是否已经注册
        Clerk clerkByEmail = sectorMapper.findOneByEmail(email);
        if (clerkByEmail != null) {
            return ResponseResult.FAILED("该邮箱地址已经注册");
        }
        //达到可以注册的条件
        //第六步：对密码进行加密
        String password = clerk.getClerkPwd();
        if (TextUtils.isEmpty(password)) {
            return ResponseResult.FAILED("密码不可以为空");
        }
        clerk.setClerkPwd(bCryptPasswordEncoder.encode(password));
        //第七布：补全数据
        //包括：ID，注册IP，登录IP，角色、头像、创建时间、更新时间
        clerk.setClerkID(idWorker.nextId() + "");
        clerk.setClerkPhoto(Constants.Clerk.DEFAULT_PHOTO);
        clerk.setClerkStatus("1");
        //第八步：保存到数据库
        adminMapper.addClerk(clerk);
        //提交事务，关闭sqlSession
        sqlSession.commit();
        sqlSession.close();
        //第九步：返回结果
        return ResponseResult.GET(ResponseState.JOIN_SUCCESS);
    }

    @Override
    public ResponseResult deleteClerkByStatus(String clerkId) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        SectorMapper sectorMapper = sqlSession.getMapper(SectorMapper.class);
        AdminMapper adminMapper = sqlSession.getMapper(AdminMapper.class);
        //查找clerk是否存在
        Clerk clerk = sectorMapper.findOneById(clerkId);
        if (clerk == null){
            return ResponseResult.FAILED("用户不存在");
        }
        adminMapper.deleteClerkByStatus(clerkId);
        sqlSession.commit();
        sqlSession.close();
        return ResponseResult.SUCCESS("用户删除成功");
    }

    @Override
    public ResponseResult updateClerk(String clerkId, Clerk clerk) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        SectorMapper sectorMapper = sqlSession.getMapper(SectorMapper.class);
        //查询
        Clerk clerkFromDB = sectorMapper.findOneById(clerkId);
        if (clerkFromDB == null){
            return ResponseResult.FAILED("没有这个用户");
        }
        if (!TextUtils.isEmpty(clerk.getClerkName())){
            clerkFromDB.setClerkName(clerk.getClerkName());
        }
        if (!TextUtils.isEmpty(clerk.getClerkEmail())){
            clerkFromDB.setClerkName(clerk.getClerkEmail());
        }
        if (!TextUtils.isEmpty(clerk.getClerkPhoto())){
            clerkFromDB.setClerkName(clerk.getClerkPhoto());
        }
        if (!TextUtils.isEmpty(clerk.getDepartment())){
            clerkFromDB.setClerkName(clerk.getDepartment());
        }
        //修改
        sectorMapper.updateClerk(clerkFromDB);
        sqlSession.commit();
        sqlSession.close();
        return ResponseResult.SUCCESS("用户信息更新成功");
    }

    @Override
    public ResponseResult getClerkById(String clerkId) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        SectorMapper sectorMapper = sqlSession.getMapper(SectorMapper.class);
        //查询
        Clerk clerk = sectorMapper.findOneById(clerkId);
        sqlSession.close();
        return ResponseResult.SUCCESS("查询用户成功").setData(clerk);
    }

    @Override
    public ResponseResult getAllClerks() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        AdminMapper adminMapper = sqlSession.getMapper(AdminMapper.class);
        //查询
        List<Clerk> clerks = adminMapper.findAllClerks();
        sqlSession.close();
        return ResponseResult.SUCCESS("查询所有用户成功").setData(clerks);
    }

    @Override
    public ResponseResult addRoleToUser(String clerkId, List<Role> roles) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserRoleMapper userRoleMapper = sqlSession.getMapper(UserRoleMapper.class);
        SectorMapper sectorMapper = sqlSession.getMapper(SectorMapper.class);
        Clerk clerk = sectorMapper.findOneById(clerkId);
        if (clerk == null){
            return ResponseResult.FAILED("该用户不存在");
        }
        userRoleMapper.deleteAllRoleByUser(clerkId);
        for (Role role : roles) {
            userRoleMapper.addRoleToUser(String.valueOf(idWorker.nextId()),clerkId,role.getId());
        }
        sqlSession.commit();
        sqlSession.close();
        return ResponseResult.SUCCESS("用户角色添加成功");
    }

    @Override
    public ResponseResult getRolesByUser(String clerkId) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserRoleMapper userRoleMapper = sqlSession.getMapper(UserRoleMapper.class);
        SectorMapper sectorMapper = sqlSession.getMapper(SectorMapper.class);
        Clerk clerk = sectorMapper.findOneById(clerkId);
        if (clerk == null){
            return ResponseResult.FAILED("该用户不存在");
        }
        userRoleMapper.getRolesByUser(clerkId);
        sqlSession.close();
        return null;
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
