package com.cognizant.userservice.model;

import com.cognizant.userservice.enums.Theme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Preference {

    private Theme theme;
    private boolean notifications;
}