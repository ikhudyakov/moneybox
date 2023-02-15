package ml.ilei.moneybox.controllers.api;

import ml.ilei.moneybox.domains.Category;
import ml.ilei.moneybox.domains.Receipts;
import ml.ilei.moneybox.domains.User;
import ml.ilei.moneybox.repositories.CategoryRepository;
import ml.ilei.moneybox.repositories.ReceiptsRepository;
import ml.ilei.moneybox.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiReceiptController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReceiptsRepository receiptsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    //Страница со всеми расходами
    @GetMapping("/receipts")
    public Iterable<Receipts> receipts() {
        User currentUser = userRepository.findByUsername("demo");
        Iterable<Receipts> receipts = receiptsRepository.findAllByUserAndDateGreaterThanEqualAndDateLessThanEqual(currentUser,
                currentUser.getStartDate(), currentUser.getEndDate());
        Iterable<Category> categories = categoryRepository.findAllByType("2");
        Date date = new Date(System.currentTimeMillis());
        currentUser.getStartDate().toString();
        currentUser.getEndDate().toString();
        return receipts;
    }

    /*//Выбор даты для расходов
    @PostMapping("/costs/date")
    public String dateCost(
            @AuthenticationPrincipal User user,
            @RequestParam("startDate") Date startDate,
            @RequestParam("endDate") Date endDate,
            Model model
    ) throws IOException {
        dateUpdate(startDate, endDate, user);
        return "redirect:/costs";
    }*/

    //Доабвление расходов
    @PostMapping("/receipts")
    public Iterable<Receipts> addReceipt(@RequestBody Map<String, String> bodyReceipt) throws IOException {
        Receipts receipt = new Receipts();
        User currentUser = userRepository.findByUsername("demo");
        Date date = new Date(System.currentTimeMillis());
        Category category = categoryRepository.findAllByType("2").get(0);

        receipt.setUser(currentUser);
        receipt.setDate(date);
        receipt.setCategory(category);
        receipt.setAmount(Float.parseFloat(bodyReceipt.get("amount")));
        receipt.setDescription(bodyReceipt.get("description"));
        receiptsRepository.save(receipt);
        Iterable<Receipts> receipts = receiptsRepository.findAllByUserAndDateGreaterThanEqualAndDateLessThanEqual(currentUser,
                currentUser.getStartDate(), currentUser.getEndDate());
        return receipts;
    }

    /*//Обновление записи расхода
    @GetMapping("/costs/{cost}")
    public String costEditForm(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Costs cost,
            Model model) {
        if (!user.equals(cost.getUser())) {
            return "redirect:/costs";
        }
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("cost", cost);
        String amount = String.valueOf(cost.getAmount());
        model.addAttribute("amount", amount);
        String sDate = cost.getDate().toString();
        model.addAttribute("date", sDate);
        Iterable<Category> categories = categoryRepository.findAllByType("1");
        model.addAttribute("categories", categories);
        String description;
        if (cost.getDescription() == null) {
            description = "";
        } else {
            description = cost.getDescription();
        }
        model.addAttribute("description", description);
        return "editCost";
    }

    @PostMapping("/costs/{cost}")
    public String updateCost(
            @AuthenticationPrincipal User currentUser,
            @RequestParam("id") Costs cost,
            @RequestParam("amount") double amount,
            @RequestParam("date") Date date,
            @RequestParam("category") Category category,
            @RequestParam("description") String description
    ) throws IOException {
        if (!currentUser.equals(cost.getUser())) {
            return "redirect:/costs";
        }
        if (cost.getUser().equals(currentUser)) {
            cost.setAmount(amount);
            cost.setDate(date);
            if (!description.trim().equals("")) {
                cost.setDescription(description);
            }
            if (!cost.getCategory().equals(category)) {
                cost.setCategory(category);
            }
            costsRepository.save(cost);
        }
        return "redirect:/costs";
    }

    //Удаление записи расхода
    @GetMapping("/costs/{cost}/delete")
    public String deleteCost(
            @AuthenticationPrincipal User currentUser,
            @RequestParam("id") Costs cost
    ) {
        if (cost.getUser().equals(currentUser)) {
            costsRepository.delete(cost);
        }
        return "redirect:/costs";
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
    }*/
}
