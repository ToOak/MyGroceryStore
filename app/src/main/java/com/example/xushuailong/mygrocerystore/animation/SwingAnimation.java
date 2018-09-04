package com.example.xushuailong.mygrocerystore.animation;

import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.example.xushuailong.mygrocerystore.utils.LogUtil;

public class SwingAnimation extends Animation {
    private float mMiddleDegrees;
    private float mLeftDegrees;
    private float mRightDegrees;
    private int mPivotXType = ABSOLUTE;
    private int mPivotYType = ABSOLUTE;
    private float mPivotXValue = 0.0f;
    private float mPivotYValue = 0.0f;
    private float mPivotX;
    private float mPivotY;

    public SwingAnimation(float mMiddleDegrees, float mLeftDegrees, float mRightDegrees) {
        this.mLeftDegrees = mLeftDegrees;
        this.mMiddleDegrees = mMiddleDegrees;
        this.mRightDegrees = mRightDegrees;
        mPivotX = 0.0f;
        mPivotY = 0.0f;
    }


    public SwingAnimation(float middleDegrees, float leftDegrees, float rightDegrees, float pivotX, float pivotY) {
        mMiddleDegrees = middleDegrees;
        mLeftDegrees = leftDegrees;
        mRightDegrees = rightDegrees;
        mPivotXType = ABSOLUTE;
        mPivotYType = ABSOLUTE;
        mPivotXValue = pivotX;
        mPivotYValue = pivotY;
        initializePivotPoint();
    }

    /**
     * //坐标类型有三种：
     * ABSOLUTE 绝对坐标，RELATIVE_TO_SELF 相对自身的坐标，RELATIVE_TO_PARENT 相对上级视图的坐标
     * <p>
     * //X坐标相对比例，为0时表示左边顶点，为1表示右边顶点，为0.5表示水平中心点
     * //Y坐标相对比例，为0时表示上边顶点，为1表示下边顶点，为0.5表示垂直中心点
     *
     * @param middleDegrees 中间度数
     * @param leftDegrees   摆到左侧的度数
     * @param rightDegrees  摆到右侧的度数
     * @param pivotXType    圆心X坐标类型
     * @param pivotXValue   圆心X坐标相对比例
     * @param pivotYType    圆心Y坐标类型
     * @param pivotYValue   圆心Y坐标相对比例
     */
    public SwingAnimation(float middleDegrees, float leftDegrees, float rightDegrees, int pivotXType, float pivotXValue,
                          int pivotYType, float pivotYValue) {
        mMiddleDegrees = middleDegrees;
        mLeftDegrees = leftDegrees;
        mRightDegrees = rightDegrees;
        mPivotXValue = pivotXValue;
        mPivotXType = pivotXType;
        mPivotYValue = pivotYValue;
        mPivotYType = pivotYType;
        initializePivotPoint();
    }

    private void initializePivotPoint() {
        if (mPivotXType == ABSOLUTE) {
            mPivotX = mPivotXValue;
        }
        if (mPivotYType == ABSOLUTE) {
            mPivotY = mPivotYValue;
        }
    }


    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mPivotX = resolveSize(mPivotXType, mPivotXValue, width, parentWidth);
        mPivotY = resolveSize(mPivotYType, mPivotYValue, height, parentHeight);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
//        super.applyTransformation(interpolatedTime, t);
        float degrees;
        float leftPos = (float) (1.0 / 4.0);
        float rightPos = (float) (3.0 / 4.0);
        if (interpolatedTime <= leftPos) {
            degrees = mMiddleDegrees + ((mLeftDegrees - mMiddleDegrees) * interpolatedTime * 4);
        } else if (interpolatedTime > leftPos && interpolatedTime < rightPos) {
            degrees = mLeftDegrees + ((mRightDegrees - mLeftDegrees) * (interpolatedTime - leftPos) * 2);
        } else {
            degrees = mRightDegrees + ((mMiddleDegrees - mRightDegrees) * (interpolatedTime - rightPos) * 4);
        }
        LogUtil.e("degrees: " + degrees);

        float scale = getScaleFactor();
        if (mPivotX == 0.0f && mPivotY == 0.0f) {
            t.getMatrix().setRotate(degrees);
        } else {
            t.getMatrix().setRotate(degrees, mPivotX * scale, mPivotY * scale);
        }

    }
}
