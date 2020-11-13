package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        ValidationUtil.validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) != 0) {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        } else {
            return null;
        }
        saveRoles(user);
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users JOIN user_roles ON users.id = user_roles.user_id WHERE id=?", JdbcUserRepository::userMapper, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users JOIN user_roles ON users.id = user_roles.user_id WHERE email=?", JdbcUserRepository::userMapper, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users JOIN user_roles ON users.id = user_roles.user_id ORDER BY name, email", JdbcUserRepository::userMapper);
    }

    private static List<User> userMapper(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        User currentUser = null;
        while (rs.next()) {
            int id = rs.getInt("id");
            if (currentUser == null || currentUser.getId() != id) {
                currentUser = ROW_MAPPER.mapRow(rs, rs.getRow());
            }
            if (currentUser.getRoles() == null) {
                Set<Role> roles = EnumSet.noneOf(Role.class);
                roles.add(Role.valueOf(rs.getObject("role", String.class)));
                currentUser.setRoles(roles);
            } else {
                currentUser.getRoles().add(Role.valueOf(rs.getObject("role", String.class)));
            }
            if (!users.contains(currentUser)) {
                users.add(currentUser);
            }
        }
        return users;
    }

    private void saveRoles(User user) {
        ValidationUtil.validate(user);
        List<Pair<Integer, String>> rolesPairs = new ArrayList<>();
        user.getRoles().forEach(r -> rolesPairs.add(Pair.of(user.getId(), r.toString())));
        jdbcTemplate.batchUpdate("INSERT INTO user_roles values (?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, rolesPairs.get(i).getFirst());
                ps.setString(2, rolesPairs.get(i).getSecond());
            }

            @Override
            public int getBatchSize() {
                return rolesPairs.size();
            }
        });
    }
}
