package in.cdac.medinfo.loinc;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import in.cdac.medinfo.loinc.elasticsearch.ElasticSearchConfiguration;
import in.cdac.medinfo.loinc.exceptions.CodeNotFoundException;
import in.cdac.medinfo.loinc.exceptions.InternalServerException;
import in.cdac.medinfo.loinc.response.serv.model.PartModel;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;

@Component
public class ElasticSearchClient {

    private static final Logger logger = LogManager.getLogger(ElasticSearchClient.class);

    /**
     * 
     * @param text
     * @return List<PartModel>
     * @throws IOException This method gives list of classes available in loinc.
     */

    public List<PartModel> classes(String text) throws IOException {
        List<PartModel> classes = new ArrayList<>();

        try {

            BoolQuery.Builder boolQueryBuilder = QueryBuilders.bool();
            boolQueryBuilder.must(QueryBuilders.matchPhrase().field("PartTypeName").query("CLASS").build()._toQuery());

            if(text != null && text.compareToIgnoreCase("all") != 0 && !text.equals("-")) {
                String[] fieldNames = {"PartName", "PartDisplayName"};
                boolQueryBuilder.must(QueryBuilders.multiMatch().fields(text, fieldNames).query(text).build()._toQuery());
            }

            BoolQuery boolQuery = boolQueryBuilder.build();
            Query query = new Query.Builder().bool(boolQuery).build();

            SearchRequest searchRequest = new SearchRequest.Builder().index("loincpart").query(query).from(0).size(10000).build();
            SearchResponse<Object> searchResponse = ElasticSearchConfiguration.elasticsearchClient.search(searchRequest, Object.class);

            List<Hit<Object>> hits = searchResponse.hits().hits();
            TotalHits totalHits = searchResponse.hits().total();
            long count = totalHits.value();

            if(count == 0) {
                throw new NoSuchElementException("ERROR : the scales do not exist for the provided text");
            }

            for(Hit<Object> hit : hits) {
                PartModel partModel = new PartModel();
                Object source = hit.source();

                @SuppressWarnings("unchecked")
                Map<String, Object> sourceAsMap = (Map<String, Object>) source;

                partModel.setLOINC_PART_DESCRIPTION((String) sourceAsMap.get("PartDisplayName"));
                partModel.setLOINC_PART_NAME((String) sourceAsMap.get("PartName"));
                partModel.setLOINC_PART_NUMBER((String) sourceAsMap.get("PartNumber"));
                partModel.setSTATUS((String) sourceAsMap.get("Status"));
                classes.add(partModel);
            }   

        } catch (NoSuchElementException ex) {
            logger.error(ex.getMessage());
            throw new CodeNotFoundException(ex.getMessage());
        } catch (ElasticsearchException ex) {
            logger.error("ERROR : " + ex.getMessage() + " Please check connection with elasticsearch client");
            throw new InternalServerException("ERROR : " + ex.getMessage() + " Please check connection with elasticsearch client");
        } catch (Exception ex) {
            logger.error("ERROR : " + ex.getMessage());
            throw new InternalServerException("ERROR : " + ex.getMessage());
        }
        logger.info("Classes API completed");
        return classes;
    }

    /**
     * 
     * @param text
     * @return List<PartModel>
     * @throws IOException This method gives list of systems available in loinc.
     */

    public List<PartModel> systems(String text) throws IOException {
        List<PartModel> systems = new ArrayList<>();

        try {

            BoolQuery.Builder boolQueryBuilder = QueryBuilders.bool();
            boolQueryBuilder.must(QueryBuilders.matchPhrase().field("PartTypeName").query("SYSTEM").build()._toQuery());

            if(text != null && text.compareToIgnoreCase("all") != 0 && !text.equals("-")) {
                String[] fieldNames = {"PartName", "PartDisplayName"};
                boolQueryBuilder.must(QueryBuilders.multiMatch().fields(text, fieldNames).query(text).build()._toQuery());
            }

            BoolQuery boolQuery = boolQueryBuilder.build();
            Query query = new Query.Builder().bool(boolQuery).build();

            SearchRequest searchRequest = new SearchRequest.Builder().index("loincpart").query(query).from(0).size(10000).build();
            SearchResponse<Object> searchResponse = ElasticSearchConfiguration.elasticsearchClient.search(searchRequest, Object.class);

            List<Hit<Object>> hits = searchResponse.hits().hits();
            TotalHits totalHits = searchResponse.hits().total();
            long count = totalHits.value();

            if(count == 0) {
                throw new NoSuchElementException("ERROR : the systems do not exist for the provided text");
            }

            for(Hit<Object> hit : hits) {
                PartModel partModel = new PartModel();
                Object source = hit.source();

                @SuppressWarnings("unchecked")
                Map<String, Object> sourceAsMap = (Map<String, Object>) source;

                partModel.setLOINC_PART_DESCRIPTION((String) sourceAsMap.get("PartDisplayName"));
                partModel.setLOINC_PART_NAME((String) sourceAsMap.get("PartName"));
                partModel.setLOINC_PART_NUMBER((String) sourceAsMap.get("PartNumber"));
                partModel.setSTATUS((String) sourceAsMap.get("Status"));
                systems.add(partModel);
            }   

        } catch (NoSuchElementException ex) {
            logger.error(ex.getMessage());
            throw new CodeNotFoundException(ex.getMessage());
        } catch (ElasticsearchException ex) {
            logger.error("ERROR : " + ex.getMessage() + " Please check connection with elasticsearch client");
            throw new InternalServerException("ERROR : " + ex.getMessage() + " Please check connection with elasticsearch client");
        } catch (Exception ex) {
            logger.error("ERROR : " + ex.getMessage());
            throw new InternalServerException("ERROR : " + ex.getMessage());
        }
        logger.info("Systems API completed");
        return systems;
    }

    /**
     * 
     * @param text
     * @return List<PartModel>
     * @throws IOException This method gives list of scales available in loinc.
     */

    public List<PartModel> scale(String text) throws IOException {
        List<PartModel> scales = new ArrayList<>();

        try {

            BoolQuery.Builder boolQueryBuilder = QueryBuilders.bool();
            boolQueryBuilder.must(QueryBuilders.matchPhrase().field("PartTypeName").query("SCALE").build()._toQuery());

            if(text != null && text.compareToIgnoreCase("all") != 0 && !text.equals("-")) {
                String[] fieldNames = {"PartName", "PartDisplayName"};
                boolQueryBuilder.must(QueryBuilders.multiMatch().fields(text, fieldNames).query(text).build()._toQuery());
            }

            BoolQuery boolQuery = boolQueryBuilder.build();
            Query query = new Query.Builder().bool(boolQuery).build();

            SearchRequest searchRequest = new SearchRequest.Builder().index("loincpart").query(query).from(0).size(10000).build();
            SearchResponse<Object> searchResponse = ElasticSearchConfiguration.elasticsearchClient.search(searchRequest, Object.class);

            List<Hit<Object>> hits = searchResponse.hits().hits();
            TotalHits totalHits = searchResponse.hits().total();
            long count = totalHits.value();

            if(count == 0) {
                throw new NoSuchElementException("ERROR : the scales do not exist for the provided text");
            }

            for(Hit<Object> hit : hits) {
                PartModel partModel = new PartModel();
                Object source = hit.source();

                @SuppressWarnings("unchecked")
                Map<String, Object> sourceAsMap = (Map<String, Object>) source;

                partModel.setLOINC_PART_DESCRIPTION((String) sourceAsMap.get("PartDisplayName"));
                partModel.setLOINC_PART_NAME((String) sourceAsMap.get("PartName"));
                partModel.setLOINC_PART_NUMBER((String) sourceAsMap.get("PartNumber"));
                partModel.setSTATUS((String) sourceAsMap.get("Status"));
                scales.add(partModel);
            }   

        } catch (NoSuchElementException ex) {
            logger.error(ex.getMessage());
            throw new CodeNotFoundException(ex.getMessage());
        } catch (ElasticsearchException ex) {
            logger.error("ERROR : " + ex.getMessage() + " Please check connection with elasticsearch client");
            throw new InternalServerException("ERROR : " + ex.getMessage() + " Please check connection with elasticsearch client");
        } catch (Exception ex) {
            logger.error("ERROR : " + ex.getMessage());
            throw new InternalServerException("ERROR : " + ex.getMessage());
        }
        logger.info("Scales API completed");
        return scales;
    }

    /**
     * 
     * @param text
     * @return List<PartModel>
     * @throws ElasticsearchException
     * @throws IOException This method gives list of Properties available in loinc.
     */

    public List<PartModel> property(String text) throws IOException {
        List<PartModel> properties = new ArrayList<>();

        try {
            BoolQuery.Builder boolQueryBuilder = QueryBuilders.bool();
            boolQueryBuilder.must(QueryBuilders.matchPhrase().field("PartTypeName").query("PROPERTY").build()._toQuery());

            if(text != null && text.compareToIgnoreCase("all") != 0) {
                String[] fieldNames = {"PartName", "PartDisplayName"};
                boolQueryBuilder.must(QueryBuilders.multiMatch().fields(text, fieldNames).query(text).build()._toQuery());
            }

            BoolQuery boolQuery = boolQueryBuilder.build();
            Query query = new Query.Builder().bool(boolQuery).build();

            SearchRequest searchRequest = new SearchRequest.Builder().index("loincpart").query(query).from(0).size(10000).build();
            SearchResponse<Object> searchResponse = ElasticSearchConfiguration.elasticsearchClient.search(searchRequest, Object.class);

            List<Hit<Object>> hits = searchResponse.hits().hits();
            TotalHits totalHits = searchResponse.hits().total();
            long count = totalHits.value();

            if(count == 0) {
                throw new NoSuchElementException("ERROR : There is no properties exist for the text");
            }

            for(Hit<Object> hit : hits) {
                PartModel partModel = new PartModel();
                Object source = hit.source();

                @SuppressWarnings("unchecked")
                Map<String, Object> sourceAsMap = (Map<String, Object>) source;

                partModel.setLOINC_PART_DESCRIPTION((String) sourceAsMap.get("PartDisplayName"));
                partModel.setLOINC_PART_NAME((String) sourceAsMap.get("PartName"));
                partModel.setLOINC_PART_NUMBER((String) sourceAsMap.get("PartNumber"));
                partModel.setSTATUS((String) sourceAsMap.get("Status"));
                properties.add(partModel);
            }
        } catch (NoSuchElementException ex) {
            logger.error(ex.getMessage());
            throw new CodeNotFoundException(ex.getMessage());
        } catch (ElasticsearchException ex) {
            logger.error("ERROR : " + ex.getMessage() + " Please check connection with elasticsearch");
            throw new InternalServerException("ERROR : " + ex.getMessage() + " Please check connection with elasticsearch");
        } catch (Exception ex) {
            logger.error("ERROR : " + ex.getMessage());
            throw new InternalServerException("ERROR : " + ex.getMessage());
        }
        logger.info("Properties API completed");
        return properties;
    }


    /**
     * 
     * @param text
     * @return List<PartModel>
     * @throws IOException This method gives list of all timings available in Loinc.
     * 
     */

    public List<PartModel> timing(String text) throws IOException {
        List<PartModel> timings = new ArrayList<>();

        try {

            BoolQuery.Builder boolQueryBuilder = QueryBuilders.bool();
            boolQueryBuilder.must(QueryBuilders.matchPhrase().field("PartTypeName").query("METHOD").build()._toQuery());

            if(text != null && text.compareToIgnoreCase("all") != 0) {
                String[] fieldNames = {"PartName", "PartDisplayName"};
                boolQueryBuilder.must(QueryBuilders.multiMatch().fields(text, fieldNames).query(text).build()._toQuery());
            }

            BoolQuery boolQuery = boolQueryBuilder.build();
            Query query = new Query.Builder().bool(boolQuery).build();

            SearchRequest searchRequest = new SearchRequest.Builder().index("loincpart").query(query).from(0).size(10000).build();
            SearchResponse<Object> searchResponse = ElasticSearchConfiguration.elasticsearchClient.search(searchRequest, Object.class);

            List<Hit<Object>> hits = searchResponse.hits().hits();
            TotalHits totalHits = searchResponse.hits().total();
            long count = totalHits.value();

            if(count == 0) {
                throw new NoSuchElementException("ERROR : No timings found for the provided text");
            }

            for(Hit<Object> hit : hits) {
                PartModel partModel = new PartModel();
                Object source = hit.source();

                @SuppressWarnings("unchecked")
                Map<String, Object> sourceAsMap = (Map<String, Object>) source;

                partModel.setLOINC_PART_DESCRIPTION((String) sourceAsMap.get("PartDisplayName"));
                partModel.setLOINC_PART_NAME((String) sourceAsMap.get("PartName"));
                partModel.setLOINC_PART_NUMBER((String) sourceAsMap.get("PartNumber"));
                partModel.setSTATUS((String) sourceAsMap.get("Status"));
                timings.add(partModel);
            }

        } catch (NoSuchElementException ex) {
            logger.error(ex.getMessage());
            throw new CodeNotFoundException(ex.getMessage());
        } catch (ElasticsearchException ex) {
            logger.error("ERROR : " + ex.getMessage() + " Please check your connection to Elasticsearch");
            throw new InternalServerException("ERROR : " + ex.getMessage() + " Please check your connection to Elasticsearch");
        } catch (Exception ex) {
            logger.error("ERROR : " + ex.getMessage());
            throw new InternalServerException(" ERROR : " + ex.getMessage());
        }
        logger.info("Timings API completed");
        return timings;
    }


    /**
	 * @param text
	 * @return List<PartModel>
	 * @throws IOException This method gives list of all methods available in Loinc.
	 */

    public List<PartModel> methods(String text) throws IOException {
        List<PartModel> methods = new ArrayList<PartModel>();

        try {
            BoolQuery.Builder boolQueryBuilder = QueryBuilders.bool();
            boolQueryBuilder.must(QueryBuilders.matchPhrase().field("PartTypeName").query("METHOD").build()._toQuery());

            if(text != null && text.compareToIgnoreCase("all") != 0) {
                String[] fieldNames = {"PartName", "PartDisplayName"};
                boolQueryBuilder.must(QueryBuilders.multiMatch().fields(text, fieldNames).query(text).build()._toQuery());
            }

            BoolQuery boolQuery = boolQueryBuilder.build();
            Query query = new Query.Builder().bool(boolQuery).build();

            SearchRequest searchRequest = new SearchRequest.Builder().index("loincpart").query(query).from(0).size(10000).build();
            SearchResponse<Object> searchResponse = ElasticSearchConfiguration.elasticsearchClient.search(searchRequest, Object.class);

            List<Hit<Object>> hits = searchResponse.hits().hits();
            TotalHits totalHits = searchResponse.hits().total();
            long count = totalHits.value();

            if(count == 0) {
                throw new NoSuchElementException("ERROR : method with the provided text does not exist");
            }

            for(Hit<Object> hit : hits) {
                PartModel partModel = new PartModel();
                Object source = hit.source();

                @SuppressWarnings("unchecked")
                Map<String, Object> sourceAsMap = (Map<String, Object>) source;

                partModel.setLOINC_PART_NUMBER((String) sourceAsMap.get("PartNumber"));
                partModel.setLOINC_PART_NAME((String) sourceAsMap.get("PartName"));
                partModel.setLOINC_PART_DESCRIPTION((String) sourceAsMap.get("PartDisplayName"));
                partModel.setSTATUS((String) sourceAsMap.get("Status"));
                methods.add(partModel);
            }

        } catch (NoSuchElementException ex) {
            logger.error(ex.getMessage());
            throw new CodeNotFoundException(ex.getMessage());
        } catch (ElasticsearchException ex) {
            logger.error("ERROR : " + ex.getMessage() + " Please check connection with Elasticsearch");
            throw new InternalServerException("ERROR : " + ex.getMessage() + " Please check connection with Elasticsearch");
        } catch (Exception ex) {
            logger.error("ERROR : " + ex.getMessage());
            throw new InternalServerException("ERROR : " + ex.getMessage());
        } 
        logger.info("Methods API completed");
        return methods;
    }
    

    /**
     * @param text
     * @return PartModel POJO
     * @throws IOException This method returns the components available in loinc for given text.
     * 
     */

    public List<PartModel> component(String text) throws IOException {
        List<PartModel> components = new ArrayList<>();
        
        try {
            BoolQuery.Builder boolQueryBuilder = QueryBuilders.bool();
            boolQueryBuilder.must(QueryBuilders.matchPhrase().field("PartTypeName").query("COMPONENT").build()._toQuery());
            if(text != null && text.compareToIgnoreCase("all") != 0) {
                String[] fieldNames = {"PartName, PartDisplayName"};
                boolQueryBuilder.must(QueryBuilders.multiMatch().fields(text, fieldNames).query(text).build()._toQuery());
            }
            
            BoolQuery boolQuery = boolQueryBuilder.build();
            Query query = new Query.Builder().bool(boolQuery).build();
            
            SearchRequest searchRequest = new SearchRequest.Builder().index("loincpart").query(query).from(0).size(10000).build();
            
            SearchResponse<Object> searchResponse = ElasticSearchConfiguration.elasticsearchClient.search(searchRequest, Object.class);

            List<Hit<Object>> hits = searchResponse.hits().hits();
            TotalHits totalHits = searchResponse.hits().total();
            long count = totalHits.value();

            System.out.println(count);
            if(count == 0) {
                throw new InternalServerException("ERROR : component with provided text does not exist");
            }

            for(Hit<Object> hit : hits) {
                PartModel partModel = new PartModel();
                Object source = hit.source();
                @SuppressWarnings("unchecked")
                Map<String,Object> sourceAsMap = (Map<String, Object>) source;
                partModel.setLOINC_PART_DESCRIPTION((String) sourceAsMap.get("PartDisplayName"));
                partModel.setLOINC_PART_NAME((String) sourceAsMap.get("PartName"));
                partModel.setLOINC_PART_NUMBER((String) sourceAsMap.get("PartNumber"));
                partModel.setSTATUS((String) sourceAsMap.get("Status"));
                components.add(partModel);
            }
        } catch(NoSuchElementException ex) {
            logger.error(ex.getMessage());
            throw new CodeNotFoundException(ex.getMessage());
        } catch(ElasticsearchException ex) {
            logger.error("ERROR: " + ex.getMessage() + " Please check connection with elasticsearch");
            throw new InternalServerException("ERROR : " + ex.getMessage() + " Please check connection with elasticsearch");
        } catch(Exception ex) {
            logger.error("ERROR : " + ex.getMessage());
            throw new InternalServerException("ERROR : " + ex.getMessage());
        }
        logger.info("Components API completed");
        return components;
    } 

    /*
     * This method gives list of all units available in loinc.
     * @throws IOException.
     * @return List<String> 
     */

    public List<String> exampleUnits() throws IOException {
        List<String> setWithUniqueValues = new ArrayList<String>();
        try {
            TermsAggregation termsAggregation = new TermsAggregation.Builder().field("EXAMPLE_UCUM_UNITS.keyword").size(10000).build();

            SearchRequest searchRequest = new SearchRequest.Builder().index("loinc").aggregations("EXAMPLE_UCUM_UNITS", new Aggregation(termsAggregation)).size(10000).build();

            SearchResponse<Object> searchResponse = ElasticSearchConfiguration.elasticsearchClient.search(searchRequest, Object.class);

            Aggregate aggregate = searchResponse.aggregations().get("units");

            List<StringTermsBucket> sterms = aggregate.sterms().buckets().array();

            logger.info("Size of buckets : " + sterms.size());

            for(StringTermsBucket bucket : sterms) {
                String value = bucket.key().toLowerCase();
                setWithUniqueValues.add(value);
            }

        } catch (ElasticsearchException ex) {
            logger.error(ex.getMessage() + "Please check connection with elasticsearch");
            throw new InternalServerException("ERROR : " + ex.getMessage() + " Please check connection with elasticsearch");
        } catch (Exception ex) {
            logger.error("ERROR : " + ex.getMessage());
            throw new InternalServerException("ERROR : " + ex.getMessage() + " Please check connection with elasticsearch");
        }
        logger.info("All example unit types collected");
        return setWithUniqueValues;
    }

    public String getVersionLastChanged() throws IOException {
        double maxLoincVersion = 1.0d;

        if(ElasticSearchConfiguration.elasticsearchClient != null) {
            
            TermsAggregation termAggregation = new TermsAggregation.Builder().field("VersionLastChanged.keyword").size(10000).build();

            SearchRequest searchRequest = new SearchRequest.Builder().index("loinc").aggregations("version", new Aggregation(termAggregation)).size(10000).build();

            SearchResponse<Object> searchResponse = ElasticSearchConfiguration.elasticsearchClient.search(searchRequest, Object.class);

            Aggregate aggregate = searchResponse.aggregations().get("version");

            List<StringTermsBucket> stringTermsBucket = aggregate.sterms().buckets().array();

            for(StringTermsBucket bucket : stringTermsBucket) {
                String loincVersion = bucket.key().toLowerCase();
                loincVersion = loincVersion.replaceAll("[^\\d.]", "");
                Double version = Double.valueOf(loincVersion);
                if(version > maxLoincVersion)
                    maxLoincVersion = version;
            }
        }
        String versionValue = Double.toString(maxLoincVersion);
        return versionValue;
    }
}
