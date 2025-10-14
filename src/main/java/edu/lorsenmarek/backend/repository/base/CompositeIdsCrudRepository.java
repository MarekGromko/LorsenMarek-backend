package edu.lorsenmarek.backend.repository.base;

import edu.lorsenmarek.backend.annotation.CompositeId;
import org.springframework.core.ResolvableType;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

abstract public class CompositeIdsCrudRepository<T, CI> {
    final protected JdbcTemplate jdbc;
    final protected JdbcMappingContext jdbcMap;
    final protected RowMapper<T> rowMapper;
    final protected Class<?> tclass;
    protected CompositeIdsCrudRepository(
            final JdbcTemplate jdbc,
            final JdbcMappingContext jdbcMap,
            final RowMapper<T> mapper
    ) {
        this.jdbc = jdbc;
        this.jdbcMap = jdbcMap;
        this.rowMapper = mapper;
        tclass = ResolvableType
                .forClass(getClass())
                .getSuperType()
                .getGeneric(0)
                .resolve();
    }
    final protected RelationalPersistentEntity<?> extractEntity() { return jdbcMap.getPersistentEntity(tclass); }
    final protected List<RelationalPersistentProperty> extractIdsProperties(RelationalPersistentEntity<?> entity) {
        var ids = StreamSupport
                .stream(entity.getPersistentProperties(CompositeId.class).spliterator(), false)
                .toList();
        if(ids.size() < 2)
            throw new IllegalArgumentException("Object %s has less than two @CompositeId field".formatted(entity.getName()));
        return ids;
    }
    public List<T> findByIds(CI ids) {
        var entity = extractEntity();
        var idsFields = new ArrayList<String>();
        var idsParams = new ArrayList<>();
        extractIdsProperties(entity).forEach(prop->{
            var param = extractId(prop.getField().getName(), ids);
            if(param == null)
                return;
            idsFields.add("%s = ?".formatted(prop.getColumnName().toString()));
            idsParams.add(param);
        });

        if(idsFields.isEmpty())
            return Collections.emptyList();
        var sql = "SELECT * FROM %s WHERE %s".formatted(
                entity.getTableName().toString(),
                String.join(" AND ", idsFields)
        );
        return jdbc.query(sql, rowMapper, idsParams);
    }
    public Optional<T> findOneByIds(CI ids) {
        return findByIds(ids).stream().findFirst();
    }
    public void insert(T data) {
        var entity = extractEntity();
        var fields = new ArrayList<String>();
        var params = new ArrayList<>();
        entity.doWithAll(prop -> {
            fields.add(prop.getColumnName().toString());
            params.add(prop.getAccessorForOwner(data).getProperty(prop));
        });
        var sql = "INSERT INTO %s (%s) VALUES (%s)".formatted(
                entity.getTableName(),
                String.join(", ", fields),
                String.join(", ", Collections.nCopies(fields.size(), "?"))
        );
        jdbc.update(sql, params);
    }
    public int update(T data) {
        var entity = extractEntity();

        // get ids
        var idsFields = new ArrayList<String>();
        var idsParams = new ArrayList<>();
        extractIdsProperties(entity).forEach(prop->{
            idsFields.add("%s = ?".formatted(prop.getColumnName().toString()));
            idsParams.add(prop.getAccessorForOwner(data).getProperty(prop));
        });
        if(idsParams.stream().anyMatch(Objects::isNull))
            return 0;

        // get values
        var fields = new ArrayList<String>();
        var params = new ArrayList<>();
        entity.doWithAll(prop -> {
            if(prop.isAnnotationPresent(CompositeId.class))
                return;
            fields.add("%s = ?".formatted(prop.getColumnName().toString()));
            params.add(prop.getAccessorForOwner(data).getProperty(prop));
        });
        var sql = "UPDATE %s SET %s WHERE %s".formatted(
                entity.getTableName().toString(),
                String.join(", ", fields),
                String.join(" AND ", idsFields)
        );
        return jdbc.update(sql, Stream.concat(params.stream(), idsParams.stream()).toList());
    }
    public int deleteByIds(CI ids) {
        var entity = extractEntity();
        var idsFields = new ArrayList<String>();
        var idsParams = new ArrayList<>();

        extractIdsProperties(entity).forEach(prop->{
            var param = extractId(prop.getField().getName(), ids);
            if(param == null)
                return;
            idsFields.add("%s = ?".formatted(prop.getColumnName().toString()));
            idsParams.add(param);
        });

        if(idsFields.isEmpty())
            return 0;
        var sql = "DELETE FROM %s WHERE %s".formatted(
                entity.getTableName().toString(),
                String.join(" AND ", idsFields)
        );
        return jdbc.update(sql, idsParams);
    }
    abstract protected Object extractId(String fieldName, CI ids);
}
