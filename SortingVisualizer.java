import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class SortingVisualizer extends JPanel {
    private int[] arr;
    private int[] originalArray;
    private static final int BAR_WIDTH = 50;
    private static final int MAX_BAR_HEIGHT = 300;

    private int currentStep = 0;
    private boolean isSorting = false;
    private int algorithmIndex = 0; // To track which algorithm is running
    private JLabel explanationLabel;

    // Constructor to initialize the JPanel and sorting
    public SortingVisualizer(JLabel explanationLabel) {
        this.explanationLabel = explanationLabel;
        setPreferredSize(new Dimension(400, MAX_BAR_HEIGHT + 60));
        this.arr = new int[]{5, 2, 9, 1, 5, 6};  // Default array
        this.originalArray = arr.clone();  // Store the original unsorted array
    }

    // Paint method to draw bars representing the array elements
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < arr.length; i++) {
            int barHeight = arr[i] * 30;  // Scale the height for visibility
            g.setColor(Color.BLUE);
            g.fillRect(i * (BAR_WIDTH + 5), MAX_BAR_HEIGHT - barHeight, BAR_WIDTH, barHeight);

            // Display the value of each bar
            g.setColor(Color.BLACK); // Set color to black for the text
            g.setFont(new Font("Arial", Font.BOLD, 14));
            String value = String.valueOf(arr[i]);
            g.drawString(value, i * (BAR_WIDTH + 5) + BAR_WIDTH / 4, MAX_BAR_HEIGHT - barHeight - 5);
        }

        // Draw current step information
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Step: " + (currentStep + 1), 10, MAX_BAR_HEIGHT + 20);
    }

    // Sort step for selected algorithm
    private void sortStep() {
        switch (algorithmIndex) {
            case 0:
                bubbleSortStep();
                break;
            case 1:
                selectionSortStep();
                break;
            case 2:
                insertionSortStep();
                break;
        }
    }

    // Perform one step of Bubble Sort
    private void bubbleSortStep() {
        boolean swapped = false;
        for (int i = 0; i < arr.length - 1 - currentStep; i++) {
            if (arr[i] > arr[i + 1]) {
                // Swap elements
                int temp = arr[i];
                arr[i] = arr[i + 1];
                arr[i + 1] = temp;
                swapped = true;
            }
        }
        if (!swapped) {
            explanationLabel.setText("Bubble Sort completed! Complexity: O(n^2) Time, O(1) Space.");
        } else {
            explanationLabel.setText("Bubble Sort: Comparing and swapping adjacent elements.");
        }
    }

    // Perform one step of Selection Sort
    private void selectionSortStep() {
        int minIdx = currentStep;
        for (int i = currentStep + 1; i < arr.length; i++) {
            if (arr[i] < arr[minIdx]) {
                minIdx = i;
            }
        }
        if (minIdx != currentStep) {
            int temp = arr[currentStep];
            arr[currentStep] = arr[minIdx];
            arr[minIdx] = temp;
            explanationLabel.setText("Selection Sort: Swapping " + arr[currentStep] + " with " + arr[minIdx]);
        } else {
            explanationLabel.setText("Selection Sort: No swap needed, moving to next element.");
        }

        if (currentStep < arr.length - 1) {
            explanationLabel.setText("Selection Sort: Finding minimum element in the remaining unsorted portion.");
        } else {
            explanationLabel.setText("Selection Sort completed! Complexity: O(n^2) Time, O(1) Space.");
        }
    }

    // Perform one step of Insertion Sort
    private void insertionSortStep() {
        int key = arr[currentStep];
        int j = currentStep - 1;
        while (j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j];
            j--;
        }
        arr[j + 1] = key;

        if (currentStep < arr.length - 1) {
            explanationLabel.setText("Insertion Sort: Inserting " + key + " into the sorted portion.");
        } else {
            explanationLabel.setText("Insertion Sort completed! Complexity: O(n^2) Time, O(1) Space.");
        }
    }

    // Method to start sorting based on selected algorithm
    public void startSorting(int selectedAlgorithm) {
        this.algorithmIndex = selectedAlgorithm;
        this.currentStep = 0;
        this.isSorting = true;
        explanationLabel.setText("Sorting in progress...");
        resetArray(); // Reset the array to its unsorted form
        repaint();
    }

    // Method to reset the array to its original unsorted state
    private void resetArray() {
        arr = originalArray.clone();
    }

    // Method to move one step forward in the sorting
    public void nextStep() {
        if (isSorting) {
            sortStep();
            currentStep++;
            repaint();
        }
    }

    // Method to move one step backward in the sorting
    public void previousStep() {
        if (currentStep > 0) {
            currentStep--;
            resetArray();
            sortStep();
            repaint();
        }
    }

    // Method to update the array based on user input
    public void updateArray(String input) {
        try {
            String[] inputStrings = input.split(",");
            arr = Arrays.stream(inputStrings)
                    .map(String::trim)
                    .mapToInt(Integer::parseInt)
                    .toArray();
            originalArray = arr.clone(); // Reset original array
            currentStep = 0; // Reset step counter
            repaint();
            explanationLabel.setText("Array updated. Select a sorting algorithm to start.");
        } catch (NumberFormatException e) {
            explanationLabel.setText("Invalid input. Please enter numbers separated by commas.");
        }
    }

    // Main method to launch the app
    public static void main(String[] args) {
        JFrame frame = new JFrame("Sorting Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a label for explanation
        JLabel explanationLabel = new JLabel("Select a sorting algorithm to start.");
        explanationLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // Input field for custom array
        JTextField arrayInputField = new JTextField(20);
        arrayInputField.setText("5, 2, 9, 1, 5, 6");  // Default array text

        // Button to update the array
        JButton updateArrayButton = new JButton("Update Array");
        updateArrayButton.addActionListener(e -> {
            SortingVisualizer visualizer = (SortingVisualizer) frame.getContentPane().getComponent(1);
            String input = arrayInputField.getText();
            visualizer.updateArray(input);
        });

        // Dropdown for selecting sorting algorithm
        String[] algorithms = {"Bubble Sort", "Selection Sort", "Insertion Sort"};
        JComboBox<String> algorithmComboBox = new JComboBox<>(algorithms);
        algorithmComboBox.addActionListener(e -> {
            String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
            SortingVisualizer visualizer = (SortingVisualizer) frame.getContentPane().getComponent(1);
            int algorithmIndex = algorithmComboBox.getSelectedIndex();
            visualizer.startSorting(algorithmIndex);
        });

        // Buttons to control sorting
        JButton nextButton = new JButton("Next Step");
        nextButton.addActionListener(e -> {
            SortingVisualizer visualizer = (SortingVisualizer) frame.getContentPane().getComponent(1);
            visualizer.nextStep();
        });

        JButton prevButton = new JButton("Previous Step");
        prevButton.addActionListener(e -> {
            SortingVisualizer visualizer = (SortingVisualizer) frame.getContentPane().getComponent(1);
            visualizer.previousStep();
        });

        // Panel to hold the components
        JPanel panel = new JPanel();
        panel.add(explanationLabel);
        panel.add(arrayInputField);
        panel.add(updateArrayButton);
        panel.add(algorithmComboBox);
        panel.add(nextButton);
        panel.add(prevButton);

        SortingVisualizer visualizer = new SortingVisualizer(explanationLabel);
        panel.add(visualizer);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(visualizer, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
