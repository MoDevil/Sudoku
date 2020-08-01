package com.modev.sudoku.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.modev.sudoku.adapter.HomeListViewAdapter;
import com.modev.sudoku.modile.Problem;
import com.modev.sudoku.utils.DbUtil;
import com.modev.sudoku.utils.SharePreferencesUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author modev
 */
public class HomeService extends Service {
    private DbUtil dbUtil;
    private List<Problem> problems;
    private HomeListViewAdapter adapter;

    @Override
    public void onCreate() {
        dbUtil = new DbUtil(getApplicationContext());
        Log.d("测试", "服务创建HomeService");

        boolean isFirstStart = SharePreferencesUtil.getBooleanFromSharedPreference(getApplicationContext(), "Sudoku_data", "IsFirstStart");
        if (isFirstStart) {
            initApk();
            SharePreferencesUtil.setBooleanToSharedPreference(getApplicationContext(), "Sudoku_data", "IsFirstStart", false);
        }else {
            this.problems = dbUtil.getAllProblems();
        }

    }

    /**
     * @author: modev
     * @date: 2020/7/9 22:12
     * @description: 初始化apk
     * @return: void
     * */
    public void initApk() {
        AssetManager assetManager = getAssets();
        List<Problem> problems = new ArrayList<>();
        try {
            InputStream in =  assetManager.open("p");
            InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            int id = 1;
            while ((tmp = br.readLine()) != null) {
                Problem p = createProblemFromJson(tmp);
                p.setId(id++);
                problems.add(p);
            }

            br.close();
            reader.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.problems = problems;
        dbUtil.addAllProblems(problems);

    }

    public List<Problem> getProblems() {
        return dbUtil.getAllProblems();
    }

    public List<Problem> getProblemsByConditions(boolean[] conditions) {
        StringBuilder sb = new StringBuilder();
        sb.append(" where ");
        int count = 0;
        if (!conditions[0]) {
            count++;
            sb.append("status != 0");
        }
        if (!conditions[1]) {
            if (count++ > 0) { sb.append(" and "); }
            sb.append("status != -1");
        }
        if (!conditions[2]) {
            if (count++ > 0) { sb.append(" and "); }
            sb.append("status != 1");
        }
        if (!conditions[3]) {
            if (count++ > 0) { sb.append(" and "); }
            sb.append("diff != 1");
        }
        if (!conditions[4]) {
            if (count++ > 0) { sb.append(" and "); }
            sb.append("diff != 2");
        }
        if (!conditions[5]) {
            if (count++ > 0) { sb.append(" and "); }
            sb.append("diff != 3");
        }
        if (!conditions[6]) {
            if (count++ > 0) { sb.append(" and "); }
            sb.append("diff != 4");
        }
        if (!conditions[7]) {
            if (count++ > 0) { sb.append(" and "); }
            sb.append("diff != 5");
        }
        if (count > 0) {
            return dbUtil.getProblemByCondition(sb.toString());
        }else {
            return dbUtil.getProblemByCondition("");
        }
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new HomeServiceBind();
    }

    public Problem createProblemFromJson(String s) {
        //Log.d("测试：", s);
        if (!"".equals(s)) {
            Gson gson = new Gson();
            return gson.fromJson(s, Problem.class);
        }else {
            return null;
        }
    }

    public class HomeServiceBind extends Binder {
        public HomeService getHomeService() {
            return HomeService.this;
        }
        public void setView(HomeListViewAdapter a) {
            adapter = a;
            adapter.setProblemsList(getProblems());
            Log.d("测试", "setView" + a.toString());
        }
    }
}


