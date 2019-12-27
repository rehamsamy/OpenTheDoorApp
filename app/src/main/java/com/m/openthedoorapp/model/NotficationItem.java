package com.m.openthedoorapp.model;


import com.google.gson.annotations.SerializedName;


public class NotficationItem {

	@SerializedName("user_notf_ar")
	private String userNotfAr;

	@SerializedName("user_notf_link")
	private String userNotfLink;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("user_notf_en")
	private String userNotfEn;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	public void setUserNotfAr(String userNotfAr){
		this.userNotfAr = userNotfAr;
	}

	public String getUserNotfAr(){
		return userNotfAr;
	}

	public void setUserNotfLink(String userNotfLink){
		this.userNotfLink = userNotfLink;
	}

	public String getUserNotfLink(){
		return userNotfLink;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setUserNotfEn(String userNotfEn){
		this.userNotfEn = userNotfEn;
	}

	public String getUserNotfEn(){
		return userNotfEn;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"NotficationItem{" + 
			"user_notf_ar = '" + userNotfAr + '\'' + 
			",user_notf_link = '" + userNotfLink + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",user_id = '" + userId + '\'' + 
			",user_notf_en = '" + userNotfEn + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}