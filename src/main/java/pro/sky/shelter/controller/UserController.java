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
import pro.sky.shelter.core.record.ReportRecord;
import pro.sky.shelter.core.record.UserRecord;
import pro.sky.shelter.service.UserService;

import java.util.Collection;
import java.util.List;

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
                                    array = @ArraySchema(schema = @Schema(implementation = UserRecord.class))
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
                                    schema = @Schema(implementation = UserRecord.class)
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public UserRecord findUserById(@Parameter(description = "Введите id пользователя", example = "1")
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
                                    array = @ArraySchema(schema = @Schema(implementation = ReportRecord.class))
                            )
                    )
            }
    )
    @GetMapping("{id}/reports")
    public Collection<ReportRecord> findReportsByUser(@Parameter(description = "Введите id пользователя", example = "1")
                                                      @PathVariable Long id) {
        return userService.findReportsByUser(id);
    }

    @Operation(
            summary = "Отправка сообщений пользователю",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отправка сообщений пользователю",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserRecord.class)
                            )
                    )
            }
    )
    @GetMapping("{id}/message")
    public String sendMessageToUser(@Parameter(description = "Введите id пользователя", example = "1")
                                    @PathVariable Long id,
                                    @Parameter(description = "Введите сообщение для пользователя", example = "Вы плохо заполняете отчет.")
                                    @RequestParam("text") String text) {
        userService.sendMessage(id, "Сообщение от волонтера: " + text);
        return "Сообщение пользователю отправлено";
    }

    @Operation(
            summary = "Запись пользователя в БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Запись пользователя в БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserRecord.class)
                            )
                    )
            }
    )
    @PostMapping
    public UserRecord createUser(@RequestBody @Valid UserRecord userRecord) {
        return userService.createUser(userRecord);
    }

    @Operation(
            summary = "Привязка животного к пользователю по их id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Привязка животного к пользователю по их id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserRecord.class)
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
            summary = "Увеличение испытательного срока пользователю",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Увеличение испытательного срока пользователю",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserRecord.class)
                            )
                    )
            }
    )
    @PatchMapping("{id}/period")
    public UserRecord extendPeriod(@Parameter(description = "Введите id пользователя", example = "1")
                                   @PathVariable Long id,
                                   @Parameter(description = "Введите на сколько дней увеличить испытательный период", example = "14")
                                   @RequestParam("number") Integer number) {
        return userService.extendPeriod(id, number);
    }

    @Operation(
            summary = "Удаление пользователя из БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удаление пользователя из БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserRecord.class)
                            )
                    )
            }
    )

    @DeleteMapping("{id}")
    public UserRecord deleteUser(@Parameter(description = "Введите id пользователя", example = "1")
                                 @PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
