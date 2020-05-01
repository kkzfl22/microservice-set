package dependency;

public class DataDependency {

  public static void main(String[] args) {
    System.out.println("print curr data");

    for (int i = 0; i < args.length; i++) {
      System.out.println("itme : " + i + ",value:" + args[i]);
    }
  }
}
