package com.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryUtil {
    private static final Logger logger = LoggerFactory.getLogger(QueryUtil.class);
    private static final Map<String, String> queries = new HashMap<>();
    
    static {
        try {
            loadQueriesFromXml();
        } catch (Exception e) {
            logger.warn("queries.xml을 불러올 수 없습니다. 기본 쿼리를 사용합니다.");
            setDefaultQueries();
        }
    }

    private static void loadQueriesFromXml() throws Exception {
        InputStream inputStream = QueryUtil.class.getResourceAsStream("/queries.xml");
        if (inputStream == null) {
            throw new Exception("queries.xml 파일을 찾을 수 없습니다.");
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);
        
        NodeList queryNodes = document.getElementsByTagName("query");
        for (int i = 0; i < queryNodes.getLength(); i++) {
            Element query = (Element) queryNodes.item(i);
            String id = query.getAttribute("id");
            String sql = query.getTextContent().trim();
            queries.put(id, sql);
        }
    }

    private static void setDefaultQueries() {
        // User queries
        queries.put("getAllUsers", 
            "SELECT * FROM users ORDER BY id");
        queries.put("getUserById", 
            "SELECT * FROM users WHERE id = ?");
        queries.put("getUserByUsername", 
            "SELECT * FROM users WHERE username = ?");
        queries.put("addUser", 
            "INSERT INTO users (username) VALUES (?)");

        // Game result queries
        queries.put("getAllGameResults", 
            "SELECT * FROM game_results ORDER BY created_at DESC");
        queries.put("getGameResultById", 
            "SELECT * FROM game_results WHERE id = ?");
        queries.put("getGameResultsByUserId", 
            "SELECT * FROM game_results WHERE user_id = ? ORDER BY created_at DESC");
        queries.put("addGameResult", 
            "INSERT INTO game_results (user_id, score, duration) VALUES (?, ?, ?)");

        // Comment queries
        queries.put("getAllComments", 
            "SELECT * FROM comments ORDER BY created_at DESC");
        queries.put("getCommentById", 
            "SELECT * FROM comments WHERE id = ?");
        queries.put("getAllCommentsByUserId", 
            "SELECT * FROM comments WHERE user_id = ? ORDER BY created_at DESC");
        queries.put("addComment", 
            "INSERT INTO comments (user_id, game_result_id, content) VALUES (?, ?, ?)");

        logger.info("기본 쿼리가 설정되었습니다.");
    }
    
    public static String getQuery(String id) {
        String query = queries.get(id);
        if (query == null) {
            logger.error("쿼리를 찾을 수 없습니다: {}", id);
            throw new IllegalArgumentException("해당 ID의 쿼리를 찾을 수 없습니다: " + id);
        }
        return query;
    }
}
