package com.modev.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.modev.sudoku.adapter.HomeListViewAdapter;
import com.modev.sudoku.modile.Problem;
import com.modev.sudoku.service.HomeService;

import java.util.ArrayList;

/**
 * @author: modev
 * @description: 游戏启动界面
 * @date: Created in 2020/7/3 20:00
 * @version: 1.0
 */
public class HomeActivity extends AppCompatActivity {
    private ServiceConnection homeServiceConn;
    private HomeService homeService;

    private ListView problemsLv;
    private HomeListViewAdapter adapter;

    private CheckBox[] gameStatus;
    private CheckBox[] diffLevels;
    private boolean[] conditions = {true, true, true, true, true, true, true, true};
    private final int[] GAME_STATUS_ID = {R.id.home_cb_status_unsolved, R.id.home_cb_status_untried,
            R.id.home_cb_status_solved};
    private final int[] DIFF_LEVEL_ID = {R.id.home_cb_diff_1, R.id.home_cb_diff_2, R.id.home_cb_diff_3,
            R.id.home_cb_diff_4, R.id.home_cb_diff_5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        setMethod();

        Intent homeServiceIntent = new Intent(this, HomeService.class);
        homeServiceConn = new HomeServiceConnection();
        bindService(homeServiceIntent, homeServiceConn, BIND_AUTO_CREATE);

        Log.d("测试", "create");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("测试", "start");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(homeServiceConn);
    }

    /**
     * @author: modev
     * @date: 2020/7/4 23:42
     * @description: 初始化控件
     * @return: void
     */
    private void init() {
        problemsLv = findViewById(R.id.home_lv_problems);
        gameStatus = new CheckBox[3];
        diffLevels = new CheckBox[5];
        for (int i = 0; i < 3; i++) {
            gameStatus[i] = findViewById(GAME_STATUS_ID[i]);
        }

        for (int i = 0; i < 5; i++) {
            diffLevels[i] = findViewById(DIFF_LEVEL_ID[i]);
        }
    }

    /**
     * @author: modev
     * @date: 2020/7/4 23:47
     * @description: 给控件添加事件监听
     * @return: void
     */
    private void setMethod() {
        adapter = new HomeListViewAdapter(getApplicationContext(), R.layout.home_item, new ArrayList<Problem>());
//        adapter = new TestAdapter(getApplicationContext(), R.layout.home_item, new ArrayList<Problem>());
        problemsLv.setAdapter(adapter);
        problemsLv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        intent.putExtra("problemId", homeService.getProblems().get(position).getId());
                        startActivity(intent);
                    }
                }
        );

        GameStatusChangedListener statusListener = new GameStatusChangedListener();
        for (int i = 0; i < 3; i++) {
            gameStatus[i].setOnCheckedChangeListener(statusListener);
        }

        GameDiffLevelChangedListener diffListener = new GameDiffLevelChangedListener();
        for (int i = 0; i < 5; i++) {
            diffLevels[i].setOnCheckedChangeListener(diffListener);
        }
    }

    class GameStatusChangedListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.home_cb_status_unsolved:
                    Log.d("测试", "未完成选项，选中状态" + isChecked);
                    conditions[0] = isChecked;
                    break;
                case R.id.home_cb_status_untried:
                    Log.d("测试", "未尝试选项，选中状态" + isChecked);
                    conditions[1] = isChecked;
                    break;
                case R.id.home_cb_status_solved:
                    Log.d("测试", "已完成选项，选中状态" + isChecked);
                    conditions[2] = isChecked;
                    break;
                default:
                    break;
            }
            adapter.setProblemsList(homeService.getProblemsByConditions(conditions));
        }
    }

    class GameDiffLevelChangedListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.home_cb_diff_1:
                    Log.d("测试", "难度1，选中状态" + isChecked);
                    conditions[3] = isChecked;
                    break;
                case R.id.home_cb_diff_2:
                    Log.d("测试", "难度2，选中状态" + isChecked);
                    conditions[4] = isChecked;
                    break;
                case R.id.home_cb_diff_3:
                    Log.d("测试", "难度3，选中状态" + isChecked);
                    conditions[5] = isChecked;
                    break;
                case R.id.home_cb_diff_4:
                    Log.d("测试", "难度4，选中状态" + isChecked);
                    conditions[6] = isChecked;
                    break;
                case R.id.home_cb_diff_5:
                    Log.d("测试", "难度5，选中状态" + isChecked);
                    conditions[7] = isChecked;
                    break;
                default:
                    break;
            }
            adapter.setProblemsList(homeService.getProblemsByConditions(conditions));
        }
    }
    class HomeServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            HomeService.HomeServiceBind bind = (HomeService.HomeServiceBind) service;
            Log.d("测试", "绑定成功");
            bind.setView(adapter);
            homeService = bind.getHomeService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            homeService = null;
        }
    }
}