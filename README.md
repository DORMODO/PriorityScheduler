# Priority Preemptive Scheduler in Java

This project implements a Priority Preemptive scheduling algorithm with a graphical user interface using Java Swing. The scheduler simulates process execution based on priority levels, where processes with higher priority (lower priority number) preempt processes with lower priority.

## Project Structure

The project is organized into several packages:

### 1. Model Package (`model`)
- **Process.java**: Represents a process with attributes like:
  - Process ID
  - Arrival Time
  - Burst Time
  - Priority
  - Various timing metrics (waiting time, turnaround time, etc.)

### 2. Logic Package (`logic`)
- **PriorityScheduler.java**: Implements the scheduling algorithm with features:
  - Preemptive priority scheduling
  - Process execution timeline tracking
  - Performance metrics calculation
  - Ready queue management

### 3. UI Package (`ui`)
- **SchedulerGUI.java**: Main application window containing:
  - Process input controls
  - Results display
  - Performance metrics
  - Simulation controls
- **GanttChartPanel.java**: Visual representation of process execution:
  - Timeline visualization
  - Color-coded process execution blocks
  - Time scale

## How the Scheduler Works

1. **Process Creation**
   - Users input process details (arrival time, burst time, priority)
   - Each process is assigned a unique ID

2. **Scheduling Algorithm**
   - Processes are scheduled based on priority
   - Lower priority numbers indicate higher priority
   - Running processes can be preempted by higher priority processes
   - The scheduler maintains a ready queue of arrived processes

3. **Metrics Calculation**
   - Waiting Time: Time spent in ready queue
   - Turnaround Time: Total time from arrival to completion
   - Response Time: Time between arrival and first execution

## Key Features

1. **Interactive GUI**
   - Dynamic process input generation
   - Auto-fill sample data option
   - Real-time Gantt chart visualization
   - Results table with performance metrics

2. **Visual Feedback**
   - Color-coded Gantt chart
   - Timeline markers
   - Process execution visualization

3. **Performance Metrics**
   - Individual process metrics
   - Average performance calculations
   - Complete execution timeline

## Usage Instructions

1. Enter the number of processes
2. Click "Generate Input Fields" or "Auto Fill Sample Data"
3. Input process details:
   - Arrival Time: When process enters system
   - Burst Time: Required execution time
   - Priority: Scheduling priority (lower = higher priority)
4. Click "Start Simulation" to run the scheduler
5. View results in the Gantt chart and metrics table

## Technical Implementation

- Built using Java Swing for GUI
- Object-oriented design with clear separation of concerns
- Real-time visualization of process execution
- Dynamic resource allocation and management
- Efficient algorithm implementation for process scheduling

## Performance Considerations

- Efficient ready queue management
- Optimal process switching decisions
- Accurate timing calculations
- Real-time display updates
- Memory-efficient process tracking

Would you like me to continue with detailed comments for the other code files as well?