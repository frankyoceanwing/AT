package com.oceanwing.at;

import android.content.Context;
import android.util.Log;

import com.oceanwing.at.model.Position;
import com.oceanwing.at.model.Task;
import com.oceanwing.at.util.CSVUtil;
import com.oceanwing.at.util.JSONUtil;
import com.oceanwing.at.util.StorageUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskFile {

    public static final String DIR_ROUTES = "routes";
    public static final String DIR_RECORDS = "records";
    private static final String TAG = TaskFile.class.getSimpleName();

    private Task.Type mType;
    private String mName;
    private long mSize;
    private long mModifiedTime;

    public TaskFile(Task.Type type, String name, long size, long modifiedTime) {
        this.mType = type;
        this.mName = name;
        this.mSize = size;
        this.mModifiedTime = modifiedTime;
    }

    public Task.Type getType() {
        return mType;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public long getSize() {
        return mSize;
    }

    public void setSize(long size) {
        this.mSize = size;
    }

    public long getModifiedTime() {
        return mModifiedTime;
    }

    public void setModifiedTime(long modifiedTime) {
        this.mModifiedTime = modifiedTime;
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

    public static List<Position> parseGTFile(Context context, String name) throws IOException {
        List<Position> positions = new ArrayList<>();
        if (StorageUtil.isExternalReadable()) {
            String dir = StorageUtil.getExternalAppFilesDir(context, TaskFile.DIR_RECORDS);
            File f = new File(dir + File.separator + name + ".gps");
            Position position = null;
            if (f.exists()) {
                try (FileInputStream fileInputStream = new FileInputStream(f)) {
                    List<String[]> rows = CSVUtil.read(fileInputStream);
                    for (String[] row : rows) {
                        if (row.length != 8) {
                            continue;
                        }
                        position = new Position(Double.valueOf(row[1]), Double.valueOf(row[0]));
                        position.setAccuracy(Float.valueOf(row[2]));
                        position.setBearing(Float.valueOf(row[3]));
                        position.setSpeed(Float.valueOf(row[4]));
                        position.setAltitude(Double.valueOf(row[7]));
                        positions.add(position);
                    }
                }
            }

        }
        return positions;
    }
}
