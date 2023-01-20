package pro.sky.shelter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pro.sky.shelter.core.entity.AnimalEntity;
import pro.sky.shelter.core.record.AnimalRecord;
import pro.sky.shelter.service.AnimalService;

import java.util.Collection;

/**
 * Контроллер животных
 *
 * @autor Shikunov Andrey
 */
@RestController
@RequestMapping("/animal")
public class AnimalController {
    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    /**
     * Вывод всех животных из БД
     */
    @Operation(
            summary = "Вывод всех животных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вывод всех животных",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = AnimalEntity.class))
                            )
                    )
            }
    )
    @GetMapping
    public Collection<AnimalRecord> getAllAnimals() {
        return animalService.getAllAnimal();
    }


    /**
     * Поиск животного по его id
     */
    @Operation(
            summary = "Поиск животного по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Поиск животного по id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AnimalEntity.class)
                            )
                    )
            }
    )
    @GetMapping("{id}")
    public AnimalRecord findAnimal(@Parameter(description = "Введите id животного", example = "1")
                             @PathVariable Long id) {
        return animalService.findAnimal(id);
    }

    /**
     * Изменение записи животного в БД
     */
    @Operation(
            summary = "Изменение животного",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Изменение животного",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AnimalEntity.class)
                            )
                    )
            }
    )
    @PutMapping("{id}")
    public AnimalRecord putUser(@Parameter(description = "Введите id животного", example = "1")
                                @PathVariable Long id,
                                @RequestBody @Valid AnimalRecord animalRecord) {
        return animalService.editAnimal(id, animalRecord);
    }

    /**
     * Добавление нового животного в БД
     */
    @Operation(
            summary = "Запись животного в БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Запись животного в БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AnimalEntity.class)
                            )
                    )
            }
    )
    @PostMapping
    public AnimalRecord postAnimal(@RequestBody @Valid AnimalRecord animalRecord) {
        return animalService.createAnimal(animalRecord);
    }

    /**
     * Удаление животного из БД
     */
    @Operation(
            summary = "Удаление животного",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удаление животного",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AnimalEntity.class)
                            )
                    )
            }
    )
    @DeleteMapping("{id}")
    public AnimalEntity deleteAnimal(@Parameter(description = "Введите id животного", example = "1")
                               @PathVariable Long id) {
        return animalService.deleteAnimal(id);
    }
}
