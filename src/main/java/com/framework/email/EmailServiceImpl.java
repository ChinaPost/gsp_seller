package com.framework.email;

import java.io.File;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * 邮件处理类
 * 
 * @author H
 *
 */
@Service("emailService")
public class EmailServiceImpl {

	@Autowired
	private JavaMailSender mailSender;
	@Resource
	private TaskExecutor taskExecutor;

	@Value("${mail.smtp.username}")
	private String smtpFrom;

	public void setEmailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	// 增加邮件发送任务
	public void addSendMailTask(final MimeMessage mimeMessage) {
		try {
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					mailSender.send(mimeMessage);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendEmailOfSimpleText(Email email) {
		SimpleMailMessage simpleTextMessage = new SimpleMailMessage();
		// email.setSender("${jdbc.password}");
		simpleTextMessage.setFrom(smtpFrom);
		simpleTextMessage.setTo(email.getReceivers());
		if (email.getBcc() != null) {
			simpleTextMessage.setBcc(email.getBcc());
		}
		if (email.getCc() != null) {
			simpleTextMessage.setCc(email.getCc());
		}
		simpleTextMessage.setText(email.getContext());

		simpleTextMessage.setSentDate(email.getSentDate());
		mailSender.send(simpleTextMessage);

	}

	public void sendEmailOfHtmlText(Email email) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(smtpFrom);
		helper.setValidateAddresses(true);
		if (StringUtils.isNotBlank(email.getContext())) {
			helper.setText(email.getContext(), true);
		}
		helper.setSubject(email.getSubject());
		helper.setTo(email.getReceivers());
		if (email.getCc() != null)
			helper.setCc(email.getCc());
		if (email.getBcc() != null)
			helper.setCc(email.getBcc());

		helper.setSentDate(email.getSentDate());

		addSendMailTask(message);

	}

	public void sendEmailOfFileAndText(Email email, boolean isHtmlText) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom(smtpFrom);
		// helper.setValidateAddresses(true);
		helper.setText(email.getContext(), isHtmlText);
		helper.setSubject(email.getSubject());
		helper.setTo(email.getReceivers());
		if (email.getCc() != null)
			helper.setCc(email.getCc());
		if (email.getBcc() != null)
			helper.setCc(email.getBcc());

		helper.setSentDate(email.getSentDate());

		for (File file : email.getAttachFile()) {
			FileSystemResource fileSystemResource = new FileSystemResource(file);
			helper.addAttachment(file.getName(), fileSystemResource);
		}
		addSendMailTask(message);

	}
}
