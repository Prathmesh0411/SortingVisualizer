import java.awt.*;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.*;

public class SortingVisualizer1 extends JPanel {
    private int[] array;
    private int delay = 50;

    public SortingVisualizer1(int[] inputArray) {
        array = Arrays.copyOf(inputArray, inputArray.length);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        for (int i = 0; i < array.length; i++) {
            int barWidth = getWidth() / array.length;
            int barHeight = array[i];
            int x = i * barWidth;
            int y = getHeight() - barHeight;
            g.fillRect(x, y, barWidth - 2, barHeight);
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(array[i]), x + (barWidth / 4), y - 5);
            g.setColor(Color.BLUE);
        }
    }

    private void swap(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
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
        new Thread(() -> {
            for (int i = 0; i < array.length - 1; i++) {
                for (int j = 0; j < array.length - i - 1; j++) {
                    if (array[j] > array[j + 1]) {
                        swap(j, j + 1);
                    }
                }
            }
        }).start();
    }

    public void selectionSort() {
        new Thread(() -> {
            for (int i = 0; i < array.length - 1; i++) {
                int minIdx = i;
                for (int j = i + 1; j < array.length; j++) {
                    if (array[j] < array[minIdx]) {
                        minIdx = j;
                    }
                }
                swap(i, minIdx);
            }
        }).start();
    }

    public void insertionSort() {
        new Thread(() -> {
            for (int i = 1; i < array.length; i++) {
                int key = array[i];
                int j = i - 1;
                while (j >= 0 && array[j] > key) {
                    array[j + 1] = array[j];
                    j--;
                    repaint();
                    sleep();
                }
                array[j + 1] = key;
                repaint();
                sleep();
            }
        }).start();
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
            if (array[j] < pivot) {
                swap(++i, j);
            }
        }
        swap(i + 1, high);
        return i + 1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter numbers to sort (comma separated): ");
        String input = scanner.nextLine();
        String[] tokens = input.split(",");
        int[] userArray = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            userArray[i] = Integer.parseInt(tokens[i].trim());
        }

        JFrame frame = new JFrame("Sorting Visualizer");
        SortingVisualizer1 sorter = new SortingVisualizer1(userArray);
        frame.add(sorter);
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        String[] options = {"Bubble Sort", "Selection Sort", "Insertion Sort", "Merge Sort", "Quick Sort"};
        int choice = JOptionPane.showOptionDialog(null, "Choose Sorting Algorithm", "Sorting Visualizer",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0: sorter.bubbleSort(); break;
            case 1: sorter.selectionSort(); break;
            case 2: sorter.insertionSort(); break;
            case 3: sorter.mergeSort(0, sorter.array.length - 1); break;
            case 4: sorter.quickSort(0, sorter.array.length - 1); break;
            default: System.exit(0);
        }

        scanner.close();
    }
}
