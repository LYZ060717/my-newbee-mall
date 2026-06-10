package com.zaaach.citypicker;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zaaach.citypicker.adapter.CityListAdapter;
import com.zaaach.citypicker.adapter.ProvinceListAdapter;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市选择器（省-市-区/县 三级联动）
 */
public class CityPicker {
    private Context mContext;
    private Dialog mDialog;
    private RecyclerView rvProvince;
    private RecyclerView rvCity;
    private RecyclerView rvDistrict;
    private TextView tvTitle;
    private TextView tvConfirm;
    private TextView tvCancel;

    private List<Province> provinceList = new ArrayList<>();
    private List<City> cityList = new ArrayList<>();
    private List<City> districtList = new ArrayList<>();

    private ProvinceListAdapter provinceAdapter;
    private CityListAdapter cityAdapter;
    private CityListAdapter districtAdapter;

    private int selectedProvinceIndex = 0;
    private int selectedCityIndex = 0;
    private int selectedDistrictIndex = 0;

    private OnCityPickerListener mListener;

    public interface OnCityPickerListener {
        void onPicked(String province, String city, String district);
    }

    public CityPicker(Context context) {
        mContext = context;
        initData();
    }

    private void initData() {
        provinceList.add(new Province("北京市", getCityList("北京市")));
        provinceList.add(new Province("天津市", getCityList("天津市")));
        provinceList.add(new Province("河北省", getCityList("河北省")));
        provinceList.add(new Province("山西省", getCityList("山西省")));
        provinceList.add(new Province("内蒙古自治区", getCityList("内蒙古自治区")));
        provinceList.add(new Province("辽宁省", getCityList("辽宁省")));
        provinceList.add(new Province("吉林省", getCityList("吉林省")));
        provinceList.add(new Province("黑龙江省", getCityList("黑龙江省")));
        provinceList.add(new Province("上海市", getCityList("上海市")));
        provinceList.add(new Province("江苏省", getCityList("江苏省")));
        provinceList.add(new Province("浙江省", getCityList("浙江省")));
        provinceList.add(new Province("安徽省", getCityList("安徽省")));
        provinceList.add(new Province("福建省", getCityList("福建省")));
        provinceList.add(new Province("江西省", getCityList("江西省")));
        provinceList.add(new Province("山东省", getCityList("山东省")));
        provinceList.add(new Province("河南省", getCityList("河南省")));
        provinceList.add(new Province("湖北省", getCityList("湖北省")));
        provinceList.add(new Province("湖南省", getCityList("湖南省")));
        provinceList.add(new Province("广东省", getCityList("广东省")));
        provinceList.add(new Province("广西壮族自治区", getCityList("广西壮族自治区")));
        provinceList.add(new Province("海南省", getCityList("海南省")));
        provinceList.add(new Province("重庆市", getCityList("重庆市")));
        provinceList.add(new Province("四川省", getCityList("四川省")));
        provinceList.add(new Province("贵州省", getCityList("贵州省")));
        provinceList.add(new Province("云南省", getCityList("云南省")));
        provinceList.add(new Province("西藏自治区", getCityList("西藏自治区")));
        provinceList.add(new Province("陕西省", getCityList("陕西省")));
        provinceList.add(new Province("甘肃省", getCityList("甘肃省")));
        provinceList.add(new Province("青海省", getCityList("青海省")));
        provinceList.add(new Province("宁夏回族自治区", getCityList("宁夏回族自治区")));
        provinceList.add(new Province("新疆维吾尔自治区", getCityList("新疆维吾尔自治区")));

        // 默认选中第一个省份的城市和区县
        if (!provinceList.isEmpty()) {
            cityList.clear();
            cityList.addAll(provinceList.get(0).getCityList());
            if (!cityList.isEmpty()) {
                districtList.clear();
                districtList.addAll(getDistrictList(cityList.get(0).getName()));
            }
        }
    }

    private List<City> getCityList(String province) {
        List<City> cities = new ArrayList<>();
        switch (province) {
            case "北京市":
                cities.add(new City("北京市"));
                break;
            case "天津市":
                cities.add(new City("天津市"));
                break;
            case "上海市":
                cities.add(new City("上海市"));
                break;
            case "重庆市":
                cities.add(new City("重庆市"));
                break;
            case "河北省":
                cities.add(new City("石家庄市"));
                cities.add(new City("唐山市"));
                cities.add(new City("秦皇岛市"));
                cities.add(new City("邯郸市"));
                cities.add(new City("邢台市"));
                cities.add(new City("保定市"));
                cities.add(new City("张家口市"));
                cities.add(new City("承德市"));
                cities.add(new City("沧州市"));
                cities.add(new City("廊坊市"));
                cities.add(new City("衡水市"));
                break;
            case "广东省":
                cities.add(new City("广州市"));
                cities.add(new City("韶关市"));
                cities.add(new City("深圳市"));
                cities.add(new City("珠海市"));
                cities.add(new City("汕头市"));
                cities.add(new City("佛山市"));
                cities.add(new City("江门市"));
                cities.add(new City("湛江市"));
                cities.add(new City("茂名市"));
                cities.add(new City("肇庆市"));
                cities.add(new City("惠州市"));
                cities.add(new City("梅州市"));
                cities.add(new City("汕尾市"));
                cities.add(new City("河源市"));
                cities.add(new City("阳江市"));
                cities.add(new City("清远市"));
                cities.add(new City("东莞市"));
                cities.add(new City("中山市"));
                cities.add(new City("潮州市"));
                cities.add(new City("揭阳市"));
                cities.add(new City("云浮市"));
                break;
            case "浙江省":
                cities.add(new City("杭州市"));
                cities.add(new City("宁波市"));
                cities.add(new City("温州市"));
                cities.add(new City("嘉兴市"));
                cities.add(new City("湖州市"));
                cities.add(new City("绍兴市"));
                cities.add(new City("金华市"));
                cities.add(new City("衢州市"));
                cities.add(new City("舟山市"));
                cities.add(new City("台州市"));
                cities.add(new City("丽水市"));
                break;
            case "江苏省":
                cities.add(new City("南京市"));
                cities.add(new City("无锡市"));
                cities.add(new City("徐州市"));
                cities.add(new City("常州市"));
                cities.add(new City("苏州市"));
                cities.add(new City("南通市"));
                cities.add(new City("连云港市"));
                cities.add(new City("淮安市"));
                cities.add(new City("盐城市"));
                cities.add(new City("扬州市"));
                cities.add(new City("镇江市"));
                cities.add(new City("泰州市"));
                cities.add(new City("宿迁市"));
                break;
            case "山西省":
                cities.add(new City("太原市"));
                cities.add(new City("大同市"));
                cities.add(new City("阳泉市"));
                cities.add(new City("长治市"));
                cities.add(new City("晋城市"));
                cities.add(new City("朔州市"));
                cities.add(new City("晋中市"));
                cities.add(new City("运城市"));
                cities.add(new City("忻州市"));
                cities.add(new City("临汾市"));
                cities.add(new City("吕梁市"));
                break;
            case "内蒙古自治区":
                cities.add(new City("呼和浩特市"));
                cities.add(new City("包头市"));
                cities.add(new City("乌海市"));
                cities.add(new City("赤峰市"));
                cities.add(new City("通辽市"));
                cities.add(new City("鄂尔多斯市"));
                cities.add(new City("呼伦贝尔市"));
                cities.add(new City("巴彦淖尔市"));
                cities.add(new City("乌兰察布市"));
                cities.add(new City("兴安盟"));
                cities.add(new City("锡林郭勒盟"));
                cities.add(new City("阿拉善盟"));
                break;
            case "辽宁省":
                cities.add(new City("沈阳市"));
                cities.add(new City("大连市"));
                cities.add(new City("鞍山市"));
                cities.add(new City("抚顺市"));
                cities.add(new City("本溪市"));
                cities.add(new City("丹东市"));
                cities.add(new City("锦州市"));
                cities.add(new City("营口市"));
                cities.add(new City("阜新市"));
                cities.add(new City("辽阳市"));
                cities.add(new City("盘锦市"));
                cities.add(new City("铁岭市"));
                cities.add(new City("朝阳市"));
                cities.add(new City("葫芦岛市"));
                break;
            case "吉林省":
                cities.add(new City("长春市"));
                cities.add(new City("吉林市"));
                cities.add(new City("四平市"));
                cities.add(new City("辽源市"));
                cities.add(new City("通化市"));
                cities.add(new City("白山市"));
                cities.add(new City("松原市"));
                cities.add(new City("白城市"));
                cities.add(new City("延边朝鲜族自治州"));
                break;
            case "黑龙江省":
                cities.add(new City("哈尔滨市"));
                cities.add(new City("齐齐哈尔市"));
                cities.add(new City("鸡西市"));
                cities.add(new City("鹤岗市"));
                cities.add(new City("双鸭山市"));
                cities.add(new City("大庆市"));
                cities.add(new City("伊春市"));
                cities.add(new City("佳木斯市"));
                cities.add(new City("七台河市"));
                cities.add(new City("牡丹江市"));
                cities.add(new City("黑河市"));
                cities.add(new City("绥化市"));
                cities.add(new City("大兴安岭地区"));
                break;
            case "安徽省":
                cities.add(new City("合肥市"));
                cities.add(new City("芜湖市"));
                cities.add(new City("蚌埠市"));
                cities.add(new City("淮南市"));
                cities.add(new City("马鞍山市"));
                cities.add(new City("淮北市"));
                cities.add(new City("铜陵市"));
                cities.add(new City("安庆市"));
                cities.add(new City("黄山市"));
                cities.add(new City("滁州市"));
                cities.add(new City("阜阳市"));
                cities.add(new City("宿州市"));
                cities.add(new City("六安市"));
                cities.add(new City("亳州市"));
                cities.add(new City("池州市"));
                cities.add(new City("宣城市"));
                break;
            case "福建省":
                cities.add(new City("福州市"));
                cities.add(new City("厦门市"));
                cities.add(new City("莆田市"));
                cities.add(new City("三明市"));
                cities.add(new City("泉州市"));
                cities.add(new City("漳州市"));
                cities.add(new City("南平市"));
                cities.add(new City("龙岩市"));
                cities.add(new City("宁德市"));
                break;
            case "江西省":
                cities.add(new City("南昌市"));
                cities.add(new City("景德镇市"));
                cities.add(new City("萍乡市"));
                cities.add(new City("九江市"));
                cities.add(new City("新余市"));
                cities.add(new City("鹰潭市"));
                cities.add(new City("赣州市"));
                cities.add(new City("吉安市"));
                cities.add(new City("宜春市"));
                cities.add(new City("抚州市"));
                cities.add(new City("上饶市"));
                break;
            case "山东省":
                cities.add(new City("济南市"));
                cities.add(new City("青岛市"));
                cities.add(new City("淄博市"));
                cities.add(new City("枣庄市"));
                cities.add(new City("东营市"));
                cities.add(new City("烟台市"));
                cities.add(new City("潍坊市"));
                cities.add(new City("济宁市"));
                cities.add(new City("泰安市"));
                cities.add(new City("威海市"));
                cities.add(new City("日照市"));
                cities.add(new City("临沂市"));
                cities.add(new City("德州市"));
                cities.add(new City("聊城市"));
                cities.add(new City("滨州市"));
                cities.add(new City("菏泽市"));
                break;
            case "河南省":
                cities.add(new City("郑州市"));
                cities.add(new City("开封市"));
                cities.add(new City("洛阳市"));
                cities.add(new City("平顶山市"));
                cities.add(new City("安阳市"));
                cities.add(new City("鹤壁市"));
                cities.add(new City("新乡市"));
                cities.add(new City("焦作市"));
                cities.add(new City("濮阳市"));
                cities.add(new City("许昌市"));
                cities.add(new City("漯河市"));
                cities.add(new City("三门峡市"));
                cities.add(new City("南阳市"));
                cities.add(new City("商丘市"));
                cities.add(new City("信阳市"));
                cities.add(new City("周口市"));
                cities.add(new City("驻马店市"));
                break;
            case "湖北省":
                cities.add(new City("武汉市"));
                cities.add(new City("黄石市"));
                cities.add(new City("十堰市"));
                cities.add(new City("宜昌市"));
                cities.add(new City("襄阳市"));
                cities.add(new City("鄂州市"));
                cities.add(new City("荆门市"));
                cities.add(new City("孝感市"));
                cities.add(new City("荆州市"));
                cities.add(new City("黄冈市"));
                cities.add(new City("咸宁市"));
                cities.add(new City("随州市"));
                cities.add(new City("恩施土家族苗族自治州"));
                break;
            case "湖南省":
                cities.add(new City("长沙市"));
                cities.add(new City("株洲市"));
                cities.add(new City("湘潭市"));
                cities.add(new City("衡阳市"));
                cities.add(new City("邵阳市"));
                cities.add(new City("岳阳市"));
                cities.add(new City("常德市"));
                cities.add(new City("张家界市"));
                cities.add(new City("益阳市"));
                cities.add(new City("郴州市"));
                cities.add(new City("永州市"));
                cities.add(new City("怀化市"));
                cities.add(new City("娄底市"));
                cities.add(new City("湘西土家族苗族自治州"));
                break;
            case "广西壮族自治区":
                cities.add(new City("南宁市"));
                cities.add(new City("柳州市"));
                cities.add(new City("桂林市"));
                cities.add(new City("梧州市"));
                cities.add(new City("北海市"));
                cities.add(new City("防城港市"));
                cities.add(new City("钦州市"));
                cities.add(new City("贵港市"));
                cities.add(new City("玉林市"));
                cities.add(new City("百色市"));
                cities.add(new City("贺州市"));
                cities.add(new City("河池市"));
                cities.add(new City("来宾市"));
                cities.add(new City("崇左市"));
                break;
            case "海南省":
                cities.add(new City("海口市"));
                cities.add(new City("三亚市"));
                cities.add(new City("三沙市"));
                cities.add(new City("儋州市"));
                break;
            case "四川省":
                cities.add(new City("成都市"));
                cities.add(new City("自贡市"));
                cities.add(new City("攀枝花市"));
                cities.add(new City("泸州市"));
                cities.add(new City("德阳市"));
                cities.add(new City("绵阳市"));
                cities.add(new City("广元市"));
                cities.add(new City("遂宁市"));
                cities.add(new City("内江市"));
                cities.add(new City("乐山市"));
                cities.add(new City("南充市"));
                cities.add(new City("眉山市"));
                cities.add(new City("宜宾市"));
                cities.add(new City("广安市"));
                cities.add(new City("达州市"));
                cities.add(new City("雅安市"));
                cities.add(new City("巴中市"));
                cities.add(new City("资阳市"));
                cities.add(new City("阿坝藏族羌族自治州"));
                cities.add(new City("甘孜藏族自治州"));
                cities.add(new City("凉山彝族自治州"));
                break;
            case "贵州省":
                cities.add(new City("贵阳市"));
                cities.add(new City("六盘水市"));
                cities.add(new City("遵义市"));
                cities.add(new City("安顺市"));
                cities.add(new City("毕节市"));
                cities.add(new City("铜仁市"));
                cities.add(new City("黔西南布依族苗族自治州"));
                cities.add(new City("黔东南苗族侗族自治州"));
                cities.add(new City("黔南布依族苗族自治州"));
                break;
            case "云南省":
                cities.add(new City("昆明市"));
                cities.add(new City("曲靖市"));
                cities.add(new City("玉溪市"));
                cities.add(new City("保山市"));
                cities.add(new City("昭通市"));
                cities.add(new City("丽江市"));
                cities.add(new City("普洱市"));
                cities.add(new City("临沧市"));
                cities.add(new City("楚雄彝族自治州"));
                cities.add(new City("红河哈尼族彝族自治州"));
                cities.add(new City("文山壮族苗族自治州"));
                cities.add(new City("西双版纳傣族自治州"));
                cities.add(new City("大理白族自治州"));
                cities.add(new City("德宏傣族景颇族自治州"));
                cities.add(new City("怒江傈僳族自治州"));
                cities.add(new City("迪庆藏族自治州"));
                break;
            case "西藏自治区":
                cities.add(new City("拉萨市"));
                cities.add(new City("日喀则市"));
                cities.add(new City("昌都市"));
                cities.add(new City("林芝市"));
                cities.add(new City("山南市"));
                cities.add(new City("那曲市"));
                cities.add(new City("阿里地区"));
                break;
            case "陕西省":
                cities.add(new City("西安市"));
                cities.add(new City("铜川市"));
                cities.add(new City("宝鸡市"));
                cities.add(new City("咸阳市"));
                cities.add(new City("渭南市"));
                cities.add(new City("延安市"));
                cities.add(new City("汉中市"));
                cities.add(new City("榆林市"));
                cities.add(new City("安康市"));
                cities.add(new City("商洛市"));
                break;
            case "甘肃省":
                cities.add(new City("兰州市"));
                cities.add(new City("嘉峪关市"));
                cities.add(new City("金昌市"));
                cities.add(new City("白银市"));
                cities.add(new City("天水市"));
                cities.add(new City("武威市"));
                cities.add(new City("张掖市"));
                cities.add(new City("平凉市"));
                cities.add(new City("酒泉市"));
                cities.add(new City("庆阳市"));
                cities.add(new City("定西市"));
                cities.add(new City("陇南市"));
                cities.add(new City("临夏回族自治州"));
                cities.add(new City("甘南藏族自治州"));
                break;
            case "青海省":
                cities.add(new City("西宁市"));
                cities.add(new City("海东市"));
                cities.add(new City("海北藏族自治州"));
                cities.add(new City("黄南藏族自治州"));
                cities.add(new City("海南藏族自治州"));
                cities.add(new City("果洛藏族自治州"));
                cities.add(new City("玉树藏族自治州"));
                cities.add(new City("海西蒙古族藏族自治州"));
                break;
            case "宁夏回族自治区":
                cities.add(new City("银川市"));
                cities.add(new City("石嘴山市"));
                cities.add(new City("吴忠市"));
                cities.add(new City("固原市"));
                cities.add(new City("中卫市"));
                break;
            case "新疆维吾尔自治区":
                cities.add(new City("乌鲁木齐市"));
                cities.add(new City("克拉玛依市"));
                cities.add(new City("吐鲁番市"));
                cities.add(new City("哈密市"));
                cities.add(new City("昌吉回族自治州"));
                cities.add(new City("博尔塔拉蒙古自治州"));
                cities.add(new City("巴音郭楞蒙古自治州"));
                cities.add(new City("阿克苏地区"));
                cities.add(new City("克孜勒苏柯尔克孜自治州"));
                cities.add(new City("喀什地区"));
                cities.add(new City("和田地区"));
                cities.add(new City("伊犁哈萨克自治州"));
                cities.add(new City("塔城地区"));
                cities.add(new City("阿勒泰地区"));
                break;
        }
        return cities;
    }

    /**
     * 根据城市名获取区/县列表
     */
    private List<City> getDistrictList(String city) {
        List<City> districts = new ArrayList<>();
        switch (city) {
            // === 直辖市 ===
            case "北京市":
                districts.add(new City("东城区"));
                districts.add(new City("西城区"));
                districts.add(new City("朝阳区"));
                districts.add(new City("丰台区"));
                districts.add(new City("石景山区"));
                districts.add(new City("海淀区"));
                districts.add(new City("门头沟区"));
                districts.add(new City("房山区"));
                districts.add(new City("通州区"));
                districts.add(new City("顺义区"));
                districts.add(new City("昌平区"));
                districts.add(new City("大兴区"));
                districts.add(new City("怀柔区"));
                districts.add(new City("平谷区"));
                districts.add(new City("密云区"));
                districts.add(new City("延庆区"));
                break;
            case "天津市":
                districts.add(new City("和平区"));
                districts.add(new City("河东区"));
                districts.add(new City("河西区"));
                districts.add(new City("南开区"));
                districts.add(new City("河北区"));
                districts.add(new City("红桥区"));
                districts.add(new City("东丽区"));
                districts.add(new City("西青区"));
                districts.add(new City("津南区"));
                districts.add(new City("北辰区"));
                districts.add(new City("武清区"));
                districts.add(new City("宝坻区"));
                districts.add(new City("滨海新区"));
                districts.add(new City("宁河区"));
                districts.add(new City("静海区"));
                districts.add(new City("蓟州区"));
                break;
            case "上海市":
                districts.add(new City("黄浦区"));
                districts.add(new City("徐汇区"));
                districts.add(new City("长宁区"));
                districts.add(new City("静安区"));
                districts.add(new City("普陀区"));
                districts.add(new City("虹口区"));
                districts.add(new City("杨浦区"));
                districts.add(new City("闵行区"));
                districts.add(new City("宝山区"));
                districts.add(new City("嘉定区"));
                districts.add(new City("浦东新区"));
                districts.add(new City("金山区"));
                districts.add(new City("松江区"));
                districts.add(new City("青浦区"));
                districts.add(new City("奉贤区"));
                districts.add(new City("崇明区"));
                break;
            case "重庆市":
                districts.add(new City("万州区"));
                districts.add(new City("涪陵区"));
                districts.add(new City("渝中区"));
                districts.add(new City("大渡口区"));
                districts.add(new City("江北区"));
                districts.add(new City("沙坪坝区"));
                districts.add(new City("九龙坡区"));
                districts.add(new City("南岸区"));
                districts.add(new City("北碚区"));
                districts.add(new City("渝北区"));
                districts.add(new City("巴南区"));
                districts.add(new City("长寿区"));
                districts.add(new City("江津区"));
                districts.add(new City("合川区"));
                districts.add(new City("永川区"));
                districts.add(new City("南川区"));
                districts.add(new City("綦江区"));
                districts.add(new City("大足区"));
                districts.add(new City("璧山区"));
                districts.add(new City("铜梁区"));
                districts.add(new City("潼南区"));
                districts.add(new City("荣昌区"));
                districts.add(new City("开州区"));
                districts.add(new City("梁平区"));
                districts.add(new City("武隆区"));
                break;
            // === 省会及主要城市 ===
            case "广州市":
                districts.add(new City("荔湾区"));
                districts.add(new City("越秀区"));
                districts.add(new City("海珠区"));
                districts.add(new City("天河区"));
                districts.add(new City("白云区"));
                districts.add(new City("黄埔区"));
                districts.add(new City("番禺区"));
                districts.add(new City("花都区"));
                districts.add(new City("南沙区"));
                districts.add(new City("从化区"));
                districts.add(new City("增城区"));
                break;
            case "深圳市":
                districts.add(new City("罗湖区"));
                districts.add(new City("福田区"));
                districts.add(new City("南山区"));
                districts.add(new City("宝安区"));
                districts.add(new City("龙岗区"));
                districts.add(new City("盐田区"));
                districts.add(new City("龙华区"));
                districts.add(new City("坪山区"));
                districts.add(new City("光明区"));
                break;
            case "杭州市":
                districts.add(new City("上城区"));
                districts.add(new City("下城区"));
                districts.add(new City("江干区"));
                districts.add(new City("拱墅区"));
                districts.add(new City("西湖区"));
                districts.add(new City("滨江区"));
                districts.add(new City("萧山区"));
                districts.add(new City("余杭区"));
                districts.add(new City("富阳区"));
                districts.add(new City("临安区"));
                districts.add(new City("临平区"));
                districts.add(new City("钱塘区"));
                break;
            case "南京市":
                districts.add(new City("玄武区"));
                districts.add(new City("秦淮区"));
                districts.add(new City("建邺区"));
                districts.add(new City("鼓楼区"));
                districts.add(new City("浦口区"));
                districts.add(new City("栖霞区"));
                districts.add(new City("雨花台区"));
                districts.add(new City("江宁区"));
                districts.add(new City("六合区"));
                districts.add(new City("溧水区"));
                districts.add(new City("高淳区"));
                break;
            case "武汉市":
                districts.add(new City("江岸区"));
                districts.add(new City("江汉区"));
                districts.add(new City("硚口区"));
                districts.add(new City("汉阳区"));
                districts.add(new City("武昌区"));
                districts.add(new City("青山区"));
                districts.add(new City("洪山区"));
                districts.add(new City("东西湖区"));
                districts.add(new City("蔡甸区"));
                districts.add(new City("江夏区"));
                districts.add(new City("黄陂区"));
                districts.add(new City("新洲区"));
                districts.add(new City("汉南区"));
                break;
            case "成都市":
                districts.add(new City("锦江区"));
                districts.add(new City("青羊区"));
                districts.add(new City("金牛区"));
                districts.add(new City("武侯区"));
                districts.add(new City("成华区"));
                districts.add(new City("龙泉驿区"));
                districts.add(new City("青白江区"));
                districts.add(new City("新都区"));
                districts.add(new City("温江区"));
                districts.add(new City("双流区"));
                districts.add(new City("郫都区"));
                districts.add(new City("新津区"));
                break;
            case "西安市":
                districts.add(new City("新城区"));
                districts.add(new City("碑林区"));
                districts.add(new City("莲湖区"));
                districts.add(new City("灞桥区"));
                districts.add(new City("未央区"));
                districts.add(new City("雁塔区"));
                districts.add(new City("阎良区"));
                districts.add(new City("临潼区"));
                districts.add(new City("长安区"));
                districts.add(new City("高陵区"));
                districts.add(new City("鄠邑区"));
                break;
            case "郑州市":
                districts.add(new City("中原区"));
                districts.add(new City("二七区"));
                districts.add(new City("管城回族区"));
                districts.add(new City("金水区"));
                districts.add(new City("上街区"));
                districts.add(new City("惠济区"));
                districts.add(new City("中牟县"));
                districts.add(new City("巩义市"));
                districts.add(new City("荥阳市"));
                districts.add(new City("新密市"));
                districts.add(new City("新郑市"));
                districts.add(new City("登封市"));
                break;
            case "长沙市":
                districts.add(new City("芙蓉区"));
                districts.add(new City("天心区"));
                districts.add(new City("岳麓区"));
                districts.add(new City("开福区"));
                districts.add(new City("雨花区"));
                districts.add(new City("望城区"));
                districts.add(new City("长沙县"));
                districts.add(new City("浏阳市"));
                districts.add(new City("宁乡市"));
                break;
            case "济南市":
                districts.add(new City("历下区"));
                districts.add(new City("市中区"));
                districts.add(new City("槐荫区"));
                districts.add(new City("天桥区"));
                districts.add(new City("历城区"));
                districts.add(new City("长清区"));
                districts.add(new City("章丘区"));
                districts.add(new City("济阳区"));
                districts.add(new City("莱芜区"));
                districts.add(new City("钢城区"));
                break;
            case "青岛市":
                districts.add(new City("市南区"));
                districts.add(new City("市北区"));
                districts.add(new City("黄岛区"));
                districts.add(new City("崂山区"));
                districts.add(new City("李沧区"));
                districts.add(new City("城阳区"));
                districts.add(new City("即墨区"));
                districts.add(new City("胶州市"));
                districts.add(new City("平度市"));
                districts.add(new City("莱西市"));
                break;
            case "哈尔滨市":
                districts.add(new City("道里区"));
                districts.add(new City("南岗区"));
                districts.add(new City("道外区"));
                districts.add(new City("平房区"));
                districts.add(new City("松北区"));
                districts.add(new City("香坊区"));
                districts.add(new City("呼兰区"));
                districts.add(new City("阿城区"));
                districts.add(new City("双城区"));
                break;
            case "长春市":
                districts.add(new City("南关区"));
                districts.add(new City("宽城区"));
                districts.add(new City("朝阳区"));
                districts.add(new City("二道区"));
                districts.add(new City("绿园区"));
                districts.add(new City("双阳区"));
                districts.add(new City("九台区"));
                break;
            case "沈阳市":
                districts.add(new City("和平区"));
                districts.add(new City("沈河区"));
                districts.add(new City("大东区"));
                districts.add(new City("皇姑区"));
                districts.add(new City("铁西区"));
                districts.add(new City("苏家屯区"));
                districts.add(new City("浑南区"));
                districts.add(new City("沈北新区"));
                districts.add(new City("于洪区"));
                break;
            case "大连市":
                districts.add(new City("中山区"));
                districts.add(new City("西岗区"));
                districts.add(new City("沙河口区"));
                districts.add(new City("甘井子区"));
                districts.add(new City("旅顺口区"));
                districts.add(new City("金州区"));
                districts.add(new City("普兰店区"));
                break;
            case "合肥市":
                districts.add(new City("瑶海区"));
                districts.add(new City("庐阳区"));
                districts.add(new City("蜀山区"));
                districts.add(new City("包河区"));
                districts.add(new City("长丰县"));
                districts.add(new City("肥东县"));
                districts.add(new City("肥西县"));
                districts.add(new City("庐江县"));
                districts.add(new City("巢湖市"));
                break;
            case "福州市":
                districts.add(new City("鼓楼区"));
                districts.add(new City("台江区"));
                districts.add(new City("仓山区"));
                districts.add(new City("马尾区"));
                districts.add(new City("晋安区"));
                districts.add(new City("长乐区"));
                break;
            case "昆明市":
                districts.add(new City("五华区"));
                districts.add(new City("盘龙区"));
                districts.add(new City("官渡区"));
                districts.add(new City("西山区"));
                districts.add(new City("东川区"));
                districts.add(new City("呈贡区"));
                districts.add(new City("晋宁区"));
                break;
            case "石家庄市":
                districts.add(new City("长安区"));
                districts.add(new City("桥西区"));
                districts.add(new City("新华区"));
                districts.add(new City("井陉矿区"));
                districts.add(new City("裕华区"));
                districts.add(new City("藁城区"));
                districts.add(new City("鹿泉区"));
                districts.add(new City("栾城区"));
                break;
            case "兰州市":
                districts.add(new City("城关区"));
                districts.add(new City("七里河区"));
                districts.add(new City("西固区"));
                districts.add(new City("安宁区"));
                districts.add(new City("红古区"));
                break;
            case "呼和浩特市":
                districts.add(new City("新城区"));
                districts.add(new City("回民区"));
                districts.add(new City("玉泉区"));
                districts.add(new City("赛罕区"));
                break;
            case "南宁市":
                districts.add(new City("兴宁区"));
                districts.add(new City("青秀区"));
                districts.add(new City("江南区"));
                districts.add(new City("西乡塘区"));
                districts.add(new City("良庆区"));
                districts.add(new City("邕宁区"));
                break;
            case "银川市":
                districts.add(new City("兴庆区"));
                districts.add(new City("西夏区"));
                districts.add(new City("金凤区"));
                break;
            case "西宁市":
                districts.add(new City("城东区"));
                districts.add(new City("城中区"));
                districts.add(new City("城西区"));
                districts.add(new City("城北区"));
                break;
            case "拉萨市":
                districts.add(new City("城关区"));
                districts.add(new City("堆龙德庆区"));
                districts.add(new City("达孜区"));
                break;
            case "乌鲁木齐市":
                districts.add(new City("天山区"));
                districts.add(new City("沙依巴克区"));
                districts.add(new City("新市区"));
                districts.add(new City("水磨沟区"));
                districts.add(new City("头屯河区"));
                districts.add(new City("达坂城区"));
                districts.add(new City("米东区"));
                break;
            case "海口市":
                districts.add(new City("秀英区"));
                districts.add(new City("龙华区"));
                districts.add(new City("琼山区"));
                districts.add(new City("美兰区"));
                break;
            case "三亚市":
                districts.add(new City("海棠区"));
                districts.add(new City("吉阳区"));
                districts.add(new City("天涯区"));
                districts.add(new City("崖州区"));
                break;
            case "苏州市":
                districts.add(new City("虎丘区"));
                districts.add(new City("吴中区"));
                districts.add(new City("相城区"));
                districts.add(new City("姑苏区"));
                districts.add(new City("吴江区"));
                districts.add(new City("苏州工业园区"));
                districts.add(new City("常熟市"));
                districts.add(new City("张家港市"));
                districts.add(new City("昆山市"));
                districts.add(new City("太仓市"));
                break;
            case "无锡市":
                districts.add(new City("锡山区"));
                districts.add(new City("惠山区"));
                districts.add(new City("滨湖区"));
                districts.add(new City("梁溪区"));
                districts.add(new City("新吴区"));
                districts.add(new City("江阴市"));
                districts.add(new City("宜兴市"));
                break;
            case "宁波市":
                districts.add(new City("海曙区"));
                districts.add(new City("江北区"));
                districts.add(new City("北仑区"));
                districts.add(new City("镇海区"));
                districts.add(new City("鄞州区"));
                districts.add(new City("奉化区"));
                break;
            case "温州市":
                districts.add(new City("鹿城区"));
                districts.add(new City("龙湾区"));
                districts.add(new City("瓯海区"));
                districts.add(new City("洞头区"));
                districts.add(new City("瑞安市"));
                districts.add(new City("乐清市"));
                break;
            case "厦门市":
                districts.add(new City("思明区"));
                districts.add(new City("海沧区"));
                districts.add(new City("湖里区"));
                districts.add(new City("集美区"));
                districts.add(new City("同安区"));
                districts.add(new City("翔安区"));
                break;
            case "泉州市":
                districts.add(new City("鲤城区"));
                districts.add(new City("丰泽区"));
                districts.add(new City("洛江区"));
                districts.add(new City("泉港区"));
                districts.add(new City("晋江市"));
                districts.add(new City("石狮市"));
                districts.add(new City("南安市"));
                break;
            case "东莞市":
                districts.add(new City("莞城街道"));
                districts.add(new City("南城街道"));
                districts.add(new City("东城街道"));
                districts.add(new City("万江街道"));
                break;
            case "佛山市":
                districts.add(new City("禅城区"));
                districts.add(new City("南海区"));
                districts.add(new City("顺德区"));
                districts.add(new City("三水区"));
                districts.add(new City("高明区"));
                break;
            case "烟台市":
                districts.add(new City("芝罘区"));
                districts.add(new City("福山区"));
                districts.add(new City("牟平区"));
                districts.add(new City("莱山区"));
                districts.add(new City("蓬莱区"));
                break;
            case "潍坊市":
                districts.add(new City("潍城区"));
                districts.add(new City("寒亭区"));
                districts.add(new City("坊子区"));
                districts.add(new City("奎文区"));
                break;
            case "徐州市":
                districts.add(new City("鼓楼区"));
                districts.add(new City("云龙区"));
                districts.add(new City("贾汪区"));
                districts.add(new City("泉山区"));
                districts.add(new City("铜山区"));
                break;
            case "常州市":
                districts.add(new City("天宁区"));
                districts.add(new City("钟楼区"));
                districts.add(new City("新北区"));
                districts.add(new City("武进区"));
                districts.add(new City("金坛区"));
                break;
            case "南通市":
                districts.add(new City("崇川区"));
                districts.add(new City("通州区"));
                districts.add(new City("海门区"));
                break;
            case "绍兴市":
                districts.add(new City("越城区"));
                districts.add(new City("柯桥区"));
                districts.add(new City("上虞区"));
                break;
            case "金华市":
                districts.add(new City("婺城区"));
                districts.add(new City("金东区"));
                districts.add(new City("义乌市"));
                districts.add(new City("东阳市"));
                break;
            case "嘉兴市":
                districts.add(new City("南湖区"));
                districts.add(new City("秀洲区"));
                districts.add(new City("海宁市"));
                districts.add(new City("平湖市"));
                break;
            case "洛阳市":
                districts.add(new City("老城区"));
                districts.add(new City("西工区"));
                districts.add(new City("瀍河回族区"));
                districts.add(new City("涧西区"));
                districts.add(new City("洛龙区"));
                break;
            case "唐山市":
                districts.add(new City("路南区"));
                districts.add(new City("路北区"));
                districts.add(new City("古冶区"));
                districts.add(new City("开平区"));
                districts.add(new City("丰南区"));
                districts.add(new City("丰润区"));
                break;
            case "保定市":
                districts.add(new City("竞秀区"));
                districts.add(new City("莲池区"));
                districts.add(new City("满城区"));
                districts.add(new City("清苑区"));
                districts.add(new City("徐水区"));
                break;
            case "邯郸市":
                districts.add(new City("邯山区"));
                districts.add(new City("丛台区"));
                districts.add(new City("复兴区"));
                districts.add(new City("峰峰矿区"));
                districts.add(new City("肥乡区"));
                districts.add(new City("永年区"));
                break;
            case "南昌市":
                districts.add(new City("东湖区"));
                districts.add(new City("西湖区"));
                districts.add(new City("青云谱区"));
                districts.add(new City("青山湖区"));
                districts.add(new City("新建区"));
                districts.add(new City("红谷滩区"));
                break;
            case "贵阳市":
                districts.add(new City("南明区"));
                districts.add(new City("云岩区"));
                districts.add(new City("花溪区"));
                districts.add(new City("乌当区"));
                districts.add(new City("白云区"));
                districts.add(new City("观山湖区"));
                break;
            case "太原市":
                districts.add(new City("小店区"));
                districts.add(new City("迎泽区"));
                districts.add(new City("杏花岭区"));
                districts.add(new City("尖草坪区"));
                districts.add(new City("万柏林区"));
                districts.add(new City("晋源区"));
                break;
            // === 广东省 ===
            case "韶关市":
                districts.add(new City("武江区"));
                districts.add(new City("浈江区"));
                districts.add(new City("曲江区"));
                break;
            case "珠海市":
                districts.add(new City("香洲区"));
                districts.add(new City("斗门区"));
                districts.add(new City("金湾区"));
                break;
            case "汕头市":
                districts.add(new City("龙湖区"));
                districts.add(new City("金平区"));
                districts.add(new City("濠江区"));
                districts.add(new City("潮阳区"));
                districts.add(new City("潮南区"));
                districts.add(new City("澄海区"));
                break;
            case "江门市":
                districts.add(new City("蓬江区"));
                districts.add(new City("江海区"));
                districts.add(new City("新会区"));
                break;
            case "湛江市":
                districts.add(new City("赤坎区"));
                districts.add(new City("霞山区"));
                districts.add(new City("坡头区"));
                districts.add(new City("麻章区"));
                break;
            case "茂名市":
                districts.add(new City("茂南区"));
                districts.add(new City("电白区"));
                break;
            case "肇庆市":
                districts.add(new City("端州区"));
                districts.add(new City("鼎湖区"));
                districts.add(new City("高要区"));
                break;
            case "惠州市":
                districts.add(new City("惠城区"));
                districts.add(new City("惠阳区"));
                break;
            case "梅州市":
                districts.add(new City("梅江区"));
                districts.add(new City("梅县区"));
                break;
            case "汕尾市":
                districts.add(new City("城区"));
                break;
            case "河源市":
                districts.add(new City("源城区"));
                break;
            case "阳江市":
                districts.add(new City("江城区"));
                districts.add(new City("阳东区"));
                break;
            case "清远市":
                districts.add(new City("清城区"));
                districts.add(new City("清新区"));
                break;
            case "中山市":
                districts.add(new City("石岐街道"));
                districts.add(new City("东区街道"));
                districts.add(new City("西区街道"));
                districts.add(new City("南区街道"));
                break;
            case "潮州市":
                districts.add(new City("湘桥区"));
                districts.add(new City("潮安区"));
                break;
            case "揭阳市":
                districts.add(new City("榕城区"));
                districts.add(new City("揭东区"));
                break;
            case "云浮市":
                districts.add(new City("云城区"));
                districts.add(new City("云安区"));
                break;
            // === 浙江省 ===
            case "湖州市":
                districts.add(new City("吴兴区"));
                districts.add(new City("南浔区"));
                break;
            case "衢州市":
                districts.add(new City("柯城区"));
                districts.add(new City("衢江区"));
                break;
            case "舟山市":
                districts.add(new City("定海区"));
                districts.add(new City("普陀区"));
                break;
            case "台州市":
                districts.add(new City("椒江区"));
                districts.add(new City("黄岩区"));
                districts.add(new City("路桥区"));
                break;
            case "丽水市":
                districts.add(new City("莲都区"));
                break;
            // === 江苏省 ===
            case "连云港市":
                districts.add(new City("连云区"));
                districts.add(new City("海州区"));
                districts.add(new City("赣榆区"));
                break;
            case "淮安市":
                districts.add(new City("淮安区"));
                districts.add(new City("淮阴区"));
                districts.add(new City("清江浦区"));
                districts.add(new City("洪泽区"));
                break;
            case "盐城市":
                districts.add(new City("亭湖区"));
                districts.add(new City("盐都区"));
                districts.add(new City("大丰区"));
                break;
            case "扬州市":
                districts.add(new City("广陵区"));
                districts.add(new City("邗江区"));
                districts.add(new City("江都区"));
                break;
            case "镇江市":
                districts.add(new City("京口区"));
                districts.add(new City("润州区"));
                districts.add(new City("丹徒区"));
                break;
            case "泰州市":
                districts.add(new City("海陵区"));
                districts.add(new City("高港区"));
                districts.add(new City("姜堰区"));
                break;
            case "宿迁市":
                districts.add(new City("宿城区"));
                districts.add(new City("宿豫区"));
                break;
            // === 山东省 ===
            case "淄博市":
                districts.add(new City("淄川区"));
                districts.add(new City("张店区"));
                districts.add(new City("博山区"));
                districts.add(new City("临淄区"));
                districts.add(new City("周村区"));
                districts.add(new City("桓台县"));
                districts.add(new City("高青县"));
                districts.add(new City("沂源县"));
                break;
            case "枣庄市":
                districts.add(new City("市中区"));
                districts.add(new City("薛城区"));
                districts.add(new City("峄城区"));
                districts.add(new City("台儿庄区"));
                districts.add(new City("山亭区"));
                districts.add(new City("滕州市"));
                break;
            case "东营市":
                districts.add(new City("东营区"));
                districts.add(new City("河口区"));
                districts.add(new City("垦利区"));
                districts.add(new City("利津县"));
                districts.add(new City("广饶县"));
                break;
            case "济宁市":
                districts.add(new City("任城区"));
                districts.add(new City("兖州区"));
                districts.add(new City("微山县"));
                districts.add(new City("鱼台县"));
                districts.add(new City("金乡县"));
                districts.add(new City("嘉祥县"));
                districts.add(new City("汶上县"));
                districts.add(new City("泗水县"));
                districts.add(new City("梁山县"));
                districts.add(new City("曲阜市"));
                districts.add(new City("邹城市"));
                break;
            case "泰安市":
                districts.add(new City("泰山区"));
                districts.add(new City("岱岳区"));
                districts.add(new City("宁阳县"));
                districts.add(new City("东平县"));
                districts.add(new City("新泰市"));
                districts.add(new City("肥城市"));
                break;
            case "威海市":
                districts.add(new City("环翠区"));
                districts.add(new City("文登区"));
                districts.add(new City("荣成市"));
                districts.add(new City("乳山市"));
                break;
            case "日照市":
                districts.add(new City("东港区"));
                districts.add(new City("岚山区"));
                districts.add(new City("五莲县"));
                districts.add(new City("莒县"));
                break;
            case "临沂市":
                districts.add(new City("兰山区"));
                districts.add(new City("罗庄区"));
                districts.add(new City("河东区"));
                districts.add(new City("沂南县"));
                districts.add(new City("郯城县"));
                districts.add(new City("沂水县"));
                districts.add(new City("兰陵县"));
                districts.add(new City("费县"));
                districts.add(new City("平邑县"));
                districts.add(new City("莒南县"));
                districts.add(new City("蒙阴县"));
                districts.add(new City("临沭县"));
                break;
            case "德州市":
                districts.add(new City("德城区"));
                districts.add(new City("陵城区"));
                districts.add(new City("宁津县"));
                districts.add(new City("庆云县"));
                districts.add(new City("临邑县"));
                districts.add(new City("齐河县"));
                districts.add(new City("平原县"));
                districts.add(new City("夏津县"));
                districts.add(new City("武城县"));
                districts.add(new City("乐陵市"));
                districts.add(new City("禹城市"));
                break;
            case "聊城市":
                districts.add(new City("东昌府区"));
                districts.add(new City("茌平区"));
                districts.add(new City("阳谷县"));
                districts.add(new City("莘县"));
                districts.add(new City("东阿县"));
                districts.add(new City("冠县"));
                districts.add(new City("高唐县"));
                districts.add(new City("临清市"));
                break;
            case "滨州市":
                districts.add(new City("滨城区"));
                districts.add(new City("沾化区"));
                districts.add(new City("惠民县"));
                districts.add(new City("阳信县"));
                districts.add(new City("无棣县"));
                districts.add(new City("博兴县"));
                districts.add(new City("邹平市"));
                break;
            case "菏泽市":
                districts.add(new City("牡丹区"));
                districts.add(new City("定陶区"));
                districts.add(new City("曹县"));
                districts.add(new City("单县"));
                districts.add(new City("成武县"));
                districts.add(new City("巨野县"));
                districts.add(new City("郓城县"));
                districts.add(new City("鄄城县"));
                districts.add(new City("东明县"));
                break;
            // === 河北省 ===
            case "秦皇岛市":
                districts.add(new City("海港区"));
                districts.add(new City("山海关区"));
                districts.add(new City("北戴河区"));
                districts.add(new City("抚宁区"));
                districts.add(new City("昌黎县"));
                districts.add(new City("青龙满族自治县"));
                districts.add(new City("卢龙县"));
                break;
            case "邢台市":
                districts.add(new City("襄都区"));
                districts.add(new City("信都区"));
                districts.add(new City("任泽区"));
                districts.add(new City("南和区"));
                districts.add(new City("临城县"));
                districts.add(new City("内丘县"));
                districts.add(new City("柏乡县"));
                districts.add(new City("隆尧县"));
                districts.add(new City("宁晋县"));
                districts.add(new City("巨鹿县"));
                districts.add(new City("新河县"));
                districts.add(new City("广宗县"));
                districts.add(new City("平乡县"));
                districts.add(new City("威县"));
                districts.add(new City("清河县"));
                districts.add(new City("临西县"));
                districts.add(new City("南宫市"));
                districts.add(new City("沙河市"));
                break;
            case "张家口市":
                districts.add(new City("桥东区"));
                districts.add(new City("桥西区"));
                districts.add(new City("宣化区"));
                districts.add(new City("下花园区"));
                districts.add(new City("万全区"));
                districts.add(new City("崇礼区"));
                districts.add(new City("张北县"));
                districts.add(new City("康保县"));
                districts.add(new City("沽源县"));
                districts.add(new City("尚义县"));
                districts.add(new City("蔚县"));
                districts.add(new City("阳原县"));
                districts.add(new City("怀安县"));
                districts.add(new City("怀来县"));
                districts.add(new City("涿鹿县"));
                districts.add(new City("赤城县"));
                break;
            case "承德市":
                districts.add(new City("双桥区"));
                districts.add(new City("双滦区"));
                districts.add(new City("鹰手营子矿区"));
                districts.add(new City("兴隆县"));
                districts.add(new City("滦平县"));
                districts.add(new City("隆化县"));
                districts.add(new City("丰宁满族自治县"));
                districts.add(new City("宽城满族自治县"));
                districts.add(new City("围场满族蒙古族自治县"));
                districts.add(new City("平泉市"));
                break;
            case "沧州市":
                districts.add(new City("新华区"));
                districts.add(new City("运河区"));
                districts.add(new City("沧县"));
                districts.add(new City("青县"));
                districts.add(new City("东光县"));
                districts.add(new City("海兴县"));
                districts.add(new City("盐山县"));
                districts.add(new City("肃宁县"));
                districts.add(new City("南皮县"));
                districts.add(new City("吴桥县"));
                districts.add(new City("献县"));
                districts.add(new City("孟村回族自治县"));
                districts.add(new City("泊头市"));
                districts.add(new City("任丘市"));
                districts.add(new City("黄骅市"));
                districts.add(new City("河间市"));
                break;
            case "廊坊市":
                districts.add(new City("安次区"));
                districts.add(new City("广阳区"));
                districts.add(new City("固安县"));
                districts.add(new City("永清县"));
                districts.add(new City("香河县"));
                districts.add(new City("大城县"));
                districts.add(new City("文安县"));
                districts.add(new City("大厂回族自治县"));
                districts.add(new City("霸州市"));
                districts.add(new City("三河市"));
                break;
            case "衡水市":
                districts.add(new City("桃城区"));
                districts.add(new City("冀州区"));
                districts.add(new City("枣强县"));
                districts.add(new City("武邑县"));
                districts.add(new City("武强县"));
                districts.add(new City("饶阳县"));
                districts.add(new City("安平县"));
                districts.add(new City("故城县"));
                districts.add(new City("景县"));
                districts.add(new City("阜城县"));
                districts.add(new City("深州市"));
                break;
            // === 河南省 ===
            case "开封市":
                districts.add(new City("龙亭区"));
                districts.add(new City("顺河回族区"));
                districts.add(new City("鼓楼区"));
                districts.add(new City("禹王台区"));
                districts.add(new City("祥符区"));
                districts.add(new City("杞县"));
                districts.add(new City("通许县"));
                districts.add(new City("尉氏县"));
                districts.add(new City("兰考县"));
                break;
            case "平顶山市":
                districts.add(new City("新华区"));
                districts.add(new City("卫东区"));
                districts.add(new City("石龙区"));
                districts.add(new City("湛河区"));
                districts.add(new City("宝丰县"));
                districts.add(new City("叶县"));
                districts.add(new City("鲁山县"));
                districts.add(new City("郏县"));
                districts.add(new City("舞钢市"));
                districts.add(new City("汝州市"));
                break;
            case "安阳市":
                districts.add(new City("文峰区"));
                districts.add(new City("北关区"));
                districts.add(new City("殷都区"));
                districts.add(new City("龙安区"));
                districts.add(new City("安阳县"));
                districts.add(new City("汤阴县"));
                districts.add(new City("滑县"));
                districts.add(new City("内黄县"));
                districts.add(new City("林州市"));
                break;
            case "鹤壁市":
                districts.add(new City("鹤山区"));
                districts.add(new City("山城区"));
                districts.add(new City("淇滨区"));
                districts.add(new City("浚县"));
                districts.add(new City("淇县"));
                break;
            case "新乡市":
                districts.add(new City("红旗区"));
                districts.add(new City("卫滨区"));
                districts.add(new City("凤泉区"));
                districts.add(new City("牧野区"));
                districts.add(new City("新乡县"));
                districts.add(new City("获嘉县"));
                districts.add(new City("原阳县"));
                districts.add(new City("延津县"));
                districts.add(new City("封丘县"));
                districts.add(new City("卫辉市"));
                districts.add(new City("辉县市"));
                districts.add(new City("长垣市"));
                break;
            case "焦作市":
                districts.add(new City("解放区"));
                districts.add(new City("中站区"));
                districts.add(new City("马村区"));
                districts.add(new City("山阳区"));
                districts.add(new City("修武县"));
                districts.add(new City("博爱县"));
                districts.add(new City("武陟县"));
                districts.add(new City("温县"));
                districts.add(new City("沁阳市"));
                districts.add(new City("孟州市"));
                break;
            case "濮阳市":
                districts.add(new City("华龙区"));
                districts.add(new City("清丰县"));
                districts.add(new City("南乐县"));
                districts.add(new City("范县"));
                districts.add(new City("台前县"));
                districts.add(new City("濮阳县"));
                break;
            case "许昌市":
                districts.add(new City("魏都区"));
                districts.add(new City("建安区"));
                districts.add(new City("鄢陵县"));
                districts.add(new City("襄城县"));
                districts.add(new City("禹州市"));
                districts.add(new City("长葛市"));
                break;
            case "漯河市":
                districts.add(new City("源汇区"));
                districts.add(new City("郾城区"));
                districts.add(new City("召陵区"));
                districts.add(new City("舞阳县"));
                districts.add(new City("临颍县"));
                break;
            case "三门峡市":
                districts.add(new City("湖滨区"));
                districts.add(new City("陕州区"));
                districts.add(new City("渑池县"));
                districts.add(new City("卢氏县"));
                districts.add(new City("义马市"));
                districts.add(new City("灵宝市"));
                break;
            case "南阳市":
                districts.add(new City("宛城区"));
                districts.add(new City("卧龙区"));
                districts.add(new City("南召县"));
                districts.add(new City("方城县"));
                districts.add(new City("西峡县"));
                districts.add(new City("镇平县"));
                districts.add(new City("内乡县"));
                districts.add(new City("淅川县"));
                districts.add(new City("社旗县"));
                districts.add(new City("唐河县"));
                districts.add(new City("新野县"));
                districts.add(new City("桐柏县"));
                districts.add(new City("邓州市"));
                break;
            case "商丘市":
                districts.add(new City("梁园区"));
                districts.add(new City("睢阳区"));
                districts.add(new City("民权县"));
                districts.add(new City("睢县"));
                districts.add(new City("宁陵县"));
                districts.add(new City("柘城县"));
                districts.add(new City("虞城县"));
                districts.add(new City("夏邑县"));
                districts.add(new City("永城市"));
                break;
            case "信阳市":
                districts.add(new City("浉河区"));
                districts.add(new City("平桥区"));
                districts.add(new City("罗山县"));
                districts.add(new City("光山县"));
                districts.add(new City("新县"));
                districts.add(new City("商城县"));
                districts.add(new City("固始县"));
                districts.add(new City("潢川县"));
                districts.add(new City("淮滨县"));
                districts.add(new City("息县"));
                break;
            case "周口市":
                districts.add(new City("川汇区"));
                districts.add(new City("淮阳区"));
                districts.add(new City("扶沟县"));
                districts.add(new City("西华县"));
                districts.add(new City("商水县"));
                districts.add(new City("沈丘县"));
                districts.add(new City("郸城县"));
                districts.add(new City("太康县"));
                districts.add(new City("鹿邑县"));
                districts.add(new City("项城市"));
                break;
            case "驻马店市":
                districts.add(new City("驿城区"));
                districts.add(new City("西平县"));
                districts.add(new City("上蔡县"));
                districts.add(new City("平舆县"));
                districts.add(new City("正阳县"));
                districts.add(new City("确山县"));
                districts.add(new City("泌阳县"));
                districts.add(new City("汝南县"));
                districts.add(new City("遂平县"));
                districts.add(new City("新蔡县"));
                break;
            // === 湖北省 ===
            case "黄石市":
                districts.add(new City("黄石港区"));
                districts.add(new City("西塞山区"));
                districts.add(new City("下陆区"));
                districts.add(new City("铁山区"));
                break;
            case "十堰市":
                districts.add(new City("茅箭区"));
                districts.add(new City("张湾区"));
                districts.add(new City("郧阳区"));
                break;
            case "宜昌市":
                districts.add(new City("西陵区"));
                districts.add(new City("伍家岗区"));
                districts.add(new City("点军区"));
                districts.add(new City("猇亭区"));
                districts.add(new City("夷陵区"));
                districts.add(new City("远安县"));
                districts.add(new City("兴山县"));
                districts.add(new City("秭归县"));
                districts.add(new City("长阳土家族自治县"));
                districts.add(new City("五峰土家族自治县"));
                districts.add(new City("宜都市"));
                districts.add(new City("当阳市"));
                districts.add(new City("枝江市"));
                break;
            case "襄阳市":
                districts.add(new City("襄城区"));
                districts.add(new City("樊城区"));
                districts.add(new City("襄州区"));
                districts.add(new City("南漳县"));
                districts.add(new City("谷城县"));
                districts.add(new City("保康县"));
                districts.add(new City("老河口市"));
                districts.add(new City("枣阳市"));
                districts.add(new City("宜城市"));
                break;
            case "鄂州市":
                districts.add(new City("梁子湖区"));
                districts.add(new City("华容区"));
                districts.add(new City("鄂城区"));
                break;
            case "荆门市":
                districts.add(new City("东宝区"));
                districts.add(new City("掇刀区"));
                break;
            case "孝感市":
                districts.add(new City("孝南区"));
                break;
            case "荆州市":
                districts.add(new City("沙市区"));
                districts.add(new City("荆州区"));
                districts.add(new City("公安县"));
                districts.add(new City("监利市"));
                districts.add(new City("江陵县"));
                districts.add(new City("石首市"));
                districts.add(new City("洪湖市"));
                districts.add(new City("松滋市"));
                break;
            case "黄冈市":
                districts.add(new City("黄州区"));
                districts.add(new City("团风县"));
                districts.add(new City("红安县"));
                districts.add(new City("罗田县"));
                districts.add(new City("英山县"));
                districts.add(new City("浠水县"));
                districts.add(new City("蕲春县"));
                districts.add(new City("黄梅县"));
                districts.add(new City("麻城市"));
                districts.add(new City("武穴市"));
                break;
            case "咸宁市":
                districts.add(new City("咸安区"));
                break;
            case "随州市":
                districts.add(new City("曾都区"));
                break;
            case "恩施土家族苗族自治州":
                districts.add(new City("恩施市"));
                districts.add(new City("利川市"));
                break;
            // === 湖南省 ===
            case "株洲市":
                districts.add(new City("荷塘区"));
                districts.add(new City("芦淞区"));
                districts.add(new City("石峰区"));
                districts.add(new City("天元区"));
                districts.add(new City("渌口区"));
                break;
            case "湘潭市":
                districts.add(new City("雨湖区"));
                districts.add(new City("岳塘区"));
                break;
            case "衡阳市":
                districts.add(new City("珠晖区"));
                districts.add(new City("雁峰区"));
                districts.add(new City("石鼓区"));
                districts.add(new City("蒸湘区"));
                districts.add(new City("南岳区"));
                districts.add(new City("衡阳县"));
                districts.add(new City("衡南县"));
                districts.add(new City("衡山县"));
                districts.add(new City("衡东县"));
                districts.add(new City("祁东县"));
                districts.add(new City("耒阳市"));
                districts.add(new City("常宁市"));
                break;
            case "邵阳市":
                districts.add(new City("双清区"));
                districts.add(new City("大祥区"));
                districts.add(new City("北塔区"));
                districts.add(new City("新邵县"));
                districts.add(new City("邵阳县"));
                districts.add(new City("隆回县"));
                districts.add(new City("洞口县"));
                districts.add(new City("绥宁县"));
                districts.add(new City("新宁县"));
                districts.add(new City("城步苗族自治县"));
                districts.add(new City("武冈市"));
                districts.add(new City("邵东市"));
                break;
            case "岳阳市":
                districts.add(new City("岳阳楼区"));
                districts.add(new City("云溪区"));
                districts.add(new City("君山区"));
                districts.add(new City("岳阳县"));
                districts.add(new City("华容县"));
                districts.add(new City("湘阴县"));
                districts.add(new City("平江县"));
                districts.add(new City("汨罗市"));
                districts.add(new City("临湘市"));
                break;
            case "常德市":
                districts.add(new City("武陵区"));
                districts.add(new City("鼎城区"));
                districts.add(new City("安乡县"));
                districts.add(new City("汉寿县"));
                districts.add(new City("澧县"));
                districts.add(new City("临澧县"));
                districts.add(new City("桃源县"));
                districts.add(new City("石门县"));
                districts.add(new City("津市市"));
                break;
            case "张家界市":
                districts.add(new City("永定区"));
                districts.add(new City("武陵源区"));
                break;
            case "益阳市":
                districts.add(new City("资阳区"));
                districts.add(new City("赫山区"));
                break;
            case "郴州市":
                districts.add(new City("北湖区"));
                districts.add(new City("苏仙区"));
                break;
            case "永州市":
                districts.add(new City("零陵区"));
                districts.add(new City("冷水滩区"));
                break;
            case "怀化市":
                districts.add(new City("鹤城区"));
                break;
            case "娄底市":
                districts.add(new City("娄星区"));
                break;
            case "湘西土家族苗族自治州":
                districts.add(new City("吉首市"));
                break;
            // === 四川省 ===
            case "自贡市":
                districts.add(new City("自流井区"));
                districts.add(new City("贡井区"));
                districts.add(new City("大安区"));
                districts.add(new City("沿滩区"));
                break;
            case "攀枝花市":
                districts.add(new City("东区"));
                districts.add(new City("西区"));
                districts.add(new City("仁和区"));
                break;
            case "泸州市":
                districts.add(new City("江阳区"));
                districts.add(new City("纳溪区"));
                districts.add(new City("龙马潭区"));
                break;
            case "德阳市":
                districts.add(new City("旌阳区"));
                districts.add(new City("罗江区"));
                break;
            case "绵阳市":
                districts.add(new City("涪城区"));
                districts.add(new City("游仙区"));
                districts.add(new City("安州区"));
                districts.add(new City("三台县"));
                districts.add(new City("盐亭县"));
                districts.add(new City("梓潼县"));
                districts.add(new City("北川羌族自治县"));
                districts.add(new City("平武县"));
                districts.add(new City("江油市"));
                break;
            case "广元市":
                districts.add(new City("利州区"));
                districts.add(new City("昭化区"));
                districts.add(new City("朝天区"));
                break;
            case "遂宁市":
                districts.add(new City("船山区"));
                districts.add(new City("安居区"));
                break;
            case "内江市":
                districts.add(new City("市中区"));
                districts.add(new City("东兴区"));
                break;
            case "乐山市":
                districts.add(new City("市中区"));
                districts.add(new City("沙湾区"));
                districts.add(new City("五通桥区"));
                districts.add(new City("金口河区"));
                break;
            case "南充市":
                districts.add(new City("顺庆区"));
                districts.add(new City("高坪区"));
                districts.add(new City("嘉陵区"));
                districts.add(new City("南部县"));
                districts.add(new City("营山县"));
                districts.add(new City("蓬安县"));
                districts.add(new City("仪陇县"));
                districts.add(new City("西充县"));
                districts.add(new City("阆中市"));
                break;
            case "眉山市":
                districts.add(new City("东坡区"));
                districts.add(new City("彭山区"));
                break;
            case "宜宾市":
                districts.add(new City("翠屏区"));
                districts.add(new City("南溪区"));
                districts.add(new City("叙州区"));
                districts.add(new City("江安县"));
                districts.add(new City("长宁县"));
                districts.add(new City("高县"));
                districts.add(new City("珙县"));
                districts.add(new City("筠连县"));
                districts.add(new City("兴文县"));
                districts.add(new City("屏山县"));
                break;
            case "广安市":
                districts.add(new City("广安区"));
                districts.add(new City("前锋区"));
                break;
            case "达州市":
                districts.add(new City("通川区"));
                districts.add(new City("达川区"));
                districts.add(new City("宣汉县"));
                districts.add(new City("开江县"));
                districts.add(new City("大竹县"));
                districts.add(new City("渠县"));
                districts.add(new City("万源市"));
                break;
            case "雅安市":
                districts.add(new City("雨城区"));
                districts.add(new City("名山区"));
                break;
            case "巴中市":
                districts.add(new City("巴州区"));
                districts.add(new City("恩阳区"));
                break;
            case "资阳市":
                districts.add(new City("雁江区"));
                break;
            case "阿坝藏族羌族自治州":
                districts.add(new City("马尔康市"));
                break;
            case "甘孜藏族自治州":
                districts.add(new City("康定市"));
                break;
            case "凉山彝族自治州":
                districts.add(new City("西昌市"));
                break;
            // === 贵州省 ===
            case "六盘水市":
                districts.add(new City("钟山区"));
                districts.add(new City("六枝特区"));
                districts.add(new City("水城区"));
                break;
            case "遵义市":
                districts.add(new City("红花岗区"));
                districts.add(new City("汇川区"));
                districts.add(new City("播州区"));
                break;
            case "安顺市":
                districts.add(new City("西秀区"));
                districts.add(new City("平坝区"));
                break;
            case "毕节市":
                districts.add(new City("七星关区"));
                break;
            case "铜仁市":
                districts.add(new City("碧江区"));
                districts.add(new City("万山区"));
                break;
            case "黔西南布依族苗族自治州":
                districts.add(new City("兴义市"));
                break;
            case "黔东南苗族侗族自治州":
                districts.add(new City("凯里市"));
                break;
            case "黔南布依族苗族自治州":
                districts.add(new City("都匀市"));
                break;
            // === 云南省 ===
            case "曲靖市":
                districts.add(new City("麒麟区"));
                districts.add(new City("沾益区"));
                districts.add(new City("马龙区"));
                break;
            case "玉溪市":
                districts.add(new City("红塔区"));
                districts.add(new City("江川区"));
                break;
            case "保山市":
                districts.add(new City("隆阳区"));
                break;
            case "昭通市":
                districts.add(new City("昭阳区"));
                break;
            case "丽江市":
                districts.add(new City("古城区"));
                break;
            case "普洱市":
                districts.add(new City("思茅区"));
                break;
            case "临沧市":
                districts.add(new City("临翔区"));
                break;
            case "楚雄彝族自治州":
                districts.add(new City("楚雄市"));
                break;
            case "红河哈尼族彝族自治州":
                districts.add(new City("个旧市"));
                districts.add(new City("开远市"));
                districts.add(new City("蒙自市"));
                break;
            case "文山壮族苗族自治州":
                districts.add(new City("文山市"));
                break;
            case "西双版纳傣族自治州":
                districts.add(new City("景洪市"));
                break;
            case "大理白族自治州":
                districts.add(new City("大理市"));
                break;
            case "德宏傣族景颇族自治州":
                districts.add(new City("芒市"));
                districts.add(new City("瑞丽市"));
                break;
            case "怒江傈僳族自治州":
                districts.add(new City("泸水市"));
                break;
            case "迪庆藏族自治州":
                districts.add(new City("香格里拉市"));
                break;
            // === 西藏自治区 ===
            case "日喀则市":
                districts.add(new City("桑珠孜区"));
                break;
            case "昌都市":
                districts.add(new City("卡若区"));
                break;
            case "林芝市":
                districts.add(new City("巴宜区"));
                break;
            case "山南市":
                districts.add(new City("乃东区"));
                break;
            case "那曲市":
                districts.add(new City("色尼区"));
                break;
            case "阿里地区":
                districts.add(new City("噶尔县"));
                break;
            // === 陕西省 ===
            case "铜川市":
                districts.add(new City("王益区"));
                districts.add(new City("印台区"));
                districts.add(new City("耀州区"));
                break;
            case "宝鸡市":
                districts.add(new City("渭滨区"));
                districts.add(new City("金台区"));
                districts.add(new City("陈仓区"));
                break;
            case "咸阳市":
                districts.add(new City("秦都区"));
                districts.add(new City("杨陵区"));
                districts.add(new City("渭城区"));
                break;
            case "渭南市":
                districts.add(new City("临渭区"));
                districts.add(new City("华州区"));
                break;
            case "延安市":
                districts.add(new City("宝塔区"));
                districts.add(new City("安塞区"));
                break;
            case "汉中市":
                districts.add(new City("汉台区"));
                districts.add(new City("南郑区"));
                break;
            case "榆林市":
                districts.add(new City("榆阳区"));
                districts.add(new City("横山区"));
                break;
            case "安康市":
                districts.add(new City("汉滨区"));
                break;
            case "商洛市":
                districts.add(new City("商州区"));
                break;
            // === 甘肃省 ===
            case "嘉峪关市":
                districts.add(new City("雄关区"));
                districts.add(new City("镜铁区"));
                districts.add(new City("长城区"));
                break;
            case "金昌市":
                districts.add(new City("金川区"));
                districts.add(new City("永昌县"));
                break;
            case "白银市":
                districts.add(new City("白银区"));
                districts.add(new City("平川区"));
                break;
            case "天水市":
                districts.add(new City("秦州区"));
                districts.add(new City("麦积区"));
                break;
            case "武威市":
                districts.add(new City("凉州区"));
                break;
            case "张掖市":
                districts.add(new City("甘州区"));
                break;
            case "平凉市":
                districts.add(new City("崆峒区"));
                break;
            case "酒泉市":
                districts.add(new City("肃州区"));
                break;
            case "庆阳市":
                districts.add(new City("西峰区"));
                break;
            case "定西市":
                districts.add(new City("安定区"));
                break;
            case "陇南市":
                districts.add(new City("武都区"));
                break;
            case "临夏回族自治州":
                districts.add(new City("临夏市"));
                break;
            case "甘南藏族自治州":
                districts.add(new City("合作市"));
                break;
            // === 青海省 ===
            case "海东市":
                districts.add(new City("乐都区"));
                districts.add(new City("平安区"));
                break;
            case "海北藏族自治州":
                districts.add(new City("海晏县"));
                break;
            case "黄南藏族自治州":
                districts.add(new City("同仁市"));
                break;
            case "海南藏族自治州":
                districts.add(new City("共和县"));
                break;
            case "果洛藏族自治州":
                districts.add(new City("玛沁县"));
                break;
            case "玉树藏族自治州":
                districts.add(new City("玉树市"));
                break;
            case "海西蒙古族藏族自治州":
                districts.add(new City("德令哈市"));
                districts.add(new City("格尔木市"));
                break;
            // === 宁夏回族自治区 ===
            case "石嘴山市":
                districts.add(new City("大武口区"));
                districts.add(new City("惠农区"));
                break;
            case "吴忠市":
                districts.add(new City("利通区"));
                districts.add(new City("红寺堡区"));
                break;
            case "固原市":
                districts.add(new City("原州区"));
                break;
            case "中卫市":
                districts.add(new City("沙坡头区"));
                break;
            // === 新疆维吾尔自治区 ===
            case "克拉玛依市":
                districts.add(new City("克拉玛依区"));
                districts.add(new City("独山子区"));
                districts.add(new City("白碱滩区"));
                districts.add(new City("乌尔禾区"));
                break;
            case "吐鲁番市":
                districts.add(new City("高昌区"));
                break;
            case "哈密市":
                districts.add(new City("伊州区"));
                break;
            case "昌吉回族自治州":
                districts.add(new City("昌吉市"));
                break;
            case "博尔塔拉蒙古自治州":
                districts.add(new City("博乐市"));
                break;
            case "巴音郭楞蒙古自治州":
                districts.add(new City("库尔勒市"));
                break;
            case "阿克苏地区":
                districts.add(new City("阿克苏市"));
                break;
            case "克孜勒苏柯尔克孜自治州":
                districts.add(new City("阿图什市"));
                break;
            case "喀什地区":
                districts.add(new City("喀什市"));
                break;
            case "和田地区":
                districts.add(new City("和田市"));
                break;
            case "伊犁哈萨克自治州":
                districts.add(new City("伊宁市"));
                break;
            case "塔城地区":
                districts.add(new City("塔城市"));
                break;
            case "阿勒泰地区":
                districts.add(new City("阿勒泰市"));
                break;
            // === 山西省其余城市 ===
            case "大同市":
                districts.add(new City("新荣区"));
                districts.add(new City("平城区"));
                districts.add(new City("云冈区"));
                districts.add(new City("云州区"));
                districts.add(new City("阳高县"));
                districts.add(new City("天镇县"));
                districts.add(new City("广灵县"));
                districts.add(new City("灵丘县"));
                districts.add(new City("浑源县"));
                districts.add(new City("左云县"));
                break;
            case "阳泉市":
                districts.add(new City("城区"));
                districts.add(new City("矿区"));
                districts.add(new City("郊区"));
                districts.add(new City("平定县"));
                districts.add(new City("盂县"));
                break;
            case "长治市":
                districts.add(new City("潞州区"));
                districts.add(new City("上党区"));
                districts.add(new City("屯留区"));
                districts.add(new City("潞城区"));
                districts.add(new City("襄垣县"));
                districts.add(new City("平顺县"));
                districts.add(new City("黎城县"));
                districts.add(new City("壶关县"));
                districts.add(new City("长子县"));
                districts.add(new City("武乡县"));
                districts.add(new City("沁县"));
                districts.add(new City("沁源县"));
                break;
            case "晋城市":
                districts.add(new City("城区"));
                districts.add(new City("沁水县"));
                districts.add(new City("阳城县"));
                districts.add(new City("陵川县"));
                districts.add(new City("泽州县"));
                districts.add(new City("高平市"));
                break;
            case "朔州市":
                districts.add(new City("朔城区"));
                districts.add(new City("平鲁区"));
                districts.add(new City("山阴县"));
                districts.add(new City("应县"));
                districts.add(new City("右玉县"));
                districts.add(new City("怀仁市"));
                break;
            case "晋中市":
                districts.add(new City("榆次区"));
                districts.add(new City("太谷区"));
                districts.add(new City("榆社县"));
                districts.add(new City("左权县"));
                districts.add(new City("和顺县"));
                districts.add(new City("昔阳县"));
                districts.add(new City("寿阳县"));
                districts.add(new City("祁县"));
                districts.add(new City("平遥县"));
                districts.add(new City("灵石县"));
                districts.add(new City("介休市"));
                break;
            case "运城市":
                districts.add(new City("盐湖区"));
                districts.add(new City("临猗县"));
                districts.add(new City("万荣县"));
                districts.add(new City("闻喜县"));
                districts.add(new City("稷山县"));
                districts.add(new City("新绛县"));
                districts.add(new City("绛县"));
                districts.add(new City("垣曲县"));
                districts.add(new City("夏县"));
                districts.add(new City("平陆县"));
                districts.add(new City("芮城县"));
                districts.add(new City("永济市"));
                districts.add(new City("河津市"));
                break;
            case "忻州市":
                districts.add(new City("忻府区"));
                districts.add(new City("定襄县"));
                districts.add(new City("五台县"));
                districts.add(new City("代县"));
                districts.add(new City("繁峙县"));
                districts.add(new City("宁武县"));
                districts.add(new City("静乐县"));
                districts.add(new City("神池县"));
                districts.add(new City("五寨县"));
                districts.add(new City("岢岚县"));
                districts.add(new City("河曲县"));
                districts.add(new City("保德县"));
                districts.add(new City("偏关县"));
                districts.add(new City("原平市"));
                break;
            case "临汾市":
                districts.add(new City("尧都区"));
                districts.add(new City("曲沃县"));
                districts.add(new City("翼城县"));
                districts.add(new City("襄汾县"));
                districts.add(new City("洪洞县"));
                districts.add(new City("古县"));
                districts.add(new City("安泽县"));
                districts.add(new City("浮山县"));
                districts.add(new City("吉县"));
                districts.add(new City("乡宁县"));
                districts.add(new City("大宁县"));
                districts.add(new City("隰县"));
                districts.add(new City("永和县"));
                districts.add(new City("蒲县"));
                districts.add(new City("汾西县"));
                districts.add(new City("侯马市"));
                districts.add(new City("霍州市"));
                break;
            case "吕梁市":
                districts.add(new City("离石区"));
                districts.add(new City("文水县"));
                districts.add(new City("交城县"));
                districts.add(new City("兴县"));
                districts.add(new City("临县"));
                districts.add(new City("柳林县"));
                districts.add(new City("石楼县"));
                districts.add(new City("岚县"));
                districts.add(new City("方山县"));
                districts.add(new City("中阳县"));
                districts.add(new City("交口县"));
                districts.add(new City("孝义市"));
                districts.add(new City("汾阳市"));
                break;
            // === 内蒙古自治区 ===
            case "包头市":
                districts.add(new City("东河区"));
                districts.add(new City("昆都仑区"));
                districts.add(new City("青山区"));
                districts.add(new City("石拐区"));
                districts.add(new City("白云鄂博矿区"));
                districts.add(new City("九原区"));
                break;
            case "乌海市":
                districts.add(new City("海勃湾区"));
                districts.add(new City("海南区"));
                districts.add(new City("乌达区"));
                break;
            case "赤峰市":
                districts.add(new City("红山区"));
                districts.add(new City("元宝山区"));
                districts.add(new City("松山区"));
                break;
            case "通辽市":
                districts.add(new City("科尔沁区"));
                break;
            case "鄂尔多斯市":
                districts.add(new City("东胜区"));
                districts.add(new City("康巴什区"));
                break;
            case "呼伦贝尔市":
                districts.add(new City("海拉尔区"));
                break;
            case "巴彦淖尔市":
                districts.add(new City("临河区"));
                break;
            case "乌兰察布市":
                districts.add(new City("集宁区"));
                break;
            case "兴安盟":
                districts.add(new City("乌兰浩特市"));
                break;
            case "锡林郭勒盟":
                districts.add(new City("锡林浩特市"));
                break;
            case "阿拉善盟":
                districts.add(new City("阿拉善左旗"));
                break;
            // === 辽宁省其余城市 ===
            case "鞍山市":
                districts.add(new City("铁东区"));
                districts.add(new City("铁西区"));
                districts.add(new City("立山区"));
                districts.add(new City("千山区"));
                break;
            case "抚顺市":
                districts.add(new City("新抚区"));
                districts.add(new City("东洲区"));
                districts.add(new City("望花区"));
                districts.add(new City("顺城区"));
                break;
            case "本溪市":
                districts.add(new City("平山区"));
                districts.add(new City("溪湖区"));
                districts.add(new City("明山区"));
                districts.add(new City("南芬区"));
                break;
            case "丹东市":
                districts.add(new City("元宝区"));
                districts.add(new City("振兴区"));
                districts.add(new City("振安区"));
                break;
            case "锦州市":
                districts.add(new City("古塔区"));
                districts.add(new City("凌河区"));
                districts.add(new City("太和区"));
                break;
            case "营口市":
                districts.add(new City("站前区"));
                districts.add(new City("西市区"));
                districts.add(new City("鲅鱼圈区"));
                districts.add(new City("老边区"));
                break;
            case "阜新市":
                districts.add(new City("海州区"));
                districts.add(new City("新邱区"));
                districts.add(new City("太平区"));
                districts.add(new City("清河门区"));
                districts.add(new City("细河区"));
                break;
            case "辽阳市":
                districts.add(new City("白塔区"));
                districts.add(new City("文圣区"));
                districts.add(new City("宏伟区"));
                districts.add(new City("弓长岭区"));
                districts.add(new City("太子河区"));
                break;
            case "盘锦市":
                districts.add(new City("双台子区"));
                districts.add(new City("兴隆台区"));
                districts.add(new City("大洼区"));
                break;
            case "铁岭市":
                districts.add(new City("银州区"));
                districts.add(new City("清河区"));
                break;
            case "朝阳市":
                districts.add(new City("双塔区"));
                districts.add(new City("龙城区"));
                break;
            case "葫芦岛市":
                districts.add(new City("连山区"));
                districts.add(new City("龙港区"));
                districts.add(new City("南票区"));
                break;
            // === 吉林省 ===
            case "吉林市":
                districts.add(new City("昌邑区"));
                districts.add(new City("龙潭区"));
                districts.add(new City("船营区"));
                districts.add(new City("丰满区"));
                break;
            case "四平市":
                districts.add(new City("铁西区"));
                districts.add(new City("铁东区"));
                break;
            case "辽源市":
                districts.add(new City("龙山区"));
                districts.add(new City("西安区"));
                break;
            case "通化市":
                districts.add(new City("东昌区"));
                districts.add(new City("二道江区"));
                break;
            case "白山市":
                districts.add(new City("浑江区"));
                districts.add(new City("江源区"));
                break;
            case "松原市":
                districts.add(new City("宁江区"));
                break;
            case "白城市":
                districts.add(new City("洮北区"));
                break;
            case "延边朝鲜族自治州":
                districts.add(new City("延吉市"));
                break;
            // === 黑龙江省 ===
            case "齐齐哈尔市":
                districts.add(new City("龙沙区"));
                districts.add(new City("建华区"));
                districts.add(new City("铁锋区"));
                districts.add(new City("昂昂溪区"));
                districts.add(new City("富拉尔基区"));
                break;
            case "鸡西市":
                districts.add(new City("鸡冠区"));
                districts.add(new City("恒山区"));
                districts.add(new City("滴道区"));
                districts.add(new City("城子河区"));
                break;
            case "鹤岗市":
                districts.add(new City("向阳区"));
                districts.add(new City("工农区"));
                districts.add(new City("南山区"));
                districts.add(new City("兴安区"));
                districts.add(new City("东山区"));
                districts.add(new City("兴山区"));
                break;
            case "双鸭山市":
                districts.add(new City("尖山区"));
                districts.add(new City("岭东区"));
                districts.add(new City("四方台区"));
                districts.add(new City("宝山区"));
                break;
            case "大庆市":
                districts.add(new City("萨尔图区"));
                districts.add(new City("龙凤区"));
                districts.add(new City("让胡路区"));
                districts.add(new City("红岗区"));
                districts.add(new City("大同区"));
                break;
            case "伊春市":
                districts.add(new City("伊美区"));
                districts.add(new City("乌翠区"));
                districts.add(new City("友好区"));
                break;
            case "佳木斯市":
                districts.add(new City("向阳区"));
                districts.add(new City("前进区"));
                districts.add(new City("东风区"));
                districts.add(new City("郊区"));
                break;
            case "七台河市":
                districts.add(new City("新兴区"));
                districts.add(new City("桃山区"));
                districts.add(new City("茄子河区"));
                break;
            case "牡丹江市":
                districts.add(new City("东安区"));
                districts.add(new City("阳明区"));
                districts.add(new City("爱民区"));
                districts.add(new City("西安区"));
                break;
            case "黑河市":
                districts.add(new City("爱辉区"));
                break;
            case "绥化市":
                districts.add(new City("北林区"));
                break;
            case "大兴安岭地区":
                districts.add(new City("加格达奇区"));
                break;
            // === 安徽省 ===
            case "芜湖市":
                districts.add(new City("镜湖区"));
                districts.add(new City("弋江区"));
                districts.add(new City("鸠江区"));
                districts.add(new City("湾沚区"));
                districts.add(new City("繁昌区"));
                break;
            case "蚌埠市":
                districts.add(new City("龙子湖区"));
                districts.add(new City("蚌山区"));
                districts.add(new City("禹会区"));
                districts.add(new City("淮上区"));
                break;
            case "淮南市":
                districts.add(new City("大通区"));
                districts.add(new City("田家庵区"));
                districts.add(new City("谢家集区"));
                districts.add(new City("八公山区"));
                districts.add(new City("潘集区"));
                break;
            case "马鞍山市":
                districts.add(new City("花山区"));
                districts.add(new City("雨山区"));
                districts.add(new City("博望区"));
                break;
            case "淮北市":
                districts.add(new City("杜集区"));
                districts.add(new City("相山区"));
                districts.add(new City("烈山区"));
                break;
            case "铜陵市":
                districts.add(new City("铜官区"));
                districts.add(new City("义安区"));
                districts.add(new City("郊区"));
                break;
            case "安庆市":
                districts.add(new City("迎江区"));
                districts.add(new City("大观区"));
                districts.add(new City("宜秀区"));
                break;
            case "黄山市":
                districts.add(new City("屯溪区"));
                districts.add(new City("黄山区"));
                districts.add(new City("徽州区"));
                break;
            case "滁州市":
                districts.add(new City("琅琊区"));
                districts.add(new City("南谯区"));
                break;
            case "阜阳市":
                districts.add(new City("颍州区"));
                districts.add(new City("颍东区"));
                districts.add(new City("颍泉区"));
                break;
            case "宿州市":
                districts.add(new City("埇桥区"));
                break;
            case "六安市":
                districts.add(new City("金安区"));
                districts.add(new City("裕安区"));
                districts.add(new City("叶集区"));
                break;
            case "亳州市":
                districts.add(new City("谯城区"));
                break;
            case "池州市":
                districts.add(new City("贵池区"));
                break;
            case "宣城市":
                districts.add(new City("宣州区"));
                break;
            // === 福建省 ===
            case "莆田市":
                districts.add(new City("城厢区"));
                districts.add(new City("涵江区"));
                districts.add(new City("荔城区"));
                districts.add(new City("秀屿区"));
                break;
            case "三明市":
                districts.add(new City("三元区"));
                districts.add(new City("沙县区"));
                break;
            case "漳州市":
                districts.add(new City("芗城区"));
                districts.add(new City("龙文区"));
                districts.add(new City("龙海区"));
                districts.add(new City("长泰区"));
                break;
            case "南平市":
                districts.add(new City("延平区"));
                districts.add(new City("建阳区"));
                break;
            case "龙岩市":
                districts.add(new City("新罗区"));
                districts.add(new City("永定区"));
                break;
            case "宁德市":
                districts.add(new City("蕉城区"));
                break;
            // === 江西省 ===
            case "景德镇市":
                districts.add(new City("昌江区"));
                districts.add(new City("珠山区"));
                break;
            case "萍乡市":
                districts.add(new City("安源区"));
                districts.add(new City("湘东区"));
                break;
            case "九江市":
                districts.add(new City("濂溪区"));
                districts.add(new City("浔阳区"));
                districts.add(new City("柴桑区"));
                break;
            case "新余市":
                districts.add(new City("渝水区"));
                break;
            case "鹰潭市":
                districts.add(new City("月湖区"));
                districts.add(new City("余江区"));
                break;
            case "赣州市":
                districts.add(new City("章贡区"));
                districts.add(new City("南康区"));
                districts.add(new City("赣县区"));
                break;
            case "吉安市":
                districts.add(new City("吉州区"));
                districts.add(new City("青原区"));
                break;
            case "宜春市":
                districts.add(new City("袁州区"));
                break;
            case "抚州市":
                districts.add(new City("临川区"));
                districts.add(new City("东乡区"));
                break;
            case "上饶市":
                districts.add(new City("信州区"));
                districts.add(new City("广丰区"));
                districts.add(new City("广信区"));
                break;
            // === 广西壮族自治区 ===
            case "柳州市":
                districts.add(new City("城中区"));
                districts.add(new City("鱼峰区"));
                districts.add(new City("柳南区"));
                districts.add(new City("柳北区"));
                districts.add(new City("柳江区"));
                break;
            case "桂林市":
                districts.add(new City("秀峰区"));
                districts.add(new City("叠彩区"));
                districts.add(new City("象山区"));
                districts.add(new City("七星区"));
                districts.add(new City("雁山区"));
                districts.add(new City("临桂区"));
                break;
            case "梧州市":
                districts.add(new City("万秀区"));
                districts.add(new City("长洲区"));
                districts.add(new City("龙圩区"));
                break;
            case "北海市":
                districts.add(new City("海城区"));
                districts.add(new City("银海区"));
                districts.add(new City("铁山港区"));
                break;
            case "防城港市":
                districts.add(new City("港口区"));
                districts.add(new City("防城区"));
                break;
            case "钦州市":
                districts.add(new City("钦南区"));
                districts.add(new City("钦北区"));
                break;
            case "贵港市":
                districts.add(new City("港北区"));
                districts.add(new City("港南区"));
                districts.add(new City("覃塘区"));
                break;
            case "玉林市":
                districts.add(new City("玉州区"));
                districts.add(new City("福绵区"));
                break;
            case "百色市":
                districts.add(new City("右江区"));
                districts.add(new City("田阳区"));
                break;
            case "贺州市":
                districts.add(new City("八步区"));
                districts.add(new City("平桂区"));
                break;
            case "河池市":
                districts.add(new City("金城江区"));
                districts.add(new City("宜州区"));
                break;
            case "来宾市":
                districts.add(new City("兴宾区"));
                break;
            case "崇左市":
                districts.add(new City("江州区"));
                break;
            // === 海南省 ===
            case "三沙市":
                districts.add(new City("西沙区"));
                districts.add(new City("南沙区"));
                break;
            case "儋州市":
                districts.add(new City("那大镇"));
                break;
            default:
                districts.add(new City("城区"));
                break;
        }
        return districts;
    }

    public CityPicker setOnCityPickerListener(OnCityPickerListener listener) {
        mListener = listener;
        return this;
    }

    public void show() {
        if (mDialog == null) {
            mDialog = new Dialog(mContext, R.style.CityPickerDialogStyle);
            mDialog.setContentView(R.layout.cp_dialog_city_picker);

            Window window = mDialog.getWindow();
            if (window != null) {
                window.setGravity(Gravity.BOTTOM);
                window.setWindowAnimations(R.style.CityPickerAnimation);

                WindowManager.LayoutParams params = window.getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = (int) (mContext.getResources().getDisplayMetrics().heightPixels * 0.6);
                window.setAttributes(params);
            }

            initViews();
            initListeners();
        }

        mDialog.show();
    }

    private void initViews() {
        tvTitle = mDialog.findViewById(R.id.tv_title);
        tvConfirm = mDialog.findViewById(R.id.tv_confirm);
        tvCancel = mDialog.findViewById(R.id.tv_cancel);

        rvProvince = mDialog.findViewById(R.id.rv_province);
        rvCity = mDialog.findViewById(R.id.rv_city);
        rvDistrict = mDialog.findViewById(R.id.rv_district);

        // 初始化省份列表
        rvProvince.setLayoutManager(new LinearLayoutManager(mContext));
        provinceAdapter = new ProvinceListAdapter(mContext, provinceList);
        provinceAdapter.setSelectedIndex(selectedProvinceIndex);
        rvProvince.setAdapter(provinceAdapter);

        // 初始化城市列表
        rvCity.setLayoutManager(new LinearLayoutManager(mContext));
        cityAdapter = new CityListAdapter(mContext, cityList);
        cityAdapter.setSelectedIndex(selectedCityIndex);
        rvCity.setAdapter(cityAdapter);

        // 初始化区/县列表
        rvDistrict.setLayoutManager(new LinearLayoutManager(mContext));
        districtAdapter = new CityListAdapter(mContext, districtList);
        districtAdapter.setSelectedIndex(selectedDistrictIndex);
        rvDistrict.setAdapter(districtAdapter);
    }

    private void initListeners() {
        // 省份点击事件
        provinceAdapter.setOnItemClickListener(position -> {
            selectedProvinceIndex = position;
            selectedCityIndex = 0;
            selectedDistrictIndex = 0;
            provinceAdapter.setSelectedIndex(position);

            // 更新城市列表
            cityList.clear();
            cityList.addAll(provinceList.get(position).getCityList());
            cityAdapter.setSelectedIndex(0);
            cityAdapter.notifyDataSetChanged();

            // 更新区/县列表
            districtList.clear();
            if (!cityList.isEmpty()) {
                districtList.addAll(getDistrictList(cityList.get(0).getName()));
            }
            districtAdapter.setSelectedIndex(0);
            districtAdapter.notifyDataSetChanged();

            updateTitle();
        });

        // 城市点击事件
        cityAdapter.setOnItemClickListener(position -> {
            selectedCityIndex = position;
            selectedDistrictIndex = 0;
            cityAdapter.setSelectedIndex(position);

            // 更新区/县列表
            districtList.clear();
            districtList.addAll(getDistrictList(cityList.get(position).getName()));
            districtAdapter.setSelectedIndex(0);
            districtAdapter.notifyDataSetChanged();

            updateTitle();
        });

        // 区/县点击事件
        districtAdapter.setOnItemClickListener(position -> {
            selectedDistrictIndex = position;
            districtAdapter.setSelectedIndex(position);

            updateTitle();
        });

        // 取消按钮
        tvCancel.setOnClickListener(v -> mDialog.dismiss());

        // 确认按钮
        tvConfirm.setOnClickListener(v -> {
            if (mListener != null) {
                String province = provinceList.get(selectedProvinceIndex).getName();
                String city = cityList.get(selectedCityIndex).getName();
                String district = districtList.isEmpty() ? "" : districtList.get(selectedDistrictIndex).getName();
                mListener.onPicked(province, city, district);
            }
            mDialog.dismiss();
        });

        updateTitle();
    }

    private void updateTitle() {
        if (!provinceList.isEmpty() && !cityList.isEmpty()) {
            String province = provinceList.get(selectedProvinceIndex).getName();
            String city = cityList.get(selectedCityIndex).getName();
            String district = districtList.isEmpty() ? "" : districtList.get(selectedDistrictIndex).getName();
            tvTitle.setText(province + " " + city + " " + district);
        }
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
