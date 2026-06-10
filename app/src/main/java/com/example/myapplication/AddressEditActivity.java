package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.api.Api;
import com.example.myapplication.bean.AddressBean;
import com.example.myapplication.util.HttpUtil;
import com.example.myapplication.util.SPUtil;
import com.zaaach.citypicker.CityPicker;

import org.json.JSONObject;

public class AddressEditActivity extends AppCompatActivity {

    private static final String TAG = "AddressEditActivity";
    public static final String EXTRA_ADDRESS_ID = "address_id";

    private EditText etName;
    private EditText etPhone;
    private TextView tvRegion;
    private EditText etDetail;
    private View ivDefault;
    private TextView tvTitle;

    private long addressId = 0;
    private AddressBean address;
    private boolean isDefault = false;
    private String provinceName = "";
    private String cityName = "";
    private String regionName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_edit);

        addressId = getIntent().getLongExtra(EXTRA_ADDRESS_ID, 0);

        initViews();
        initListeners();

        if (addressId > 0) {
            tvTitle.setText("编辑收货地址");
            loadAddressDetail();
        }
    }

    private void initViews() {
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        tvRegion = findViewById(R.id.tv_region);
        etDetail = findViewById(R.id.et_detail);
        ivDefault = findViewById(R.id.iv_default);
        tvTitle = findViewById(R.id.tv_title);

        // 给顶部栏添加状态栏高度的 padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ll_top_bar), (v, insets) -> {
            Insets statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(v.getPaddingLeft(), statusBar.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });
    }

    private void initListeners() {
        // 返回按钮
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());

        // 选择地区
        findViewById(R.id.ll_region).setOnClickListener(v -> showCityPicker());

        // 设为默认
        findViewById(R.id.ll_default).setOnClickListener(v -> {
            isDefault = !isDefault;
            ivDefault.setBackgroundResource(isDefault ?
                    R.drawable.bg_radio_checked : R.drawable.bg_radio_unchecked);
        });

        // 保存按钮
        findViewById(R.id.tv_save).setOnClickListener(v -> saveAddress());
    }

    /**
     * 显示城市选择器
     */
    private void showCityPicker() {
        CityPicker picker = new CityPicker(this);
        picker.setOnCityPickerListener((province, city, district) -> {
            provinceName = province;
            cityName = city;
            regionName = district;
            tvRegion.setText(province + " " + city + " " + district);
            tvRegion.setTextColor(getResources().getColor(R.color.black));
        });
        picker.show();
    }

    /**
     * 加载地址详情（编辑模式）
     */
    private void loadAddressDetail() {
        new Thread(() -> {
            try {
                String url = Api.ADDRESS_LIST;
                String token = SPUtil.getToken(AddressEditActivity.this);
                String response = HttpUtil.get(url, token);
                JSONObject json = new JSONObject(response);

                if (json.getInt("resultCode") == 200) {
                    // 在列表中查找对应的地址
                    org.json.JSONArray data = json.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject item = data.getJSONObject(i);
                        if (item.getLong("addressId") == addressId) {
                            address = new AddressBean();
                            address.setAddressId(item.getLong("addressId"));
                            address.setUserName(item.getString("userName"));
                            address.setUserPhone(item.getString("userPhone"));
                            address.setProvinceName(item.getString("provinceName"));
                            address.setCityName(item.getString("cityName"));
                            address.setRegionName(item.getString("regionName"));
                            address.setDetailAddress(item.getString("detailAddress"));
                            address.setDefaultFlag(item.getInt("defaultFlag"));
                            break;
                        }
                    }

                    if (address != null) {
                        runOnUiThread(() -> bindDataToView());
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "loadAddressDetail error", e);
                runOnUiThread(() -> Toast.makeText(this, "加载地址失败", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    /**
     * 绑定数据到视图
     */
    private void bindDataToView() {
        etName.setText(address.getUserName());
        etPhone.setText(address.getUserPhone());

        provinceName = address.getProvinceName();
        cityName = address.getCityName();
        regionName = address.getRegionName();
        tvRegion.setText(provinceName + " " + cityName + " " + regionName);
        tvRegion.setTextColor(getResources().getColor(R.color.black));

        etDetail.setText(address.getDetailAddress());

        isDefault = address.getDefaultFlag() == 1;
        ivDefault.setBackgroundResource(isDefault ?
                R.drawable.bg_radio_checked : R.drawable.bg_radio_unchecked);
    }

    /**
     * 保存地址
     */
    private void saveAddress() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String detail = etDetail.getText().toString().trim();

        // 验证输入
        if (name.isEmpty()) {
            Toast.makeText(this, "请输入收货人姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.isEmpty()) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (provinceName.isEmpty()) {
            Toast.makeText(this, "请选择省市区", Toast.LENGTH_SHORT).show();
            return;
        }
        if (detail.isEmpty()) {
            Toast.makeText(this, "请输入详细地址", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                JSONObject body = new JSONObject();
                if (addressId > 0) {
                    body.put("addressId", addressId);
                }
                body.put("userName", name);
                body.put("userPhone", phone);
                body.put("provinceName", provinceName);
                body.put("cityName", cityName);
                body.put("regionName", regionName);
                body.put("detailAddress", detail);
                body.put("defaultFlag", isDefault ? 1 : 0);

                String url = addressId > 0 ? Api.ADDRESS_UPDATE : Api.ADDRESS_ADD;
                String token = SPUtil.getToken(AddressEditActivity.this);
                String response = HttpUtil.post(url, body.toString(), token);
                JSONObject json = new JSONObject(response);

                runOnUiThread(() -> {
                    if (json.optInt("resultCode") == 200) {
                        Toast.makeText(this, addressId > 0 ? "修改成功" : "添加成功", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(this, json.optString("message", "保存失败"), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "saveAddress error", e);
                runOnUiThread(() -> Toast.makeText(this, "网络请求失败", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
