package com.example.aston_courseproject_rickmorty.utils.mapper

interface Mapper<SRC, DTO> {
    fun transform(data: SRC): DTO
}