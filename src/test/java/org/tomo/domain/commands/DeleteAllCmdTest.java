package org.tomo.domain.commands;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tomo.domain.IUserRepository;

@ExtendWith(MockitoExtension.class)
class DeleteAllCmdTest {

  @Mock
  IUserRepository mockedUserRepo;

  @Test
  void deleteAll() {
    DeleteAllCmd deleteAllCmd = new DeleteAllCmd(mockedUserRepo);
    deleteAllCmd.deleteAll();
    verify(mockedUserRepo).deleteAll();
  }
}