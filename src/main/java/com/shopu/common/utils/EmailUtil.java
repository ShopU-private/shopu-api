package com.shopu.common.utils;

public class EmailUtil {

    public static String getHtmlTemplate(String otp) {
        return """
                <!DOCTYPE html>
                <html>
                <body style="font-family: sans-serif; padding: 10px;">
                  <h2>Login Verification Code</h2>
                  <p>Your OTP is: <strong>%s</strong></p>
                  <p>Thank you,<br>ShopU Team</p>
                </body>
                </html>
                """.formatted(otp);
    }


    public static String getPlainTextTemplate(String otp) {
        return """
                Login Verification Code
              
                Your OTP is: %s
                
                Thank you,
                Team ShopU
                """.formatted(otp);
    }
}
