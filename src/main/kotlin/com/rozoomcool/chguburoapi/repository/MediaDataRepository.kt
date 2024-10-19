package com.rozoomcool.chguburoapi.repository

import com.rozoomcool.chguburoapi.entity.MediaData
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MediaDataRepository : CrudRepository<MediaData, Long> {
}