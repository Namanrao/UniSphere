package com.unisphere.config;

import com.unisphere.entity.Community;
import com.unisphere.entity.Role;
import com.unisphere.entity.University;
import com.unisphere.entity.User;
import com.unisphere.enums.RoleName;
import com.unisphere.repository.CommunityRepository;
import com.unisphere.repository.RoleRepository;
import com.unisphere.repository.UniversityRepository;
import com.unisphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeSampleUniversities();
        initializeAdminUser();
        initializeSampleCommunities(); // ADD THIS
    }

    private void initializeRoles() {
        for (RoleName roleName : RoleName.values()) {
            if (!roleRepository.findByName(roleName).isPresent()) {
                roleRepository.save(new Role(roleName));
                System.out.println("Initialized role: " + roleName);
            }
        }
    }

    private void initializeSampleUniversities() {
        if (universityRepository.count() == 0) {
            // Sample universities for development
            University harvard = new University(
                    "Harvard University",
                    "harvard.edu",
                    "Private Ivy League research university in Cambridge, Massachusetts",
                    "Cambridge, MA"
            );

            University mit = new University(
                    "Massachusetts Institute of Technology",
                    "mit.edu",
                    "Private land-grant research university in Cambridge, Massachusetts",
                    "Cambridge, MA"
            );

            University stanford = new University(
                    "Stanford University",
                    "stanford.edu",
                    "Private research university in Stanford, California",
                    "Stanford, CA"
            );

            University caltech = new University(
                    "California Institute of Technology",
                    "caltech.edu",
                    "Private research university in Pasadena, California",
                    "Pasadena, CA"
            );

            University princeton = new University(
                    "Princeton University",
                    "princeton.edu",
                    "Private Ivy League research university in Princeton, New Jersey",
                    "Princeton, NJ"
            );

            universityRepository.save(harvard);
            universityRepository.save(mit);
            universityRepository.save(stanford);
            universityRepository.save(caltech);
            universityRepository.save(princeton);

            System.out.println("Sample universities initialized: 5 universities added");
        } else {
            System.out.println("Universities already exist in database. Count: " + universityRepository.count());
        }
    }

    private void initializeAdminUser() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@unisphere.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("System");
            admin.setLastName("Administrator");

            Set<Role> adminRoles = new HashSet<>();
            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
            adminRoles.add(adminRole);

            // Also add USER role for basic access
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("User role not found"));
            adminRoles.add(userRole);

            admin.setRoles(adminRoles);
            userRepository.save(admin);
            System.out.println("Admin user created: username='admin', password='admin123'");
        } else {
            System.out.println("Admin user already exists");
        }
    }
    private void initializeSampleCommunities() {
        if (communityRepository.count() == 0) {
            // Get universities
            University harvard = universityRepository.findByName("Harvard University")
                    .orElseThrow(() -> new RuntimeException("Harvard not found"));
            University mit = universityRepository.findByName("Massachusetts Institute of Technology")
                    .orElseThrow(() -> new RuntimeException("MIT not found"));

            // Get admin user
            User admin = userRepository.findByUsername("admin")
                    .orElseThrow(() -> new RuntimeException("Admin user not found"));

            // Create sample communities
            Community csHarvard = new Community(
                    "cs-harvard",
                    "Computer Science at Harvard",
                    "Discussion forum for CS students at Harvard University",
                    harvard,
                    admin
            );

            Community mathMit = new Community(
                    "math-mit",
                    "Mathematics at MIT",
                    "Math enthusiasts and students at MIT",
                    mit,
                    admin
            );

            Community physicsHarvard = new Community(
                    "physics-harvard",
                    "Physics Department",
                    "Physics students and research discussions at Harvard",
                    harvard,
                    admin
            );

            communityRepository.save(csHarvard);
            communityRepository.save(mathMit);
            communityRepository.save(physicsHarvard);

            System.out.println("Sample communities initialized: 3 communities added");
        } else {
            System.out.println("Communities already exist in database. Count: " + communityRepository.count());
        }
    }
}