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
}
