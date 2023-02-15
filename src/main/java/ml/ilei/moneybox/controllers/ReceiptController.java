package ml.ilei.moneybox.controllers;

import ml.ilei.moneybox.domains.Category;
import ml.ilei.moneybox.domains.Receipts;
import ml.ilei.moneybox.domains.Role;
import ml.ilei.moneybox.domains.User;
import ml.ilei.moneybox.repositories.CategoryRepository;
import ml.ilei.moneybox.repositories.ReceiptsRepository;
import ml.ilei.moneybox.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Date;

@Controller
public class ReceiptController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReceiptsRepository receiptsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    //Страница со всеми доходами
    @GetMapping("/receipts")
    public String receipts(
            @PageableDefault(sort = {"date", "id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User user,
            Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        User currentUser = userRepository.findByUsername(user.getUsername());
        Page<Receipts> receipts = receiptsRepository.findAllByUserAndDateGreaterThanEqualAndDateLessThanEqual(currentUser, currentUser.getStartDate(), currentUser.getEndDate(), pageable);
        model.addAttribute("receipts", receipts);
        Iterable<Category> categories = categoryRepository.findAllByType("2");
        model.addAttribute("categories", categories);
        Date date = new Date(System.currentTimeMillis());
        model.addAttribute("startDate", currentUser.getStartDate().toString());
        model.addAttribute("endDate", currentUser.getEndDate().toString());
        model.addAttribute("date", date.toString());
        model.addAttribute("url", "/receipts");
        return "receipts";
    }

    //Выбор даты для доходов
    @PostMapping("/receipts/date")
    public String dateReceipt(
            @AuthenticationPrincipal User user,
            @RequestParam("startDate") Date startDate,
            @RequestParam("endDate") Date endDate,
            Model model
    ) throws IOException {
        dateUpdate(startDate, endDate, user);
        return "redirect:/receipts";
    }

    //Доабвление доходов
    @PostMapping("/receipts")
    public String addReceipt(
            @PageableDefault(sort = {"date", "id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, defaultValue = "0") int page,
            @AuthenticationPrincipal User user,
            @Valid Receipts receipt,
            BindingResult bindingResult,
            Model model
    ) throws IOException {
        receipt.setUser(user);
        receiptsRepository.save(receipt);
        receipt.setsId();
        receiptsRepository.save(receipt);
        Iterable<Receipts> receipts = receiptsRepository.findAllByUser(user, pageable);
        model.addAttribute("receipts", receipts);
        Iterable<Category> categories = categoryRepository.findAllByType("2");
        model.addAttribute("categories", categories);
        Date date = new Date(System.currentTimeMillis());
        model.addAttribute("date", date.toString());
        model.addAttribute("url", "/receipts");
        return "redirect:/receipts";
    }

    //Обновление записи дохода
    @GetMapping("/receipts/{receipt}")
    public String receiptEditForm(
            @AuthenticationPrincipal User user,
            @RequestParam("sId") Receipts receipt,
            Model model) {
        if (!user.equals(receipt.getUser())) {
            return "redirect:/receipt";
        }
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("receipt", receipt);
        String amount = String.valueOf(receipt.getAmount());
        model.addAttribute("amount", amount);
        String sDate = receipt.getDate().toString();
        model.addAttribute("date", sDate);
        Iterable<Category> categories = categoryRepository.findAllByType("2");
        model.addAttribute("categories", categories);
        String description;
        if (receipt.getDescription() == null) {
            description = "";
        } else {
            description = receipt.getDescription();
        }
        model.addAttribute("description", description);
        return "editReceipt";
    }

    @PostMapping("/receipts/{receipt}")
    public String updateReceipt(
            @AuthenticationPrincipal User currentUser,
            @RequestParam("sId") Receipts receipt,
            @RequestParam("amount") float amount,
            @RequestParam("date") Date date,
            @RequestParam("category") Category category,
            @RequestParam("description") String description
    ) throws IOException {
        if (!currentUser.equals(receipt.getUser())) {
            return "redirect:/receipts";
        }
        if (receipt.getUser().equals(currentUser)) {
            receipt.setAmount(amount);
            receipt.setDate(date);
            if (!description.trim().equals("")) {
                receipt.setDescription(description);
            }
            if (!receipt.getCategory().equals(category)) {
                receipt.setCategory(category);
            }
            receiptsRepository.save(receipt);
        }
        return "redirect:/receipts";
    }

    //Удаление записи дохода
    @GetMapping("/receipts/{receipt}/delete")
    public String deleteReceipt(
            @AuthenticationPrincipal User currentUser,
            @RequestParam("sId") Receipts receipt
    ) {
        if (receipt.getUser().equals(currentUser)) {
            receiptsRepository.delete(receipt);
        }
        return "redirect:/receipts";
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
