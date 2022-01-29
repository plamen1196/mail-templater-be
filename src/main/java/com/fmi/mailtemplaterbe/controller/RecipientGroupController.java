package com.fmi.mailtemplaterbe.controller;

import com.fmi.mailtemplaterbe.domain.resource.RecipientGroupResource;
import com.fmi.mailtemplaterbe.domain.resource.RecipientResource;
import com.fmi.mailtemplaterbe.service.RecipientGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
public class RecipientGroupController {

    private final RecipientGroupService recipientGroupService;

    @PostMapping(
            value = "/recipient-groups",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipientGroupResource> createRecipientGroup(
            @Valid @RequestBody RecipientGroupResource recipientGroupResource) {
        return ResponseEntity.ok(recipientGroupService.createRecipientGroup(recipientGroupResource));
    }

    @GetMapping(
            value = "/recipient-groups",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecipientGroupResource>> getRecipientGroups() {
        return ResponseEntity.ok(recipientGroupService.getAllRecipientGroups());
    }

    @PatchMapping(
            value = "/recipient-groups/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipientGroupResource> updateRecipientGroup(
            @PathVariable(value = "id") Long id, @RequestBody RecipientGroupResource recipientGroupResource) {
        return ResponseEntity.ok(recipientGroupService.updateRecipientGroupById(id, recipientGroupResource));
    }

    @DeleteMapping(
            value = "/recipient-groups/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipientGroupResource> deleteRecipientGroup(@PathVariable(value = "id") Long id) {
        recipientGroupService.deleteRecipientGroupById(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping(
            value = "/recipient-groups/{id}/recipients",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecipientResource>> getRecipientsOfRecipientGroupById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(recipientGroupService.getRecipientsOfRecipientGroupById(id));
    }

    @PostMapping(
            value = "/recipient-groups/{recipientGroupId}/recipients/{recipientId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecipientResource>> addRecipientToRecipientGroup(
            @PathVariable(value = "recipientGroupId") Long recipientGroupId,
            @PathVariable(value = "recipientId") Long recipientId) {
        return ResponseEntity.ok(recipientGroupService.addRecipientToRecipientGroup(recipientGroupId, recipientId));
    }

    @DeleteMapping(
            value = "/recipient-groups/{recipientGroupId}/recipients/{recipientId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecipientResource>> removeRecipientFromRecipientGroup(
            @PathVariable(value = "recipientGroupId") Long recipientGroupId,
            @PathVariable(value = "recipientId") Long recipientId) {
        return ResponseEntity.ok(recipientGroupService.removeRecipientFromRecipientGroup(recipientGroupId, recipientId));
    }
}
