package com.idega.core.user.data;

import com.idega.core.data.GenericGroup;
import java.sql.SQLException;

/**
 * Title:        User
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
 * @version 1.0
 */

public class UserGroupRepresentative extends GenericGroup {

  public UserGroupRepresentative() {
    super();
  }

  public UserGroupRepresentative(int id) throws SQLException  {
    super(id);
  }


  public String getGroupTypeValue(){
    return "ic_user_representive";
  }


  public static String getClassName(){
    return UserGroupRepresentative.class.getName();
  }

  protected boolean identicalGroupExistsInDatabase() throws Exception {
    return false;
  }

}