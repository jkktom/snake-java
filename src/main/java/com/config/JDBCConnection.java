package com.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/*
 * config 패키지
 * config 패키지는 주로 설정 정보와 같은 소스 코드를 저장하는 패키지이다.
 * */
public class JDBCConnection {
    private static final Logger logger = LoggerFactory.getLogger(JDBCConnection.class);
    private static final HikariDataSource dataSource;
    private static boolean isInitialized = false;

    static {
        try {
            /*
             * properties
             * 키 - 값을 쌍으로 저장하는 방식이다.
             * 주로 설정 정보나 구성 데이터를 관리하는데 유용하게 사용된다.
             * */
            Properties props = new Properties();
            /*
             * properties.load 외부 파일을 읽어오는 역할을 수행
             * JDBCConnection.class.GetClassLoader() : 클래스를 메모리에 로드하는 역할을 수행하며 이를 통해 설정 파일에 접근이 가능하다.
             * getResourceAsStream ("config") : 매개변수로 전달된 파일을 스트림으로 가져오는 역할을 수행한다.
             * */
            var configStream = JDBCConnection.class.getClassLoader().getResourceAsStream("config.properties");
            if (configStream == null) {
                throw new IOException("설정 파일을 찾을 수 없습니다: config.properties");
            }
            props.load(configStream);
            
            if (props.isEmpty()) {
                throw new IOException("설정 파일이 비어 있습니다: config.properties");
            }

            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            if (url == null || username == null || password == null) {
                throw new IOException("데이터베이스 연결 정보가 불완전합니다. URL, 사용자 이름, 비밀번호를 확인해주세요.");
            }

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(5);
            config.setIdleTimeout(30000);
            config.setMaxLifetime(1800000);
            config.setConnectionTimeout(2000);
            config.setConnectionTestQuery("SELECT 1");

            dataSource = new HikariDataSource(config);
            isInitialized = true;
            logger.info("데이터베이스 연결 풀이 성공적으로 초기화되었습니다.");

        } catch (IOException e) {
            logger.error("설정 파일 로드 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("데이터베이스 연결 초기화 실패: " + e.getMessage());
        }
    }

    // 커넥션 풀에서 연결되어 있는 객체를 꺼내오는 메서드
    public static Connection getConnection() throws SQLException {
        if (!isInitialized || dataSource == null) {
            throw new SQLException("데이터베이스 연결 풀이 초기화되지 않았습니다.");
        }
        try {
            Connection conn = dataSource.getConnection();
            if (!conn.isValid(1)) {
                throw new SQLException("데이터베이스 연결이 유효하지 않습니다.");
            }
            return conn;
        } catch (SQLException e) {
            logger.error("데이터베이스 연결 실패: {}", e.getMessage());
            throw new SQLException("데이터베이스 연결을 얻을 수 없습니다: " + e.getMessage());
        }
    }

    // hikaricp 전체 커넥션 풀을 종료하는 메서드
    // application -> 전체 종료 -> connection pool 더 이상 사용 불가.
    public static void close() {
        if (dataSource != null) {
            try {
                dataSource.close();
                isInitialized = false;
                logger.info("데이터베이스 연결 풀이 정상적으로 종료되었습니다.");
            } catch (Exception e) {
                logger.error("데이터베이스 연결 풀 종료 중 오류 발생: {}", e.getMessage());
            }
        }
    }

    public static void printConnectionPoolStatus() {
        if (dataSource != null) {
            try {
                HikariPoolMXBean poolMXBean = dataSource.getHikariPoolMXBean();
                logger.info("\n[데이터베이스 연결 풀 상태]");
                logger.info("전체 연결 수: {}", poolMXBean.getTotalConnections());
                logger.info("활성 연결 수: {}", poolMXBean.getActiveConnections());
                logger.info("유휴 연결 수: {}", poolMXBean.getIdleConnections());
                logger.info("대기 중인 스레드 수: {}", poolMXBean.getThreadsAwaitingConnection());
            } catch (Exception e) {
                logger.error("연결 풀 상태 확인 중 오류 발생: {}", e.getMessage());
            }
        }
    }

    public static boolean isConnectionValid() {
        if (!isInitialized || dataSource == null) {
            return false;
        }
        try (Connection conn = dataSource.getConnection()) {
            return conn.isValid(1);
        } catch (SQLException e) {
            logger.error("연결 상태 확인 중 오류 발생: {}", e.getMessage());
            return false;
        }
    }

    // Test method to verify connection
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            logger.info("Successfully connected to database!");
            printConnectionPoolStatus();
        } catch (SQLException e) {
            logger.error("Failed to connect to database: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
