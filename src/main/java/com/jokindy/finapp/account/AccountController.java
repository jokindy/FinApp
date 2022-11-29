package com.jokindy.finapp.account;

import com.jokindy.finapp.account.dto.AccountDto;
import com.jokindy.finapp.currency.Currency;
import com.jokindy.finapp.exception.NotEnoughMoneyException;
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
@RequestMapping(path = "/accounts")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final UserRepository userRepository;

    @GetMapping("add")
    public String addAccount(Model model) {
        model.addAttribute("account", new AccountDto());
        model.addAttribute("currencies", Currency.values());
        return "account/add";
    }

    @PostMapping("add")
    public String saveAccount(@Valid @ModelAttribute("account") AccountDto accountDto, BindingResult result,
                              Model model) {
        long userId = getUserId();
        if (result.hasErrors()) {
            for (FieldError fieldError : result.getFieldErrors()) {
                model.addAttribute("message", fieldError.getDefaultMessage());
            }
            model.addAttribute("redirect", "/accounts/add");
            return "error/400";
        }
        accountService.save(accountDto, userId);
        return "redirect:/accounts/info";
    }

    @GetMapping("transfer")
    public String transfer(Model model) {
        long userId = getUserId();
        List<Account> accountList = accountService.getAccounts(userId);
        if (!accountList.isEmpty()) {
            if (accountList.size() == 1) {
                model.addAttribute("message", "У вас только один счет");
                model.addAttribute("redirect", "/menu");
                return "error/400";
            }
            model.addAttribute("accounts", accountList);
            model.addAttribute("transfer_info", new AccountTransferInfo());
            return "account/transfer";
        } else {
            model.addAttribute("message", "У вас еще нет счетов для списания средств");
            model.addAttribute("redirect", "/menu");
            return "error/400";
        }
    }

    @PostMapping("transfer")
    public String transferAccounts(@Valid @ModelAttribute("transfer_info") AccountTransferInfo accountTransferInfo,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            for (FieldError fieldError : result.getFieldErrors()) {
                model.addAttribute("message", fieldError.getDefaultMessage());
            }
            model.addAttribute("redirect", "/accounts/transfer");
            return "error/400";
        }
        try {
            accountService.transfer(accountTransferInfo);
            return "redirect:/accounts/info";
        } catch (NotEnoughMoneyException e) {
            model.addAttribute("message", "На счету недостаточно средств для перевода");
            model.addAttribute("redirect", "/accounts/transfer");
            return "error/400";
        }
    }

    @GetMapping("/info")
    public String getInfo(Model model) {
        long userId = getUserId();
        model.addAttribute("header", "Ваши счета:");
        model.addAttribute("accounts", accountService.getAccounts(userId));
        double balance = accountService.getBalance(userId);
        model.addAttribute("total_balance", "Ваш баланс: " + balance + " USD");
        return "account/info";
    }

    private long getUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return userRepository.findByUsername(username).getId();
    }
}