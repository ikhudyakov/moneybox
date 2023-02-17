package ml.ilei.moneybox.controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import ml.ilei.moneybox.domains.Costs;
import ml.ilei.moneybox.domains.Receipts;
import ml.ilei.moneybox.domains.User;
import ml.ilei.moneybox.repositories.CategoryRepository;
import ml.ilei.moneybox.repositories.CostsRepository;
import ml.ilei.moneybox.repositories.ReceiptsRepository;
import ml.ilei.moneybox.repositories.UserRepository;
import ml.ilei.moneybox.services.MailSender;
import ml.ilei.moneybox.utilities.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ReportController {
    private final UserRepository userRepository;

    private final CostsRepository costsRepository;

    private final ReceiptsRepository receiptsRepository;

    private final MailSender mailSender;

    private final Utility utility;

    public ReportController(UserRepository userRepository, CostsRepository costsRepository, ReceiptsRepository receiptsRepository, MailSender mailSender, Utility utility) {
        this.userRepository = userRepository;
        this.costsRepository = costsRepository;
        this.receiptsRepository = receiptsRepository;
        this.mailSender = mailSender;
        this.utility = utility;
    }

    //Отчет
    @PostMapping("/report")
    public String report(
            @AuthenticationPrincipal User user,
            @RequestParam("startDate") Date startDate,
            @RequestParam("endDate") Date endDate,
            @RequestParam("email") String email
    ) throws MessagingException, DocumentException, IOException {

        utility.createReport(userRepository, costsRepository, receiptsRepository, mailSender, user, startDate, endDate, email);

        return "redirect:/main?report=true";
    }
}
