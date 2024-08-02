/*
 * A sudoku program for solving a sudoku grid
 * Banele Mdluli
 * 2024-07-28
 */
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuSolverGUI extends JFrame {
    private static final int N = 9;
    private JTextField[][] cells = new JTextField[N][N];
    private int[][] grid = new int[N][N];
    private JTextArea initialGridDisplay;

    public SudokuSolverGUI() {
        setTitle("Sudoku Solver");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridLayout(N, N)) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                int width = getWidth();
                int height = getHeight();
                int boxWidth = width / 3;
                int boxHeight = height / 3;

                g.setColor(Color.BLACK);
                for (int i = 0; i <= 3; i++) {
                    int x = i * boxWidth;
                    int y = i * boxHeight;
                    g.drawLine(x, 0, x, height);
                    g.drawLine(0, y, width, y);
                }
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                cells[row][col] = new JTextField();
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                cells[row][col].setFont(new Font("Arial", Font.BOLD, 20));
                Border border = BorderFactory.createMatteBorder(
                        row % 3 == 0 ? 2 : 1,
                        col % 3 == 0 ? 2 : 1,
                        row % 3 == 2 ? 2 : 1,
                        col % 3 == 2 ? 2 : 1,
                        Color.BLACK
                );
                cells[row][col].setBorder(border);
                panel.add(cells[row][col]);
            }
        }
        add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton solveButton = new JButton("Solve");
        solveButton.setFont(new Font("Arial", Font.PLAIN, 16));
        solveButton.addActionListener(new SolveButtonListener());
        buttonPanel.add(solveButton);

        JButton resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.PLAIN, 16));
        resetButton.addActionListener(new ResetButtonListener());
        buttonPanel.add(resetButton);

        add(buttonPanel, BorderLayout.SOUTH);

        initialGridDisplay = new JTextArea(10, 20);
        initialGridDisplay.setEditable(false);
        initialGridDisplay.setFont(new Font("Arial", Font.PLAIN, 14));
        initialGridDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JScrollPane scrollPane = new JScrollPane(initialGridDisplay);
        scrollPane.setPreferredSize(new Dimension(200, 600));
        add(scrollPane, BorderLayout.EAST);
    }

    private class SolveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (parseGrid()) {
                displayInitialGrid();
                if (solveSudoku(grid, 0, 0)) {
                    displaySolution();
                } else {
                    JOptionPane.showMessageDialog(SudokuSolverGUI.this, "No Solution exists");
                }
            } else {
                JOptionPane.showMessageDialog(SudokuSolverGUI.this, "Invalid input. Please enter numbers between 1 and 9.");
            }
        }
    }

    private class ResetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int row = 0; row < N; row++) {
                for (int col = 0; col < N; col++) {
                    cells[row][col].setText("");
                }
            }
            initialGridDisplay.setText("");
        }
    }

    private boolean parseGrid() {
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                String text = cells[row][col].getText();
                if (text.isEmpty()) {
                    grid[row][col] = 0;
                } else {
                    try {
                        int num = Integer.parseInt(text);
                        if (num < 1 || num > 9) {
                            return false;
                        }
                        grid[row][col] = num;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void displayInitialGrid() {
        StringBuilder sb = new StringBuilder();
        sb.append("Initial Sudoku Grid:\n");
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                sb.append(grid[row][col]).append(" ");
            }
            sb.append("\n");
        }
        initialGridDisplay.setText(sb.toString());
    }

    private void displaySolution() {
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                cells[row][col].setText(Integer.toString(grid[row][col]));
            }
        }
    }

    private boolean solveSudoku(int[][] grid, int row, int col) {
        if (row == N - 1 && col == N) {
            return true;
        }

        if (col == N) {
            row++;
            col = 0;
        }

        if (grid[row][col] != 0) {
            return solveSudoku(grid, row, col + 1);
        }

        for (int num = 1; num <= 9; num++) {
            if (isSafe(grid, row, col, num)) {
                grid[row][col] = num;
                if (solveSudoku(grid, row, col + 1)) {
                    return true;
                }
                grid[row][col] = 0;
            }
        }
        return false;
    }

    private boolean isSafe(int[][] grid, int row, int col, int num) {
        for (int x = 0; x < N; x++) {
            if (grid[row][x] == num) {
                return false;
            }
        }

        for (int x = 0; x < N; x++) {
            if (grid[x][col] == num) {
                return false;
            }
        }

        int startRow = row - row % 3, startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SudokuSolverGUI frame = new SudokuSolverGUI();
            frame.setVisible(true);
        });
    }
}
