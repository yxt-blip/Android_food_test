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

        // 设置新鲜度百分比和状态文本
        TextView percentageTextView = view.findViewById(R.id.tv_percentage);
        percentageTextView.setText(freshnessPercentage + "%");

        // 根据新鲜度百分比设置文本颜色
        int textColor;
        if (freshnessPercentage >= 70) {
            textColor = 0xFF27AE60; // 绿色 - 新鲜
        } else if (freshnessPercentage >= 30) {
            textColor = 0xFFF1C40F; // 黄色 - 即将过期
        } else {
            textColor = 0xFFE74C3C; // 红色 - 已过期或接近过期
        }
        percentageTextView.setTextColor(textColor);

        // 设置箭头位置
        ImageView indicatorArrow = view.findViewById(R.id.iv_indicator);
        View colorBar = view.findViewById(R.id.view_color_bar);
        
        colorBar.post(() -> {
            // 计算箭头应该在的位置
            int barWidth = colorBar.getWidth();
            float position = (float) freshnessPercentage / 100 * barWidth;
            
            // 确保箭头不会超出边界
            int arrowWidth = indicatorArrow.getWidth();
            int maxPosition = barWidth - arrowWidth;
            int finalPosition = Math.min(Math.max(0, (int) position - arrowWidth / 2), maxPosition);
            
            // 设置箭头的左边距
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) indicatorArrow.getLayoutParams();
            params.leftMargin = finalPosition;
            indicatorArrow.setLayoutParams(params);
        });

        // 设置食品详情
        TextView foodNameTextView = view.findViewById(R.id.tv_food_name);
        TextView purchaseDateTextView = view.findViewById(R.id.tv_purchase_date);
        TextView expiryDateTextView = view.findViewById(R.id.tv_expiry_date);
        TextView storageLocationTextView = view.findViewById(R.id.tv_storage_location);

        foodNameTextView.setText(foodName);
        purchaseDateTextView.setText(purchaseDate);
        expiryDateTextView.setText(expiryDate);
        storageLocationTextView.setText(storageLocation);
    }
} 