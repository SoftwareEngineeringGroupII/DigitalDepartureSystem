package com.digitaldeparturesystem.controller.sector;

import com.digitaldeparturesystem.mapper.LibraryMapper;
import com.digitaldeparturesystem.pojo.Dorm;
import com.digitaldeparturesystem.pojo.Lib;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ILibraryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/sector/library")
public class LibraryApi {

    @Autowired
    private ILibraryService libraryService;

    /**
     * 通过学生学号查询图书馆详情
     * @param stuNumber
     * @return
     */
    @GetMapping("/stuNumber")
    public ResponseResult getLibraryByStudentId(@PathVariable("stuNumber")String stuNumber){
        return null;
    }

    /**
     * 审核图书管信息
     * @param studentId
     * @param lib
     * @return
     */
    @PostMapping("/studentId")
    public ResponseResult checkLibrary(@PathVariable("studentId")String studentId, @RequestBody Lib lib){
        return null;
    }

    /**
     * 获取全部图书管列表
     * @return
     */
    @GetMapping
    public ResponseResult getAllLibraries(){
        return null;
    }

    /**
     * 获取已审核的图书管列表
     * @return
     */
    @GetMapping("/check")
    public ResponseResult getCheckLibraries(){
        return null;
    }

    /**
     * 获取未审核的图书管列表
     * @return
     */
    @GetMapping("/uncheck")
    public ResponseResult getUnCheckLibraries(){
        return null;
    }


    /**
     * 分页查询所有学生的结束信息
     * @param start
     * @param size
     * @return
     */
    @GetMapping("/findAllByPage")
    public ResponseResult findAllByPage(@RequestParam("start")Integer start,@RequestParam("size") Integer size){
        return libraryService.findAllByPage(start,size);
    }


    /**
     * 查询某个学生借书详情
     * @param stuNumber
     * @return
     */
    @GetMapping("/getStuBookDetail/{stuNumber}")
    public ResponseResult getStuBookDetail(@PathVariable("stuNumber") String stuNumber){
        //判断传进来的学号是否为空
        if (stuNumber == null) {
            return ResponseResult.FAILED("请重新输入信息");
        }
       return libraryService.getStuBookDetail(stuNumber);
    }


    /**
     *  审核学生某本书接口
     * @param stuNumber
     * @return
     */
    @PutMapping("/checkLibrary/{stuNumber}/{bookId}")
    public ResponseResult checkLibrary(@PathVariable("stuNumber")String stuNumber,
                                       @PathVariable("bookId") String bookId){

       return libraryService.checkLibrary(stuNumber,bookId);

    }

}
