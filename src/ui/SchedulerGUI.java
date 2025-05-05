package ui;

import logic.PriorityScheduler;
import model.Process;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SchedulerGUI extends JFrame implements ActionListener {

  private JTextField numProcessesField;
  private JPanel processesPanel;
  private JButton addButton;
  private JButton startButton;
  private GanttChartPanel ganttChartPanel;
  private JTable resultsTable;
  private DefaultTableModel resultsTableModel;
  private JLabel avgWaitingTimeLabel;
  private JLabel avgTurnaroundTimeLabel;
  private JLabel avgResponseTimeLabel;

  private List<JTextField[]> processInputFields = new ArrayList<>();
  private PriorityScheduler scheduler = new PriorityScheduler();

  private JScrollPane processesScrollPane; // Declare it here as an instance variable

  public SchedulerGUI() {
    setTitle("Priority Preemptive Scheduler");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new FlowLayout()); // TEMPORARY LAYOUT CHANGE
    setPreferredSize(new Dimension(800, 600));

    // Initialize processesPanel first
    processesPanel = new JPanel();
    processesPanel.setLayout(new BoxLayout(processesPanel, BoxLayout.Y_AXIS));

    // Initialize processesScrollPane, passing in processesPanel
    processesScrollPane = new JScrollPane(processesPanel);

    // Initialize inputPanel
    JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    inputPanel.add(new JLabel("Number of Processes:"));
    numProcessesField = new JTextField(5);
    inputPanel.add(numProcessesField);
    addButton = new JButton("Add Processes");
    addButton.addActionListener(this);
    inputPanel.add(addButton);

    // Initialize the other panels
    JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    startButton = new JButton("Start Simulation");
    startButton.addActionListener(this);
    startButton.setEnabled(false);
    controlPanel.add(startButton);

    ganttChartPanel = new GanttChartPanel();
    ganttChartPanel.setPreferredSize(new Dimension(780, 200));

    JPanel resultsPanel = new JPanel(new BorderLayout());
    resultsTableModel = new DefaultTableModel(new Object[] { "Process ID", "Arrival Time", "Burst Time", "Priority",
        "Waiting Time", "Turnaround Time", "Response Time" }, 0);
    resultsTable = new JTable(resultsTableModel);
    JScrollPane resultsScrollPane = new JScrollPane(resultsTable);
    resultsPanel.add(new JLabel("Simulation Results:"), BorderLayout.NORTH);
    resultsPanel.add(resultsScrollPane, BorderLayout.CENTER);

    JPanel avgPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    avgWaitingTimeLabel = new JLabel("Avg Waiting Time: ");
    avgPanel.add(avgWaitingTimeLabel);
    avgTurnaroundTimeLabel = new JLabel("Avg Turnaround Time: ");
    avgPanel.add(avgTurnaroundTimeLabel);
    avgResponseTimeLabel = new JLabel("Avg Response Time: ");
    avgPanel.add(avgResponseTimeLabel);
    resultsPanel.add(avgPanel, BorderLayout.SOUTH);

    // Add panels in the correct order
    add(inputPanel); // Add without BorderLayout constraints
    add(processesScrollPane); // Add without BorderLayout constraints
    add(controlPanel); // Add without BorderLayout constraints
    add(ganttChartPanel); // Add without BorderLayout constraints
    add(resultsPanel); // Add without BorderLayout constraints

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    System.out.println("Action performed: " + e.getActionCommand());

    if (e.getSource() == addButton) {
      System.out.println("Add Processes button clicked.");
      try {
        int numProcesses = Integer.parseInt(numProcessesField.getText());
        System.out.println("Number of processes entered: " + numProcesses);
        if (numProcesses > 0) {
          System.out.println("Number of processes is valid. Proceeding to add fields.");
          processesPanel.removeAll();
          processInputFields.clear();
          System.out.println("Previous process input fields cleared.");
          for (int i = 0; i < numProcesses; i++) {
            JPanel processPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            processPanel.setBorder(new LineBorder(Color.GREEN, 2)); // Add a visible border TEMPORARILY
            processPanel.add(new JLabel("Process " + (i + 1) + ":"));
            JTextField arrivalField = new JTextField(5);
            JTextField burstField = new JTextField(5);
            JTextField priorityField = new JTextField(5);
            processPanel.add(new JLabel("Arrival:"));
            processPanel.add(arrivalField);
            processPanel.add(new JLabel("Burst:"));
            processPanel.add(burstField);
            processPanel.add(new JLabel("Priority:"));
            processPanel.add(priorityField);
            processesPanel.add(processPanel);
            processInputFields.add(new JTextField[] { arrivalField, burstField, priorityField });
            System.out.println("Added input fields for Process " + (i + 1) + " with a green border.");
          }
          startButton.setEnabled(true);
          System.out.println("Start button enabled.");

          // Force visibility and layout update
          processesPanel.setVisible(true);
          processesPanel.revalidate();
          processesPanel.repaint();

          processesScrollPane.setVisible(true);
          processesScrollPane.revalidate();
          processesScrollPane.repaint();

          revalidate();
          repaint();
          System.out.println("Processes panel, scroll pane, and frame visibility forced, revalidated, and repainted.");
        } else {
          JOptionPane.showMessageDialog(this, "Number of processes must be greater than 0.", "Input Error",
              JOptionPane.ERROR_MESSAGE);
          System.out.println("Error: Number of processes not greater than 0.");
        }
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid number of processes.", "Input Error", JOptionPane.ERROR_MESSAGE);
        System.out.println("Error: NumberFormatException - Invalid number of processes.");
      }
    } else if (e.getSource() == startButton) {
      List<Process> processes = new ArrayList<>();
      boolean validInput = true;
      for (int i = 0; i < processInputFields.size(); i++) {
        JTextField[] fields = processInputFields.get(i);
        try {
          int arrivalTime = Integer.parseInt(fields[0].getText());
          int burstTime = Integer.parseInt(fields[1].getText());
          int priority = Integer.parseInt(fields[2].getText());
          if (arrivalTime < 0 || burstTime <= 0) {
            validInput = false;
            JOptionPane.showMessageDialog(this,
                "Arrival time cannot be negative, and burst time must be positive for Process " + (i + 1) + ".",
                "Input Error", JOptionPane.ERROR_MESSAGE);
            break;
          }
          processes.add(new Process("P" + (i + 1), arrivalTime, burstTime, priority));
        } catch (NumberFormatException ex) {
          validInput = false;
          JOptionPane.showMessageDialog(this, "Invalid input for Process " + (i + 1) + ".", "Input Error",
              JOptionPane.ERROR_MESSAGE);
          break;
        }
      }

      if (validInput && !processes.isEmpty()) {
        List<Process> completedProcesses = scheduler.schedule(processes);
        ganttChartPanel.setExecutionTimeline(scheduler.getExecutionTimeline());
        resultsTableModel.setRowCount(0); // Clear previous results
        for (Process p : completedProcesses) {
          resultsTableModel.addRow(new Object[] { p.getProcessId(), p.getArrivalTime(), p.getBurstTime(),
              p.getPriority(), p.getWaitingTime(), p.getTurnaroundTime(), p.getResponseTime() });
        }
        avgWaitingTimeLabel
            .setText("Avg Waiting Time: " + String.format("%.2f", scheduler.getAverageWaitingTime(completedProcesses)));
        avgTurnaroundTimeLabel.setText(
            "Avg Turnaround Time: " + String.format("%.2f", scheduler.getAverageTurnaroundTime(completedProcesses)));
        avgResponseTimeLabel.setText(
            "Avg Response Time: " + String.format("%.2f", scheduler.getAverageResponseTime(completedProcesses)));
      }
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(SchedulerGUI::new);
  }
}