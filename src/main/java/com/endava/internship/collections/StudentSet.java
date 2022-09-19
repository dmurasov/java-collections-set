package com.endava.internship.collections;

import java.util.*;
import java.lang.reflect.Array;

public class StudentSet implements Set<Student> {
    private Entry<Student>[] buckets;
    private int capacity, size;
    private static final int INITIAL_CAPACITY = 16;
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        int i = o == null ? 0 : hash(o.hashCode());
        Entry<Student> bucket = buckets[i];

        while (bucket != null) {
            if(o == null){
                if(bucket.object == null) {
                    return true;
                }
            }
            if(bucket.object != null) {
                if(bucket.object.equals(o)) {
                    return true;
                }
            }
            bucket = bucket.next;
        }
        return false;
    }

    @Override
    public Iterator<Student> iterator() {
        return new StudentSetIterator();
    }

    @Override
    public Object[] toArray() {
        Iterator<Student> stIterator = iterator();
        Object[] array = new Object[size()];

        for (int i = 0; i < array.length; i++) {
            if (stIterator.hasNext()) {
                array[i] = stIterator.next();
            }else{
                return Arrays.copyOf(array, i);
            }
        }
        return array;
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        T[] array;
        Iterator<Student> stIterator = iterator();

        if(ts.length >= size()){
            array = ts;
        }else {
            //if the array is too small, a new one is created
            array = (T[])Array.newInstance(ts.getClass().getComponentType(), size);
        }

        for(int i = 0; i < array.length; i++) {
            if(!stIterator.hasNext()){
                if(ts == array){
                    array[i] = null;
                }else if(ts.length < i) {
                    return Arrays.copyOf(array, i);
                }else {
                    System.arraycopy(array, 0, ts, 0, i);
                    if (ts.length > i) {
                        ts[i] = null;
                    }
                }
                return ts;
            }
            array[i] = (T)stIterator.next();
        }
        return array;
    }

    @Override
    public boolean add(Student student){
        resizeHashSet();

        int i = student == null ? 0 : hash(student.hashCode());
        Entry<Student> bucket = buckets[i];

        //check if element is already in set with cycle
        while (bucket != null) {
            if(student == null){
                if(bucket.object == null){
                    return false;
                }
            }
            if(bucket.object != null){
                if(bucket.object.equals(student)) {
                    return false;
                }
            }
            //visit next entry in the bucket
            bucket = bucket.next;
        }
        Entry<Student> entry = new Entry<>();
        entry.object = student;

        if(buckets[i] == null){
            buckets[i] = entry;
        }else{
            Entry<Student> temp = buckets[i];
            while(temp.next != null){
                temp = temp.next;
            }
            temp.next = entry;
        }
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int i;
        if(o == null) {
            i = 0;
        }else{
            i = hash(o.hashCode());
        }
        Entry<Student> bucket = buckets[i];
        Entry<Student> previous = null;

        //find the element with cycle
        while (bucket != null){
            if(bucket.object == null){
                if(bucket.object == o) {
                    buckets[i] = bucket.next;
                    return true;
                }
            }
            if (bucket.object.equals(o)) {
                if(previous == null){
                    buckets[i] = bucket.next;
                }else{
                    previous.next = bucket.next;
                }
                size--;
                return true;
            }
            previous = bucket;
            bucket = bucket.next;
        }
        return false;
    }

    @Override
    public void clear() {
        if (buckets != null && size > 0) {
            this.size = 0;
            for (int i = 0; i < buckets.length; ++i)
                buckets[i] = null;
        }
    }

    @Override
    public boolean addAll(Collection<? extends Student> collection) {
        boolean added = false;

        for(Student st : collection){
            if(add(st)){
                added = true;
            }
        }
        return added;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        boolean contain = false;
        for (Object e : collection) {
            if (contains(e)) {
                contain = true;
            }else{
                contain = false;
                break;
            }
        }
        return contain;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        //throwing exception if null
        Objects.requireNonNull(collection);

        boolean retained = false;
        Iterator<?> stIterator = iterator();
        while(stIterator.hasNext()) {
            Student st = (Student)stIterator.next();
            if (!collection.contains(st)) {
                this.remove(st);
                retained = true;
            }
        }
        return retained;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        //throwing exception if null
        Objects.requireNonNull(collection);
        boolean removed = false;
        Iterator<?> stIterator = iterator();
        while (stIterator.hasNext()){
            Student st = (Student)stIterator.next();
            if (collection.contains(st)){
                System.out.println(st);
                this.remove(st);
                removed = true;
            }
        }
        return removed;
    }

    //hash function
    private int hash(int hashCode) {
        int index = hashCode;
        if (index < 0) {
            index = -index;
        }
        return index % this.buckets.length;
    }

    private void setCapacity(int capacity){
        this.capacity = capacity;
    }
    public StudentSet(int capacity){
        this.buckets = new Entry[capacity];
        setCapacity(capacity);
    }
    public StudentSet(){
        this(INITIAL_CAPACITY);
    }

    private void resizeHashSet(){
        double checkSize = capacity * 0.75;
        if(size > checkSize){
            this.capacity *= 2;
            this.size = 0;

            Entry<Student>[] old = buckets;
            this.buckets = new Entry[capacity];

            for(Entry<Student> o : old){
                if(o != null) {
                    while(o != null) {
                        add(o.object);
                        o = o.next;
                    }
                }
            }
        }
    }

    private static class Entry<T> {
        private T object;
        private Entry<T> next = null;
    }

    //Iterator for HashSet
    private class StudentSetIterator implements Iterator<Student> {
        private int currentBucket;
        private int previousBucket;
        private Entry<Student> currentEntry;
        private Entry<Student> previousEntry;

        public StudentSetIterator() {
            currentEntry = null;
            previousEntry = null;
            currentBucket = -1;
            previousBucket = -1;
        }

        @Override
        public boolean hasNext() {
            if (currentEntry != null && currentEntry.next != null) {
                return true;
            }
            for (int i = currentBucket+1; i < buckets.length; i++) {
                if (buckets[i] != null) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Student next() {
            previousEntry = currentEntry;
            previousBucket = currentBucket;

            if (currentEntry == null || currentEntry.next == null) {
                currentBucket++;
                while (currentBucket < buckets.length && buckets[currentBucket] == null) {
                    currentBucket++;
                }
                if (currentBucket < buckets.length) {
                    currentEntry = buckets[currentBucket];
                }
                else {
                    throw new NoSuchElementException();
                }
            }
            else {
                currentEntry = currentEntry.next;
            }
            return currentEntry.object;
        }
    }
}
