package ru.tigerbank.infrastructure.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import ru.tigerbank.application.interfaces.repository.IBankAccountRepository;
import ru.tigerbank.domain.model.BankAccount;

@Repository
public class LocalBankAccountRepository implements IBankAccountRepository {
    private final Map<UUID, BankAccount> storage = new HashMap<>();

    @Override
    public BankAccount save(BankAccount account) {
        storage.put(account.getId(), account);
        return account;
    }

    @Override
    public Optional<BankAccount> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<BankAccount> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean deleteById(UUID id) {
        return storage.remove(id) != null;
    }

    @Override
    public void deleteAll() {
        storage.clear();
    }
}