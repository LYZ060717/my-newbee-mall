package com.example.myapplication.api;

/**
 * API 接口常量类
 * 基础地址：http://172.21.3.8:28019/mallapi/api/v1
 * Swagger：http://172.21.3.8:28019/mallapi/swagger-ui/index.html
 * Vue 版：http://172.21.3.8:3000/#/home
 */
public class Api {

    /** 基础地址（模拟器用 10.0.2.2，真机用实际 IP） */
    public static final String BASE_URL = "http://172.21.3.8:28019/mallapi/api/v1";
    /** 管理后台基础地址 */
    public static final String MANAGE_BASE_URL = "http://172.21.3.8:28019/mallapi/manage-api/v1";

    // ==================== 用户模块 ====================

    /** 登录 */
    public static final String LOGIN = BASE_URL + "/user/login";
    /** 注册 */
    public static final String REGISTER = BASE_URL + "/user/register";
    /** 获取用户信息 */
    public static final String USER_INFO = BASE_URL + "/user/info";
    /** 修改用户信息 */
    public static final String USER_UPDATE = BASE_URL + "/user/update";

    // ==================== 管理员模块 ====================

    /** 管理员登录 */
    public static final String ADMIN_LOGIN = MANAGE_BASE_URL + "/adminUser/login";

    // ==================== 首页模块 ====================

    /** 首页轮播图 */
    public static final String INDEX_CAROUSEL = MANAGE_BASE_URL + "/carousels";
    /** 首页热销商品 */
    public static final String INDEX_HOT = BASE_URL + "/index/hotGoodsList";
    /** 首页新品推荐 */
    public static final String INDEX_NEW = BASE_URL + "/index/newGoodsList";
    /** 首页推荐商品 */
    public static final String INDEX_RECOMMEND = BASE_URL + "/index/recommendGoodsList";

    // ==================== 商品模块 ====================

    /** 商品列表 */
    public static final String GOODS_LIST = BASE_URL + "/goods/list";
    /** 商品详情 */
    public static final String GOODS_DETAIL = BASE_URL + "/goods/detail";

    // ==================== 分类模块 ====================

    /** 分类列表 */
    public static final String CATEGORY_LIST = BASE_URL + "/category/list";
    /** 分类商品 */
    public static final String CATEGORY_GOODS = BASE_URL + "/category/goods";

    // ==================== 购物车模块 ====================

    /** 购物车列表 */
    public static final String CART_LIST = BASE_URL + "/shopping-cart/list";
    /** 添加购物车 */
    public static final String CART_ADD = BASE_URL + "/shopping-cart/add";
    /** 修改购物车数量 */
    public static final String CART_UPDATE = BASE_URL + "/shopping-cart/update";
    /** 删除购物车商品 */
    public static final String CART_DELETE = BASE_URL + "/shopping-cart/delete";

    // ==================== 订单模块 ====================

    /** 生成订单 */
    public static final String ORDER_CREATE = BASE_URL + "/order/create";
    /** 订单列表 */
    public static final String ORDER_LIST = BASE_URL + "/order/list";
    /** 订单详情 */
    public static final String ORDER_DETAIL = BASE_URL + "/order/detail";
    /** 取消订单 */
    public static final String ORDER_CANCEL = BASE_URL + "/order/cancel";
    /** 确认收货 */
    public static final String ORDER_CONFIRM = BASE_URL + "/order/confirm";
}
