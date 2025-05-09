package ui;

import logic.PriorityScheduler;
import model.Process;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class SchedulerGUI extends JFrame {

  // GUI Components
  private JTextField numProcessesField; // Input field for number of processes
  private JPanel inputFieldsPanel; // Panel containing process input fields
  private JButton generateButton, startButton; // Control buttons
  private JTable resultsTable; // Table showing process metrics
  private DefaultTableModel tableModel; // Data model for results table
  private JLabel avgWTLabel, avgTATLabel, avgRTLabel; // Labels for average metrics
  private GanttChartPanel ganttChartPanel; // Custom panel for Gantt chart

  // Data structures
  private List<JTextField[]> inputFieldsList = new ArrayList<>(); // Stores input fields
  private PriorityScheduler scheduler = new PriorityScheduler(); // Scheduler instance

  // Constructor sets up the main window
  public SchedulerGUI() {
    setTitle("Priority Preemptive Scheduler");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout(10, 10));
    setSize(900, 800);  // Increased height to accommodate all panels

    // Create a main scroll pane
    JPanel mainContainer = new JPanel(new BorderLayout(10, 10));
    mainContainer.add(createTopInputPanel(), BorderLayout.NORTH);
    mainContainer.add(createMainPanel(), BorderLayout.CENTER);
    mainContainer.add(createBottomPanel(), BorderLayout.SOUTH);

    // Add the main container to a scroll pane
    JScrollPane mainScroll = new JScrollPane(mainContainer);
    mainScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    mainScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    add(mainScroll);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private JPanel createTopInputPanel() {
    // Create a container panel with BorderLayout
    JPanel containerPanel = new JPanel(new BorderLayout());
    containerPanel.setBorder(BorderFactory.createTitledBorder("Configuration"));

    // Create the content panel
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    // Add components as before
    panel.add(new JLabel("Number of Processes:"));
    numProcessesField = new JTextField(5);
    panel.add(numProcessesField);

    generateButton = new JButton("Generate Input Fields");
    generateButton.addActionListener(this::generateInputFields);
    panel.add(generateButton);

    startButton = new JButton("Start Simulation");
    JButton autoFillButton = new JButton("Auto Fill Sample Data");
    autoFillButton.addActionListener(this::autoFillSampleData);
    panel.add(autoFillButton);
    startButton.setEnabled(false);
    startButton.addActionListener(this::startSimulation);
    panel.add(startButton);

    // Modify scroll pane settings
    JScrollPane scrollPane = new JScrollPane(panel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setPreferredSize(new Dimension(850, 100));
    scrollPane.setBorder(BorderFactory.createTitledBorder("Configuration"));

    containerPanel.add(scrollPane, BorderLayout.CENTER);
    return containerPanel;
  }

  private JPanel createMainPanel() {
    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

    // Input Panel with enhanced scroll
    inputFieldsPanel = new JPanel();
    inputFieldsPanel.setLayout(new BoxLayout(inputFieldsPanel, BoxLayout.Y_AXIS));
    JScrollPane inputScrollPane = new JScrollPane(inputFieldsPanel);
    inputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    inputScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    inputScrollPane.setBorder(BorderFactory.createTitledBorder("Process Inputs"));
    inputScrollPane.setPreferredSize(new Dimension(850, 200));

    // Gantt Chart Panel with enhanced scroll
    ganttChartPanel = new GanttChartPanel();
    JScrollPane ganttScrollPane = new JScrollPane(ganttChartPanel);
    ganttScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    ganttScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    ganttScrollPane.setBorder(BorderFactory.createTitledBorder("Gantt Chart"));
    ganttScrollPane.setPreferredSize(new Dimension(850, 200));

    // Add panels with weight
    mainPanel.add(inputScrollPane, BorderLayout.NORTH);
    mainPanel.add(ganttScrollPane, BorderLayout.CENTER);
    return mainPanel;
  }

  private JPanel createBottomPanel() {
    JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));

    // Results Table with enhanced scroll
    tableModel = new DefaultTableModel(
        new Object[] { "ID", "Arrival", "Burst", "Priority", "Waiting", "Turnaround", "Response" }, 0);
    resultsTable = new JTable(tableModel);
    JScrollPane resultsScroll = new JScrollPane(resultsTable);
    resultsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    resultsScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    resultsScroll.setBorder(BorderFactory.createTitledBorder("Results Table"));
    resultsScroll.setPreferredSize(new Dimension(850, 150));

    // Averages Panel with enhanced scroll
    JPanel avgPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    avgWTLabel = new JLabel("Avg Waiting Time: ");
    avgTATLabel = new JLabel("Avg Turnaround Time: ");
    avgRTLabel = new JLabel("Avg Response Time: ");
    avgPanel.add(avgWTLabel);
    avgPanel.add(avgTATLabel);
    avgPanel.add(avgRTLabel);

    JScrollPane avgScrollPane = new JScrollPane(avgPanel);
    avgScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    avgScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    avgScrollPane.setPreferredSize(new Dimension(850, 60));

    bottomPanel.add(resultsScroll, BorderLayout.CENTER);
    bottomPanel.add(avgScrollPane, BorderLayout.SOUTH);
    return bottomPanel;
  }

  private void generateInputFields(ActionEvent e) {
    inputFieldsPanel.removeAll();
    inputFieldsList.clear();
    try {
      int num = Integer.parseInt(numProcessesField.getText());
      if (num <= 0)
        throw new NumberFormatException();

      for (int i = 0; i < num; i++) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("P" + (i + 1) + " → Arrival:"));
        JTextField arrival = new JTextField(3);
        row.add(arrival);
        row.add(new JLabel("Burst:"));
        JTextField burst = new JTextField(3);
        row.add(burst);
        row.add(new JLabel("Priority:"));
        JTextField priority = new JTextField(3);
        row.add(priority);
        inputFieldsList.add(new JTextField[] { arrival, burst, priority });
        inputFieldsPanel.add(row);
      }

      inputFieldsPanel.revalidate();
      inputFieldsPanel.repaint();
      startButton.setEnabled(true);
    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(this, "Please enter a valid positive integer.");
    }
  }

  private void startSimulation(ActionEvent e) {
    List<Process> processes = new ArrayList<>();
    boolean valid = true;

    for (int i = 0; i < inputFieldsList.size(); i++) {
      JTextField[] fields = inputFieldsList.get(i);
      try {
        int arrival = Integer.parseInt(fields[0].getText());
        int burst = Integer.parseInt(fields[1].getText());
        int priority = Integer.parseInt(fields[2].getText());

        if (arrival < 0 || burst <= 0)
          throw new NumberFormatException();

        processes.add(new Process("P" + (i + 1), arrival, burst, priority));
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid input at Process " + (i + 1));
        valid = false;
        break;
      }
    }

    if (valid) {
      List<Process> completed = scheduler.schedule(processes);
      ganttChartPanel.setExecutionTimeline(scheduler.getExecutionTimeline());

      tableModel.setRowCount(0);
      for (Process p : completed) {
        tableModel.addRow(new Object[] {
            p.getProcessId(), p.getArrivalTime(), p.getBurstTime(), p.getPriority(),
            p.getWaitingTime(), p.getTurnaroundTime(), p.getResponseTime()
        });
      }

      avgWTLabel.setText("Avg Waiting Time: " + String.format("%.2f", scheduler.getAverageWaitingTime(completed)));
      avgTATLabel
          .setText("Avg Turnaround Time: " + String.format("%.2f", scheduler.getAverageTurnaroundTime(completed)));
      avgRTLabel.setText("Avg Response Time: " + String.format("%.2f", scheduler.getAverageResponseTime(completed)));
    }
  }

  private void autoFillSampleData(ActionEvent e) {
    numProcessesField.setText("5");
    generateInputFields(null); // Trigger field generation

    // Define sample data: {arrival, burst, priority}
    int[][] data = {
        { 0, 3, 3 },
        { 1, 4, 2 },
        { 2, 6, 4 },
        { 3, 4, 6 },
        { 5, 2, 10 }
    };

    for (int i = 0; i < data.length; i++) {
      JTextField[] fields = inputFieldsList.get(i);
      fields[0].setText(String.valueOf(data[i][0])); // Arrival
      fields[1].setText(String.valueOf(data[i][1])); // Burst
      fields[2].setText(String.valueOf(data[i][2])); // Priority
    }

    JOptionPane.showMessageDialog(this, "Sample data auto-filled!");
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(SchedulerGUI::new);
  }
}