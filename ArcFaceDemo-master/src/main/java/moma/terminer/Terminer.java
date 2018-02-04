package moma.terminer;

import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Date;

import moma.utils.Utils;

/**
 * Created by diamond on 2018/1/16.
 */

public class Terminer {

    private static Terminer instance = new Terminer();

    private boolean     ctlonline = false;  //控制器在线
    private InetAddress ctladdr;    //controller地址
    private long         secho;      //心跳标志
    private int         userid;     //当前用户id 判断是否连续开门
    private long        pre_req;     //上一次请求时间
    private byte[] data_head = new byte[2];
    private byte[] data_cmd = new byte[1];
    private byte[] data_length = new byte[4];
    private byte[] data_end = new byte[3];


    private DatagramSocket discoryscoket;
    private DatagramSocket echosocket;
    private ServerSocket ctlsocket;

    private Terminer(){

        try {
            discoryscoket = new DatagramSocket(Port.controller_discover.value()+10);
            echosocket = new DatagramSocket(Port.terminal_echo.value()+10);
            ctlsocket = new ServerSocket(Port.terminal_ctl.value()+10);
            secho = System.currentTimeMillis();
            pre_req = System.currentTimeMillis();

            data_head[0] =(byte) 0xEA;
            data_head[1] =(byte) 0xAE;

            data_end[0] = (byte) 0xEE;
            data_end[1] = (byte) 0xEE;
            data_end[2] = (byte) 0xEE;


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Terminer getInstance() {
        return instance;
    }


    public boolean isCtlonline() {
        return instance.ctlonline;
    }

    public void setCtlonline(boolean ctlonline) {
        instance.ctlonline = ctlonline;
    }

    public InetAddress getCtladdr() {
        return instance.ctladdr;
    }

    public void setCtladdr(InetAddress ctladdr) {
        instance.ctladdr = ctladdr;
    }

    public long getSecho() {
        return instance.secho;
    }

    public void setSecho(long secho) {
        instance.secho = secho;
    }

    public int getUserid() {
        return instance.userid;
    }

    public void setUserid(int userid) {
        instance.userid = userid;
    }

    public long getPre_req() {
        return instance.pre_req;
    }

    public void setPre_req(long pre_req) {
        instance.pre_req = pre_req;
    }

    class dodiscory implements Runnable {
        String TAG = "dodiscory";
        DatagramPacket packet;

        public dodiscory(DatagramPacket packet){
            this.packet = packet;
        }
        @Override
        public void run() {

            Log.d(TAG,"处理广播数据包");
            //校验数据包
            InetAddress address = packet.getAddress();

            byte[] data = packet.getData();
            Log.d(TAG,Utils.bytesToHexString(data));
            if("eaae04".equals(Utils.bytesToHexString(data).substring(0,6))){
                Log.d(TAG,"地址有效"+address.getHostAddress());
                Terminer.getInstance().setCtladdr(address);
                Terminer.getInstance().setSecho(System.currentTimeMillis());
                Terminer.getInstance().setCtlonline(true);
            }else {
                Log.d(TAG,"地址无效"+address.getHostAddress());
            }

        }
    }
    class doecho implements Runnable {
        String TAG = "doecho";
        DatagramPacket packet;

        public doecho(DatagramPacket packet){
            this.packet = packet;
        }
        @Override
        public void run() {
            InetAddress address = packet.getAddress();
            Log.d(TAG,"处理心跳数据包");
            byte[] data = packet.getData();
            Log.d(TAG,Utils.bytesToHexString(data));
            if(Terminer.getInstance().getCtladdr().getHostAddress().equals(packet.getAddress().getHostAddress())){
                Log.d(TAG,"心跳有效"+address.getHostAddress());
                Terminer.getInstance().setSecho(System.currentTimeMillis());
            }
        }
    }
    class doctl implements Runnable {
        String TAG = "doectl";
        Socket socket;

        public doctl(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            Log.d(TAG,"闸机控制");
        }
    }

    public void listenDis(){
        String TAG = "控制器回复发现包==";
        Log.d(TAG,"开始监听");
        while (true){
            DatagramPacket packet = null;
            try {
                byte[] data = new byte[10];
                packet = new DatagramPacket(data, data.length);
                discoryscoket.receive(packet);//此方法在接收到数据报之前会一直阻塞
                dodiscory do_  = new dodiscory(packet);
                Thread thread = new Thread(do_);
                thread.setPriority(Thread.MAX_PRIORITY);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void listenEcho(){
        String TAG = "控制器回复心跳包==";
        Log.d(TAG,"开始监听");
        while (true){
            DatagramPacket packet = null;
            try {
                byte[] data = new byte[10];
                packet = new DatagramPacket(data, data.length);
                echosocket.receive(packet);//此方法在接收到数据报之前会一直阻塞
                doecho do_  = new doecho(packet);
                Thread thread = new Thread(do_);
                thread.setPriority(Thread.MAX_PRIORITY);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void listenCtl(){
        String TAG = "控制器回复控制包==";
        Log.d(TAG,"开始监听");
        while (true){
            try {
                //判断是否来自控制器
                Socket socket = ctlsocket.accept();
                //获取包数据
                doctl do_  = new doctl(socket);
                Thread thread = new Thread(do_);
                thread.setPriority(Thread.MAX_PRIORITY);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void req(Type type, byte[] image,short w,short h){
       reqRunnable req = new reqRunnable(type,image,w,h);
       Thread send = new Thread(req);
       send.setPriority(Thread.MAX_PRIORITY);
       send.start();
    }
    public void sendDis(){
        Thread dis  = new Thread(new DisRunnable());
        dis.setPriority(Thread.MAX_PRIORITY);
        dis.start();
    }
    public void sendEcho(){
        Thread echo  = new Thread(new EchoRunnable());
        echo.setPriority(Thread.NORM_PRIORITY);
        echo.start();
    }

    class DisRunnable implements Runnable {

        @Override
        public void run() {
            try {

                DatagramPacket dataPacket = null;
                byte[] code = new byte[10];
                code[0] =(byte) 0xEA;
                code[1] =(byte) 0xAE;
                code[2] = (byte)0x03;
                code[3] = (byte)0x00;
                code[4] = (byte)0x00;
                code[5] = (byte)0x00;
                code[6] = (byte)0x0A;
                code[7] = (byte)0xEE;
                code[8] = (byte)0xEE;
                code[9] = (byte)0xEE;
                InetAddress address = InetAddress.getByName("255.255.255.255");
                dataPacket = new DatagramPacket(code, code.length, address,
                        Port.controller_discover.value());
                //广播数据包
                DatagramSocket ds = new DatagramSocket();
                ds.send(dataPacket);
                ds.close();
                Log.d("", "广播数据发现包"+new Date().toString());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class EchoRunnable implements Runnable {

        @Override
        public void run() {
            try {
                DatagramPacket dataPacket = null;
                byte[] code = new byte[10];
                code[0] =(byte) 0xEA;
                code[1] =(byte) 0xAE;
                code[2] = (byte)0x00;
                code[3] = (byte)0x00;
                code[4] = (byte)0x00;
                code[5] = (byte)0x00;
                code[6] = (byte)0x0A;
                code[7] = (byte)0xEE;
                code[8] = (byte)0xEE;
                code[9] = (byte)0xEE;
                InetAddress address = Terminer.getInstance().ctladdr;
                dataPacket = new DatagramPacket(code, code.length, address,
                        Port.terminal_echo.value());
                //广播数据包
                DatagramSocket ds = new DatagramSocket();
                ds.send(dataPacket);
                ds.close();
                Log.d("", "心跳数据包"+new Date().toString());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class reqRunnable implements Runnable{

        private byte[] image;
        private Type type;
        private short w;
        private  short h;
        public reqRunnable(Type type, byte[] image,short w,short h){
            this.image = image;
            this.type = type;
            this.w = w ;
            this.h = h;
        }
        @Override
        public void run() {
            String TAG = "控制器发送req包==";
            long now_time= System.currentTimeMillis();
            Socket socket = null;
            if(ctlonline  ) {
                Log.d(TAG,"控制器在线");
                Terminer.getInstance().setPre_req(System.currentTimeMillis());
                try {
                    socket = new Socket(Terminer.getInstance().ctladdr.getHostAddress(),Port.terminal_req.value());
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(data_head);
                    outputStream.write(0x02);
                    outputStream.write(image.length+16);
                    outputStream.write(type.value()== 0 ? 0x00:0x01);
                    outputStream.write(w);
                    outputStream.write(h);
                    outputStream.write(image);
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(socket!=null){
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }else {
                Log.d(TAG,"控制器不在线");
            }
        }
    }
}
