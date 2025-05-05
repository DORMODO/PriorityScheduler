package ui;

import model.Process;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class GanttChartPanel extends JPanel {
    private List<Process> executionTimeline;
    private Map<String, Color> processColors = new HashMap<>();
    private static final int BAR_HEIGHT = 30;
    private static final int Y_OFFSET = 50;

    public void setExecutionTimeline(List<Process> timeline) {
        this.executionTimeline = timeline;
        assignColors();
        repaint();
    }

    private void assignColors() {
        if (executionTimeline != null) {
            Random random = new Random();
            for (Process process : executionTimeline) {
                if (!processColors.containsKey(process.getProcessId())) {
                    processColors.put(process.getProcessId(), new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (executionTimeline != null && !executionTimeline.isEmpty()) {
            int startTime = executionTimeline.get(0).getArrivalTime();
            int endTime = executionTimeline.stream().mapToInt(p -> p.getArrivalTime() + p.getBurstTime()).max().orElse(0);
            double scale = (double) (getWidth() - 100) / (endTime - startTime);

            g.drawString("Time", 50, 20);
            for (int i = startTime; i <= endTime; i++) {
                g.setColor(Color.BLACK);
                g.drawLine(50 + (int) ((i - startTime) * scale), 30, 50 + (int) ((i - startTime) * scale), 35);
                if (i % 5 == 0) {
                    g.drawString(String.valueOf(i), 40 + (int) ((i - startTime) * scale), 45);
                }
            }

            int y = Y_OFFSET;
            Map<String, List<Process>> processesByName = executionTimeline.stream()
                    .collect(Collectors.groupingBy(Process::getProcessId));

            for (Map.Entry<String, List<Process>> entry : processesByName.entrySet()) {
                String processId = entry.getKey();
                Color color = processColors.get(processId);
                g.setColor(color);
                for (Process segment : entry.getValue()) {
                    int x = 50 + (int) ((segment.getArrivalTime() - startTime) * scale);
                    int width = (int) (segment.getBurstTime() * scale);
                    g.fillRect(x, y, width, BAR_HEIGHT);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, width, BAR_HEIGHT);
                    g.drawString(processId, x + width / 2 - 10, y + BAR_HEIGHT / 2 + 5);
                }
                y += BAR_HEIGHT + 20;
            }
        } else {
            g.drawString("No execution timeline to display.", 50, 50);
        }
    }
}