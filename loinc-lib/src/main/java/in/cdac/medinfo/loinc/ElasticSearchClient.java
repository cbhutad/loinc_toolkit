package in.cdac.medinfo.loinc;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchPhrasePrefixQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.json.JsonData;
import in.cdac.medinfo.loinc.commons.EnumClassTypes;
import in.cdac.medinfo.loinc.commons.EnumStatus;
import in.cdac.medinfo.loinc.elasticsearch.ElasticSearchConfiguration;
import in.cdac.medinfo.loinc.exceptions.CodeNotFoundException;
import in.cdac.medinfo.loinc.exceptions.InternalServerException;
import in.cdac.medinfo.loinc.response.serv.model.PanelModel;
import in.cdac.medinfo.loinc.response.serv.model.PartModel;
import in.cdac.medinfo.loinc.response.serv.model.SearchModel;
import in.cdac.medinfo.loinc.response.serv.model.ChildModel;
import in.cdac.medinfo.loinc.response.serv.model.Model;

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
     * @param loincNum
     * @return List<Model>
     */

    public Model lookup(String loincNum) {

        Model concept;

        try {
            
            //Parent Loinc Concept Query code
            TermQuery termQuery = new TermQuery.Builder().field("LOINC_NUM.keyword").value(FieldValue.of(loincNum)).build();
            Query query = new Query.Builder().term(termQuery).build();
            SearchRequest searchRequest = new SearchRequest.Builder().index("loinc").query(query).build();
            SearchResponse<Object> searchResponse = ElasticSearchConfiguration.elasticsearchClient.search(searchRequest, Object.class); 

            List<Hit<Object>> hit = searchResponse.hits().hits();
            TotalHits totalHits = searchResponse.hits().total();
            long count = totalHits.value();

            if(count == 0) {
                throw new NoSuchElementException("ERROR :  code " + loincNum + " not found");
            }

            concept = new Model();
            Object source = hit.get(0).source();

            @SuppressWarnings("unchecked")
            Map<String, Object> sourceAsMap = (Map<String, Object>) source;

            concept.setCLASS((String) sourceAsMap.get("CLASS"));

            String classType = (String) sourceAsMap.get("CLASSTYPE");
            if(classType.compareToIgnoreCase("1") == 0) {
                concept.setCLASSTYPE("Laboratory");
            } else if(classType.compareToIgnoreCase("2") == 0) {
                concept.setCLASSTYPE("Clinical");
            } else if(classType.compareToIgnoreCase("3") == 0) {
                concept.setCLASSTYPE("Attachment");
            } else {
                concept.setCLASSTYPE("Survey");
            }

            concept.setCOMPONENT((String) sourceAsMap.get("COMPONENT"));
            concept.setDisplayName((String) sourceAsMap.get("DisplayName"));
            concept.setExampleUnits((String) sourceAsMap.get("EXAMPLE_UCUM_UNITS"));
            concept.setLOINC((String) sourceAsMap.get("LOINC_NUM"));
            concept.setLONG_COMMON_NAME((String) sourceAsMap.get("LONG_COMMON_NAME"));
            concept.setMETHOD_TYP((String) sourceAsMap.get("METHOD_TYP"));
            concept.setORDER_OBS((String) sourceAsMap.get("ORDER_OBS"));
            concept.setPROPERTY((String) sourceAsMap.get("PROPERTY"));
            concept.setRelatedNames((String) sourceAsMap.get("RELATEDNAMES2"));
            concept.setSCALE_TYP((String) sourceAsMap.get("SCALE_TYP"));
            concept.setSTATUS((String) sourceAsMap.get("STATUS"));
            concept.setSYSTEM((String) sourceAsMap.get("SYSTEM"));
            concept.setShortName((String) sourceAsMap.get("SHORTNAME"));
            concept.setTIME_ASPCT((String) sourceAsMap.get("TIME_ASPCT"));
            concept.setVersionFirstReleased((String) sourceAsMap.get("VersionFirstReleased"));
            concept.setVersionLastChanged((String) sourceAsMap.get("VersionLastChanged"));

            //Searching for child concepts if any
            MatchPhrasePrefixQuery matchPhrasePrefixQuery = new MatchPhrasePrefixQuery.Builder().field("ParentLoinc").query(loincNum).build();
            Query childConceptsQuery = new Query.Builder().matchPhrasePrefix(matchPhrasePrefixQuery).build();
            SearchRequest childConceptSearchRequest = new SearchRequest.Builder().index("loincPanel").query(childConceptsQuery).from(0).size(10000).build();
            SearchResponse<Object> childConceptSearchResponse = ElasticSearchConfiguration.elasticsearchClient.search(childConceptSearchRequest, Object.class);

            List<Hit<Object>> childHits = childConceptSearchResponse.hits().hits();
            TotalHits childTotalHits = childConceptSearchResponse.hits().total();

            List<ChildModel> childList = new ArrayList<ChildModel>();

            if(childTotalHits.value() != 0) {

                String parentId = "";

                for(Hit<Object> hitPanel : childHits) {

                    ChildModel childConcept = new ChildModel();
                    Object childSource = hitPanel.source();

                    @SuppressWarnings("unchecked")
                    Map<String, Object> childSourceAsMap = (Map<String, Object>) childSource;

                    String parentIdCompare = parentId;
                    parentId = (String) childSourceAsMap.get("ParentId");

                    if(parentIdCompare.compareToIgnoreCase(parentId) != 0 && parentIdCompare.compareToIgnoreCase("") != 0)
                        break;

                    String loincChild = (String) childSourceAsMap.get("Loinc");
                    if(loincChild.compareToIgnoreCase(loincNum) != 0) {

                        MatchPhrasePrefixQuery matchPhrasePrefixQueryGrandChildren = new MatchPhrasePrefixQuery.Builder().field("ParentLoinc").query(loincChild).build();
                        Query grandChildrenQuery = new Query.Builder().matchPhrasePrefix(matchPhrasePrefixQueryGrandChildren).build();
                        SearchRequest grandChildrenSearchRequest = new SearchRequest.Builder().index("loincPanel").query(grandChildrenQuery).from(0).size(10000).build();
                        SearchResponse<Object> grandChildrenSearchResponse = ElasticSearchConfiguration.elasticsearchClient.search(grandChildrenSearchRequest, Object.class);

                        List<Hit<Object>> grandChildHits = grandChildrenSearchResponse.hits().hits();
                        TotalHits grandChildTotalHits = grandChildrenSearchResponse.hits().total();
                        long grandChildrenCount = grandChildTotalHits.value();

                        if (grandChildrenCount != 0) {
                         
                            List<ChildModel> grandChildList = new ArrayList<>();

                            for(Hit<Object> hitG3 : grandChildHits) {

                                ChildModel grandChildConcept = new ChildModel();
                                Object grandChildSource = hitG3.source();

                                @SuppressWarnings("unchecked")
                                Map<String, Object> grandChildSourceAsMap = (Map<String, Object>) grandChildSource;

                                String grandChildLoinc = (String) grandChildSourceAsMap.get("Loinc");
                                if(grandChildLoinc.compareTo(loincChild) != 0) {
                                    grandChildList.add(setChildModel(grandChildSourceAsMap, grandChildConcept));
                                }
                            }
                            childConcept.setGrandChildren(grandChildList);
                        }
                        childList.add(setChildModel(childSourceAsMap, childConcept));
                    }
                }
                concept.setChildren(childList);
            }
            
        } catch (NoSuchElementException ex) {
            logger.error(ex.getMessage());
            throw new CodeNotFoundException(ex.getMessage());
        } catch (ElasticsearchException ex) {
            logger.error("ERROR : " + ex.getMessage() + " Please check your connection with Elasticsearch");
            throw new InternalServerException("ERROR : " + ex.getMessage() + " Please check your connection with Elasticsearch");
        } catch (Exception ex) {
            logger.error("ERROR : " + ex.getMessage());
            throw new InternalServerException("ERROR : " + ex.getMessage());
        }

        return concept;
    }

    /**
     * @param sourceAsMap
     * @param childConcept
     * @return ChildModel
     * @throws IOException
     */

    public ChildModel setChildModel(Map<String,Object> sourceAsMap, ChildModel childConcept) throws IOException {
        ChildModel concept = new ChildModel();

        concept.setLOINC((String) sourceAsMap.get("Loinc"));
        concept.setObservationRequiredInPanel((String) sourceAsMap.get("ObservationRequiredInPanel"));
        
        MatchPhrasePrefixQuery matchPhrasePrefixQuery = new MatchPhrasePrefixQuery.Builder().field("LOINC_NUM").query(concept.getLOINC()).build();
        Query query = new Query.Builder().matchPhrasePrefix(matchPhrasePrefixQuery).build();
        SearchRequest searchRequest = new SearchRequest.Builder().index("loinc").query(query).build();
        SearchResponse<Object> searchResponse = ElasticSearchConfiguration.elasticsearchClient.search(searchRequest, Object.class);

        List<Hit<Object>> hits = searchResponse.hits().hits();

        Object source = hits.get(0).source();
        
        @SuppressWarnings("unchecked")
        Map<String,Object> sourceMap = (Map<String, Object>) source;

        concept.setExample_UCUM_UNITS((String) sourceMap.get("EXAMPLE_UCUM_UNITS"));
        concept.setName((String) sourceMap.get("SHORTNAME"));

        return concept;
    }

    /**
     * 
     * @param Boolean sortByRank 
     * @param EnumClassTypes type 
     * @param EnumStatus status
     * @param int limit
     * @param String className
     * @param String component
     * @param String exampleUnits
     * @param String panelType
     * @param String property
     * @param String method
     * @param String scale
     * @param String system
     * @param String term
     * @param String timing
     * @return List<SearchModel>
     * @throws 
     */
    
    public List<SearchModel> search(Boolean sortByRank, EnumClassTypes type, EnumStatus status, int limit, String className, String component, String exampleUnits, String panelType, String property, String method, String scale, String system, String term, String timing) {
        
        List<SearchModel> concepts = new ArrayList<>();

        try {
            BoolQuery.Builder boolQueryBuilder = QueryBuilders.bool();

            if(term != null ) {

                List<String> fieldNamesList = new ArrayList<String>() {
                    {
                        add("COMPONENT");
                        add("DisplayName");
                        add("LONG_COMMON_NAME");
                        add("RELATEDNAMES2");
                        add("SHORTNAME");
                        add("SYSTEM");
                    }
                };

                //Case 1 : term contains ","~. Example -> Component:"functional panel"~1 of proximity search.
                if(term.contains("\"") && term.contains("\"~")) {
                    if(term.contains(":")) {
                        String[] terms = term.replaceAll("\"", "").split(":");
                        String[] data = terms[1].split("~");
                        boolQueryBuilder.must(QueryBuilders.matchPhrase().field(terms[0]).query(data[1]).slop(Integer.parseInt(data[1])).build()._toQuery());
                    } else {
                        String[] terms = term.replaceAll("\"", "").split("~");
                        boolQueryBuilder.must(QueryBuilders.matchPhrase().field("LONG_COMMON_NAME").query(terms[0]).slop(Integer.parseInt(terms[1])).build()._toQuery());
                    }
                }
                //Case 2 : term contians " +", " -", "~", "(" or ")". Example -> bacillus +anthracis, (influenza OR rhinovirus), -haemophilus
                else if(term.contains(" +") || term.contains(" -") || term.contains("~") || (term.contains("(") && term.contains(")"))) {
                    if(term.contains("and") || term.contains("or")) {
                        boolQueryBuilder.should(QueryBuilders.queryString().query(term).build()._toQuery());
                    } else {
                        boolQueryBuilder.must(QueryBuilders.queryString().query(term).build()._toQuery());
                    }
                }
                //Case 3 : term contains ":" and "(", "[", ")", "]". Example -> VersionLastChanged:[2 to 2.48] also called range search with lower and upper bounds
                else if(term.contains(":") && (term.contains("(") || term.contains("[")) && (term.contains(")") || term.contains("]"))) {
                    String[] parameters = term.split(":");
                    String range = parameters[1].replaceAll("(","").replaceAll("[", "").replaceAll(")","").replaceAll("]", "");
                    String[] ranges = range.split("to");
                    boolQueryBuilder.must(QueryBuilders.range().field(parameters[0]).from(JsonData.of(ranges[0])).to(JsonData.of(ranges[1])).build()._toQuery());
                }
                //Case 4 : term contains "*" or "?". Wildcard characters
                else if(term.contains("*") || term.contains("?")) {
                    if(term.contains(":")) {
                        String[] terms = term.split(":");
                        boolQueryBuilder.must(QueryBuilders.wildcard().field(terms[0]).queryName(terms[1]).build()._toQuery());
                    } else {
                        boolQueryBuilder.must(QueryBuilders.wildcard().field("LONG_COMMON_NAME").queryName(term).build()._toQuery());
                    }
                }
                //Case 5 : term contains " "
                else if(term.contains(" ")) {
                    String[] terms = term.split(" ");
                    for (String temp : terms) {
                        boolQueryBuilder.must(QueryBuilders.multiMatch().fields(fieldNamesList).query(temp).type(TextQueryType.BoolPrefix).build()._toQuery());
                    }
                }
                else {
                    boolQueryBuilder.must(QueryBuilders.multiMatch().fields(fieldNamesList).query(term).type(TextQueryType.BoolPrefix).build()._toQuery());
                }

            }

            
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new InternalServerException("ERROR : " + ex.getMessage());
        }
        return concepts;
    }


    /**
     * 
     * @param className
     * @param status
     * @param panelType
     * @return List<PanelModel>
     * @throws IOException
     * 
     * Provides facility to retrieve information of particular panel by specifying class parameter.
     */

    public List<PanelModel> expandPanel(String className, String panelType, String status) {
        List<PanelModel> panels = new ArrayList<>();

        try {

            if(className != null) {
                BoolQuery.Builder boolQueryBuilder = QueryBuilders.bool();
                boolQueryBuilder.must(QueryBuilders.matchPhrase().field("CLASS").query(className).build()._toQuery());

                if(panelType != null && panelType.compareToIgnoreCase("all") != 0) {
                    boolQueryBuilder.must(QueryBuilders.matchPhrase().field("PanelType").query(panelType).build()._toQuery());
                }
                if(status != null && status.compareToIgnoreCase("all") != 0) {
                    boolQueryBuilder.must(QueryBuilders.matchPhrase().field("STATUS").query(status).build()._toQuery());
                }

                BoolQuery boolQuery = boolQueryBuilder.build();
                Query query = new Query.Builder().bool(boolQuery).build();

                SearchRequest searchRequest = new SearchRequest.Builder().index("loinc").query(query).from(0).size(10000).build();
                SearchResponse<Object> searchResponse = ElasticSearchConfiguration.elasticsearchClient.search(searchRequest, Object.class);

                List<Hit<Object>> hits = searchResponse.hits().hits();
                TotalHits totalHits = searchResponse.hits().total();
                long count = totalHits.value();

                if(count == 0) {
                    throw new NoSuchElementException("ERROR : class name " + className + " with given filteration parameter not found or class does not belong to any panel");
                }

                for(Hit<Object> hit : hits) {
                    PanelModel panelModel = new PanelModel();
                    Object source = hit.source();

                    @SuppressWarnings("unchecked")
                    Map<String, Object> sourceAsMap = (Map<String, Object>) source;

                    panelModel.setLOINC_NUM((String) sourceAsMap.get("LOINC_NUM"));
                    panelModel.setLONG_COMMON_NAME((String) sourceAsMap.get("LONG_COMMON_NAME"));

                    panels.add(panelModel);
                }

            } else {
                throw new NoSuchElementException("ERROR : class name " + className + " with given filteration parameter not found or class does not belong to any panel");
            }
        } catch (NoSuchElementException ex) {
            logger.error(ex.getMessage());
            throw new CodeNotFoundException(ex.getMessage());
        } catch (ElasticsearchException ex) {
            logger.error("ERROR : " + ex.getMessage() + " Please check connection with Elasticsearch");
            throw new InternalServerException("ERROR : " + ex.getMessage() + " Please check connection with Elasticsearch");
        }catch (Exception ex) {
            logger.error("ERROR : " + ex.getMessage());
            throw new InternalServerException("ERROR : " + ex.getMessage());
        }
        logger.info("Completed ExpandPanels API");
        return panels;
    } 

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
