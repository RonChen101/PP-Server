import org.json.JSONObject;
import java.io.*;
import log.Manager;
import java.net.*;

// 接收用户消息，保存到log.json文件中
class receiveMessage extends Thread {
    @Override
    public void run() {
        try {
            // 用户名、时间、内容
            String userName, time, content;

            // 缓冲区
            byte[] buf = new byte[10000];

            // UDP相关的类
            DatagramSocket UDPsocket = new DatagramSocket(8888);
            DatagramPacket UDPpacket = new DatagramPacket(buf, buf.length);

            // 文件操作相关的类
            File log = new File("./resources/log.json");
            FileInputStream readLog;
            FileOutputStream writeLog;

            // Json相关的类
            JSONObject inputJson;
            JSONObject logJson;

            // 自定义类，详细请参考/log/Manager.java
            Manager myManager;

            while (true) {
                // 检查log.json
                if (!log.exists()) {
                    log.createNewFile();
                }
                // 接收数据
                UDPsocket.receive(UDPpacket);

                // 读数据
                inputJson = new JSONObject(new String(buf));
                userName = inputJson.getString("userName");
                time = inputJson.getString("time");
                content = inputJson.getString("content");

                // 读本地Json
                readLog = new FileInputStream("./resources/log.json");
                readLog.read(buf);
                // 判断是否为空
                writeLog = new FileOutputStream("./resources/log.json");
                if (buf[0] == 0) {
                    writeLog.write("{\"data\":[]}".getBytes());
                    readLog.read(buf);
                }
                readLog.close();

                logJson = new JSONObject(new String(buf));

                // 编辑本地Json
                myManager = new Manager(userName);
                myManager.add(logJson, time, content);

                // 写数据
                writeLog.write(logJson.toString(4).getBytes());
                writeLog.close();
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
