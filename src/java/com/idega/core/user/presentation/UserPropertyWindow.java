package com.idega.core.user.presentation;

import com.idega.presentation.TabbedPropertyWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.TabbedPropertyPanel;
import com.idega.presentation.PresentationObject;

/**
 * Title:        User
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author <a href="mailto:gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
 * @version 1.0
 */

public class UserPropertyWindow extends TabbedPropertyWindow{

  public static final String PARAMETERSTRING_USER_ID = "ic_user_id";

  public UserPropertyWindow(){
    super();
    this.setBackgroundColor("#d4d0c8");
  }

  public String getSessionAddressString(){
    return "ic_user_property_window";
  }

  public void initializePanel( IWContext iwc, TabbedPropertyPanel panel){
    GeneralUserInfoTab genTab = new GeneralUserInfoTab();

    panel.addTab(genTab, 0, iwc);
    panel.addTab(new AddressInfoTab(), 1, iwc);
    panel.addTab(new UserPhoneTab(), 2, iwc);
    panel.addTab(new UserGroupList(),3,iwc);

    UserLoginTab ult = new UserLoginTab();
    ult.displayLoginInfoSettings();
    panel.addTab(ult,4,iwc);


  }

  public void main(IWContext iwc) throws Exception {
    String id = iwc.getParameter(UserPropertyWindow.PARAMETERSTRING_USER_ID);
    if(id != null){
      int newId = Integer.parseInt(id);
      PresentationObject[] obj = this.getAddedTabs();
      for (int i = 0; i < obj.length; i++) {
        PresentationObject mo = obj[i];
        if( mo instanceof UserTab && ((UserTab)mo).getUserId() != newId){
          ((UserTab)mo).setUserID(newId);
        }
      }
    }
  }

}