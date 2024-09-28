
/**
 * pp
 */
import java.net.*;

public class pp {

  public static void main(String[] args) throws Exception {
    InetAddress address = InetAddress.getByName("127.0.0.1");
    DatagramSocket socket = new DatagramSocket(8088, address);
    byte[] str = new byte[100];
    DatagramPacket packet = new DatagramPacket(str, str.length);
    // str.substring(0, 100);
    while (true) {
      socket.receive(packet);
      System.out.println(new String(str));
    }
  }
}