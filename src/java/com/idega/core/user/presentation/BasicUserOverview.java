package com.idega.core.user.presentation;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.business.UserGroupBusiness;
import com.idega.core.user.data.User;
import com.idega.data.EntityFinder;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Window;
import com.idega.user.data.UserBMPBean;

/**
 * Title:        User
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
 * @version 1.0
 */

public class BasicUserOverview extends Page {

  private static final String PARAMETER_DELETE_USER =  "delte_ic_user";
  private String styledLinkClass = "styledLinkGeneral";

  public BasicUserOverview(IWContext iwc) throws Exception {
    //this.empty();
    //this.add(this.getUsers(iwc));
  }
  public BasicUserOverview(){
    super();
  }


  public Table getUsers(IWContext iwc) throws Exception{
    List users = EntityFinder.findAllOrdered(UserBMPBean.getStaticInstance(),UserBMPBean.getColumnNameFirstName());
    Table userTable = null;
    List adminUsers = UserGroupBusiness.getUsersContainedDirectlyRelated(iwc.getAccessController().getPermissionGroupAdministrator());

    if(users != null){
      if(adminUsers == null){
        adminUsers = new Vector(0);
      }
      userTable = new Table(3,(users.size()>8)?users.size():8);
      userTable.setCellspacing(0);
      userTable.setHorizontalZebraColored("D8D4CD","C3BEB5");
      userTable.setWidth("100%");
      for (int i = 1; i <= userTable.getRows() ; i++) {
        userTable.setHeight(i,"20");
      }

      int line = 1;
      for (int i = 0; i < users.size(); i++) {
        User tempUser = (User)users.get(i);
        if(tempUser != null){

          boolean userIsSuperAdmin = iwc.getAccessController().getAdministratorUser().equals(tempUser);
          boolean delete = false;

          if(!userIsSuperAdmin){
            Link aLink = new Link(new Text(tempUser.getName()));
            //added for a new link style
            aLink.setStyleClass(this.styledLinkClass);
            aLink.setWindowToOpen(UserPropertyWindow.class);
            aLink.addParameter(UserPropertyWindow.PARAMETERSTRING_USER_ID, tempUser.getID());
            userTable.add(aLink,2,line);
            delete = true;
            line++;
          }else if(userIsSuperAdmin && iwc.isSuperAdmin() ){
//            Text aText = new Text(tempUser.getName());
//            userTable.add(aText,2,i+1);
            Link aLink = new Link(new Text(tempUser.getName()));
            //added for a new link style
            aLink.setStyleClass(this.styledLinkClass);
            aLink.setWindowToOpen(AdministratorPropertyWindow.class);
            aLink.addParameter(UserPropertyWindow.PARAMETERSTRING_USER_ID, tempUser.getID());
            userTable.add(aLink,2,line);
            delete = true;
            line++;
          }

          if(delete && !adminUsers.contains(tempUser) && !userIsSuperAdmin && iwc.getAccessController().isAdmin(iwc)){
            Link delLink = new Link(new Text("Delete"));
            delLink.setStyleClass(this.styledLinkClass);
            delLink.setWindowToOpen(ConfirmWindow.class);
            delLink.addParameter(BasicUserOverview.PARAMETER_DELETE_USER , tempUser.getID());
            userTable.add(delLink,3,line-1);
          }


        }
      }
    }

    return userTable;
  }




  public void main(IWContext iwc) throws Exception {

    this.empty();
    this.add(this.getUsers(iwc));
    this.getParentPage().setAllMargins(0);
    this.getParentPage().setBackgroundColor("#d4d0c8");
  }




  public static class ConfirmWindow extends Window{

    public Text question;
    public Form myForm;

    public SubmitButton confirm;
    public CloseButton close;
    public Table myTable = null;

    public static final String PARAMETER_CONFIRM = "confirm";

    public Vector parameters;

    public ConfirmWindow(){
      super("ConfirmWindow",300,130);
      super.setBackgroundColor("#d4d0c8");
      super.setScrollbar(false);
      super.setAllMargins(0);

      this.question = Text.getBreak();
      this.myForm = new Form();
      this.parameters = new Vector();
      this.confirm = new SubmitButton(ConfirmWindow.PARAMETER_CONFIRM,"   Yes   ");
      this.close = new CloseButton("   No    ");
      // close.setOnFocus();
      initialze();

    }


    public void lineUpElements(){
      this.myTable = new Table(2,2);
      this.myTable.setWidth("100%");
      this.myTable.setHeight("100%");
      this.myTable.setCellpadding(5);
      this.myTable.setCellspacing(5);
      //myTable.setBorder(1);


      this.myTable.mergeCells(1,1,2,1);

      this.myTable.add(this.question,1,1);

      this.myTable.add(this.confirm,1,2);

      this.myTable.add(this.close,2,2);

      this.myTable.setAlignment(1,1,"center");
//      myTable.setAlignment(2,1,"center");
      this.myTable.setAlignment(1,2,"right");
      this.myTable.setAlignment(2,2,"left");

      this.myTable.setVerticalAlignment(1,1,"middle");
      this.myTable.setVerticalAlignment(1,2,"middle");
      this.myTable.setVerticalAlignment(2,2,"middle");

      this.myTable.setHeight(2,"30%");

      this.myForm.add(this.myTable);

    }

    public void setQuestion(Text Question){
      this.question = Question;
    }


    /*abstract*/
    public void initialze(){
      this.setQuestion(new Text("Are you sure you want to delete this user?"));
      this.maintainParameter(BasicUserOverview.PARAMETER_DELETE_USER);
    }


    public void maintainParameter(String parameter){
      this.parameters.add(parameter);
    }

    /*abstract*/
    public void actionPerformed(IWContext iwc)throws Exception{
      String userDelId = iwc.getParameter(BasicUserOverview.PARAMETER_DELETE_USER);

      if(userDelId != null){
        UserBusiness.deleteUser(Integer.parseInt(userDelId));
      }
    }


    public void _main(IWContext iwc) throws Exception {
      Iterator iter = this.parameters.iterator();
      while (iter.hasNext()) {
        String item = (String)iter.next();
        this.myForm.maintainParameter(item);
      }

      String confirmThis = iwc.getParameter(ConfirmWindow.PARAMETER_CONFIRM);

      if(confirmThis != null){
        this.actionPerformed(iwc);
        this.setParentToReload();
        this.close();
      } else{
        this.empty();
        if(this.myTable == null){
          lineUpElements();
        }
        this.add(this.myForm);
      }
      super._main(iwc);
    }

  }






} //Class end
