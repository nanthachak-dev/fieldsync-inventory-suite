package com.example.fieldsync_inventory_api.repository.sales;

import com.example.fieldsync_inventory_api.entity.sales.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {

}
