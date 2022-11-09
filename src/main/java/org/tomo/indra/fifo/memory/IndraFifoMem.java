package org.tomo.indra.fifo.memory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomo.indra.fifo.api.IndraFifo;

/**
 * Concurrent in-memory thread-safe FIFO.
 *
 * @see IndraFifo
 */
public class IndraFifoMem<T> implements IndraFifo<T> {
  private final Logger log = LoggerFactory.getLogger(IndraFifoMem.class);

  private final BlockingQueue<T> queue;

  private final String channelName;

  /**
   * Create a new in-memory {@link IndraFifo} implementation.
   *
   * @param capacity    Maximum number of messages in FIFO.
   *                    This capacity can't be changed when FIFO is created.
   * @param channelName Name of the created FIFO channel instance.
   *                    Channel ist just more general name for exchanging messages through various
   *                    data channels like a FIFO.
   * @see IndraFifo
   */
  public IndraFifoMem(int capacity, String channelName) {
    this.queue = new LinkedBlockingQueue<>(capacity);
    this.channelName = channelName;
    log.info("Created a new FIFO message channel `{}` with capacity: {}", channelName,
        queue.remainingCapacity());
  }

  @Override
  public boolean isEmpty() {
    return queue.isEmpty();
  }

  @Override
  public void put(T message) throws InterruptedException {
    if (message == null) {
      log.warn("null message");
      return;
    }

    log.debug("Insert message: `{}` before calling (blocking) put(). | free: {}", message,
        queue.remainingCapacity());
    this.queue.put(message);
    log.info("Insert message: `{}` | free: {}", message, queue.remainingCapacity());
  }

  @Override
  public T take() throws InterruptedException {
    log.debug("Take message - before calling (blocking) take(). | free: {}",
        queue.remainingCapacity());

    var msg = this.queue.take();
    log.info("Take message: `{}` | free: {}", msg, queue.remainingCapacity());

    return msg;
  }

  @Override
  public int getFreeCapacity() {
    return queue.remainingCapacity();
  }

  @Override
  public String getChannelName() {
    return this.channelName;
  }

  @Override
  public int getUsedCapacity() {
    return queue.size();
  }
}
