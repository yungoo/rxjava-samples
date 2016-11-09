package com.rainbow;

import rx.Observable;
import rx.Subscriber;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

/**
 * Created by Administrator on 2016/11/5.
 */
public class BlockingIOSample3 {
    public static void main(String[] args) {
        File dir = new File("C:\\Users\\Administrator\\Desktop\\tmp");

        Observable.from(dir.listFiles())
                .filter(f -> f.isFile())
                .sorted((l, r) -> l.getName().compareTo(r.getName()))
                .flatMap(f -> {
                    return Observable.create(new Observable.OnSubscribe<String>() {
                        @Override
                        public void call(Subscriber<? super String> subscriber) {
                            try {
                                try (Stream<String> stream = Files.lines(Paths.get(f.getAbsolutePath()))) {
                                    stream.forEach(line -> {
                                        if (!subscriber.isUnsubscribed()) {
                                            subscriber.onNext(line);
                                        }
                                    });
                                }
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onCompleted();
                                }
                            } catch (IOException e) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onError(e);
                                }
                            }
                        }
                    });
                })
                .reduce("", (l, r) -> l + " " + r)
                .subscribe(s -> System.out.println(s));
    }
}
