package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.*;
import com.digitaldeparturesystem.pojo.Authorities;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Role;
import com.digitaldeparturesystem.pojo.Settings;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.response.ResponseStatus;
import com.digitaldeparturesystem.service.IAdminService;
import com.digitaldeparturesystem.utils.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Transactional
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    private SettingsMapper settingsMapper;

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private SectorMapper sectorMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private AuthoritiesMapper authoritiesMapper;

    @Override
    public ResponseResult insertManagerAccount(Clerk clerk) {
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
        adminMapper.addClerk(clerk);
        //更新已经添加的标记
        //肯定没有的
        Settings settings = new Settings();
        settings.setId(String.valueOf(idWorker.nextId()));
        settings.setCreateTime(new Date());
        settings.setUpdateTime(new Date());
        settings.setKey(Constants.Setting.ADMIN_ACCOUNT_INIT_STATUS);
        settings.setValue("1");
        settingsMapper.addSetting(settings);
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
        return ResponseResult.SUCCESS("查找成功").setData(menus);
    }

    @Override
    public ResponseResult insertClerk(Clerk clerk, HttpServletRequest request) {
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
        //第九步：返回结果
        return ResponseResult.GET(ResponseStatus.JOIN_SUCCESS);
    }

    @Override
    public ResponseResult deleteClerkByStatus(String clerkId) {
        //查找clerk是否存在
        Clerk clerk = sectorMapper.findOneById(clerkId);
        if (clerk == null){
            return ResponseResult.FAILED("用户不存在");
        }
        adminMapper.deleteClerkByStatus(clerkId);
        return ResponseResult.SUCCESS("用户删除成功");
    }

    @Override
    public ResponseResult updateClerk(String clerkId, Clerk clerk) {
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
        return ResponseResult.SUCCESS("用户信息更新成功");
    }

    @Override
    public ResponseResult findClerkById(String clerkId) {
        //查询
        Clerk clerk = sectorMapper.findOneById(clerkId);
        List<Clerk> clerkList = new ArrayList<>();
        clerkList.add(clerk);
        return ResponseResult.SUCCESS("查询用户成功").setData(clerkList);
    }

    @Override
    public ResponseResult findAllClerks() {
        //查询
        List<Clerk> clerks = adminMapper.findAllClerks();
        return ResponseResult.SUCCESS("查询所有用户成功").setData(clerks);
    }

    @Override
    public ResponseResult insertRoleToUser(String clerkId, List<String> roleIds) {
        Clerk clerk = sectorMapper.findOneById(clerkId);
        if (clerk == null){
            return ResponseResult.FAILED("该用户不存在");
        }
        userRoleMapper.deleteAllRoleByUser(clerkId);
        Map<String,String> map = new HashMap<>();
        for (String roleId : roleIds) {
            map.put("id",String.valueOf(idWorker.nextId()));
            map.put("clerkId",clerkId);
            map.put("roleId",roleId);
            userRoleMapper.addRoleToUser(map);
        }
        return ResponseResult.SUCCESS("用户角色添加成功");
    }

    @Override
    public ResponseResult findRolesByUser(String clerkId) {
        Clerk clerk = sectorMapper.findOneById(clerkId);
        if (clerk == null){
            return ResponseResult.FAILED("该用户不存在");
        }
        List<Role> roles = userRoleMapper.getRolesByUser(clerkId);
        return ResponseResult.SUCCESS("查询用户所拥有的角色成功").setData(roles);
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
        Authorities authority = authoritiesMapper.findByName(name);
        return authority;
    }
}
