package com.idega.core.user.presentation;

import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.interfaceobject.TextInput;
import com.idega.jmodule.object.interfaceobject.TextArea;
import com.idega.jmodule.object.interfaceobject.DateInput;
import com.idega.jmodule.object.interfaceobject.DropdownMenu;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.textObject.Text;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.data.User;
import com.idega.util.datastructures.Collectable;
import com.idega.util.idegaTimestamp;
import java.util.Hashtable;
import java.util.StringTokenizer;


/**
 * Title:        User
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
 * @version 1.0
 */

public class GeneralUserInfoTab extends Table implements Collectable{

  private UserBusiness business;

  private TextInput firstNameField;
  private TextInput middleNameField;
  private TextInput lastNameField;
  private TextInput displayNameField;
  private TextArea descriptionField;
  private DateInput dateOfBirthField;
  private DropdownMenu genderField;

  private int userId = -1;
  private int userIdOld = -1;

  private String firstNameFieldName = "UMfname";
  private String middleNameFieldName = "UMmname";
  private String lastNameFieldName = "UMlname";
  private String displayNameFieldName = "UMdname";
  private String descriptionFieldName = "UMdesc";
  private String dateOfBirthFieldName = "UMdateofbirth";
  private String genderFieldName = "UMgender";



  private Text firstNameText;
  private Text middleNameText;
  private Text lastNameText;
  private Text displayNameText;
  private Text descriptionText;
  private Text dateOfBirthText;
  private Text genderText;

  private Hashtable fieldValues;


  private String columnHeight = "37";
  private int fontSize = 2;


  public GeneralUserInfoTab() {
    super();
    business = new UserBusiness();
    this.setCellpadding(0);
    this.setCellspacing(0);
    this.setName("General");
    this.setWidth("370");
    firstNameFieldName += this.getID();
    middleNameFieldName += this.getID();
    lastNameFieldName += this.getID();
    displayNameFieldName += this.getID();
    descriptionFieldName += this.getID();
    dateOfBirthFieldName += this.getID();
    genderFieldName += this.getID();
    initializeFields();
    initializeTexts();
    initializeFieldValues();
    lineUpFields();
  }

  public GeneralUserInfoTab(int userId){
    this();
    this.setUserID(userId);
  }

  public void initializeFieldValues(){

    fieldValues = new Hashtable();
    fieldValues.put(this.firstNameFieldName,"");
    fieldValues.put(this.middleNameFieldName,"");
    fieldValues.put(this.lastNameFieldName,"");
    fieldValues.put(this.displayNameFieldName,"");
    fieldValues.put(this.descriptionFieldName,"");
    fieldValues.put(this.dateOfBirthFieldName,"");
    fieldValues.put(this.genderFieldName,"");

    this.updateFieldsDisplayStatus();
  }

  public void updateFieldsDisplayStatus(){
    firstNameField.setContent((String)fieldValues.get(this.firstNameFieldName));

    middleNameField.setContent((String)fieldValues.get(this.middleNameFieldName));

    lastNameField.setContent((String)fieldValues.get(this.lastNameFieldName));

    displayNameField.setContent((String)fieldValues.get(this.displayNameFieldName));

    descriptionField.setContent((String)fieldValues.get(this.descriptionFieldName));

    StringTokenizer date = new StringTokenizer((String)fieldValues.get(this.dateOfBirthFieldName)," -");
    System.err.println("Date : "+(String)fieldValues.get(this.dateOfBirthFieldName));
    if(date.hasMoreTokens()){
      dateOfBirthField.setYear(date.nextToken());
    }
    if(date.hasMoreTokens()){
      dateOfBirthField.setDay(date.nextToken());
    }
    if(date.hasMoreTokens()){
      dateOfBirthField.setMonth(date.nextToken());
    }

    genderField.setContent((String)fieldValues.get(this.genderFieldName));
  }


  public void initializeFields(){
    firstNameField = new TextInput(firstNameFieldName);
    firstNameField.setLength(12);

    middleNameField = new TextInput(middleNameFieldName);
    middleNameField.setLength(5);

    lastNameField = new TextInput(lastNameFieldName);
    lastNameField.setLength(12);

    displayNameField = new TextInput(displayNameFieldName);
    displayNameField.setLength(12);

    descriptionField = new TextArea(descriptionFieldName);
    descriptionField.setHeight(5);
    descriptionField.setWidth(42);
    descriptionField.setWrap(true);

    dateOfBirthField = new DateInput(dateOfBirthFieldName);
    idegaTimestamp time = idegaTimestamp.RightNow();
    dateOfBirthField.setYearRange(time.getYear(),time.getYear()-100);

    genderField = new DropdownMenu(genderFieldName);
    genderField.addMenuElement("M","Male");
    genderField.addMenuElement("F", "Female");

  }

  public void initializeTexts(){
    firstNameText = new Text("First name");
    firstNameText.setFontSize(fontSize);

    middleNameText = new Text("Middle name");
    middleNameText.setFontSize(fontSize);

    lastNameText = new Text("Last name");
    lastNameText.setFontSize(fontSize);

    displayNameText = new Text("Display name");
    displayNameText.setFontSize(fontSize);

    descriptionText = new Text("Description : ");
    descriptionText.setFontSize(fontSize);

    dateOfBirthText = new Text("Date of birth : ");
    dateOfBirthText.setFontSize(fontSize);

    genderText = new Text("Gender");
    genderText.setFontSize(fontSize);

  }


  public void lineUpFields(){
    this.resize(1,3);

    //First Part (names)
    Table nameTable = new Table(4,3);
    nameTable.setWidth("100%");
    nameTable.setCellpadding(0);
    nameTable.setCellspacing(0);
    nameTable.setHeight(1,columnHeight);
    nameTable.setHeight(2,columnHeight);
    nameTable.setHeight(3,columnHeight);

    nameTable.add(firstNameText,1,1);
    nameTable.add(this.firstNameField,2,1);
    nameTable.add(middleNameText,3,1);
    nameTable.add(this.middleNameField,4,1);
    nameTable.add(lastNameText,1,2);
    nameTable.add(this.lastNameField,2,2);
    nameTable.add(displayNameText,1,3);
    nameTable.add(this.displayNameField,2,3);
    nameTable.add(genderText,3,3);
    nameTable.add(this.genderField,4,3);
    this.add(nameTable,1,1);
    //First Part ends

    //Second Part (Date of birth)
    Table dateofbirthTable = new Table(2,1);
    dateofbirthTable.setCellpadding(0);
    dateofbirthTable.setCellspacing(0);
    dateofbirthTable.setHeight(1,columnHeight);
    dateofbirthTable.add(dateOfBirthText,1,1);
    dateofbirthTable.add(this.dateOfBirthField,2,1);
    this.add(dateofbirthTable,1,2);
    //Second Part Ends

    //Third Part (description)
    Table descriptionTable = new Table(1,2);
    descriptionTable.setCellpadding(0);
    descriptionTable.setCellspacing(0);
    descriptionTable.setHeight(1,columnHeight);
    descriptionTable.add(descriptionText,1,1);
    descriptionTable.add(this.descriptionField,1,2);
    this.add(descriptionTable,1,3);
    //Third Part ends
  }


  public boolean collect(ModuleInfo modinfo){

    if(modinfo != null){
      String fname = modinfo.getParameter(this.firstNameFieldName);
      String mname = modinfo.getParameter(this.middleNameFieldName);
      String lname = modinfo.getParameter(this.lastNameFieldName);
      String dname = modinfo.getParameter(this.displayNameFieldName);
      String desc = modinfo.getParameter(this.descriptionFieldName);
      String dateofbirth = modinfo.getParameter(this.dateOfBirthFieldName);
      String gender = modinfo.getParameter(this.genderFieldName);

      if(fname != null){
        fieldValues.put(this.firstNameFieldName,fname);
      }
      if(mname != null){
        fieldValues.put(this.middleNameFieldName,mname);
      }
      if(lname != null){
        fieldValues.put(this.lastNameFieldName,lname);
      }
      if(dname != null){
        fieldValues.put(this.displayNameFieldName,dname);
      }
      if(desc != null){
        fieldValues.put(this.descriptionFieldName,desc);
      }
      if(dateofbirth != null){
        fieldValues.put(this.dateOfBirthFieldName,dateofbirth);
      }
      if(gender != null){
        fieldValues.put(this.genderFieldName,gender);
      }

      this.updateFieldsDisplayStatus();

      return true;
    }
    return false;
  }

  public boolean store(ModuleInfo modinfo){
    try{
      if(userId > -1){
        business.updateUser(userId,(String)fieldValues.get(this.firstNameFieldName),
                            (String)fieldValues.get(this.middleNameFieldName),(String)fieldValues.get(this.lastNameFieldName),
                            (String)fieldValues.get(this.displayNameFieldName),(String)fieldValues.get(this.descriptionFieldName),
                            Integer.decode((String)fieldValues.get(this.genderFieldName)),new idegaTimestamp((String)fieldValues.get(this.dateOfBirthFieldName)));
      }
    }catch(Exception e){
      return true;
    }
    return false;
  }

  public void setUserID(int id){
    userId = id;
    initFieldContents();
  }

  protected void initFieldContents(){

    try{
      User user = new User(userId);

      fieldValues.put(this.firstNameFieldName,(user.getFirstName() != null) ? user.getFirstName():"" );
      fieldValues.put(this.middleNameFieldName,(user.getMiddleName() != null) ? user.getMiddleName():"" );
      fieldValues.put(this.lastNameFieldName,(user.getLastName() != null) ? user.getLastName():"" );
      fieldValues.put(this.displayNameFieldName,(user.getDisplayName() != null) ? user.getDisplayName():"" );
      fieldValues.put(this.descriptionFieldName,(user.getDescription() != null) ? user.getDescription():"" );
      fieldValues.put(this.dateOfBirthFieldName,(user.getDateOfBirth()!= null) ? new idegaTimestamp(user.getDateOfBirth()).toSQLDateString() : "");
      fieldValues.put(this.genderFieldName,(user.getGenderID() != -1) ? Integer.toString(user.getGenderID()):"" );
      this.updateFieldsDisplayStatus();

    }catch(Exception e){
      System.err.println("GeneralUserInfoTab error initFieldContents, userId : " + userId);
    }


  }


} // Class GeneralUserInfoTab