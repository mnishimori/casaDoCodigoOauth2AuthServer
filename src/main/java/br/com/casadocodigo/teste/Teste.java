package br.com.casadocodigo.teste;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Teste {

  public static void main(String[] args) {
    int x = 10;
    int y = 5;
    y *= x + 2;
    System.out.println(y);

    List<Integer> list = new ArrayList<>(Arrays.asList(15, 25, 35));
    int product = list.stream().reduce(1, (a, b) -> a * b);
    System.out.println(product);

    final String ABC = "";

    list = new ArrayList<>(Arrays.asList(1, 2, 3));
    int sum = list.stream().reduce(0, (a, b) -> a + b);
    System.out.println(sum);

    List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 3));
    int sum2 = list.stream().mapToInt(Integer::intValue).sum();
    System.out.println(sum);



    //Stream.of("a", "b", "c").forEach(System.out::print);

    System.out.print(sum);
    System.out.println(" teste ");
    System.out.println(sum);

    Stream.of("a", "b", "c").map(s -> s.toUpperCase()).forEach(System.out::print);
  }

}
