package com.wochacha.scan;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;

import com.example.xushuailong.mygrocerystore.R;
import com.example.xushuailong.mygrocerystore.utils.ScreenUtil;

public final class ViewfinderView extends View {
    private static final int[] SCANNER_ALPHA = {0, 32, 64, 96, 128, 160, 192, 224, 255, 224, 192, 160, 128, 96, 64, 32};
    private int scannerAlpha;
    private static final long ANIMATION_DELAY = 80L;

    private final Paint paintAnim;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint eraser = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap laserBitmapBottom;
    private Bitmap laserBitmapTop;
    Bitmap tempBottom;
    Bitmap tempTop;
    int centerHeight;

    private int stopDraw;
    private Rect frame;

    int diff;

    private int maskColor = Color.argb(180, 0, 0, 0);
    private Context context;

    int cornerLength = 100;
    int cornerLineWidth = 6;


    {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        paint.setColor(getContext().getResources().getColor(R.color.scan_info_bg));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);

        laserBitmapBottom = BitmapFactory.decodeResource(getResources(), R.drawable.laser_bottom);
        laserBitmapTop = BitmapFactory.decodeResource(getResources(), R.drawable.laser_top);

        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        SharedPreferences sharepre = PreferenceManager.getDefaultSharedPreferences(context);

        int left = sharepre.getInt("camera_rotate_left", 0);
        int top = sharepre.getInt("camera_rotate_top", 0);
        int right = sharepre.getInt("camera_rotate_right", 0);
        int bottom = sharepre.getInt("camera_rotate_bottom", 0);

        frame = new Rect(left, top, right, bottom);


        paintAnim = new Paint();

        scannerAlpha = 0;// zxing_lib


        stopDraw = 0;




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

        canvas.drawColor(maskColor);
        if (diff == 0) {
            diff = ScreenUtil.getScreenHeight(context) - getHeight();

            frame.top = frame.top - diff;
            frame.bottom = frame.bottom - diff;
        }
        paint.setStrokeWidth(ScreenUtil.dip2px(context, 1));
        canvas.drawRect(frame, paint);
        canvas.drawRect(frame, eraser);
        drawCorners(canvas, frame);


        if (centerHeight == 0) {
            centerHeight = frame.height() / 2 + frame.top;
        }
        if (tempBottom == null) {
            tempBottom = ThumbnailUtils.extractThumbnail(laserBitmapBottom, frame.width(), laserBitmapBottom.getHeight());
        }
        if (tempTop == null) {
            tempTop = ThumbnailUtils.extractThumbnail(laserBitmapTop, frame.width(), laserBitmapTop.getHeight());
        }

        paintAnim.setAlpha(255);
        canvas.drawBitmap(tempTop, frame.left, centerHeight - tempTop.getHeight() / 2, paintAnim);
        paintAnim.setAlpha(SCANNER_ALPHA[scannerAlpha]);
        scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
        canvas.drawBitmap(tempBottom, frame.left, centerHeight - tempBottom.getHeight() / 2, paintAnim);

        postInvalidateDelayed(ANIMATION_DELAY);

    }


    public void drawViewfinder() {
        stopDraw = 0;
    }

    private void drawCorners(Canvas canvas, Rect frameRect) {
        paint.setStrokeWidth(cornerLineWidth);

        int halfCornerLineWidth = cornerLineWidth / 2;

        // left top
        drawLine(canvas, frameRect.left, frameRect.top + halfCornerLineWidth, cornerLength, 0);
        drawLine(canvas, frameRect.left + halfCornerLineWidth, frameRect.top, 0, cornerLength);

        // right top
        drawLine(canvas, frameRect.right, frameRect.top + halfCornerLineWidth, -cornerLength, 0);
        drawLine(canvas, frameRect.right - halfCornerLineWidth, frameRect.top, 0, cornerLength);

        // right bottom
        drawLine(canvas, frameRect.right - halfCornerLineWidth, frameRect.bottom, 0, -cornerLength);
        drawLine(canvas, frameRect.right, frameRect.bottom - halfCornerLineWidth, -cornerLength, 0);

        // left bottom
        drawLine(canvas, frameRect.left, frameRect.bottom - halfCornerLineWidth, cornerLength, 0);
        drawLine(canvas, frameRect.left + halfCornerLineWidth, frameRect.bottom, 0, -cornerLength);
    }

    private void drawLine(Canvas canvas, float x, float y, int dx, int dy) {
        canvas.drawLine(x, y, x + dx, y + dy, paint);
    }

}