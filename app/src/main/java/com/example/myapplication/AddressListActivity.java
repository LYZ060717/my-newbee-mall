package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.api.Api;
import com.example.myapplication.bean.AddressBean;
import com.example.myapplication.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddressListActivity extends AppCompatActivity {

    private static final String TAG = "AddressListActivity";
    public static final String EXTRA_SELECT_MODE = "select_mode";
    public static final String EXTRA_SELECTED_ID = "selected_id";
    public static final int REQUEST_EDIT = 100;
    public static final int RESULT_SELECT = 200;

    private RecyclerView rvAddress;
    private LinearLayout llEmpty;

    private boolean isSelectMode = false;
    private long selectedAddressId = 0;
    private List<AddressBean> addressList = new ArrayList<>();
    private AddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        isSelectMode = getIntent().getBooleanExtra(EXTRA_SELECT_MODE, false);
        selectedAddressId = getIntent().getLongExtra(EXTRA_SELECTED_ID, 0);

        initViews();
        initListeners();
        loadAddressList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAddressList();
    }

    private void initViews() {
        rvAddress = findViewById(R.id.rv_address);
        llEmpty = findViewById(R.id.ll_empty);

        // 给顶部栏添加状态栏高度的 padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ll_top_bar), (v, insets) -> {
            Insets statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(v.getPaddingLeft(), statusBar.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        // 初始化 RecyclerView
        rvAddress.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddressAdapter();
        rvAddress.setAdapter(adapter);
    }

    private void initListeners() {
        // 返回按钮
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());

        // 新增地址
        findViewById(R.id.tv_add_address).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddressEditActivity.class);
            startActivityForResult(intent, REQUEST_EDIT);
        });
    }

    /**
     * 加载地址列表
     */
    private void loadAddressList() {
        new Thread(() -> {
            try {
                String response = HttpUtil.get(Api.ADDRESS_LIST);
                JSONObject json = new JSONObject(response);

                if (json.getInt("resultCode") == 200) {
                    JSONArray data = json.getJSONArray("data");
                    addressList.clear();

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject item = data.getJSONObject(i);
                        AddressBean address = new AddressBean();
                        address.setAddressId(item.getLong("addressId"));
                        address.setUserName(item.getString("userName"));
                        address.setUserPhone(item.getString("userPhone"));
                        address.setProvinceName(item.getString("provinceName"));
                        address.setCityName(item.getString("cityName"));
                        address.setRegionName(item.getString("regionName"));
                        address.setDetailAddress(item.getString("detailAddress"));
                        address.setDefaultFlag(item.getInt("defaultFlag"));
                        addressList.add(address);
                    }

                    runOnUiThread(() -> showResult());
                } else {
                    runOnUiThread(() -> showEmpty());
                }
            } catch (Exception e) {
                Log.e(TAG, "loadAddressList error", e);
                runOnUiThread(() -> showEmpty());
            }
        }).start();
    }

    private void showResult() {
        if (addressList.isEmpty()) {
            llEmpty.setVisibility(View.VISIBLE);
            rvAddress.setVisibility(View.GONE);
        } else {
            llEmpty.setVisibility(View.GONE);
            rvAddress.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

    private void showEmpty() {
        llEmpty.setVisibility(View.VISIBLE);
        rvAddress.setVisibility(View.GONE);
    }

    /**
     * 删除地址
     */
    private void deleteAddress(AddressBean address) {
        new Thread(() -> {
            try {
                JSONObject body = new JSONObject();
                body.put("addressId", address.getAddressId());

                String response = HttpUtil.post(Api.ADDRESS_DELETE, body.toString());
                JSONObject json = new JSONObject(response);

                runOnUiThread(() -> {
                    if (json.optInt("resultCode") == 200) {
                        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                        loadAddressList();
                    } else {
                        Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "deleteAddress error", e);
                runOnUiThread(() -> Toast.makeText(this, "网络请求失败", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    /**
     * 设置默认地址
     */
    private void setDefaultAddress(AddressBean address) {
        new Thread(() -> {
            try {
                JSONObject body = new JSONObject();
                body.put("addressId", address.getAddressId());

                String response = HttpUtil.post(Api.ADDRESS_DEFAULT, body.toString());
                JSONObject json = new JSONObject(response);

                runOnUiThread(() -> {
                    if (json.optInt("resultCode") == 200) {
                        Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
                        loadAddressList();
                    } else {
                        Toast.makeText(this, "设置失败", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "setDefaultAddress error", e);
                runOnUiThread(() -> Toast.makeText(this, "网络请求失败", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK) {
            loadAddressList();
        }
    }

    /**
     * 地址列表适配器
     */
    class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_address, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            AddressBean address = addressList.get(position);

            // 设置收货人信息
            holder.tvName.setText(address.getUserName());
            holder.tvPhone.setText(address.getUserPhone());

            // 设置地址
            holder.tvAddress.setText(address.getFullAddress());

            // 设置默认状态
            if (address.getDefaultFlag() == 1) {
                holder.ivDefault.setBackgroundResource(R.drawable.bg_radio_checked);
            } else {
                holder.ivDefault.setBackgroundResource(R.drawable.bg_radio_unchecked);
            }

            // 设为默认
            holder.llDefault.setOnClickListener(v -> setDefaultAddress(address));

            // 编辑
            holder.tvEdit.setOnClickListener(v -> {
                Intent intent = new Intent(AddressListActivity.this, AddressEditActivity.class);
                intent.putExtra(AddressEditActivity.EXTRA_ADDRESS_ID, address.getAddressId());
                startActivityForResult(intent, REQUEST_EDIT);
            });

            // 删除
            holder.tvDelete.setOnClickListener(v -> deleteAddress(address));

            // 选择模式下的点击事件
            if (isSelectMode) {
                holder.itemView.setOnClickListener(v -> {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("addressId", address.getAddressId());
                    resultIntent.putExtra("address", address.getFullAddress());
                    resultIntent.putExtra("userName", address.getUserName());
                    resultIntent.putExtra("userPhone", address.getUserPhone());
                    setResult(RESULT_SELECT, resultIntent);
                    finish();
                });
            }
        }

        @Override
        public int getItemCount() {
            return addressList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName;
            TextView tvPhone;
            TextView tvAddress;
            View ivDefault;
            LinearLayout llDefault;
            TextView tvEdit;
            TextView tvDelete;

            ViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tv_name);
                tvPhone = itemView.findViewById(R.id.tv_phone);
                tvAddress = itemView.findViewById(R.id.tv_address);
                ivDefault = itemView.findViewById(R.id.iv_default);
                llDefault = itemView.findViewById(R.id.ll_default);
                tvEdit = itemView.findViewById(R.id.tv_edit);
                tvDelete = itemView.findViewById(R.id.tv_delete);
            }
        }
    }
}
