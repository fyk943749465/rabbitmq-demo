package com.feng.phonenum;

import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Locale;

@SpringBootTest
@Slf4j
class PhonenumGoogleApplicationTests {

    @Test
    void contextLoads() {
    }


    /**
     * 验证号码是否有效
     * @param phone
     * @return
     */
    public boolean validNumber(String phone) {
        return PhoneNumberUtil.getInstance().isValidNumber(getPhoneNumber(phone));
    }

    /**
     *
     * @param phone
     * @return
     */
    private Phonenumber.PhoneNumber getPhoneNumber(String phone) {

        Phonenumber.PhoneNumber phoneNumber = new Phonenumber.PhoneNumber();
        phoneNumber.setCountryCode(86);
        phoneNumber.setNationalNumber(Long.parseLong(phone));
        return phoneNumber;
    }

    /**
     * 查询号码运营商
     * @param phone
     * @return
     */
    public String getOperator(String phone) {
        return validNumber(phone) ? PhoneNumberToCarrierMapper.getInstance()
                .getNameForNumber(getPhoneNumber(phone), Locale.CHINA) : "";
    }

    /**
     * 查询号码归属地
     * @param phone
     * @return
     */
    public String getLocation(String phone) {
        return validNumber(phone) ? PhoneNumberOfflineGeocoder.getInstance()
                .getDescriptionForNumber(getPhoneNumber(phone), Locale.CHINESE) : "";
    }




    @Test
    void testPhonenum() {

        String phone = "电话号码";

        String operator = getOperator(phone);
        String location = getLocation(phone);

        System.out.println(operator + " : " + location);


    }

}
