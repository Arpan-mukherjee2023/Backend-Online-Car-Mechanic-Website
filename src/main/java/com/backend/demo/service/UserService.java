package com.backend.demo.service;


import com.backend.demo.DTO.FavouriteMechanicDTO;
import com.backend.demo.DTO.GarageDTO;
import com.backend.demo.entity.Garage;
import com.backend.demo.entity.Mechanic;
import com.backend.demo.repository.GarageRepository;
import com.backend.demo.repository.MechanicRepository;
import org.springframework.stereotype.Service;

import com.backend.demo.entity.User;
import com.backend.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GarageRepository garageRepository;
    private final MechanicRepository mechanicRepository;

    private final Random random = new Random();

    public User postUser(User user) {

        if(user.getUserId() == null || user.getUserId().isEmpty()) {
            String userId;
            do {
                userId = generateUserId(user.getName());
            } while(userRepository.existsByUserId(userId));
            user.setUserId(userId);

        }

        return userRepository.save(user);
    }

    public User findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }


    private String generateUserId(String name) {
        int number = 100 + random.nextInt(900); // Generates a 3-digit number
        return "user." + name + "." + number;
    }

    public void updateUserName(String newName, String password, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID" + userId));
        // set the name and save it to the DB
        user.setName(newName);
        userRepository.save(user);
    }

    public void updateUserAddress(String newAddress, String password, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID" + userId));
        // set the address and save it to the DB
        user.setAddress(newAddress);
        userRepository.save(user);
    }

    public void updatePhoneNumber(String newNumber, String password, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID" + userId));
        // set the phone number and save it to the DB
        user.setPhone(newNumber);
        userRepository.save(user);
    }

    public Optional<User> getUserById(String userId) {
        return userRepository.findByUserId(userId);
    }

    public void updateUserEmail(String newEmail, String password, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID" + userId));
        // set the email and save it to the DB
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    public String changePassword(String userId, String oldPassword, String newPassword) {
        Optional<User> optionalUser = userRepository.findByUserIdAndPassword(userId, oldPassword);
        if (optionalUser.isEmpty()) {
            return "Password incorrect";
        }
        User user = optionalUser.get();
        user.setPassword(newPassword);  // Remember to hash password in real app!
        userRepository.save(user);
        return "Password changed successfully";
    }

    public void toggleFavourite(String userId, String garageId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Garage garage = garageRepository.findByGarageId(garageId)
                .orElseThrow(() -> new RuntimeException("Garage Not found"));

        Set<Garage> favourites = user.getFavouriteGarages();

        if (favourites.contains(garage)) {
            System.out.println("Yes it has garage");
            favourites.remove(garage);
            userRepository.save(user);
        } else {
            System.out.println("No it do not hs grage");
            favourites.add(garage);
            garage.getLikedByUsers().add(user);
            userRepository.save(user);
        }

    }

    // returns list of all favourite mechanics of an user
    public List<FavouriteMechanicDTO> getFavouriteMechanicDTOs(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        return user.getFavouriteMechanics().stream()
                .map(mechanic -> new FavouriteMechanicDTO(
                        mechanic.getMechanicId(),
                        mechanic.getMechanicName(),
                        mechanic.getGarage() != null ? mechanic.getGarage().getName() : null,
                        mechanic.getMechanicPhoneNumber(),
                        mechanic.getSpecialization()
                ))
                .collect(Collectors.toList());
    }

    // removes favourite mechanic of an user from DB
    public void removeFavouriteMechanic(String userId, String mechanicId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Mechanic mechanic = mechanicRepository.findById(mechanicId)
                .orElseThrow(() -> new RuntimeException("Mechanic not found"));

        user.getFavouriteMechanics().remove(mechanic);
        userRepository.save(user); // Save changes
    }


    public void removeFavouriteGarage(String userId, String garageId) {
        try {
            System.out.println("Attempting to remove garage: " + garageId + " for user: " + userId);

            // Fetch the user from the repository
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User  Not Found"));

            // Fetch the garage from the repository
            Garage garage = garageRepository.findByGarageId(garageId)
                    .orElseThrow(() -> new RuntimeException("Garage not Found"));

            // Create a copy of the favourite garages to avoid ConcurrentModificationException
            Set<Garage> favouriteGaragesCopy = new HashSet<>(user.getFavouriteGarages());

            System.out.println("User 's favorite garages before removal: " + favouriteGaragesCopy);

            // Check if the garage is in the user's favorites
            if (!favouriteGaragesCopy.contains(garage)) {
                throw new RuntimeException("Garage is not in user's favourites");
            }

            // Remove the garage from the copied collection
            favouriteGaragesCopy.remove(garage);

            // Update the user's favorite garages
            user.setFavouriteGarages(favouriteGaragesCopy);
            userRepository.save(user);

            System.out.println("Garage removed successfully.");
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for debugging
            throw e; // Optionally rethrow the exception if you want to handle it further up the stack
        }
    }



}


