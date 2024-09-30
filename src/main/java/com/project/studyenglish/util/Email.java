package com.project.studyenglish.util;


import com.project.studyenglish.service.impl.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Email {
    @Autowired
    private EmailService emailService;
    public void sendEmailDetailOrder(String email,String detail){
        String subject = "Thông báo đơn hàng của bạn tại Cửa Hàng Kinn";
        String text = "<html><body> <h3>Cửa h&agrave;ng Kinn về đơn h&agrave;ng đang được giao</h3>\n" +
                "<p>Đơn h&agrave;ng gồm:</p>\n" + detail+
                "<p>Đơn h&agrave;ng sẽ được nh&acirc;n vi&ecirc;n<span style=\"color: #ff0000;\"> Cửa h&agrave;ng Kinn</span> giao tới bạn trong vài ngày tới:</p>\n" +
                "<p>Xin ch&acirc;n th&agrave;nh cảm ơn sự tin tưởng của qu&yacute; kh&aacute;ch!</p> </body> </html>";
        emailService.sendMessage("kienhien200418@gmail.com", email, subject, text);
    }
    public   void sendEmail(String email, int code) {
        String subject = "Thông báo kích hoạt tài khoản của bạn tại Cửa Hàng Kinn";
        String text = "<html><body> <h3>Cửa h&agrave;ng Kinn xin tr&acirc;n trọng th&ocirc;ng b&aacute;o về việc gửi m&atilde; k&iacute;ch hoạt t&agrave;i khoản đến qu&yacute; kh&aacute;ch h&agrave;ng.</h3>\n" +
                "<p>Sau khi qu&yacute; kh&aacute;ch ho&agrave;n tất việc đăng k&yacute; t&agrave;i khoản tr&ecirc;n trang web của ch&uacute;ng t&ocirc;i, email n&agrave;y sẽ chứa một m&atilde; k&iacute;ch hoạt duy nhất để bảo đảm an to&agrave;n v&agrave; x&aacute;c minh t&agrave;i khoản.</p>\n" +
                "<p>H&atilde;y nhập m&atilde; v&agrave;o trang website Cửa h&agrave;ng Kinn để được k&iacute;ch hoạt t&agrave;i khoản:</p>\n" +
                "<p><strong>M&atilde; code:</strong>&nbsp; <strong>" + code + "</strong></p>\n" +
                "<div class=\"\\&quot;flex\">\n" +
                "<div class=\"\\&quot;min-h-[20px]\" dir=\"\\&quot;auto\\&quot;\" data-message-author-role=\"\\&quot;assistant\\&quot;\" data-message-id=\"\\&quot;4289b4b5-78ab-4c76-b48b-dbf63a220f09\\&quot;\">\n" +
                "<div class=\"\\&quot;flex\">\n" +
                "<div class=\"\\&quot;markdown\">\n" +
                "<p>Nếu c&oacute; bất kỳ vấn đề g&igrave; trong qu&aacute; tr&igrave;nh nhận m&atilde; k&iacute;ch hoạt, qu&yacute; kh&aacute;ch vui l&ograve;ng li&ecirc;n hệ với bộ phận hỗ trợ kh&aacute;ch h&agrave;ng của ch&uacute;ng t&ocirc;i để được trợ gi&uacute;p kịp thời.</p>\n" +
                "<br />\n" +
                "<p>Xin ch&acirc;n th&agrave;nh cảm ơn sự tin tưởng của qu&yacute; kh&aacute;ch!</p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div> </body> </html>";
        emailService.sendMessage("kienhien200418@gmail.com", email, subject, text);
    }
}
