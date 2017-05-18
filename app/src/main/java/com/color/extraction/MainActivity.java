package com.color.extraction;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hdl.elog.ELog;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;

import java.util.List;

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
    private FunctionOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FunctionOptions options = new FunctionOptions.Builder()
                .setType(FunctionConfig.TYPE_IMAGE)
                .setCompress(true)
                .setGrade(Luban.THIRD_GEAR)
                .create();
        PictureConfig.getInstance().init(options);

        initView();
        canvas = new Canvas();
        point = new Paint();
        point.setAntiAlias(true);
        point.setColor(Color.WHITE);

        initOptions();
    }

    private void initOptions() {
        options = new FunctionOptions.Builder()
                .setType(FunctionConfig.TYPE_IMAGE) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
                .setCropMode(FunctionConfig.CROP_MODEL_16_9) // 裁剪模式 默认、1:1、3:4、3:2、16:9
                .setCompress(true) //是否压缩
                .setEnablePixelCompress(false) //是否启用像素压缩
                .setEnableQualityCompress(false) //是否启质量压缩
                .setMaxSelectNum(1) // 可选择图片的数量
                .setSelectMode(FunctionConfig.MODE_SINGLE) // 单选 or 多选 FunctionConfig.MODE_SINGLE FunctionConfig.MODE_MULTIPLE
                .setShowCamera(true) //是否显示拍照选项 这里自动根据type 启动拍照或录视频
                .setEnablePreview(true) // 是否打开预览选项
                .setEnableCrop(true) // 是否打开剪切选项
                .setCircularCut(false)// 是否采用圆形裁剪
                .setCropW(500) // cropW-->裁剪宽度 值不能小于100  如果值大于图片原始宽高 将返回原图大小
                .setCropH(800) // cropH-->裁剪高度 值不能小于100 如果值大于图片原始宽高 将返回原图大小
                .setImageSpanCount(4) // 每行个数
                .setCompressFlag(1) // 1 系统自带压缩 2 luban压缩
                .setNumComplete(false) // 0/9 完成  样式
                .create();
    }

    private void initView() {
        tvR = (TextView) findViewById(R.id.tv_R);
        tvG = (TextView) findViewById(R.id.tv_G);
        tvB = (TextView) findViewById(R.id.tv_B);
        tvColor = (TextView) findViewById(R.id.tv_color);
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

    public void onTakePhoto(View view) {
        PictureConfig.getInstance().openPhoto(this, resultCallback);
    }

    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            LocalMedia media = resultList.get(0);
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                String path = media.getCutPath();
                ELog.hdl("" + path);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                iv_image.setImageBitmap(bitmap);
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                String path = media.getCompressPath();
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                iv_image.setImageBitmap(bitmap);
                ELog.hdl("" + path);
            } else {
                // 原图地址
                String path = media.getPath();
                ELog.hdl("" + path);
            }
        }

        @Override
        public void onSelectSuccess(LocalMedia media) {
            ELog.hdl("" + media);
        }
    };
}