package logic;

import model.Process;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class PriorityScheduler {

  private List<Process> executionTimeline = new ArrayList<>(); // To help with Gantt chart

  public List<Process> schedule(List<Process> processes) {
    List<Process> completedProcesses = new ArrayList<>();
    PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getPriority));
    List<Process> arrivedProcesses = new ArrayList<>();

    int currentTime = 0;
    Process currentProcess = null;

    while (!arrivedProcesses.isEmpty() || !readyQueue.isEmpty() || currentProcess != null) {
      // Add arrived processes to the arrivedProcesses list
      for (Process process : processes) {
        if (process.getArrivalTime() == currentTime && !arrivedProcesses.contains(process)) {
          arrivedProcesses.add(process);
        }
      }

      // Add arrived processes to the ready queue
      readyQueue.addAll(arrivedProcesses);
      arrivedProcesses.clear();

      if (currentProcess != null && currentProcess.getRemainingBurstTime() == 0) {
        currentProcess.setFinishTime(currentTime);
        currentProcess.setTurnaroundTime(currentProcess.getFinishTime() - currentProcess.getArrivalTime());
        currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
        completedProcesses.add(currentProcess);
        currentProcess = null;
      }

      if (!readyQueue.isEmpty()) {
        Process nextProcess = readyQueue.poll();
        if (currentProcess == null || nextProcess.getPriority() < currentProcess.getPriority()) {
          if (currentProcess != null) {
            readyQueue.offer(currentProcess);
          }
          currentProcess = nextProcess;
          if (currentProcess.getStartTime() == -1) {
            currentProcess.setStartTime(currentTime);
            currentProcess.setResponseTime(currentProcess.getStartTime() - currentProcess.getArrivalTime());
          }
        } else if (currentProcess != null) {
          // Put the currently running process back (it wasn't preempted by a higher
          // priority)
          readyQueue.offer(nextProcess);
        }
      }

      if (currentProcess != null) {
        currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - 1);
        executionTimeline.add(new Process(currentProcess.getProcessId(), currentTime, 1, currentProcess.getPriority())); // Record
                                                                                                                         // execution
      }

      currentTime++;
    }

    return completedProcesses;
  }

  // Method to get the execution timeline for Gantt chart
  public List<Process> getExecutionTimeline() {
    return executionTimeline;
  }

  public double getAverageWaitingTime(List<Process> processes) {
    return processes.stream().mapToInt(Process::getWaitingTime).average().orElse(0);
  }

  public double getAverageTurnaroundTime(List<Process> processes) {
    return processes.stream().mapToInt(Process::getTurnaroundTime).average().orElse(0);
  }

  public double getAverageResponseTime(List<Process> processes) {
    return processes.stream().filter(p -> p.getResponseTime() != -1).mapToInt(Process::getResponseTime).average()
        .orElse(0);
  }
}