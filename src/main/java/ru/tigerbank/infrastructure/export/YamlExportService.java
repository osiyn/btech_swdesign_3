package ru.tigerbank.infrastructure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import ru.tigerbank.application.interfaces.services.IExportService;
import ru.tigerbank.domain.model.BankAccount;

public class YamlExportService implements IExportService {
    private final Yaml yaml;

    public YamlExportService() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        this.yaml = new Yaml(options);
    }

    @Override
    public String export(List<BankAccount> accounts) {
        Map<String, Object> exportData = new HashMap<>();
        exportData.put("format", "TigerBank Finance Export");
        exportData.put("version", "1.0");
        exportData.put("exported_at", java.time.LocalDateTime.now().toString());

        List<Map<String, Object>> accountsData = new ArrayList<>();
        for (BankAccount account : accounts) {
            Map<String, Object> accountMap = new HashMap<>();
            accountMap.put("id", account.getId().toString());
            accountMap.put("name", account.getName());
            accountMap.put("balance", account.getBalance().toString());
            accountMap.put("created_at", account.getCreatedAt().toString());
            accountsData.add(accountMap);
        }
        exportData.put("accounts", accountsData);

        return yaml.dump(exportData);
    }

    @Override
    public String getFormatName() {
        return "YAML";
    }
}