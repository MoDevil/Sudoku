package com.modev.sudoku.modile;

import android.content.Intent;

import com.google.gson.Gson;
import java.sql.Time;
import java.util.Arrays;

/**
 * @author: modev
 * @description: 问题信息封装类
 * @date: Created in 2020/7/3 20:00
 * @version: 1.0
 */
public class Problem {
    private int id;
    private int diff;
    private int[][] defaultProblem;
    private int[][] board;
    private int[][] answer;
    private int status;
    private int time;

    public Problem() {
//        SudokuMaker maker = new SudokuMaker();
//        defaultProblem = maker.getArr();
//        answer = maker.getAnswer();
        board = new int[9][9];
        status = -1;
        time = 0;
    }

    public Problem(int id, int diff, int[][] defaultProblem, int[][] answer) {
        this.id = id;
        this.diff = diff;
        this.defaultProblem = defaultProblem;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDiff() {
        return diff;
    }

    public void setDiff(int diff) {
        this.diff = diff;
    }

    public int[][] getDefaultProblem() {
        return defaultProblem;
    }

    public void setDefaultProblem(int[][] defaultProblem) {
        this.defaultProblem = defaultProblem;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int[][] getAnswer() {
        return answer;
    }

    public void setAnswer(int[][] answer) {
        this.answer = answer;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    /**
     * @author: modev
     * @date: 2020/7/4 2:02
     * @description: 判断当前位置是否是题目中的结果
     * @param: x 横坐标
     * @param: y 纵坐标
     * @return: boolean
     * */
    public boolean isDefaultNumber(int x, int y) {
        if (x < 0 || x > 9 || y < 0 || y > 9) { return false; }
        return defaultProblem[x][y] != 0;
    }

    /**
     * @author: modev
     * @date: 2020/7/9 18:41
     * @description: 判断是否已经把问题解决了
     * @return: boolean
     * */
    public boolean isSolve() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j <9; j++) {
                if (answer[i][j] != (board[i][j]+defaultProblem[i][j])) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * @author: modev
     * @date: 2020/7/9 18:42
     * @description: 获取json格式的defaultProblem数据
     * @return: java.lang.String
     * */
    public String defaultProblemToString() {
        return toJson(defaultProblem);
    }

    public String boardToString() {
        return toJson(board);
    }

    public String answerToString() {
        return toJson(answer);
    }


    public void setDefaultProblemByJson(String value) {
        this.defaultProblem = toArray(value);
    }

    public void setBoardByJson(String value) {
        this.board = toArray(value);
    }

    public void setAnswerByJson(String value) {
        this.answer = toArray(value);
    }

    /**
     * @author: modev
     * @date: 2020/7/9 21:59
     * @description: 将二维数组转化为json
     * @param: values
     * @return: java.lang.String
     * */
    public String toJson(int[][] values) {
        Gson gson = new Gson();
        return gson.toJson(values);
    }

    /**
     * @author: modev
     * @date: 2020/7/9 22:01
     * @description: 将json对象转化为二维数组
     * @param: values
     * @return: int[][]
     * */
    public int[][] toArray(String values) {
        Gson gson = new Gson();
        return gson.fromJson(values, int[][].class);
    }
}
