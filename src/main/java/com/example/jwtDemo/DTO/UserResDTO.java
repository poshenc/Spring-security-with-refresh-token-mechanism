package com.example.jwtDemo.DTO;

import com.example.jwtDemo.data.AppUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResDTO {
    private Long id;
    private String name;
    private String userName;

    public UserResDTO(AppUser appUser) {
        id = appUser.getId();
        name = appUser.getName();
        userName = appUser.getUserName();
    }
}
