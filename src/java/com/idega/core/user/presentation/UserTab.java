package com.idega.core.user.presentation;

import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.textObject.Text;
import com.idega.core.user.business.UserBusiness;
import com.idega.util.datastructures.Collectable;
import java.util.Hashtable;


/**
 * Title:        User
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author <a href="mailto:gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
 * @version 1.0
 */

public abstract class UserTab extends Table implements Collectable{

  private int userId = -1;

  protected String columnHeight = "37";
  protected int fontSize = 2;

  protected Text proxyText;

  protected UserBusiness business;

  protected Hashtable fieldValues;


  public UserTab() {
    super();
    business = new UserBusiness();
    init();
    this.setCellpadding(0);
    this.setCellspacing(0);
    this.setWidth("370");
    initializeFieldNames();
    initializeFields();
    initializeTexts();
    initializeFieldValues();
    lineUpFields();
  }

  public UserTab(int userId){
    this();
    this.setUserID(userId);
  }

  public void init(){}
  public abstract void initializeFieldNames();
  public abstract void initializeFieldValues();
  public abstract void updateFieldsDisplayStatus();
  public abstract void initializeFields();
  public abstract void initializeTexts();
  public abstract void lineUpFields();

  public abstract boolean collect(ModuleInfo modinfo);
  public abstract boolean store(ModuleInfo modinfo);
  public abstract void initFieldContents();

  private void initProxyText(){
    proxyText = new Text("");
    proxyText.setFontSize(fontSize);

  }

  public Text getTextObject(){
    if(proxyText == null){
      initProxyText();
    }
    return (Text)proxyText.clone();
  }

  public void setUserID(int id){
    userId = id;
    initFieldContents();
  }

  public int getUserId(){
    return userId;
  }


} // Class GeneralUserInfoTab