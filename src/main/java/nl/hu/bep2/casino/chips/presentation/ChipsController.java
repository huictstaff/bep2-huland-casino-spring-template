package nl.hu.bep2.casino.chips.presentation;

import nl.hu.bep2.casino.chips.application.ChipsService;
import nl.hu.bep2.casino.chips.domain.exception.NegativeNumberException;
import nl.hu.bep2.casino.chips.domain.exception.NotEnoughChipsException;
import nl.hu.bep2.casino.chips.domain.Balance;
import nl.hu.bep2.casino.chips.presentation.dto.Deposit;
import nl.hu.bep2.casino.chips.presentation.dto.Withdrawal;
import nl.hu.bep2.casino.security.domain.UserProfile;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/chips")
public class ChipsController {
    private final ChipsService service;

    public ChipsController(ChipsService service) {
        this.service = service;
    }

    @GetMapping("/balance")
    public Balance showBalance(Authentication authentication) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();
        Balance balance = this.service.findBalance(profile.getUsername());

        return balance;
    }

    @PostMapping("/deposit")
    public Balance deposit(Authentication authentication, @Validated @RequestBody Deposit deposit) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();

        try {
            Balance balance = this.service.depositChips(profile.getUsername(), deposit.amount);
            return balance;
        } catch (NegativeNumberException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }

    }
;
    @PostMapping("/withdraw")
    public Balance withdraw(Authentication authentication, @Validated @RequestBody Withdrawal withdrawal) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();

        try {
            Balance balance = this.service.withdrawChips(profile.getUsername(), withdrawal.amount);
            return balance;
        } catch (NotEnoughChipsException exception) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, exception.getMessage());
        }
    }
}
