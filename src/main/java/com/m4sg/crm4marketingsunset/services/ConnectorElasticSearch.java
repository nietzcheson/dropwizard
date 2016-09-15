package com.m4sg.crm4marketingsunset.services;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * Created by Fernando on 27/08/2015.
 */
public class ConnectorElasticSearch {

    public Client getClient(){

        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", "Elastic_Sunset")
                .build();

        Client client = new TransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress("10.194.21.107", 9300));
        return  client;
    }

    public SearchResponse search(String term){

        SearchResponse searchResponse = getClient().prepareSearch("dncm4c").setTypes("dnc")
                .setQuery(QueryBuilders.matchQuery("TELEFONO_LADA", term))
                .setSize(25)
                .execute()
                .actionGet();


        return searchResponse;
    }
}
