package com.framework.email;

import java.io.File;
import java.util.Date;

/**
* @author charles E-mail:csp6353963@163.com
* @version 2016年6月2日
*/
public class Email {
	private String sender;//发送者
	private String[] receivers;//接收者
	private String[] cc;//抄送人
	private String[] bcc;//密送
	private String context;//发送内容
	private String subject;//主题
	private File[] attachFile;//附件
	private Date sentDate;
	
	public Email(){
		this.sentDate = new Date();
	}
	
	public Email(String receiver,String subject,String context){
		this.sentDate = new Date();
		this.receivers = new String[]{receiver};
		this.subject = subject;
		this.context = context;
	}
	
	public String[] getBcc() {
		return bcc;
	}
	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String[] getReceivers() {
		return receivers;
	}
	public void setReceivers(String[] receivers) {
		this.receivers = receivers;
	}
	public String[] getCc() {
		return cc;
	}
	public void setCc(String[] cc) {
		this.cc = cc;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public File[] getAttachFile() {
		return attachFile;
	}
	public void setAttachFile(File[] attachFile) {
		this.attachFile = attachFile;
	}
	public Date getSentDate() {
		return sentDate;
	}
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}
	
}
