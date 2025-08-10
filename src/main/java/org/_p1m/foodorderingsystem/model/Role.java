package org._p1m.foodorderingsystem.model;

import org._p1m.foodorderingsystem.common.entity.MasterData;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Role extends MasterData {
    @Column(unique = true)
    private String name;

    public Role() {}

    public Role(final String name) {
        this.name = name;
    }
}
