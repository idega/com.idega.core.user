package com.idega.core.user.presentation;

import com.idega.presentation.ui.IFrame;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Link;
import com.idega.presentation.Page;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Window;
import com.idega.presentation.ui.SelectionDoubleBox;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.data.GenericGroup;
import com.idega.core.user.data.User;
import java.util.List;
import java.util.Iterator;
import java.util.Enumeration;
import com.idega.util.Disposable;
import java.sql.SQLException;
import com.idega.event.IWLinkListener;
import com.idega.event.IWLinkEvent;

/**
 * Title:        User
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
 * @version 1.0
 */

public class UserGroupList extends UserTab implements Disposable, IWLinkListener {

  private Link addLink;
  private IFrame memberofFrame;

  private DropdownMenu primaryGroupField;

  private String primaryGroupFieldName;

  private Text primaryGroupText;

  public static final String PARAMETER_USER_ID = "ic_user_id";
  public static final String SESSIONADDRESS_USERGROUPS_DIRECTLY_RELATED = "ic_user_ic_group_direct_UGL";
  public static final String SESSIONADDRESS_USERGROUPS_NOT_DIRECTLY_RELATED = "ic_user_ic_group_not_direct_UGL";

  protected Text memberof;

  public UserGroupList() {
    super();
    this.setName("Groups");

  }
  public void initFieldContents() {
    addLink.setWindowToOpen(UserGroupList.UserGroupSetter.class);
    addLink.addParameter(UserGroupList.PARAMETER_USER_ID,this.getUserId());
    List userGroups = UserBusiness.getUserGroupsDirectlyRelated(this.getUserId());
    if(userGroups != null){
      Iterator iter = userGroups.iterator();
      while (iter.hasNext()) {
        GenericGroup item = (GenericGroup)iter.next();
        primaryGroupField.addMenuElement(item.getID(),item.getName());
      }
    }
    try {
      User user = new User(this.getUserId());
      int prgroupid = user.getPrimaryGroupID();
      fieldValues.put(primaryGroupFieldName, (prgroupid != -1)?Integer.toString(prgroupid):"");
    }
    catch (SQLException ex) {

    }
    updateFieldsDisplayStatus();
  }
  public void updateFieldsDisplayStatus() {
    primaryGroupField.setSelectedElement((String)fieldValues.get(primaryGroupFieldName));
  }
  public void initializeFields() {
    memberofFrame = new IFrame("ic_user_memberof_ic_group",UserGroupList.GroupList.class);
    memberofFrame.setHeight(280);
    memberofFrame.setWidth(370);
    memberofFrame.setScrolling(IFrame.SCROLLING_YES);

    primaryGroupField = new DropdownMenu(primaryGroupFieldName);
    primaryGroupField.keepStatusOnAction();

    addLink = new Link("  Add  ");
  }

  public void actionPerformed(IWLinkEvent e){
    this.collect(e.getIWContext());
  }

  public void initializeTexts() {
    memberof = this.getTextObject();
    memberof.setText("Member of:");

    primaryGroupText = this.getTextObject();
    primaryGroupText.setText("Primarygroup");
  }
  public boolean store(IWContext iwc) {
    try {
      String pr = (String)this.fieldValues.get(this.primaryGroupFieldName);
      UserBusiness.setPermissionGroup(new User(this.getUserId()), ("".equals(pr))?null:new Integer(pr));
      return true;
    }
    catch (SQLException ex) {
      return false;
    }
  }
  public void lineUpFields() {
    this.resize(1,4);

    Table prTable = new Table(2,1);

    prTable.add(this.primaryGroupText,1,1);
    prTable.add(this.primaryGroupField,2,1);
    prTable.setHeight(1,"30");
    prTable.setWidth(1,"100");

    this.add(prTable,1,1);
    this.add(memberof,1,2);
    this.add(memberofFrame,1,3);

    this.setHeight(1,"30");
    this.setHeight(2,super.rowHeight);
    this.setHeight(4,super.rowHeight);

    this.add(addLink,1,4);
  }
  public boolean collect(IWContext iwc) {
    String prgroup = iwc.getParameter(primaryGroupFieldName);
    if(prgroup != null){
      fieldValues.put(primaryGroupFieldName,prgroup);
    }
    return true;
  }
  public void initializeFieldNames() {
    this.primaryGroupFieldName = "primary_group";
  }
  public void initializeFieldValues() {
    fieldValues.put(this.primaryGroupFieldName,"");
    this.updateFieldsDisplayStatus();
  }

  public void dispose(IWContext iwc){
    iwc.removeSessionAttribute(UserGroupList.SESSIONADDRESS_USERGROUPS_DIRECTLY_RELATED);
    iwc.removeSessionAttribute(UserGroupList.SESSIONADDRESS_USERGROUPS_NOT_DIRECTLY_RELATED);
  }

  public void main(IWContext iwc) throws Exception {
    primaryGroupField.removeElements();
    primaryGroupField.addSeparator();
    List userGroups = UserBusiness.getUserGroupsDirectlyRelated(this.getUserId());
    if(userGroups != null){
      Iterator iter = userGroups.iterator();
      while (iter.hasNext()) {
        GenericGroup item = (GenericGroup)iter.next();
        primaryGroupField.addMenuElement(item.getID(),item.getName());
      }
    }
    primaryGroupField.setSelectedElement((String)fieldValues.get(primaryGroupFieldName));


    Object obj = UserBusiness.getUserGroupsDirectlyRelated(this.getUserId());
    if(obj != null){
      iwc.setSessionAttribute(UserGroupList.SESSIONADDRESS_USERGROUPS_DIRECTLY_RELATED,obj);
    }else{
      iwc.removeSessionAttribute(UserGroupList.SESSIONADDRESS_USERGROUPS_DIRECTLY_RELATED);
    }

    Object ob = UserBusiness.getUserGroupsNotDirectlyRelated(this.getUserId());
    if(ob != null){
      iwc.setSessionAttribute(UserGroupList.SESSIONADDRESS_USERGROUPS_NOT_DIRECTLY_RELATED,ob);
    }else{
      iwc.removeSessionAttribute(UserGroupList.SESSIONADDRESS_USERGROUPS_NOT_DIRECTLY_RELATED);
    }
  }


  public static class GroupList extends Page {

    private List groups = null;

    public GroupList(){
      super();
    }

    public Table getGroupTable(IWContext iwc){

      List direct = (List)iwc.getSessionAttribute(UserGroupList.SESSIONADDRESS_USERGROUPS_DIRECTLY_RELATED);
      List notDirect = (List)iwc.getSessionAttribute(UserGroupList.SESSIONADDRESS_USERGROUPS_NOT_DIRECTLY_RELATED);

      Table table = null;
      Iterator iter = null;
      int row = 1;
      if(direct != null && notDirect != null){
        table = new Table(5,direct.size()+notDirect.size());

        iter = direct.iterator();
        while (iter.hasNext()) {
          Object item = iter.next();
          table.add("D",1,row);
          table.add(((GenericGroup)item).getName(),3,row++);
        }

        iter = notDirect.iterator();
        while (iter.hasNext()) {
          Object item = iter.next();
          table.add("E",1,row);
          table.add(((GenericGroup)item).getName(),3,row++);
        }

      } else if(direct != null){
        table = new Table(5,direct.size());
        iter = direct.iterator();
        while (iter.hasNext()) {
          Object item = iter.next();
          table.add("D",1,row);
          table.add(((GenericGroup)item).getName(),3,row++);
        }
      }

      if(table != null){
        table.setWidth("100%");
        table.setWidth(1,"10");
        table.setWidth(2,"3");
        table.setWidth(4,"10");
        table.setWidth(5,"10");
      }



      return table;
    }

    public void main(IWContext iwc) throws Exception {
      this.getParentPage().setAllMargins(0);
      Table tb = getGroupTable(iwc);
      if(tb != null){
        this.add(tb);
      }
    }



  } // InnerClass


  public static class UserGroupSetter extends Window {

    private static final String FIELDNAME_SELECTION_DOUBLE_BOX = "related_groups";

    public UserGroupSetter(){
      super("add user to groups");
      this.setAllMargins(0);
      this.setWidth(400);
      this.setHeight(300);
      this.setBackgroundColor("#d4d0c8");
    }


    private void LineUpElements(IWContext iwc){

      Form form = new Form();

      Table frameTable = new Table(3,3);
      frameTable.setWidth("100%");
      frameTable.setHeight("100%");
      //frameTable.setBorder(1);


      SelectionDoubleBox sdb = new SelectionDoubleBox(FIELDNAME_SELECTION_DOUBLE_BOX,"Not in","In");

      SelectionBox left = sdb.getLeftBox();
      left.setHeight(8);
      left.selectAllOnSubmit();


      SelectionBox right = sdb.getRightBox();
      right.setHeight(8);
      right.selectAllOnSubmit();



      String stringUserId = iwc.getParameter(UserGroupList.PARAMETER_USER_ID);
      int userId = Integer.parseInt(stringUserId);
      form.addParameter(UserGroupList.PARAMETER_USER_ID,stringUserId);

      List directGroups = UserBusiness.getUserGroupsDirectlyRelated(userId);

      Iterator iter = null;
      if(directGroups != null){
        iter = directGroups.iterator();
        while (iter.hasNext()) {
          Object item = iter.next();
          right.addElement(Integer.toString(((GenericGroup)item).getID()),((GenericGroup)item).getName());
        }
      }
      List notDirectGroups = UserBusiness.getAllGroupsNotDirectlyRelated(userId);
      if(notDirectGroups != null){
        iter = notDirectGroups.iterator();
        while (iter.hasNext()) {
          Object item = iter.next();
          left.addElement(Integer.toString(((GenericGroup)item).getID()),((GenericGroup)item).getName());
        }
      }


      frameTable.setAlignment(2,2,"center");
      frameTable.add("UserId: "+userId,2,1);
      frameTable.add(sdb,2,2);
      frameTable.add(new SubmitButton("  Save  ","save","true"),2,3);
      frameTable.setAlignment(2,3,"right");
      form.add(frameTable);
      this.add(form);
    }

    public void main(IWContext iwc) throws Exception {


      String save = iwc.getParameter("save");
      if(save != null){
        String stringUserId = iwc.getParameter(UserGroupList.PARAMETER_USER_ID);
        int userId = Integer.parseInt(stringUserId);

        String[] related = iwc.getParameterValues(UserGroupSetter.FIELDNAME_SELECTION_DOUBLE_BOX);

        User user = new User(userId);
        List currentRelationShip = UserBusiness.getUserGroupsDirectlyRelated(user);


        if(related != null){

          if(currentRelationShip != null){
            for (int i = 0; i < related.length; i++) {
              int id = Integer.parseInt(related[i]);
              GenericGroup gr = new GenericGroup(id);
              if(!currentRelationShip.remove(gr)){
                //user.addTo(gr);
                gr.addUser(user);
              }
            }

            Iterator iter = currentRelationShip.iterator();
            while (iter.hasNext()) {
              Object item = iter.next();
              //user.removeFrom((GenericGroup)item);
              ((GenericGroup)item).removeUser(user);
            }

          } else{
            for (int i = 0; i < related.length; i++) {
              //user.addTo(GenericGroup.class,Integer.parseInt(related[i]));
              //new GenericGroup(Integer.parseInt(related[i])).addUser(user);
              GenericGroup.addUser(Integer.parseInt(related[i]),user);
            }
          }

        }else if (currentRelationShip != null){
            Iterator iter = currentRelationShip.iterator();
            while (iter.hasNext()) {
              Object item = iter.next();
              ((GenericGroup)item).removeUser(user);
            }
          }

        this.close();
        this.setParentToReload();
      } else {
        LineUpElements(iwc);
      }

/*
      Enumeration enum = iwc.getParameterNames();
       System.err.println("--------------------------------------------------");
      if(enum != null){
        while (enum.hasMoreElements()) {
          Object item = enum.nextElement();
          if(item.equals("save")){
            this.close();
          }
          String val[] = iwc.getParameterValues((String)item);
          System.err.print(item+" = ");
          if(val != null){
            for (int i = 0; i < val.length; i++) {
              System.err.print(val[i]+", ");
            }
          }
          System.err.println();
        }
      }
*/
    }

  } // InnerClass



} // Class

