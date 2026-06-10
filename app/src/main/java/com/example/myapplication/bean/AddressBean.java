package com.example.myapplication.bean;

/**
 * 收货地址数据模型
 */
public class AddressBean {
    /** 地址ID */
    private Long addressId;
    /** 收货人姓名 */
    private String userName;
    /** 手机号 */
    private String userPhone;
    /** 省份 */
    private String provinceName;
    /** 城市 */
    private String cityName;
    /** 区县 */
    private String regionName;
    /** 详细地址 */
    private String detailAddress;
    /** 是否默认地址 0-否 1-是 */
    private Integer defaultFlag;

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public Integer getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(Integer defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    /**
     * 获取完整地址
     */
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (provinceName != null) sb.append(provinceName);
        if (cityName != null) sb.append(cityName);
        if (regionName != null) sb.append(regionName);
        if (detailAddress != null) sb.append(detailAddress);
        return sb.toString();
    }
}
