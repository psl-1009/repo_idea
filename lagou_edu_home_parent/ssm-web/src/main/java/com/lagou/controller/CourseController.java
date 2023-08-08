package com.lagou.controller;

import com.lagou.domain.Course;
import com.lagou.domain.CourseVO;
import com.lagou.domain.ResponseResult;
import com.lagou.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController   //组合注解
@RequestMapping("/course")
public class CourseController {


    @Autowired
    private CourseService courseService;

    @RequestMapping("/findCourseByCondition")
    public ResponseResult findCourseByCondition(@RequestBody CourseVO courseVO){

        //调用service
        List<Course> list = courseService.findCourseByCondition(courseVO);

        ResponseResult responseResult = new ResponseResult(true, 200, "响应成功", list);
        return responseResult;
    }

    /*
        课程图片上传
     */
    @RequestMapping("/courseUpload")
    public ResponseResult findUpload(@RequestParam("file")MultipartFile file, HttpServletRequest request) throws IOException {

        //1.判断接收到的上传文件是否为空
        if (file.isEmpty()){
            throw new RuntimeException();
        }
        //2.获取项目部署路径
        String realPath = request.getServletContext().getRealPath("/");
       String substring =  realPath.substring(0,realPath.indexOf("ssm-web"));


        //3.获取原文件名
        String originalFilename = file.getOriginalFilename();

        //4.生成一个新的文件名
       String newFileName = System.currentTimeMillis() + originalFilename.substring(originalFilename.lastIndexOf("."));

       //5.文件上传
        String uploadPath = substring + "upload\\";

        File filePath = new File(uploadPath, newFileName);

        //如果目录不存在就创建目录
        if (!filePath.getParentFile().exists()){
            filePath.getParentFile().mkdirs();
            System.out.println("创建目录:" + filePath);
        }

        //图片就进行了真正的上传
        file.transferTo(filePath);

        //6.将文件名和文件路径返回，进行响应
        Map<String,String> map = new HashMap<>();
        map.put("fileName",newFileName);
        map.put("filePath","http://localhost:8080/upload" + newFileName);

        ResponseResult responseResult = new ResponseResult(true, 200, "图片上传成功", map);

        return responseResult;
    }

    /*
        新增课程信息及讲师信息
        新增和修改信息要写在同一个方法中
     */

    @RequestMapping("/saveOrUpdateCourse")
    public ResponseResult saveOrUpdateCourse(@RequestBody CourseVO courseVO) throws InvocationTargetException, IllegalAccessException {

        if (courseVO.getId() == null){
            //调用service
            courseService.saveCourseOrTeacher(courseVO);
            ResponseResult responseResult = new ResponseResult(true, 200, "新增成功成功", null);
            return responseResult;
        }else {
            courseService.updateCourseOrTeacher(courseVO);
            ResponseResult responseResult = new ResponseResult(true, 200, "修改成功", null);
            return responseResult;
        }

    }

    /*
        回显课程信息(根据ID查询对应的课程信息及关联的讲师信息)
     */
    @RequestMapping("/findCourseById")
    public ResponseResult findCourseById(Integer id){

        CourseVO courseVO = courseService.findCourseById(id);

        ResponseResult responseResult = new ResponseResult(true, 200, "根据ID查询课程信息成功", courseVO);
        return responseResult;

    }

    /*
        课程状态管理
     */
    @RequestMapping("/updateCourseStatus")
    public ResponseResult updateCourseStatus(Integer id,Integer status){

        //调用service传递参数，完成课程状态变更
        courseService.updateCourseStatus(id,status);

        //响应数据
        HashMap<String,Object> map = new HashMap<>();
        map.put("status",status);

        ResponseResult responseResult = new ResponseResult(true, 200, "课程状态变更成功", map);
        return responseResult;
    }
}
