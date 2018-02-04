package moma.terminer;

/**
 * Created by diamond on 2018/1/17.
 */

public enum Port {

    /**
     *
     */
    terminal_req( 3001),
    controller_discover(3002),
    terminal_echo( 3003),
    terminal_ctl( 3004),
    CIGETFEATURE( 3020),
    AFFIRMFEATURE(3021),
    CIRECORD(3022),
    AFFIRMRECORD(3023),
    CILOCALID(3024);
    private int value = 0;

    private Port(int value) {    //    必须是private的，否则编译错误
        this.value = value;
    }

    public static Port valueOf(int value) {    //    手写的从int到enum的转换函数
        switch (value) {
            case 3001:
                return terminal_req;
            case 3002:
                return controller_discover;
            case 3003:
                return terminal_echo;
            case 3004:
                return terminal_ctl;
            case 3020:
                return CIGETFEATURE;
            case 3021:
                return AFFIRMFEATURE;
            case 3022:
                return CIRECORD;
            case 3023:
                return AFFIRMRECORD;
            case 3024:
                return CILOCALID;
            default:
                return null;
        }
    }
    public int value() {
        return this.value;
    }
}
