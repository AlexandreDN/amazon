package com.esme.amazon.application.rappels.application;

import com.esme.amazon.application.rappels.Domain.Amazon;
import com.esme.amazon.application.rappels.Domain.AmazonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class Controller {

    private AmazonService amazonService;
    private ObjectMapper objectMapper;

    public Controller(AmazonService amazonService, ObjectMapper objectMapper) {
        this.amazonService = amazonService;
        this.objectMapper = objectMapper;
    }


    @ApiOperation(value = "View a list of available products", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @RequestMapping(value = "/amazons", method = RequestMethod.GET)
    public ResponseEntity<List<Amazon>> getAmazons() {
        return new ResponseEntity<>(amazonService.getAmazons(), HttpStatus.OK);
    }

    @RequestMapping(value = "/amazons/{id}", method = RequestMethod.GET)
    public ResponseEntity<Amazon> getAmazonsById( @PathVariable(value = "id") Long id) {
        try {
            return new ResponseEntity<>(amazonService.getAmazons(id), HttpStatus.OK);
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found", e);
        }
    }

    @RequestMapping(value = "/amazons", method = RequestMethod.POST)
    public ResponseEntity<Amazon> createAmazons(
            @ApiParam(value = "Product object store in database table", required = true)
            @RequestBody Amazon amazon) {
        amazonService.addAmazon(amazon);
        return new ResponseEntity<>(amazon, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/amazons/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Amazon> replaceAmazons(
            @PathVariable(value = "id") Long id,
            @RequestBody Amazon amazon) {
        amazon.setId(id);
        amazonService.replaceAmazon(amazon);
        return new ResponseEntity<>(amazon, HttpStatus.OK);
    }

    @RequestMapping(value = "/amazons/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Amazon> deleteAmazons(@PathVariable(value = "id") Long id) {
        amazonService.deleteAmazons(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/amazons/{id}", method = RequestMethod.PATCH, consumes = "com/esme/amazon/application/rappels/com.esme.amazon.application.rappels.application/json-patch+json")
    public ResponseEntity<String> patchAmazons(
            @PathVariable(value = "id") Long id,
            @RequestBody JsonPatch patch)  {
        try {
            amazonService.patchAmazons(applyPatchToCustomer(patch, amazonService.findAmazon(id)));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (JsonPatchException | JsonProcessingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Amazon applyPatchToCustomer(JsonPatch patch, Amazon targetAmazon)
            throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(targetAmazon, JsonNode.class));
        return objectMapper.treeToValue(patched, Amazon.class);
    }
}

