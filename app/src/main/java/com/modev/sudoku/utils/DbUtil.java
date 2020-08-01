package com.modev.sudoku.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.modev.sudoku.modile.Problem;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: modev
 * @description:
 * @date: Created in 2020/7/9 18:23
 * @version: 1.0
 */
public class DbUtil {
    private DbHelper dbHelper;

    public DbUtil(Context context) {
        dbHelper = new DbHelper(context, "Games.db", null, 1);
    }
    /**
     * @author: modev
     * @date: 2020/7/8 23:23
     * @description: 将游戏信息加入数据库
     * @param: problem
     * @return: void
     * */
    public void addProblem(Problem problem) {
        String sql = "insert into games values (?,?,?,?,?,?,?);";
        Object[] params = {problem.getId(), problem.getDiff(),
                problem.answerToString(), problem.boardToString(),
                problem.answerToString(), problem.getStatus(), problem.getTime()};
        SQLiteDatabase wdb = dbHelper.getWritableDatabase();
        wdb.execSQL(sql, params);
        wdb.close();
    }

        public void updateProblem(Problem problem) {
        String sql = "update games set board = ?, status = ?, time = ? where id = ?";
        Object[] params = {problem.boardToString(), problem.getStatus(), problem.getTime(), problem.getId()};
        SQLiteDatabase wdb = dbHelper.getWritableDatabase();
        wdb.execSQL(sql, params);
        wdb.close();
    }

    /**
     * @author: modev
     * @date: 2020/7/9 22:08
     * @description: 获取数据库中的所有问题列表
     * @return: java.util.List<com.modev.sudoku.modile.Problem>
     * */
    public List<Problem> getAllProblems() {
        String sql = "SELECT * FROM games";
        List<Problem> problems = new ArrayList<>();

        SQLiteDatabase rdb = dbHelper.getReadableDatabase();
        Cursor cursor = rdb.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int diff = cursor.getInt(1);
            String defaultProblem = cursor.getString(2);
            String board = cursor.getString(3);
            String answer = cursor.getString(4);
            int status = cursor.getInt(5);
            int time = cursor.getInt(6);

            Problem p = new Problem();
            p.setId(id);
            p.setDiff(diff);
            p.setDefaultProblemByJson(defaultProblem);
            p.setBoardByJson(board);
            p.setAnswerByJson(answer);
            p.setStatus(status);
            p.setTime(time);
            problems.add(p);
        }
        cursor.close();
        rdb.close();

        return problems;
    }

    public List<Problem> getProblemByCondition(String val) {
        String sql = "select * from games" + val;
        List<Problem> problems = new ArrayList<>();

        SQLiteDatabase rdb = dbHelper.getReadableDatabase();
        Cursor cursor = rdb.rawQuery(sql, null );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int diff = cursor.getInt(1);
            String defaultProblem = cursor.getString(2);
            String board = cursor.getString(3);
            String answer = cursor.getString(4);
            int status = cursor.getInt(5);
            int time = cursor.getInt(6);

            Problem p = new Problem();
            p.setId(id);
            p.setDiff(diff);
            p.setDefaultProblemByJson(defaultProblem);
            p.setBoardByJson(board);
            p.setAnswerByJson(answer);
            p.setStatus(status);
            p.setTime(time);
            problems.add(p);
        }
        cursor.close();
        rdb.close();

        return problems;
    }
    /**
     * @author: modev
     * @date: 2020/7/9 22:14
     * @description: 通过id查询问题
     * @param: id
     * @return: com.modev.sudoku.modile.Problem
     * */
    public Problem getProblemById(int id) {
        String sql = "SELECT * FROM games where id = ?";
        String[] params = {String.valueOf(id)};
        SQLiteDatabase rdb = dbHelper.getReadableDatabase();
        Cursor cursor = rdb.rawQuery(sql, params);
        Problem p = new Problem();
        p.setId(id);

        while (cursor.moveToNext()) {
            int diff = cursor.getInt(1);
            String defaultProblem = cursor.getString(2);
            String board = cursor.getString(3);
            String answer = cursor.getString(4);
            int status = cursor.getInt(5);
            int time = cursor.getInt(6);

            p.setDiff(diff);
            p.setDefaultProblemByJson(defaultProblem);
            p.setBoardByJson(board);
            p.setAnswerByJson(answer);
            p.setStatus(status);
            p.setTime(time);
        }
        cursor.close();
        rdb.close();

        return p;
    }


    /**
     * @author: modev
     * @date: 2020/7/9 21:49
     * @description: 向数据库添多个问题
     * @param: problems
     * @return: void
     * */
    public void addAllProblems(List<Problem> problems) {
        String sql = "insert into games values (?,?,?,?,?,?,?);";
        SQLiteDatabase wdb = dbHelper.getWritableDatabase();
        wdb.beginTransaction();
        for (Problem problem : problems) {
            Object[] params = {problem.getId(), problem.getDiff(),
                    problem.defaultProblemToString(), problem.boardToString(),
                    problem.answerToString(), problem.getStatus(), problem.getTime()};
            wdb.execSQL(sql, params);
        }
        wdb.setTransactionSuccessful();
        wdb.endTransaction();
        wdb.close();
    }

    /**
     * @author: modev
     * @description: 数据库操作辅助类
     * @date: Created in 2020/7/8 22:24
     * @version: 1.0
     */
    public class DbHelper extends SQLiteOpenHelper {

        public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("测试", "创建数据库");

            String sql = "create table if not exists games" +
                    "(id integer primary key autoincrement, " +
                    "diff integer, defaultProblem text, board text, answer text, status integer, time integer);";

            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
