package org.bff.javampd.database;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bff.javampd.command.CommandExecutor;

/**
 * Implementation of {@link TagLister} for the MPD database
 *
 * @author Bill
 */
public class MPDTagLister implements TagLister {
  private final DatabaseProperties databaseProperties;
  private final CommandExecutor commandExecutor;

  @Inject
  public MPDTagLister(DatabaseProperties databaseProperties, CommandExecutor commandExecutor) {
    this.databaseProperties = databaseProperties;
    this.commandExecutor = commandExecutor;
  }

  @Override
  public List<String> listInfo(ListInfoType... types) {
    List<String> returnList = new ArrayList<>();
    List<String> list = commandExecutor.sendCommand(databaseProperties.getListInfo());

    for (String s : list) {
      for (ListInfoType type : types) {
        if (s.startsWith(type.getPrefix())) {
          returnList.add(s.substring(type.getPrefix().length()).trim());
        }
      }
    }

    return returnList;
  }

  @Override
  public List<String> list(ListType listType) {
    return list(listType, new ArrayList<>());
  }

  @Override
  public List<String> list(ListType listType, List<String> params, GroupType... groupTypes) {
    addGroupParams(params, groupTypes);

    return commandExecutor.sendCommand(
        databaseProperties.getList(), generateParamList(listType, params));
  }

  @Override
  public List<String> list(ListType listType, GroupType... groupTypes) {
    List<String> params = new ArrayList<>();
    addGroupParams(params, groupTypes);

    return commandExecutor.sendCommand(
        databaseProperties.getList(), generateParamList(listType, params));
  }

  private void addGroupParams(List<String> params, GroupType... groupTypes) {
    if (groupTypes.length > 0) {
      Arrays.stream(groupTypes)
          .forEach(
              group -> {
                params.add(databaseProperties.getGroup());
                params.add(group.getType());
              });
    }
  }

  private static String[] generateParamList(ListType listType, List<String> params) {
    String[] paramList;

    var i = 0;

    if (params != null) {
      paramList = new String[params.size() + 1];
    } else {
      paramList = new String[1];
    }

    paramList[i++] = listType.getType();

    if (params != null) {
      for (String s : params) {
        paramList[i++] = s;
      }
    }

    return paramList;
  }
}
