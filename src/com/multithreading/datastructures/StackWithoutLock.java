package com.multithreading.datastructures;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class StackWithoutLock {

    private static class StaticStack<T> {
        public AtomicReference<StackNode<T>> head = new AtomicReference<>();
        private AtomicInteger counter = new AtomicInteger(0);

        public void push(T value) {
            StackNode<T> newNode = new StackNode<>(value);

            while (true) {
                StackNode<T> currentHead = head.get();
                newNode.next = currentHead;
                if (head.compareAndSet(currentHead, newNode)) {
                    break;
                } else {
                    LockSupport.parkNanos(2);
                }
            }
            counter.incrementAndGet();
        }

        public T pop() {
            StackNode<T> currentHead = head.get();
            StackNode<T> newHead;
            while (Objects.nonNull(currentHead)) {
                newHead = currentHead.next;
                if (head.compareAndSet(currentHead, newHead)) {
                    break;
                } else {
                    LockSupport.parkNanos(2);
                    currentHead = head.get();
                }
            }
            counter.incrementAndGet();
            return Objects.nonNull(currentHead) ? currentHead.value : null;
        }

        public int getCounter() {
            return counter.get();
        }
    }

    private static class StackNode<T> {
        public T value;
        public StackNode<T> next;

        public StackNode(T value) {
            this.value = value;
            this.next = null;
        }
    }
}
