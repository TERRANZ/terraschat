package ru.terra.tschat.client.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import dalvik.system.DexFile;
import ru.terra.tschat.shared.core.AbstractClassSearcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Date: 20.03.15
 * Time: 16:39
 */
public class AndroidClassSearcher<T> extends AbstractClassSearcher<T> {
    private static final String TAG = "AndroidClassSearcher";
    private Context context;

    public AndroidClassSearcher(Context context) {
        this.context = context;
    }

    @Override
    public List<T> load(String packageName, Class annotaion) {
        List<T> ret = new ArrayList<>();
        ApplicationInfo ai = context.getApplicationInfo();
        String classPath = ai.sourceDir;
        DexFile dex = null;
        try {
            dex = new DexFile(classPath);
            Enumeration<String> apkClassNames = dex.entries();
            while (apkClassNames.hasMoreElements()) {
                String className = apkClassNames.nextElement();
                try {
                    Class c = context.getClassLoader().loadClass(className);
                    if (c != null && c.getName().contains(packageName) && c.getAnnotation(annotaion) != null) {
                        try {
                            ret.add((T) c.newInstance());
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //              android.util.Log.i("nora", className);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                dex.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        return ret;
    }
}
