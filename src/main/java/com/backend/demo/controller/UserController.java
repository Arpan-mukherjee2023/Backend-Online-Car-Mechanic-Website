package com.backend.demo.controller;

import com.backend.demo.DTO.*;
import com.backend.demo.service.GarageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.demo.entity.User;
import com.backend.demo.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/api/users") // base path
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    private final GarageService garageService;


    @PostMapping("/register") // full path: /api/register
    public User postUser(@RequestBody User user) {
        return userService.postUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            System.out.println("Something is Wrong!!");
            return ResponseEntity.status(401).body("Password or Email not Provided");
        }
        User user = userService.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        if (user != null) {
            return ok(user); // You can send a DTO or token instead
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PutMapping("/settings/update-name")
    public ResponseEntity<APIResponseDTO> updateName(@RequestBody UpdateRequestDTO request) {
        try {
            userService.updateUserName(request.getUpdate(), request.getPassword(), request.getUserId());
            return ResponseEntity.ok(new APIResponseDTO("Name updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIResponseDTO("Failed to update name: " + e.getMessage()));
        }
    }

    @PutMapping("/settings/update-address")
    public ResponseEntity<APIResponseDTO> updateAddress(@RequestBody UpdateRequestDTO request) {
        try {
            userService.updateUserAddress(request.getUpdate(), request.getPassword(), request.getUserId());
            return ResponseEntity.ok(new APIResponseDTO("Address updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIResponseDTO("Failed to update name: " + e.getMessage()));
        }
    }

    @PutMapping("/settings/update-phone")
    public ResponseEntity<APIResponseDTO> updatePhoneNumber(@RequestBody UpdateRequestDTO request) {
        try {
            userService.updatePhoneNumber(request.getUpdate(), request.getPassword(), request.getUserId());
            return ResponseEntity.ok(new APIResponseDTO("Phone Number updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIResponseDTO("Failed to update name: " + e.getMessage()));
        }
    }

    @PutMapping("/settings/update-email")
    public ResponseEntity<APIResponseDTO> updateEmail(@RequestBody UpdateRequestDTO request) {
        try {
            userService.updateUserEmail(request.getUpdate(), request.getPassword(), request.getUserId());
            return ResponseEntity.ok(new APIResponseDTO("Email updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIResponseDTO("Failed to update name: " + e.getMessage()));
        }
    }

    @PostMapping("/settings/change-password")
    public ResponseEntity<APIResponseDTO> changePassword(@RequestBody ChangePasswordRequestDTO request) {
        String result = userService.changePassword(request.getUserId(), request.getOldPassword(), request.getNewPassword());
        // if password incorrect return this
        if ("Password incorrect".equals(result)) {
            APIResponseDTO response = new APIResponseDTO(null, result);
            return ResponseEntity.status(400).body(response);
        }

        APIResponseDTO response = new APIResponseDTO(result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/favourite-add")
    public ResponseEntity<String> addFavourite(@RequestBody FavouriteGarageDTO request) {
        System.out.println(request.toString());
        try {
            userService.toggleFavourite(request.getUserId(), request.getGarageId());
            return ResponseEntity.ok("Garage Added Successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/favourite-mechanics")
    public ResponseEntity<List<FavouriteMechanicDTO>> getFavouritemechanics(@PathVariable String userId) {
        List<FavouriteMechanicDTO> mechanics = userService.getFavouriteMechanicDTOs(userId);
        return ResponseEntity.ok(mechanics);
    }

    @DeleteMapping("/{userId}/favourite-mechanics/{mechanicId}")
    public ResponseEntity<?> removeFavouriteMechanic(
            @PathVariable String userId,
            @PathVariable String mechanicId) {
        try {
            userService.removeFavouriteMechanic(userId, mechanicId);
            return ResponseEntity.ok("Mechanic removed from favorites");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/{user_id}/favourite-garages")
    public List<GarageDTOTwo> getFavoriteGarages(@PathVariable("user_id") String userId) {
        return garageService.getFavoriteGarages(userId);
    }


    @DeleteMapping("/{userId}/favourite-garages/{garageId}")
    public ResponseEntity<?> removeFavouriteGarage(
            @PathVariable String userId,
            @PathVariable String garageId) {
        try {
            System.out.println(userId + " " + garageId);

            userService.removeFavouriteGarage(userId, garageId);
            return ResponseEntity.ok("Garage removed from Favourites");
        } catch(RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public Optional<User> getUserByUserId(@PathVariable String userId) {
        return userService.getUserById(userId);
    }

}


