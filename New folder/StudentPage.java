import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentPage {
    private JFrame frame;
    private List<String[]> questions;
    private int currentIndex = 0;
    private JRadioButton[] optionButtons;
    private ButtonGroup group;
    private JButton nextButton;
    private List<String> selectedAnswers = new ArrayList<>();

    public StudentPage() {
        questions = loadQuestions();
        frame = new JFrame("Student Quiz");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        showQuestion();
        frame.setVisible(true);
    }

    private void showQuestion() {
        frame.getContentPane().removeAll();
        frame.repaint();
        frame.setLayout(null);

        if (currentIndex >= questions.size()) {
            saveResults();
            JOptionPane.showMessageDialog(frame, "Quiz Completed!");
            new LoginPage();
            frame.dispose();
            return;
        }

        String[] q = questions.get(currentIndex);

        JLabel questionLabel = new JLabel("Q" + (currentIndex + 1) + ": " + q[0]);
        questionLabel.setBounds(10, 10, 480, 25);
        frame.add(questionLabel);

        optionButtons = new JRadioButton[4];
        group = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton(q[i + 1]);
            optionButtons[i].setBounds(20, 40 + i * 30, 400, 25);
            group.add(optionButtons[i]);
            frame.add(optionButtons[i]);
        }

        nextButton = new JButton(currentIndex == questions.size() - 1 ? "Finish" : "Next");
        nextButton.setBounds(150, 180, 150, 30);
        frame.add(nextButton);

        nextButton.addActionListener(this::nextQuestion);
        frame.revalidate();
        frame.repaint();
    }

    private void nextQuestion(ActionEvent e) {
        for (int i = 0; i < 4; i++) {
            if (optionButtons[i].isSelected()) {
                selectedAnswers.add(String.valueOf(i + 1));
                break;
            }
        }
        if (selectedAnswers.size() <= currentIndex) {
            selectedAnswers.add("No Answer");
        }
        currentIndex++;
        showQuestion();
    }

    private List<String[]> loadQuestions() {
        List<String[]> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("questions.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] q = new String[6];
                q[0] = line;
                for (int i = 1; i <= 5; i++) {
                    q[i] = reader.readLine();
                }
                list.add(q);
                reader.readLine(); // skip -----
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "No questions found.");
        }
        return list;
    }

private void saveResults() {
    String name = JOptionPane.showInputDialog(frame, "Enter your name:");
    if (name == null || name.trim().isEmpty()) {
        name = "Unknown";
    }

    int score = 0;
    for (int i = 0; i < questions.size(); i++) {
        String[] q = questions.get(i);
        String correctAnswer = q[5]; // correct option number (e.g. "2")
        if (i < selectedAnswers.size() && selectedAnswers.get(i).equals(correctAnswer)) {
            score++;
        }
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt", true))) {
        writer.write(name + "\n");
        writer.write(score + "\n");
        writer.write("-----\n");
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
