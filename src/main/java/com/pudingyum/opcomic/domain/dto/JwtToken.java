package com.pudingyum.opcomic.domain.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {
    private String token;
}
