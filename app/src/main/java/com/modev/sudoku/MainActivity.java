package com.modev.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;

import com.modev.sudoku.service.MainService;
import com.modev.sudoku.views.SudokuView;

/**
 * @author: modev
 * @description: 游戏界面
 * @date: Created in 2020/7/3 20:00
 * @version: 1.0
 */
public class MainActivity extends AppCompatActivity {
    private SudokuView sudokuView;
    private Button[] buttons;
    private final int[] BUTTONS_ID = {R.id.main_bt_num1, R.id.main_bt_num2, R.id.main_bt_num3,
            R.id.main_bt_num4, R.id.main_bt_num5, R.id.main_bt_num6,
            R.id.main_bt_num7, R.id.main_bt_num8, R.id.main_bt_num9};
    private Button deleteBt;
    private Button testBt;
    private Button clearAllBt;
    private ImageButton backBt;
    private Chronometer timerChr;

    private MainService mainService;
    private ServiceConnection mainServiceConn;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent mainServeiceIntent = new Intent(this, MainService.class);
        mainServiceConn = new MainServiceConnection();
        bindService(mainServeiceIntent, mainServiceConn, BIND_AUTO_CREATE);

        init();
        setMethod();
        loadProblem();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainService.getProblem().setTime((int)(timerChr.getBase()-SystemClock.elapsedRealtime()));
        mainService.updateProblem();
        Log.d("测试", "更新问题信息"+(timerChr.getBase()-SystemClock.elapsedRealtime()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mainServiceConn);
    }

    /**
     * @author: modev
     * @date: 2020/7/4 1:26
     * @description: 初始化视图组件
     * @return: void
     * */
    private void init() {
        sudokuView= findViewById(R.id.main_suv_game);

        buttons = new Button[9];
        for (int i = 0; i < 9; i++) {
            buttons[i] = findViewById(BUTTONS_ID[i]);
        }
        deleteBt = findViewById(R.id.main_bt_delete);
        backBt = findViewById(R.id.main_ib_back);
        timerChr = findViewById(R.id.main_chr_timer);
        testBt = findViewById(R.id.main_bt_test);
        clearAllBt = findViewById(R.id.main_bt_clear_all);
    }

    /**
     * @author: modev
     * @date: 2020/7/7 19:20
     * @description: 给组件添加监听事件
     * @return: void
     * */
    private void setMethod() {
        NumberButtonClickActionListener handler = new NumberButtonClickActionListener();
        for (int i = 0; i < 9; i++) {
            buttons[i].setOnClickListener(handler);
        }
        deleteBt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sudokuView.clearNumber();
                    }
                }
        );

        backBt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        testBt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sudokuView.isDrawTestNumber()) {
                            sudokuView.setDrawTestNumber(false);
                            testBt.setBackgroundResource(R.drawable.normal_button_drawbable);
                        }else {
                            sudokuView.setDrawTestNumber(true);
                            testBt.setBackgroundResource(R.drawable.selected_button_drawbable);
                        }
                    }
                }
        );

        clearAllBt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sudokuView.clearAllNum();
                    }
                }
        );
//        timerChr.setBase(SystemClock.elapsedRealtime()+mainService.getProblem().getTime());
//        timerChr.start();
    }

    private void loadProblem() {
        Intent intent = getIntent();
        id = intent.getIntExtra("problemId", 1);
        if (mainService != null) {
            mainService.setProblem(id);
            sudokuView.setProblem(mainService.getProblem());
            Log.d("测试", "问题添加成功，problemId：" + id);
        }else {
            Log.d("测试", "问题还未添加，problemId：" + id);
        }
    }

    class MainServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MainService.MainServiceBind bind = (MainService.MainServiceBind) service;
            mainService = bind.getMainService();
            Log.d("测试", "bind中的id" + id);
            bind.setSudoViewAndProblem(sudokuView, id);
            bind.setTimer(timerChr);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mainService = null;
        }
    }

    class NumberButtonClickActionListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_bt_num1:
                    sudokuView.setNumber(1);
                    break;
                case R.id.main_bt_num2:
                    sudokuView.setNumber(2);
                    break;
                case R.id.main_bt_num3:
                    sudokuView.setNumber(3);
                    break;
                case R.id.main_bt_num4:
                    sudokuView.setNumber(4);
                    break;
                case R.id.main_bt_num5:
                    sudokuView.setNumber(5);
                    break;
                case R.id.main_bt_num6:
                    sudokuView.setNumber(6);
                    break;
                case R.id.main_bt_num7:
                    sudokuView.setNumber(7);
                    break;
                case R.id.main_bt_num8:
                    sudokuView.setNumber(8);
                    break;
                case R.id.main_bt_num9:
                    sudokuView.setNumber(9);
                    break;
                default:
                    break;
            }
        }
    }

}
