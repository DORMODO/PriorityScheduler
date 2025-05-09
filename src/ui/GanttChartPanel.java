package ui;

// Import required classes for process model, swing components, and graphics
import model.Process;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

// Custom JPanel class for drawing Gantt chart
public class GanttChartPanel extends JPanel {
    // Store the timeline of process executions
    private List<Process> executionTimeline;

    // Map to store unique colors for each process
    private Map<String, Color> processColors = new HashMap<>();

    // Constants for chart display dimensions
    private static final int BAR_HEIGHT = 30; // Height of each process bar
    private static final int Y_OFFSET = 50; // Vertical spacing from top

    // Method to update the timeline data and trigger redraw
    public void setExecutionTimeline(List<Process> timeline) {
        this.executionTimeline = timeline; // Store new timeline
        assignColors(); // Generate colors for processes
        repaint(); // Request panel redraw
    }

    // Assigns unique colors to each process
    private void assignColors() {
        Random random = new Random();
        for (Process p : executionTimeline) {
            String id = p.getProcessId();
            // Only assign color if process doesn't have one
            if (!processColors.containsKey(id)) {
                // Create random pastel color (max 200 to avoid too bright colors)
                processColors.put(id, new Color(
                        random.nextInt(200), // Red component
                        random.nextInt(200), // Green component
                        random.nextInt(200) // Blue component
                ));
            }
        }
    }

    // Override paintComponent to draw the Gantt chart
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Call parent's paint method

        // Only draw if there's data to display
        if (executionTimeline != null && !executionTimeline.isEmpty()) {
            // Calculate time range
            int startTime = executionTimeline.get(0).getArrivalTime();
            int endTime = executionTimeline.get(executionTimeline.size() - 1).getArrivalTime() + 1;

            // Calculate scaling factor for drawing
            double scale = (double) (getWidth() - 100) / (endTime - startTime);

            // Draw time axis
            g.setColor(Color.BLACK);
            g.drawString("Time (ms)", 50, 20); // Time axis label

            // Draw time markers and labels
            for (int i = startTime; i <= endTime; i++) {
                int x = 50 + (int) ((i - startTime) * scale);
                g.drawLine(x, 30, x, 35); // Draw tick mark
                if (i % 5 == 0) { // Draw label every 5 units
                    g.drawString(String.valueOf(i), x - 5, 45);
                }
            }

            // Draw process execution bars
            int y = Y_OFFSET;
            int barY = y;
            for (int i = 0; i < executionTimeline.size(); i++) {
                Process p = executionTimeline.get(i);
                String pid = p.getProcessId();

                // Get process color or use gray if none assigned
                Color color = processColors.get(pid);
                if (color == null)
                    color = Color.GRAY;

                // Calculate bar position and width
                int x = 50 + (int) ((p.getArrivalTime() - startTime) * scale);
                int width = (int) (1 * scale);

                // Draw filled bar with border
                g.setColor(color);
                g.fillRect(x, barY, width, BAR_HEIGHT);
                g.setColor(Color.BLACK);
                g.drawRect(x, barY, width, BAR_HEIGHT);

                // Draw process ID in the bar
                g.drawString(pid, x + width / 4, barY + BAR_HEIGHT / 2 + 5);
            }
        } else {
            // Display message if no data available
            g.drawString("No execution timeline to display.", 50, 50);
        }
    }
}