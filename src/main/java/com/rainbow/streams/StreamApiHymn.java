package com.rainbow.streams;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * Created by Administrator on 2016/11/5.
 */
public class StreamApiHymn {
    public static void main(String[] args) {
        File dir = new File("C:\\Users\\Administrator\\Desktop\\tmp");

        final String hymn = Stream.of(dir.listFiles())
                .filter(File::isFile)
                .sorted(Comparator.comparing(File::getName))
                .map(child -> {
                    try (Stream<String> stream = Files.lines(Paths.get(child.getAbsolutePath()))) {
                        return stream.collect(Collectors.toList());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return new ArrayList<String>();
                })
                .flatMap(p -> p.stream())
                .collect(joining(" "));

        //print: you are awesome haiyang !
        System.out.println(hymn);
    }
}
