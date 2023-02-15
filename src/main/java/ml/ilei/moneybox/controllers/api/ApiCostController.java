package ml.ilei.moneybox.controllers.api;

import ml.ilei.moneybox.domains.Category;
import ml.ilei.moneybox.domains.Costs;
import ml.ilei.moneybox.domains.User;
import ml.ilei.moneybox.repositories.CategoryRepository;
import ml.ilei.moneybox.repositories.CostsRepository;
import ml.ilei.moneybox.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiCostController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CostsRepository costsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    //Страница со всеми расходами
    @GetMapping("/costs")
    public Iterable<Costs> costs() {
        User currentUser = userRepository.findByUsername("demo");
        Iterable<Costs> costs = costsRepository.findAllByUserAndDateGreaterThanEqualAndDateLessThanEqual(currentUser,
                currentUser.getStartDate(), currentUser.getEndDate());
        Iterable<Category> categories = categoryRepository.findAllByType("1");
        Date date = new Date(System.currentTimeMillis());
        currentUser.getStartDate().toString();
        currentUser.getEndDate().toString();
        return costs;
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
    }
    */

    //Доабвление расходов
    @PostMapping("/costs")
    public Iterable<Costs> addCost(@RequestBody Map<String, String> bodyCost) throws IOException {
        Costs cost = new Costs();
        User currentUser = userRepository.findByUsername("demo");
        Date date = new Date(System.currentTimeMillis());
        Category category = categoryRepository.findAllByType("1").get(0);

        cost.setUser(currentUser);
        cost.setDate(date);
        cost.setCategory(category);
        cost.setAmount(Float.parseFloat(bodyCost.get("amount")));
        cost.setDescription(bodyCost.get("description"));
        costsRepository.save(cost);
        Iterable<Costs> costs = costsRepository.findAllByUserAndDateGreaterThanEqualAndDateLessThanEqual(currentUser,
                currentUser.getStartDate(), currentUser.getEndDate());
        return costs;
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
