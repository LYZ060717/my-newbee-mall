package com.zaaach.citypicker.model;

import java.util.List;

/**
 * 省份模型
 */
public class Province {
    private String name;
    private List<City> cityList;

    public Province(String name, List<City> cityList) {
        this.name = name;
        this.cityList = cityList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }
}
