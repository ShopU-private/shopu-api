package com.shopu.common.utils;

public class EmailUtil {

    public static String getHtmlTemplate(String otp) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                  <style>
                    body {
                      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                      background-color: #f9f9f9;
                      margin: 0;
                      padding: 0;
                    }
                    .container {
                      max-width: 500px;
                      margin: 30px auto;
                      background: #ffffff;
                      border-radius: 10px;
                      padding: 20px;
                      box-shadow: 0 4px 12px rgba(0,0,0,0.1);
                      border-top: 6px solid #0097A7; /* primaryColor */
                    }
                    h2 {
                      color: #2D6D6D; /* secondaryColor */
                    }
                    p {
                      color: #333333;
                      font-size: 15px;
                      line-height: 1.6;
                    }
                    .otp-box {
                      font-size: 22px;
                      font-weight: bold;
                      background: #0097A7; /* primaryColor */
                      color: #ffffff;
                      padding: 12px;
                      border-radius: 8px;
                      text-align: center;
                      letter-spacing: 3px;
                      margin: 20px 0;
                    }
                    .footer {
                      margin-top: 20px;
                      font-size: 13px;
                      color: #777777;
                      text-align: center;
                    }
                  </style>
                </head>
                <body>
                  <div class="container">
                    <h2>Login Verification Code</h2>
                    <p>Hello,</p>
                    <p>Please use the following One Time Password (OTP) to verify your login:</p>
                    <div class="otp-box">%s</div>
                    <p>If you did not request this, you can safely ignore this email.</p>
                    <div class="footer">
                      <p>Thank you,<br><strong>ShopU Team</strong></p>
                    </div>
                  </div>
                </body>
                </html>
                """.formatted(otp);
    }

    public static String getPlainTextTemplate(String otp) {
        return """
                Login Verification Code
                ------------------------

                Hello,

                Please use the following One Time Password (OTP) to verify your login:

                OTP: %s

                If you did not request this, you can safely ignore this email.

                Thank you,
                Team ShopU
                """.formatted(otp);
    }
}
