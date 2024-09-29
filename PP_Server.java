
import java.net.*;

public class PP_Server {

    public static void main(String[] args) {
        try {
            // 创建Socket类
            DatagramSocket socket = new DatagramSocket(8888);

            // 创建缓冲区
            byte[] buf = new byte[1024];

            // 创建UDP数据包类
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            // 接收UDP数据包，并存入packet中
            socket.receive(packet);

            // 输出接收到的数据
            System.out.println(new String(packet.getData(), 0, packet.getLength()));

            // 关闭Socket
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}