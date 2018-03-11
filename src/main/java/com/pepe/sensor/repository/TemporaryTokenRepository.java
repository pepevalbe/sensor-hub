/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pepe.sensor.repository;

import com.pepe.sensor.persistence.TemporaryToken;
import org.springframework.data.repository.CrudRepository;


public interface TemporaryTokenRepository extends CrudRepository<TemporaryToken, Long> {
    
}
