package cmc.mellyserver.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonDetailResponse<T> {

    public T data;
}
