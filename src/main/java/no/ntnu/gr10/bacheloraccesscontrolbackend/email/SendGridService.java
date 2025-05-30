package no.ntnu.gr10.bacheloraccesscontrolbackend.email;

import static no.ntnu.gr10.bacheloraccesscontrolbackend.security.JwtTokenProvider.PASSWORD_RESET_TOKEN_EXPIRATION_MS;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import java.util.Map;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.SendMailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * Service class for sending emails using SendGrid.
 * This class provides methods to send password reset and admin invitation emails.
 *
 * @author Anders Lund
 * @version 23.04.2025
 */
@Service
public class SendGridService {

  private final SendGrid sendGrid;

  @Value("${sendgrid.template.reset-password}")
  private String resetPasswordTemplateId;

  @Value("${sendgrid.template.invite-admin}")
  private String inviteAdminTemplateId;

  /**
   * Constructs a new SendGridService with the provided API key.
   *
   * @param apiKey the SendGrid API key
   */
  public SendGridService(@Value("${sendgrid.api.key}") String apiKey) {
    this.sendGrid = new SendGrid(apiKey);
  }

  /**
   * Sends a password reset email to the specified recipient.
   *
   * @param to        the recipient's email address
   * @param resetUrl  the URL for resetting the password
   * @param firstName the recipient's first name
   * @throws SendMailException if an error occurs while sending the email
   */
  public void sendPasswordResetEmail(
          String to, String resetUrl, String firstName
  ) throws SendMailException {
    String expirationMinutes = String.valueOf(PASSWORD_RESET_TOKEN_EXPIRATION_MS / 60000);
    Mail mail = buildDynamicTemplateMail(to, resetPasswordTemplateId, Map.of(
            "firstName", firstName,
            "passwordResetUrl", resetUrl,
            "expirationMinutes", expirationMinutes
    ));
    send(mail);
  }

  /**
   * Sends an admin invitation email to the specified recipient.
   *
   * @param to          the recipient's email address
   * @param companyName the name of the company
   * @param inviteUrl   the URL for accepting the invitation
   * @throws SendMailException if an error occurs while sending the email
   */
  public void sendInviteAdminEmail(
          String to, String companyName, String inviteUrl
  ) throws SendMailException {
    Mail mail = buildDynamicTemplateMail(to, inviteAdminTemplateId, Map.of(
            "companyName", companyName,
            "inviteUrl", inviteUrl
    ));
    send(mail);
  }

  private Mail buildDynamicTemplateMail(
          String to, String templateId, Map<String, String> dynamicData
  ) {
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
