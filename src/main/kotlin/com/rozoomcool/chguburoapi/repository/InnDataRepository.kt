package com.rozoomcool.chguburoapi.repository;

import com.rozoomcool.chguburoapi.entity.InnData
import org.springframework.data.repository.CrudRepository

interface InnDataRepository : CrudRepository<InnData, Long> {
}