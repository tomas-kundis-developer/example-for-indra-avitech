package org.tomo.indra.fifo.api;

/**
 * Concurrent thread-safe FIFO.
 *
 * <p>All implementations of this interface must be thread-safe.
 *
 * @param <T> Type of the message held by the FIFO.
 */
public interface IndraFifo<T> {

  /**
   * Return number of free FIFO message capacity.
   */
  int getFreeCapacity();

  /**
   * Get the channel name for concrete FIFO instance.
   *
   * <p>Channel ist just more general name for exchanging messages through various data channels
   * like a FIFO.
   */
  String getChannelName();

  /**
   * Return number of messages inserted into FIFO.
   */
  int getUsedCapacity();

  /**
   * Returns {@code true} if FIFO is empty.
   *
   * @return {@code true} if FIFO is empty, otherwise {@code false}.
   */
  boolean isEmpty();

  /**
   * Insert message into FIFO.
   *
   * <p>If FIFO is full, this method will wait (block) until the capacity becomes available.
   *
   * @throws InterruptedException Throws when thread is interrupted during waiting.
   */
  void put(T message) throws InterruptedException;

  /**
   * Take a message from FIFO.
   *
   * <p>If FIFO is empty, this method will wait (block)
   * until there will be at least one message inserted.
   *
   * @throws InterruptedException Throws when thread is interrupted during waiting.
   */
  T take() throws InterruptedException;
}
