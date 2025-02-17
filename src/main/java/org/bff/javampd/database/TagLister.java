package org.bff.javampd.database;

import java.util.List;
import lombok.Getter;

/**
 * Performs list operations against the MPD database.
 *
 * @author Bill
 */
public interface TagLister {
  @Getter
  enum ListType {
    ALBUM("album"),
    ALBUM_ARTIST("albumartist"),
    ARTIST("artist"),
    GENRE("genre"),
    DATE("date");

    private final String type;

    ListType(String type) {
      this.type = type;
    }
  }

  @Getter
  enum GroupType {
    ALBUM("album"),
    ALBUM_ARTIST("albumartist"),
    ARTIST("artist"),
    GENRE("genre"),
    DATE("date");

    private final String type;

    GroupType(String type) {
      this.type = type;
    }
  }

  @Getter
  enum ListInfoType {
    PLAYLIST("playlist:"),
    DIRECTORY("directory:"),
    FILE("file:"),
    LAST_MODIFIED("Last-Modified:");

    private final String prefix;

    ListInfoType(String prefix) {
      this.prefix = prefix;
    }
  }

  List<String> listInfo(ListInfoType... types);

  List<String> list(ListType listType);

  List<String> list(ListType listType, GroupType... groupTypes);

  List<String> list(ListType listType, List<String> params, GroupType... groupTypes);
}
