package com.jokindy.finapp.operation;

import com.jokindy.finapp.account.Account;
import com.jokindy.finapp.account.AccountService;
import com.jokindy.finapp.currency.Currency;
import com.jokindy.finapp.exception.BalanceIsNegativeException;
import com.jokindy.finapp.operation.dto.OperationDto;
import com.jokindy.finapp.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(path = "/operations")
@AllArgsConstructor
public class OperationController {

    private UserRepository userRepository;
    private OperationService operationService;
    private AccountService accountService;

    @GetMapping("/add")
    public String newOperation(Model model) {
        long userId = getUserId();
        model.addAttribute("operation", new Operation());
        List<Account> accountList = accountService.getAccounts(userId);
        if (!accountList.isEmpty()) {
            model.addAttribute("accounts", accountList);
            model.addAttribute("currencies", Currency.values());
            return "operation/add";
        } else {
            model.addAttribute("message", "У вас еще нет счетов для списания средств");
            model.addAttribute("redirect", "/menu");
            return "error/400";
        }
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute("operation") OperationDto operationDto, BindingResult result, Model model) {
        long userId = getUserId();
        if (result.hasErrors()) {
            for (FieldError fieldError : result.getFieldErrors()) {
                model.addAttribute("message", fieldError.getDefaultMessage());
            }
            model.addAttribute("redirect", "/operations/add");
            return "error/400";
        }
        try {
            operationService.save(operationDto, userId);
            return "redirect:/operations/all";
        } catch (BalanceIsNegativeException e) {
            model.addAttribute("message", "Невозможно добавить операцию - недостаточно средств на счёте.");
            return "error/400";
        }
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        long userId = getUserId();
        model.addAttribute("header", "Ваши последние операции:");
        model.addAttribute("operations", operationService.getAll(userId));
        return "operation/all";
    }

    private long getUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return userRepository.findByUsername(username).getId();
    }
}