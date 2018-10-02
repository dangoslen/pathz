package me.dangoslen.pathz.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhoneUtils {

    private static final Logger LOG = LoggerFactory.getLogger(PhoneUtils.class);
    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    public static String getFormattedNumber(String number) {
        String formattedE164 = number;

        try {
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(number, "US");

            formattedE164 = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException e) {
            LOG.error("To Number is not a valid number");
        }

        return formattedE164;
    }
}
