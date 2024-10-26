
import org.json.JSONObject;
import java.io.*;
import json.Manager;
import java.net.*;

// 接收用户消息，保存到log.json文件中
class receiveMessage extends Thread {
    @Override
    public void run() {
        try {
            // 缓冲区
            byte[] buf = new byte[10000];

            // UDP相关的类
            DatagramSocket UDPsocket = new DatagramSocket(8888);
            DatagramPacket UDPpacket = new DatagramPacket(buf, buf.length);

            JSONObject json;

            // 自定义类，详细请参考/log/Manager.java
            Manager manager = new Manager();

            while (true) {
                // 接收数据
                UDPsocket.receive(UDPpacket);

                // 读数据
                json = new JSONObject(buf.toString());

                // 打印数据
                System.out.println(json.toString(4));

                // 检查log.json
                manager.check("log.json");

                // 读本地json
                manager.read("log.json");

                // 合并
                manager.merge(json);

                // 写数据
                manager.write("log.json");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}

public class PP_Server {
    public static void main(String args[]) throws Exception {
        receiveMessage myReceiveM = new receiveMessage();
        myReceiveM.start();
    }
}
