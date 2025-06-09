package com.backend.demo.service;

import com.backend.demo.DTO.AppointmentDTO;
import com.backend.demo.DTO.AppointmentDetailsDTO;
import com.backend.demo.DTO.AppointmentRequestDTO;
import com.backend.demo.DTO.AppointmentResponseDTO;
import com.backend.demo.entity.*;
import com.backend.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private MechanicRepository mechanicRepository;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private EmailService emailService;


    public List<AppointmentDetailsDTO> getAppointmentsByUserId(String userId) {
        return appointmentRepository.findAppointmentsByUserId(userId);
    }

    public List<AppointmentDTO> getUpcomingAppointmentsByGarageId(String garageId) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        List<Appointment> appointments = appointmentRepository
                .findUpcomingAppointmentsByGarageId(garageId, currentDate, currentTime);

        List<AppointmentDTO> dtoList = new ArrayList<>();
        for (Appointment appointment : appointments) {
            AppointmentDTO dto = new AppointmentDTO();
            dto.setAppointmentId(appointment.getAppointmentId());

            // Vehicle
            Vehicle vehicle = appointment.getVehicle();
            dto.setVehicleMake(vehicle.getMake());
            dto.setVehicleModel(vehicle.getModel());
            dto.setVehicleRegistrationNumber(vehicle.getRegistrationNumber());

            // Mechanic
            Mechanic mechanic = appointment.getMechanic();
            dto.setMechanicId(mechanic.getMechanicId());
            dto.setMechanicName(mechanic.getMechanicName());

            // User
            User user = appointment.getUser();
            dto.setUserId(user.getUserId());
            dto.setUserName(user.getName());
            dto.setUserPhoneNumber(user.getPhone());

            // Garage
            Garage garage = appointment.getGarage();
            dto.setGarageId(garage.getGarageId());
            dto.setGarageName(garage.getName());

            // Appointment
            dto.setServiceName(appointment.getServiceType());
            dto.setDate(appointment.getAppointmentDate().toString());
            dto.setTime(appointment.getAppointmentTime().toString());

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Transactional // Ensures the entire operation is a single transaction
    public void cancelAppointment(Long appointmentId) {
        // 1. Find the appointment
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

        if (optionalAppointment.isEmpty()) {
            throw new NoSuchElementException("Appointment with ID " + appointmentId + " not found.");
        }

        Appointment appointment = optionalAppointment.get();

        // 2. Optional: Add business logic to check if cancellation is allowed
        // For example, you might only allow cancellation if status is SCHEDULED or PENDING
        // if (appointment.getStatus() == ServiceStatus.COMPLETED || appointment.getStatus() == ServiceStatus.CANCELLED) {
        //     throw new IllegalStateException("Appointment cannot be cancelled as it is already " + appointment.getStatus());
        // }

        // 3. Store details for email before deletion (as relationships might be lazy-loaded)
        String userEmail = appointment.getUser() != null ? appointment.getUser().getEmail() : "N/A";
        String userName = appointment.getUser() != null ? appointment.getUser().getName() : "N/A";
        String serviceType = appointment.getServiceType();
        String appointmentDate = appointment.getAppointmentDate() != null ? appointment.getAppointmentDate().toString() : "N/A";
        String appointmentTime = appointment.getAppointmentTime() != null ? appointment.getAppointmentTime().toString() : "N/A";
        String garageName = appointment.getGarage() != null ? appointment.getGarage().getName() : "N/A";
        String vehicleDetails = appointment.getVehicle() != null ?
                appointment.getVehicle().getYear() + " " +
                        appointment.getVehicle().getMake() + " " +
                        appointment.getVehicle().getModel() : "N/A";

        // 4. Delete the appointment
        appointmentRepository.delete(appointment);
        // Or simply: appointmentRepository.deleteById(appointmentId);

        // 5. Send cancellation email
        String subject = "Appointment Cancellation Confirmation - " + garageName;
        String body = String.format(
                "Dear %s,\n\n" +
                        "This email confirms that your appointment with %s has been successfully cancelled.\n\n" +
                        "Appointment Details:\n" +
                        "  - Appointment ID: %d\n" +
                        "  - Service Type: %s\n" +
                        "  - Date: %s\n" +
                        "  - Time: %s\n" +
                        "  - Vehicle: %s\n\n" +
                        "If you have any questions, please contact us.\n\n" +
                        "Thank you,\n" +
                        "%s Team",
                userName, garageName, appointment.getAppointmentId(), serviceType,
                appointmentDate, appointmentTime, vehicleDetails, garageName
        );

        emailService.sendEmail(userEmail, subject, body);
    }


   public String bookAppointment(AppointmentRequestDTO request) {
        LocalDate date = LocalDate.parse(request.getAppointmentDate());
        LocalTime time = LocalTime.parse(request.getAppointmentTime());

        final LocalTime openingTime = LocalTime.of(9, 0);
        final LocalTime closingTime = LocalTime.of(20,0);

        // if requested time is prior then set to openningtime
       if(time.isBefore(openingTime)) {
           time = openingTime;
       }

       List<Mechanic> garageMechanics = mechanicRepository.findByGarageGarageId(request.getGarageId());

       LocalTime checkTime = time;

       while(!checkTime.isAfter(closingTime)) {
           // STEP 1 : CHECK ALL MECHANICS FOR AVAILABILITY FOR GIVEN TIME SLOT

           for(Mechanic mechanic : garageMechanics) {
               boolean isBusy = appointmentRepository.existsByMechanicAndAppointmentDateAndAppointmentTime(
                       mechanic, date, checkTime
               );

               // IF FOUND AVAILABLE ASSIGN IT
               if(!isBusy) {
                   return assignAppointment(
                           request, mechanic, date, time
                   );
               }
           }

           // IF NO MECHANIC FOUND AND USER CAN'T WAIT
           if("no".equalsIgnoreCase(request.getCanWait())) {
               return "No mechanic available for given time slot";
           }

           checkTime = checkTime.plusHours(1);
       }
        return "All mechanics are busy for the requested Date";
    }

    // HELPER : ASSIGN APPOINTMENT AND SAVE SCHEDULE
    private String assignAppointment(
            AppointmentRequestDTO request,
            Mechanic mechanic,
            LocalDate date,
            LocalTime time
    ) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(date);
        appointment.setAppointmentTime(time);

        appointment.setStatus(ServiceStatus.IN_PROGRESS);
        appointment.setServiceId(request.getServiceId());

        Garage garage = garageRepository.findByGarageId(request.getGarageId())
                .orElseThrow(() -> new RuntimeException("No Garage Found"));
        appointment.setGarage(garage);

        Vehicle vehicle = vehicleRepository.findByRegistrationNumber(request.getRegistrationNumber())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        appointment.setVehicle(vehicle);

        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() ->  new RuntimeException("No User Found"));
        appointment.setUser(user);

        appointment.setMechanic(mechanic);
        appointment.setServiceType(serviceRepository.findServiceNameByServiceId(request.getServiceId()));
        appointmentRepository.save(appointment);

        return "Appointment confirmed with mechanic " + mechanic.getMechanicName() + " at time " + time.toString();
    }

    public List<AppointmentResponseDTO> getAppointmentsByGarageId(String garageId) {
        List<Appointment> appointments = appointmentRepository.findAllByGarageIdWithDetails(garageId);

        return appointments.stream().map(a -> new AppointmentResponseDTO(
                a.getAppointmentId(),
                a.getUser().getName(),
                a.getUser().getEmail(),
                a.getUser().getPhone(),
                a.getVehicle().getMake(),
                a.getVehicle().getModel(),
                a.getVehicle().getYear(),
                a.getVehicle().getRegistrationNumber(),
                a.getAppointmentDate(),
                a.getAppointmentTime(),
                a.getServiceType(),
                a.getStatus().toString()
        )).collect(Collectors.toList());
    }

}
