package com.rainbow.streams;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * Created by Administrator on 2016/11/5.
 */
public class TraditinalHymn {
    public static void main(String[] args) {
        File dir = new File("C:\\Users\\Administrator\\Desktop\\tmp");

        File[] children = dir.listFiles();
        if (children != null) {
            List<File> txtFiles = new ArrayList<>();
            for (int i = 0; i < children.length; i++) {
                if (children[i].isFile() && children[i].getName().endsWith(".txt")) {
                    txtFiles.add(children[i]);
                }
            }

            Collections.sort(txtFiles, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });

            List<String> hymn = new ArrayList<>();
            for (File child : txtFiles) {
                if (child.isFile()) {
                    try (BufferedReader br = new BufferedReader(new FileReader(child))) {
                        for (String line; (line = br.readLine()) != null; ) {
                            hymn.add(line);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hymn.size(); i++) {
                sb.append(hymn.get(i));

                if (i != hymn.size() - 1) {
                    sb.append(' ');
                }
            }

            System.out.print(sb.toString());
        }
    }
}
