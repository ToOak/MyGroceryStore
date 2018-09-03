package com.example.xushuailong.mygrocerystore.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.xushuailong.mygrocerystore.R;
import com.example.xushuailong.mygrocerystore.bean.PathPosition;
import com.example.xushuailong.mygrocerystore.utils.LogUtil;

import java.util.ArrayList;

/**
 * 自定义视图的编码主要由四部分组成：
 * 一、重写构造函数，并初始化个性化参数；
 * 二、重写测量函数onMesure，计算该视图的宽与高（除了复杂视图，实际开发中一般不进行重写）；
 * 三、重写绘图函数onDraw、onLayout、dispatchDraw，视情况重写三个其中的一个或多个；
 * 四、重写触摸事件函数dispatchTouchEvent、onInterceptTouchEvent、onTouchEvent，一般情况不做重写，
 * 当然存在手势滑动冲突时，就必须重写；
 */
public class SignatureView extends View {
    private Paint paint;
    private static final String TAG = "SignatureView";
    private Canvas cacheCanvas;
    private Bitmap cachebBitmap;
    private Path path;
    private int paint_color = Color.BLACK;
    private float stroke_width = 3;
    private PathPosition pos = new PathPosition();
    private ArrayList<PathPosition> pathArray = new ArrayList<PathPosition>();
    private int mWidth = 0, mHeight = 0;


    //只有一个参数，用于在代码中构造对象
    public SignatureView(Context context) {
        super(context);
    }

    //有两个参数，用于在XML布局中构造对象
    public SignatureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SignatureView);
            paint_color = typedArray.getColor(R.styleable.SignatureView_paint_color, 0);
            stroke_width = typedArray.getDimension(R.styleable.SignatureView_stroke_width, 2);
            typedArray.recycle();
        }
    }

    //有三个参数，用于在XML布局中构造对象
    public SignatureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        LogUtil.e("SignatureView onDraw");
        super.onDraw(canvas);
        canvas.drawBitmap(cachebBitmap, 0, 0, null);
        canvas.drawPath(path, paint); //这个是需要的，最近一次的路径保存在这里
//        if (paint == null) {
//            paint = new Paint();
//            paint.setColor(colorInt);
//        }
//        canvas.drawColor(colorInt);
    }

    /**
     * 对应上面layout_width和layout_height的三种赋值方式，Android的视图底层也提供了三种测量模式，分别是：
     * 1、MeasureSpec.AT_MOST : 表示达到最大，该常量的十进制数值为-2147483648，对应赋值方式的MATCH_PARENT；
     * 2、MeasureSpec.UNSPECIFIED : 表示未指定（实际就是自适应），该常量的十进制数值为0，
     * 对应赋值方式的WRAP_CONTENT；
     * 3、MeasureSpec.EXACTLY : 表示精确，该常量的十进制数值为1073741824，对应赋值方式的具体dp值；
     * 围绕着这三种模式，衍生了MeasureSpec的相关方法，如getChildMeasureSpec、makeMeasureSpec、measure等等
     *
     * @param widthMeasureSpec  width
     * @param heightMeasureSpec height
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LogUtil.e("SignatureView onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = this.getMeasuredWidth();
        mHeight = this.getMeasuredHeight();
        Log.d(TAG, "onMeasure width=" + mWidth + ",height=" + mHeight);
        init(mWidth, mHeight);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        LogUtil.e("SignatureView onLayout");
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 在自定义视图中，有三个函数可以重写用于界面绘制，在视图创建过程中，
     * 三个函数的执行顺序依次是：onLayout、onDraw、dispatchDraw。
     * 1、onLayout(boolean changed, int left, int top, int right, int bottom) :
     * onLayout用于定位该视图在上级视图中的位置，从其参数中就可以看出来。
     * 由于该函数没有画布，因此只适合绘制现成的视图控件。
     * 2、onDraw(Canvas canvas) :
     * 自定义控件一般是重写onDraw方法，在画布中绘制各种图形。
     * 3、dispatchDraw(Canvas canvas) :
     * dispatchDraw与onDraw的区别在于：onDraw在绘制下级视图之前，而dispatchDraw在绘制下级视图之后，
     * 所以如果不想自己的绘图被下级视图覆盖的话，就要在dispatchDraw中进行绘制操作。
     * 为方便记忆，只要是从ViewGroup衍生出的视图，都用dispatchDraw，其他小控件都用onDraw。
     *
     * @param canvas canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }


    public SignatureView(Context context, int width, int height) {
        super(context);
        init(width, height);
    }


    public int getPaintColor() {
        return paint_color;
    }

    public void setPaintColor(int paint_color) {
        this.paint_color = paint_color;
    }

    public float getStrokeWidth() {
        return stroke_width;
    }

    public void setStrokeWidth(float stroke_width) {
        this.stroke_width = stroke_width;
    }

    public Bitmap getCachebBitmap() {
        return getDrawingCache();
    }

    private void init(int width, int height) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(stroke_width);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(paint_color);
        path = new Path();

        setDrawingCacheEnabled(true);
        cachebBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas(cachebBitmap);
        cacheCanvas.drawColor(Color.WHITE);
    }

    public void clear() {
        if (cacheCanvas != null) {
            pathArray.clear();
            cacheCanvas.drawRGB(255, 255, 255);
            invalidate();
        }
    }

    public void revoke() {
        if (pathArray.size() > 0) {
            pathArray.remove(pathArray.size() - 1);
            cacheCanvas.drawRGB(255, 255, 255);
            for (int i = 0; i < pathArray.size(); i++) {
                Path posPath = new Path();
                posPath.moveTo(pathArray.get(i).firstX, pathArray.get(i).firstY);
                posPath.quadTo(pathArray.get(i).firstX, pathArray.get(i).firstY,
                        pathArray.get(i).nextX, pathArray.get(i).nextY);
                cacheCanvas.drawPath(posPath, paint);
            }
            invalidate();
        }
    }

    private float cur_x, cur_y;


    //    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        super.onTouchEvent(event);
//        this.performClick();

//        我们可以在onTouchEvent里面先调用
//        requestDisallowInterceptTouchEvent(true);,
//        这句代码的意思就是不允许父控件拦截我们的触摸事件,
//        这样action_cancel事件就永远不会被触发.问题也就不存在了.

        getParent().requestDisallowInterceptTouchEvent(true);
        float x = event.getX();
        float y = event.getY();

        LogUtil.e("event action: " + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                cur_x = x;
                cur_y = y;
                path.moveTo(cur_x, cur_y);
                pos.firstX = cur_x;
                pos.firstY = cur_y;
                break;
            case MotionEvent.ACTION_MOVE:
                path.quadTo(cur_x, cur_y, x, y);
                cur_x = x;
                cur_y = y;
                pos.nextX = cur_x;
                pos.nextY = cur_y;
                pathArray.add(pos);
                pos = new PathPosition();
                pos.firstX = cur_x;
                pos.firstY = cur_y;
                break;
            case MotionEvent.ACTION_UP:
                cacheCanvas.drawPath(path, paint);
                path.reset();
                break;
//            case MotionEvent.ACTION_CANCEL:
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
        }
        invalidate();
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean result = super.dispatchTouchEvent(event);
        LogUtil.e("dispatchTouchEvent result: " + result);
        return result;
    }
}
