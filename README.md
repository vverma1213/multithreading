I'll create a comprehensive README file with all multithreading concepts and ASCII diagrams. Here's the complete content you can copy into your `README.md` file:

```markdown
# Java Multithreading Concepts - Complete Guide

A comprehensive repository demonstrating key multithreading concepts in Java with practical examples, detailed explanations, and visual diagrams. This guide covers everything from basic synchronization to advanced Fork/Join frameworks.

## Table of Contents

1. [Introduction](#introduction)
2. [Core Concepts](#core-concepts)
   - [Synchronization](#synchronization)
   - [Volatile Keyword](#volatile-keyword)
   - [Atomic Variables](#atomic-variables)
   - [Locks (ReentrantLock)](#locks-reentrantlock)
   - [Thread Join](#thread-join)
3. [Concurrency Tools](#concurrency-tools)
   - [Thread Pools (Executor Framework)](#thread-pools-executor-framework)
   - [Blocking Queues](#blocking-queues)
   - [Concurrent Collections](#concurrent-collections)
4. [Synchronization Aids](#synchronization-aids)
   - [CountDownLatch](#countdownlatch)
   - [CyclicBarrier](#cyclicbarrier)
   - [Exchanger](#exchanger)
   - [Semaphores](#semaphores)
5. [Advanced Patterns](#advanced-patterns)
   - [Fork/Join Framework](#forkjoin-framework)
   - [Deadlock](#deadlock)
   - [Livelock](#livelock)
   - [Dining Philosophers Problem](#dining-philosophers-problem)
6. [Parallel Computing](#parallel-computing)
7. [Stream API Examples](#stream-api-examples)
8. [Quick Start](#quick-start)
9. [Project Structure](#project-structure)
10. [Resources](#resources)

---

## Introduction

Multithreading allows a program to execute multiple tasks concurrently, improving performance and responsiveness. Java provides powerful tools in the `java.util.concurrent` package to manage threads safely and efficiently.

### Key Principles:
- **Thread Safety**: Prevent race conditions when accessing shared resources
- **Concurrency vs Parallelism**: Concurrency manages multiple tasks (even on single core); Parallelism runs on multiple cores
- **Visibility**: Ensure changes made by one thread are visible to others
- **Atomicity**: Operations complete without interruption

---

## Core Concepts

### Synchronization

**Purpose**: Control access to shared resources, preventing race conditions.

**How it works**: Uses `synchronized` keyword or locks to ensure only one thread accesses a resource at a time.

**When to use**: When multiple threads modify shared data.

**Diagram - Race Condition vs Synchronized**:
```
WITHOUT SYNCHRONIZATION (Race Condition):
┌─────────────────┐        ┌─────────────────┐
│   Thread 1      │        │   Thread 2      │
│ Read: 0         │        │ Read: 0         │
│ +1              │        │ +1              │
│ Write: 1        │        │ Write: 1        │
└─────────────────┘        └─────────────────┘
↓ Counter = 1 (WRONG! Should be 2)

WITH SYNCHRONIZATION:
┌─────────────────────────────────────┐
│   Thread 1 (Lock Acquired)          │
│ Read: 0 → +1 → Write: 1            │
│ Release Lock                        │
└─────────────────────────────────────┘
↓
┌─────────────────────────────────────┐
│   Thread 2 (Lock Acquired)          │
│ Read: 1 → +1 → Write: 2            │
│ Release Lock                        │
└─────────────────────────────────────┘
↓ Counter = 2 (CORRECT!)
```

**Code Location**: `src/main/java/org/example/synchronization/SynchronizationExample.java`

**Example**:
```java
public synchronized void increment() {
    counter++;  // Only one thread at a time
}
```

---

### Volatile Keyword

**Purpose**: Ensure visibility of changes across threads immediately.

**How it works**: Forces variables to be read/written to main memory instead of thread-local cache.

**When to use**: Flags that signal thread state changes, especially simple boolean values.

**Diagram - Visibility Problem**:
```
WITHOUT VOLATILE:
┌──────────────┐                    ┌──────────────┐
│  Thread A    │                    │  Thread B    │
│ L1/L2 Cache: │                    │ L1/L2 Cache: │
│ flag=false   │                    │ flag=false   │
└──────────────┘                    └──────────────┘
       │                                   │
       │ Updates flag                      │ Reads flag
       ↓                                   ↓
    [Main Memory: flag=true]
  (Update may be delayed or not visible to Thread B)

WITH VOLATILE:
┌──────────────┐                    ┌──────────────┐
│  Thread A    │                    │  Thread B    │
│ Updates flag │──────→ [Main Memory] ←── Reads flag
│ (force sync) │       flag=true      (force sync)
└──────────────┘                    └──────────────┘
  (Immediate visibility guaranteed)
```

**Code Location**: `src/main/java/org/example/volatileexample/VolatileExample.java`

**Example**:
```java
private volatile boolean isTerminated = false;

public void run() {
    while (!isTerminated) {  // Always reads latest value
        // do work
    }
}
```

---

### Atomic Variables

**Purpose**: Provide thread-safe operations without explicit locking.

**How it works**: Uses low-level CPU atomic instructions for read-modify-write operations.

**When to use**: Simple operations like increment, decrement on counters.

**Advantages**: Faster than synchronization for simple cases.

**Diagram - Atomic Operations**:
```
TRADITIONAL SYNCHRONIZED:
Read → Modify → Write (3 steps, lock entire time)

ATOMIC OPERATION:
┌─────────────────────────────┐
│ Read-Modify-Write (ATOMIC)  │  ← Cannot be interrupted
│ Hardware ensures atomicity  │
└─────────────────────────────┘

MULTIPLE THREADS WITH ATOMICINTEGER:
Thread 1: getAndIncrement() → [Atomic operation]
Thread 2: getAndIncrement() → [Atomic operation] (Concurrent, no blocking)
Result: Correct and faster!
```

**Code Location**: `src/main/java/org/example/atomic/AtomicIntegerExample.java`

**Example**:
```java
private static AtomicInteger counter = new AtomicInteger(0);

public static void increment() {
    counter.getAndIncrement();  // Atomic increment
}
```

---

### Locks (ReentrantLock)

**Purpose**: Explicit locking with more control than `synchronized`.

**How it works**: Provides lock/unlock methods with features like fairness, timeouts, and conditional waits.

**When to use**: Need for fine-grained control, timeouts, or fairness.

**Advantages**:
- `tryLock()` with timeout
- Fairness option
- Better for multiple conditions
- Readable code

**Diagram - Lock vs Synchronized**:
```
SYNCHRONIZED:
lock acquired    ┌──────────────────┐
                 │  Critical Code   │
lock released    └──────────────────┘

REENTRANTLOCK (Same Thread Can Re-acquire):
Thread A acquires lock (count=1)
┌─────────────────┐
│ Critical Section│
│ Acquires again  │ (count=2) ← Can happen!
└─────────────────┘
Release (count=1)
Release (count=0) → Other threads proceed

TRYLOCK WITH TIMEOUT:
                 Thread A
                    │
          ┌─────────┴─────────┐
          │                   │
      Lock free?          Lock busy?
          │                   │
       ✓ PROCEED          ⏱ WAIT
          │              (timeout)
          │                   │
          └─────────┬─────────┘
                 Continue
```

**Code Location**: `src/main/java/org/example/lock/LockExample.java`

**Example**:
```java
private ReentrantLock lock = new ReentrantLock(true);  // true = fair

public void incrementCounter1() {
    lock.lock();
    try {
        counter1++;
    } finally {
        lock.unlock();
    }
}
```

---

### Thread Join

**Purpose**: Make one thread wait for another to complete before proceeding.

**How it works**: Calling `thread.join()` blocks until the specified thread finishes.

**When to use**: Ensuring sequential execution or waiting for completion before proceeding.

**Diagram - Thread Execution Order**:
```
WITHOUT JOIN:
Main
  ├─→ Thread 1 starts
  ├─→ Thread 2 starts
  └─→ Continues (doesn't wait)
      ├─ Thread 1 running...
      └─ Thread 2 running...

WITH JOIN:
Main
  ├─→ Thread 1 starts
  │    └─→ Thread 1 completes ✓
  │
  ├─→ Thread 2 starts
  │    └─→ Thread 2 completes ✓
  │
  └─→ Continues (only after both done)

DEPENDENCY CHAIN (Thread 3 waits for Thread 2, which waits for Thread 1):
┌───────────────┐
│  Thread 1     │ ─→ complete
└───────────────┘
        ↓ join()
┌───────────────┐
│  Thread 2     │ ─→ complete
└───────────────┘
        ↓ join()
┌───────────────┐
│  Thread 3     │ ─→ complete
└───────────────┘
        ↓
    Main continues
```

**Code Location**: `src/main/java/org/example/join/JoinExample.java`

**Example**:
```java
Thread t1 = new Thread(() -> System.out.println("Thread 1"));
Thread t2 = new Thread(() -> System.out.println("Thread 2"));

t1.start();
t2.start();

t1.join();  // Wait for t1 to finish
t2.join();  // Wait for t2 to finish

System.out.println("All threads completed");
```

---

## Concurrency Tools

### Thread Pools (Executor Framework)

**Purpose**: Manage a pool of reusable threads to execute tasks efficiently.

**How it works**: Submits tasks to a queue; worker threads pick up and execute them.

**Advantages**:
- Reduces overhead of creating/destroying threads
- Automatic load balancing
- Queue management built-in

**Types**:
- `FixedThreadPool(n)`: Fixed n threads
- `CachedThreadPool()`: Creates threads as needed, reuses idle ones
- `SingleThreadExecutor()`: Single thread, sequential execution
- `ScheduledThreadPool(n)`: Schedule tasks with delay/period

**Diagram - Thread Pool Workflow**:
```
TASK SUBMISSION:
┌─────────┐  ┌─────────┐  ┌─────────┐
│ Task 1  │  │ Task 2  │  │ Task 3  │
└────┬────┘  └────┬────┘  └────┬────┘
     │            │            │
     └────────────┼────────────┘
                  ↓
          ┌───────────────┐
          │  Task Queue   │
          │ (Unbounded)   │
          └────┬──────────┘
               │
    ┌──────────┼──────────┐
    │          │          │
┌───▼───┐ ┌───▼───┐ ┌───▼───┐
│Worker1│ │Worker2│ │Worker3│
│ Busy  │ │ Idle  │ │ Idle  │
└───────┘ └───────┘ └───────┘
    │
    ↓
  EXECUTE

FIXED THREAD POOL (5 threads):
┌──────────────────────────────────────┐
│         Thread Pool (5)              │
│  ┌─────┬─────┬─────┬─────┬─────┐    │
│  │ W1  │ W2  │ W3  │ W4  │ W5  │    │
│  │Busy │Busy │Idle │Idle │Busy │    │
│  └─────┴─────┴─────┴─────┴─────┘    │
│           ↓                          │
│    ┌──────────────┐                  │
│    │  Task Queue  │                  │
│    │ Task6        │                  │
│    │ Task7        │                  │
│    └──────────────┘                  │
└──────────────────────────────────────┘
```

**Code Location**: `src/main/java/org/example/threadpool/ThreadPoolExample.java`

**Example**:
```java
ExecutorService executor = Executors.newFixedThreadPool(5);

for (int i = 0; i < 10; i++) {
    executor.execute(() -> System.out.println("Task executing"));
}

executor.shutdown();
```

---

### Blocking Queues

**Purpose**: Thread-safe queues that block when full (put) or empty (take).

**How it works**: Put/take operations block until condition is met.

**When to use**: Producer-consumer patterns.

**Diagram - Producer-Consumer with BlockingQueue**:
```
BLOCKED PUT (Queue Full):
Producer
  │
  ├─→ put(item) → Queue FULL! → BLOCKED
  │                             (Wait for space)
  │                                  ↓
  └─ Consumer takes item → Space available
                                  │
                                  ↓
                            Producer UNBLOCKED
                            put completes ✓

BLOCKED TAKE (Queue Empty):
Consumer
  │
  ├─→ take() → Queue EMPTY! → BLOCKED
  │                           (Wait for item)
  │                                ↓
  └─ Producer puts item → Item available
                              │
                              ↓
                         Consumer UNBLOCKED
                         take() returns ✓

WORKFLOW:
PRODUCER                    QUEUE              CONSUMER
   │                          │                    │
   ├─→ put(1) ─────────→ [1] ─────────→ take()
   │                          │
   ├─→ put(2) ─────────→ [1,2]
   │
   ├─→ put(3) ─────────→ [1,2,3]
   │                          │
   │                          │ ← take() ← [returns 1]
   │                    [2,3] 
   │                          │
   ├─→ put(4) ─────────→ [2,3,4]
   │                          │
   │                          │ ← take() ← [returns 2]
```

**Code Location**: `src/main/java/org/example/blockingqueue/ArrayBlockingQueueExample.java`

**Example**:
```java
BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

// Producer
new Thread(() -> {
    for (int i = 0; i < 100; i++) {
        queue.put(i);  // Blocks if queue full
    }
}).start();

// Consumer
new Thread(() -> {
    while (true) {
        int item = queue.take();  // Blocks if queue empty
        System.out.println(item);
    }
}).start();
```

---

### Concurrent Collections

**Purpose**: Thread-safe collections for concurrent access without manual synchronization.

**How it works**: Uses segment-level locking (ConcurrentHashMap) allowing multiple threads to access different segments.

**Types**:
- `ConcurrentHashMap`: Thread-safe map with segment locking
- `Collections.synchronizedList()`: Synchronized list
- `CopyOnWriteArrayList`: Good for read-heavy workloads

**Diagram - ConcurrentHashMap (Segment Locking)**:
```
TRADITIONAL HASHMAP (Single Lock):
┌─────────────────────────┐
│   Entire HashMap        │
│   [LOCK]                │
│  ┌─┬─┬─┬─┬─┬─┬─┬─┐     │
│  │0│1│2│3│4│5│6│7│     │
│  └─┴─┴─┴─┴─┴─┴─┴─┘     │
│                         │
│  Thread A access → LOCK │
│  Thread B blocked...    │
└─────────────────────────┘

CONCURRENTHASHMAP (Segment Locking - 16 segments):
┌──────────────────────────────────────┐
│   ConcurrentHashMap                  │
│  Segment1    Segment2    Segment3    │
│  [LOCK1]     [LOCK2]     [LOCK3]     │
│  ┌──┐        ┌──┐        ┌──┐        │
│  │  │        │  │        │  │        │
│  └──┘        └──┘        └──┘        │
│                          ...         │
│  Thread A locks Segment1             │
│  Thread B can lock Segment2 (concurrent!)
└──────────────────────────────────────┘

CONCURRENT ACCESS:
Thread A: put("A", 1) → Lock Segment 1 ✓
Thread B: put("K", 2) → Lock Segment 2 ✓ (No blocking!)
```

**Code Location**: `src/main/java/org/example/concurrentmap/ConcurrentMapExample.java`

**Example**:
```java
ConcurrentMap<String, Integer> map = new ConcurrentHashMap<>();

Thread t1 = new Thread(() -> map.put("A", 1));
Thread t2 = new Thread(() -> map.put("B", 2));

t1.start();
t2.start();
```

---

## Synchronization Aids

### CountDownLatch

**Purpose**: Allow threads to wait until a count reaches zero.

**How it works**: Initialized with a count; threads call `countDown()` to decrement; `await()` blocks until count reaches 0.

**When to use**: Coordinating task completion without tracking threads.

**Diagram - CountDownLatch Operation**:
```
INITIALIZATION:
CountDownLatch latch = new CountDownLatch(3)
Count: 3

WORKER THREADS:
Worker1: doWork() → countDown() → Count: 2
Worker2: doWork() → countDown() → Count: 1
Worker3: doWork() → countDown() → Count: 0 ✓

MAIN THREAD:
Main: await() → BLOCKED
                    │
                    │ (All workers done)
                    ↓
              Count reaches 0
                    │
                    ↓
         Main UNBLOCKED → Continue

TIMELINE:
─────────────────────────────────────────────
  0ms: Main thread calls await() → BLOCKED
       All workers start
  
 100ms: Worker1 finishes → countDown() [Count: 3→2]
 200ms: Worker2 finishes → countDown() [Count: 2→1]
 300ms: Worker3 finishes → countDown() [Count: 1→0]
        Main thread unblocked!
 
 301ms: Main continues execution
─────────────────────────────────────────────
```

**Code Location**: `src/main/java/org/example/latch/CountDownLatchExample.java`

**Example**:
```java
CountDownLatch latch = new CountDownLatch(3);

// Workers
for (int i = 0; i < 3; i++) {
    executor.execute(() -> {
        doWork();
        latch.countDown();  // Decrements count
    });
}

// Main waits
latch.await();  // Blocks until count = 0
System.out.println("All workers done!");
```

---

### CyclicBarrier

**Purpose**: Threads wait at a barrier until all arrive, then proceed together (reusable).

**How it works**: Threads call `await()` and block until N threads reach the barrier; then all proceed together.

**Key Difference from CountDownLatch**: Reusable and symmetric (all threads participate equally).

**Diagram - CyclicBarrier Synchronization**:
```
BARRIER WITH 4 THREADS:

Initial state:
Barrier: ════════════════ (4 threads needed)
         Count: 0/4

Thread1 arrives: ════════════════ [T1 WAITING] (1/4)
Thread2 arrives: ════════════════ [T1,T2 WAITING] (2/4)
Thread3 arrives: ════════════════ [T1,T2,T3 WAITING] (3/4)
Thread4 arrives: ════════════════ [ALL ARRIVED!]
                         ↓
              ALL THREADS PROCEED ✓

MULTIPLE PHASES (Reusable):
Phase 1:
─────────────────────────────────────
Barrier await() → T1,T2,T3,T4 BLOCKED
                  (all 4 waiting)
                         ↓
              All proceed ✓

Phase 2:
─────────────────────────────────────
Barrier await() → T1,T2,T3 BLOCKED
                  (waiting for T4)
                  T4 arrives...
                         ↓
              All proceed again ✓

TIMELINE:
T1: ▁▁▁[AWAIT]████ (Proceed at barrier)
T2: ▁▁▁▁▁[AWAIT]██ (Proceed at barrier)
T3: ▁▁▁▁▁▁[AWAIT]█ (Proceed at barrier)
T4: ▁▁▁▁▁▁▁[AWAIT] (Triggers barrier!)
    ════════════════════════════════════
```

**Code Location**: `src/main/java/org/example/barrier/CyclicBarrierExample.java`

**Example**:
```java
CyclicBarrier barrier = new CyclicBarrier(5);
ExecutorService executor = Executors.newFixedThreadPool(5);

for (int i = 0; i < 5; i++) {
    executor.execute(() -> {
        System.out.println("Working...");
        barrier.await();  // Wait for all 5 threads
        System.out.println("All synchronized, proceeding!");
    });
}
```

---

### Exchanger

**Purpose**: Two threads exchange data at a synchronization point.

**How it works**: Threads call `exchange()` with their data; blocks until both threads arrive, then swap data.

**When to use**: Producer-consumer pairs exchanging buffers or state.

**Diagram - Data Exchange**:
```
BASIC EXCHANGE:
Thread A                          Thread B
    │                                │
    ├─→ exchange(Data_A)            │
    │    (BLOCKED, waiting)          │
    │                                │
    │                    ← exchange(Data_B)
    │                      (BLOCKED, waiting)
    │                                │
    │◄─ Receives Data_B              │
    │                          Data_A ─→
    │                                │
    ↓                                ↓
  SUCCESS ✓                       SUCCESS ✓

ALTERNATING EXCHANGE:
Time  │ Thread 1                Thread 2
──────┼──────────────────────────────────
  0   │ Produce Data1           Produce Data2
  1   │ exchange(Data1) ←→ exchange(Data2)
  2   │ Received Data2          Received Data1
  3   │ Process Data2           Process Data1
  4   │ exchange(Data2') ←→ exchange(Data1')
  5   │ Received Data1'         Received Data2'
```

**Code Location**: `src/main/java/org/example/exchanger/ExchangerExample.java`

**Example**:
```java
Exchanger<Integer> exchanger = new Exchanger<>();

Thread t1 = new Thread(() -> {
    int myValue = 100;
    int receivedValue = exchanger.exchange(myValue);
    System.out.println("Got: " + receivedValue);
});

Thread t2 = new Thread(() -> {
    int myValue = 200;
    int receivedValue = exchanger.exchange(myValue);
    System.out.println("Got: " + receivedValue);
});
```

---

### Semaphores

**Purpose**: Controls access to a resource with a fixed number of permits.

**How it works**: Threads acquire permits before accessing resource; release when done.

**When to use**: Limiting concurrent access (e.g., connection pool, parking lot).

**Diagram - Semaphore with 3 Permits**:
```
INITIAL STATE:
Semaphore (permits=3)
Available: 3

THREAD ACQUISITION:
Thread1 acquire() → Available: 2 ✓
Thread2 acquire() → Available: 1 ✓
Thread3 acquire() → Available: 0 ✓
Thread4 acquire() → BLOCKED (no permits)
Thread5 acquire() → BLOCKED (no permits)

ACCESS CONTROL:
┌─────────────────────────┐
│   Semaphore (3 permits) │
│   ┌─────────────────┐   │
│   │ T1 (permits=1) │   │
│   │ T2 (permits=1) │   │
│   │ T3 (permits=1) │   │
│   └─────────────────┘   │
│   T4 [BLOCKED]          │
│   T5 [BLOCKED]          │
└─────────────────────────┘

RELEASE FLOW:
T1 release() → Available: 1
                   │
              T4 unblocked
              acquire() → Available: 0

TIMELINE:
T1: [WORK]release() ─────────────────────
T2: ─[WORK]release() ──────────────────
T3: ──[WORK]release() ────────────────
T4: ───────[BLOCKED][WORK]release() ──
T5: ────────────────[BLOCKED][WORK]
    ════════════════════════════════════
```

**Code Location**: (Not found in repo, but standard usage)

**Example**:
```java
Semaphore semaphore = new Semaphore(3);  // 3 permits

Thread thread = new Thread(() -> {
    semaphore.acquire();  // Get a permit (blocks if none available)
    try {
        // Access protected resource
        System.out.println("Using resource");
    } finally {
        semaphore.release();  // Release the permit
    }
});
```

---

## Advanced Patterns

### Fork/Join Framework

**Purpose**: Efficiently parallelize divide-and-conquer algorithms using recursive tasks.

**How it works**:
- Tasks are split into smaller subtasks (fork)
- Subtasks execute in parallel
- Results are merged (join)
- Work-stealing balances load

**When to use**: Recursive algorithms, parallel array processing.

**Key Classes**:
- `RecursiveTask<V>`: Returns a result
- `RecursiveAction`: No return value

**Diagram - Fork/Join Operation**:
```
DIVIDE AND CONQUER:
                        Task [1..100]
                              │
                    ┌─────────┴─────────┐
                    │                   │
            Task [1..50]      Task [51..100]
                    │                   │
              ┌─────┴─────┐      ┌─────┴─────┐
              │           │      │           │
         [1..25]      [26..50] [51..75] [76..100]
              │           │      │           │
              └─────┬─────┘      └─────┬─────┘
                    │                   │
                Result1            Result2
                    │                   │
                    └─────────┬─────────┘
                             │
                        Final Result

EXECUTION TIMELINE:
ForkJoinPool
  Worker1: Task[1..100] → Fork → [1..50]
  Worker2: Task[1..100] → Fork → [51..100]
  Worker3: [1..50] → Fork → [1..25]
  Worker4: [1..50] → Fork → [26..50]
  Worker5: [51..100] → Fork → [51..75]
  Worker2: [51..100] → Fork → [76..100]
           
  (All working in parallel)
           
  Worker3: Join [1..25] result
  Worker4: Join [26..50] result
  Worker1: Join [1..50] result
  Worker5: Join [51..75] result
  Worker2: Join [76..100] result
           
  Main: Get final merged result ✓

WORK STEALING:
Worker A busy (processing big task)
Worker B idle
    │
    └─→ Steal half of A's queue
        (Keep load balanced!)
```

**Code Location**: `src/main/java/org/example/forkjoin/PrintAction.java`, `src/main/java/org/example/forkjoin/FibonacciTask.java`

**Example**:
```java
class SumTask extends RecursiveTask<Long> {
    private int[] array;
    
    protected Long compute() {
        if (array.length <= 10) {
            long sum = 0;
            for (int i : array) sum += i;
            return sum;
        } else {
            int mid = array.length / 2;
            SumTask left = new SumTask(/* left half */);
            SumTask right = new SumTask(/* right half */);
            
            left.fork();
            long rightResult = right.compute();
            long leftResult = left.join();
            
            return leftResult + rightResult;
        }
    }
}

// Usage
ForkJoinPool pool = new ForkJoinPool();
long result = pool.invoke(new SumTask(array));
```

---

### Deadlock

**Purpose**: Understanding and preventing deadlock situations.

**What it is**: Two or more threads are blocked forever, waiting for each other to release resources.

**Conditions (All must be present)**:
1. Mutual Exclusion: Resource can't be shared
2. Hold and Wait: Thread holds resource while waiting
3. No Preemption: Can't force resource release
4. Circular Wait: T1 waits for T2, T2 waits for T1

**Prevention Strategies**:
- Acquire locks in consistent order
- Use timeouts with `tryLock()`
- Avoid circular dependencies

**Diagram - Deadlock Scenario**:
```
DEADLOCK EXAMPLE:
Thread A                          Thread B
    │                                │
    ├─→ Acquire Lock1 ✓              │
    │                                │
    │                    ← Acquire Lock2 ✓
    │
    ├─→ Try to acquire Lock2        │
    │   (Held by Thread B)          │
    │   BLOCKED ⛔                   │
    │                                │
    │                    Try to acquire Lock1
    │                    (Held by Thread A)
    │                    BLOCKED ⛔
    │                                │
    └─ DEADLOCK! Both waiting forever ─┘

CIRCULAR DEPENDENCY:
┌──────────────────────────────────────┐
│           DEADLOCK CYCLE             │
│                                      │
│  Thread A ──→ Lock1 ─────┐          │
│      ↑                    │          │
│      │                    ↓          │
│      └─── Lock2 ← Thread B          │
│                                      │
└──────────────────────────────────────┘

PREVENTION - ORDERED ACQUISITION:
Thread A                          Thread B
    │                                │
    ├─→ Acquire Lock1 ✓              │
    │                    ← Wait (T1 has L1)
    ├─→ Acquire Lock2 ✓              │
    │                                │
    ├─→ Complete work                │
    │                                │
    ├─→ Release Lock1                │
    ├─→ Release Lock2                │
    │                    Thread B:
    │                    Can now acquire both
    │                    in same order ✓
    │                                │
    └─ NO DEADLOCK! ─────────────────┘
```

**Code Location**: `src/main/java/org/example/deadlock/DeadlockExample.java`

**Example - Prevention**:
```java
public void worker1() {
    lock1.lock();
    try {
        // work with lock1
        lock2.lock();
        try {
            // work with both
        } finally {
            lock2.unlock();
        }
    } finally {
        lock1.unlock();
    }
}

public void worker2() {
    // IMPORTANT: Same order (lock1 then lock2)
    lock1.lock();
    try {
        // work with lock1
        lock2.lock();
        try {
            // work with both
        } finally {
            lock2.unlock();
        }
    } finally {
        lock1.unlock();
    }
}
```

---

### Livelock

**Purpose**: Understanding and preventing livelock situations.

**What it is**: Threads are active but make no progress, constantly yielding to each other.

**Difference from Deadlock**:
- Deadlock: Threads are blocked, waiting
- Livelock: Threads are running but accomplishing nothing

**Prevention**:
- Use timeouts
- Randomize retry delays
- Use proper synchronization primitives

**Diagram - Livelock Scenario**:
```
LIVELOCK EXAMPLE:
Thread A                          Thread B
    │                                │
    ├─→ Try Lock1                    │
    │   Success! ✓                   │
    │                                │
    │                    ← Try Lock2
    │                      Success! ✓
    │
    ├─→ Try Lock2                    │
    │   FAILED (held by T2)          │
    │   Yield/Retry                  │
    │                                │
    │                    Try Lock1
    │                    FAILED (held by T1)
    │                    Yield/Retry
    │
    ├─→ Try Lock1 again ✓            │
    │                                │
    │                    ← Try Lock2 again ✓
    │
    ├─→ Try Lock2 again              │
    │   FAILED... Retry... RETRY...  │
    │   (ENDLESS LOOP!)              │
    │                    Try Lock1 again
    │                    FAILED... Retry...
    │                    (ENDLESS LOOP!)
    │                                │
    └─ LIVELOCK! Busy but stuck! ────┘

COMPARISON:
DEADLOCK vs LIVELOCK:

Deadlock:
Thread A: [BLOCKED ⛔]
Thread B: [BLOCKED ⛔]
CPU: Low usage (waiting)

Livelock:
Thread A: [RUNNING → YIELD] → [RUNNING → YIELD]...
Thread B: [RUNNING → YIELD] → [RUNNING → YIELD]...
CPU: High usage (busy doing nothing!)

PREVENTION - TIMEOUTS:
if (lock1.tryLock(100, TimeUnit.MILLISECONDS)) {
    try {
        // work
    } finally {
        lock1.unlock();
    }
} else {
    // Back off / retry with delay
    Thread.sleep(random.nextInt(100));
}
```

**Code Location**: `src/main/java/org/example/livelock/LiveLockExample.java`

---

### Dining Philosophers Problem

**Purpose**: Classic synchronization problem illustrating deadlock and resource contention.

**Problem**:
- N philosophers sit around a table
- Each has a bowl of spaghetti
- Between each pair: one chopstick
- To eat: philosopher needs BOTH chopsticks

**Deadlock Scenario**:
```
DEADLOCK CONDITION:
     ┌─────────────────┐
     │   Philosopher1  │
     │   Holds: Left   │
     │   Wants: Right  │
     │   [BLOCKED ⛔]   │
     └────────┬────────┘
              │ Chopstick
              │
         ┌────┴───┐
         │         │
     Chop1       Chop5
         │         │
    ┌────┴────┬───┴────┐
    │        │         │
Philosopher2        Philosopher5
Holds: Left         Holds: Right
Wants: Right        Wants: Left
[BLOCKED ⛔]        [BLOCKED ⛔]
    │                 │
    └─ Circular dependency (DEADLOCK!)

SOLUTION 1 - ASYMMETRIC ACQUISITION:
All odd philosophers: Pick up left, then right
Even philosophers: Pick up right, then left

SOLUTION 2 - RESOURCE LIMIT:
Only N-1 philosophers allowed to eat simultaneously

SOLUTION 3 - MONITOR PATTERN:
Central manager grants permission to eat
```

**Code Location**: (Not present; classic example to implement)

---

## Parallel Computing

**Purpose**: Leveraging multiple processor cores for speed.

**How it works**:
- Operating system schedules threads on different cores
- Each core executes independently
- True parallelism (not just time-slicing)

**Concurrency vs Parallelism**:
```
CONCURRENCY (Single Core - Time Slicing):
Task A: ████ ░ ████ ░ ████ ░ (Interleaved)
Task B: ░ ████ ░ ████ ░ ████
        Time ────────────────→

PARALLELISM (Multiple Cores):
Core 1: Task A ████████████████
Core 2: Task B ████████████████
Core 3: Task C ████████████████
        Time ────────────────→
        (Simultaneous execution!)

SPEEDUP EXAMPLE:
Sequential (1 core):
Total Time: 8 hours (8 tasks × 1 hour each)

Parallel (8 cores):
Total Time: 1 hour (8 tasks × 1 hour each, in parallel)
Speedup: 8x
```

**Code Location**: `src/main/java/org/example/parallelcomputing/ParallelComputingExample.java`

---

## Stream API Examples

**Purpose**: Functional programming style operations on collections.

**Concepts**:
- `map()`: Transform each element
- `filter()`: Keep elements matching condition
- `collect()`: Gather results
- `groupingBy()`: Group elements by key
- `sorted()`: Sort elements

**Diagram - Stream Operations**:
```
STREAM PIPELINE:
Source → Filter → Map → Collect → Result

Book List
    │
    ├─→ filter(pages > 200)
    │   Result: [Book1, Book3, Book5]
    │
    ├─→ map(Book::getTitle)
    │   Result: ["Title1", "Title3", "Title5"]
    │
    ├─→ collect(toList())
    │   Result: List<String>

GROUPING BY:
People List
    │
    ├─→ groupingBy(Person::getDepartment)
    │   Result:
    │   {
    │       "Engineering": [P1, P3, P6],
    │       "Sales": [P2, P5],
    │       "Marketing": [P4, P8],
    │       "HR": [P7, P10]
    │   }

COUNT OPERATION:
groupingBy(Person::getDepartment, counting())
    │
    ├─→ Result:
    │   {
    │       "Engineering": 3,
    │       "Sales": 2,
    │       "Marketing": 2,
    │       "HR": 2
    │   }
```

**Code Locations**:
- `src/main/java/org/example/streamapi/BookCustomExample.java`
- `src/main/java/org/example/streamapi/groupingby/PersonExample.java`

---

## Quick Start

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### Build the Project
```bash
cd multithreading
mvn clean compile
```

### Run Examples

**Run all tests**:
```bash
mvn test
```

**Run specific class**:
```bash
java -cp target/classes org.example.synchronization.SynchronizationExample
java -cp target/classes org.example.forkjoin.FibonacciTask
java -cp target/classes org.example.streamapi.BookCustomExample
```

**Run via IDE**:
1. Open project in IDE
2. Navigate to main class
3. Right-click → Run

---

## Project Structure

```
multithreading/
├── src/main/java/org/example/
│   ├── Main.java
│   ├── atomic/
│   │   └── AtomicIntegerExample.java
│   ├── barrier/
│   │   ├── CyclicBarrierExample.java
│   │   └── Worker.java
│   ├── blockingqueue/
│   │   ├── ArrayBlockingQueueExample.java
│   │   ├── LinkedBlockingQueueExample.java
│   │   ├── PriorityQueueExample.java
│   │   └── ...
│   ├── collection/
│   │   └── ListExample.java
│   ├── concurrentmap/
│   │   ├── ConcurrentMapExample.java
│   │   └── ...
│   ├── deadlock/
│   │   └── DeadlockExample.java
│   ├── exchanger/
│   │   ├── ExchangerExample.java
│   │   └── ...
│   ├── forkjoin/
│   │   ├── ForkJoin.java
│   │   ├── PrintAction.java
│   │   ├── SimpleRecursiveAction.java
│   │   └── FibonacciTask.java
│   ├── join/
│   │   └── JoinExample.java
│   ├── latch/
│   │   ├── CountDownLatchExample.java
│   │   └── Work.java
│   ├── livelock/
│   │   └── LiveLockExample.java
│   ├── lock/
│   │   ├── LockExample.java
│   │   ├── ReentrantExample.java
│   │   └── ...
│   ├── parallelcomputing/
│   │   └── ParallelComputingExample.java
│   ├── philosopher/
│   │   └── (Implement dining philosophers)
│   ├── semaphores/
│   │   └── (Implement semaphore examples)
│   ├── synchronization/
│   │   └── SynchronizationExample.java
│   ├── threadpool/
│   │   └── ThreadPoolExample.java
│   ├── volatileexample/
│   │   ├── VolatileExample.java
│   │   └── Worker.java
│   └── streamapi/
│       ├── BookCustomExample.java
│       ├── Book.java
│       ├── Type.java
│       └── groupingby/
│           ├── PersonExample.java
│           └── Person.java
├── pom.xml
└── README.md
```

---

## Common Patterns & Best Practices

### Thread-Safe Patterns

| Pattern | Use Case | Thread-Safe |
|---------|----------|-------------|
| `synchronized` | Simple shared resource | ✓ |
| `AtomicInteger` | Counter operations | ✓ |
| `ReentrantLock` | Complex locking needs | ✓ |
| `ConcurrentHashMap` | Shared map | ✓ |
| `BlockingQueue` | Producer-consumer | ✓ |
| `CountDownLatch` | Task coordination | ✓ |
| `CyclicBarrier` | Phase synchronization | ✓ |
| `ThreadPool` | Task execution | ✓ |

### When to Use What

```
Need simple mutual exclusion?
  → synchronized or ReentrantLock

Need lock with timeout?
  → ReentrantLock.tryLock()

Need to coordinate tasks?
  → CountDownLatch or CyclicBarrier

Need producer-consumer?
  → BlockingQueue

Need to share data safely?
  → ConcurrentHashMap, ConcurrentList

Need parallel divide-and-conquer?
  → Fork/Join Framework

Need to exchange data?
  → Exchanger

Need to limit concurrent access?
  → Semaphore

Need visibility across threads?
  → volatile keyword
```

---

## Troubleshooting

### Common Issues

**Issue**: Race condition - incorrect counter value
```
Cause: Multiple threads modifying counter without synchronization
Solution: Use synchronized, AtomicInteger, or ReentrantLock
```

**Issue**: Deadlock - program hangs indefinitely
```
Cause: Circular lock dependencies
Solution: Acquire locks in consistent order, use tryLock() with timeout
```

**Issue**: Livelock - high CPU but no progress
```
Cause: Threads busy yielding to each other
Solution: Add timeouts, randomize retries, use better synchronization
```

**Issue**: Visibility problem - thread doesn't see updates
```
Cause: CPU caching, compiler optimizations
Solution: Use volatile keyword or proper synchronization
```

---

## Resources

### Official Documentation
- [Oracle Java Concurrency Tutorial](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
- [Java Concurrent API Javadocs](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/package-summary.html)
- [Java Memory Model](https://docs.oracle.com/javase/specs/jls/se17/html/jls-17.html)

### Books
- "Java Concurrency in Practice" by Brian Goetz et al.
- "Effective Java" by Joshua Bloch

### Online Courses
- [Udemy Multithreading Courses](https://www.udemy.com/)
- [Java Concurrency Tutorials](https://www.baeldung.com/java-concurrency)

### Tools
- VisualVM: Thread monitoring and analysis
- JConsole: Java monitoring console
- IntelliJ Debugger: Thread debugging
- Thread Dump Analyzer: Analyze deadlocks

---

## Contributing

Feel free to extend this repository with:
- Additional examples for Dining Philosophers problem
- Semaphore implementations
- Performance comparisons
- More Stream API examples

---

## License

This project is for educational purposes.

---

## Quick Reference Card

```
╔══════════════════════════════════════════════════════════════╗
║           JAVA MULTITHREADING QUICK REFERENCE              ║
╠══════════════════════════════════════════════════════════════╣
║ synchronized         → Mutual exclusion                      ║
║ volatile            → Visibility guarantee                   ║
║ AtomicInteger       → Lock-free atomic ops                   ║
║ ReentrantLock       → Explicit lock control                  ║
║ CountDownLatch      → Wait for N tasks                       ║
║ CyclicBarrier       → Synchronize N threads                  ║
║ Exchanger           → Swap data between 2 threads            ║
║ BlockingQueue       → Thread-safe queue                      ║
║ ConcurrentHashMap   → Thread-safe map                        ║
║ Semaphore           → Resource permits                        ║
║ Fork/Join           → Parallel divide-and-conquer            ║
║ ExecutorService     → Thread pool management                 ║
╚══════════════════════════════════════════════════════════════╝
```

---

**Last Updated**: April 27, 2026
**Version**: 1.0
**Maintainer**: Multithreading Study Guide
```