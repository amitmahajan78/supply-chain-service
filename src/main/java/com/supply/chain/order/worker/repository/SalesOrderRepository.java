package com.supply.chain.order.worker.repository;

import com.supply.chain.order.worker.domain.SalesOrder;
import com.supply.chain.order.worker.domain.SalesOrderSelect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.UUID;


@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, UUID> {

    @Query(value = "SELECT productId as productId, SUM(quantity) as quantity, store as store FROM SalesOrder " +
            "GROUP BY productId, store, status HAVING status LIKE \'NEW\'")
    public ArrayList<SalesOrderSelect> findSalesOrderForReplenishment();

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE SalesOrder SET status = \'ORDERED\' WHERE productId = :productId " +
            "AND status = :status AND store = :store")
    public int updateSalesOrderStatus(@Param("productId") String productId, @Param("status") String status,
                                      @Param("store") String store);

}
