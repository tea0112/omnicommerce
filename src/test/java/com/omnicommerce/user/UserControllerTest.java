package com.omnicommerce.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.omnicommerce.user.position.PositionRepository;
import com.omnicommerce.user.role.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

// @Import({CustomSecurity.class, CustomAccessDeniedHandler.class})
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private PasswordEncoder passwordEncoder;
  @MockBean private RoleRepository roleRepository;
  @MockBean private UserRepository userRepository;
  @MockBean private PositionRepository positionRepository;
  @MockBean private UserDetailsService userDetailsService;
  @MockBean private UserService userService;

  @Autowired private WebApplicationContext context;

  @Test
  @WithMockUser(value = "myusername")
  public void shouldReturn() throws Exception {
    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/api/users/test-jwt"))
        .andDo(print())
        .andExpect(content().string("you are eligible to pass this wall"));
  }
}
