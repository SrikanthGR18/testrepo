import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class TeacherPage {
    private JFrame frame;
    private JTextField questionField, option1Field, option2Field, option3Field, option4Field;
    private JComboBox<String> correctOptionComboBox;

    public TeacherPage() {
        frame = new JFrame("Teacher Page");
        frame.setSize(500, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel(null);
        frame.add(panel);

        JLabel qLabel = new JLabel("Question:");
        qLabel.setBounds(10, 10, 100, 25);
        panel.add(qLabel);

        questionField = new JTextField();
        questionField.setBounds(100, 10, 350, 25);
        panel.add(questionField);

        option1Field = new JTextField();
        option1Field.setBounds(100, 40, 350, 25);
        JLabel o1 = new JLabel("Option 1:");
        o1.setBounds(10, 40, 100, 25);
        panel.add(o1);
        panel.add(option1Field);

        option2Field = new JTextField();
        option2Field.setBounds(100, 70, 350, 25);
        JLabel o2 = new JLabel("Option 2:");
        o2.setBounds(10, 70, 100, 25);
        panel.add(o2);
        panel.add(option2Field);

        option3Field = new JTextField();
        option3Field.setBounds(100, 100, 350, 25);
        JLabel o3 = new JLabel("Option 3:");
        o3.setBounds(10, 100, 100, 25);
        panel.add(o3);
        panel.add(option3Field);

        option4Field = new JTextField();
        option4Field.setBounds(100, 130, 350, 25);
        JLabel o4 = new JLabel("Option 4:");
        o4.setBounds(10, 130, 100, 25);
        panel.add(o4);
        panel.add(option4Field);

        correctOptionComboBox = new JComboBox<>(new String[]{"1", "2", "3", "4"});
        correctOptionComboBox.setBounds(100, 160, 100, 25);
        JLabel correctLabel = new JLabel("Correct Option:");
        correctLabel.setBounds(10, 160, 100, 25);
        panel.add(correctLabel);
        panel.add(correctOptionComboBox);

        JButton submitButton = new JButton("Add Question");
        submitButton.setBounds(100, 200, 150, 25);
        panel.add(submitButton);

        JButton viewResultsButton = new JButton("View Results");
        viewResultsButton.setBounds(100, 230, 150, 25);
        panel.add(viewResultsButton);

        JButton clearResultsButton = new JButton("Clear Results");
        clearResultsButton.setBounds(100, 260, 150, 25);
        panel.add(clearResultsButton);

        JButton deleteQuestionsButton = new JButton("Delete All Questions");
        deleteQuestionsButton.setBounds(100, 290, 180, 25);
        panel.add(deleteQuestionsButton);

        JButton backButton = new JButton("Back to Login");
        backButton.setBounds(100, 320, 150, 25);
        panel.add(backButton);

        submitButton.addActionListener(e -> saveQuestion());
        viewResultsButton.addActionListener(e -> viewResults());
        clearResultsButton.addActionListener(e -> clearResults());
        deleteQuestionsButton.addActionListener(e -> deleteAllQuestions());
        backButton.addActionListener(e -> {
            frame.dispose();
            new LoginPage();
        });

        frame.setVisible(true);
    }

    private void saveQuestion() {
        String question = questionField.getText().trim();
        String op1 = option1Field.getText().trim();
        String op2 = option2Field.getText().trim();
        String op3 = option3Field.getText().trim();
        String op4 = option4Field.getText().trim();
        String correct = (String) correctOptionComboBox.getSelectedItem();

        if (question.isEmpty() || op1.isEmpty() || op2.isEmpty() || op3.isEmpty() || op4.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields before adding a question.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("questions.txt", true))) {
            writer.write(question + "\n");
            writer.write(op1 + "\n");
            writer.write(op2 + "\n");
            writer.write(op3 + "\n");
            writer.write(op4 + "\n");
            writer.write(correct + "\n");
            writer.write("-----\n");
            JOptionPane.showMessageDialog(frame, "Question added successfully!");
            clearFields();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void clearFields() {
        questionField.setText("");
        option1Field.setText("");
        option2Field.setText("");
        option3Field.setText("");
        option4Field.setText("");
        correctOptionComboBox.setSelectedIndex(0);
    }

private void viewResults() {
    int totalQuestions = countTotalQuestions();
    if (totalQuestions == 0) {
        JOptionPane.showMessageDialog(frame, "No questions found. Cannot calculate scores.");
        return;
    }

    File file = new File("results.txt");
    if (!file.exists()) {
        JOptionPane.showMessageDialog(frame, "No results available.");
        return;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        StringBuilder resultText = new StringBuilder();
        String name;

        while ((name = reader.readLine()) != null) {
            String scoreLine = reader.readLine();
            String separator = reader.readLine();

            // Validate scoreLine and format properly
            if (scoreLine == null || separator == null) {
                resultText.append("Invalid entry detected, skipping...\n\n");
                continue;
            }

            try {
                int score = Integer.parseInt(scoreLine.trim());
                resultText.append("Student: ").append(name).append("\n")
                          .append("Score: ").append(score).append("/").append(totalQuestions).append("\n\n");
            } catch (NumberFormatException e) {
                resultText.append("Student: ").append(name).append("\n")
                          .append("Score: [Invalid Format]\n\n");
            }
        }

        if (resultText.length() == 0) {
            resultText.append("No valid results found.");
        }

        JTextArea area = new JTextArea(resultText.toString());
        area.setEditable(false);
        JOptionPane.showMessageDialog(frame, new JScrollPane(area), "Student Results", JOptionPane.INFORMATION_MESSAGE);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(frame, "Error reading results: " + e.getMessage());
    }
}


    private int countTotalQuestions() {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("questions.txt"))) {
            while (reader.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            return 0;
        }
        return count / 7; // each question uses 7 lines
    }

    private void clearResults() {
        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to clear all student results?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (PrintWriter pw = new PrintWriter("results.txt")) {
                pw.print("");
                JOptionPane.showMessageDialog(frame, "All results cleared.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error clearing results: " + e.getMessage());
            }
        }
    }

    private void deleteAllQuestions() {
        int confirm = JOptionPane.showConfirmDialog(frame, "Delete all questions? This cannot be undone.", "Warning", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (PrintWriter pw = new PrintWriter("questions.txt")) {
                pw.print("");
                JOptionPane.showMessageDialog(frame, "All questions deleted.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error deleting questions: " + e.getMessage());
            }
        }
    }
}
