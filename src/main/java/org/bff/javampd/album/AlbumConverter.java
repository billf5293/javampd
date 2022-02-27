package org.bff.javampd.album;

import java.util.Collection;
import java.util.List;

/**
 * Converts the MPD response to a {@link MPDAlbum}
 *
 * @author bill
 */
@FunctionalInterface
public interface AlbumConverter {
    /**
     * Converts the response from the MPD server into a {@link MPDAlbum} object.
     *
     * @param list the response from the MPD server
     * @return a {@link MPDAlbum} object
     */
    Collection<MPDAlbum> convertResponseToAlbum(List<String> list);
}
