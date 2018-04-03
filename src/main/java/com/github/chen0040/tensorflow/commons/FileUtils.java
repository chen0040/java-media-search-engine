package com.github.chen0040.tensorflow.commons;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static List<String> getAudioFilePaths() {
        return getAudioFilePaths("music_samples", ".au");
    }

    public static List<String> getAudioFilePaths(String dirPath, String extension) {
        List<String> result = new ArrayList<>();
        File dir = new File(dirPath, extension);
        System.out.println(dir.getAbsolutePath());
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                String file_path = f.getAbsolutePath();
                if (file_path.toLowerCase().endsWith(extension)) {
                    result.add(file_path);
                }
            }
        }

        return result;
    }


    public static File[] getAudioFiles() {
        List<File> result = new ArrayList<>();
        File dir = new File("music_samples");
        System.out.println(dir.getAbsolutePath());
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                String file_path = f.getAbsolutePath();
                if (file_path.endsWith(".au")) {
                    result.add(f);
                }
            }
        }

        File[] files = new File[result.size()];
        for(int i=0; i < files.length;++i) {
            files[i] = result.get(i);
        }
        return files;
    }

    public static File[] getImageFiles() {
        return getImageFiles("image_samples", ".png");
    }

    public static File[] getImageFiles(String dirPath, String extension) {
        List<File> result = new ArrayList<>();
        File dir = new File(dirPath);
        System.out.println(dir.getAbsolutePath());

        getImageFiles(dir, result, extension);

        File[] files = new File[result.size()];
        for(int i=0; i < files.length;++i) {
            files[i] = result.get(i);
        }
        return files;
    }

    private static void getImageFiles(File dir, List<File> result, String extension) {
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                if(f.isDirectory()){
                    getImageFiles(f, result, extension);
                } else {
                    String file_path = f.getAbsolutePath();
                    if (file_path.endsWith(extension)) {
                        result.add(f);
                    }
                }
            }
        }
    }

    public static List<String> getImageFilePaths() {
        return getImageFilePaths("image_samples", ".png");
    }

    public static List<String> getImageFilePaths(String dirPath, String extension) {
        List<String> result = new ArrayList<>();
        File[] files = getImageFiles(dirPath, extension);
        for(File f : files) {
            result.add(f.getAbsolutePath());
        }
        return result;
    }

}
