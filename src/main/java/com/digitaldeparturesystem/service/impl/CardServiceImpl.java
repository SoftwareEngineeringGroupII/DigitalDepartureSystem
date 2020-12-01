package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.mapper.CardMapper;
import com.digitaldeparturesystem.mapper.NoticeMapper;
import com.digitaldeparturesystem.mapper.SectorMapper;
import com.digitaldeparturesystem.mapper.StudentMapper;
import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Notice;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ICardService;
import com.digitaldeparturesystem.utils.Constants;
import com.digitaldeparturesystem.utils.IdWorker;
import com.digitaldeparturesystem.utils.MybatisUtils;
import com.digitaldeparturesystem.utils.TextUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service  //一卡通
@Transactional
public class CardServiceImpl implements ICardService {


    @Autowired
    private IdWorker idWorker;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private NoticeMapper noticeMapper;

    @Resource
    private CardMapper cardMapper;

    //-----2020.11.24 周二增加部分---- //

    @Value("${photo.dir}")
    private String realPath;

    /**
     * 上传公告:图片字段
     * @param notice
     * @param photo
     * @return
     */
    @Override
    public ResponseResult uploadNotice(Notice notice, MultipartFile photo) throws IOException {

        //获取当前用户信息

        //检查数据:标题、内容不可以为空
        if (TextUtils.isEmpty(notice.getTitle())) {
            ResponseResult.FAILED("标题不可以为空！");
        }

        if (TextUtils.isEmpty(notice.getContent())) {
            ResponseResult.FAILED("公告内容不可以为空！");
        }

        log.info("公告信息：[{}]",notice.toString());
        log.info("公告图片：[{}]",photo.getOriginalFilename());
        try{
            String newFileName = idWorker.nextId() + "." + FilenameUtils.getExtension(photo.getOriginalFilename());
            photo.transferTo(new File(realPath, newFileName));//保存到指定的路径文件路径中
            //设置数据
            Notice newNotice = new Notice();
            newNotice.setPath(newFileName);//图片路径
            newNotice.setNoticeId(idWorker.nextId()+"");//公告ID
            newNotice.setTitle(notice.getTitle());//标题
            newNotice.setContent(notice.getContent());//内容
            newNotice.setRemark(notice.getRemark());//设置备注
            //newNotice1.setPublisherId(currentUser.getClerkID());//获取当前发布者ID
            //newNotice1.setNoticeType(currentUser.getDepartment()); //获取当前发布者部门
            newNotice.setCheckStatus("0");//默认未审核
            newNotice.setIsTop(notice.getIsTop()); //默认非置顶:这里前端有个选择置不置顶,但是好像是超级管理员的事情
            newNotice.setPublishTime(new Date());//发布时间

            //保存数据
            noticeMapper.save(newNotice);

        }catch (Exception e){
            return ResponseResult.FAILED("上传公告失败!");
        }
        //返回结果
        return ResponseResult.SUCCESS("公告添加成功");
    }



    /**
     *  分页查询所有学生的所有信息(仅限于学生表)
     * @param map
     * @return
     */
    @Override
    public ResponseResult findAllByPage(Map<String,Object> map) {
        Integer page = (Integer)map.get("page");
        Integer size = (Integer)map.get("size");

        //pageHelper使用
        PageHelper.startPage(page,size);
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> userList = studentMapper.getStudentList();
        PageInfo<Student> studentPageInfo = new PageInfo<>(userList);

        int pageNum = studentPageInfo.getPageNum();
        int pages = studentPageInfo.getPages();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("list",userList);
        map1.put("pageNum",pageNum);
        map1.put("pages",pages);
        return ResponseResult.SUCCESS("查询成功").setData(map1);
    }



    /**
     *  按学号查询一卡通详情
     * @param studentId
     * @return
     */
    @Override
    public ResponseResult getStudentByIdForCard(String studentId) {
        Map<String, Object> studentByIdForCard = cardMapper.getStudentByIdForCard(studentId);
        if (studentByIdForCard == null) {
            return  ResponseResult.FAILED("查找失败！没有该学生的一卡通详情！");
        }
        return ResponseResult.SUCCESS("查找成功").setData(studentByIdForCard);
    }


    /**
     *  审核一卡通
     * @param stuNumber
     * @return
     */
    public  ResponseResult doCheckForCard(String stuNumber){
        int i = studentMapper.doCheckCard(stuNumber);
        if (i > 0){
            return ResponseResult.SUCCESS("审核成功！");
        }
        return ResponseResult.FAILED("审核失败！请重新操作");
    }


    public  ResponseResult findAllByPageAndType(Integer start,Integer size,String stuDept,String stuType,String cardStatus){
        //判断类型,如果是所有类型的状态将其置空
        stuDept = (stuDept.equals("所有学院") ?"":stuDept);
        stuType =(stuType.equals("所有学生")?"":stuType);
        cardStatus = (cardStatus.equals("所有状态")?"":cardStatus);

        Map<String,String> params = new HashMap<>();
        params.put("stuDept",stuDept);
        params.put("stuType",stuType);
        params.put("cardStatus",cardStatus);
        log.info("params --- >> "+params);

        if(size==0){
            //如果未设置显示条数，默认为5
            size=5;
        }

        //pageHelper使用
        //分页处理,显示第start页的size条数据
        PageHelper.startPage(start,size);
        List<Map<String, Object>> students = cardMapper.listStudentCardInfos(params);
        PageInfo<Map<String, Object>> cardPageInfo = new PageInfo<>(students);
        int pageNum = cardPageInfo.getPageNum();
        int pages = cardPageInfo.getPages();
        long total = cardPageInfo.getTotal();//获取记录总数
        if (students.isEmpty()) {
            return ResponseResult.FAILED("没有数据");
        }

        Map<String,Object> map = new HashMap<>();
        map.put("list",students);
        map.put("pageNum",pageNum);
        map.put("pages",pages);
        map.put("total",total);
        return ResponseResult.SUCCESS("查询成功").setData(map);

    }





}
