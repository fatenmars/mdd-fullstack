package com.orion.mddapi.dto;

import java.util.List;

public record UserProfileDto(String email, String username, List<ThemeDto> subscriptions) {

}
