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
package org.springframework.samples.petclinic.vet;

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
 * REST Controller for Specialty operations. Provides endpoints for managing veterinary
 * specialties.
 *
 * @author Spring PetClinic Team
 */
@RestController
@RequestMapping("/api/specialties")
public class SpecialtyRestController {

	private final SpecialtyRepository specialties;

	public SpecialtyRestController(SpecialtyRepository specialties) {
		this.specialties = specialties;
	}

	/**
	 * Get all specialties.
	 * @return list of all specialties
	 */
	@GetMapping
	public ResponseEntity<List<Specialty>> getAllSpecialties() {
		List<Specialty> specialtiesList = specialties.findSpecialties();
		return ResponseEntity.ok(specialtiesList);
	}

	/**
	 * Get a specific specialty by ID.
	 * @param specialtyId the specialty ID
	 * @return the specialty
	 */
	@GetMapping("/{specialtyId}")
	public ResponseEntity<Specialty> getSpecialty(@PathVariable("specialtyId") Integer specialtyId) {
		Optional<Specialty> specialty = specialties.findById(specialtyId);
		return specialty.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	/**
	 * Create a new specialty.
	 * @param specialty the specialty to create
	 * @return the created specialty with HTTP 201
	 */
	@PostMapping
	public ResponseEntity<Specialty> createSpecialty(@Valid @RequestBody Specialty specialty) {
		if (!specialty.isNew()) {
			return ResponseEntity.badRequest().build();
		}
		Specialty savedSpecialty = specialties.save(specialty);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedSpecialty);
	}

	/**
	 * Update an existing specialty.
	 * @param specialtyId the specialty ID
	 * @param specialtyData the updated specialty data
	 * @return the updated specialty
	 */
	@PutMapping("/{specialtyId}")
	public ResponseEntity<Specialty> updateSpecialty(@PathVariable("specialtyId") Integer specialtyId,
			@Valid @RequestBody Specialty specialtyData) {
		Optional<Specialty> existingSpecialty = specialties.findById(specialtyId);
		if (existingSpecialty.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Specialty specialty = existingSpecialty.get();
		specialty.setName(specialtyData.getName());

		Specialty updatedSpecialty = specialties.save(specialty);
		return ResponseEntity.ok(updatedSpecialty);
	}

	/**
	 * Delete a specialty.
	 * @param specialtyId the specialty ID
	 * @return HTTP 204 No Content if successful
	 */
	@DeleteMapping("/{specialtyId}")
	public ResponseEntity<Void> deleteSpecialty(@PathVariable("specialtyId") Integer specialtyId) {
		if (!specialties.existsById(specialtyId)) {
			return ResponseEntity.notFound().build();
		}
		specialties.deleteById(specialtyId);
		return ResponseEntity.noContent().build();
	}

}
