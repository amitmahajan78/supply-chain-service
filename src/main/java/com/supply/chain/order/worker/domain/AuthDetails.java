package com.supply.chain.order.worker.domain;


import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class AuthDetails {
    String access_token;
    String token_type;
    int expires_in;
    String scope;
    String jti;
}
