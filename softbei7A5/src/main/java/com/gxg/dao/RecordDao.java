package com.gxg.dao;

import com.gxg.entities.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by 郭欣光 on 2018/5/28.
 */

@Repository
public class RecordDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int getRecordCount() {
        String sql = "select count(*) from record";
        int rowCount = this.jdbcTemplate.queryForObject(sql, Integer.class);
        return rowCount;
    }

    public int getAllImgCount() {
        String sql = "select sum(img_all) from record";
        int rowCount = this.jdbcTemplate.queryForObject(sql, Integer.class);
        return rowCount;
    }

    public long getAllProcessTime() {
        String sql = "select sum(process_time) from record";
        long rowCount = this.jdbcTemplate.queryForObject(sql, Long.class);
        return rowCount;
    }

    public List<Record> getRecordListByOrderByTimeDescLimitStartAndEnd(int limitStart, int limitEnd) {
        String sql = "select * from record order by time desc limit ?,?";
        List<Record> recordList = jdbcTemplate.query(sql, new RowMapper<Record>() {
            @Override
            public Record mapRow(ResultSet resultSet, int i) throws SQLException {
                Record record = new Record();
                record.setId(resultSet.getString("id"));
                record.setImgSrc(resultSet.getString("img_src"));
                record.setSaveSrc(resultSet.getString("save_src"));
                record.setImgAll(resultSet.getInt("img_all"));
                record.setImgAlready(resultSet.getInt("img_already"));
                record.setImgError(resultSet.getInt("img_error"));
                record.setProcessNumber(resultSet.getInt("process_number"));
                record.setProcessTime(resultSet.getInt("process_time"));
                record.setTime(resultSet.getTimestamp("time"));
                return record;
            }
        }, limitStart, limitEnd);
        return recordList;
    }

    public void insertRecord(Record record) {
        String sql = "insert into record values(?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, record.getId(), record.getImgSrc(), record.getSaveSrc(), record.getImgAll(), record.getImgAlready(), record.getImgError(), record.getProcessNumber(), record.getProcessTime(), record.getTime());
    }
}
