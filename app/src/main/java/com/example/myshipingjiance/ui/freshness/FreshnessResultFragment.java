package com.example.myshipingjiance.ui.freshness;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myshipingjiance.R;

public class FreshnessResultFragment extends Fragment {

    private static final String ARG_FRESHNESS = "freshness_percentage";
    private static final String ARG_FOOD_NAME = "food_name";
    private static final String ARG_PURCHASE_DATE = "purchase_date";
    private static final String ARG_EXPIRY_DATE = "expiry_date";
    private static final String ARG_STORAGE_LOCATION = "storage_location";

    private int freshnessPercentage;
    private String foodName;
    private String purchaseDate;
    private String expiryDate;
    private String storageLocation;

    public static FreshnessResultFragment newInstance(int freshnessPercentage, String foodName,
                                                     String purchaseDate, String expiryDate,
                                                     String storageLocation) {
        FreshnessResultFragment fragment = new FreshnessResultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FRESHNESS, freshnessPercentage);
        args.putString(ARG_FOOD_NAME, foodName);
        args.putString(ARG_PURCHASE_DATE, purchaseDate);
        args.putString(ARG_EXPIRY_DATE, expiryDate);
        args.putString(ARG_STORAGE_LOCATION, storageLocation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            freshnessPercentage = getArguments().getInt(ARG_FRESHNESS, 0);
            foodName = getArguments().getString(ARG_FOOD_NAME, "");
            purchaseDate = getArguments().getString(ARG_PURCHASE_DATE, "");
            expiryDate = getArguments().getString(ARG_EXPIRY_DATE, "");
            storageLocation = getArguments().getString(ARG_STORAGE_LOCATION, "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.freshness_result_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    
        // 设置返回按钮点击事件
        view.findViewById(R.id.iv_back).setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
        
        // 获取状态文本视图和箭头组件
        TextView statusTextView = view.findViewById(R.id.tv_status);
        ImageView indicatorArrow = view.findViewById(R.id.iv_indicator);
        View colorBar = view.findViewById(R.id.view_color_bar);

        // 初始化拖动功能
        indicatorArrow.setOnTouchListener(new View.OnTouchListener() {
            private float lastX;
            private int originalLeft;
            private boolean isDragging = false;

            @Override
            public boolean onTouch(View v, android.view.MotionEvent event) {
                switch (event.getAction()) {
                    case android.view.MotionEvent.ACTION_DOWN:
                        lastX = event.getRawX();
                        originalLeft = indicatorArrow.getLeft();
                        isDragging = true;
                        return true;

                    case android.view.MotionEvent.ACTION_MOVE:
                        if (isDragging) {
                            float delta = event.getRawX() - lastX;
                            int newLeft = (int) (originalLeft + delta);
                            
                            // 限制箭头在颜色条范围内
                            int maxLeft = colorBar.getWidth() - indicatorArrow.getWidth();
                            newLeft = Math.max(0, Math.min(newLeft, maxLeft));
                            
                            // 更新箭头位置
                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) indicatorArrow.getLayoutParams();
                            params.leftMargin = newLeft;
                            indicatorArrow.setLayoutParams(params);
                            
                            // 计算新鲜度百分比
                            float percentage = (float) newLeft / maxLeft * 100;
                            
                            // 更新状态文本和颜色
                            int textColor;
                            String statusText;
                            
                            if (percentage >= 70) {
                                textColor = 0xFF27AE60; // 绿色
                                statusText = "新鲜";
                            } else if (percentage >= 40) {
                                textColor = 0xFFF1C40F; // 黄色
                                statusText = "较新鲜";
                            } else if (percentage >= 20) {
                                textColor = 0xFFF39C12; // 橙色
                                statusText = "即将过期";
                            } else {
                                textColor = 0xFFE74C3C; // 红色
                                statusText = "已过期";
                            }
                            
                            statusTextView.setTextColor(textColor);
                            statusTextView.setText(statusText);
                        }
                        return true;

                    case android.view.MotionEvent.ACTION_UP:
                    case android.view.MotionEvent.ACTION_CANCEL:
                        isDragging = false;
                        return true;
                }
                return false;
            }
        });

        // 设置初始箭头位置
        colorBar.post(() -> {
            int barWidth = colorBar.getWidth();
            float position = 0.75f * barWidth;
            
            int arrowWidth = indicatorArrow.getWidth();
            int maxPosition = barWidth - arrowWidth;
            int finalPosition = Math.min(Math.max(0, (int) position - arrowWidth / 2), maxPosition);
            
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) indicatorArrow.getLayoutParams();
            params.leftMargin = finalPosition;
            indicatorArrow.setLayoutParams(params);
            
            // 设置初始状态
            statusTextView.setTextColor(0xFF27AE60);
            statusTextView.setText("新鲜");
        });
        
        // 注释掉尝试查找不存在ID的代码
        // 如果要显示食品详情，需要先在布局中添加对应的视图
        /*
        try {
            TextView foodNameTextView = view.findViewById(R.id.tv_food_name);
            TextView purchaseDateTextView = view.findViewById(R.id.tv_purchase_date);
            TextView expiryDateTextView = view.findViewById(R.id.tv_expiry_date);
            TextView storageLocationTextView = view.findViewById(R.id.tv_storage_location);
            
            if (foodNameTextView != null) foodNameTextView.setText(foodName);
            if (purchaseDateTextView != null) purchaseDateTextView.setText(purchaseDate);
            if (expiryDateTextView != null) expiryDateTextView.setText(expiryDate);
            if (storageLocationTextView != null) storageLocationTextView.setText(storageLocation);
        } catch (Exception e) {
            // 忽略未找到的视图
        }
        */
    }
}