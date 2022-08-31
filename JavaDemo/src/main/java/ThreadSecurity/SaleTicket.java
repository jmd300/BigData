package ThreadSecurity;


/**
 * 经典并发---多个窗口卖票
 */
public class SaleTicket implements Runnable{
    private int tickets = 100;

    SaleTicket(){

    }

    SaleTicket(int tickets){
        this.tickets = tickets;
    }

    @Override
    public void run() {
        while (true){
            if(tickets > 0){
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "线程卖票， 票剩余:" + tickets--);
            }else{
                break;
            }
        }
    }
}
