package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.model.entity.Cart;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Cart,Integer> {
    @Query(value = "SELECT weeks.weekDate AS startDate,\n" +
            "       DATE_ADD(weeks.weekDate, INTERVAL 6 DAY) AS endDate,\n" +
            "       IFNULL(SUM(cart.tax), 0) AS totalTax,\n" +
            "       IFNULL(SUM(cart.shipping), 0) AS totalShip,\n" +
            "       IFNULL(SUM(cart.discount), 0) AS totalDiscount,\n" +
            "       IFNULL(SUM(cart.total), 0) AS total,\n" +
            "       IFNULL(city, :city) city\n" +
            "FROM (\n" +
            "         SELECT ADDDATE(:start, INTERVAL n WEEK) AS weekDate\n" +
            "         FROM (\n" +
            "                  SELECT @row \\:= @row + 1 AS n\n" +
            "                  FROM (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3) r1,\n" +
            "                       (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3) r2,\n" +
            "                       (SELECT @row \\:= -1) x\n" +
            "              ) numbers\n" +
            "     ) weeks\n" +
            "         LEFT JOIN cart ON cart.creatDate BETWEEN weeks.weekDate AND DATE_ADD(weeks.weekDate, INTERVAL 6 DAY) and cart.city=:city and cart.status=:status\n" +
            "WHERE weeks.weekDate BETWEEN :start AND :end\n" +
            "GROUP BY weeks.weekDate",nativeQuery = true)
    List<Object[]> find_by_week_address(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("city") String city, @Param("status") Integer status);


    @Query(value = "SELECT DATE_FORMAT(weeks.weekDate, '%Y-%m-01') AS startDate,\n" +
            "       LAST_DAY(weeks.weekDate) AS endDate,\n" +
            "       IFNULL(SUM(cart.tax), 0) AS totalTax,\n" +
            "       IFNULL(SUM(cart.shipping), 0) AS totalShip,\n" +
            "       IFNULL(SUM(cart.discount), 0) AS totalDiscount,\n" +
            "       IFNULL(SUM(cart.total), 0) AS total,\n" +
            "       IFNULL(city, ?3) city\n" +
            "FROM (\n" +
            "         SELECT ADDDATE(?1, INTERVAL n MONTH) AS weekDate\n" +
            "         FROM (\n" +
            "                  SELECT @row \\:= @row + 1 AS n\n" +
            "                  FROM (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3) r1,\n" +
            "                       (SELECT @row \\:= -1) x\n" +
            "              ) numbers\n" +
            "     ) weeks\n" +
            "         LEFT JOIN cart ON cart.creatDate BETWEEN weeks.weekDate AND LAST_DAY(weeks.weekDate) and cart.city=?3 and cart.status=?4\n" +
            "WHERE weeks.weekDate BETWEEN ?1 AND ?2\n" +
            "GROUP BY weeks.weekDate, city;",nativeQuery = true)
    List<Object[]> find_by_month_address(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("city") String city, @Param("status") Integer status);

}
