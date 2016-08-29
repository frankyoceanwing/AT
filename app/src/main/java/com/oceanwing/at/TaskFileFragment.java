package com.oceanwing.at;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oceanwing.at.model.Task;
import com.oceanwing.at.util.StorageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TaskFileFragment extends Fragment {

    private static final String TAG = TaskFileFragment.class.getSimpleName();
    private RecyclerView mRootView;
    private Task.Type mTaskType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskType = (Task.Type) getArguments().getSerializable(Task.EXTRA_TASK_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (RecyclerView) inflater.inflate(R.layout.fragment_task_file, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
        initRecyclerView();
    }

    private void initRecyclerView() {
        List<TaskFile> items = new ArrayList<>();
        switch (mTaskType) {
            case MOCK:
                items = getTasks(TaskFile.DIR_ROUTES, mTaskType);
                break;
            case RECORD:
                items = getTasks(TaskFile.DIR_RECORDS, mTaskType);
                break;
        }
        mRootView.setAdapter(new TaskFileAdapter(items));
    }

    private List<TaskFile> getTasks(String dir, Task.Type type) {
        List<TaskFile> taskFiles = new ArrayList<>();
        if (StorageUtil.isExternalWritable()) {
            File f = new File(StorageUtil.getExternalAppFilesDir(getContext(), dir));
            if (!f.exists()) {
                f.mkdirs();
            }

            File[] files = f.listFiles();
            Arrays.sort(files, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return Long.compare(f2.lastModified(), f1.lastModified()); // 逆序
                }
            });
            for (File file : files) {
                taskFiles.add(new TaskFile(type, getBaseName(file.getName()), file.length(), file.lastModified()));
            }
        }
        return taskFiles;
    }

    private String getBaseName(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return fileName;
        } else {
            return fileName.substring(0, index);
        }
    }

    public static Fragment newInstance(Task.Type type) {
        TaskFileFragment f = new TaskFileFragment();
        Bundle bdl = new Bundle();
        bdl.putSerializable(Task.EXTRA_TASK_TYPE, type);
        f.setArguments(bdl);
        return f;
    }

}
