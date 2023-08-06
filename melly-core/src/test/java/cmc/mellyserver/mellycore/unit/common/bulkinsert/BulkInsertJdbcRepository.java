package cmc.mellyserver.mellycore.unit.common.bulkinsert;

import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.scrap.domain.PlaceScrap;
import cmc.mellyserver.mellycore.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BulkInsertJdbcRepository {

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
                        ps.setLong(1, placeScraps.get(i).getUser().getId());
                        ps.setString(2, placeScraps.get(i).getScrapType().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return placeScraps.size();
                    }
                });
    }


    //       .randomize(email(), new StringRandomizer()) // 내가 강제로 주입받은 걸로 바꿀 수 있다.
//            .randomize(password(), () -> password)
//            .randomize(ageGroup(), new EnumRandomizer<>(AgeGroup .class))
//            .randomize(gender(), new EnumRandomizer<>(Gender .class))
//            .randomize(nickname(), new StringRandomizer())
//            .randomize(provider(), new EnumRandomizer<>(Provider .class))
//            .randomize(role_type(), new EnumRandomizer<>(RoleType .class))
//            .randomize(user_id(), new StringRandomizer())
//            .randomize(userStatus(), new EnumRandomizer<>(UserStatus .class));
    public void saveUser(List<User> user) {

        jdbcTemplate.batchUpdate("insert into tb_user(email, password, age_group,gender, nickname, provider, role_type, user_id, user_status) " +
                        "values(?,?,?,?,?,?,?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, user.get(i).getEmail());
                        ps.setString(2, user.get(i).getPassword());
                        ps.setString(3, user.get(i).getAgeGroup().name());
                        ps.setString(4, user.get(i).getGender().name());
                        ps.setString(5, user.get(i).getNickname());
                        ps.setString(6, user.get(i).getProvider().name());
                        ps.setString(7, user.get(i).getRoleType().name());
                        ps.setString(8, user.get(i).getSocialId());
                        ps.setString(9, user.get(i).getUserStatus().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return user.size();
                    }
                });
    }
}