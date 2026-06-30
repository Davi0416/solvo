package io.github.solvo.api.controllers;

import io.github.solvo.api.dtos.CreateWalletRequest;
import io.github.solvo.api.dtos.WalletResponse;
import io.github.solvo.api.mappers.WalletApiMapper;
import io.github.solvo.api.security.SecurityUtils;
import io.github.solvo.application.ports.out.WalletRepositoryPort;
import io.github.solvo.domain.entities.Wallet;
import io.github.solvo.domain.exceptions.ForbiddenException;
import io.github.solvo.domain.exceptions.WalletNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {
    private final WalletApiMapper walletApiMapper;
    private final WalletRepositoryPort walletRepositoryPort;

    public WalletController(WalletApiMapper walletApiMapper, WalletRepositoryPort walletRepositoryPort) {
        this.walletApiMapper = walletApiMapper;
        this.walletRepositoryPort = walletRepositoryPort;
    }

    @Cacheable(cacheNames = "wallets", key = "#id")
    @GetMapping("/{id}")
    public WalletResponse getWallet(@PathVariable UUID id) {
        Wallet wallet = walletRepositoryPort.findById(id)
                .orElseThrow(() -> new WalletNotFoundException(id));
        if (!wallet.getUserId().equals(SecurityUtils.getCurrentUserId())) {
            throw new ForbiddenException();
        }
        return walletApiMapper.toResponse(wallet);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public WalletResponse createWallet(@Valid @RequestBody CreateWalletRequest createWalletRequest) {
        var wallet = new Wallet(SecurityUtils.getCurrentUserId(), createWalletRequest.userType());
        var savedWallet = walletRepositoryPort.save(wallet);
        return walletApiMapper.toResponse(savedWallet);
    }
}
