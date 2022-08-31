package MultiThreadDemo;

/**
 * 实现接口方式的好处在于：还可以继承别的类，可以方便在不同线程之间共享变量-类中的变量
 */
class MyRunAbleThread implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 100; i += 2) {
            System.out.println(Thread.currentThread().getName() + ":" + i);
        }
    }
}

public class ImplRunAbleDemo {
    public static void main(String[] args) {
        Runnable r1 = new MyRunAbleThread();
        Thread t1 = new Thread(r1);
        t1.start();
    }
}
