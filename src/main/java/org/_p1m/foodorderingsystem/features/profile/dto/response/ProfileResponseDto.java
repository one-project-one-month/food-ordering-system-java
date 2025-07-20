package org._p1m.foodorderingsystem.features.profile.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfileResponseDto {
    private String name;
    private String nrc;
    private String phone;
    private LocalDate dob;
    private String gender;
    private String profilePic;
    private String address;
}
