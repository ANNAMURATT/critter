package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired UserService userService;

    public Pet savePet(Pet pet){
        pet=petRepository.save(pet);
        userService.addPetToCustomer(pet,pet.getOwner());
        return pet;
    }

    public List<Pet> getAllPets(){
        return petRepository.findAll();
    }

    public List<Pet> getPetsByOwnerId(Long customerId){
        return petRepository.findByOwnerId(customerId);
    }

    public Pet findById(Long id){
        Optional<Pet> optionalPet = petRepository.findById(id);
        if (optionalPet.isPresent()){
            return optionalPet.get();
        }else {
            return null;
        }
    }
    public List<Pet> findAllByIds(List<Long> ids) {
        return petRepository.findAllById(ids);
    }
}
