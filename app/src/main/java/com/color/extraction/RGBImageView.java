package com.color.extraction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.hdl.elog.ELog;

/**
 * RGB颜色提取
 */

public class RGBImageView extends ImageView {
    private Paint mPaint;
    private Bitmap bitmap;
    private int distance = 50 / 2;
    private int smallRudius = 5;
    private int bigRudius = 10;

    private OnClickListener listener;
    private float curX, curY;
    private float ltX, ltY;
    private float lbX, lbY;
    private float rtX, rtY;
    private float rbX, rbY;

    public RGBImageView(Context context) {
        this(context, null);
    }

    public RGBImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RGBImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        bitmap = ((BitmapDrawable) this.getDrawable()).getBitmap();
        mPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(-getMeasuredWidth() / 2, 0, getMeasuredWidth() / 2, 0, mPaint);
        canvas.drawLine(0, getMeasuredHeight() / 2, 0, getMeasuredHeight() / 2, mPaint);
        if (curX != 0) {
            canvas.drawCircle(curX, curY, bigRudius, mPaint);
            canvas.drawCircle(ltX, ltY, smallRudius, mPaint);
            canvas.drawCircle(lbX, lbY, smallRudius, mPaint);
            canvas.drawCircle(rtX, rtY, smallRudius, mPaint);
            canvas.drawCircle(rbX, rbY, smallRudius, mPaint);
        }
    }

    /**
     * 获取平均颜色
     *
     * @param x
     * @param y
     * @return
     */
    public int[] getAvgColor(float x, float y, int redius) {
        int x1 = (int) (x - redius / 1.44);
        int y1 = (int) (y - redius / 1.44);
        int sumr = 0;
        int sumg = 0;
        int sumb = 0;
        int count = 0;
        for (int i = x1; i < x + redius / 1.44; i++) {
            for (int j = y1; j < y + redius / 1.44; j++) {
                count++;
                sumr += Color.red(bitmap.getPixel(i, j));
                sumg += Color.green(bitmap.getPixel(i, j));
                sumb += Color.blue(bitmap.getPixel(i, j));
            }
        }
        int buf[] = {sumr / count, sumg / count, sumb / count};
        return buf;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                curY = y;
                curX = x;
                //点击处
                int buf[] = getAvgColor(x, y, bigRudius);
                // 如果你想做的更细致的话 可以把颜色值的R G B 拿到做响应的处理
                int cr = buf[0];
                int cg = buf[1];
                int cb = buf[2];

                //左上角
                ltX = x - distance;
                ltY = y - distance;
                int ltColor[] = getAvgColor(ltX, ltY, smallRudius);


                int ltr = ltColor[0];
                int ltg = ltColor[1];
                int ltb = ltColor[2];


                //左下角
                lbX = x - distance;
                lbY = y + distance;

                int lbColor[] = getAvgColor(lbX, lbY, smallRudius);
                // 如果你想做的更细致的话 可以把颜色值的R G B 拿到做响应的处理
                int lbr = lbColor[0];
                int lbg = lbColor[1];
                int lbb = lbColor[2];
                //右上角
                rtX = x + distance;
                rtY = y - distance;
                int rtColor[] = getAvgColor(rtX, rtY, smallRudius);
                // 如果你想做的更细致的话 可以把颜色值的R G B 拿到做响应的处理
                int rtr = rtColor[0];
                int rtg = rtColor[1];
                int rtb = rtColor[2];
                //右下角
                rbX = x + distance;
                rbY = y + distance;
                int rbColor[] = getAvgColor(rbX, rbY, smallRudius);
                // 如果你想做的更细致的话 可以把颜色值的R G B 拿到做响应的处理
                int rbr = rbColor[0];
                int rbg = rbColor[1];
                int rbb = rbColor[2];
                int sumr = (int) ((cr + ltr + rbr + lbr + rtr) / 5.0);
                int sumg = (int) ((cg + ltg + rbg + lbg + rtg) / 5.0);
                int sumb = (int) ((cb + ltb + rbb + lbb + rtb) / 5.0);
                listener.onClick(Color.argb(255,sumr,sumg,sumb), sumr, sumg, sumb);
                ELog.hdl("圆心坐标为：" + curX + "--" + curY);
                postInvalidate();
            }
        }
        return true;
    }


    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    interface OnClickListener {
        void onClick(int color, int r, int g, int b);
    }
}
