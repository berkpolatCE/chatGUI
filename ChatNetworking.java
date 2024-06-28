import java.net.*;

public class ChatNetworking {
    private DatagramSocket socket;
    private String nickname;

    public ChatNetworking(String nickname) throws SocketException {
        this.nickname = nickname;
        socket = new DatagramSocket();
    }

    public void sendMessage(String message, String address, int port) throws Exception {
        String fullMessage = nickname + ": " + message;
        byte[] buffer = fullMessage.getBytes();
        InetAddress recipientAddress = InetAddress.getByName(address);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, recipientAddress, port);
        socket.send(packet);
    }

    public void receiveMessages(ChatGUI chatGUI, int port) throws Exception {
        DatagramSocket receiveSocket = new DatagramSocket(port);
        byte[] buffer = new byte[1024];
        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            receiveSocket.receive(packet);
            String message = new String(packet.getData(), 0, packet.getLength());
            chatGUI.receiveMessage(message);
        }
    }
}
