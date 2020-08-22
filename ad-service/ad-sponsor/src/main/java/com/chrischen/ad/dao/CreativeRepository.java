package com.chrischen.ad.dao;

import com.chrischen.ad.entity.Creative;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Chris Chen
 */
public interface CreativeRepository extends JpaRepository<Creative, Long> {
}
