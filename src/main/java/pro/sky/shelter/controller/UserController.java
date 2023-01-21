package pro.sky.shelter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pro.sky.shelter.core.entity.ReportEntity;
import pro.sky.shelter.core.entity.UserEntity;
import pro.sky.shelter.core.record.ReportRecord;
import pro.sky.shelter.core.record.UserRecord;
import pro.sky.shelter.service.UserService;

import java.util.Collection;

/**
 * Контроллер - работа с пользователями
 *
 * @autor Shikunov Andrey
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Вывод всех пользователей из БД
     */
    @Operation(
            summary = "Вывод всех пользователей",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вывод всех пользователей",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = UserEntity.class))
                            )
                    )
            }
    )
    @GetMapping
    public Collection<UserRecord> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Поиск пользователя по id
     */
    @Operation(
            summary = "Поиск пользователя по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Поиск пользователя по id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserEntity.class)
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public UserRecord findUser(@Parameter(description = "Введите id пользователя", example = "1")
                               @PathVariable Long id) {
        return userService.findUserById(id);
    }

    /**
     * Поиск отчетов по пользователю
     */
    @Operation(
            summary = "Поиск всех отчетов пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Поиск всех отчетов пользователя",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ReportEntity.class))
                            )
                    )
            },
            tags = "Report"
    )
    @GetMapping("{id}/reports")
    public Collection<ReportRecord> findReportsByUser(@Parameter(description = "Введите id пользователя", example = "1")
                                                      @PathVariable Long id) {
        return userService.findReportsByUser(id);
    }

    @Operation(
            summary = "Привязка животного к пользователю по их id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Привязка животного к пользователю по их id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserEntity.class)
                            )
                    )
            }
    )
    @PatchMapping("/{id}/animal")
    public UserRecord patchUserAnimal(@Parameter(description = "Введите id пользователя", example = "1")
                                      @PathVariable Long id,
                                      @Parameter(description = "Введите id животного", example = "1")
                                      @RequestParam("animalId") Long animalId) {
        return userService.patchUserAnimal(id, animalId);
    }

    @Operation(
            summary = "Удаление пользователя из БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удаление пользователя из БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserEntity.class)
                            )
                    )
            }
    )
    @DeleteMapping("{id}")
    public UserEntity deleteUser(@Parameter(description = "Введите id пользователя", example = "1")
                                 @PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
