package com.example.xushuailong.mygrocerystore.scan.scan1;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.xushuailong.mygrocerystore.scan.util.HardWare;
import com.example.xushuailong.mygrocerystore.scan.util.WccConfigure;
import com.example.xushuailong.mygrocerystore.R;
public final class ViewfinderView extends View {
    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    String TAG = "ViewfinderView";
    private int scannerAlpha;
    private static final long ANIMATION_DELAY = 1000L;

    private final Paint paint;
    private final Rect box;
    private final int maskColor;
    private int laserColor;
    private final int outRangeColor;
    private int stopDraw;
    private int width;
    private int height;
    private Rect frame;
    private Rect line_v;

    private int m1;
    private int m2;
    private int m3;
    private int m4;
    private int line_w;
    private int pen = 4;
    int middle_v;
    int middle_h;
    float newHeight;
    int newTop;
    int newBottom;
    int gapDistance = 0;

    public Rect scanLine;
    private boolean colorOn = false;
    private Context con;
    private int i = 0;

    //用于画弧的四个边界矩形
    private RectF leftTopRect, leftBottomRect, rightTopRect, rightBottomRect;

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        con = context;
        SharedPreferences sharepre = PreferenceManager.getDefaultSharedPreferences(context);
//        if (!HardWare.needRotateActivity()) {
            int left = sharepre.getInt("camera_rotate_left", 0);
            int top = sharepre.getInt("camera_rotate_top", 0);
            int right = sharepre.getInt("camera_rotate_right", 0);
            int bottom = sharepre.getInt("camera_rotate_bottom", 0);
            frame = new Rect(left, top, right, bottom);
//        } else {
//            int left = sharepre.getInt("activity_rotate_left", 0);
//            int top = sharepre.getInt("activity_rotate_top", 0);
//            int right = sharepre.getInt("activity_rotate_right", 0);
//            int bottom = sharepre.getInt("activity_rotate_bottom", 0);
//            frame = new Rect(left, top, right, bottom);
//        }

        paint = new Paint();
        box = new Rect();

        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        scannerAlpha = 0;// zxing_lib

        // 扫描线固定专用
        outRangeColor = resources.getColor(R.color.viewfinder_outrange);
        laserColor = outRangeColor;
        // 扫描线上下移动专用
//        outRangeColor = resources.getColor(R.color.white);
//        laserColor = resources.getColor(R.color.viewfinder_outrange);

        stopDraw = 0;

        line_v = new Rect();

        middle_v = frame.width() / 2 + frame.left;
        middle_h = frame.height() / 2 + frame.top;

        newHeight = frame.height();
        newTop = (int) (frame.exactCenterY() - newHeight * 0.5F);
        newBottom = (int) (newTop + newHeight);
        gapDistance = frame.height() / 10;

        m1 = frame.left + 1 + gapDistance;
        m2 = frame.top + 1 + gapDistance;
        m3 = frame.right - 1 - gapDistance;
        m4 = frame.bottom - 1 - gapDistance;

        scanLine = new Rect();
        scanLine.set(m1, m2, m3, m4);

        leftTopRect = new RectF();
        leftBottomRect = new RectF();
        rightTopRect = new RectF();
        rightBottomRect = new RectF();
    }

    // gc_lib
    public void stopDraw() {
        stopDraw = 1;
        invalidate();
    }

    // end

    @Override
    public void onDraw(Canvas canvas) {
        if (stopDraw == 1)
            return;
        if (frame == null)
            return;

        width = canvas.getWidth();
        height = canvas.getHeight();

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(maskColor);

//        if (HardWare.needRotateActivity()) {
//                if (colorOn || WccConfigure.getColorMode(con)) {
//                    box.set(0, frame.top, m1, frame.bottom + 1);
//                    canvas.drawRect(box, paint);
//                    box.set(m3, frame.top, width, frame.bottom + 1);
//                    canvas.drawRect(box, paint);
//                } else {
//                box.set(0, frame.top, frame.left, frame.bottom + 1);
//                canvas.drawRect(box, paint);
//                box.set(frame.right + 1, frame.top, width, frame.bottom + 1);
//                canvas.drawRect(box, paint);
//            }
//        } else {
            if (colorOn || WccConfigure.getColorMode(con)) {
                box.set(0, 0, width, m2);
                canvas.drawRect(box, paint);
                box.set(0, m4, width, height);
                canvas.drawRect(box, paint);
            } else {
                box.set(0, 0, width, frame.top);
                canvas.drawRect(box, paint);
                box.set(0, frame.bottom, width, height);
                canvas.drawRect(box, paint);
            }
//        }

        line_w = width >> 4;


        paint.setColor(getResources().getColor(R.color.white));
        paint.setStrokeWidth(4.0F);
        paint.setAlpha(200);

        //UI三，弧线比较小，接一段直线
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        leftTopRect.set(m1, m2, m1 + line_w, m2 + line_w);
        canvas.drawArc(leftTopRect, 180, 90, false, paint);
        canvas.drawLine(m1 + line_w / 2, m2, m1 + line_w, m2, paint);
        canvas.drawLine(m1, m2 + line_w / 2, m1, m2 + line_w, paint);

        rightTopRect.set(m3 - line_w, m2, m3, m2 + line_w);
        canvas.drawArc(rightTopRect, 270, 90, false, paint);
        canvas.drawLine(m3 - line_w, m2, m3 - line_w / 2, m2, paint);
        canvas.drawLine(m3, m2 + line_w / 2, m3, m2 + line_w, paint);

        rightBottomRect.set(m3 - line_w, m4 - line_w, m3, m4);
        canvas.drawArc(rightBottomRect, 0, 90, false, paint);
        canvas.drawLine(m3, m4 - line_w, m3, m4 - line_w / 2, paint);
        canvas.drawLine(m3 - line_w, m4, m3 - line_w / 2, m4, paint);

        leftBottomRect.set(m1, m4 - line_w, m1 + line_w, m4);
        canvas.drawArc(leftBottomRect, 90, 90, false, paint);
        canvas.drawLine(m1 + line_w / 2, m4, m1 + line_w, m4, paint);
        canvas.drawLine(m1, m4 - line_w, m1, m4 - line_w / 2, paint);

        //}

        // Draw a red "laser scanner" line through the middle to show decoding
        // is active
        paint.setColor(laserColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2.0F);
        if (laserColor == outRangeColor) {
            paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
            scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
        } else
            paint.setAlpha(200);

//        if (HardWare.needRotateActivity()) {
//            int offset = frame.height() / 6;
//            box.set(middle_v - 3, (newTop + offset + gapDistance), middle_v + 3, (newBottom - offset - gapDistance));
//            canvas.drawRect(box, paint);
//
//            postInvalidateDelayed(ANIMATION_DELAY, box.left, box.top, box.right, box.bottom);
//        } else {
            // 固定扫描线
            int offset = frame.width() / 6;
            box.set(frame.left + gapDistance + offset, middle_h - 3, frame.right - gapDistance - offset, middle_h + 3);
            canvas.drawRect(box, paint);
            postInvalidateDelayed(ANIMATION_DELAY, box.left, box.top, box.right, box.bottom);

//            // V8.8将扫描线改为上下移动
//            int offset = frame.width() / 6;
//            if ((i += 3) < frame.bottom - frame.top - gapDistance * 2) {
//                box.set(frame.left + gapDistance + offset, frame.top + gapDistance + i - 3, frame.right - gapDistance - offset, frame.top + gapDistance + 3 + i);
//                canvas.drawRect(box, paint);
//                postInvalidate(box.left, box.top, box.right, box.bottom);
//            } else {
//                i = 0;
//            }
//            postInvalidateDelayed(ANIMATION_DELAY, box.left, box.top, box.right, box.bottom);
//        }
    }

    public void setColorOnOff(boolean v) {
        colorOn = v;
    }

    public void drawViewfinder() {
        stopDraw = 0;
    }

}