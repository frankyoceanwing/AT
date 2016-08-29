package com.oceanwing.at;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oceanwing.at.util.DateTimeUtil;

import java.util.List;

public class TaskFileAdapter extends RecyclerView.Adapter<TaskFileVH> {

    private final List<TaskFile> mItems;

    public TaskFileAdapter(List<TaskFile> items) {
        this.mItems = items;
    }

    @Override
    public TaskFileVH onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.task_file_item, viewGroup, false);

        return new TaskFileVH(itemView);
    }

    @Override
    public void onBindViewHolder(TaskFileVH vh, int i) {
        TaskFile item = mItems.get(i);
        vh.mType = item.getType();
        vh.mName.setText(item.getName());
        vh.mSize.setText(String.format("%.2f KB", (item.getSize() / 1024D)));
        vh.mModifiedTime.setText(DateTimeUtil.getTime(item.getModifiedTime()));
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }
}

