package com.bkap.fruitshop.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {

    private String token;
    boolean authenticated;
}
