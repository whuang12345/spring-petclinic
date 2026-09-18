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

import java.util.Collection;
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
 * REST Controller for Visit operations. Provides endpoints for managing pet visits.
 *
 * @author Spring PetClinic Team
 */
@RestController
@RequestMapping("/api/owners/{ownerId}/pets/{petId}/visits")
public class VisitRestController {

	private final OwnerRepository owners;

	public VisitRestController(OwnerRepository owners) {
		this.owners = owners;
	}

	/**
	 * Get all visits for a specific pet.
	 * @param ownerId the owner ID
	 * @param petId the pet ID
	 * @return collection of visits for the pet
	 */
	@GetMapping
	public ResponseEntity<Collection<Visit>> getVisits(@PathVariable("ownerId") Integer ownerId,
			@PathVariable("petId") Integer petId) {
		Optional<Owner> owner = owners.findById(ownerId);
		if (owner.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Pet pet = owner.get().getPet(petId);
		if (pet == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(pet.getVisits());
	}

	/**
	 * Get a specific visit.
	 * @param ownerId the owner ID
	 * @param petId the pet ID
	 * @param visitId the visit ID
	 * @return the visit
	 */
	@GetMapping("/{visitId}")
	public ResponseEntity<Visit> getVisit(@PathVariable("ownerId") Integer ownerId,
			@PathVariable("petId") Integer petId, @PathVariable("visitId") Integer visitId) {
		Optional<Owner> owner = owners.findById(ownerId);
		if (owner.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Pet pet = owner.get().getPet(petId);
		if (pet == null) {
			return ResponseEntity.notFound().build();
		}

		Visit visit = pet.getVisits().stream().filter(v -> v.getId().equals(visitId)).findFirst().orElse(null);

		if (visit == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(visit);
	}

	/**
	 * Create a new visit for a pet.
	 * @param ownerId the owner ID
	 * @param petId the pet ID
	 * @param visit the visit to create
	 * @return the created visit with HTTP 201
	 */
	@PostMapping
	public ResponseEntity<Visit> createVisit(@PathVariable("ownerId") Integer ownerId,
			@PathVariable("petId") Integer petId, @Valid @RequestBody Visit visit) {
		Optional<Owner> owner = owners.findById(ownerId);
		if (owner.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Pet pet = owner.get().getPet(petId);
		if (pet == null) {
			return ResponseEntity.notFound().build();
		}

		pet.addVisit(visit);
		owners.save(owner.get());
		return ResponseEntity.status(HttpStatus.CREATED).body(visit);
	}

	/**
	 * Update an existing visit.
	 * @param ownerId the owner ID
	 * @param petId the pet ID
	 * @param visitId the visit ID
	 * @param visitData the updated visit data
	 * @return the updated visit
	 */
	@PutMapping("/{visitId}")
	public ResponseEntity<Visit> updateVisit(@PathVariable("ownerId") Integer ownerId,
			@PathVariable("petId") Integer petId, @PathVariable("visitId") Integer visitId,
			@Valid @RequestBody Visit visitData) {
		Optional<Owner> owner = owners.findById(ownerId);
		if (owner.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Pet pet = owner.get().getPet(petId);
		if (pet == null) {
			return ResponseEntity.notFound().build();
		}

		Visit visit = pet.getVisits().stream().filter(v -> v.getId().equals(visitId)).findFirst().orElse(null);

		if (visit == null) {
			return ResponseEntity.notFound().build();
		}

		visit.setDate(visitData.getDate());
		visit.setDescription(visitData.getDescription());

		owners.save(owner.get());
		return ResponseEntity.ok(visit);
	}

	/**
	 * Delete a visit.
	 * @param ownerId the owner ID
	 * @param petId the pet ID
	 * @param visitId the visit ID
	 * @return HTTP 204 No Content if successful
	 */
	@DeleteMapping("/{visitId}")
	public ResponseEntity<Void> deleteVisit(@PathVariable("ownerId") Integer ownerId,
			@PathVariable("petId") Integer petId, @PathVariable("visitId") Integer visitId) {
		Optional<Owner> owner = owners.findById(ownerId);
		if (owner.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Pet pet = owner.get().getPet(petId);
		if (pet == null) {
			return ResponseEntity.notFound().build();
		}

		Visit visit = pet.getVisits().stream().filter(v -> v.getId().equals(visitId)).findFirst().orElse(null);

		if (visit == null) {
			return ResponseEntity.notFound().build();
		}

		pet.getVisits().remove(visit);
		owners.save(owner.get());
		return ResponseEntity.noContent().build();
	}

}
