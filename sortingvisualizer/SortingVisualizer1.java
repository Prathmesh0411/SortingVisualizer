import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;

public class SortingVisualizer extends JPanel {
    private int[] array;
    private int delay = 50;
    private int comparisons = 0;
    private int swaps = 0;
    private int barWidth = 10;
    private Thread sortThread;

    public SortingVisualizer(int[] inputArray) {
        array = Arrays.copyOf(inputArray, inputArray.length);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        int barSpacing = 2;

        for (int i = 0; i < array.length; i++) {
            int barHeight = array[i] * 4; // Scaling for visualization
            int x = i * (barWidth + barSpacing);
            int y = height - barHeight;

            g.setColor(Color.GREEN);
            g.fillRect(x, y, barWidth, barHeight);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, barWidth, barHeight);
        }
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

    public void bubbleSort() {
        sortThread = new Thread(() -> {
            for (int i = 0; i < array.length - 1; i++) {
                for (int j = 0; j < array.length - i - 1; j++) {
                    comparisons++;
                    if (array[j] > array[j + 1]) {
                        swap(j, j + 1);
                    }
                }
            }
        });
        sortThread.start();
    }

    public void selectionSort() {
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
        });
        sortThread.start();
    }

    public void insertionSort() {
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
        });
        sortThread.start();
    }

    public void mergeSort(int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(left, mid);
            mergeSort(mid + 1, right);
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

    public void quickSort(int low, int high) {
        if (low < high) {
            int pi = partition(low, high);
            quickSort(low, pi - 1);
            quickSort(pi + 1, high);
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

    public void heapSort() {
        int n = array.length;
        for (int i = n / 2 - 1; i >= 0; i--) heapify(n, i);
        for (int i = n - 1; i > 0; i--) {
            swap(0, i);
            heapify(i, 0);
        }
    }

    private void heapify(int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && array[left] > array[largest]) largest = left;
        if (right < n && array[right] > array[largest]) largest = right;

        if (largest != i) {
            swap(i, largest);
            heapify(n, largest);
        }
    }

    public void shuffleArray() {
        Random rand = new Random();
        for (int i = 0; i < array.length; i++) {
            int j = rand.nextInt(array.length);
            swap(i, j);
        }
        repaint();
    }

    public void resetArray(int[] inputArray) {
        array = Arrays.copyOf(inputArray, inputArray.length);
        comparisons = 0;
        swaps = 0;
        repaint();
    }

    // GUI Setup
    public static void main(String[] args) {
        JFrame frame = new JFrame("Sorting Visualizer");
        int[] defaultArray = generateRandomArray(30, 100);
        SortingVisualizer sorter = new SortingVisualizer(defaultArray);

        // Top panel with sorting buttons
        JPanel topPanel = new JPanel();
        String[] algorithms = {"Bubble", "Selection", "Insertion", "Merge", "Quick", "Heap"};
        for (String algo : algorithms) {
            JButton algoButton = new JButton(algo);
            algoButton.addActionListener(e -> runAlgorithm(sorter, algo));
            topPanel.add(algoButton);
        }

        // Control panel with sliders, buttons, and array input
        JPanel controlPanel = new JPanel();
        JButton shuffleButton = new JButton("Shuffle");
        JButton resetButton = new JButton("Reset");
        JTextField arrayInput = new JTextField(20);
        JButton setArrayButton = new JButton("Set Array");

        JSlider speedSlider = new JSlider(1, 100, 50);
        JSlider sizeSlider = new JSlider(5, 100, 30);

        controlPanel.add(shuffleButton);
        controlPanel.add(resetButton);
        controlPanel.add(new JLabel("Speed:"));
        controlPanel.add(speedSlider);
        controlPanel.add(new JLabel("Size:"));
        controlPanel.add(sizeSlider);
        controlPanel.add(arrayInput);
        controlPanel.add(setArrayButton);

        // Event listeners
        shuffleButton.addActionListener(e -> sorter.shuffleArray());
        resetButton.addActionListener(e -> sorter.resetArray(defaultArray));
        setArrayButton.addActionListener(e -> {
            String input = arrayInput.getText();
            String[] tokens = input.split(",");
            int[] userArray = new int[tokens.length];
            try {
                for (int i = 0; i < tokens.length; i++) {
                    userArray[i] = Integer.parseInt(tokens[i].trim());
                }
                sorter.resetArray(userArray);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter numbers separated by commas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        speedSlider.addChangeListener(e -> sorter.delay = 101 - speedSlider.getValue());
        sizeSlider.addChangeListener(e -> {
            int size = sizeSlider.getValue();
            sorter.resetArray(generateRandomArray(size, 100));
        });

        frame.setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(sorter, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void runAlgorithm(SortingVisualizer sorter, String algo) {
        switch (algo) {
            case "Bubble": sorter.bubbleSort(); break;
            case "Selection": sorter.selectionSort(); break;
            case "Insertion": sorter.insertionSort(); break;
            case "Merge": sorter.mergeSort(0, sorter.array.length - 1); break;
            case "Quick": sorter.quickSort(0, sorter.array.length - 1); break;
            case "Heap": sorter.heapSort(); break;
        }
    }

    private static int[] generateRandomArray(int size, int maxValue) {
        Random rand = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextInt(maxValue) + 1;
        }
        return array;
    }
}
