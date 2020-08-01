package com.modev.sudoku.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;

import com.modev.sudoku.modile.Problem;
import com.modev.sudoku.utils.DbUtil;
import com.modev.sudoku.views.SudokuView;

public class MainService extends Service {
    private SudokuView sudokuView;
    private Problem problem;
    private DbUtil dbUtil;

    @Override
    public void onCreate() {
        dbUtil = new DbUtil(getApplicationContext());
    }

    public void setProblem(int id) {
        this.problem = dbUtil.getProblemById(id);
        problem.setStatus(0);
    }

    public void updateProblem() {
        Log.d("测试", "游戏状态："+problem.getStatus());
        dbUtil.updateProblem(problem);
    }
    public Problem getProblem() {
        return problem;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MainServiceBind();
    }

    public class MainServiceBind extends Binder {
        public MainService getMainService() {
            return MainService.this;
        }

        public void setSudoViewAndProblem(SudokuView view, int id) {
            sudokuView = view;
            setProblem(id);

            if (!sudokuView.haveProblem()) {
                sudokuView.setProblem(problem);
            }
        }

        public void setTimer(Chronometer timer) {
            timer.setBase(SystemClock.elapsedRealtime()+problem.getTime());
            timer.start();
        }
    }
}
