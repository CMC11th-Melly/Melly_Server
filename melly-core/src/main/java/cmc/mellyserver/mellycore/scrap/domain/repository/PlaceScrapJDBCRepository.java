package cmc.mellyserver.mellycore.scrap.domain.repository;

import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.scrap.domain.PlaceScrap;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlaceScrapJDBCRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveAll(List<Place> places) {

        jdbcTemplate.batchUpdate("insert into tb_place(place_name,is_deleted) " +
                        "values(?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, places.get(i).getPlaceName());
                        ps.setString(2, places.get(i).getIsDeleted().toString());
                    }

                    @Override
                    public int getBatchSize() {
                        return places.size();
                    }
                });
    }

    public void savePlaceScrap(List<PlaceScrap> placeScraps) {

        jdbcTemplate.batchUpdate("insert into tb_place_scrap(user_seq, scrap_type) " +
                        "values(?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, placeScraps.get(i).getUser().getUserSeq());
                        ps.setString(2, placeScraps.get(i).getScrapType().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return placeScraps.size();
                    }
                });
    }
}