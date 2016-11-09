package com.rainbow;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.observables.AsyncOnSubscribe;
import rx.schedulers.Schedulers;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2016/11/5.
 */

class User {
    private Long id;
    private Long schoolId;
    private String name;

    public User(Long id, String name, Long schoolId) {
        this.id = id;
        this.name = name;
        this.schoolId = schoolId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getSchoolId() {
        return schoolId;
    }
}

class Course {
    private String name;

    public Course(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        return name != null ? name.equals(course.name) : course.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}

public class BlockingIOSample {

    public static Observable<User> retrieveUser(long userId) {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> subscriber) {
                subscriber.onStart();
                try {
                    System.out.println(Thread.currentThread().getName() + ": retrieveUser");

                    Thread.sleep(1000);
                    User user = new User(userId, "haiyang", 1000L);
                    subscriber.onNext(user);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public static Observable<List<Course>> queryUserJoinedCourses(long userId, long schoolId) {
            return Observable.create(new Observable.OnSubscribe<List<Course>>() {
                @Override
                public void call(Subscriber<? super List<Course>> subscriber) {
                    subscriber.onStart();
                    try {
                        System.out.println(Thread.currentThread().getName() + ": queryUserJoinedCourses");

//                        Thread.sleep(1000);
                        List<Course> joinedCourses = Arrays.asList(new Course("literature"), new Course("math"));
                        subscriber.onNext(joinedCourses);
                        subscriber.onCompleted();
                    } catch (Exception e) {
                        subscriber.onCompleted();
                    }
                }
            });
    }

    public static Observable<List<Course>> querySchoolCourses(long schoolId) {
        return Observable.create(new Observable.OnSubscribe<List<Course>>() {
            @Override
            public void call(Subscriber<? super List<Course>> subscriber) {
                subscriber.onStart();
                try {
                    System.out.println(Thread.currentThread().getName() + ": querySchoolCourses");

//                    Thread.sleep(1000);
                    List<Course> joinedCourses = Arrays.asList(new Course("chinese"), new Course("physical"),
                            new Course("literature"), new Course("math"));
                    subscriber.onNext(joinedCourses);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(10);
        Scheduler scheduler = Schedulers.from(executor);

        final long userId = 1;
        retrieveUser(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .flatMap(user -> {
                    return Observable.zip(querySchoolCourses(user.getSchoolId()).subscribeOn(Schedulers.io()).observeOn(scheduler),
                            queryUserJoinedCourses(user.getId(), user.getSchoolId()).subscribeOn(Schedulers.io()).observeOn(scheduler),
                            (openCourses, joinedCourse) -> {
                                System.out.println(Thread.currentThread().getName() + ": zip");
                                return Observable.from(openCourses)
                                        .filter(c -> joinedCourse.contains(c))
                                        .toList()
                                        .toBlocking()
                                        .single();
                            });
                })
                .flatMap(courses -> Observable.from(courses))
                .subscribe(course -> {
                    System.out.println(Thread.currentThread().getName() + ": result");
                    System.out.println(course.getName());
                });

        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
