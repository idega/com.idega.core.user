package com.idega.core.user.presentation;

import com.idega.jmodule.object.interfaceobject.Window;
import com.idega.jmodule.object.interfaceobject.TextInput;
import com.idega.jmodule.object.interfaceobject.TextArea;
import com.idega.jmodule.object.interfaceobject.SubmitButton;
import com.idega.jmodule.object.interfaceobject.RadioGroup;
import com.idega.jmodule.object.interfaceobject.Form;
import com.idega.jmodule.object.interfaceobject.HiddenInput;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.interfaceobject.FramePane;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.textObject.Text;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.data.User;
import com.idega.util.idegaTimestamp;
import com.idega.core.data.GenericGroup;
import com.idega.core.accesscontrol.data.PermissionGroup;

import java.sql.SQLException;
import java.util.Vector;

/**
 * Title:        User
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
 * @version 1.0
 */

public class CreateUserGroup extends Window {

  private Text groupNameText;
  private Text descriptionText;
  private Text groupTypeText;

  private TextInput groupNameField;
  private TextArea descriptionField;
  private RadioGroup groupTypeField;

  private SubmitButton okButton;
  private SubmitButton cancelButton;

  private Vector groupType;

  private Form myForm;

  public static String okButtonParameterValue = "ok";
  public static String cancelButtonParameterValue = "cancel";
  public static String submitButtonParameterName = "submit";

  public static String groupNameFieldParameterName = "groupName";
  public static String descriptionFieldParameterName = "description";
  public static String groupTypeFieldParameterName = "group_type";

  private UserBusiness business;

  private String rowHeight = "37";

  public CreateUserGroup() {
    super();
    groupType = new  Vector();
    this.setName("idegaWeb Builder - Stofna H�p");
    this.setHeight(340);
    this.setWidth(390);
    this.setBackgroundColor("#d4d0c8");
    myForm = new Form();
    this.add(myForm);
    business = new UserBusiness();
    initializeTexts();
    initializeFields();
    init();
    lineUpElements();
  }

  protected void initializeTexts(){

    groupNameText = new Text("Group name:");
    descriptionText = new Text("Description : ");
    groupTypeText = new Text("Type: ");
  }

  protected void initializeFields(){
    groupNameField = new TextInput(groupNameFieldParameterName);
    groupNameField.setLength(20);

    descriptionField = new TextArea(descriptionFieldParameterName);
    descriptionField.setHeight(3);
    descriptionField.setWidth(20);

    groupTypeField = new RadioGroup(groupTypeFieldParameterName);
    groupTypeField.setWidth(1);

    okButton = new SubmitButton("     OK     ",submitButtonParameterName,okButtonParameterValue);
    cancelButton = new SubmitButton(" Cancel ",submitButtonParameterName,cancelButtonParameterValue);

  }

  public void init(){
    this.addGroupType(GenericGroup.class);
    this.addGroupType(PermissionGroup.class);
  }



  public void addGroupType(Class genricGroup){
    groupType.add(genricGroup);
  }


  public void lineUpElements(){

    Table frameTable = new Table(1,3);
    frameTable.setAlignment("center");
    frameTable.setVerticalAlignment("middle");
    frameTable.setCellpadding(0);
    frameTable.setCellspacing(0);

    // nameTable begin
    Table nameTable = new Table(1,4);
    nameTable.setCellpadding(0);
    nameTable.setCellspacing(0);
    nameTable.setHeight(1,rowHeight);
    nameTable.setHeight(2,rowHeight);

    nameTable.add(groupNameText,1,1);
    nameTable.add(groupNameField,1,2);
    nameTable.add(descriptionText,1,3);
    nameTable.add(descriptionField,1,4);
    // nameTable end

    // Property begin
    int size = groupType.size();
    if(size > 1){
      Table propertyTable = new Table(2,1);
      propertyTable.setCellpadding(0);
      propertyTable.setCellspacing(0);
      propertyTable.setHeight(1,rowHeight);

      FramePane frPane = new FramePane("Type");


      for (int i = 0; i < groupType.size(); i++){
        String value = ((GenericGroup)GenericGroup.getStaticInstance((Class)groupType.get(i))).getGroupTypeValue();
        String text = value.substring(1);
        text = value.substring(0,1).toUpperCase() + text;

        if(i==0){
          groupTypeField.addRadioButton(value,new Text(text),true);
        }else{
          groupTypeField.addRadioButton(value,new Text(text));
        }
      }

      frPane.add(groupTypeField);
      frPane.setWidth(200);
      propertyTable.add(frPane,1,1);
      frameTable.add(propertyTable,1,2);
    }else if (size == 1){
      frameTable.add(new HiddenInput(((GenericGroup)GenericGroup.getStaticInstance((Class)groupType.get(0))).getGroupTypeValue()));
    }else{
      frameTable.add(new HiddenInput(groupTypeFieldParameterName,GenericGroup.getStaticInstance().getGroupTypeValue()));
    }
    // Property end



    // buttonTable begin
    Table buttonTable = new Table(3,1);
    buttonTable.setCellpadding(0);
    buttonTable.setCellspacing(0);
    buttonTable.setHeight(1,rowHeight);
    buttonTable.setWidth(2,"5");

    buttonTable.add(okButton,1,1);
    buttonTable.add(cancelButton,3,1);
    // buttonTable end


    frameTable.add(nameTable,1,1);


    frameTable.add(buttonTable,1,3);
    frameTable.setAlignment(1,3,"right");

    myForm.add(frameTable);

  }



  public void commitCreation(ModuleInfo modinfo) throws Exception{

    GenericGroup newGroup;

    String name = modinfo.getParameter(this.groupNameFieldParameterName);
    String description = modinfo.getParameter(this.descriptionFieldParameterName);
    String type = modinfo.getParameter(this.groupTypeFieldParameterName);

    if(type == null){
      throw new Exception("no group_type selected");
    }

    newGroup = new GenericGroup();
    newGroup.setName(name);
    newGroup.setDescription(description);
    newGroup.setGroupType(type);

    newGroup.insert();

  }


  public void main(ModuleInfo modinfo) throws Exception {
    String submit = modinfo.getParameter("submit");
    if(submit != null){
      if(submit.equals("ok")){
        this.commitCreation(modinfo);
        this.close();
        this.setParentToReload();
      }else if(submit.equals("cancel")){
        this.close();
      }
    }
  }


}