package MapDemo;

/**
 * LinkedHashMap是HashMap的一个子类，但它保持了记录的插入顺序（内部维护了一个双向链表），遍历时会通过链来遍历。
 * 可以在构造时带参数，按照应用次数排序，在遍历时会比HahsMap慢，不过有个例外，当HashMap的容量很大，实际数据少时，
 * 遍历起来会比LinkedHashMap慢(因为它是链)，因为HashMap的遍历速度和它容量有关，LinkedHashMap遍历速度只与数据多少有关
 */

public class LinkedHashMapDemo {
    public static void main(String[] args) {

    }
}
