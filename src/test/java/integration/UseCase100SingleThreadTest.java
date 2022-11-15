package integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomo.AppInitialization;
import org.tomo.domain.IUserRepository;
import org.tomo.domain.User;
import org.tomo.domain.commands.AddCmd;
import org.tomo.domain.commands.AddCmdMessage;
import org.tomo.domain.commands.DeleteAllCmd;
import org.tomo.domain.commands.DeleteAllCmdMessage;
import org.tomo.domain.commands.ICommandMessage;
import org.tomo.domain.commands.PrintAllCmd;
import org.tomo.domain.commands.PrintAllCmdMessage;
import org.tomo.domain.services.ConsumerService;
import org.tomo.indra.fifo.api.IndraFifo;
import org.tomo.indra.fifo.memory.IndraFifoMem;
import org.tomo.infrastructure.datasource.SqlDataSource;
import org.tomo.infrastructure.repositories.UserJdbcRepository;
import org.tomo.shared.exceptions.AppInitializationException;

class UseCase100SingleThreadTest {
  private final Logger log = LoggerFactory.getLogger(UseCase100SingleThreadTest.class);

  @Test
  void testScenario1() throws AppInitializationException {
    // Initialize application.
    // ---------------------------------------------------------------------------------------------

    AppInitialization.init();

    assertThat(SqlDataSource.getInstance()).isNotNull();

    // Initialize global shared (singleton) services.
    // ---------------------------------------------------------------------------------------------

    IUserRepository userRepo = UserJdbcRepository.getInstance();

    assertThat(userRepo).isNotNull();

    // Initialize FIFO.
    // ---------------------------------------------------------------------------------------------

    String fifoChannelName = "use-case-single-t channel";
    int fifoMaxCapacity = 10;
    IndraFifo<ICommandMessage> fifo = new IndraFifoMem<>(fifoMaxCapacity, fifoChannelName);

    assertThat(fifo.getUsedCapacity()).isZero();
    assertThat(fifo.getFreeCapacity()).isEqualTo(fifoMaxCapacity);

    // Prepare command's messages for exchanging through FIFO (test scenario).
    // ---------------------------------------------------------------------------------------------

    List<ICommandMessage> messagesToSend = Arrays.asList(
        AddCmdMessage.create(new User(1L, "5f311e76-35d4-4167-9542-e6b4e965e881", "Robert")),
        AddCmdMessage.create(new User(2L, "241e243b-63e6-43d9-981b-ab0a62024bb2", "Martin")),
        PrintAllCmdMessage.create(),
        DeleteAllCmdMessage.create(),
        PrintAllCmdMessage.create());

    assertThat(messagesToSend).hasSize(5);

    // PRODUCER: Insert command's messages into FIFO, one by one.
    // ---------------------------------------------------------------------------------------------

    messagesToSend.forEach((message) -> {
      try {
        fifo.put(message);
      } catch (InterruptedException e) {
        log.info("FIFO PRODUCER thread interrupted during message waiting for putting to FIFO: {}",
            message);
      }
    });

    assertThat(fifo.getUsedCapacity()).isEqualTo(messagesToSend.size());

    // Forget all prepared command's messages - they are always in FIFO message channel.
    // ---------------------------------------------------------------------------------------------

    messagesToSend = Collections.emptyList();
    assertThat(messagesToSend).isEmpty();

    // CONSUMER: Take and process command's messages from FIFO, one by one.
    // ---------------------------------------------------------------------------------------------

    AddCmd addCmd = new AddCmd(userRepo);
    DeleteAllCmd deleteAllCmd = new DeleteAllCmd(userRepo);
    PrintAllCmd printAllCmd = new PrintAllCmd(userRepo);

    ConsumerService consumerService =
        new ConsumerService(fifo, addCmd, deleteAllCmd, printAllCmd);

    // Start blocking never-ending loop.
    consumerService.consumeAll();
  }
}
