package com.enjoy.patch;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;


public class EnjoyFix {

    private static final String TAG = "EnjoyFix";

    private static File initHack(Context context) {
        File hackFile = new File(context.getExternalFilesDir(""), "hack.dex");
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            fos = new FileOutputStream(hackFile);
            is = context.getAssets().open("hack.dex");
            int len;
            byte[] buffer = new byte[2048];
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return hackFile;
    }

    /**
     * 1、获取程序的PathClassLoader对象
     * 2、反射获得PathClassLoader父类BaseDexClassLoader的pathList对象
     * 3、反射获取pathList的dexElements对象 （oldElement）
     * 4、把补丁包变成Element数组：patchElement（反射执行makePathElements）
     * 5、合并patchElement+oldElement = newElement （Array.newInstance）
     * 6、反射把oldElement赋值成newElement
     *
     * @param application
     * @param patch
     */
    public static void installPatch(Application application, File patch) {
        File hackDex = initHack(application);
        List<File> patchs = new ArrayList<>();
        patchs.add(hackDex);
        if (patch.exists()) {
            patchs.add(patch);
        }

        //1、获取程序的PathClassLoader对象
        ClassLoader classLoader = application.getClassLoader();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                ClassLoaderInjector.inject(application, classLoader, patchs);
            } catch (Throwable throwable) {
            }
            return;
        }
        //2、反射获得PathClassLoader父类BaseDexClassLoader的pathList对象
        try {
            Field pathListField = ShareReflectUtil.findField(classLoader, "pathList");
            Object pathList = pathListField.get(classLoader);
            //3、反射获取pathList的dexElements对象 （oldElement）
            Field dexElementsField = ShareReflectUtil.findField(pathList, "dexElements");
            Object[] oldElements = (Object[]) dexElementsField.get(pathList);
            //4、把补丁包变成Element数组：patchElement（反射执行makePathElements）
            Object[] patchElements = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Method makePathElements = ShareReflectUtil.findMethod(pathList, "makePathElements",
                        List.class, File.class,
                        List.class);
                ArrayList<IOException> ioExceptions = new ArrayList<>();
                patchElements = (Object[])
                        makePathElements.invoke(pathList, patchs, application.getCacheDir(), ioExceptions);

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Method makePathElements = ShareReflectUtil.findMethod(pathList, "makeDexElements",
                        ArrayList.class, File.class, ArrayList.class);
                ArrayList<IOException> ioExceptions = new ArrayList<>();
                patchElements = (Object[])
                        makePathElements.invoke(pathList, patchs, application.getCacheDir(), ioExceptions);
            }


            //5、合并patchElement+oldElement = newElement （Array.newInstance）
            //创建一个新数组，大小 oldElements+patchElements
//                int[].class.getComponentType() ==int.class
            Object[] newElements = (Object[]) Array.newInstance(oldElements.getClass().getComponentType(),
                    oldElements.length + patchElements.length);

            System.arraycopy(patchElements, 0, newElements, 0, patchElements.length);
            System.arraycopy(oldElements, 0, newElements, patchElements.length, oldElements.length);
            //6、反射把oldElement赋值成newElement
            dexElementsField.set(pathList, newElements);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
