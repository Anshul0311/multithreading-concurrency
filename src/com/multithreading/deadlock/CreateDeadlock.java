package com.multithreading.deadlock;

import java.util.Random;

public class CreateDeadlock {

    public static void createDeadlock() {
        RoadIntersection roadIntersection = new RoadIntersection();
        Thread trainA = new Thread(new TrainA(roadIntersection));
        Thread trainB = new Thread(new TrainB(roadIntersection));
        trainA.start();
        trainB.start();
    }

    private static class TrainA implements Runnable {

        private RoadIntersection roadIntersection;
        private Random random = new Random();

        public TrainA(RoadIntersection roadIntersection) {
            this.roadIntersection = roadIntersection;
        }

        @Override
        public void run() {
            while (true) {
                int runningTime = random.nextInt(5);
                try {
                    Thread.sleep(runningTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                roadIntersection.takeRoadA();
            }
        }
    }

    private static class TrainB implements Runnable {

        private RoadIntersection roadIntersection;
        private Random random = new Random();

        public TrainB(RoadIntersection roadIntersection) {
            this.roadIntersection = roadIntersection;
        }

        @Override
        public void run() {
            while (true) {
                int runningTime = random.nextInt(5);
                try {
                    Thread.sleep(runningTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                roadIntersection.takeRoadB();
            }
        }
    }

    private static class RoadIntersection {

        Object roadA = new Object();
        Object roadB = new Object();

        public void takeRoadA() {
            synchronized (roadA) {
                System.out.println("Road A is locked by Thread : " + Thread.currentThread().getName());

                //while roadA is being used, don't allow any thread to use roadB
                synchronized (roadB) {
                    System.out.println("Train is passing through road A");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        public void takeRoadB() {
            synchronized (roadB) {
                System.out.println("Road B is locked by Thread : " + Thread.currentThread().getName());

                //while roadB is being used, don't allow any thread to use roadA
                synchronized (roadA) {
                    System.out.println("Train is passing through road B");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    }
}
