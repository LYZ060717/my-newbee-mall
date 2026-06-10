package com.example.myapplication.bean;

import java.util.List;

/**
 * 首页聚合数据模型（对应后端 /index-infos 接口）
 */
public class IndexInfoBean {

    /** 轮播图列表 */
    private List<CarouselBean> carousels;
    /** 首页热销商品列表 */
    private List<GoodsBean> hotGoodses;
    /** 首页新品推荐列表 */
    private List<GoodsBean> newGoodses;
    /** 首页推荐商品列表 */
    private List<GoodsBean> recommendGoodses;

    public List<CarouselBean> getCarousels() {
        return carousels;
    }

    public void setCarousels(List<CarouselBean> carousels) {
        this.carousels = carousels;
    }

    public List<GoodsBean> getHotGoodses() {
        return hotGoodses;
    }

    public void setHotGoodses(List<GoodsBean> hotGoodses) {
        this.hotGoodses = hotGoodses;
    }

    public List<GoodsBean> getNewGoodses() {
        return newGoodses;
    }

    public void setNewGoodses(List<GoodsBean> newGoodses) {
        this.newGoodses = newGoodses;
    }

    public List<GoodsBean> getRecommendGoodses() {
        return recommendGoodses;
    }

    public void setRecommendGoodses(List<GoodsBean> recommendGoodses) {
        this.recommendGoodses = recommendGoodses;
    }
}
