package org.alpermelkeli.dto.response;

import lombok.Data;

@Data
public class RefreshTokenResponseDto {
    private String jwtToken;

    public RefreshTokenResponseDto(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }


}
