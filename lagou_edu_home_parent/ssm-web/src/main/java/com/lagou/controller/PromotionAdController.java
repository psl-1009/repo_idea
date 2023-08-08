package com.lagou.controller;

import com.github.pagehelper.PageInfo;
import com.lagou.domain.PromotionAd;
import com.lagou.domain.PromotionAdVO;
import com.lagou.domain.ResponseResult;
import com.lagou.service.PromotionAdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/PromotionAd")
public class PromotionAdController {

    @Autowired
    private PromotionAdService promotionAdService;


    /*
        广告分页查询
     */
    @RequestMapping("/findAllPromotionAdByPage")
    public ResponseResult findAllAdPage(PromotionAdVO promotionAdVO){

        PageInfo<PromotionAd> pageInfo = promotionAdService.findAllPromotionAdByPage(promotionAdVO);

        ResponseResult responseResult = new ResponseResult(true, 200, "广告分页查询成功", pageInfo);

        return responseResult;
    }

    /*
        图片上传
     */
    @RequestMapping("/PromotionAdUpload")
    public ResponseResult findUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {

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
新增或更新广告位置
*/
    @RequestMapping("/saveOrUpdatePromotionAd")
    public ResponseResult saveOrUpdatePromotionAd(@RequestBody PromotionAd promotionAd) {
        try {
            if (promotionAd.getId() == null) {
                Date date = new Date();
                promotionAd.setCreateTime(date);
                promotionAd.setUpdateTime(date);
                promotionAdService.savePromotionAd(promotionAd);
                ResponseResult result = new ResponseResult(true, 200, "响应成功",
                        null);
                return result;
            } else {
                Date date = new Date();
                promotionAd.setUpdateTime(date);
                promotionAdService.updatePromotionAd(promotionAd);
                ResponseResult result = new ResponseResult(true, 200, "响应成功",
                        null);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据id回显 广告数据
     * */
    @RequestMapping("/findPromotionAdById")
    public ResponseResult findPromotionAdById(@RequestParam int id){
        try {
            PromotionAd promotionAd = promotionAdService.findPromotionAdById(id);
            ResponseResult result = new ResponseResult(true,200,"响应成功",promotionAd);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
        广告动态上下线
     */
    @RequestMapping("/updatePromotionAdStatus")
    public ResponseResult updatePromotionStatus(Integer id,Integer status){

        promotionAdService.updatePromotionAdStatus(id,status);

        ResponseResult responseResult = new ResponseResult(true, 200, "广告动态上下线成功", null);
        return responseResult;
    }
}


