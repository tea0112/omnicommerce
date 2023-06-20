package com.omnicommerce.token.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class Util {
  public static SimpleGrantedAuthority SimpleGrantedAuthorities(String authority) {
    return new SimpleGrantedAuthority(authority);
  }
}
