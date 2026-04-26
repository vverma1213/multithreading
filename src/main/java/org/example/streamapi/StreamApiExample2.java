package org.example.streamapi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/*
    Stream provide interfaces to Data Structures representing a sequenced set of values

    Stream are fixed data structures whose elements are computed on demand. (lazy collection)

    No matter how many intermediate operations are there they will execute right before the terminal operation.
    That is why Streams are faster.

 */
public class StreamApiExample2 {
    static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("Adam");
        list.add("Ben");
        list.add("tiger");

        Stream<String> stream = list.stream();
        stream.forEach(System.out::println);
        //If we reuse same string again it will throw illegalestateexception
        //stream can be traveresed only once
        //stream.forEach(System.out::println);

        //this will create new stream so no exception will be thrown
        list.stream().forEach(System.out::println);
    }
}
