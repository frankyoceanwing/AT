package com.oceanwing.at;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.oceanwing.at.model.Task;

public class TaskFileVH extends RecyclerView.ViewHolder {

    public TextView mName;
    public TextView mSize;
    public TextView mModifiedTime;

    public TaskFileVH(View itemView) {
        super(itemView);

        mName = (TextView) itemView.findViewById(R.id.item_name);
        mSize = (TextView) itemView.findViewById(R.id.item_size);
        mModifiedTime = (TextView) itemView.findViewById(R.id.item_modified_time);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, TaskActivity.class);
                intent.setAction(Task.ACTION_OPEN);
                intent.putExtra(Task.EXTRA_TASK_NAME, mName.getText().toString());
                context.startActivity(intent);
            }
        });
    }
}