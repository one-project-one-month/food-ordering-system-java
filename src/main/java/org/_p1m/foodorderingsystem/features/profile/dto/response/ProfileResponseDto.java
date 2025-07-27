package org._p1m.foodorderingsystem.features.profile.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Profile response data")
public class ProfileResponseDto {

    @Schema(description = "User's full name", example = "Ko PU")
    private String name;

    @Schema(description = "User's NRC", example = "12/ABC(N)123456")
    private String nrc;

    @Schema(description = "User's phone number", example = "09123456789")
    private String phone;

    @Schema(description = "Date of birth", type = "string", format = "date", example = "1990-01-01")
    private LocalDate dob;

    @Schema(description = "Gender of the user", example = "MALE")
    private String gender;

    @Schema(description = "Profile picture URL or path", example = "https://example.com/images/profile.jpg")
    private String profilePic;

    @Schema(description = "Home address", example = "No.123, Main Road, Yangon")
    private String address;
}
