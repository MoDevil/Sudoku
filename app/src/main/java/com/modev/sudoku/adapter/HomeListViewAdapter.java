package com.modev.sudoku.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.modev.sudoku.R;
import com.modev.sudoku.modile.Problem;
import com.modev.sudoku.views.SudokuCover;

import java.util.List;

/**
 * @author: modev
 * @description:
 * @date: Created in 2020/7/9 19:06
 * @version: 1.0
 */
public class HomeListViewAdapter extends ArrayAdapter<Problem> {
    private int resource;

    public HomeListViewAdapter(@NonNull Context context, int resource, @NonNull List<Problem> objects) {
        super(context, resource, objects);
        this.resource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Problem problem = getItem(position);
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.cover = view.findViewById(R.id.home_item_iv_cover);
            viewHolder.level = view.findViewById(R.id.home_item_tv_level);
            viewHolder.diff = view.findViewById(R.id.home_item_tv_diff);
            viewHolder.status = view.findViewById(R.id.home_item_tv_status);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.cover.setImageResource(R.drawable.icon);
//        Log.d("测试", problem.getId()+"\t " + viewHolder.cover);
//        viewHolder.cover.setProblem(problem);
        viewHolder.level.setText("第"+(problem.getId())+"题");
        viewHolder.diff.setText("难度："+problem.getDiff());
        if (problem.getStatus() == -1) {
            viewHolder.status.setText("状态：未尝试");
        }else if (problem.getStatus() == 0) {
            viewHolder.status.setText("状态：未完成");
        }else {
            viewHolder.status.setText("状态：已完成");
        }
        return view;
    }
    public void setProblemsList(List<Problem> problems) {
        super.clear();
        super.addAll(problems);
//        super.notifyDataSetChanged();
    }
    class ViewHolder{
        ImageView cover;
//        SudokuCover cover;
        TextView level;
        TextView diff;
        TextView status;
    }
}
