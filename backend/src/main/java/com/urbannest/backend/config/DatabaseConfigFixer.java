package com.urbannest.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConfigFixer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            System.out.println("Checking if image_data column needs conversion from OID to BYTEA...");
            // This SQL checks the data type of the column and alters it if it's 'oid'
            // We use lo_get to safely convert existing OID data to bytea if any exists
            String sql = "DO $$ " +
                         "BEGIN " +
                         "    IF EXISTS (SELECT 1 FROM information_schema.columns " +
                         "               WHERE table_name = 'properties' " +
                         "               AND column_name = 'image_data' " +
                         "               AND data_type = 'oid') THEN " +
                         "        ALTER TABLE properties ALTER COLUMN image_data TYPE bytea USING CASE WHEN image_data IS NULL THEN NULL ELSE lo_get(image_data::oid) END; " +
                         "        RAISE NOTICE 'Converted properties.image_data from OID to BYTEA'; " +
                         "    END IF; " +
                         "END $$;";
            jdbcTemplate.execute(sql);
            System.out.println("Database check completed.");
        } catch (Exception e) {
            System.err.println("Could not alter table: " + e.getMessage());
            // If lo_get fails (e.g. invalid OID), we might need to just drop and recreate
            try {
                System.out.println("Attempting to fix by dropping and recreating column...");
                jdbcTemplate.execute("ALTER TABLE properties DROP COLUMN IF EXISTS image_data");
                jdbcTemplate.execute("ALTER TABLE properties ADD COLUMN image_data bytea");
            } catch (Exception e2) {
                System.err.println("Final attempt failed: " + e2.getMessage());
            }
        }
    }
}
