package local.ims.task1.resource_service.services;

import local.ims.task1.resource_service.dto.DeletedIdsDto;
import local.ims.task1.resource_service.dto.SongMetadataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetadataService {

    @Value("${metadata.service.url}")
    private String metadataServiceUrl;

    private final RestTemplate restTemplate;


    public String createSongMetadata(SongMetadataDto metadata) {
        String url = metadataServiceUrl + "/songs";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SongMetadataDto> request = new HttpEntity<>(metadata, headers);

        try {
            restTemplate.postForEntity(
                    url,
                    request,
                    SongMetadataDto.class
            );
            log.info("Successfully created song metadata: {}", metadata);
            return StringUtils.EMPTY;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("Error creating song metadata: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            return ex.getResponseBodyAsString();
        }
    }

    public List<Integer> deleteSongMetadata(String ids) {
        String url = metadataServiceUrl + "/songs?id=" + ids;
        try {
            ResponseEntity<DeletedIdsDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    null,
                    DeletedIdsDto.class
            );

            DeletedIdsDto deletedIds = response.getBody();
            log.info("Successfully deleted song metadata with ids: {}", ids);
            if (deletedIds == null) {
                log.warn("Received null response body when deleting song metadata with ids: {}", ids);
                return List.of();
            }
            return deletedIds.ids();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("Error deleting song metadata: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            return List.of();
        }
    }

    public List<Integer> deleteSongMetadataByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        String idsCsv = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return deleteSongMetadata(idsCsv);
    }
}
