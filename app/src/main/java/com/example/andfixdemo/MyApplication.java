package com.example.andfixdemo;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import com.alipay.euler.andfix.patch.PatchManager;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by v_yanligang on 2017/4/26.
 */

public class MyApplication extends Application {
    private Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();

        PatchManager patchManager = new PatchManager(mContext);
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            patchManager.init(versionName);
            patchManager.loadPatch();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        Log.e("MyApplication", "apatch" + file.getAbsolutePath());
        List<File> apatchs = listFiles(file, "apatch");
        if (apatchs != null && apatchs.size() != 0) {

            try {
                for(File apatch: apatchs) {
                    Log.e("MyApplication", "apatch" + apatch.getName());
                    patchManager.addPatch(apatch.getAbsolutePath());
                    if (apatch.exists()) {
                        apatch.delete();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.mContext = base;
    }

    private List<File> listFiles(File file, final String extension) {
        File[] files = null;
        if (file == null || !file.exists() || !file.isDirectory()) {
            return null;
        }
        files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(extension);
            }
        });
        if (files != null) {
            ArrayList<File> lists = new ArrayList<>(Arrays.asList(files));
            return lists;
        }
        return null;
    }
}
