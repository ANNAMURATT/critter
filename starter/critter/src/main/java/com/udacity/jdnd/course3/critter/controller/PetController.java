package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exceptions.NotFoundException;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    PetService petService;

    @Autowired
    UserService userService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {

        Pet pet = convertDTOToPet(petDTO);
        petService.savePet(pet);
        return convertPetToDTO(pet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.findById(petId);
        if (pet!=null)
        return convertPetToDTO(pet);
        throw new NotFoundException("No pet found with id: "+petId);
    }

    @GetMapping
    public List<PetDTO> getPets(){

        List<Pet> pets = petService.getAllPets();
        return convertPetListToDTOList(pets);
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> petList = petService.getPetsByOwnerId(ownerId);
        if (petList.size()>0)
            return convertPetListToDTOList(petList);
        throw new NotFoundException("Customer with id: "+ownerId+" has no pets");
    }

    /**
     * DTO converters only beyond this line
     *
     */

    private Pet convertDTOToPet(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        if (petDTO.getOwnerId() != 0L) {
            Customer customer = userService.findCustomerById(petDTO.getOwnerId());
            pet.setOwner(customer);
        }
        return pet;
    }

    private PetDTO convertPetToDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        if (pet.getOwner() != null) {
            petDTO.setOwnerId(pet.getOwner().getId());
        }
        return petDTO;
    }

    private List<PetDTO> convertPetListToDTOList(List<Pet> pets) {
        List<PetDTO> petDTOs = new ArrayList<>();
        for (Pet pet : pets) {
            petDTOs.add(convertPetToDTO(pet));
        }
        return petDTOs;
    }
}
