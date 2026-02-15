package local.ims.task1.song_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import local.ims.task1.song_service.dto.DeletedIdsDto;
import local.ims.task1.song_service.dto.ResourceIdDto;
import local.ims.task1.song_service.dto.SongDto;
import local.ims.task1.song_service.service.SongService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/songs")
@Validated
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @PostMapping
    public ResponseEntity<ResourceIdDto> createSong(@Valid @RequestBody SongDto songDto) {
        return new ResponseEntity<>(new ResourceIdDto(songService.createSong(songDto)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDto> getSongById(@PathVariable @Min(value = 1, message = "Must be a positive integer") Integer id) {
        SongDto songDto = songService.getSongById(id);
        return new ResponseEntity<>(songDto, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<DeletedIdsDto> deleteSongs(@RequestParam String id) {
        return new ResponseEntity<>(new DeletedIdsDto(songService.deleteSongs(id)), HttpStatus.OK);
    }
}

