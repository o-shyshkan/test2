package com.example.test2.mapper.impl;

import com.example.test2.model.User;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getString(1));
        user.setUsername(rs.getString(2));
        user.setName(rs.getString(3));
        user.setSurname(rs.getString(4));
        return user;
    }
}
