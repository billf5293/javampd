package org.bff.javampd.album;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.database.TagLister;
import org.bff.javampd.genre.MPDGenre;

/**
 * MPDAlbumDatabase represents an album database to a {@link org.bff.javampd.server.MPD}. To obtain
 * an instance of the class you must use the {@link
 * org.bff.javampd.database.MusicDatabase#getAlbumDatabase()} method from the {@link
 * org.bff.javampd.server.MPD} connection class.
 *
 * @author Bill
 */
public class MPDAlbumDatabase implements AlbumDatabase {

  private final TagLister tagLister;
  private final AlbumConverter albumConverter;

  private static final TagLister.GroupType[] ALBUM_TAGS = {
    TagLister.GroupType.ARTIST,
    TagLister.GroupType.DATE,
    TagLister.GroupType.GENRE,
    TagLister.GroupType.ALBUM_ARTIST,
  };

  @Inject
  public MPDAlbumDatabase(TagLister tagLister, AlbumConverter albumConverter) {
    this.tagLister = tagLister;
    this.albumConverter = albumConverter;
  }

  @Override
  public Collection<MPDAlbum> listAlbumsByAlbumArtist(MPDArtist albumArtist) {
    var p = new ArrayList<String>();
    p.add(TagLister.ListType.ALBUM_ARTIST.getType());
    p.add(albumArtist.getName());

    return convertResponseToAlbum(tagLister.list(TagLister.ListType.ALBUM, p, ALBUM_TAGS));
  }

  @Override
  public Collection<MPDAlbum> listAlbumsByArtist(MPDArtist artist) {
    List<String> list = new ArrayList<>();
    list.add(TagLister.ListType.ARTIST.getType());
    list.add(artist.getName());

    return convertResponseToAlbum(tagLister.list(TagLister.ListType.ALBUM, list, ALBUM_TAGS));
  }

  @Override
  public Collection<String> listAllAlbumNames() {
    return tagLister.list(TagLister.ListType.ALBUM).stream()
        .map(s -> s.substring(s.split(":")[0].length() + 1).trim())
        .toList();
  }

  @Override
  public Collection<MPDAlbum> listAllAlbums() {
    return convertResponseToAlbum(this.tagLister.list(TagLister.ListType.ALBUM, ALBUM_TAGS));
  }

  @Override
  public Collection<MPDAlbum> findAlbum(String albumName) {
    List<String> list = new ArrayList<>();
    list.add(TagLister.ListType.ALBUM.getType());
    list.add(albumName);

    return convertResponseToAlbum(tagLister.list(TagLister.ListType.ALBUM, list, ALBUM_TAGS));
  }

  @Override
  public Collection<MPDAlbum> listAlbumsByGenre(MPDGenre genre) {
    List<String> list = new ArrayList<>();
    list.add(TagLister.ListType.GENRE.getType());
    list.add(genre.getName());

    return convertResponseToAlbum(tagLister.list(TagLister.ListType.ALBUM, list, ALBUM_TAGS));
  }

  @Override
  public Collection<MPDAlbum> listAlbumsByYear(String year) {
    List<String> list = new ArrayList<>();
    list.add(TagLister.ListType.DATE.getType());
    list.add(year);

    return convertResponseToAlbum(tagLister.list(TagLister.ListType.ALBUM, list, ALBUM_TAGS));
  }

  private Collection<MPDAlbum> convertResponseToAlbum(List<String> response) {
    return this.albumConverter.convertResponseToAlbum(response);
  }
}
