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

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for Vet operations. Provides endpoints for managing veterinarians.
 *
 * @author Spring PetClinic Team
 */
@RestController
@RequestMapping("/api/vets")
public class VetRestController {

	private final VetRepository vets;

	public VetRestController(VetRepository vets) {
		this.vets = vets;
	}

	/**
	 * Get all vets with pagination.
	 * @param page the page number (default 1)
	 * @return Page of vets
	 */
	@GetMapping
	public ResponseEntity<Page<Vet>> getAllVets(@RequestParam(defaultValue = "1") int page) {
		int pageSize = 10;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		Page<Vet> vetsPage = vets.findAll(pageable);
		return ResponseEntity.ok(vetsPage);
	}

	/**
	 * Get all vets as a collection.
	 * @return collection of all vets
	 */
	@GetMapping("/all")
	public ResponseEntity<Collection<Vet>> getAllVetsAsList() {
		Collection<Vet> vetsList = vets.findAll();
		return ResponseEntity.ok(vetsList);
	}

}
