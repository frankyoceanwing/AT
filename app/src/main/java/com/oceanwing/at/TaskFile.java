package com.oceanwing.at;

import android.content.Context;
import android.util.Log;

import com.oceanwing.at.model.Task;
import com.oceanwing.at.util.JSONUtil;
import com.oceanwing.at.util.StorageUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class TaskFile {

    public static final String DIR_ROUTES = "routes";
    public static final String DIR_RECORDS = "records";
    private static final String TAG = TaskFile.class.getSimpleName();

    private String name;
    private long size;
    private long modifiedTime;

    public TaskFile(String name, long size, long modifiedTime) {
        this.name = name;
        this.size = size;
        this.modifiedTime = modifiedTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(long modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public static Task getTask(Context context, String name) throws IOException {
        Task task = null;
        if (StorageUtil.isExternalReadable()) {
            String dir = StorageUtil.getExternalAppFilesDir(context, TaskFile.DIR_ROUTES);
            File f = new File(dir + File.separator + name + ".txt");
            if (f.exists()) {
                try (FileReader fileReader = new FileReader(f)) {
                    task = JSONUtil.fromJson(fileReader, Task.class);
                }
            }

        }
        return task;
    }

    public static void saveTask(Context context, Task task) throws IOException {
        if (StorageUtil.isExternalWritable()) {
            String dir = StorageUtil.getExternalAppFilesDir(context, TaskFile.DIR_ROUTES);
            File d = new File(dir);
            if (!d.exists()) {
                d.mkdirs();
            }
            File f = new File(dir + File.separator + task.getName() + ".txt");
            try (FileOutputStream os = new FileOutputStream(f)) {
                String json = JSONUtil.toJson(task);
                os.write(json.getBytes());
                Log.i(TAG, "task saved");
            }
        }
    }
}
