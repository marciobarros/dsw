package br.unirio.dsw.compartilhador.api.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/*import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import br.unirio.dsw.service.configuration.Configuration;*/

/**
 * Classe responsável pelo envio de e-mails
 * 
 * @author marciobarros
 */
@Service("emailService")
public class EmailService
{
	/**
	 * Modelo padronizado dos e-mails transacionais
	 */
	private static final String EMAIL_PADRAO =
		"<table style='width:100%;background-color:#F7F7F7;text-align:center;' cellspacing='0' cellpadding='0' border='0' align='center'>" +
		"<tbody>" +
		"<tr>" +
		"<td style='padding-top:8px;padding-bottom:8px;'>" +
		"    <table style='width:100%;max-width:600px;background-color:#FFFFFF;text-align:left;' cellspacing='0' cellpadding='0' border='0' align='center'>" +
		"    <tbody>" +
		"    <tr>" +
		"    <td>" +
		"        <table style='width:100%;' cellspacing='0' cellpadding='0' border='0'>" +
		"        <tbody>" +
		"        <tr>" +
		"        <td style='padding:0px 0px 2px 0px;background-color:#86B13B;'>" +
		"        </td>" +
		"        </tr>" +
		"        <tr>" +
		"        <td>" +
		"            <br>" + 
		"            <p>Olá <b>@1.</b></p>" +
		"            @2" +
		"            <p>Atenciosamente,</p>" +
		"            <p>Equipe UNIRIO</p>" + 
		"            <br>" + 
		"        </td>" +
		"        </tr>" +
		"        <tr>" +
		"        <td style='padding:0px 0px 2px 0px;background-color:#86B13B;'>" +
		"        </td>" +
		"        </tr>" +
		"        </tbody>" +
		"        </table>" +
		"    </td>" +
		"    </tr>" +
		"    </tbody>" +
		"    </table>" +
		"</td>" +
		"</tr>" +
		"</tbody>" +
		"</table>";
	
	/**
	 * Conta dos desenvolvedores
	 */
//	@Value("${email.developer.account}")
//	private String developerAccount;
	
	/**
	 * E-mail de origem das mensagens enviadas pela plataforma
	 */
//	@Value("${email.source.account}")
//	private String emailSource;

	/**
	 * Prefixo para envio de e-mails, vindo do arquivo de configuração da aplicação
	 */
//	@Value("${email.sendgrid.key}")
//	private String sendGridKey;

	/**
	 * Prefixo para envio de e-mails, vindo do arquivo de configuração da aplicação
	 */
//	@Value("${email.mail.notice}")
//	private String mailNotice;
	
	@Value("${mail.smtp.host}")
	private String smtpHostname; 

	@Value("${mail.smtp.port}")
	private String smtpPort; 
	
	@Value("${mail.smtp.username}")
	private String smtpUsername;
	
	@Value("${mail.smtp.password}")
	private String smtpPassword;

	@Value("${mail.smtp.system.account}")
	private String smtpSystemAccount;

	@Value("${mail.smtp.developer.account}")
	private String smtpDeveloperAccount;

	/**
	 * Impede a instanciação direta do serviço de envio de e-mails
	 */
	private EmailService()
	{
	}

	/**
	 * Envia um e-mail referente à plataforma
	 */
	public boolean sendToUser(String destinationName, String destinationEmail, String title, String contents)
	{
		String html = EMAIL_PADRAO;
		html = html.replace("@1", destinationName);
		html = html.replace("@2", contents);
		return send(destinationEmail, title, html);
	}

	/**
	 * Envia um e-mail para os desenvolvedores do sistema
	 */
	public boolean sendToDevelopers(String title, String contents)
	{
		return send(smtpDeveloperAccount, title, contents);
	}

	/**
	 * Envia e-mail utilizando modelo assincrono pela API do sendgrid
	 */
	private boolean send(String email, String title, String contents)
	{
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", smtpHostname);
		prop.put("mail.smtp.port", smtpPort);
		prop.put("mail.smtp.ssl.trust", smtpHostname);

		Session session = Session.getInstance(prop, new Authenticator() {
		    @Override
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication(smtpUsername, smtpPassword);
		    }
		});

		try
		{
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(smtpSystemAccount));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(title);
			 
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(contents, "text/html; charset=utf-8");
			 
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);
			message.setContent(multipart);
			Transport.send(message);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}