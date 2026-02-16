1. why did I even create a capacity class?
2. What if we reserver the place when we check for the capacity already? And then when we listen the event that id has the priority since they reserved?
Part 2: Critique & Improvement Discussion (20-30 minutes)
Critical Issues to Acknowledge
Issue 1: Race Condition in Concurrent Bookings
Interviewer: "What happens if two users try to book the last spot simultaneously?"

Problem Walkthrough:

T1: User A requests booking → gRPC validates (1 spot available) ✓
T2: User B requests booking → gRPC validates (1 spot available) ✓
T3: User A's booking persisted → event published
T4: User B's booking persisted → event published
T5: Class Service processes A's event → spots: 1 → 0
T6: Class Service processes B's event → spots: 0 → -1 ❌ OVERBOOKING!

3. What does this mean? Idempotent Event Handlers:

// Include booking ID in event, track processed IDs
if (processedEventIds.contains(event.getBookingId())) {
    return;  // Already processed
}


4.Issue 4: No Circuit Breaker for gRPC
Interviewer: "Class Service goes down for 10 seconds. What happens to booking requests?"

Problem: Every booking request will:

Try gRPC call
Wait for timeout (default 10+ seconds)
Fail with GrpcCommunicationException
Return 503 to user
Impact: Slow failure, thread pool exhaustion, cascading failures

Solution - Resilience4j Circuit Breaker:

@CircuitBreaker(name = "classService", fallbackMethod = "validateClassFallback")
public void validateClassAvailability(Long classId) {
    // gRPC call
}

private void validateClassFallback(Long classId, Exception e) {
    throw new ServiceUnavailableException("Class Service temporarily unavailable");
}