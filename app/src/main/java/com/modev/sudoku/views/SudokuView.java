package com.modev.sudoku.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.modev.sudoku.modile.Problem;

/**
 * @author: modev
 * @description: 数独自定义视图
 * @date: Created in 2020/7/3 20:00
 * @version: 1.0
 */
public class SudokuView extends View {
    private int height;
    private int width;
    private float sideLenght;
    private float[] points = new float[2];
    private int[] indexs = new int[2];
    private boolean status;
    private Problem problem;

    /**
     * 每个位置的测试数字
     * */
    private int[][][] testNumbers;

    /**
     * 标记是不是填写测试的数字
     * */
    private boolean drawTestNumber;

    /**
     * 前后左右的偏移量
     * */
    private float[][] offset;

    /**
     * 标记是否出错
     * */
    private boolean[][] error;

    public SudokuView(Context context) {
        super(context);
    }

    public SudokuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        testNumbers = new int[9][9][9];
        error = new boolean[9][9];
        drawTestNumber = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        height = getMeasuredWidth();
        width = getMeasuredWidth();
        sideLenght = width / 9F;
        setMeasuredDimension(width,height);

        offset = new float[][]{{-sideLenght/3F, -sideLenght/3F}, {0F, -sideLenght/3F}, {sideLenght/3F, -sideLenght/3F},
                {-sideLenght/3F, 0}, {0F, 0F}, {sideLenght/3F, 0F},
                {-sideLenght/3F, sideLenght/3F}, {0F, sideLenght/3F}, {sideLenght/3F, sideLenght/3F}};

        //Log.d("测试：", "widthMeasureSpec" + widthMeasureSpec + "\t heightMeasureSpec" + heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public void onDraw(Canvas canvas) {

        paintPanel(canvas);
        if (problem != null) {
            paintNumbers(canvas);
        }

        //判断是否需要将选中的方块显示
        if (status) {
            Paint yellowPaint = new Paint();
            yellowPaint.setColor(Color.YELLOW);
            yellowPaint.setAlpha(100);
            canvas.drawRect(points[0], points[1], points[0]+sideLenght, points[1]+sideLenght,yellowPaint );
        }

    }

    //点按事件，显示选中的小方块
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (x >= 0 && x <= width && y >= 0 && y <= height) {
            indexs = formatPosition(x, y);

            if (!problem.isDefaultNumber(indexs[1], indexs[0])) {
                status = true;
                points[0] = indexs[0] * sideLenght;
                points[1] = indexs[1] * sideLenght;
            }
        }

        invalidate();
        return true;
    }

    /**
     * 画面板
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
     * 画出数字
     * @param: canvas 画布
     * @return: void
     * */
    private void paintNumbers(Canvas canvas) {

        //数组
        int[][] problemArr = problem.getDefaultProblem();
        int[][] board = problem.getBoard();

        //绘制数字的黑色颜色，设置字体大小，居中，颜色
        Paint blackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackPaint.setColor(0xFF666666);
        blackPaint.setTextSize(84);
        blackPaint.setTextAlign(Paint.Align.CENTER);

        //绘制出来的有问题数字的颜色
        Paint redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint.setColor(Color.RED);
        redPaint.setTextSize(84);
        redPaint.setTextAlign(Paint.Align.CENTER);

        //灰色蒙版的颜色，设置颜色，透明度
        Paint grayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        grayPaint.setColor(Color.GRAY);
        grayPaint.setAlpha(55);

        //测试字体的颜色
        Paint yellowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yellowPaint.setColor(0xFF00688B);
        yellowPaint.setTextSize(30);
        yellowPaint.setTextAlign(Paint.Align.CENTER);

        //遍历数组，然后画东西
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (problemArr[i][j] != 0) {
                    if (error[i][j]) {
                        canvas.drawText(String.valueOf(problemArr[i][j]), j*sideLenght+sideLenght/2, i*sideLenght+3*sideLenght/4, redPaint);
                    }else {
                        canvas.drawText(String.valueOf(problemArr[i][j]), j*sideLenght+sideLenght/2, i*sideLenght+3*sideLenght/4, blackPaint);
                    }
                    canvas.drawRect(j*sideLenght, i*sideLenght, j*sideLenght+sideLenght, i*sideLenght+sideLenght,grayPaint );

                }else {
                    if (board[i][j] != 0) {
                        if (error[i][j]) {
                            canvas.drawText(String.valueOf(board[i][j]), j*sideLenght+sideLenght/2, i*sideLenght+3*sideLenght/4, redPaint);
                        }else {
                            canvas.drawText(String.valueOf(board[i][j]), j*sideLenght+sideLenght/2, i*sideLenght+3*sideLenght/4, blackPaint);
                        }
                    }else {
                        for (int k = 0; k < 9; k++) {
                            if (testNumbers[i][j][k] != 0) {
                                Log.d("测试", "这里"+testNumbers[i][j][k] + "\t offset:" + offset[k][0] + ", " + offset[k][1]);
                                canvas.drawText(String.valueOf(testNumbers[i][j][k]), (j*sideLenght+sideLenght/2)+offset[k][0], (i*sideLenght+3*sideLenght/5)+offset[k][1], yellowPaint);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 将输入的坐标格式化为小方块左上角的坐标
     * @param: x x坐标
     * @param: y y坐标
     * @return: int[] 格式化后的索引值
     * */
    private int[] formatPosition(float x, float y){
        int[] index = new int[2];
        index[0] = (int)x/(int)sideLenght;
        index[1] = (int)y/(int)sideLenght;
        return index;
    }

    /**
     * 将选中的单元格的数据清空
     * @return: boolean
     * */
    public boolean clearNumber() {

        if (drawTestNumber) {
            for (int i = 0; i < 9; i++) {
                testNumbers[indexs[1]][indexs[0]][i] = 0;
            }
            invalidate();
            return true;
        }

        if (status) {
            problem.getBoard()[indexs[1]][indexs[0]] = 0;
            check();
            invalidate();
            return true;
        }else {
            return false;
        }
    }
    /**
     * 给选中的单元格添加数据
     * @param: num 数字
     * @return: boolean 处理结果
     * */
    public boolean setNumber(int num) {

        if (drawTestNumber) {
            setTestNum(num);
            Log.d("测试", num + "\t 测试：" + drawTestNumber);
            return true;
        }

        if (status) {
            problem.getBoard()[indexs[1]][indexs[0]] = num;
            check();
            invalidate();
            //Log.d("测试：", problem.toString());
            if (problem.isSolve()) {
                problem.setStatus(1);
                Toast.makeText(getContext(), "问题解决！！", Toast.LENGTH_SHORT).show();
            }
            return true;
        }else {
            return false;
        }
    }


    /**
     * 设置测试的数字
     * @param: num
     * @return: void
     * */
    public void setTestNum(int num) {
        testNumbers[indexs[1]][indexs[0]][num-1] = num;
        invalidate();
    }

    /**
     * 设置问题并更新视图
     * @param: problem
     * @return: void
     * */
    public void setProblem(Problem problem) {
        this.problem = problem;
        invalidate();
    }

    /**
     * 删除所有已经填写的数
     * @return void
     * */
    public void clearAllNum() {
        this.problem.setBoard(new int[9][9]);
        testNumbers = new int[9][9][9];
        error = new boolean[9][9];
        invalidate();
    }
    public boolean isDrawTestNumber() {
        return drawTestNumber;
    }

    public void setDrawTestNumber(boolean drawTestNumber) {
        this.drawTestNumber = drawTestNumber;
    }

    public boolean haveProblem() {
        return problem != null;
    }
    public Problem getProblem() {
        return problem;
    }

    /**
     * 遍历所有位置，检查是否出错
     * @return void
     * */
    private void check() {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int target = problem.getDefaultProblem()[i][j] + problem.getBoard()[i][j];
                error[i][j] = checkOne(i, j, target);
            }
        }
    }

    /**
     * 检查一个位置是数是否有问题
     * @param: i
     * @param: j
     * @param: target
     * @return: boolean
     * */
    private boolean checkOne(int i, int j, int target) {
        for (int k = 0; k < 9; k++) {
            int v1 = problem.getDefaultProblem()[k][j] + problem.getBoard()[k][j];
            int v2 = problem.getDefaultProblem()[i][k] + problem.getBoard()[i][k];
            if (k != i && v1 == target) { return true; }
            if (k != j && v2 == target) { return true; }
        }

        i = (i / 3) * 3 + 1;
        j = (j / 3) * 3 + 1;

        for (int m = -1; m < 2; m++) {
            for (int n = -1; n < 2; n++) {
                int v = problem.getDefaultProblem()[i+m][j+n] + problem.getBoard()[i+m][j+n];
                if ((m != 0 && n != 0) && v == target) { return false; }
            }
        }
        return false;
    }
}