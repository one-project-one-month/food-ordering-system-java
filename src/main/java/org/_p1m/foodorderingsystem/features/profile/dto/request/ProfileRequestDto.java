package org._p1m.foodorderingsystem.features.profile.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Profile request body")
public class ProfileRequestDto {

    @NotBlank(message = "Name must not be empty")
    @Schema(description = "User's full name", example = "Ko PU")
    private String name;


    @Schema(
            description = "User's NRC",
            example = "12/Takana(N)123456"
    )
    @Pattern(
            regexp = "^(1[0-4]|[1-9])/[A-Za-z]+\\([A-Z]\\)\\d{6}$",
            message = "Invalid NRC format. Must be in the format 1-14/Word(A-Z)123456"
    )
    private String nrc;

    @Pattern(regexp = "^09\\d{9}$", message = "Phone number must start with 09 and be exactly 11 digits")
    @Schema(description = "User's phone number", example = "09123456789")
    private String phone;

    @Schema(description = "Date of birth", type = "string", format = "date", example = "1990-01-01")
    private LocalDate dob;

    @Schema(description = "Gender of the user", example = "MALE")
    private String gender;

    @Schema(description = "Home address", example = "No.123, Main Road, Yangon")
    private String address;
}
