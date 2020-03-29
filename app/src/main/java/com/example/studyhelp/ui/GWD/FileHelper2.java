package com.example.studyhelp.ui.GWD;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;

public class FileHelper2 implements java.io.Serializable {

    public static final String FILENAME = "listinfo2.dat";

    public static void writeData(HashMap<String, List<String>> tasks, Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(tasks);
            oos.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, List<String>> readData(Context context) {
        HashMap<String, List<String>> tasksList = null;
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            tasksList = (HashMap<String, List<String>>) ois.readObject();
        } catch (FileNotFoundException e) {
            tasksList = new HashMap<String, List<String>>();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return tasksList;
    }

}

