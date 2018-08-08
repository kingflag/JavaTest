package thread.dirtyread;

import java.util.concurrent.TimeUnit;

/**
 * @author King
 *         Created by King on 2018/8/8.
 */
public class Account {
    private String name;
    private double age;


    public synchronized void set(String name, double age) {
        this.name = name;
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.age = age;
    }

    public double getAge(String name) {
        return this.age;
    }

    public static void main(String[] args) {
        Account account = new Account();
        new Thread(() -> account.set("zhangsan", 100.0)).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(account.getAge("zhangsan"));

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(account.getAge("zhangsan"));
    }
}
