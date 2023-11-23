package com.example.test2.mapper;

public interface DtoResponseMapper<D, C> {
    D toDto(C object);
}