package org.tomo.indra.fifo.memory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomo.domain.commands.ICommandMessage;
import org.tomo.indra.fifo.api.IndraFifo;
import test.utils.TestCommandMessage1;
import test.utils.TestCommandMessage2;
import test.utils.TestUtils;

class IndraFifoMemTest {

  @Test
  void testTake() {
    IndraFifo<ICommandMessage> fifo = new IndraFifoMem<>(2, "testTake channel");

    assertDoesNotThrow(() -> {
      fifo.put(new TestCommandMessage2("cmd 101"));
      fifo.put(new TestCommandMessage2("cmd 102"));
    });

    assertDoesNotThrow(() -> {
      assertThat(((TestCommandMessage2) fifo.take()).getId()).isEqualTo("cmd 101");
      assertThat(((TestCommandMessage2) fifo.take()).getId()).isEqualTo("cmd 102");
    });
  }

  /**
   * Simple single-thread test without concurrency.
   */
  @Test
  void simpleTest() throws Exception {
    int capacity = 10;
    String channelName = "simpleTest channel";

    IndraFifo<ICommandMessage> fifo = new IndraFifoMem<>(capacity, channelName);

    assertThat(fifo.getFreeCapacity()).isEqualTo(capacity);
    assertThat(fifo.getUsedCapacity()).isZero();
    assertThat(fifo.getChannelName()).isEqualTo(channelName);

    fifo.put(null);
    fifo.put(null);
    assertThat(fifo.getFreeCapacity()).isEqualTo(capacity);

    fifo.put(new TestCommandMessage1());
    fifo.put(new TestCommandMessage1());
    fifo.put(new TestCommandMessage1());
    assertThat(fifo.getFreeCapacity()).isEqualTo(capacity - 3);
    assertThat(fifo.getUsedCapacity()).isEqualTo(3);
  }

  /**
   * Test full capacity.
   *
   * <p>Test, that {@link IndraFifo#put(Object)} will not block before FIFO capacity is exhausted.
   */
  @Test
  void testEmptyAndFullCapacity() throws Exception {
    int capacity = 5;
    IndraFifo<ICommandMessage> fifo =
        new IndraFifoMem<>(capacity, "testEmptyAndFullCapacity channel");

    assertThat(fifo.isEmpty()).isTrue();

    while (fifo.getFreeCapacity() != 0) {
      fifo.put(new TestCommandMessage1());
    }

    assertThat(fifo.getFreeCapacity()).isZero();
    assertThat(fifo.getUsedCapacity()).isEqualTo(capacity);

    while (fifo.getFreeCapacity() != capacity) {
      fifo.take();
    }

    assertThat(fifo.isEmpty()).isTrue();
  }

  /**
   * Runs 3 threads for putting messages + 3 threads for taking messages.
   *
   * <p>Because of 3+3, in the end, all threads will complete without waiting for putting/taking
   * a message, so the whole test will complete at some time and won't block to infinity.
   *
   * <p>To achieve mentioned run-to-complete thread plan, we need a FIFO with maximum capacity of 1.
   */
  @Test
  void testConcurrency() throws InterruptedException {
    Logger log = LoggerFactory.getLogger(this.getClass());

    // Prepare FIFO.
    // ---------------------------------------------------------------------------------------------

    int capacity = 1;
    IndraFifo<ICommandMessage> fifo = new IndraFifoMem<>(capacity, "testConcurrency channel");

    // Prepare threads.
    // ---------------------------------------------------------------------------------------------

    int numberOfThreads = 6;
    ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);

    // Latch for 3 putting messages threads.
    CountDownLatch latch1 = new CountDownLatch(numberOfThreads / 2);

    // Latch for 3 taking messages threads.
    CountDownLatch latch2 = new CountDownLatch(numberOfThreads / 2);

    // Run the threads.
    // ---------------------------------------------------------------------------------------------

    assertThat(fifo.isEmpty()).isTrue();

    // In every cycle-run runs 2 threads - 1 for putting a message, 1 for reading a message.
    for (int i = 0; i < numberOfThreads / 2; i++) {

      // Create a new thread - put message.
      service.execute(() -> {
        try {
          Thread.sleep(TestUtils.randInt(1000, 1200));
          fifo.put(new TestCommandMessage1());
        } catch (InterruptedException e) {
          log.error("InterruptedException during the thread execution.");
        }
        latch1.countDown();
      });

      // Create a new thread - take message.
      service.execute(() -> {
        try {
          Thread.sleep(TestUtils.randInt(0, 200));
          fifo.take();
        } catch (InterruptedException e) {
          log.error("InterruptedException during the thread execution.");
        }
        latch2.countDown();
      });
    }

    log.info("All threads are running. Waiting to be all threads done.");
    // Wait for both groups of threads - putting and taking threads.
    latch1.await();
    latch2.await();
    log.info("All threads done.");

    assertThat(fifo.isEmpty()).isTrue();
  }
}