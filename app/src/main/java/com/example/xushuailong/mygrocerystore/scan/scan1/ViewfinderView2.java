package com.example.xushuailong.mygrocerystore.scan.scan1;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.example.xushuailong.mygrocerystore.R;
import com.example.xushuailong.mygrocerystore.scan.util.HardWare;

public final class ViewfinderView2 extends View {
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
    private Rect frame;
    private Rect line_v;

    private int line_w;
    private int pen = 4;
    int middle_v;
    int middle_h;
    float newHeight;
    int newTop;
    int newBottom;
    int gapDistance = 0;
    private int viewWidth;
    private int viewHeight;

    private int scanViewPaddingX;
    private int scanViewPaddingY;
    private int scanViewHeight;
    private int scanViewLeft;
    private int scanViewTop;
    private int scanViewRight;
    private int scanViewBottom;


    public Rect scanLine;
    private boolean colorOn = false;
    private Context con;
    private int i = 0;

    //用于画弧的四个边界矩形
    private RectF leftTopRect, leftBottomRect, rightTopRect, rightBottomRect;

    public ViewfinderView2(Context context) {
        this(context, null);
    }

    public ViewfinderView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewfinderView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        con = context;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ViewfinderView2, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ViewfinderView2_cameraPaddingX:
                    scanViewPaddingX = (int) a.getDimension(attr, 0);
                    break;
                case R.styleable.ViewfinderView2_cameraPaddingY:
                    scanViewPaddingY = (int) a.getDimension(attr, 0);
                    break;
                case R.styleable.ViewfinderView2_cameraHeight:
                    scanViewHeight = (int) a.getDimension(attr, 0);
                    break;
            }
        }
        a.recycle();

        paint = new Paint();
        box = new Rect();

        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
//        maskColor = resources.getColor(R.color.blue);
        scannerAlpha = 0;// zxing_lib

        // 扫描线固定专用
        outRangeColor = resources.getColor(R.color.viewfinder_outrange);
        laserColor = outRangeColor;

    }

    // gc_lib
    public void stopDraw() {
        stopDraw = 1;
        invalidate();
    }

    // end

    @Override
    public void onDraw(Canvas canvas) {

        viewWidth = canvas.getWidth();
        viewHeight = canvas.getHeight();
        frame = new Rect(0, 0, viewWidth, viewHeight);

//        WccLogger.e(TAG, "onDraw " + viewWidth + " .... " + viewHeight);

        // 扫描线上下移动专用
//        outRangeColor = resources.getColor(R.color.white);
//        laserColor = resources.getColor(R.color.viewfinder_outrange);

        line_v = new Rect();

        middle_v = frame.width() / 2;
        middle_h = frame.height() / 2;

        newHeight = frame.height();
        newTop = (int) (frame.exactCenterY() - newHeight * 0.5F);
        newBottom = (int) (newTop + newHeight);
        gapDistance = frame.height() / 10;

        scanViewLeft = scanViewPaddingX + 1;
        scanViewTop = (viewHeight - scanViewHeight) / 2 + 1;
        scanViewRight = viewWidth - scanViewPaddingX - 1;
        scanViewBottom = (viewHeight + scanViewHeight) / 2 - 1;
        scanLine = new Rect();
        scanLine.set(scanViewLeft, scanViewTop, scanViewRight, scanViewBottom);

        leftTopRect = new RectF();
        leftBottomRect = new RectF();
        rightTopRect = new RectF();
        rightBottomRect = new RectF();

        if (stopDraw == 1)
            return;
        if (frame == null)
            return;

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(maskColor);

        box.set(0, 0, viewWidth, (viewHeight - scanViewHeight) / 2 - scanViewPaddingY);
        canvas.drawRect(box, paint);
        box.set(0, viewHeight - ((viewHeight - scanViewHeight) / 2 - scanViewPaddingY), viewWidth, viewHeight);
        canvas.drawRect(box, paint);

        line_w = viewWidth >> 4;

        /**
         * 绘制四个边角，开始
         */
        paint.setColor(getResources().getColor(R.color.white));
        paint.setStrokeWidth(4.0F);
        paint.setAlpha(200);

        //UI三，弧线比较小，接一段直线
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        leftTopRect.set(scanViewLeft, scanViewTop, scanViewLeft + line_w, scanViewTop + line_w);
        canvas.drawArc(leftTopRect, 180, 90, false, paint);
        canvas.drawLine(scanViewLeft + line_w / 2, scanViewTop, scanViewLeft + line_w, scanViewTop, paint);
        canvas.drawLine(scanViewLeft, scanViewTop + line_w / 2, scanViewLeft, scanViewTop + line_w, paint);

        rightTopRect.set(scanViewRight - line_w, scanViewTop, scanViewRight, scanViewTop + line_w);
        canvas.drawArc(rightTopRect, 270, 90, false, paint);
        canvas.drawLine(scanViewRight - line_w, scanViewTop, scanViewRight - line_w / 2, scanViewTop, paint);
        canvas.drawLine(scanViewRight, scanViewTop + line_w / 2, scanViewRight, scanViewTop + line_w, paint);

        rightBottomRect.set(scanViewRight - line_w, scanViewBottom - line_w, scanViewRight, scanViewBottom);
        canvas.drawArc(rightBottomRect, 0, 90, false, paint);
        canvas.drawLine(scanViewRight, scanViewBottom - line_w, scanViewRight, scanViewBottom - line_w / 2, paint);
        canvas.drawLine(scanViewRight - line_w, scanViewBottom, scanViewRight - line_w / 2, scanViewBottom, paint);

        leftBottomRect.set(scanViewLeft, scanViewBottom - line_w, scanViewLeft + line_w, scanViewBottom);
        canvas.drawArc(leftBottomRect, 90, 90, false, paint);
        canvas.drawLine(scanViewLeft + line_w / 2, scanViewBottom, scanViewLeft + line_w, scanViewBottom, paint);
        canvas.drawLine(scanViewLeft, scanViewBottom - line_w, scanViewLeft, scanViewBottom - line_w / 2, paint);
        /**
         * 绘制四个边角，结束
         */
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

        if (HardWare.needRotateActivity()) {
            int offset = frame.height() / 6;
            box.set(middle_v - 3, (newTop + offset + gapDistance), middle_v + 3, (newBottom - offset - gapDistance));
            canvas.drawRect(box, paint);

            postInvalidateDelayed(ANIMATION_DELAY, box.left, box.top, box.right, box.bottom);
        } else {
            // 固定扫描线
            int offset = frame.width() / 6;
            box.set(frame.left + offset, middle_h - 3, frame.right - offset, middle_h + 3);
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
        }
    }

    public void setColorOnOff(boolean v) {
        colorOn = v;
    }

    public void drawViewfinder() {
        stopDraw = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置扫描框的大小
     * @param viewHeight 控件的高度
     * @param scanViewHeight 中部扫描框线的高度
     * @param scanViewPaddingX 中部扫描框线的左右边距
     * @param scanViewPaddingY 中部扫描框线的上下边距
     */
    public void setViewValues(int viewHeight, int scanViewHeight, int scanViewPaddingX, int scanViewPaddingY) {
//        this.viewHeight = MetricUtil.dip2pix(con,viewHeight);
//        this.scanViewHeight = MetricUtil.dip2pix(con,scanViewHeight);
//        this.scanViewPaddingX = MetricUtil.dip2pix(con,scanViewPaddingX);
//        this.scanViewPaddingY = MetricUtil.dip2pix(con,scanViewPaddingY);
    }
}