package com.multithreading.datastructures;

public class StackWithLock {

    private static class StaticStack<T> {
        public StackNode<T> head;

        public synchronized void push(T value) {
            StackNode<T> newNode = new StackNode<>(value);
            newNode.next = head;
            head = newNode;
        }

        public synchronized T pop() {
            if(head == null) {
                return null;
            }
            T value = head.value;
            head = head.next;
            return value;
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
