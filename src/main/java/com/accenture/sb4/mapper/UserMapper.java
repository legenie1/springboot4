package com.accenture.sb4.mapper;

import com.accenture.sb4.dto.UserDTOv1;
import com.accenture.sb4.dto.UserDTOv2;
import com.accenture.sb4.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTOv1 toV1(User user){
        return new UserDTOv1(user.id(), user.name(), user.email());
    }

    public UserDTOv2 toV2(User user){
        String[] nameParts = user.name().split(" ", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        return new UserDTOv2(user.id(), firstName, lastName, user.email());
    }
}
