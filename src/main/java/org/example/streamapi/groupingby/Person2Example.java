package org.example.streamapi.groupingby;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Person2Example {

    static void main(String[] args) {
        List<Person2> person2List = new ArrayList<>();
        Person2 person1 = new Person2("Alice Johnson", 28, List.of("Engineering","Sales"));
        Person2 person2 = new Person2("Bob Smith", 35, List.of("Sales"));
        Person2 person3 = new Person2("Charlie Brown", 32, List.of("HR","IT"));
        Person2 person4 = new Person2("Diana Prince", 29, List.of("Marketing"));
        Person2 person5 = new Person2("Evan Davis", 41, List.of("Sales"));
        person2List.add(person1);
        person2List.add(person2);
        person2List.add(person3);
        person2List.add(person4);
        person2List.add(person5);

        var result = person2List.stream().flatMap(p->p.getDepartment().stream()
                .map(d-> new AbstractMap.SimpleEntry<>(d,p)))
                .collect(Collectors.groupingBy(pair->pair.getKey(),
                        Collectors.mapping(pair->pair.getValue().getName(),Collectors.toList())));

        System.out.println(result);


    }
}
