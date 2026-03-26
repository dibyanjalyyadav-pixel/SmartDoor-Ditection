import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SmartDoorGUI{

    static Process process;
    static String adminEmail = "";

    public static void main(String[] args) {

        JFrame frame = new JFrame("Smart Door Login");
        frame.setSize(400,300);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel nameLabel = new JLabel("Admin Name:");
        nameLabel.setBounds(40,30,120,30);
        frame.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(170,30,160,30);
        frame.add(nameField);

        JLabel emailLabel = new JLabel("Admin Email:");
        emailLabel.setBounds(40,80,120,30);
        frame.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(170,80,160,30);
        frame.add(emailField);

        JButton loginBtn = new JButton("Login & Start Camera");
        loginBtn.setBounds(90,150,200,40);
        frame.add(loginBtn);

        loginBtn.addActionListener(e -> {

            adminEmail = emailField.getText();

            try {
                process = Runtime.getRuntime().exec(
                    "C:\\Users\\Admin\\source\\repos\\OpenCV_Test\\x64\\Release\\OpenCV_Test.exe"
                );
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            java.util.Timer timer = new java.util.Timer();
            timer.schedule(new TimerTask() {
                public void run() {

                    File alertFile = new File("C:\\visitor\\alert.txt");

                    if (alertFile.exists()) {

                        sendMail(adminEmail);

                        JOptionPane.showMessageDialog(frame,
                                "Email sent: Someone's standing at the door");

                        alertFile.delete();
                    }
                }
            }, 0, 4000);
        });

        frame.setVisible(true);
    }

    public static void sendMail(String toEmail) {

        final String fromEmail = "divs062007@gmail.com";
        final String password = "xahi glzc dyjs sgus";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));

            message.setSubject("Smart Door Alert");
            message.setText("Someone's standing at the door");

            Transport.send(message);

            System.out.println("Email Sent Successfully");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}