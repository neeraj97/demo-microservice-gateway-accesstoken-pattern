package com.example.ZuulGatewayService.Utilities;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

//for more implementations refer to https://www.twilio.com/docs
@Component
public class TwilioSMSOtpServiceProvider implements OtpServiceProvider {

	@Value("${twilio.account_sid}")
	private String ACCOUNT_SID;

	@Value("${twilio.auth_token}")
	private String AUTH_TOKEN;

	@Value("${twilio.service_sid}")
	private String SERVICE_SID;

	protected final Log logger = LogFactory.getLog(getClass());

	@PostConstruct
	public void intializeTwilioEnvironment() {

		logger.info(String.format("Initializing Twilio environment with ACCOUNT_SID %s , AUTH_TOKEN %s", ACCOUNT_SID,
				AUTH_TOKEN));
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
	}

	@Override
	public boolean VerifyOTP(String user, String token) {
		try {
			VerificationCheck verificationCheck = VerificationCheck.creator(SERVICE_SID, token).setTo(user).create();

			// return
			logger.info(String.format("OTP verification check for user %s token %s is %s", user, token,
					verificationCheck.getValid()));

			return verificationCheck.getValid();
		} catch (Exception ex) {
			logger.error(ex);
			return false;
		}
	}

	@Override
	public String GenerateOTP(String user) {

		logger.info(String.format("Generating OTP for %s", user));

		Verification verification = Verification.creator(SERVICE_SID, user, "sms").create();

		logger.info(verification);

		return verification.getSid();
	}

}
