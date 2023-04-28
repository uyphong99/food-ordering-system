package com.food.ordering.system.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public abstract class BaseEntity<ID> {
    private ID id;
}
