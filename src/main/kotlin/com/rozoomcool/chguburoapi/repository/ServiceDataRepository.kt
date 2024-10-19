package com.rozoomcool.chguburoapi.repository;

import com.rozoomcool.chguburoapi.entity.ServiceData
import org.springframework.data.repository.CrudRepository

interface ServiceDataRepository : CrudRepository<ServiceData, Long> {
}