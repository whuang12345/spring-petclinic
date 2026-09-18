/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * REST Controller for Pet operations. Provides endpoints for managing pets.
 *
 * @author Spring PetClinic Team
 */
@RestController
@RequestMapping("/api")
public class PetRestController {

	private final OwnerRepository owners;

	private final PetRepository pets;

	private final PetTypeRepository petTypes;

	public PetRestController(OwnerRepository owners, PetRepository pets, PetTypeRepository petTypes) {
		this.owners = owners;
		this.pets = pets;
		this.petTypes = petTypes;
	}

	/**
	 * Get all pets for a specific owner.
	 * @param ownerId the owner ID
	 * @return list of pets for the owner
	 */
	@GetMapping("/owners/{ownerId}/pets")
	public ResponseEntity<List<Pet>> getPetsByOwner(@PathVariable("ownerId") Integer ownerId) {
		Optional<Owner> owner = owners.findById(ownerId);
		if (owner.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(owner.get().getPets());
	}

	/**
	 * Get a specific pet.
	 * @param petId the pet ID
	 * @return the pet
	 */
	@GetMapping("/pets/{petId}")
	public ResponseEntity<Pet> getPet(@PathVariable("petId") Integer petId) {
		Optional<Pet> pet = pets.findById(petId);
		return pet.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	/**
	 * Get a specific pet for an owner.
	 * @param ownerId the owner ID
	 * @param petId the pet ID
	 * @return the pet
	 */
	@GetMapping("/owners/{ownerId}/pets/{petId}")
	public ResponseEntity<Pet> getOwnerPet(@PathVariable("ownerId") Integer ownerId,
			@PathVariable("petId") Integer petId) {
		Optional<Owner> owner = owners.findById(ownerId);
		if (owner.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Pet pet = owner.get().getPet(petId);
		if (pet == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(pet);
	}

	/**
	 * Create a new pet for an owner.
	 * @param ownerId the owner ID
	 * @param pet the pet to create
	 * @return the created pet with HTTP 201
	 */
	@PostMapping("/owners/{ownerId}/pets")
	public ResponseEntity<Pet> createPet(@PathVariable("ownerId") Integer ownerId, @Valid @RequestBody Pet pet) {
		Optional<Owner> owner = owners.findById(ownerId);
		if (owner.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		// Validate pet name doesn't already exist for this owner
		if (owner.get().getPet(pet.getName(), true) != null) {
			return ResponseEntity.badRequest().build();
		}

		// Validate birth date is not in the future
		LocalDate currentDate = LocalDate.now();
		if (pet.getBirthDate() != null && pet.getBirthDate().isAfter(currentDate)) {
			return ResponseEntity.badRequest().build();
		}

		owner.get().addPet(pet);
		owners.save(owner.get());
		return ResponseEntity.status(HttpStatus.CREATED).body(pet);
	}

	/**
	 * Update an existing pet.
	 * @param ownerId the owner ID
	 * @param petId the pet ID
	 * @param petData the updated pet data
	 * @return the updated pet
	 */
	@PutMapping("/owners/{ownerId}/pets/{petId}")
	public ResponseEntity<Pet> updatePet(@PathVariable("ownerId") Integer ownerId, @PathVariable("petId") Integer petId,
			@Valid @RequestBody Pet petData) {
		Optional<Owner> owner = owners.findById(ownerId);
		if (owner.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Pet pet = owner.get().getPet(petId);
		if (pet == null) {
			return ResponseEntity.notFound().build();
		}

		// Validate pet name doesn't already exist for another pet
		if (petData.getName() != null) {
			Pet existingPet = owner.get().getPet(petData.getName(), false);
			if (existingPet != null && !existingPet.getId().equals(petId)) {
				return ResponseEntity.badRequest().build();
			}
		}

		// Validate birth date is not in the future
		LocalDate currentDate = LocalDate.now();
		if (petData.getBirthDate() != null && petData.getBirthDate().isAfter(currentDate)) {
			return ResponseEntity.badRequest().build();
		}

		pet.setName(petData.getName());
		pet.setBirthDate(petData.getBirthDate());
		pet.setType(petData.getType());

		owners.save(owner.get());
		return ResponseEntity.ok(pet);
	}

	/**
	 * Delete a pet.
	 * @param ownerId the owner ID
	 * @param petId the pet ID
	 * @return HTTP 204 No Content if successful
	 */
	@DeleteMapping("/owners/{ownerId}/pets/{petId}")
	public ResponseEntity<Void> deletePet(@PathVariable("ownerId") Integer ownerId,
			@PathVariable("petId") Integer petId) {
		Optional<Owner> owner = owners.findById(ownerId);
		if (owner.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Pet pet = owner.get().getPet(petId);
		if (pet == null) {
			return ResponseEntity.notFound().build();
		}

		owner.get().getPets().remove(pet);
		owners.save(owner.get());
		return ResponseEntity.noContent().build();
	}

}
