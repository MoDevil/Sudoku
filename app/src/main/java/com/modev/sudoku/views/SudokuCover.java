package com.modev.sudoku.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.modev.sudoku.modile.Problem;

/**
 * @author: modev
 * @description:
 * @date: Created in 2020/7/14 23:09
 * @version: 1.0
 */
public class SudokuCover extends View {
    private int height;
    private int width;
    private float sideLenght;
    private Problem problem;

    public SudokuCover(Context context) {
        super(context);
    }

    public SudokuCover(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        height = getMeasuredWidth();
        width = getMeasuredWidth();
        sideLenght = width / 9F;
        setMeasuredDimension(width,height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    @Override
    public void onDraw(Canvas canvas) {

        Log.d("测试", "绘制");
        paintPanel(canvas);
//        if (problem != null) {
//            paintNumbers(canvas);
//        }
    }

    /**
     * @author: modev
     * @date: 2020/7/3 20:18
     * @description: 画面板
     * @param: canvas 画布
     * @return: void
     * */
    private void paintPanel(Canvas canvas) {
        //浅色线条
        Paint grayPaint = new Paint();
        grayPaint.setColor(Color.GRAY);

        //深色线条
        Paint blackPaint = new Paint();
        blackPaint.setStrokeWidth(5);
        blackPaint.setColor(Color.BLACK);

        //绘制浅色线条
        for (int i = 0; i <= 9; i++) {
            canvas.drawLine(i*width/9F, 0F, i*width/9F, height, grayPaint);
            canvas.drawLine(0F, i*height/9F, width, i*height/9F, grayPaint);

        }

        //绘制四条深色线条
        canvas.drawLine(width/3F, 0f, width/3F, height, blackPaint);
        canvas.drawLine(2F*width/3F, 0f, 2F*width/3F, height, blackPaint);
        canvas.drawLine(0, height/3F, width, height/3F, blackPaint);
        canvas.drawLine(0, 2F*height/3F, width, 2F*height/3F, blackPaint);
    }
    /**
     * @author: modev
     * @date: 2020/7/3 20:19
     * @description: 画出数字
     * @param: canvas 画布
     * @return: void
     * */
    private void paintNumbers(Canvas canvas) {

        //数组
        int[][] problemArr = problem.getDefaultProblem();

        //绘制数字的黑色颜色，设置字体大小，居中，颜色
        Paint blackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackPaint.setColor(0xFF666666);
        blackPaint.setTextSize(84);
        blackPaint.setTextAlign(Paint.Align.CENTER);

        //灰色蒙版的颜色，设置颜色，透明度
        Paint grayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        grayPaint.setColor(Color.GRAY);
        grayPaint.setAlpha(55);

        //遍历数组，然后画东西
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (problemArr[i][j] != 0) {
                    canvas.drawText(String.valueOf(problemArr[i][j]), j*sideLenght+sideLenght/2, i*sideLenght+3*sideLenght/4, blackPaint);
                    canvas.drawRect(j*sideLenght, i*sideLenght, j*sideLenght+sideLenght, i*sideLenght+sideLenght,grayPaint );
                }
            }
        }
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
        Log.d("测试", "问题id:"+problem.getId() +" \t " + this.getId());
        invalidate();
    }
}
