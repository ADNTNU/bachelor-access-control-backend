package no.ntnu.gr10.bacheloraccesscontrolbackend.email;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.SendMailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Map;

import static no.ntnu.gr10.bacheloraccesscontrolbackend.security.JwtTokenProvider.PASSWORD_RESET_TOKEN_EXPIRATION_MS;

@Service
public class SendGridService {

  private final SendGrid sendGrid;

  @Value("${sendgrid.template.reset-password}")
  private String resetPasswordTemplateId;

  @Value("${sendgrid.template.invite-admin}")
  private String inviteAdminTemplateId;

  public SendGridService(@Value("${sendgrid.api.key}") String apiKey) {
    this.sendGrid = new SendGrid(apiKey);
  }

  public void sendPasswordResetEmail(String to, String resetUrl, String firstName) throws SendMailException {
    String expirationMinutes = String.valueOf(PASSWORD_RESET_TOKEN_EXPIRATION_MS / 60000);
    Mail mail = buildDynamicTemplateMail(to, resetPasswordTemplateId, Map.of(
            "firstName", firstName,
            "passwordResetUrl", resetUrl,
            "expirationMinutes", expirationMinutes
    ));
    send(mail);
  }

  public void sendInviteAdminEmail(String to, String companyName, String inviteUrl) throws SendMailException {
    Mail mail = buildDynamicTemplateMail(to, inviteAdminTemplateId, Map.of(
            "companyName", companyName,
            "inviteUrl", inviteUrl
    ));
    send(mail);
  }

  private Mail buildDynamicTemplateMail(String to, String templateId, Map<String, String> dynamicData) {
    Email from = new Email("support@flightfinder.space");
    Email toEmail = new Email(to);
    Mail mail = new Mail();
    mail.setFrom(from);
    mail.setTemplateId(templateId);
    Personalization personalization = new Personalization();
    personalization.addTo(toEmail);

    dynamicData.forEach(personalization::addDynamicTemplateData);
    mail.addPersonalization(personalization);

    return mail;
  }

  private void send(Mail mail) throws SendMailException {
    try {
      Request request = new Request();
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      sendGrid.api(request);
    } catch (Exception ex) {
      throw new SendMailException("Failed to send email", ex);
    }
  }
}
