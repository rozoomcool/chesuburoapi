package com.rozoomcool.chguburoapi.repository;

import com.rozoomcool.chguburoapi.entity.PassportData
import org.springframework.data.repository.CrudRepository

interface PassportDataRepository : CrudRepository<PassportData, Long> {
}