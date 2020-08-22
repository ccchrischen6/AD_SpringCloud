package com.chrischen.ad.dao;

import com.chrischen.ad.entity.AdUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Chris Chen
 */
public interface AdUserRepository extends JpaRepository<AdUser, Long> {
    /**
     * @param username
     * @return AdUser
     */
    AdUser findByUsername(String username);
}
