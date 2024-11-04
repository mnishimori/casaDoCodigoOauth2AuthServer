package br.com.casadocodigo.teste;

class A {
  public void foo() {
    System.out.println("A");
  }
}

class B extends A {
  public void foo() {
    System.out.println("B");
  }
}

public class Test {
  public static void main(String[] args) {
    A a = new B();
    a.foo();
  }
}
