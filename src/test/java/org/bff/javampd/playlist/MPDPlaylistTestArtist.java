package org.bff.javampd.playlist;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.command.MPDCommand;
import org.bff.javampd.server.ServerStatus;
import org.bff.javampd.song.MPDSong;
import org.bff.javampd.song.SongConverter;
import org.bff.javampd.song.SongDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MPDPlaylistTestArtist {

  @Mock private SongDatabase songDatabase;
  @Mock private ServerStatus serverStatus;
  @Mock private PlaylistProperties playlistProperties;
  @Mock private CommandExecutor commandExecutor;
  @Mock private SongConverter songConverter;
  @Mock private PlaylistSongConverter playlistSongConverter;
  @InjectMocks private MPDPlaylist playlist;
  @Captor private ArgumentCaptor<String> stringArgumentCaptor;
  @Captor private ArgumentCaptor<Integer> integerArgumentCaptor;
  @Captor private ArgumentCaptor<List<MPDCommand>> commandArgumentCaptor;

  private PlaylistProperties realPlaylistProperties;

  @BeforeEach
  void setup() {
    realPlaylistProperties = new PlaylistProperties();
  }

  @Test
  void testRemoveArtist() {
    MPDArtist artist = new MPDArtist("testArtist");

    List<MPDPlaylistSong> mockedSongs = new ArrayList<>();
    MPDPlaylistSong song1 =
        MPDPlaylistSong.builder()
            .file("file1")
            .title("testSong1")
            .artistName(artist.name())
            .id(1)
            .build();

    MPDPlaylistSong song2 =
        MPDPlaylistSong.builder()
            .file("file2")
            .title("testSong1")
            .artistName(artist.name())
            .id(2)
            .build();

    MPDPlaylistSong song3 =
        MPDPlaylistSong.builder()
            .file("file3")
            .title("testSong1")
            .artistName("bogus")
            .albumName("bogus")
            .build();

    mockedSongs.add(song1);
    mockedSongs.add(song2);
    mockedSongs.add(song3);

    List<String> response = new ArrayList<>();
    response.add("test");
    when(commandExecutor.sendCommand(realPlaylistProperties.getInfo())).thenReturn(response);
    when(playlistSongConverter.convertResponseToSongs(response)).thenReturn(mockedSongs);

    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

    playlist.removeArtist(artist);

    verify(commandExecutor).sendCommand(stringArgumentCaptor.capture());
    verify(commandExecutor, times(2))
        .sendCommand(stringArgumentCaptor.capture(), integerArgumentCaptor.capture());

    assertEquals(realPlaylistProperties.getRemoveId(), stringArgumentCaptor.getAllValues().get(1));
    assertEquals((Integer) song1.getId(), integerArgumentCaptor.getAllValues().getFirst());
    assertEquals(realPlaylistProperties.getRemoveId(), stringArgumentCaptor.getAllValues().get(2));
    assertEquals((Integer) song2.getId(), integerArgumentCaptor.getAllValues().get(1));
    assertEquals(PlaylistChangeEvent.Event.SONG_DELETED, changeEvent[0].getEvent());
  }

  @Test
  void testRemoveArtistByName() {
    String artist = "testArtist";

    List<MPDPlaylistSong> mockedSongs = new ArrayList<>();
    MPDPlaylistSong song1 =
        MPDPlaylistSong.builder().file("file1").title("testSong1").artistName(artist).id(1).build();

    MPDPlaylistSong song2 =
        MPDPlaylistSong.builder().file("file2").title("testSong1").artistName(artist).id(2).build();

    MPDPlaylistSong song3 =
        MPDPlaylistSong.builder()
            .file("file3")
            .title("testSong1")
            .artistName("bogus")
            .albumName("bogus")
            .build();

    mockedSongs.add(song1);
    mockedSongs.add(song2);
    mockedSongs.add(song3);

    List<String> response = new ArrayList<>();
    response.add("test");
    when(commandExecutor.sendCommand(realPlaylistProperties.getInfo())).thenReturn(response);
    when(playlistSongConverter.convertResponseToSongs(response)).thenReturn(mockedSongs);

    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

    playlist.removeArtist(artist);

    verify(commandExecutor).sendCommand(stringArgumentCaptor.capture());
    verify(commandExecutor, times(2))
        .sendCommand(stringArgumentCaptor.capture(), integerArgumentCaptor.capture());

    assertEquals(realPlaylistProperties.getRemoveId(), stringArgumentCaptor.getAllValues().get(1));
    assertEquals((Integer) song1.getId(), integerArgumentCaptor.getAllValues().getFirst());
    assertEquals(realPlaylistProperties.getRemoveId(), stringArgumentCaptor.getAllValues().get(2));
    assertEquals((Integer) song2.getId(), integerArgumentCaptor.getAllValues().get(1));
    assertEquals(PlaylistChangeEvent.Event.SONG_DELETED, changeEvent[0].getEvent());
  }

  @Test
  void testInsertArtist() {
    MPDArtist artist = new MPDArtist("testArtist");

    List<MPDSong> songs = new ArrayList<>();
    songs.add(MPDSong.builder().file("file1").title("testSong1").build());
    songs.add(MPDSong.builder().file("file2").title("testSong2").build());

    when(songDatabase.findArtist(artist.name())).thenReturn(songs);

    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

    playlist.insertArtist(artist);

    verify(commandExecutor, times(2))
        .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

    assertEquals(realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().getFirst());
    assertEquals("file1", stringArgumentCaptor.getAllValues().get(1));
    assertEquals(realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().get(2));
    assertEquals("file2", stringArgumentCaptor.getAllValues().get(3));
    assertEquals(PlaylistChangeEvent.Event.ARTIST_ADDED, changeEvent[0].getEvent());
  }

  @Test
  void testInsertArtistByName() {
    String artist = "testArtist";

    List<MPDSong> songs = new ArrayList<>();
    songs.add(MPDSong.builder().file("file1").title("testSong1").build());
    songs.add(MPDSong.builder().file("file2").title("testSong2").build());
    when(songDatabase.findArtist(artist)).thenReturn(songs);

    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

    playlist.insertArtist(artist);

    verify(commandExecutor, times(2))
        .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

    assertAll(
        () ->
            assertEquals(
                realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().getFirst()),
        () -> assertEquals("file1", stringArgumentCaptor.getAllValues().get(1)),
        () ->
            assertEquals(
                realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().get(2)),
        () -> assertEquals("file2", stringArgumentCaptor.getAllValues().get(3)),
        () -> assertEquals(PlaylistChangeEvent.Event.ARTIST_ADDED, changeEvent[0].getEvent()));
  }
}
