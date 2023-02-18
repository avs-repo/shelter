package pro.sky.shelter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.shelter.core.record.AnimalRecord;
import pro.sky.shelter.service.AnimalService;
import pro.sky.shelter.service.ContentSaverService;

import java.util.Collection;

/**
 * Контроллер - работа с животными
 *
 * @autor Shikunov Andrey
 */
@RestController
@RequestMapping("/animal")
public class AnimalController {
    private final AnimalService animalService;
    private final ContentSaverService contentSaverService;

    public AnimalController(AnimalService animalService, ContentSaverService contentSaverService) {
        this.animalService = animalService;
        this.contentSaverService = contentSaverService;
    }

    @Operation(
            summary = "Вывод всех животных из БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вывод всех животных из БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = AnimalRecord.class))
                            )
                    )
            }
    )
    @GetMapping
    public Collection<AnimalRecord> getAllAnimals() {
        return animalService.getAllAnimal();
    }

    @Operation(
            summary = "Поиск животного по его id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Поиск животного по его id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AnimalRecord.class)
                            )
                    )
            }
    )
    @GetMapping("{id}")
    public AnimalRecord findAnimal(@Parameter(description = "Введите id животного", example = "1")
                                   @PathVariable Long id) {
        return animalService.findAnimal(id);
    }

    @Operation(
            summary = "Просмотр фотографии из БД по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Просмотр фотографии из БД по id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ContentSaverService.class)
                            )
                    )
            }
    )
    @GetMapping("/photo/{id}")
    public ResponseEntity<byte[]> getPhoto(@Parameter(description = "Введите id фотографии", example = "1")
                                           @PathVariable long id) {
        Pair<String, byte[]> pair = contentSaverService.getPhoto(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(pair.getFirst()))
                .contentLength(pair.getSecond().length)
                .body(pair.getSecond());
    }

    @Operation(
            summary = "Изменение записи животного в БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Изменение записи животного в БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AnimalRecord.class)
                            )
                    )
            }
    )
    @PutMapping("{id}")
    public AnimalRecord editAnimal(@Parameter(description = "Введите id животного", example = "1")
                                   @PathVariable Long id,
                                   @RequestBody @Valid AnimalRecord animalRecord) {
        return animalService.editAnimal(id, animalRecord);
    }

    @Operation(
            summary = "Добавление нового животного в БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Добавление нового животного в БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AnimalRecord.class)
                            )
                    )
            }
    )
    @PostMapping
    public AnimalRecord createAnimal(@RequestBody @Valid AnimalRecord animalRecord) {
        return animalService.createAnimal(animalRecord);
    }

    @Operation(
            summary = "Удаление животного из БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удаление животного из БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AnimalRecord.class)
                            )
                    )
            }
    )
    @DeleteMapping("{id}")
    public AnimalRecord deleteAnimal(@Parameter(description = "Введите id животного", example = "1")
                                     @PathVariable Long id) {
        return animalService.deleteAnimal(id);
    }
}
