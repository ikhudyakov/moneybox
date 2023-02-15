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
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CostsRepository costsRepository;

    @Autowired
    private ReceiptsRepository receiptsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MailSender mailSender;

    //Отчет
    @PostMapping("/report")
    public String report(
            @AuthenticationPrincipal User user,
            @RequestParam("startDate") Date startDate,
            @RequestParam("endDate") Date endDate,
            @RequestParam("email") String email,
            Model model
    ) throws MessagingException, DocumentException, IOException {
        float costAmount = 0.00f;
        float receiptAmount = 0.00f;
        Document document = new Document();
        String fileName = "/home/pi/Temp/report" + System.currentTimeMillis() + ".pdf";
        BaseFont bf = BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); //подключаем файл шрифта, который поддерживает кириллицу
        Font font = new Font(bf,14);

        DecimalFormat df = new DecimalFormat("#.##");
        User currentUser = userRepository.findByUsername(user.getUsername());
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            Paragraph period = new Paragraph("Отчет по операциям в период \n с " + String.format("%1$td %1$tB %1$tY %2$s", startDate, "г.") +
                    " по " + String.format("%1$td %1$tB %1$tY %2$s", endDate, "г.") + "\n", new Font(bf,20, 5));
            period.setAlignment(Element.ALIGN_CENTER);
            document.add(period);
            Paragraph costsTitle = new Paragraph("\nРасходы:", new Font(bf,18, 2));
            costsTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(costsTitle);

            PdfPTable costsTable = getPdfPTable(font);

            Iterable<Costs> costs = costsRepository.findAllByUserAndDateGreaterThanEqualAndDateLessThanEqual(user,
                    startDate, endDate);
            Map<String, Float> category = new HashMap<>();
            for (Costs cost : costs) {
                PdfPCell c4 = new PdfPCell(new Phrase(String.format("%1$td.%1$tm.%1$tY", cost.getDate()), font));
                costsTable.addCell(c4);
                PdfPCell c5 = new PdfPCell(new Phrase(cost.getDescription(), font));
                costsTable.addCell(c5);
                PdfPCell c6 = new PdfPCell(new Phrase(cost.getAmount() + " руб.", font));
                c6.setHorizontalAlignment(Element.ALIGN_RIGHT);
                costsTable.addCell(c6);
                if (category.containsKey(cost.getCategory().getName())) {
                    category.put(cost.getCategory().getName(), (float) (category.get(cost.getCategory().getName()) + cost.getAmount()));
                } else {
                    category.put(cost.getCategory().getName(), (float) cost.getAmount());
                }
                costAmount += cost.getAmount();
            }
            PdfPCell c1 = new PdfPCell(new Phrase(""));
            costsTable.addCell(c1);
            PdfPCell c2 = new PdfPCell(new Phrase("ИТОГО", new Font(bf,18, 3)));
            costsTable.addCell(c2);
            PdfPCell c3 = new PdfPCell(new Phrase(costAmount + " руб.", new Font(bf,18, 3)));
            c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
            costsTable.addCell(c3);
            document.add(costsTable);

            Paragraph receiptsTitle = new Paragraph("Доходы:", new Font(bf,18, 2));
            receiptsTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(receiptsTitle);
            PdfPTable receiptsTable = getPdfPTable(font);
            Iterable<Receipts> receipts = receiptsRepository.findAllByUserAndDateGreaterThanEqualAndDateLessThanEqual(user,
                    startDate, endDate);
            for (Receipts receipt : receipts) {
                PdfPCell c4 = new PdfPCell(new Phrase(String.format("%1$td.%1$tm.%1$tY", receipt.getDate()), font));
                receiptsTable.addCell(c4);
                PdfPCell c5 = new PdfPCell(new Phrase(receipt.getDescription(), font));
                receiptsTable.addCell(c5);
                PdfPCell c6 = new PdfPCell(new Phrase(receipt.getAmount() + " руб.", font));
                c6.setHorizontalAlignment(Element.ALIGN_RIGHT);
                receiptsTable.addCell(c6);
                if (category.containsKey(receipt.getCategory().getName())) {
                    category.put(receipt.getCategory().getName(), (float) (category.get(receipt.getCategory().getName()) + receipt.getAmount()));
                } else {
                    category.put(receipt.getCategory().getName(), (float) receipt.getAmount());
                }
                receiptAmount += receipt.getAmount();
            }
            c1 = new PdfPCell(new Phrase(""));
            receiptsTable.addCell(c1);
            c2 = new PdfPCell(new Phrase("ИТОГО", new Font(bf,18, 3)));
            receiptsTable.addCell(c2);
            c3 = new PdfPCell(new Phrase(receiptAmount + " руб.", new Font(bf,18, 3)));
            c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
            receiptsTable.addCell(c3);
            document.add(receiptsTable);

            StringBuilder stringBuilder = new StringBuilder();

            for (Map.Entry entry : category.entrySet()) {
                stringBuilder.append(entry.getKey().toString()).append(" ").append(df.format(entry.getValue())).append(" руб.").append("\n");
//                System.out.println(entry.getKey() + " " + df.format(entry.getValue()));
            }

            Paragraph report = new Paragraph("Сумма расходов за период: " + costAmount + " руб.\nСумма доходов за период: "
                    + receiptAmount + " руб.", new Font(bf,18, 2));
            document.add(report);
            document.close();
            String message = stringBuilder.toString();
            mailSender.send(email, "Отчет", message, fileName);
            File file = new File(fileName);
            file.delete();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }

        return "redirect:/main?report=true";
    }

    private PdfPTable getPdfPTable(Font font) {
        PdfPTable t = new PdfPTable(3);
        t.setSpacingBefore(15);
        t.setSpacingAfter(15);
        PdfPCell c1 = new PdfPCell(new Phrase("Дата", font));
        t.addCell(c1);
        PdfPCell c2 = new PdfPCell(new Phrase("Описание", font));
        t.addCell(c2);
        PdfPCell c3 = new PdfPCell(new Phrase("Сумма", font));
        c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        t.addCell(c3);
        return t;
    }

}
