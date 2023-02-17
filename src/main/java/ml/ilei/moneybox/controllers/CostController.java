package ml.ilei.moneybox.controllers;

import ml.ilei.moneybox.domains.Category;
import ml.ilei.moneybox.domains.Costs;
import ml.ilei.moneybox.domains.Role;
import ml.ilei.moneybox.domains.User;
import ml.ilei.moneybox.repositories.CategoryRepository;
import ml.ilei.moneybox.repositories.CostsRepository;
import ml.ilei.moneybox.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;

@Controller
public class CostController {
    private final UserRepository userRepository;

    private final CostsRepository costsRepository;

    private final CategoryRepository categoryRepository;

    public CostController(UserRepository userRepository, CostsRepository costsRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.costsRepository = costsRepository;
        this.categoryRepository = categoryRepository;
    }

    //Страница со всеми расходами
    @GetMapping("/costs")
    public String costs(
            @PageableDefault(sort = {"date", "id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User user,
            Model model) throws IOException {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        User currentUser = userRepository.findByUsername(user.getUsername());
        Page<Costs> costs = costsRepository.findAllByUserAndDateGreaterThanEqualAndDateLessThanEqual(currentUser, currentUser.getStartDate(), currentUser.getEndDate(), pageable);
        Iterable<Category> categories = categoryRepository.findAllByType("1");
        model.addAttribute("costs", costs);
        model.addAttribute("categories", categories);
        Date date = new Date(System.currentTimeMillis());
        model.addAttribute("date", date.toString());
        model.addAttribute("startDate", currentUser.getStartDate().toString());
        model.addAttribute("endDate", currentUser.getEndDate().toString());
        model.addAttribute("url", "/costs");
        return "costs";
    }

    //Выбор даты для расходов
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

    //Доабвление расходов
    @PostMapping("/costs")
    public String addCost(
            @PageableDefault(sort = {"date", "id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User user,
            @Valid Costs cost,
            Model model
    ) throws IOException {
        cost.setUser(user);
        costsRepository.save(cost);
        cost.setsId();
        costsRepository.save(cost);
        Iterable<Costs> costs = costsRepository.findAllByUser(user, pageable);
        Iterable<Category> categories = categoryRepository.findAllByType("1");
        model.addAttribute("costs", costs);
        model.addAttribute("categories", categories);
        Date date = new Date(System.currentTimeMillis());
        model.addAttribute("date", date.toString());
        model.addAttribute("url", "/costs");
        return "redirect:/costs";
    }

    //Обновление записи расхода
    @GetMapping("/costs/{cost}")
    public String costEditForm(
            @AuthenticationPrincipal User user,
            @RequestParam("sId") Costs cost,
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
            @RequestParam("sId") Costs cost,
            @RequestParam("amount") float amount,
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
            @RequestParam("sId") Costs cost
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
    }

    //пзапись в файл
    public void writeToFile(byte[] data, File file) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        out.write(data);
        out.close();
    }
}
