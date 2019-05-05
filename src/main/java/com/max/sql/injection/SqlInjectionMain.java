package com.max.sql.injection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SqlInjectionMain {

    /*

    1.

    create table company (id int auto_increment primary key, name varchar(30) not null, descr varchar(100));
    insert into company (id, name, descr) values (1, 'Oracle', 'Oracle corporate');
    insert into company (id, name, descr) values (2, 'Microsoft', 'Microsoft Inc.');
     insert into company (id, name, descr) values (3, 'IBM', 'IBM com.');
     insert into company (id, name, descr) values (4, 'Google', 'Google corporation');
     insert into company (id, name, descr) values (5, 'Facebook', 'Facebook corporation');

    select id, name descr from company where id = ?

    2.

    create table user(id int auto_increment primary key, username varchar(30) not null, password char(64) not null);

    insert into user(id, username, password) values (1, 'max', SHA2('611191', 256));
     */
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String JDBC_DRIVER = com.mysql.jdbc.Driver.class.getName();
    private static final String DATABASE_URL = "jdbc:mysql://localhost/security_test";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "611191";

    private SqlInjectionMain() throws Exception {

        Class.forName(JDBC_DRIVER);

        final String companyId = "1 or 1=1";

        final String sql = "select * from company where id = " + companyId;

        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {

            LOG.info("SQL: {}", sql);

            try (ResultSet res = statement.executeQuery(sql)) {

                int rowsCount = 0;

                while (res.next()) {
                    LOG.info("{}, {}", res.getInt("id"), res.getString("name"));
                    ++rowsCount;
                }

                if (rowsCount > 1) {
                    throw new IllegalStateException("More than 0 or 1 row returned, possible SQL injection.");
                }
            }
        }

    }

    public static void main(String[] args) {
        try {
            new SqlInjectionMain();
        }
        catch (Exception ex) {
            LOG.error("Error occurred", ex);
        }
    }
}
