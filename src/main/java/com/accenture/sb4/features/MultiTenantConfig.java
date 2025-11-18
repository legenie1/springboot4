package com.accenture.sb4.features;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * REAL ADVANTAGE: Multi-Tenant Database Configuration
 *
 * Scenario: You have multiple clients (tenants), each with their own database.
 * You don't know the list of tenants at compile time - they come from a config file or database.
 *
 * WITHOUT programmatic registration: You'd need to manually create @Bean methods for each tenant
 * WITH programmatic registration: You can dynamically create beans based on runtime configuration
 */
@Configuration
@Import(MultiTenantConfig.MultiTenantRegistrar.class)
public class MultiTenantConfig {

    static class MultiTenantRegistrar implements ImportBeanDefinitionRegistrar {

        @Override
        public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

            // In real app, this would come from database or config file
            List<TenantInfo> tenants = loadTenantsFromConfiguration();

            // Dynamically register a DataSource bean for EACH tenant
            for (TenantInfo tenant : tenants) {
                String beanName = tenant.id() + "DataSource";

                BeanDefinitionBuilder builder = BeanDefinitionBuilder
                        .genericBeanDefinition(TenantDataSource.class)
                        .addConstructorArgValue(tenant.id())
                        .addConstructorArgValue(tenant.dbUrl())
                        .addConstructorArgValue(tenant.dbUser());

                registry.registerBeanDefinition(beanName, builder.getBeanDefinition());

                System.out.println("✓ Registered DataSource for tenant: " + tenant.id());
            }
        }

        private List<TenantInfo> loadTenantsFromConfiguration() {
            // Imagine this comes from application.yaml or database
            return List.of(
                    new TenantInfo("accenture", "jdbc:postgresql://localhost:5432/accenture_db", "acc_user"),
                    new TenantInfo("microsoft", "jdbc:postgresql://localhost:5432/microsoft_db", "ms_user"),
                    new TenantInfo("google", "jdbc:postgresql://localhost:5432/google_db", "google_user")
            );
        }
    }
}

record TenantInfo(String id, String dbUrl, String dbUser) {}

class TenantDataSource {
    private final String tenantId;
    private final String dbUrl;
    private final String dbUser;

    public TenantDataSource(String tenantId, String dbUrl, String dbUser) {
        this.tenantId = tenantId;
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
    }

    public String connect() {
        return "Connected to " + tenantId + " database: " + dbUrl;
    }

    public String getTenantId() {
        return tenantId;
    }
}

@RestController
class TenantController {

    private final Map<String, TenantDataSource> dataSources;

    // Spring automatically injects ALL TenantDataSource beans as a Map
    public TenantController(Map<String, TenantDataSource> dataSources) {
        this.dataSources = dataSources;
    }

    @GetMapping("/api/tenants")
    public List<String> listTenants() {
        return dataSources.values().stream()
                .map(TenantDataSource::getTenantId)
                .toList();
    }

    @GetMapping("/api/tenants/{tenantId}/connect")
    public String connectToTenant(@PathVariable String tenantId) {
        TenantDataSource ds = dataSources.get(tenantId + "DataSource");
        if (ds == null) {
            return "Tenant not found: " + tenantId;
        }
        return ds.connect();
    }
}