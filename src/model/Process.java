package model;

public class Process {
  // Unique identifier for the process
  private String processId;
  // Time at which process arrives in the ready queue
  private int arrivalTime;
  // Total execution time required by the process
  private int burstTime;
  // Priority value (lower number means higher priority)
  private int priority;
  // Remaining execution time of the process
  private int remainingBurstTime;
  // Time when process first starts execution (-1 if not started)
  private int startTime = -1;
  // Time when process completes execution
  private int finishTime;
  // Total time spent waiting in ready queue
  private int waitingTime;
  // Total time from arrival to completion
  private int turnaroundTime;
  // Time between arrival and first execution (-1 if not started)
  private int responseTime = -1;

  // Constructor initializes basic process attributes
  public Process(String processId, int arrivalTime, int burstTime, int priority) {
    this.processId = processId;
    this.arrivalTime = arrivalTime;
    this.burstTime = burstTime;
    this.priority = priority;
    this.remainingBurstTime = burstTime;
  }

  // Getters and setters for all fields

  public String getProcessId() {
    return processId;
  }

  public int getArrivalTime() {
    return arrivalTime;
  }

  public int getBurstTime() {
    return burstTime;
  }

  public int getPriority() {
    return priority;
  }

  public int getRemainingBurstTime() {
    return remainingBurstTime;
  }

  public void setRemainingBurstTime(int remainingBurstTime) {
    this.remainingBurstTime = remainingBurstTime;
  }

  public int getStartTime() {
    return startTime;
  }

  public void setStartTime(int startTime) {
    this.startTime = startTime;
  }

  public int getFinishTime() {
    return finishTime;
  }

  public void setFinishTime(int finishTime) {
    this.finishTime = finishTime;
  }

  public int getWaitingTime() {
    return waitingTime;
  }

  public void setWaitingTime(int waitingTime) {
    this.waitingTime = waitingTime;
  }

  public int getTurnaroundTime() {
    return turnaroundTime;
  }

  public void setTurnaroundTime(int turnaroundTime) {
    this.turnaroundTime = turnaroundTime;
  }

  public int getResponseTime() {
    return responseTime;
  }

  public void setResponseTime(int responseTime) {
    this.responseTime = responseTime;
  }
}