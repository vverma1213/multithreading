package org.example.streamapi;

import java.util.*;
import java.util.stream.Collectors;

public class BookCustomExample {

    static void main(String[] args) {
        List<Book> listOfBook = new ArrayList<>();
        // Create 5 custom Book objects using the constructor
        Book book1 = new Book("George Orwell", "1984 1985", 328, Type.NOVEL);
        Book book2 = new Book("J.K. Rowling", "Harry Potter and the Sorcerer's Stone", 309, Type.FICTION);
        Book book3 = new Book("Dan Brown", "The Da Vinci Code", 454, Type.THRILLER);
        Book book8 = new Book("Michale Angelo", "Mystery", 350, Type.THRILLER);
        Book book4 = new Book("Yuval Noah Harari", "Sapiens TWO", 443, Type.HISTORY);
        Book book9 = new Book("Himanshu Mehta", "Once upon a time in Mugal empire", 800, Type.HISTORY);
        Book book10 = new Book("Kahtri kulkarni", "Once upon a time in Bangalore", 900, Type.HISTORY);
        Book book5 = new Book("Friedrich Nietzsche", "Thus Spoke Zarathustra", 352, Type.PHILOSOPHY);
        Book book6 = new Book("Kevin Bua", "My name is anthony", 510, Type.NOVEL);
        Book book7 = new Book("Zimzam", "Kabuta", 510, Type.NOVEL);


        // Add them to the list
        listOfBook.add(book1);
        listOfBook.add(book2);
        listOfBook.add(book3);
        listOfBook.add(book4);
        listOfBook.add(book5);
        listOfBook.add(book6);
        listOfBook.add(book7);
        listOfBook.add(book8);
        listOfBook.add(book9);
        listOfBook.add(book10);

        //listOfBook.stream().forEach(System.out::println);
        //filter based on novel
        //System.out.println("Filter based on novel");
        //List<Book> result = listOfBook.stream().filter(x->x.getType() == Type.NOVEL).collect(Collectors.toList());
        //System.out.println(Arrays.toString(result.toArray()));
        //add few more filters such as sort the books based on author or title
        List<Book> result = listOfBook.stream().filter(x->x.getType() == Type.NOVEL)
                .sorted(Comparator.comparing(Book::getAuthor).thenComparing(Book::getPages))
                .collect(Collectors.toList());
        result.stream().forEach(System.out::println);

        //Change Type using map
        // Map - transform the type/result for example instead of book we would like to return the authors
        System.out.println("Transform the result from Book to String");
        List<String> mapResult = listOfBook.stream().filter(x->x.getType() == Type.NOVEL)
                .sorted(Comparator.comparing(Book::getAuthor).thenComparing(Book::getPages))
                .map(Book::getAuthor)
                .collect(Collectors.toList());
        mapResult.forEach(System.out::println);

        //Group the books based on the type
        System.out.println("Group the books based on the Type");
        Map<Type,List<Book>> typeListMap = listOfBook.stream().collect(Collectors.groupingBy(Book::getType));
        typeListMap.entrySet().stream().forEach(System.out::println);
        //Finding 2 longest books (pages)

        System.out.println("Find 2 longest books (with more than 500 number of pages)");

        List<String> longestBook = listOfBook.stream().filter(p->p.getPages()>500)
                .map(Book::getTitle)
                .limit(2)
                .collect(Collectors.toList());
        longestBook.stream().forEach(System.out::println);

        //Find the books for which the title is of 2 words
        System.out.println("Find the books for which the title is of 2 words");
        List<String> booksOf2Words = listOfBook.stream().filter(x->x.getTitle().split(" ").length==2)
                .map(Book::getTitle)
                .collect(Collectors.toList());
        System.out.println(booksOf2Words);

        //short-circuiting and loop fusion
        //Filter and map are different operation they are merged into the same pass (loop fusion)
        //short-circuit - some operations don't need to process the whole stream to produce a result
        //here we are looking only 2 items so the algorithm terminates after finding 2 items.
        System.out.println("Short-Circuiting and Loop Fusion");
        List<String> shortCircuting = listOfBook.stream()
                .filter(p->{
                    System.out.println("Filtering:"+p.getPages());
                    return p.getPages()>500;
                })
                .map(b->{
                    System.out.println("Mapping:"+b.getTitle());
                    return b.getTitle();
                })
                .limit(2)
                .collect(Collectors.toList());

        //map() and flatMap()
        //map() and flatMap() are similar to selecting the columns in SQL
        //map() transform the original values
        //Number of characters in every words
        System.out.println("Number of characters in every words");
        List<String> words = List.of("Adam","Sushil","Kevin","Bob","Rosy");
        words.stream().map(String::length).collect(Collectors.toList()).forEach(System.out::println);

        //create a list containing squared values
        System.out.println("Create a list Containing squared values");
        List<Integer> listOfInt = List.of(1,2,3,4,5,6);
        listOfInt.stream().map(x->x*x).collect(Collectors.toList()).forEach(System.out::println);

        //flatMap() - mapping each array not with a stream but with the content of that stream
        // [[1,3,5], [5,13]] - [1,3,5,5,13]
        //hello, shell - we want unique characters [h,e.l,o,s]
        System.out.println("FlatMap Example");
        String[] str ={"hello","shell"};
        Arrays.stream(str)
                .map(x->x.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList())
                .forEach(System.out::println);

        //Using flatmap generate pairs [1,2,3] [4,5]
        //output: (1,4),(1,5),(2,4),(2,5),(3,4),(3,5)

        List<Integer> list1=Arrays.asList(1,2,3);
        List<Integer> list2=Arrays.asList(4,5);

        list1.stream()
                .flatMap(i->list2.stream().map(j->Arrays.asList(i,j)))
                .collect(Collectors.toList())
                .forEach(System.out::println);
    }
}
