package org.tomo.domain.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tomo.domain.IUserRepository;
import org.tomo.domain.User;

@ExtendWith(MockitoExtension.class)
class PrintAllCmdTest {
  private final PrintStream standardOut = System.out;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();


  @Mock
  IUserRepository mockedUserRepo;

  @BeforeEach
  void beforeEach() {
    // Redirect System.out.* to our custom outputStreamCaptor from which we can get output as str.
    System.setOut(new PrintStream(outputStreamCaptor));

    List<User> usersMock = Arrays.asList(
        new User(1001, "f4fe6624-d890-4619-a2fe-5a9e8ed1638c", "Test user 1"),
        new User(1002, "6036fe21-a4fb-4ed8-93cf-c25174472123", "Test user 2"),
        new User(1003, "5890f47e-7a1e-4fa1-9c3a-fbc4384ef389", "Test user 3"));

    when(mockedUserRepo.findAll()).thenReturn(usersMock);
  }

  @AfterEach
  public void tearDown() {
    System.setOut(standardOut);
  }

  @Test
  void testPrintAll() {
    PrintAllCmd printAllCmd = new PrintAllCmd(mockedUserRepo);
    printAllCmd.printAll();

    // Don't use text block because of system-dependent line separator.
    String expectedOutput =
        "#1001, 'f4fe6624-d890-4619-a2fe-5a9e8ed1638c', Test user 1" + System.lineSeparator()
            + "#1002, '6036fe21-a4fb-4ed8-93cf-c25174472123', Test user 2" + System.lineSeparator()
            + "#1003, '5890f47e-7a1e-4fa1-9c3a-fbc4384ef389', Test user 3" + System.lineSeparator();

    assertThat(outputStreamCaptor).hasToString(expectedOutput);
  }
}