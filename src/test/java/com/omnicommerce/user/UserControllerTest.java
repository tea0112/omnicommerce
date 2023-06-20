package com.omnicommerce.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.omnicommerce.reponse.exception.CustomAccessDeniedHandler;
import com.omnicommerce.security.CustomSecurity;
import com.omnicommerce.user.position.PositionRepository;
import com.omnicommerce.user.role.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

@Import({CustomSecurity.class, CustomAccessDeniedHandler.class})
@WebMvcTest(controllers = UserController.class)
class UserControllerTests {
  @Autowired private MockMvc mockMvc;

  @MockBean private PasswordEncoder passwordEncoder;
  @MockBean private RoleRepository roleRepository;
  @MockBean private UserRepository userRepository;
  @MockBean private PositionRepository positionRepository;
  @MockBean private UserDetailsService userDetailsService;
  @MockBean private UserService userService;

  @Autowired private WebApplicationContext context;

  @Test
  public void roleHierarchy() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/users/test-jwt").content(MediaType.TEXT_PLAIN_VALUE))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.content().string("pass"));
  }
}
