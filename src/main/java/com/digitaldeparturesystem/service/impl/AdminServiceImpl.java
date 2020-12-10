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

    private RoleAuthorityMapper roleAuthorityMapper;

    /**
     * 获取用户所拥有的权限对应的菜单项
     * @return
     */
    @Override
    public ResponseResult findAuditMenu() {
        List<Authorities> parentMenu = authoritiesMapper.findByParentIsNullOrderByIndex();
        for (Authorities menu : parentMenu) {
            AuthorityTreeUtils.getChildrenToMenu(roleAuthorityMapper,menu);
        }
        //根据parentId，找出children
        return ResponseResult.SUCCESS("查找成功").setData(parentMenu);
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
    public ResponseResult deleteClerk(String clerkId) {
        //查找clerk是否存在
        Clerk clerk = sectorMapper.findOneById(clerkId);
        if (clerk == null){
            return ResponseResult.FAILED("用户不存在");
        }
        adminMapper.deleteClerk(clerkId);
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

    @Resource
    private RoleMapper roleMapper;

    @Override
    public ResponseResult insertRoleToUser(String clerkId, List<String> roleIds) {
        //检查数据
        Clerk clerk = sectorMapper.findOneById(clerkId);
        if (clerk == null){
            return ResponseResult.FAILED("该用户不存在");
        }
        userRoleMapper.deleteAllRoleByUser(clerkId);
        Map<String,String> map = new HashMap<>();
        for (String roleId : roleIds) {
            Role role = roleMapper.getRoleById(roleId);
            if (role == null){
                return ResponseResult.FAILED("插入失败，" + role.getName() + "角色不存在");
            }
            map.put("clerkId",clerkId);
            map.put("roleId",roleId);
            String userRoleId = userRoleMapper.findUserRoleData(map);
            if (userRoleId == null){
                //补充数据
                map.put("id",String.valueOf(idWorker.nextId()));
                userRoleMapper.addRoleToUser(map);
            }
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

    @Override
    public ResponseResult deleteRoleToUser(String clerkId, List<String> roleIds) {
        Map<String,String> map = new HashMap<>();
        for (String roleId : roleIds) {
            map.put("clerkId",clerkId);
            map.put("roleIds",roleId);
            sectorMapper.deleteRoleToUser(map);
        }
        return ResponseResult.SUCCESS("删除用户角色成功");
    }
}
