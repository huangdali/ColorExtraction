package com.color.extraction;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv_rgb;
    private RGBImageView iv_image;
    private String TAG = "RGBActivity";
    private Button btnColor;
    public static final int NONE = 0;
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果
    public static final String IMAGE_UNSPECIFIED = "image/*";
    public static final String TEMP_JPG_NAME = "temp.jpg";
    private TextView tvR, tvB, tvG, tvColor;
    private Canvas canvas;
    private Paint point;
    private int distance = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        canvas = new Canvas();
        point = new Paint();
        point.setAntiAlias(true);
        point.setColor(Color.WHITE);
        iv_image = (RGBImageView) findViewById(R.id.iv_image);
        iv_image.setOnClickListener(new RGBImageView.OnClickListener() {
            @Override
            public void onClick(int color, int r, int g, int b) {
                tvR.setText(r + "");
                tvG.setText(g + "");
                tvB.setText(b + "");
                tvColor.setBackgroundColor(color);
            }
        });
    }

    private void initView() {
        tvR = (TextView) findViewById(R.id.tv_R);
        tvG = (TextView) findViewById(R.id.tv_G);
        tvB = (TextView) findViewById(R.id.tv_B);
        tvColor = (TextView) findViewById(R.id.tv_color);
    }

}