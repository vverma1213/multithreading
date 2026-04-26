package org.example.streamapi.partitioningby;

import org.example.streamapi.groupingby.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
    PartitionBy -
    Purpose - Divides elements into 2 groups (true/false) using a predicate
    Key Type - Always Boolean
    Resulting Map - Map<Boolean,List<Type>> or with Downstream collector
    Flexible - Very Specific exactly two buckets
    Downstream support - yes
    Performance - Slightly faster only 2 buckets
 */
public class PartitionByExample {
    static void main(String[] args) {
        List<Person> people = new ArrayList<>();

        // Create 10 dummy Person objects
        Person person1 = new Person("Alice Johnson", 28, "Engineering");
        Person person2 = new Person("Bob Smith", 35, "Sales");
        Person person3 = new Person("Charlie Brown", 32, "Engineering");
        Person person4 = new Person("Diana Prince", 29, "Marketing");
        Person person5 = new Person("Evan Davis", 41, "Sales");
        Person person6 = new Person("Fiona Green", 26, "Engineering");
        Person person7 = new Person("George Harris", 38, "HR");
        Person person8 = new Person("Hannah White", 31, "Marketing");
        Person person9 = new Person("Isaac Newton", 45, "Engineering");
        Person person10 = new Person("Julia Roberts", 27, "HR");

        // Add them to the list
        people.add(person1);
        people.add(person2);
        people.add(person3);
        people.add(person4);
        people.add(person5);
        people.add(person6);
        people.add(person7);
        people.add(person8);
        people.add(person9);
        people.add(person10);

        var result = people.stream().collect(Collectors.partitioningBy(p->p.getAge()<40,
                Collectors.mapping(p->p.getName(),
                        Collectors.toList())));
        System.out.println(result);
    }
}
