package org._p1m.foodorderingsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org._p1m.foodorderingsystem.common.entity.MasterData;

@Entity
@Getter
@Setter
public class UserToken extends MasterData {

    private String token;
    private String username;
}
