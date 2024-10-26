import org.json.JSONArray;
import org.json.JSONObject;

import java.net.*;
import java.text.DateFormat;
import java.util.Date;

import json.Manager;

// 接收用户消息，保存到log.json文件中
class receiveMessage extends Thread {
    @Override
    public void run() {
        // 缓冲区
        byte[] buf = new byte[10000];
        JSONObject bufJson;

        // 自定义类，详细请参考/log/Manager.java
        Manager manager = new Manager();

        try {
            // UDP相关的类
            DatagramSocket UDPsocket = new DatagramSocket(8888);
            DatagramPacket UDPpacket = new DatagramPacket(buf, buf.length);

            while (true) {
                // 接收数据
                UDPsocket.receive(UDPpacket);

                // 读数据
                bufJson = new JSONObject(new String(buf));

                // 检查log.json
                manager.check("log.json");

                // 读本地json
                manager.read("log.json");

                // 合并
                manager.merge(bufJson);

                // 写数据
                manager.write("log.json");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}

// 接收用户更新请求，发送新数据
class receiveUpdate extends Thread {
    @Override
    public void run() {
        // 缓冲区
        byte[] buf = new byte[10000];
        JSONObject bufJson;

        // 自定义类，详细请参考/log/Manager.java
        Manager manager = new Manager();

        try {
            // UDP相关的类
            DatagramSocket UDPsocket = new DatagramSocket(6666);
            DatagramPacket UDPpacket = new DatagramPacket(buf, buf.length);
            InetAddress inetAddress;
            int port;

            while (true) {
                // 接收数据
                UDPsocket.receive(UDPpacket);
                inetAddress = UDPpacket.getAddress();
                port = UDPpacket.getPort();

                // 读数据
                bufJson = new JSONObject(new String(buf));

                // 检查log.json
                manager.check("log.json");

                // 读log.json
                manager.read("log.json");

                // 打印数据
                System.out.println(bufJson.toString(4));

                // 取补集
                manager.takingTheComplement(bufJson);

                // 创建数据包
                UDPpacket = new DatagramPacket(
                        manager.json.toString().getBytes(),
                        manager.json.toString().getBytes().length,
                        inetAddress,
                        port);

                // 打印数据
                // System.out.println("数据");
                // System.out.println(manager.json.toString(4));
                // System.out.println("\n");

                // 发送数据
                UDPsocket.send(UDPpacket);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}

public class PP_Server {
    public static void main(String args[]) throws Exception {
        receiveMessage myReceiveMessage = new receiveMessage();
        receiveUpdate myReceiveUpdate = new receiveUpdate();
        myReceiveMessage.start();
        myReceiveUpdate.start();
    }
}
