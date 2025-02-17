package org.bff.javampd.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class MPDCommandTest {

  @Test
  void testCommand() {
    MPDCommand mpdCommand = new MPDCommand("command");
    assertEquals("command", mpdCommand.getCommand());
  }

  @Test
  void testCommandWithOneParm() {
    MPDCommand mpdCommand = new MPDCommand("command", "parm");
    assertEquals("command", mpdCommand.getCommand());
  }

  @Test
  void testCommandWithParms() {
    MPDCommand mpdCommand = new MPDCommand("command", "parm1", "parm2");
    assertEquals("command", mpdCommand.getCommand());
  }

  @Test
  void testParmsWithNoParam() {
    MPDCommand mpdCommand = new MPDCommand("command");
    assertEquals(0, mpdCommand.getParams().size());
  }

  @Test
  void testParmsWithOneParm() {
    MPDCommand mpdCommand = new MPDCommand("command", "parm");
    assertEquals(1, mpdCommand.getParams().size());
    assertEquals("parm", mpdCommand.getParams().getFirst());
  }

  @Test
  void testParmsWithMultipleParms() {
    MPDCommand mpdCommand = new MPDCommand("command", "parm1", "parm2");
    assertEquals(2, mpdCommand.getParams().size());
    assertEquals("parm1", mpdCommand.getParams().getFirst());
    assertEquals("parm2", mpdCommand.getParams().get(1));
  }

  @Test
  void equalsContract() {
    EqualsVerifier.simple().forClass(MPDCommand.class).verify();
  }

  @Test
  void testNullException() {
    assertThrows(IllegalArgumentException.class, () -> new MPDCommand(null));
  }
}
