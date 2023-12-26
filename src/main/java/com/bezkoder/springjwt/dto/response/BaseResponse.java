package com.bezkoder.springjwt.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BaseResponse<T> {
  private boolean success;
  private String message;
  private T data;
  
  public BaseResponse(boolean success, String message, T data) {
    this.success = success;
    this.message = message;
    this.data = data;
  }

  public static <T> BaseResponse<T> success(T data) {
    return new BaseResponse<>(true, null, data);
  }

  public static <T> BaseResponse<T> failure(String message) {
    return new BaseResponse<>(false, message, null);
  }

  public static <T> BaseResponse<T> noContent(boolean success, String message) {
    return new BaseResponse<>(success, message, null);
  }
}
