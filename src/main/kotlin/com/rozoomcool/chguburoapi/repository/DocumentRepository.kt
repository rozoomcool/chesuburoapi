package com.rozoomcool.chguburoapi.repository;

import com.rozoomcool.chguburoapi.entity.Document
import org.springframework.data.repository.CrudRepository

interface DocumentRepository : CrudRepository<Document, Long> {
}