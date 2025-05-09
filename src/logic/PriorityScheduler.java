package logic;

import model.Process;
import java.util.ArrayList;
import java.util.List;

public class PriorityScheduler {
  // Stores the complete execution history of processes
  private List<Process> executionTimeline;

  // Constructor initializes empty execution timeline
  public PriorityScheduler() {
    executionTimeline = new ArrayList<>();
  }

  // Main scheduling algorithm
  public List<Process> schedule(List<Process> processes) {
    List<Process> completedProcesses = new ArrayList<>(); // Stores finished processes
    List<Process> readyQueue = new ArrayList<>(); // Stores processes ready to execute
    int currentTime = 0; // Current simulation time
    Process currentProcess = null; // Currently executing process

    // Main scheduling loop
    while (hasUnfinishedProcesses(processes, completedProcesses)) {
      // Step 1: Check for newly arrived processes
      for (Process p : processes) {
        if (p.getArrivalTime() == currentTime && !readyQueue.contains(p)
            && !completedProcesses.contains(p)) {
          readyQueue.add(p);
        }
      }

      // Step 2: Handle completed processes
      if (currentProcess != null && currentProcess.getRemainingBurstTime() == 0) {
        currentProcess.setFinishTime(currentTime);
        currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
        currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
        completedProcesses.add(currentProcess);
        currentProcess = null;
      }

      // Step 3: Process scheduling and preemption
      if (!readyQueue.isEmpty()) {
        Process highestPriorityProcess = findHighestPriority(readyQueue);

        // Preempt current process if necessary
        if (currentProcess == null ||
            highestPriorityProcess.getPriority() < currentProcess.getPriority()) {

          if (currentProcess != null) {
            readyQueue.add(currentProcess);
          }

          currentProcess = highestPriorityProcess;
          readyQueue.remove(highestPriorityProcess);

          // Set initial metrics for first execution
          if (currentProcess.getStartTime() == -1) {
            currentProcess.setStartTime(currentTime);
            currentProcess.setResponseTime(currentTime - currentProcess.getArrivalTime());
          }
        }
      }

      // Step 4: Execute current process for one time unit
      if (currentProcess != null) {
        currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - 1);
        executionTimeline.add(new Process(
            currentProcess.getProcessId(),
            currentTime,
            1,
            currentProcess.getPriority()));
      }

      currentTime++; // Advance simulation time
    }

    return completedProcesses;
  }

  // Simple helper method to find highest priority process
  private Process findHighestPriority(List<Process> readyQueue) {
    Process highest = readyQueue.get(0);
    for (Process p : readyQueue) {
      if (p.getPriority() < highest.getPriority()) {
        highest = p;
      }
    }
    return highest;
  }

  // Check if there are still processes to complete
  private boolean hasUnfinishedProcesses(List<Process> all, List<Process> completed) {
    return completed.size() < all.size();
  }

  // Get average metrics
  public double getAverageWaitingTime(List<Process> processes) {
    double total = 0;
    for (Process p : processes) {
      total += p.getWaitingTime();
    }
    return total / processes.size();
  }

  public double getAverageTurnaroundTime(List<Process> processes) {
    double total = 0;
    for (Process p : processes) {
      total += p.getTurnaroundTime();
    }
    return total / processes.size();
  }

  public double getAverageResponseTime(List<Process> processes) {
    double total = 0;
    int count = 0;
    for (Process p : processes) {
      if (p.getResponseTime() != -1) {
        total += p.getResponseTime();
        count++;
      }
    }
    return count > 0 ? total / count : 0;
  }

  public List<Process> getExecutionTimeline() {
    return executionTimeline;
  }
}