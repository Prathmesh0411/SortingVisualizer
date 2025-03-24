import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Arrays;
import java.util.Random;

public class SortingVisualizer1 extends JPanel {
    private int[] array;
    private int delay = 50;
    private int barWidth = 10;
    private int comparisons = 0;
    private int swaps = 0;
    private String timeComplexity = "";
    private Thread sortThread;
    private boolean isSorted = false;

    public SortingVisualizer(int[] inputArray) {
        array = Arrays.copyOf(inputArray, inputArray.length);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        int barSpacing = 2;

        // Total width occupied by all bars
        int totalWidth = array.length * (barWidth + barSpacing) - barSpacing;

        // Centering bars
        int startX = (width - totalWidth) / 2;

        for (int i = 0; i < array.length; i++) {
            int barHeight = array[i] * 4; // Scaling bar height
            int x = startX + i * (barWidth + barSpacing);
            int y = height - barHeight - 70; // Adding space for stats

            // Draw bars
            g.setColor(Color.GREEN);
            g.fillRect(x, y, barWidth, barHeight);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, barWidth, barHeight);

            // Draw numbers on top of bars
            g.setColor(Color.BLACK);
            String value = String.valueOf(array[i]);
            int stringWidth = g.getFontMetrics().stringWidth(value);
            g.drawString(value, x + (barWidth - stringWidth) / 2, y - 5);
        }

        // Show stats and time complexity at the bottom
        g.setColor(Color.BLACK);
        g.drawString("Comparisons: " + comparisons, 10, height - 50);
        g.drawString("Swaps: " + swaps, 10, height - 35);
        g.drawString("Time Complexity: " + timeComplexity, 10, height - 20);
    }

    private void swap(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        swaps++;
        repaint();
        sleep();
    }

    private void sleep() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Bubble Sort
    public void bubbleSort() {
        updateTimeComplexity("O(n)", "O(n²)", "O(n²)");
        sortThread = new Thread(() -> {
            for (int i = 0; i < array.length - 1; i++) {
                for (int j = 0; j < array.length - i - 1; j++) {
                    comparisons++;
                    if (array[j] > array[j + 1]) {
                        swap(j, j + 1);
                    }
                }
            }
            isSorted = true;
        });
        sortThread.start();
    }

    // Selection Sort
    public void selectionSort() {
        updateTimeComplexity("O(n²)", "O(n²)", "O(n²)");
        sortThread = new Thread(() -> {
            for (int i = 0; i < array.length - 1; i++) {
                int minIdx = i;
                for (int j = i + 1; j < array.length; j++) {
                    comparisons++;
                    if (array[j] < array[minIdx]) {
                        minIdx = j;
                    }
                }
                swap(i, minIdx);
            }
            isSorted = true;
        });
        sortThread.start();
    }

    // Insertion Sort
    public void insertionSort() {
        updateTimeComplexity("O(n)", "O(n²)", "O(n²)");
        sortThread = new Thread(() -> {
            for (int i = 1; i < array.length; i++) {
                int key = array[i];
                int j = i - 1;
                while (j >= 0 && array[j] > key) {
                    comparisons++;
                    array[j + 1] = array[j];
                    j--;
                    repaint();
                    sleep();
                }
                array[j + 1] = key;
                repaint();
                sleep();
            }
            isSorted = true;
        });
        sortThread.start();
    }

    // Merge Sort
    public void mergeSort(int left, int right) {
        updateTimeComplexity("O(n log n)", "O(n log n)", "O(n log n)");
        sortThread = new Thread(() -> {
            mergeSortHelper(left, right);
            isSorted = true;
        });
        sortThread.start();
    }

    private void mergeSortHelper(int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortHelper(left, mid);
            mergeSortHelper(mid + 1, right);
            merge(left, mid, right);
        }
    }

    private void merge(int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;
        while (i <= mid && j <= right) {
            comparisons++;
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
            repaint();
            sleep();
        }
        while (i <= mid) temp[k++] = array[i++];
        while (j <= right) temp[k++] = array[j++];
        System.arraycopy(temp, 0, array, left, temp.length);
        repaint();
        sleep();
    }

    // Quick Sort
    public void quickSort(int low, int high) {
        updateTimeComplexity("O(n log n)", "O(n log n)", "O(n²)");
        sortThread = new Thread(() -> {
            quickSortHelper(low, high);
            isSorted = true;
        });
        sortThread.start();
    }

    private void quickSortHelper(int low, int high) {
        if (low < high) {
            int pi = partition(low, high);
            quickSortHelper(low, pi - 1);
            quickSortHelper(pi + 1, high);
        }
    }

    private int partition(int low, int high) {
        int pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            comparisons++;
            if (array[j] < pivot) {
                swap(++i, j);
            }
        }
        swap(i + 1, high);
        return i + 1;
    }

    // Shuffle Array
    public void shuffleArray() {
        Random rand = new Random();
        for (int i = 0; i < array.length; i++) {
            int j = rand.nextInt(array.length);
            swap(i, j);
        }
        isSorted = false;
        repaint();
    }

    // Reset Array
    public void resetArray(int[] inputArray) {
        array = Arrays.copyOf(inputArray, inputArray.length);
        comparisons = 0;
        swaps = 0;
        timeComplexity = "";
        isSorted = false;
        repaint();
    }

    // Method to update time complexity
    public void updateTimeComplexity(String best, String avg, String worst) {
        timeComplexity = String.format("Best: %s | Avg: %s | Worst: %s", best, avg, worst);
        repaint();
    }

    // Main GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sorting Visualizer");
            int[] userArray = getUserArray();
            SortingVisualizer sorter = new SortingVisualizer(userArray);
            frame.setLayout(new BorderLayout());
            frame.add(sorter, BorderLayout.CENTER);
            frame.setSize(900, 500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Top Control Panel
            JPanel topPanel = new JPanel();
            topPanel.setBackground(Color.DARK_GRAY);
            topPanel.setLayout(new GridLayout(1, 5, 5, 5));

            String[] algorithms = {"Bubble", "Selection", "Insertion", "Merge", "Quick"};
            for (String algo : algorithms) {
                JButton algoButton = new JButton(algo);
                algoButton.setForeground(Color.WHITE);
                algoButton.setBackground(Color.BLACK);
                algoButton.addActionListener(e -> runSort(algo, sorter));
                topPanel.add(algoButton);
            }
            frame.add(topPanel, BorderLayout.NORTH);

            // Bottom Control Panel
            JPanel bottomPanel = new JPanel();
            JButton shuffleButton = new JButton("Shuffle Array");
            JButton resetButton = new JButton("Reset");
            JLabel speedLabel = new JLabel("Speed: ");
            JSlider speedSlider = new JSlider(0, 100, 50);
            JLabel barWidthLabel = new JLabel("Bar Width: ");
            JSlider barWidthSlider = new JSlider(5, 50, 10);

            // Listeners
            speedSlider.addChangeListener(e -> sorter.delay = 100 - speedSlider.getValue());
            barWidthSlider.addChangeListener(e -> {
                sorter.barWidth = barWidthSlider.getValue();
                sorter.repaint();
            });
            shuffleButton.addActionListener(e -> sorter.shuffleArray());
            resetButton.addActionListener(e -> sorter.resetArray(userArray));

            bottomPanel.add(shuffleButton);
            bottomPanel.add(resetButton);
            bottomPanel.add(speedLabel);
            bottomPanel.add(speedSlider);
            bottomPanel.add(barWidthLabel);
            bottomPanel.add(barWidthSlider);

            frame.add(bottomPanel, BorderLayout.SOUTH);
            frame.setVisible(true);
        });
    }

    // Get user input through GUI
    public static int[] getUserArray() {
        String input = JOptionPane.showInputDialog(null, "Enter numbers to sort (comma separated):", "Array Input", JOptionPane.PLAIN_MESSAGE);
        if (input == null || input.isEmpty()) {
            return new int[]{8, 5, 3, 9, 4, 1, 7, 6, 2}; // Default array
        }
        String[] tokens = input.split(",");
        int[] userArray = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            userArray[i] = Integer.parseInt(tokens[i].trim());
        }
        return userArray;
    }

    // Run sorting based on selected algorithm
    public static void runSort(String algo, SortingVisualizer sorter) {
        if (sorter.isSorted) {
            JOptionPane.showMessageDialog(null, "Array is already sorted! Shuffle or reset to re-sort.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        switch (algo) {
            case "Bubble" -> sorter.bubbleSort();
            case "Selection" -> sorter.selectionSort();
            case "Insertion" -> sorter.insertionSort();
            case "Merge" -> sorter.mergeSort(0, sorter.array.length - 1);
            case "Quick" -> sorter.quickSort(0, sorter.array.length - 1);
        }
    }
}
