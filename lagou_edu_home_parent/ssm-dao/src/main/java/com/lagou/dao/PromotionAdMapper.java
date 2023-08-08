package com.lagou.dao;

import com.lagou.domain.PromotionAd;

import java.util.List;

public interface PromotionAdMapper {

    /*
        分页查询广告信息
     */
    public List<PromotionAd> findAllPromotionAdByPage();

    /*
        新增广告信息
     */
    void savePromotionAd(PromotionAd promotionAd);

    /*
       修改广告信息
     */
    void updatePromotionAd(PromotionAd promotionAd);

    /**
     * 根据id查询广告信息
     * */
    PromotionAd findPromotionAdById(int id);

    /*
        广告动态上下线
     */
    public void updatePromotionAdStatus(PromotionAd promotionAd);
}
