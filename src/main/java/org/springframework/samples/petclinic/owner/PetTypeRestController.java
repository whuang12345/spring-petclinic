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
 * REST Controller for PetType operations. Provides endpoints for managing pet types.
 *
 * @author Spring PetClinic Team
 */
@RestController
@RequestMapping("/api/pet-types")
public class PetTypeRestController {

	private final PetTypeRepository petTypes;

	public PetTypeRestController(PetTypeRepository petTypes) {
		this.petTypes = petTypes;
	}

	/**
	 * Get all pet types.
	 * @return list of all pet types
	 */
	@GetMapping
	public ResponseEntity<List<PetType>> getAllPetTypes() {
		List<PetType> petTypesList = petTypes.findPetTypes();
		return ResponseEntity.ok(petTypesList);
	}

	/**
	 * Get a specific pet type by ID.
	 * @param petTypeId the pet type ID
	 * @return the pet type
	 */
	@GetMapping("/{petTypeId}")
	public ResponseEntity<PetType> getPetType(@PathVariable("petTypeId") Integer petTypeId) {
		Optional<PetType> petType = petTypes.findById(petTypeId);
		return petType.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	/**
	 * Create a new pet type.
	 * @param petType the pet type to create
	 * @return the created pet type with HTTP 201
	 */
	@PostMapping
	public ResponseEntity<PetType> createPetType(@Valid @RequestBody PetType petType) {
		if (!petType.isNew()) {
			return ResponseEntity.badRequest().build();
		}
		PetType savedPetType = petTypes.save(petType);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedPetType);
	}

	/**
	 * Update an existing pet type.
	 * @param petTypeId the pet type ID
	 * @param petTypeData the updated pet type data
	 * @return the updated pet type
	 */
	@PutMapping("/{petTypeId}")
	public ResponseEntity<PetType> updatePetType(@PathVariable("petTypeId") Integer petTypeId,
			@Valid @RequestBody PetType petTypeData) {
		Optional<PetType> existingPetType = petTypes.findById(petTypeId);
		if (existingPetType.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		PetType petType = existingPetType.get();
		petType.setName(petTypeData.getName());

		PetType updatedPetType = petTypes.save(petType);
		return ResponseEntity.ok(updatedPetType);
	}

	/**
	 * Delete a pet type.
	 * @param petTypeId the pet type ID
	 * @return HTTP 204 No Content if successful
	 */
	@DeleteMapping("/{petTypeId}")
	public ResponseEntity<Void> deletePetType(@PathVariable("petTypeId") Integer petTypeId) {
		if (!petTypes.existsById(petTypeId)) {
			return ResponseEntity.notFound().build();
		}
		petTypes.deleteById(petTypeId);
		return ResponseEntity.noContent().build();
	}

}
