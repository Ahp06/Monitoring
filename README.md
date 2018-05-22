# Monitoring
Monitoring of the ENSISA mechanics workshop developed with Java, ElasticSearch and Grafana.

# First you need : 

1 - To install ElasticSearch from the following link : https://www.elastic.co/fr/downloads/elasticsearch .
    For more information for how to install it : https://www.elastic.co/guide/en/elasticsearch/reference/current/_installation.html
    
2 - To install Grafana from the following link : https://grafana.com/grafana/download . 

# How to use : 

In the main class Monitoring.java, you can load a configuration file (.txt) compatible with JSON format or
you can create your configuration : 

```
//By file
MonitoringConfiguration config = new MonitoringConfiguration("...\\config.txt");

//With default constructor 
MonitoringConfiguration config = new MonitoringConfiguration(yourClusterNameES, yourHostES, yourPortES, yourHostSQL,...); 
```

By default ElasticSearch use 9200 and 9300 ports and the cluster name is "elasticsearch". 

After launching Grafana and ElasticSearch, you must configure a new datasource, see this link : http://docs.grafana.org/features/datasources/elasticsearch/ , and you can start Monitoring.java. 

This project was done to retrieve machine state changes, you have to adapt it to do another monitoring. 
