/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.stubs;


import com.example.exception.AdzshopException;

import java.sql.Connection;
import java.sql.DriverManager;


public class SqliteManager {

    Connection connection = null;

    public Connection getConnection() {
        return connection;
    }

    public Connection open() {
    	return open("dihours.db");
    }

	public Connection open(String dbName) {
		try {
            Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" +
            		dbName);
            return connection;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new AdzshopException("Failed to create the db");
        }
	}

    public void close() {
        try {
            connection.close();
        } catch (Exception e) {
        }
    }
}