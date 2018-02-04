package moma.terminer;

/**
 * Created by diamond on 2018/1/17.
 */

public enum Type {
    /**
     *
     */
    in( 0),
    out(1);

    private int value = 0;

    private Type(int value) {    //    必须是private的，否则编译错误
        this.value = value;
    }

    public static Type valueOf(int value) {    //    手写的从int到enum的转换函数
        switch (value) {
            case 0:
                return in;
            case 1:
                return out;
            default:
                return null;
        }
    }
    public int value() {
        return this.value;
    }
}
