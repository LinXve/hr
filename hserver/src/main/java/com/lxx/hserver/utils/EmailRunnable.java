package com.lxx.hserver.utils;

import com.lxx.hserver.entity.Employee;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


/**
 * 邮件发送
 */
public class EmailRunnable implements Runnable {
    private Employee employee;
    private JavaMailSender javaMailSender;
    private TemplateEngine templateEngine;

    public EmailRunnable(Employee employee, JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.employee = employee;
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void run() {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(employee.getEmail());
            helper.setFrom("1043138485@qq.com");//发送者
            helper.setSubject("xxx公司:通知");
            Context context=new Context();
            context.setVariable("name",employee.getName());
            context.setVariable("workID",employee.getWorkID());
            context.setVariable("contractTerm",employee.getContractTerm());
            context.setVariable("beginContract",employee.getBeginContract());
            context.setVariable("endContract",employee.getEndContract());
            context.setVariable("departmentName",employee.getDepartmentName());
            context.setVariable("posName",employee.getPosName());
            String email=templateEngine.process("email.html",context);
            helper.setText(email,true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("邮件发送失败");
        }
    }
}
