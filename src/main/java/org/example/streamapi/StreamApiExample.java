package org.example.streamapi;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
    Stream API heavily dependent on Lambda expression
    we can construct parallel operations quite easily

    a stream is a sequence of elements from a source that supports data processing
    operations.

    Data structures are about storing items
    streams are about computation

    streams support database-like operations and common operation
    from functional programming languages
    filter, map, reduce etc....

    stream operations can be executed sequentially or in parallel

    we can define intermediate operations that return a stream as well - allow multiple
    operations to be chained (This is called Pipelining)

    Source (collection of data)-> Intermediate Operations (return streams) (filtering,sorting) ->
    Terminal Operation (return scalar or void- collect, reduce)

    Collection are essentially DS in the RAM

    Collection need the whole data in advance - every element in the collection must be computed
    before it can be added to collection.
    It takes time to handle a collection
 */
public class StreamApiExample {
    static void main() {
        IntStream numArry = IntStream.range(1,10);
        List<Integer> list = numArry.boxed().toList();
        list.stream().filter(x->x%2==0).forEach(System.out::println);

        String [] stringArra = {"Adam","Evie","Linda","Mike","Zed","Bob"};

        Stream.of(stringArra).sorted(Comparator.reverseOrder()).filter(str->str.contains("e")).forEach(System.out::println);
    }
}
