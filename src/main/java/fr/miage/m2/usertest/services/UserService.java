package fr.miage.m2.usertest.services;

import fr.miage.m2.usertest.dto.UserInputDTO;
import fr.miage.m2.usertest.model.User;
import fr.miage.m2.usertest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class UserService {


    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserInputDTO userInputDTO) {


        int age = Period.between(userInputDTO.getBirthdate(), LocalDate.now()).getYears();

        if (age < 18) {
            throw new IllegalArgumentException("L'utilisateur doit être majeur (18 ans ou plus)");
        }

        if (!"France".equalsIgnoreCase(userInputDTO.getCountry())) {
            throw new IllegalArgumentException("Seuls les résidents français peuvent s'inscrire");
        }

        List<String> genders = List.of("Homme", "Femme", "Autre");
        if (userInputDTO.getGender() != null && !genders.contains(userInputDTO.getGender())) {
            throw new IllegalArgumentException("Le genre doit être 'Homme', 'Femme' ou 'Autre'");
        }

        User user = new User();
        user.setUsername(userInputDTO.getUsername());
        user.setBirthdate(userInputDTO.getBirthdate());
        user.setCountry(userInputDTO.getCountry());
        user.setPhone(userInputDTO.getPhone());
        user.setGender(userInputDTO.getGender());
        return userRepository.save(user);
    }


    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("L'utilisateur n°" + id + " n'existe pas"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
