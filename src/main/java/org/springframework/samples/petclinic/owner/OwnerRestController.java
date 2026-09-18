package org.springframework.samples.petclinic.owner;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * REST Controller for Owner operations. Provides endpoints for managing pet owners.
 *
 * @author Spring PetClinic Team
 */
@RestController
@RequestMapping("/api/owners")
public class OwnerRestController {

	private final OwnerRepository owners;

	public OwnerRestController(OwnerRepository owners) {
		this.owners = owners;
	}

	/**
	 * Get all owners with pagination.
	 * @param page the page number (default 1)
	 * @return Page of owners
	 */
	@GetMapping
	public ResponseEntity<Page<Owner>> getAllOwners(@RequestParam(defaultValue = "1") int page) {
		int pageSize = 10;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		Page<Owner> ownersPage = owners.findByLastNameStartingWith("", pageable);
		return ResponseEntity.ok(ownersPage);
	}

	/**
	 * Search owners by last name.
	 * @param lastName the last name to search for
	 * @param page the page number (default 1)
	 * @return Page of owners matching the search criteria
	 */
	@GetMapping("/search")
	public ResponseEntity<Page<Owner>> searchOwners(@RequestParam String lastName,
			@RequestParam(defaultValue = "1") int page) {
		int pageSize = 10;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		Page<Owner> ownersPage = owners.findByLastNameStartingWith(lastName, pageable);
		if (ownersPage.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(ownersPage);
	}

	/**
	 * Get owner by ID.
	 * @param ownerId the owner ID
	 * @return the owner with the given ID
	 */
	@GetMapping("/{ownerId}")
	public ResponseEntity<Owner> getOwner(@PathVariable("ownerId") Integer ownerId) {
		Optional<Owner> owner = owners.findById(ownerId);
		return owner.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	/**
	 * Create a new owner.
	 * @param owner the owner to create
	 * @return the created owner with HTTP 201
	 */
	@PostMapping
	public ResponseEntity<Owner> createOwner(@Valid @RequestBody Owner owner) {
		if (!owner.isNew()) {
			return ResponseEntity.badRequest().build();
		}
		Owner savedOwner = owners.save(owner);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedOwner);
	}

	/**
	 * Update an existing owner.
	 * @param ownerId the owner ID
	 * @param owner the updated owner data
	 * @return the updated owner
	 */
	@PutMapping("/{ownerId}")
	public ResponseEntity<Owner> updateOwner(@PathVariable("ownerId") Integer ownerId,
			@Valid @RequestBody Owner owner) {
		Optional<Owner> existingOwner = owners.findById(ownerId);
		if (existingOwner.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Owner ownerToUpdate = existingOwner.get();
		ownerToUpdate.setFirstName(owner.getFirstName());
		ownerToUpdate.setLastName(owner.getLastName());
		ownerToUpdate.setAddress(owner.getAddress());
		ownerToUpdate.setCity(owner.getCity());
		ownerToUpdate.setTelephone(owner.getTelephone());

		Owner updatedOwner = owners.save(ownerToUpdate);
		return ResponseEntity.ok(updatedOwner);
	}

	/**
	 * Delete an owner.
	 * @param ownerId the owner ID
	 * @return HTTP 204 No Content if successful
	 */
	@DeleteMapping("/{ownerId}")
	public ResponseEntity<Void> deleteOwner(@PathVariable("ownerId") Integer ownerId) {
		if (!owners.existsById(ownerId)) {
			return ResponseEntity.notFound().build();
		}
		owners.deleteById(ownerId);
		return ResponseEntity.noContent().build();
	}

}