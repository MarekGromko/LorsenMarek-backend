package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.model.User;
import edu.lorsenmarek.backend.common.PageOptions;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class UserRepository {
    private final NamedParameterJdbcTemplate jdbc;
    private final UserMapper userMapper;
    UserRepository(final NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.userMapper = new UserMapper();
    }
    public Optional<User> findById(int id) {
        try {
            var params = new MapSqlParameterSource().addValue("id", id);
            return jdbc.query("SELECT * FROM user WHERE id = :id", params,  userMapper).stream().findFirst();
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
    public List<User> findAll(PageOptions pageOpts) {
        var sql = "";
        var params = new MapSqlParameterSource();
        if(pageOpts == null) {
            sql = "SELECT * FROM user ORDER BY id";
        } else {
            sql = "SELECT * FROM user ORDER BY id LIMIT :limit OFFSET :offset";
            params.addValue("limit", pageOpts.getPageSize() + 1);
            params.addValue("offset", pageOpts.getPageSize() * pageOpts.getPageIndex());
        }
        try {
            return jdbc.query(sql, params, userMapper);
        }catch (DataAccessException e){
            return new ArrayList<>();
        }
    }
    public boolean existsById(int id){
        try{
            var params = new MapSqlParameterSource().addValue("id", id);
            return jdbc.queryForList("SELECT id FROM user WHERE id = ? LIMIT 1", params).size() > 0;
        }catch (DataAccessException e) {
            return false;
        }
    }
    public int update(User user) {
        if(user.getId() == null)
            return 0;
        var params = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("first_name", user.getFirstName())
                .addValue("last_name", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("pwdDigest", user.getPwdDigest())
                .addValue("title", user.getTitle());
        try {
            return jdbc.update("""
                    UPDATE user SET
                        email = :email,
                        pwdDigest = :pwdDigest,
                        title = :title,
                        first_name = :first_name,
                        last_name = :last_name
                    WHERE id = :id
                    """,
                    params
            );
        } catch(Exception e){
            return 0;
        }
    }
    public int insert(User user) {
        if(user.getId() == null)
            return 0;
        var params = new MapSqlParameterSource()
                .addValue("first_name", user.getFirstName())
                .addValue("last_name", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("title", user.getTitle())
                .addValue("pwd_digest", user.getPwdDigest());
        try {
            return jdbc.update("""
                        INSERT INTO user
                            (email,  title,  first_name,  last_name, pwd_digest) VALUES
                            (:email, :title, :first_name, :last_name, :pwd_digest)
                        """,
                    params
            );
        } catch(Exception e) {
            return 0;
        }
    }
    public int deleteById(int id){
        try {
            var params = new MapSqlParameterSource().addValue("id", id);
            return jdbc.update("DELETE FROM person WHERE id = :id", params);
        }catch (DataAccessException e){
            return 0;
        }
    }
    public List<User> searchByName(String hint, PageOptions pageOpts) {
        var sql = "SELECT * FROM user WHERE INSTR(CONCAT(first_name, ' ', last_name), :hint) > 0 ORDER BY id";
        var params = new MapSqlParameterSource().addValue("hint", hint);
        if(pageOpts != null) {
            sql += " LIMIT :limit OFFSET :offset";
            params.addValue("limit", 1 + pageOpts.getPageSize());
            params.addValue("offset", pageOpts.getPageSize() * (pageOpts.getPageIndex()-1));
        }
        try {
            return jdbc.query(sql, params, userMapper);
        }catch (DataAccessException e){
            return new ArrayList<>();
        }

    }
    public static class UserMapper implements RowMapper<User> {
        @Override
        @NonNull
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .id(rs.getLong("id"))
                    .email(rs.getString("email"))
                    .title(rs.getString("title"))
                    .firstName(rs.getString("first_name"))
                    .lastName(rs.getString("last_name"))
                    .pwdDigest(rs.getString("pwd_digest"))
                    .build();
        }
    }
}
