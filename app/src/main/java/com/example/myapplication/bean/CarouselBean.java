package com.example.myapplication.bean;

/**
 * 首页轮播图数据模型
 */
public class CarouselBean {

    /** 轮播图图片地址 */
    private String carouselUrl;
    /** 轮播图点击后的跳转路径 */
    private String redirectUrl;

    public String getCarouselUrl() {
        return carouselUrl;
    }

    public void setCarouselUrl(String carouselUrl) {
        this.carouselUrl = carouselUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
