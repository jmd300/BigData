package ThreadSecurity;

public class ThreadSecurityTest {
    public static void main(String[] args) {
        SaleTicket s1 = new SaleTicket(200);
        Thread t1 = new Thread(s1);
        Thread t2 = new Thread(s1);
        t1.start();
        t2.start();
    }
}
