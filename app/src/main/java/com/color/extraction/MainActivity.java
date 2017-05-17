package com.color.extraction;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv_rgb;
    private ImageView iv_image;
    private Bitmap bitmap;
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
        iv_image = (ImageView) findViewById(R.id.iv_image);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.demopic);
        iv_image.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();

                int y = (int) event.getY();
                canvas.drawCircle(x, y, 100, point);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //点击处
                    int cColor = bitmap.getPixel(x, y);
                    tvColor.setBackgroundColor(cColor);
                    // 如果你想做的更细致的话 可以把颜色值的R G B 拿到做响应的处理
                    int cr = Color.red(cColor);
                    int cg = Color.green(cColor);
                    int cb = Color.blue(cColor);

                    //左上角
                    int ltColor = bitmap.getPixel(x - distance, y - distance);
                    tvColor.setBackgroundColor(ltColor);
                    // 如果你想做的更细致的话 可以把颜色值的R G B 拿到做响应的处理
                    int ltr = Color.red(ltColor);
                    int ltg = Color.green(ltColor);
                    int ltb = Color.blue(ltColor);

                    //左下角
                    int lbColor = bitmap.getPixel(x - distance, y + distance);
                    tvColor.setBackgroundColor(lbColor);
                    // 如果你想做的更细致的话 可以把颜色值的R G B 拿到做响应的处理
                    int lbr = Color.red(lbColor);
                    int lbg = Color.green(lbColor);
                    int lbb = Color.blue(lbColor);

                    //右上角
                    int rtColor = bitmap.getPixel(x + distance, y - distance);
                    tvColor.setBackgroundColor(rtColor);
                    // 如果你想做的更细致的话 可以把颜色值的R G B 拿到做响应的处理
                    int rtr = Color.red(rtColor);
                    int rtg = Color.green(rtColor);
                    int rtb = Color.blue(rtColor);

                    //右下角
                    int rbColor = bitmap.getPixel(x - distance, y + distance);
                    tvColor.setBackgroundColor(rbColor);
                    // 如果你想做的更细致的话 可以把颜色值的R G B 拿到做响应的处理
                    int rbr = Color.red(rbColor);
                    int rbg = Color.green(rbColor);
                    int rbb = Color.blue(rbColor);
                    int sumr = (int) ((cr + ltr + rbr + lbr + rtr) / 5.0);
                    int sumg = (int) ((cg + ltg + rbg + lbg + rtg) / 5.0);
                    int sumb = (int) ((cb + ltb + rbb + lbb + rtb) / 5.0);

                    tvR.setText(sumr + "");
                    tvG.setText(sumg + "");
                    tvB.setText(sumb + "");
                    iv_image.draw(canvas);
                }
                return true;
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