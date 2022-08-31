package MultiThreadDemo;

/**
 * 问题1：启动线程必须调用start方法，不能调用run方法的方式启动线程
 * 问题2：如果再启动一个线程，必须重新创建一个Thread子类的对象，调用此对象的start
 */
class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 100; i += 2) {
            System.out.println(Thread.currentThread().getName() + ":" + i);
        }
    }
}

public class ExtendsThreadDemo {
    public static void main(String[] args) {
        Thread t1 = new MyThread();
        t1.start();
    }
}
