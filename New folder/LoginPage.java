import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginPage {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        frame = new JFrame("Quiz Application Login");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        frame.add(panel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(100, 20, 165, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 50, 165, 25);
        panel.add(passwordField);

        JButton teacherLoginButton = new JButton("Teacher Login");
        teacherLoginButton.setBounds(10, 90, 130, 25);
        panel.add(teacherLoginButton);

        JButton studentLoginButton = new JButton("Student Login");
        studentLoginButton.setBounds(150, 90, 130, 25);
        panel.add(studentLoginButton);

        teacherLoginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (username.equals("teacher") && password.equals("password123")) {
                frame.dispose();
                new TeacherPage();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid teacher login.");
            }
        });

studentLoginButton.addActionListener(e -> {
    if (hasAtLeastOneQuestion()) {
        frame.dispose();
        new StudentPage();
    } else {
        JOptionPane.showMessageDialog(frame, "No questions available. Please ask the teacher to add questions first.");
    }
});

        frame.setVisible(true);
    }
private boolean hasAtLeastOneQuestion() {
    try (BufferedReader reader = new BufferedReader(new FileReader("questions.txt"))) {
        int lineCount = 0;
        while (reader.readLine() != null) {
            lineCount++;
        }
        return lineCount >= 7; // One complete question requires 7 lines
    } catch (Exception e) {
        return false;
    }
}

}

