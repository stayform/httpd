/**
 * AuThor：StAY_
 * Create:2019/11/17
 */
public class Test {
    public static void main(String[] args) {
        System.out.println("1.数学计算开始前");
        try {
            System.out.println("2.进⾏数学计算："+10/0);
        } catch (ArithmeticException e) {
            System.out.println("异常已经被处理了");
        }
        System.out.println("3.数学计算结束后");
    }
}
