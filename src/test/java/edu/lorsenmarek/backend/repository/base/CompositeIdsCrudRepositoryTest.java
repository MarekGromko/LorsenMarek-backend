package edu.lorsenmarek.backend.repository.base;

import edu.lorsenmarek.backend.annotation.CompositeId;
import lombok.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CompositeIdsCrudRepositoryTest {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class SomeComposite{
        @CompositeId
        private Long thisId;
        @CompositeId
        private Long thatId;
        private String someField;
    }
    record Ids(Long thisId, Long thatId){};
    @Repository
    static class SomeCompositeIdsRepository extends CompositeIdsCrudRepository<SomeComposite, Ids> {
        @Autowired
        public SomeCompositeIdsRepository(
                final JdbcTemplate jdbc,
                final JdbcMappingContext jdbcMap
        ){
            super(jdbc, jdbcMap, new RowMapper<SomeComposite>() {
                @Override
                @NonNull
                public SomeComposite mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new SomeComposite();
                }
            });
        }
        @Override
        protected Object extractId(String fieldName, Ids ids) {
            return switch(fieldName) {
                case "thisId" -> ids.thisId();
                case "thatId" -> ids.thatId();
                default -> null;
            };
        }
    }
    JdbcTemplate mockJdbc;
    SomeCompositeIdsRepository compositeRepo;
    @Autowired
    JdbcMappingContext jdbcMappingContext;
    @BeforeEach
    void createCompositeIdsRepository(){
        mockJdbc = mock(JdbcTemplate.class);
        compositeRepo = new SomeCompositeIdsRepository(mockJdbc, jdbcMappingContext);
    }
    @Nested
    class FindByIds {
        @Test
        void whenIdsAreNull_shouldReturnEmpty() {
            // act
            var result = compositeRepo.findByIds(new Ids(null, null));
            // assert
            assertTrue(result.isEmpty());
        }
        @Test
        void whenThatIdIsNull_shouldOnlyCheckWhereOnColumnThisId() {
            // arrange
            when(mockJdbc.query(
                    anyString(),
                    ArgumentMatchers.<RowMapper<SomeComposite>>any(),
                    anyList())
            ).thenReturn(List.of());

            // act
            compositeRepo.findByIds(new Ids(1L, null));

            // capture
            var sqlCaptor = ArgumentCaptor.forClass(String.class);
            var paramsCaptor = ArgumentCaptor.forClass(List.class);
            verify(mockJdbc).query(
                    sqlCaptor.capture(),
                    ArgumentMatchers.<RowMapper<SomeComposite>>any(),
                    paramsCaptor.capture()
            );

            // assert
            System.out.println(sqlCaptor.getValue());
            assertTrue(sqlCaptor.getValue().toLowerCase().contains("this_id"));
            assertFalse(sqlCaptor.getValue().toLowerCase().contains("that_id"));
            assertEquals(paramsCaptor.getValue().size(), 1);
            assertEquals(paramsCaptor.getValue().get(0), 1L);
        }
        @Test
        void whenNoneIsNull_shouldCheckBothColumn() {
            // arrange
            when(mockJdbc.query(
                    anyString(),
                    ArgumentMatchers.<RowMapper<SomeComposite>>any(),
                    anyList())
            ).thenReturn(List.of());

            // act
            compositeRepo.findByIds(new Ids(1L, 2L));

            // capture
            var sqlCaptor = ArgumentCaptor.forClass(String.class);
            var paramsCaptor = ArgumentCaptor.forClass(List.class);
            verify(mockJdbc).query(
                    sqlCaptor.capture(),
                    ArgumentMatchers.<RowMapper<SomeComposite>>any(),
                    paramsCaptor.capture()
            );

            // assert
            System.out.println(sqlCaptor.getValue());
            assertTrue(sqlCaptor.getValue().toLowerCase().contains("this_id"));
            assertTrue(sqlCaptor.getValue().toLowerCase().contains("that_id"));
            assertEquals(paramsCaptor.getValue().size(), 2);
        }
    }
    @Test
    void whenInsert_allFieldsShouldBeInserted() {
        // arrange
        when(mockJdbc.update(
                anyString(),
                anyList())
        ).thenReturn(1);

        // act
        compositeRepo.insert(new SomeComposite(1L, 2L, "123"));

        // capture
        var sqlCaptor = ArgumentCaptor.forClass(String.class);
        var paramsCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockJdbc).update(
                sqlCaptor.capture(),
                paramsCaptor.capture()
        );

        // assert
        System.out.println(sqlCaptor.getValue());
        assertTrue(sqlCaptor.getValue().toLowerCase().contains("this_id"));
        assertTrue(sqlCaptor.getValue().toLowerCase().contains("that_id"));
        assertTrue(sqlCaptor.getValue().toLowerCase().contains("some_field"));
        assertEquals(paramsCaptor.getValue().size(), 3);
    }
    @Test
    void whenUpdate_allFieldsShouldBeUpdated() {
        // arrange
        when(mockJdbc.update(
                anyString(),
                anyList())
        ).thenReturn(1);

        // act
        compositeRepo.update(new SomeComposite(1L, 2L, "123"));

        // capture
        var sqlCaptor = ArgumentCaptor.forClass(String.class);
        var paramsCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockJdbc).update(
                sqlCaptor.capture(),
                paramsCaptor.capture()
        );

        // assert
        System.out.println(sqlCaptor.getValue());
        assertTrue(sqlCaptor.getValue().toLowerCase().contains("this_id"));
        assertTrue(sqlCaptor.getValue().toLowerCase().contains("that_id"));
        assertTrue(sqlCaptor.getValue().toLowerCase().contains("some_field"));
        assertEquals(paramsCaptor.getValue().size(), 3);
    }
    @Test
    void whenDeleteByIds_allCompositeIdsShouldBeThere() {
        // arrange
        when(mockJdbc.update(
                anyString(),
                anyList())
        ).thenReturn(1);

        // act
        compositeRepo.deleteByIds(new Ids(1L, 2L));

        // capture
        var sqlCaptor = ArgumentCaptor.forClass(String.class);
        var paramsCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockJdbc).update(
                sqlCaptor.capture(),
                paramsCaptor.capture()
        );

        // assert
        System.out.println(sqlCaptor.getValue());
        assertTrue(sqlCaptor.getValue().toLowerCase().contains("this_id"));
        assertTrue(sqlCaptor.getValue().toLowerCase().contains("that_id"));
        assertFalse(sqlCaptor.getValue().toLowerCase().contains("some_field"));
        assertEquals(paramsCaptor.getValue().size(), 2);
    }
}