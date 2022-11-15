package org.tomo.domain.commands;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tomo.domain.IUserRepository;
import org.tomo.domain.User;

@ExtendWith(MockitoExtension.class)
class AddCmdTest {

  @Mock
  IUserRepository mockedUserRepo;

  @Test
  void addUser() {
    AddCmd addCmd = new AddCmd(mockedUserRepo);
    User user = new User(15033, "c613a824-fcc6-415a-af6f-0ef4fe068391", "Test user 15 033");
    addCmd.add(user);
    verify(mockedUserRepo).add(user);
  }

  @Test
  void addNullUser() {
    AddCmd addCmd = new AddCmd(mockedUserRepo);

    assertThatNullPointerException().isThrownBy(() -> addCmd.add(null))
        .withMessageContaining("marked non-null but is null");
  }
}