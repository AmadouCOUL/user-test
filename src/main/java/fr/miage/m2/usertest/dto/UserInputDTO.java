package fr.miage.m2.usertest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserInputDTO {

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    private String username;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "La date de naissance est obligatoire")
    private LocalDate birthdate;

    @NotBlank(message =" Le pays est obligatoire")
    private String country;

    private String phone;
    private String gender;
}
