package com.m4sg.crm4marketingsunset;

import com.bazaarvoice.dropwizard.assets.AssetsBundleConfiguration;
import com.bazaarvoice.dropwizard.assets.AssetsConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by Juan on 12/22/2014.
 */
public class CRM4MarketingSunsetConfiguration extends Configuration implements AssetsBundleConfiguration {
    @Valid
    @NotNull
    private DataSourceFactory databaseM4C = new DataSourceFactory();
    @Valid
    @NotNull
    private DataSourceFactory databaseOnlineDB = new DataSourceFactory();
    @Valid
    @NotNull
    private DataSourceFactory databaseInventario = new DataSourceFactory();

    @Valid
    @NotNull
    @JsonProperty
    private final AssetsConfiguration assets = new AssetsConfiguration();

    public DataSourceFactory getDatabaseM4C() {
        return databaseM4C;
    }

    @JsonProperty("databaseM4C")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.databaseM4C = dataSourceFactory;
    }

    public DataSourceFactory getDatabaseOnlineDB() {
        return databaseOnlineDB;
    }
    @JsonProperty("databaseOnlineDB")
    public void setDatabaseOnlineDB(DataSourceFactory databaseOnlineDB) {
        this.databaseOnlineDB = databaseOnlineDB;
    }

    public DataSourceFactory getDatabaseInventario() {
        return databaseInventario;
    }
    @JsonProperty("databaseInventarioDB")
    public void setDatabaseInventario(DataSourceFactory databaseInventario) {
        this.databaseInventario = databaseInventario;
    }

    @Override
    public AssetsConfiguration getAssetsConfiguration() {
        return assets;
    }
}
