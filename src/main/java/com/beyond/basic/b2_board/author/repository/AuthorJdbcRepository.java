package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dtos.AuthorDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorJdbcRepository {
    //    Datasource는 jdbc의 DB 관리 객체 . 의존성 주입(싱글톤)
    @Autowired
    private final DataSource dataSource;
    public AuthorJdbcRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //    jdbc의 단점
//    1. 쿼리 직접 작성 : 1)raw쿼리에서 오타가 나도 컴파일 에러X. 2)데이터 추가시, 컬럼의 매핑을 수작업으로 진행
//    2. 데이터 조회 후 객채 조립을 수작업으로 해야함.
    public void save(Author author) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "insert into author(name, email, password) values(?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, author.getName());
            ps.setString(2, author.getEmail());
            ps.setString(3, author.getPassword());
//            executeUpdate: 추가/수정, executeQuery : 조회
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Author> findByEmail(String inputEmail) {
        Author author = null;
        try {
            Connection connection = dataSource.getConnection();
            String sql = "select * from author where email = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, inputEmail);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                author = Author.builder().id(id).name(name).email(email).password(password).build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(author);
    }


    public Optional<Author> findById(Long inputId) {
        Author author = null;
        try {
            Connection connection = dataSource.getConnection();
            String sql = "select * from author where id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, inputId);
            ResultSet rs = ps.executeQuery();  // 조회결과가 resultset에 담김
            if (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                author = Author.builder().id(id).name(name).email(email).password(password).build(); // 객체 직접 조립
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(author);
    }

    public List<Author> findAll() {
         List<Author> authorList = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();
            String sql = "select * from author";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                Author author = Author.builder().id(id).name(name).email(email).password(password).build();
                authorList.add(author);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authorList;
    }
    public void delete(Long id) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "delete from author where id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
//            데이터 삭제
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
