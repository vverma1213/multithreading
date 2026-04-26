package org.example.streamapi.groupingby;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
    GroupingBy is a collector provided by Collectors class that allows grouping of elements in a stream
    based on a classifier function (like grouping people by age, employees by department etc...)

    Purpose - Group elements by classifier function returning any type of key
    key type - Any Object - String, Integer...custom classes
    Resulting Map - Map<K,List<T>> or with downstream collector
    Flexible - Highly Flexible group into many buckets
    Downstream Support - Yes
    Performance - can be more complex (hashing, many keys)

 */
public class GroupingByExample {
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

        //Classifier - In this example Department
        //Return map
        //we can return other Downstream collectors
        //toList, toSet, counting, mapping, reducing ,maxBy,minBy
        var result = people.stream()
                .collect(Collectors.groupingBy(p->p.getDepartment(),Collectors.counting()));
        System.out.println(result);
        //mapping - Transform elements before collecting

        var result2 = people.stream()
                .collect(Collectors.groupingBy(p->p.getDepartment(),
                        Collectors.mapping(Person::getName,Collectors.toList())));
        System.out.println(result2);

    }
}
