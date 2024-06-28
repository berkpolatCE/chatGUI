import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;

public class ChatGUI {
    private JFrame frame;
    private JTextArea messageArea;
    private JTextField inputField;
    private JButton sendButton;
    private String nickname;
    private ChatNetworking networking;

    public ChatGUI(String nickname) {
        this.nickname = nickname;
        createUI();
        try {
            networking = new ChatNetworking(nickname);
            new Thread(() -> {
                try {
                    networking.receiveMessages(this, 12345);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void createUI() {
        try {
            // Set Look and Feel to the system default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("Instant Messaging App - " + nickname);
        frame.setLayout(new BorderLayout());

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 14));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        inputField = new JTextField(25);
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            try {
                networking.sendMessage(message, "localhost", 12345);
                inputField.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void receiveMessage(String message) {
        messageArea.append(message + "\n");
    }

    public static void main(String[] args) {
        String nickname = JOptionPane.showInputDialog("Enter your nickname:");
        if (nickname != null && !nickname.trim().isEmpty()) {
            new ChatGUI(nickname);
        }
    }
}
