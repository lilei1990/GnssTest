package com.example.demo.utils.sqlite;

import java.sql.ResultSet;

public interface ResultSetExtractor<T> {

    public abstract T extractData(ResultSet rs);

}
