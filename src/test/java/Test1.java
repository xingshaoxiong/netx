public class Test1 {
    int a;
    private Test2 test2;
    public Test1(int a, Test2 test2) {
        this.a = a;
        this.test2 =test2;
    }

    public int getA() {
        return a;
    }

    public Test2 getTest2() {
        return test2;
    }
}
