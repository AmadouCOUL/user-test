package fr.miage.m2.usertest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.miage.m2.usertest.model.User;
import fr.miage.m2.usertest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void creerUtilisateurMineur() throws Exception {
        User user = new User();
        user.setUsername("Mamadou");
        user.setBirthdate(LocalDate.of(2010, 5, 10));
        user.setCountry("France");

        String body = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void creerUtilisateurPasFrancais() throws Exception {
        User user = new User();
        user.setUsername("karim");
        user.setBirthdate(LocalDate.of(1990, 5, 10));
        user.setCountry("Allemagne");

        String body = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void creerUtilisateurSansUsername() throws Exception {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("birthdate", "10-05-1990");
        userMap.put("country", "France");

        String body = objectMapper.writeValueAsString(userMap);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Le nom d'utilisateur est obligatoire")));
    }


    @Test
    void recupererUtilisateur() throws Exception {
        User user = new User();
        user.setUsername("Faty");
        user.setBirthdate(LocalDate.of(1990, 5, 10));
        user.setCountry("France");
        User saved = userRepository.save(user);

        mockMvc.perform(get("/users/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Faty")));
    }

    @Test
    void recupererUtilisateurInexistant() throws Exception {
        mockMvc.perform(get("/users/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void recupererTousLesUtilisateurs() throws Exception {
        User u1 = new User();
        u1.setUsername("Karamoko");
        u1.setBirthdate(LocalDate.of(1990, 1, 1));
        u1.setCountry("France");

        User u2 = new User();
        u2.setUsername("Adja");
        u2.setBirthdate(LocalDate.of(1995, 6, 20));
        u2.setCountry("France");

        userRepository.save(u1);
        userRepository.save(u2);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Karamoko")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Adja")));
    }

    @Test
    void creerUtilisateurAvecDateMauvaisFormat() throws Exception {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", "Coulibaly");
        userMap.put("birthdate", "1990-05-10");
        userMap.put("country", "France");

        String body = objectMapper.writeValueAsString(userMap);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("La date doit être au format jj-MM-aaaa")));
    }


}


