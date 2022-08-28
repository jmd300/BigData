package MapDemo;

/**
 * HashMap 继承自 AbstractMap 类，Serializable，Cloneable，Map接口
 * 最常用的Map，根据键的hashcode值来存储数据（采用链地址法，也就是数组+链表+红黑树的方式），
 * 根据键可以快速获得它的值（因为相同的键hashcode值相同，
 * 在地址为hashcode值的地方存储的就是值，所以根据键可以很快获得值。即使键值不同而hashcode值相同，
 * 也能快速找到对应hashcode的链表，从而通过遍历快速找到值）。
 *
 */

public class HashMapDemo {
    public static void main(String[] args) {

    }
}
