package com.idega.core.user.presentation;

import com.idega.jmodule.object.interfaceobject.IFrame;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.textObject.Link;
import com.idega.jmodule.object.Page;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.textObject.Text;
import com.idega.jmodule.object.interfaceobject.Window;
import com.idega.jmodule.object.interfaceobject.SelectionDoubleBox;
import com.idega.jmodule.object.interfaceobject.SelectionBox;
import com.idega.jmodule.object.interfaceobject.SubmitButton;
import com.idega.jmodule.object.interfaceobject.Form;
import com.idega.jmodule.object.interfaceobject.TextArea;
import com.idega.jmodule.object.interfaceobject.TextInput;
import com.idega.core.business.UserGroupBusiness;
import com.idega.core.data.GenericGroup;
import java.util.List;
import java.util.Iterator;
import java.util.Enumeration;
import com.idega.util.Disposable;

/**
 * Title:        User
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
 * @version 1.0
 */

public class GeneralGroupInfoTab extends UserGroupTab implements Disposable{


  private TextInput nameField;
  private TextArea descriptionField;

  private Text nameText;
  private Text descriptionText;

  private String nameFieldName;
  private String descriptionFieldName;

  private Link addLink;
  private IFrame memberofFrame;
  public static final String PARAMETER_GROUP_ID = "ic_group_id";
  public static final String SESSIONADDRESS_GROUPS_DIRECTLY_RELATED = "ic_group_ic_group_direct_GGIT";
  public static final String SESSIONADDRESS_GROUPS_NOT_DIRECTLY_RELATED = "ic_group_ic_group_not_direct_GGIT";

  protected Text memberof;

  public GeneralGroupInfoTab() {
    super();
    this.setName("General");
  }

  public void initFieldContents() {
    addLink.setWindowToOpen(GeneralGroupInfoTab.GroupGroupSetter.class);
    addLink.addParameter(GeneralGroupInfoTab.PARAMETER_GROUP_ID,this.getGroupId());

     try{
      GenericGroup group = new GenericGroup(getGroupId());

      fieldValues.put(this.nameFieldName,(group.getName() != null) ? group.getName():"" );
      fieldValues.put(this.descriptionFieldName,(group.getDescription() != null) ? group.getDescription():"" );
      this.updateFieldsDisplayStatus();

    }catch(Exception e){
      System.err.println("GeneralGroupInfoTab error initFieldContents, GroupId : " + getGroupId());
    }


  }
  public void updateFieldsDisplayStatus() {
    nameField.setContent((String)fieldValues.get(this.nameFieldName));

    descriptionField.setContent((String)fieldValues.get(this.descriptionFieldName));
  }
  public void initializeFields() {


    nameField = new TextInput(nameFieldName);
    nameField.setLength(26);

    descriptionField = new TextArea(descriptionFieldName);
    descriptionField.setHeight(5);
    descriptionField.setWidth(43);
    descriptionField.setWrap(true);

    memberofFrame = new IFrame("ic_user_memberof_ic_group",GeneralGroupInfoTab.GroupList.class);
    memberofFrame.setHeight(150);
    memberofFrame.setWidth(367);
    memberofFrame.setScrolling(IFrame.SCROLLING_YES);

    addLink = new Link("  Add  ");

  }
  public void initializeTexts() {

    nameText = this.getTextObject();
    nameText.setText("Name:");

    descriptionText = getTextObject();
    descriptionText.setText("Description:");

    memberof = this.getTextObject();
    memberof.setText("Member of:");


  }
  public boolean store(ModuleInfo modinfo) {
    try{
      if(getGroupId() > -1){

        GenericGroup group = new GenericGroup(getGroupId());
        group.setName((String)fieldValues.get(this.nameFieldName));
        group.setDescription((String)fieldValues.get(this.descriptionFieldName));

        group.update();

      }
    }catch(Exception e){
      //return false;
      e.printStackTrace(System.err);
      throw new RuntimeException("update group exception");
    }
    return true;
  }
  public void lineUpFields() {
    this.resize(1,5);
    this.setCellpadding(0);
    this.setCellspacing(0);

    Table nameTable = new Table(2,1);
    nameTable.setCellpadding(0);
    nameTable.setCellspacing(0);
    nameTable.setWidth(1,1,"50");
    nameTable.add(this.nameText,1,1);
    nameTable.add(this.nameField,2,1);
    this.add(nameTable,1,1);

    Table descriptionTable = new Table(1,2);
    descriptionTable.setCellpadding(0);
    descriptionTable.setCellspacing(0);
    descriptionTable.setHeight(1,rowHeight);
    descriptionTable.add(descriptionText,1,1);
    descriptionTable.add(this.descriptionField,1,2);
    this.add(descriptionTable,1,2);

    this.add(memberof,1,3);
    this.add(memberofFrame,1,4);

    this.setHeight(3,"30");
    this.setHeight(1,super.rowHeight);
    this.setHeight(5,super.rowHeight);

    this.add(addLink,1,5);
  }

  public boolean collect(ModuleInfo modinfo) {
    if(modinfo != null){

      String gname = modinfo.getParameter(this.nameFieldName);
      String desc = modinfo.getParameter(this.descriptionFieldName);

      if(gname != null){
        fieldValues.put(this.nameFieldName,gname);
      }

      if(desc != null){
        fieldValues.put(this.descriptionFieldName,desc);
      }

      this.updateFieldsDisplayStatus();

      return true;
    }
    return false;

  }
  public void initializeFieldNames() {
    descriptionFieldName = "UM_group_desc";
    nameFieldName = "UM_group_name";
  }
  public void initializeFieldValues() {
    fieldValues.put(this.nameFieldName,"");
    fieldValues.put(this.descriptionFieldName,"");

    this.updateFieldsDisplayStatus();
  }

  public void dispose(ModuleInfo modinfo){
    modinfo.removeSessionAttribute(GeneralGroupInfoTab.SESSIONADDRESS_GROUPS_DIRECTLY_RELATED);
    modinfo.removeSessionAttribute(GeneralGroupInfoTab.SESSIONADDRESS_GROUPS_NOT_DIRECTLY_RELATED);
  }

  public void main(ModuleInfo modinfo) throws Exception {
    Object obj = UserGroupBusiness.getGroupsContainingDirectlyRelated(this.getGroupId());
    if(obj != null){
      modinfo.setSessionAttribute(GeneralGroupInfoTab.SESSIONADDRESS_GROUPS_DIRECTLY_RELATED,obj);
    }else{
      modinfo.removeSessionAttribute(GeneralGroupInfoTab.SESSIONADDRESS_GROUPS_DIRECTLY_RELATED);
    }

    Object ob = UserGroupBusiness.getGroupsContainingNotDirectlyRelated(this.getGroupId());
    if(ob != null){
      modinfo.setSessionAttribute(GeneralGroupInfoTab.SESSIONADDRESS_GROUPS_NOT_DIRECTLY_RELATED,ob);
    }else{
      modinfo.removeSessionAttribute(GeneralGroupInfoTab.SESSIONADDRESS_GROUPS_NOT_DIRECTLY_RELATED);
    }
  }


  public static class GroupList extends Page {

    private List groups = null;

    public GroupList(){
      super();
    }

    public Table getGroupTable(ModuleInfo modinfo){

      List direct = (List)modinfo.getSessionAttribute(GeneralGroupInfoTab.SESSIONADDRESS_GROUPS_DIRECTLY_RELATED);
      List notDirect = (List)modinfo.getSessionAttribute(GeneralGroupInfoTab.SESSIONADDRESS_GROUPS_NOT_DIRECTLY_RELATED);

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

    public void main(ModuleInfo modinfo) throws Exception {
      this.getParentPage().setAllMargins(0);
      Table tb = getGroupTable(modinfo);
      if(tb != null){
        this.add(tb);
      }
    }



  } // InnerClass


  public static class GroupGroupSetter extends Window {

    private static final String FIELDNAME_SELECTION_DOUBLE_BOX = "related_groups";

    public GroupGroupSetter(){
      super("add groups to groups");
      this.setAllMargins(0);
      this.setWidth(400);
      this.setHeight(300);
      this.setBackgroundColor("#d4d0c8");
    }


    private void LineUpElements(ModuleInfo modinfo){

      Form form = new Form();

      Table frameTable = new Table(3,3);
      frameTable.setWidth("100%");
      frameTable.setHeight("100%");
      //frameTable.setBorder(1);


      SelectionDoubleBox sdb = new SelectionDoubleBox(GroupGroupSetter.FIELDNAME_SELECTION_DOUBLE_BOX,"Not in","In");

      SelectionBox left = sdb.getLeftBox();
      left.setHeight(8);
      left.selectAllOnSubmit();


      SelectionBox right = sdb.getRightBox();
      right.setHeight(8);
      right.selectAllOnSubmit();



      String stringGroupId = modinfo.getParameter(GeneralGroupInfoTab.PARAMETER_GROUP_ID);
      int groupId = Integer.parseInt(stringGroupId);
      form.addParameter(GeneralGroupInfoTab.PARAMETER_GROUP_ID,stringGroupId);

      List directGroups = UserGroupBusiness.getGroupsContainingDirectlyRelated(groupId);

      Iterator iter = null;
      if(directGroups != null){
        iter = directGroups.iterator();
        while (iter.hasNext()) {
          Object item = iter.next();
          right.addElement(Integer.toString(((GenericGroup)item).getID()),((GenericGroup)item).getName());
        }
      }
      List notDirectGroups = UserGroupBusiness.getAllGroupsNotDirectlyRelated(groupId);
      if(notDirectGroups != null){
        iter = notDirectGroups.iterator();
        while (iter.hasNext()) {
          Object item = iter.next();
          left.addElement(Integer.toString(((GenericGroup)item).getID()),((GenericGroup)item).getName());
        }
      }

      //left.addSeparator();
      //right.addSeparator();

      frameTable.setAlignment(2,2,"center");
      frameTable.add("GroupId: "+groupId,2,1);
      frameTable.add(sdb,2,2);
      frameTable.add(new SubmitButton("  Save  ","save","true"),2,3);
      frameTable.setAlignment(2,3,"right");
      form.add(frameTable);
      this.add(form);
    }

    public void main(ModuleInfo modinfo) throws Exception {


      String save = modinfo.getParameter("save");
      if(save != null){
        String stringGroupId = modinfo.getParameter(GeneralGroupInfoTab.PARAMETER_GROUP_ID);
        int groupId = Integer.parseInt(stringGroupId);

        String[] related = modinfo.getParameterValues(GroupGroupSetter.FIELDNAME_SELECTION_DOUBLE_BOX);

        GenericGroup group = new GenericGroup(groupId);
        List currentRelationShip = group.getListOfAllGroupsContainingThis();


        if(related != null){

          if(currentRelationShip != null){
            for (int i = 0; i < related.length; i++) {
              int id = Integer.parseInt(related[i]);
              GenericGroup gr = new GenericGroup(id);
              if(!currentRelationShip.remove(gr)){
                gr.addGroup(group);
              }
            }

            Iterator iter = currentRelationShip.iterator();
            while (iter.hasNext()) {
              Object item = iter.next();
              ((GenericGroup)item).removeGroup(group);
            }

          } else{
            for (int i = 0; i < related.length; i++) {
              new GenericGroup(Integer.parseInt(related[i])).addGroup(group);
            }
          }

        }else if (currentRelationShip != null){
            Iterator iter = currentRelationShip.iterator();
            while (iter.hasNext()) {
              Object item = iter.next();
              ((GenericGroup)item).removeGroup(group);
            }
          }

        this.close();
        this.setParentToReload();
      } else {
        LineUpElements(modinfo);
      }

/*
      Enumeration enum = modinfo.getParameterNames();
       System.err.println("--------------------------------------------------");
      if(enum != null){
        while (enum.hasMoreElements()) {
          Object item = enum.nextElement();
          if(item.equals("save")){
            this.close();
          }
          String val[] = modinfo.getParameterValues((String)item);
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



}