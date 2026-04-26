package org.example.streamapi.groupingby;

import java.util.List;

public class Person2 {

    private String name;
    private int age;
    private List<String> department;

    public Person2(String name, int age, List<String> department) {
        this.name = name;
        this.age = age;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getDepartment() {
        return department;
    }

    public void setDepartment(List<String> department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Person2{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", department=" + department +
                '}';
    }
}
