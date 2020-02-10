public class Test2 {
    private Test1 test1;

    public Test2() {
        test1 = new Test1(2, this);
    }

    public Test1 getTest1() {
        return test1;
    }

    public static void main(String[] args) {
        Test2 test2= new Test2();
        System.out.println(test2.getTest1().getA());
    }
}
