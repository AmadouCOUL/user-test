package fr.miage.m2.usertest.service;

import fr.miage.m2.usertest.dto.UserInputDTO;
import fr.miage.m2.usertest.model.User;
import fr.miage.m2.usertest.repository.UserRepository;
import fr.miage.m2.usertest.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void creerUtilisateur() {
        UserInputDTO userInputDTO = new UserInputDTO();
        userInputDTO.setUsername("Amadou");
        userInputDTO.setBirthdate(LocalDate.of(1990, 5, 10));
        userInputDTO.setCountry("France");

        User user = new User();
        user.setId(1L);
        user.setUsername("Amadou");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User resultat = userService.createUser(userInputDTO);

        assertEquals("Amadou", resultat.getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void creerUtilisateurMineur() {
        UserInputDTO userInputDTO = new UserInputDTO();
        userInputDTO.setUsername("Mariam");
        userInputDTO.setBirthdate(LocalDate.now().minusYears(15));
        userInputDTO.setCountry("France");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(userInputDTO);
        });
    }

    @Test
    void creerUtilisateurNonResidentFrancais() {
        UserInputDTO userInputDTO = new UserInputDTO();
        userInputDTO.setUsername("Frederic");
        userInputDTO.setBirthdate(LocalDate.of(1990, 5, 10));
        userInputDTO.setCountry("Espagne");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(userInputDTO);
        });
    }

    @Test
    void recupererUtilisateur() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Karim");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User resultat = userService.getUser(1L);

        assertEquals("Karim", resultat.getUsername());
    }

    @Test
    void recupererUtilisateurInexistant() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUser(99L);
        });
    }

    @Test
    void recupererTousLesUtilisateurs() {
        User u1 = new User();
        u1.setUsername("Lambert");
        User u2 = new User();
        u2.setUsername("Mariam");

        when(userRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

        List<User> resultat = userService.getAllUsers();

        assertEquals(2, resultat.size());
    }
}