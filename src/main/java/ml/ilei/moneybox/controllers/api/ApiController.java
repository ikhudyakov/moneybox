package ml.ilei.moneybox.controllers.api;

import ml.ilei.moneybox.domains.Costs;
import ml.ilei.moneybox.domains.Receipts;
import ml.ilei.moneybox.domains.User;
import ml.ilei.moneybox.repositories.CategoryRepository;
import ml.ilei.moneybox.repositories.CostsRepository;
import ml.ilei.moneybox.repositories.ReceiptsRepository;
import ml.ilei.moneybox.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CostsRepository costsRepository;

    @Autowired
    private ReceiptsRepository receiptsRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @GetMapping("/main")
    public List<Map<String, String>> home() {
        float costAmount = 0.00f;
        float allCostAmount = 0.00f;
        float receiptAmount = 0.00f;
        float allReceiptAmount = 0.00f;
        float allBalance;
        float balance;
        DecimalFormat df = new DecimalFormat("#.##");
        Map<String, Float> category = new HashMap<>();
        User currentUser = userRepository.findByUsername("demo");
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

        balance = receiptAmount - costAmount;
        allBalance = allReceiptAmount - allCostAmount;


        Date date = new Date(System.currentTimeMillis());
        float finalCostAmount = costAmount;
        float finalReceiptAmount = receiptAmount;
        float finalBalance = balance;
        float finalAllBalance = allBalance;

        return new ArrayList<Map<String, String>>() {
            {
                add(new HashMap<String, String>() {{
                    put("type", "Расходы");
                    put("value", String.valueOf(df.format(finalCostAmount)));
                }});
                add(new HashMap<String, String>() {{
                    put("type", "Доходы");
                    put("value", String.valueOf(df.format(finalReceiptAmount)));
                }});
                add(new HashMap<String, String>() {{
                    put("type", "Баланс");
                    put("value", String.valueOf(df.format(finalBalance)));
                }});
                add(new HashMap<String, String>() {{
                    put("type", "Кошелек");
                    put("value", String.valueOf(df.format(finalAllBalance)));
                }});
            }
        };
    }
}
