package local.ims.task1.resource_service.controllers;

import jakarta.validation.constraints.Min;
import local.ims.task1.resource_service.dto.DeletedIdsDto;
import local.ims.task1.resource_service.dto.ResourceIdDto;
import local.ims.task1.resource_service.services.Mp3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
public class Mp3Controller {
    private final Mp3Service mp3Service;


    @PostMapping(value = "/resources", consumes = "audio/mpeg", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResourceIdDto> uploadMp3(@RequestBody byte[] mp3Data) {
        ResourceIdDto result = new ResourceIdDto(mp3Service.saveMp3(mp3Data));
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/resources/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getMp3(@PathVariable("id") @Min(value = 1, message = "Must be a positive integer") int id) {
        byte[] mp3Data = mp3Service.getMp3ById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("audio/mpeg"))
                .body(mp3Data);
    }

    @DeleteMapping("/resources")
    public ResponseEntity<DeletedIdsDto> deleteResources(
            @RequestParam("id")
            String idsCsv
    ) {
        List<Integer> deletedIds = mp3Service.deleteResourcesWithCascading(idsCsv);
        return ResponseEntity.ok(new DeletedIdsDto(deletedIds));
    }

}
