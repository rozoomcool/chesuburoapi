package com.rozoomcool.chguburoapi.service

import com.rozoomcool.chguburoapi.entity.Role
import com.rozoomcool.chguburoapi.entity.ServiceData
import com.rozoomcool.chguburoapi.repository.ServiceDataRepository
import org.springframework.stereotype.Service

@Service
class ServiceDataService(
    private val serviceDataRepository: ServiceDataRepository
) {
    fun getAll(forRole: Role = Role.STUDENT): Iterable<ServiceData> {
        return serviceDataRepository.findAll().filter {
            it.forRole == forRole
        }
    }
}