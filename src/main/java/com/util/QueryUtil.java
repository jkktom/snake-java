package com.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.HashMap;
import java.util.Map;

public class QueryUtil {
    private static final Map<String, String> queries = new HashMap<>();
    
    static {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(QueryUtil.class.getResourceAsStream("/queries.xml"));
            
            NodeList queryNodes = document.getElementsByTagName("query");
            for (int i = 0; i < queryNodes.getLength(); i++) {
                Element query = (Element) queryNodes.item(i);
                String id = query.getAttribute("id");
                String sql = query.getTextContent().trim();
                queries.put(id, sql);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load queries.xml", e);
        }
    }
    
    public static String getQuery(String id) {
        String query = queries.get(id);
        if (query == null) {
            throw new IllegalArgumentException("Query not found: " + id);
        }
        return query;
    }
}
