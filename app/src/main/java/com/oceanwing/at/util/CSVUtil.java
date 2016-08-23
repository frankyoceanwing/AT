package com.oceanwing.at.util;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {

    private static final String _SEPARATOR = ",";

    private CSVUtil() {
    }

    public static void write(OutputStream os, List<String[]> rows) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
        try {
            for (String[] row : rows) {
                writer.write(TextUtils.join(_SEPARATOR, row));
                writer.newLine();
            }
            writer.flush();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }

    public static List<String[]> read(InputStream is) throws IOException {
        List<String[]> rows = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(_SEPARATOR);
                rows.add(row);
            }
            return rows;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
