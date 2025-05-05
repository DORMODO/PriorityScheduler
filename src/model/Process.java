package model;

public class Process {
  private String processId;
  private int arrivalTime;
  private int burstTime;
  private int priority;
  private int remainingBurstTime;
  private int startTime = -1;
  private int finishTime;
  private int waitingTime;
  private int turnaroundTime;
  private int responseTime = -1;

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