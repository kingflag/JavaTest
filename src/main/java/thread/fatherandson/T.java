package thread.fatherandson;

import java.util.concurrent.TimeUnit;

/**
 * 子类的同步方法调用父类的同步方法，锁定同一个对象（重入锁）
 * Created by King on 2018/8/8.
 */
public class T {

    public synchronized void m() {
        System.out.println("m start");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("m end");
    }

    public static void main(String[] args) {
        new TT().m();
    }
}

class TT extends T {
    @Override
    public synchronized void m() {
        System.out.println("son m end");
        super.m();
        System.out.println("son m end");
    }
}
