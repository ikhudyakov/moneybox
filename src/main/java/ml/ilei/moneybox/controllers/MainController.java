package ml.ilei.moneybox.controllers;

import ml.ilei.moneybox.domains.Costs;
import ml.ilei.moneybox.domains.Receipts;
import ml.ilei.moneybox.domains.Role;
import ml.ilei.moneybox.domains.User;
import ml.ilei.moneybox.repositories.CategoryRepository;
import ml.ilei.moneybox.repositories.CostsRepository;
import ml.ilei.moneybox.repositories.ReceiptsRepository;
import ml.ilei.moneybox.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CostsRepository costsRepository;

    @Autowired
    private ReceiptsRepository receiptsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/main")
    public String home(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false, defaultValue = "") String report,
            Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        float costAmount = 0.00f;
        float allCostAmount = 0.00f;
        float receiptAmount = 0.00f;
        float allReceiptAmount = 0.00f;
        float allBalance;
        float balance;
        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormat df1 = new DecimalFormat("#");
        Map<String, Float> category = new HashMap<>();
        List<String[]> forChart = new ArrayList<>();
        User currentUser = userRepository.findByUsername(user.getUsername());
        Iterable<Costs> costs = costsRepository.findAllByUserAndDateGreaterThanEqualAndDateLessThanEqual(currentUser,
                currentUser.getStartDate(), currentUser.getEndDate());
        for (Costs cost : costs) {
            if (category.containsKey(cost.getCategory().getName())) {
                category.put(cost.getCategory().getName(), (float) (category.get(cost.getCategory().getName()) + cost.getAmount()));
            } else {
                category.put(cost.getCategory().getName(), (float) cost.getAmount());
            }
            costAmount += cost.getAmount();
        }
        Iterable<Costs> allCosts = costsRepository.findAllByUser(currentUser);
        for (Costs cost : allCosts) {
            allCostAmount += cost.getAmount();
        }

        Iterable<Receipts> receipts = receiptsRepository.findAllByUserAndDateGreaterThanEqualAndDateLessThanEqual(currentUser,
                currentUser.getStartDate(), currentUser.getEndDate());
        for (Receipts receipt : receipts) {
            if (category.containsKey(receipt.getCategory().getName())) {
                category.put(receipt.getCategory().getName(), (float) (category.get(receipt.getCategory().getName()) + receipt.getAmount()));
            } else {
                category.put(receipt.getCategory().getName(), (float) receipt.getAmount());
            }
            receiptAmount += receipt.getAmount();
        }
        Iterable<Receipts> allReceipts = receiptsRepository.findAllByUser(currentUser);
        for (Receipts receipt : allReceipts) {
            allReceiptAmount += receipt.getAmount();
        }
        String[] s;
        for (Entry entry : category.entrySet()) {
            s = new String[]{entry.getKey().toString(), entry.getValue().toString()};
            forChart.add(s);
//            System.out.println(entry.getKey() + " " + df.format(entry.getValue()));
        }
        s = new String[]{"Тип", "Сумма"};
        forChart.add(0, s);

        balance = receiptAmount - costAmount;
        allBalance = allReceiptAmount - allCostAmount;

        if (report != null && !report.isEmpty()) {
            model.addAttribute("report", true);
        } else {
            model.addAttribute("report", false);
        }

        Date date = new Date(System.currentTimeMillis());
        model.addAttribute("costs", df.format(costAmount));
        model.addAttribute("receipts", df.format(receiptAmount));
        model.addAttribute("balance", df.format(balance));
        model.addAttribute("allBalance", df1.format(allBalance));
        model.addAttribute("startDate", currentUser.getStartDate().toString());
        model.addAttribute("endDate", currentUser.getEndDate().toString());
        model.addAttribute("categories", forChart);
        model.addAttribute("email", currentUser.getEmail());
        model.addAttribute("actionType", "/main");
        return "main";
    }

    //Выбор общей даты
    @PostMapping("/main/date")
    public String mainDate(
            @AuthenticationPrincipal User user,
            @RequestParam("startDate") Date startDate,
            @RequestParam("endDate") Date endDate,
            Model model
    ) throws IOException {
        dateUpdate(startDate, endDate, user);
        return "redirect:/main";
    }

    @GetMapping("/login")
    public String login(Map<String, Object> model) {
        return "login";
    }

    //Обновление даты
    private void dateUpdate(Date startDate, Date endDate, User user) {
        User currentUser = userRepository.findByUsername(user.getUsername());
        if (!startDate.equals(currentUser.getStartDate()) || !endDate.equals(currentUser.getEndDate())) {
            if (startDate.getTime() <= endDate.getTime()) {
                currentUser.setStartDate(startDate);
                currentUser.setEndDate(endDate);
                userRepository.save(currentUser);
            }
        }
    }
}
